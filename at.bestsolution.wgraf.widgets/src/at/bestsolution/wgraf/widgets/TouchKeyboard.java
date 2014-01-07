package at.bestsolution.wgraf.widgets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import at.bestsolution.wgraf.Application;
import at.bestsolution.wgraf.events.KeyCode;
import at.bestsolution.wgraf.events.KeyEvent;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.scene.Node;
import at.bestsolution.wgraf.style.Font;

public class TouchKeyboard extends Widget {

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
	
	private java.util.List<Integer> rows = Arrays.asList(14, 14, 13, 13, 6);
	
	private java.util.List<Key> keys = Arrays.asList(
			new Key("^", "°", null, 40),
			new Key("1", "!", null, 40),
			new Key("2", "\"", null, 40),
			new Key("3", "§", null, 40),
			new Key("4", "$", null, 40),
			new Key("5", "%", null, 40),
			new Key("6", "&", null, 40),
			new Key("7", "/", null, 40),
			new Key("8", "(", null, 40),
			new Key("9", ")", null, 40),
			new Key("0", "=", null, 40),
			new Key("ß", "?", null, 40),
			new Key("´", "`", null, 40),
			new Key("<--", "<--", KeyCode.BACKSPACE, 80),
			
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
			new Key("ü", "Ü", null, 40),
			new Key("+", "*", null, 40),
			new Key("Enter", "Enter", KeyCode.ENTER, 60),
			
			new Key("CAPS", "CAPS", KeyCode.CAPS, 70, true),
			new Key("a", "A", null, 40),
			new Key("s", "S", null, 40),
			new Key("d", "D", null, 40),
			new Key("f", "F", null, 40),
			new Key("g", "G", null, 40),
			new Key("h", "H", null, 40),
			new Key("j", "J", null, 40),
			new Key("k", "K", null, 40),
			new Key("l", "L", null, 40),
			new Key("ö", "Ö", null, 40),
			new Key("ä", "Ä", null, 40),
			new Key("#", "'", null, 40),
			
			new Key("SHIFT", "SHIFT", KeyCode.SHIFT, 50),
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
			new Key("SHIFT", "SHIFT", KeyCode.SHIFT, 90),
			
			new Key("CTRL", "CTRL", KeyCode.TAB, 50),
			new Key("OS", "OS", KeyCode.TAB, 50),
			new Key("ALT", "ALT", KeyCode.TAB, 40),
			new Key(" ", " ", null, 270),
			new Key("ALT", "ALT", KeyCode.TAB, 40),
			new Key("CTRL", "CTRL", KeyCode.TAB, 50)
			);
	
	
	private Button q;
	
	private Button w;
	
	private Button e;
	
	private Font font = new Font("Sans", 16);
	
	private Button createButton(double width, double height) {
		Button b = new Button();
		b.area.width().set(width);
		b.area.height().set(height);
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
		int y = 0;
		int x = 0;
		int keyIdx = 0;
		for (int row = 0; row < rows.size(); row++ ) {
			int length = rows.get(row);
			for (int i = 0; i < length; i++) {
				final Key key = keys.get(keyIdx);
				
				if (key.toggle) {
					ToggleButton b = new ToggleButton();
					
					b.area.width().set(key.width);
					b.area.height().set(40);
					b.acceptFocus().set(false);
					b.font().set(font);
					
					b.area.x().set(x);
					b.area.y().set(y);
					b.area.parent().set(area);
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
					b.area.x().set(x);
					b.area.y().set(y);
					b.area.parent().set(area);
					b.text().set(key.lower);
					
					
					b.activated().registerSignalListener(new SignalListener<Void>() {
						@Override
						public void onSignal(Void data) {
							onButton(key);
						}
					});
					
					buttons.put(key, b);
				}
				
				x += key.width;
				keyIdx++;
			}
			y += 40;
			x = 0;
		}
		
		
	}
	
	public TouchKeyboard() {
		
		area.width().set(800);
		area.height().set(200);
		
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
			Button up = createButton(40, 40);
			up.text().set("^");
			up.font().set(font);
			up.area.parent().set(area);
			up.area.x().set(150+500);
			up.area.y().set(120);
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
			down.text().set("v");
			down.font().set(font);
			down.area.parent().set(area);
			down.area.x().set(150+500);
			down.area.y().set(160);
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
			left.text().set("<");
			left.font().set(font);
			left.area.parent().set(area);
			left.area.x().set(150+460);
			left.area.y().set(160);
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
			right.text().set(">");
			right.font().set(font);
			right.area.parent().set(area);
			right.area.x().set(150+540);
			right.area.y().set(160);
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
