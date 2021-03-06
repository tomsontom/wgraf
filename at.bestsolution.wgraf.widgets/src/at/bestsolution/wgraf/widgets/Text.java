package at.bestsolution.wgraf.widgets;

import java.util.ArrayList;
import java.util.List;

import at.bestsolution.wgraf.Sync;
import at.bestsolution.wgraf.events.InputMethod;
import at.bestsolution.wgraf.events.KeyCode;
import at.bestsolution.wgraf.events.KeyEvent;
import at.bestsolution.wgraf.events.ScrollEvent;
import at.bestsolution.wgraf.events.ScrollLock;
import at.bestsolution.wgraf.events.TapEvent;
import at.bestsolution.wgraf.math.Vec2d;
import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.paint.LinearGradient;
import at.bestsolution.wgraf.paint.LinearGradient.CoordMode;
import at.bestsolution.wgraf.paint.LinearGradient.Spread;
import at.bestsolution.wgraf.paint.LinearGradient.Stop;
import at.bestsolution.wgraf.paint.Paint;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.ClampedDoubleIncrement;
import at.bestsolution.wgraf.properties.Converter;
import at.bestsolution.wgraf.properties.DoubleChangeListener;
import at.bestsolution.wgraf.properties.DoubleProperty;
import at.bestsolution.wgraf.properties.InvalidValueException;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.ReadOnlyProperty;
import at.bestsolution.wgraf.properties.Signal;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.properties.ValidState;
import at.bestsolution.wgraf.properties.ValidationProperty;
import at.bestsolution.wgraf.properties.binding.Binder;
import at.bestsolution.wgraf.properties.binding.GroupBinding;
import at.bestsolution.wgraf.properties.binding.Setter;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.properties.simple.SimpleSignal;
import at.bestsolution.wgraf.properties.simple.SimpleValidationProperty;
import at.bestsolution.wgraf.scene.Container;
import at.bestsolution.wgraf.scene.Image;
import at.bestsolution.wgraf.style.Background;
import at.bestsolution.wgraf.style.Border;
import at.bestsolution.wgraf.style.BorderStroke;
import at.bestsolution.wgraf.style.BorderWidths;
import at.bestsolution.wgraf.style.CornerRadii;
import at.bestsolution.wgraf.style.FillBackground;
import at.bestsolution.wgraf.style.Font;
import at.bestsolution.wgraf.style.ImageSource;
import at.bestsolution.wgraf.style.Insets;
import at.bestsolution.wgraf.transition.TouchScrollTransition;

// TODO add some kind of action menu - for copy, cut and paste
// TODO implement selection
// TODO filter keyboard events
// TODO we need some kind of delayed text change listener
//      to prevent model updates on every stroke
public class Text extends Widget {

	public static class TextButton extends Widget {

		private Image icon;
		protected at.bestsolution.wgraf.scene.Text tIcon;

		private Property<Boolean> active = new SimpleProperty<Boolean>(false);
		public Property<Boolean> active() {
			return active;
		}

		private Property<Boolean> right = new SimpleProperty<Boolean>(false);
		public Property<Boolean> right() {
			return right;
		}

		private Property<Boolean> left = new SimpleProperty<Boolean>(false);
		public Property<Boolean> left() {
			return left;
		}

		public Property<ImageSource> icon() {
			return icon.image();
		}

		private Signal<Void> activated = new SimpleSignal<Void>();
		public Signal<Void> activated() {
			return activated;
		}
		
		public Property<Paint> fill() {
			return tIcon.fill();
		}
		
		private void layoutIcon() {
			if (tIcon.font().get() != null && tIcon.text().get() != null) {
				Vec2d ex = tIcon.font().get().stringExtent(tIcon.text().get());
				tIcon.x().set(width().get()/2 - ex.x/2);
				tIcon.y().set(height().get()/2 - ex.y/2);
			}
		}

		public TextButton() {

			icon = new Image();
			icon.x().set(10);
			icon.y().set(10);
			icon.parent().set(area);

			tIcon = new at.bestsolution.wgraf.scene.Text();
			tIcon.parent().set(area);
			
			Binder.uniBind(tIcon.font(), new Setter<Font>() {
				@Override
				public void set(Font value) {
					layoutIcon();
				}
			});
			Binder.uniBind(tIcon.text(), new Setter<String>() {
				@Override
				public void set(String value) {
					layoutIcon();
				}
			});
			
			area.acceptTapEvents().set(true);
			area.onTap().registerSignalListener(new SignalListener<TapEvent>() {
				@Override
				public void onSignal(TapEvent data) {
					if (!enabled().get()) return;

					triggerActivated();
					data.consume();
				}
			});

			registerPseudoClassState("active", active);
			registerPseudoClassState("left", left);
			registerPseudoClassState("right", right);

			initDefaultStyle();
		}

		protected GroupBinding skinBinding = new GroupBinding();
		
		protected void reSkin() {
			skinBinding.dispose();
			final boolean active = active().get();
			final boolean disabled = !enabled().get();
			System.err.println("reskin: active=" + active + " disabled=" + disabled);
			if (disabled) {
				area.background().set(null);
				skinBinding.registerBindings(
					Binder.uniBind(Skin.BUTTON_TEXT_DISABLED, tIcon.fill())
				);
				
			}
			else if (active) {
				skinBinding.registerBindings(
					Binder.uniBind(Skin.HIGHLIGHT, tIcon.fill()),
					Binder.uniBind(Skin.HIGHLIGHT_30, new Setter<Paint>() {
						@Override
						public void set(Paint value) {
							System.err.println("using paint " + value);
							final Background focusRight = new FillBackground(value,new CornerRadii(0, 0, 4, 4, 0, 0, 4, 4), new Insets(1, 2, 2, 0));
							final Background focusLeft = new FillBackground(value,new CornerRadii(4, 4, 0, 0, 4, 4, 0, 0), new Insets(1, 0, 2, 1));
							final Background focusMiddle = new FillBackground(value,new CornerRadii(0), new Insets(1, 0, 2, 0));
							
							final Background bg = left.get() ? focusLeft : right.get() ? focusRight : focusMiddle;
							System.err.println("SETTING BG TO " + bg);
							TextButton.this.area.background().set(bg);
						}
					})	
				);
			}
			else {
				area.background().set(null);
				skinBinding.registerBindings(
					Binder.uniBind(Skin.BUTTON_TEXT_ACTIVE, tIcon.fill())
				);
			}
			
		}
		
		protected void initDefaultStyle() {
			Binder.uniBind(TextButton.this.active, new Setter<Boolean>() {
				@Override
				public void set(Boolean value) {
					reSkin();
				}
			});
			Binder.uniBind(TextButton.this.enabled(), new Setter<Boolean>() {
				@Override
				public void set(Boolean value) {
					reSkin();
				}
			});
			
//			LinearGradient focusGradient = new LinearGradient(0, 0, 1, 1, CoordMode.OBJECT_BOUNDING, Spread.PAD,
//					new Stop(0, new Color(225,0,0,150)),
//					new Stop(0.4, new Color(255,30,30,150)),
//					new Stop(1, new Color(255,30,30,150))
//					);
//			final Background focusRight = new FillBackground(
//					focusGradient,
//					new CornerRadii(0, 0, 4, 4, 0, 0, 4, 4), new Insets(1, 2, 2, 0)
//					);
//			final Background focusLeft = new FillBackground(
//					focusGradient,
//					new CornerRadii(4, 4, 0, 0, 4, 4, 0, 0), new Insets(1, 0, 2, 1)
//					);
//			final Background focusMiddle = new FillBackground(
//					focusGradient,
//					new CornerRadii(0), new Insets(1, 0, 2, 0)
//					);
//
//			active.registerChangeListener(new ChangeListener<Boolean>() {
//				@Override
//				public void onChange(Boolean oldValue, Boolean newValue) {
//					if (newValue) {
//						Background focus = left.get() ? focusLeft :
//							right.get() ? focusRight : focusMiddle;
//						area.background().set(focus);
//					}
//					else {
//						area.background().set(null);
//					}
//				}
//			});

		}

		public void triggerActivated() {
			active.set(true);
			Sync.get().execLaterOnUIThread(new Runnable() {
				@Override
				public void run() {
					active.set(false);
					activated.signal(null);
				}
			}, 100);
		}

		public Property<Font> font() {
			return tIcon.font();
		}
		
		public Property<String> text() {
			return tIcon.text();
		}

	}

	private List<TextButton> leftButtons = new ArrayList<TextButton>();
	private List<TextButton> rightButtons = new ArrayList<TextButton>();

	public static enum ButtonPosition {
		LEFT, RIGHT;
	}

	public TextButton addButton(ButtonPosition pos) {
		return addButton(pos, new Converter<Void, Text.TextButton>() {
			@Override
			public TextButton convert(Void value) {
				return new TextButton();
			}
		});
	}
	
	public TextButton addButton(ButtonPosition pos, boolean expandOverText) {
		return addButton(pos, expandOverText, new Converter<Void, Text.TextButton>() {
			@Override
			public TextButton convert(Void value) {
				return new TextButton();
			}
		});
	}
	
	public TextButton addButton(ButtonPosition pos, Converter<Void, TextButton> factory) {
		return addButton(pos, false, factory);
	}

	public TextButton addButton(ButtonPosition pos, boolean expandOverText, Converter<Void, TextButton> factory) {
		if (expandOverText) {
			if (expandButton != null) {
				throw new IllegalStateException("only one button may expand over the text!");
			}
		}
		TextButton btn = factory.convert(null);
		if (expandOverText) {
			expandButton = btn;
		}
		if (pos == ButtonPosition.LEFT) {
			leftButtons.add(btn);
			// fix left
			for (int i = 0; i < leftButtons.size(); i++) {
				TextButton iBtn = leftButtons.get(i);
				if (i == 0) {
					System.err.println("L: setting " + i + " to left");
					iBtn.left().set(true);
				}
				else {
					iBtn.left().set(false);
				}
			}
		}
		else {
			rightButtons.add(btn);
			// fix right
			for (int i = 0; i < rightButtons.size(); i++) {
				TextButton iBtn = rightButtons.get(i);
				if (i == rightButtons.size() - 1) {
					System.err.println("R: setting " + i + " to right");
					iBtn.right().set(true);
				}
				else {
					iBtn.right().set(false);
				}
			}
		}
		btn.area.parent().set(area);
		resize();
		return btn;
	}

	private TextButton expandButton = null;

	private static final String BACKSPACE = "\u0008";
	private static final String DELETE = "\u007F";
	private static final String ESCAPE = "\u001B";

	protected final at.bestsolution.wgraf.scene.Text nodeText;
	protected final Container nodeCursor;

	private final Property<Integer> cursorIndex = new SimpleProperty<Integer>(0);

	private static enum Cmp {
		LESSER,
		GREATER,
		EQUAL
	}

	private static class Letter {
		public int index;
		public String letter;
		public double beginX;
		public double endX;
		public Cmp cmp;
	}

	private static Letter getLetter(Font font, String text, int index) {
		if (text.length() == 0) {
			Letter l = new Letter();
			l.index = 0;
			l.beginX = 0;
			l.endX = 0;
			l.cmp = Cmp.EQUAL;
			return l;
		}

		final Letter result = new Letter();
		result.index = index;

		final String str0 = text.substring(0, index);
		final String str1 = text.substring(0, Math.min(text.length(), index+1));

		result.beginX = font.stringExtent(str0).x;
		result.endX = font.stringExtent(str1).x;
		result.cmp = Cmp.EQUAL;
		return result;
	}

	private static Letter compare(Font font, String text, int letterIdx, double xPos) {
		final Letter result = new Letter();
		result.index = letterIdx;

		final String str0 = text.substring(0, letterIdx);
		final String str1 = text.substring(0, Math.min(text.length(), letterIdx+1));

		result.beginX = font.stringExtent(str0).x;
		result.endX = font.stringExtent(str1).x;

		// ensure that xPos is never negative
		xPos = Math.max(0, xPos);

//		System.err.println("xpos = " + xPos);
//		System.err.println("str0: " + str0 + " -> " + result.beginX);
//		System.err.println("str1: " + str1 + " -> " + result.endX);

		if (xPos < result.beginX) {
			result.cmp = Cmp.LESSER;
			return result;
		}
		else if (xPos > result.endX && text.length() > letterIdx + 1) {
			result.cmp = Cmp.GREATER;
			return result;
		}
		else {
			result.letter = text.substring(letterIdx, letterIdx + 1);
			result.cmp = Cmp.EQUAL;
			return result;
		}

	}

	private static Letter binSearch(Font font, String text, int firstIdx, int lastIdx, double xPos) {

		if (text.length() == 0) {
			Letter l = new Letter();
			l.index = 0;
			l.beginX = 0;
			l.endX = 0;
			l.cmp = Cmp.EQUAL;
			return l;
		}

		int curIdx = firstIdx + (lastIdx-firstIdx)/2;
		Letter r = compare(font, text, curIdx, xPos);
		switch (r.cmp) {
		case EQUAL:
			return r;
		case LESSER:
			return binSearch(font, text, firstIdx, curIdx, xPos);
		case GREATER:
			return binSearch(font, text, curIdx, lastIdx, xPos);
		}

		// ERROR?
		return null;
	}

	private void resize() {
		double width = width().get();
		double widthLeftButtons = 40 * leftButtons.size();
		double widthRightButtons = 40 * rightButtons.size();

		double widthText = width - widthLeftButtons - widthRightButtons;

		double leftSpace = leftButtons.size() == 0 ? 10 : 0;
		double rightSpace = rightButtons.size() == 0 ? 10 : 0;

		textClip.x().set(widthLeftButtons + leftSpace);
		textClip.width().set(widthText - (leftSpace + rightSpace));

		textClip.clippingShape().set(new at.bestsolution.wgraf.geom.shape.Rectangle(0, 5, textClip.width().get(), 30));

		for (TextButton b : leftButtons) {
			int idx = leftButtons.indexOf(b);

			b.x().set(idx * 40);
			b.width().set(40);
			b.height().set(40);
		}

		for (TextButton b : rightButtons) {
			int idx = rightButtons.indexOf(b);

			b.x().set(widthLeftButtons + widthText + idx * 40);
			b.width().set(40);
			b.height().set(40);
		}

	}

	private Container textClip = new Container();


	private Property<Boolean> editable = new SimpleProperty<Boolean>(true);
	public Property<Boolean> editable() {
		return editable;
	}

	public Text() {

		Binder.uniBind(editable, new Setter<Boolean>() {
			@Override
			public void set(Boolean value) {
				area.acceptTapEvents().set(value);
				if (value) {
					area.inputMethod().set(InputMethod.TEXT_ANY);
				}
				else {
					area.inputMethod().set(InputMethod.NONE);
				}
			}
		});

		area.acceptFocus().set(true);

		area.width().set(200d);
		area.height().set(40d);
		area.cache().set(true);


		Binder.uniBind(area.width(), new Setter<Double>() {
			@Override
			public void set(Double value) {
				resize();
			}
		});

		textClip.parent().set(area);
		textClip.width().set(180);
		textClip.height().set(40);
		textClip.x().set(10);
		textClip.y().set(0);

		textClip.clippingShape().set(new at.bestsolution.wgraf.geom.shape.Rectangle(0, 5, 180, 30));



		area.onScroll().registerSignalListener(new SignalListener<ScrollEvent>() {
			@Override
			public void onSignal(ScrollEvent data) {
				if (data.scrollLock == ScrollLock.HORIZONTAL) {
					// scroll the inner text
					double min = Math.min(0, textClip.width().get() - calcTextWidth());
					double max = 0;
					nodeText.x().updateDynamic(new ClampedDoubleIncrement(-data.deltaX, min, max));
					data.consume();
				}
			}
		});

		nodeText = new at.bestsolution.wgraf.scene.Text();
		nodeText.parent().set(textClip);
		nodeText.x().set(0);
		nodeText.x().setTransition(new TouchScrollTransition());
		nodeText.cache().set(true);
		font().registerChangeListener(new ChangeListener<Font>() {
			@Override
			public void onChange(Font oldValue, Font newValue) {
				double height = newValue.stringExtent("Asdy").y;
				nodeText.y().set(40/2d - height/2);
				nodeCursor.y().set(40/2d - height/2);
				nodeCursor.height().set(height);
			}
		});

		nodeCursor = new Container();
		nodeCursor.parent().set(textClip);
//		nodeCursor.background().set(new FillBackground(new Color(0, 255, 255, 100), new CornerRadii(0), new Insets(0,0,0,0)));
		nodeCursor.width().set(3d);
		nodeCursor.height().set(30d);
		nodeCursor.cache().set(true);

		nodeText.x().registerChangeListener(new DoubleChangeListener() {
			@Override
			public void onChange(double oldValue, double newValue) {
				double textOffset = newValue;
				double letterOffset = getLetter(font().get(), text().get(), cursorIndex.get()).beginX;
				nodeCursor.x().set(textOffset + letterOffset);
			}
		});
		cursorIndex.registerChangeListener(new ChangeListener<Integer>() {
			@Override
			public void onChange(Integer oldValue, Integer newValue) {
				double textOffset = nodeText.x().get();
				double letterOffset = getLetter(font().get(), text().get(), newValue).beginX;

				if (letterOffset < -textOffset) {
					nodeText.x().setDynamic(Math.min(0, -letterOffset + 10));
				}
				else if (letterOffset > -textOffset + textClip.width().get()) {
					nodeText.x().setDynamic(-letterOffset + textClip.width().get() - 10);
				}

				nodeCursor.x().set(textOffset + letterOffset);
			}
		});

		area.onTap().registerSignalListener(new SignalListener<TapEvent>() {
			@Override
			public void onSignal(TapEvent data) {

				if (expandButton != null) {
					expandButton.triggerActivated();
					data.consume();
					return;
				}


				if (!editable().get()) return;

				final Font currentFont = font().get();
				final String currentText = text().get();
				System.err.println("currentText = " + currentText);

				final int firstIdx = 0;
				final int lastIdx = Math.max(0, currentText.length());

				final double offsetX = data.x - nodeText.x().get() - textClip.x().get();
				Letter result = binSearch(currentFont, currentText, firstIdx, lastIdx, offsetX);

				if ((result.endX - result.beginX) / 2 < offsetX - result.beginX) {
					updateCursorIndex(result.index + 1);
				}
				else {
					updateCursorIndex(result.index);
				}
				data.consume();
			}
		});
		area.onKeyPress().registerSignalListener(new SignalListener<KeyEvent>() {
			@Override
			public void onSignal(KeyEvent data) {
				if (!editable().get()) return;
//				System.err.println(data.code + " - " + data.key);

				if (data.code == KeyCode.BACKSPACE || BACKSPACE.equals(data.key)) {
					triggerBackspace();
				}
				else if (data.code == KeyCode.DELETE || DELETE.equals(data.key)) {
					triggerDelete();
				}
				else if (data.code == KeyCode.ENTER) {
					// do nothing
				}
				else if (data.code == KeyCode.TAB) {
					// do nothing
				}
				else if (data.code == KeyCode.LEFT) {
					final int idx = cursorIndex.get();
					updateCursorIndex(idx-1);
				}
				else if (data.code == KeyCode.RIGHT) {
					final int idx = cursorIndex.get();
					updateCursorIndex(idx+1);
				}
				else {
					if (data.key != null) {
						// letter
						final int idx = cursorIndex.get();
						System.err.println("cursorIndex : " + idx);
						String currentText = text().get();
						String headText = currentText.substring(0, idx);
						String tailText = currentText.substring(idx, currentText.length());
						String result = headText + data.key + tailText;
						text().set(result);
						updateCursorIndex(idx+1);
					}
				}
			}
		});



		area.inputMethod().set(InputMethod.TEXT_ANY);

		registerPseudoClassState("valid", valid());

		initDefaultStyle();
	}

	private void initDefaultStyle() {
		// this should come from css:
		Insets bgInsets = new Insets(0, 0, 0, 0);
		final FillBackground normalBackground = new FillBackground(
				new LinearGradient(0, 0, 0, 1, CoordMode.OBJECT_BOUNDING, Spread.PAD,
					new Stop(0, new Color(240, 240, 240, 255)),
					new Stop(1, new Color(255, 255, 255, 255))
				),
				new CornerRadii(4), bgInsets);
		final FillBackground invalidBackground = new FillBackground(
				new LinearGradient(0, 0, 0, 1, CoordMode.OBJECT_BOUNDING, Spread.PAD,
					new Stop(0, new Color(240, 200, 200, 255)),
					new Stop(1, new Color(255, 200, 200, 255))
				),
				new CornerRadii(4), bgInsets);

		final Border normalBorder = new Border(new BorderStroke( new Color(200, 200, 200, 255), new CornerRadii(4), new BorderWidths(1, 1, 1, 1), bgInsets));
		final Border invalidBorder = new Border(new BorderStroke( new Color(200, 0, 0, 255), new CornerRadii(4), new BorderWidths(1, 1, 1, 1), bgInsets));
		// TODO implement visibility
//		nodeCursor.background().set(new FillBackground(
//				new Color(255, 105, 105, 200),
//				new CornerRadii(0), new Insets(0)
//				));

		Binder.uniBind(valid(), new Setter<Boolean>() {
			@Override
			public void set(Boolean value) {
				area.border().set(value ? normalBorder : invalidBorder);
				area.background().set(value ? normalBackground : invalidBackground);
			}
		});

//		focus().registerChangeListener(new ChangeListener<Boolean>() {
//			@Override
//			public void onChange(Boolean oldValue, Boolean newValue) {
//				System.err.println("focus changed on " + Thread.currentThread());
//				if (newValue) {
//					area.background().set(new Backgrounds(
//							new FillBackground(new Color(255, 255, 255, 210), new CornerRadii(7), new Insets(5,5,5,5)),
//							new FillBackground(new Color(55, 55, 55, 100), new CornerRadii(10), new Insets(2,2,2,2)),
//							new FillBackground(new Color(255, 255, 0, 144), new CornerRadii(10), new Insets(0, 0, 0, 0))
//							));
//				}
//				else {
//					area.background().set(new Backgrounds(
//							new FillBackground(new Color(255, 255, 255, 210), new CornerRadii(7), new Insets(5,5,5,5)),
//							new FillBackground(new Color(55, 55, 55, 100), new CornerRadii(10), new Insets(2,2,2,2))
//							));
//				}
//			}
//		});

		// for now we change the background property of the cursor depending on the focus
		// better would be to set the visibility or the opacity - but both do not exist at the moment -.-
		// TODO add visible and opacity properties to nodes=?
		focus().registerChangeListener(new ChangeListener<Boolean>() {
			@Override
			public void onChange(Boolean oldValue, Boolean newValue) {
				if (newValue && editable().get()) {
					nodeCursor.background().set(new FillBackground(Skin.TEXT_CURSOR.get(), new CornerRadii(0), new Insets(0,0,0,0)));
				}
				else {
					nodeCursor.background().set(null);
				}
			}
		});


	}

	private double calcTextWidth() {
		return font().get().stringExtent(text().get()).x;
	}

	private void triggerDelete() {
		final int idx = cursorIndex.get();
		String currentText = text().get();
		String headText = currentText.substring(0, idx);
		String tailText = currentText.substring(idx+1, currentText.length());
		String result = headText + tailText;
		text().set(result);
		//updateCursorIndex(idx);
	}
	private void triggerBackspace() {
		final int idx = cursorIndex.get();
		String currentText = text().get();
		String headText = currentText.substring(0,  Math.max(0, idx - 1));
		String tailText = currentText.substring(idx, currentText.length());
		String result = headText + tailText;
		text().set(result);
		updateCursorIndex(idx - 1);
	}

	private void updateCursorIndex(int idx) {
		final int textLength = text().get().length();
		idx = Math.max(0, idx);
		idx = Math.min(textLength, idx);
		System.err.println("setting cursorIndex to " + idx);
		cursorIndex.set(idx);
	}

	@Override
	public Vec2d computePreferredSize() {
		return font().get().stringExtent(text.get());
	}

	private Property<Boolean> valid = new SimpleProperty<Boolean>(true);
	public Property<Boolean> valid() {
		return valid;
	}

	private ValidationProperty<String> text = null;
	public Property<String> text() {
		if (text == null) {
			text = new SimpleValidationProperty<String>("");
			Binder.uniBind(text, nodeText.text());
			text.registerChangeListener(new ChangeListener<String>() {
				@Override
				public void onChange(String oldValue, String newValue) {
					// refresh the cursor index - in case its now out of bounds
					updateCursorIndex(cursorIndex.get());
				}
			});
			text.valid().registerChangeListener(new ChangeListener<ValidState>() {
				@Override
				public void onChange(ValidState oldValue, ValidState newValue) throws InvalidValueException {
					valid().set(newValue.isValid());
				}
			});
		}
		return text;
	}

	private void hideInvalidMarker() {
		System.err.println("hideValidMarker()");
	}

	private void showInvalidMarker() {
		System.err.println("showValidMarker()");
	}

	public Property<Font> font() {
		return nodeText.font();
	}

	public ReadOnlyProperty<Boolean> focus() {
		return area.focus();
	}
}
