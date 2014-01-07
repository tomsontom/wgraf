package at.bestsolution.wgraf.widgets;

import java.awt.Rectangle;

import sun.misc.Signal;
import at.bestsolution.wgraf.events.KeyCode;
import at.bestsolution.wgraf.events.KeyEvent;
import at.bestsolution.wgraf.events.ScrollEvent;
import at.bestsolution.wgraf.events.ScrollLock;
import at.bestsolution.wgraf.events.TapEvent;
import at.bestsolution.wgraf.math.Vec2d;
import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.properties.Binder;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.ClampedDoubleIncrement;
import at.bestsolution.wgraf.properties.DoubleChangeListener;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.ReadOnlyProperty;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.scene.Container;
import at.bestsolution.wgraf.style.Backgrounds;
import at.bestsolution.wgraf.style.CornerRadii;
import at.bestsolution.wgraf.style.FillBackground;
import at.bestsolution.wgraf.style.Font;
import at.bestsolution.wgraf.style.Insets;
import at.bestsolution.wgraf.transition.TouchScrollTransition;

// TODO add some kind of action menu - for copy, cut and paste
// TODO implement selection
// TODO filter keyboard events
// TODO we need some kind of delayed text change listener
//      to prevent model updates on every stroke
public class Text extends Widget {
	
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
	
	public Text() {
		
		// this should come from css:
		area.background().set(new Backgrounds(
				new FillBackground(new Color(255, 255, 255, 210), new CornerRadii(7), new Insets(5,5,5,5)),
				new FillBackground(new Color(55, 55, 55, 100), new CornerRadii(10), new Insets(2,2,2,2))
				));
		focus().registerChangeListener(new ChangeListener<Boolean>() {
			@Override
			public void onChange(Boolean oldValue, Boolean newValue) {
				System.err.println("focus changed on " + Thread.currentThread());
				if (newValue) {
					area.background().set(new Backgrounds(
							new FillBackground(new Color(255, 255, 255, 210), new CornerRadii(7), new Insets(5,5,5,5)),
							new FillBackground(new Color(55, 55, 55, 100), new CornerRadii(10), new Insets(2,2,2,2)),
							new FillBackground(new Color(255, 255, 0, 144), new CornerRadii(10), new Insets(0, 0, 0, 0))
							));
				}
				else {
					area.background().set(new Backgrounds(
							new FillBackground(new Color(255, 255, 255, 210), new CornerRadii(7), new Insets(5,5,5,5)),
							new FillBackground(new Color(55, 55, 55, 100), new CornerRadii(10), new Insets(2,2,2,2))
							));
				}
			}
		});
		
		
		area.acceptFocus().set(true);
		area.acceptTapEvents().set(true);
		
		area.width().set(200d);
		area.height().set(40d);
		
		final Container textClip = new Container();
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
		
		// for now we change the background property of the cursor depending on the focus
		// better would be to set the visibility or the opacity - but both do not exist at the moment -.-
		// TODO add visible and opacity properties to nodes=?
		focus().registerChangeListener(new ChangeListener<Boolean>() {
			@Override
			public void onChange(Boolean oldValue, Boolean newValue) {
				if (newValue) {
					nodeCursor.background().set(new FillBackground(new Color(0, 255, 255, 100), new CornerRadii(0), new Insets(0,0,0,0)));
				}
				else {
					nodeCursor.background().set(null);
				}
			}
		});
		
		area.onTap().registerSignalListener(new SignalListener<TapEvent>() {
			@Override
			public void onSignal(TapEvent data) {
				final Font currentFont = font().get();
				final String currentText = text().get();
				
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
				
				
			}
		});
		System.err.println("TextField: " + area.onKeyPress());
		area.onKeyPress().registerSignalListener(new SignalListener<KeyEvent>() {
			@Override
			public void onSignal(KeyEvent data) {
				System.err.println(data.code + " - " + data.key);
				
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
	
	private Property<String> text = null;
	public Property<String> text() {
		if (text == null) {
			text = new SimpleProperty<String>("");
			Binder.uniBind(text, nodeText.text());
			text.registerChangeListener(new ChangeListener<String>() {
				@Override
				public void onChange(String oldValue, String newValue) {
					updateCursorIndex(0);
				}
			});
		}
		return text;
	}
	
	public Property<Font> font() {
		return nodeText.font();
	}
	
	public ReadOnlyProperty<Boolean> focus() {
		return area.focus();
	}
}
