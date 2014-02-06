package at.bestsolution.wgraf.widgets.popups;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import at.bestsolution.wgraf.Application;
import at.bestsolution.wgraf.events.KeyCode;
import at.bestsolution.wgraf.events.KeyEvent;
import at.bestsolution.wgraf.events.TapEvent;
import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.paint.LinearGradient;
import at.bestsolution.wgraf.paint.LinearGradient.CoordMode;
import at.bestsolution.wgraf.paint.LinearGradient.Spread;
import at.bestsolution.wgraf.paint.LinearGradient.Stop;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.Converter;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.scene.Container;
import at.bestsolution.wgraf.scene.Node;
import at.bestsolution.wgraf.style.Border;
import at.bestsolution.wgraf.style.BorderStroke;
import at.bestsolution.wgraf.style.BorderWidths;
import at.bestsolution.wgraf.style.CornerRadii;
import at.bestsolution.wgraf.style.FillBackground;
import at.bestsolution.wgraf.style.Font;
import at.bestsolution.wgraf.style.FontAwesome;
import at.bestsolution.wgraf.style.Insets;
import at.bestsolution.wgraf.util.NodeIterator;
import at.bestsolution.wgraf.widgets.AbsolutePane;
import at.bestsolution.wgraf.widgets.Button;
import at.bestsolution.wgraf.widgets.Popup;
import at.bestsolution.wgraf.widgets.ToggleButton;

public class FullKeyboard extends Popup {

	private static class Key {
		public final String lower;
		public final String upper;
		public final KeyCode special;

		public final double width;

		public final boolean toggle;

		public Key(String lower, String upper, KeyCode special, double width) {
			this.lower = lower;
			this.upper = upper;
			this.special = special;
			this.width = width;
			this.toggle = false;
		}

		public Key(String lower, String upper, KeyCode special, double width, boolean toggle) {
			this.lower = lower;
			this.upper = upper;
			this.special = special;
			this.width = width;
			this.toggle = toggle;
		}
	}
	
	private static class AKey extends Key {

		public AKey(String lower, String upper, KeyCode special, double width) {
			super(FontAwesome.MAP.get(lower), FontAwesome.MAP.get(upper), special, width);
		}

		public AKey(String lower, String upper, KeyCode special, double width,
				boolean toggle) {
			super(FontAwesome.MAP.get(lower), FontAwesome.MAP.get(upper), special, width, toggle);
		}
		
	}

	private java.util.List<Integer> rows = Arrays.asList(14, 14, 13, 13, 6);

	private java.util.List<Key> keys = Arrays.asList(
			new Key("^", "\u00b0", null, 40),
			new Key("1", "!", null, 40),
			new Key("2", "\"", null, 40),
			new Key("3", "\u00a7", null, 40),
			new Key("4", "$", null, 40),
			new Key("5", "%", null, 40),
			new Key("6", "&", null, 40),
			new Key("7", "/", null, 40),
			new Key("8", "(", null, 40),
			new Key("9", ")", null, 40),
			new Key("0", "=", null, 40),
			new Key("\u00df", "?", null, 40),
			new Key("\u00b4", "`", null, 40),
			new AKey("fa-long-arrow-left", "fa-long-arrow-left", KeyCode.BACKSPACE, 80),

			new Key("TAB", "TAB", KeyCode.TAB, 60),
			new Key("q", "Q", null, 40),
			new Key("w", "W", null, 40),
			new Key("e", "E", null, 40),
			new Key("r", "R", null, 40),
			new Key("t", "T", null, 40),
			new Key("z", "Z", null, 40),
			new Key("u", "U", null, 40),
			new Key("i", "I", null, 40),
			new Key("o", "O", null, 40),
			new Key("p", "P", null, 40),
			new Key("\u00fc", "\u00dc", null, 40),
			new Key("+", "*", null, 40),
			new Key("", "", KeyCode.ENTER, 60),

			new AKey("fa-arrow-circle-up", "fa-arrow-circle-up", KeyCode.CAPS, 70, true),
			new Key("a", "A", null, 40),
			new Key("s", "S", null, 40),
			new Key("d", "D", null, 40),
			new Key("f", "F", null, 40),
			new Key("g", "G", null, 40),
			new Key("h", "H", null, 40),
			new Key("j", "J", null, 40),
			new Key("k", "K", null, 40),
			new Key("l", "L", null, 40),
			new Key("\u00f6", "\u00d6", null, 40),
			new Key("\u00e4", "\u00c4", null, 40),
			new Key("#", "'", null, 40),

			new AKey("fa-arrow-circle-o-up", "fa-arrow-circle-o-up", KeyCode.SHIFT, 50),
			new Key("<", ">", null, 40),
			new Key("y", "Y", null, 40),
			new Key("x", "X", null, 40),
			new Key("c", "C", null, 40),
			new Key("v", "V", null, 40),
			new Key("b", "B", null, 40),
			new Key("n", "N", null, 40),
			new Key("m", "M", null, 40),
			new Key(",", ";", null, 40),
			new Key(".", ":", null, 40),
			new Key("-", "_", null, 40),
			new AKey("fa-arrow-circle-o-up", "fa-arrow-circle-o-up", KeyCode.SHIFT, 90),

			new Key("", "", KeyCode.TAB, 50),
			new Key("", "", KeyCode.TAB, 50),
			new Key("", "", KeyCode.TAB, 40),
			new Key(" ", " ", null, 270),
			new Key("", "", KeyCode.TAB, 40),
			new Key("", "", KeyCode.TAB, 50)
			);


	private Button q;

	private Button w;

	private Button e;

	private Font font = Font.UBUNTU.resize(16);
	private Font fontawesome = new Font(FontAwesome.FONTAWESOME, 16);
	
	private Button createButton(double width, double height) {
		Button b = new Button();
		b.width().set(width);
		b.height().set(height);
		b.acceptFocus().set(false);
		b.font().set(font);

		return b;
	}

	private Map<Key, Button> buttons = new HashMap<Key, Button>();

	private boolean shift = false;
	private boolean caps = false;

	private void onButton(Key key) {
		if (key.special == KeyCode.SHIFT) {
			shift = !shift;
			updateButtons();
			return;
		}

		boolean upper = shift || caps;



		Node<?> node = Application.get().focusNode().get();
		if (node != null) {
			if (key.special == null) {
				node.onKeyPress().signal(new KeyEvent(null, upper ? key.upper : key.lower));
			}
			else {
				node.onKeyPress().signal(new KeyEvent(key.special, null));
			}
		}

		shift = false;
		updateButtons();
	}

	private void updateButtons() {
		for (Key k : keys) {

			Button b = buttons.get(k);

			if (b != null) {
				b.text().set((shift || caps) ? k.upper : k.lower);
			}
		}
	}

	private void initButtons() {
		Container area = getContent().getAreaNode();
		AbsolutePane pane = (AbsolutePane) getContent();
		int y = 10;
		int x = 10;
		int keyIdx = 0;
		for (int row = 0; row < rows.size(); row++ ) {
			int length = rows.get(row);
			for (int i = 0; i < length; i++) {
				final Key key = keys.get(keyIdx);

				if (key.toggle) {
					ToggleButton b = new ToggleButton();

					b.width().set(key.width);
					b.height().set(40);
					b.acceptFocus().set(false);
					
					if (key instanceof AKey) {
						b.font().set(fontawesome);
					}
					else {
						b.font().set(font);
					}

					pane.addWidget(b, x, y);
//					b.x().set(x);
//					b.y().set(y);
//					b.parent().set(area);

					getContent().addWidget(b);

					b.text().set(key.lower);


					b.selected().registerChangeListener(new ChangeListener<Boolean>() {
						@Override
						public void onChange(Boolean oldValue, Boolean newValue) {
							caps = newValue;
							updateButtons();
						}
					});
				}
				else {
					Button b = createButton(key.width, 40);

					if (key instanceof AKey) {
						b.font().set(fontawesome);
					}
					else {
						b.font().set(font);
					}
					
					pane.addWidget(b, x, y);
//					b.x().set(x);
//					b.y().set(y);
//					b.parent().set(area);
					b.text().set(key.lower);


					b.activated().registerSignalListener(new SignalListener<Void>() {
						@Override
						public void onSignal(Void data) {
							onButton(key);
						}
					});

					buttons.put(key, b);
				}

				x += key.width + 4;
				keyIdx++;
			}
			y += 44;
			x = 10;
		}


	}

	public FullKeyboard() {
		super(new AbsolutePane(), false);

		position().set(PopupPosition.BOTTOM_RIGHT);
		squeezeViewport().set(true);

		Container area = getContent().getAreaNode();

		AbsolutePane pane = (AbsolutePane) getContent();

		area.width().set(750);
		area.height().set(240);
		area.x().set(50);

		area.onTap().registerSignalListener(new SignalListener<TapEvent>() {
			@Override
			public void onSignal(TapEvent data) {
				data.consume();
			}
		});

		Insets bgInsets = new Insets(0, 0, 0, 0);
		final FillBackground bg = new FillBackground(
				new LinearGradient(0, 0, 0, 1, CoordMode.OBJECT_BOUNDING, Spread.PAD,
					new Stop(0, new Color(0, 0, 0, 200)),
					new Stop(1, new Color(50, 50, 50, 200))
				),
				new CornerRadii(10), bgInsets);
		area.background().set(bg);

		area.border().set(new Border(new BorderStroke(new Color(240, 240, 240, 255), new CornerRadii(10), new BorderWidths(3), bgInsets)));

		initButtons();
//		double off = 0;
//		tab = createButton(60, 40);
//		tab.text().set("TAB");
//		tab.area.parent().set(area);
//		tab.area.x().set(off);
//		tab.area.y().set(40);
//
//		tab.activated().registerSignalListener(new SignalListener<Void>() {
//			@Override
//			public void onSignal(Void data) {
//				Node<?> node = Application.get().focusNode().get();
//				if (node != null) {
//					node.onKeyPress().signal(new KeyEvent(KeyCode.TAB, null));
//				}
//			}
//		});
//
//
//		off += 60;
//
//		q = createButton(40, 40);
//		q.text().set("q");
//		q.area.parent().set(area);
//		q.area.x().set(off);
//		q.area.y().set(40);
//
//		q.activated().registerSignalListener(new SignalListener<Void>() {
//			@Override
//			public void onSignal(Void data) {
//				Node<?> node = Application.get().focusNode().get();
//				if (node != null) {
//					node.onKeyPress().signal(new KeyEvent(KeyCode.Q, "q"));
//				}
//			}
//		});
//
//		off += 40;
//
//		w = createButton(40, 40);
//		w.text().set("w");
//		w.font().set(font);
//		w.area.parent().set(area);
//		w.area.x().set(off);
//		w.area.y().set(40);
//
//		w.activated().registerSignalListener(new SignalListener<Void>() {
//			@Override
//			public void onSignal(Void data) {
//				Node<?> node = Application.get().focusNode().get();
//				if (node != null) {
//					node.onKeyPress().signal(new KeyEvent(KeyCode.W, "w"));
//				}
//			}
//		});
//
//
//		off += 40;
//
//		e = createButton(40, 40);
//		e.text().set("e");
//		e.font().set(font);
//		e.area.parent().set(area);
//		e.area.x().set(off);
//		e.area.y().set(40);
//		e.activated().registerSignalListener(new SignalListener<Void>() {
//			@Override
//			public void onSignal(Void data) {
//				Node<?> node = Application.get().focusNode().get();
//				if (node != null) {
//					node.onKeyPress().signal(new KeyEvent(KeyCode.E, "e"));
//				}
//			}
//		});
//		off += 40;
//
//
//		{
//			Button backspace = createButton(80, 40);
//			backspace.text().set("<--");
//			backspace.font().set(font);
//			backspace.area.parent().set(area);
//			backspace.area.x().set(520);
//			backspace.area.y().set(0);
//			backspace.activated().registerSignalListener(new SignalListener<Void>() {
//				@Override
//				public void onSignal(Void data) {
//					Node<?> node = Application.get().focusNode().get();
//					if (node != null) {
//						node.onKeyPress().signal(new KeyEvent(KeyCode.BACKSPACE, null));
//					}
//				}
//			});
//		}


		{
			Button next = createButton(70, 40);
			next.text().set("next");
			next.font().set(font);
			pane.addWidget(next, 150+500 + 20, 10);
//			up.parent().set(area);
//			up.x().set(150+500);
//			up.y().set(120);
			next.activated().registerSignalListener(new SignalListener<Void>() {
				@Override
				public void onSignal(Void data) {
					Application.get().focusNextNode();
				}
			});
		}

		{
			Button prev = createButton(70, 40);
			prev.text().set("prev");
			prev.font().set(font);
			pane.addWidget(prev, 150+500 + 20, 60);
//			up.parent().set(area);
//			up.x().set(150+500);
//			up.y().set(120);
			prev.activated().registerSignalListener(new SignalListener<Void>() {
				@Override
				public void onSignal(Void data) {
					Application.get().focusPrevNode();
				}
			});
		}



		{
			Button up = createButton(40, 40);
			up.text().set(FontAwesome.MAP.get("fa-arrow-up"));
			up.font().set(fontawesome);
			pane.addWidget(up, 150+500 + 10, 120 + 25);
//			up.parent().set(area);
//			up.x().set(150+500);
//			up.y().set(120);
			up.activated().registerSignalListener(new SignalListener<Void>() {
				@Override
				public void onSignal(Void data) {
					Node<?> node = Application.get().focusNode().get();
					if (node != null) {
						node.onKeyPress().signal(new KeyEvent(KeyCode.UP, null));
					}
				}
			});
		}
		{
			Button down = createButton(40, 40);
			down.text().set(FontAwesome.MAP.get("fa-arrow-down"));
			down.font().set(fontawesome);
			pane.addWidget(down, 150+500 + 10, 162 + 25);
//			down.parent().set(area);
//			down.x().set(150+500);
//			down.y().set(162);
			down.activated().registerSignalListener(new SignalListener<Void>() {
				@Override
				public void onSignal(Void data) {
					Node<?> node = Application.get().focusNode().get();
					if (node != null) {
						node.onKeyPress().signal(new KeyEvent(KeyCode.DOWN, null));
					}
				}
			});
		}
		{
			Button left = createButton(40, 40);
			left.text().set(FontAwesome.MAP.get("fa-arrow-left"));
			left.font().set(fontawesome);
			pane.addWidget(left, 150+458 + 10, 162 + 25);
//			left.parent().set(area);
//			left.x().set(150+458);
//			left.y().set(162);
			left.activated().registerSignalListener(new SignalListener<Void>() {
				@Override
				public void onSignal(Void data) {
					Node<?> node = Application.get().focusNode().get();
					if (node != null) {
						node.onKeyPress().signal(new KeyEvent(KeyCode.LEFT, null));
					}
				}
			});
		}

		{
			Button right = createButton(40, 40);
			right.text().set(FontAwesome.MAP.get("fa-arrow-right"));
			right.font().set(fontawesome);
			pane.addWidget(right, 150+542 + 10, 162 + 25);
//			right.parent().set(area);
//			right.x().set(150+542);
//			right.y().set(162);
			right.activated().registerSignalListener(new SignalListener<Void>() {
				@Override
				public void onSignal(Void data) {
					Node<?> node = Application.get().focusNode().get();
					if (node != null) {
						node.onKeyPress().signal(new KeyEvent(KeyCode.RIGHT, null));
					}
				}
			});
		}

	}


}
