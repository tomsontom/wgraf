package at.bestsolution.wgraf.widgets.popups;

import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.scene.Container;
import at.bestsolution.wgraf.scene.Node;
import at.bestsolution.wgraf.style.Border;
import at.bestsolution.wgraf.style.BorderStroke;
import at.bestsolution.wgraf.style.BorderWidths;
import at.bestsolution.wgraf.style.CornerRadii;
import at.bestsolution.wgraf.style.FillBackground;
import at.bestsolution.wgraf.style.Font;
import at.bestsolution.wgraf.style.Insets;
import at.bestsolution.wgraf.widgets.AbsolutePane;
import at.bestsolution.wgraf.widgets.Button;
import at.bestsolution.wgraf.widgets.Popup;

public class NumblockKeyboard extends Popup {

	private static class NumKey {
		public final String content;
		public final KeyCode special;
		
		public NumKey(String content, KeyCode special) {
			this.content = content;
			this.special = special;
		}
		
	}
	
	private List<NumKey> keys = new ArrayList<NumKey>();
	
	private Font font = new Font("Sans", 16);
	
	private Button createButton(double width, double height) {
		Button b = new Button();
		b.width().set(width);
		b.height().set(height);
		b.acceptFocus().set(false);
		b.font().set(font);
		
		return b;
	}
	
	private Map<NumKey, Button> buttons = new HashMap<NumKey, Button>();
	
	private boolean shift = false;
	private boolean caps = false;
	
	private void onButton(NumKey key) {
		
		Node<?> node = Application.get().focusNode().get();
		if (node != null) {
			if (key.special == null) {
				node.onKeyPress().signal(new KeyEvent(null, key.content));
			}
			else {
				node.onKeyPress().signal(new KeyEvent(key.special, null));
			}
		}
		
		shift = false;
		updateButtons();
	}
	
	private void updateButtons() {
		for (NumKey k : keys) {
			
			Button b = buttons.get(k);
			
			if (b != null) {
				b.text().set(k.content);
			}
		}
	}
	
	private void initButtons() {
		AbsolutePane pane = (AbsolutePane) getContent();
		for (int i = 1; i <= 9; i++) {
			final NumKey k = new NumKey("" + i, null);
			final Button b = createButton(40, 40);
			buttons.put(k, b);
			keys.add(k);
			
			b.activated().registerSignalListener(new SignalListener<Void>() {
				@Override
				public void onSignal(Void data) {
					onButton(k);
				}
			});
			
			int xIdx = (i-1) % 3;
			int yIdx = (i-1) / 3;
			
			pane.addWidget(b, 10 + xIdx * 50, 10 + yIdx * 50);
		}
		
		// comma
		// TODO honor locale
		final NumKey k = new NumKey("" + DecimalFormatSymbols.getInstance().getDecimalSeparator(), null);
		final Button b = createButton(40, 40);
		buttons.put(k, b);
		keys.add(k);
		
		b.activated().registerSignalListener(new SignalListener<Void>() {
			@Override
			public void onSignal(Void data) {
				onButton(k);
			}
		});
		pane.addWidget(b, 10 + 0 * 50, 10 + 3 * 50);
		
		// zero
		final NumKey k0 = new NumKey("0", null);
		final Button b0 = createButton(40, 40);
		buttons.put(k0, b0);
		keys.add(k0);
		
		b0.activated().registerSignalListener(new SignalListener<Void>() {
			@Override
			public void onSignal(Void data) {
				onButton(k0);
			}
		});
		pane.addWidget(b0, 10 + 1 * 50, 10 + 3 * 50);
		
		// enter
		final NumKey kE = new NumKey("<-", KeyCode.BACKSPACE);
		final Button bE = createButton(40, 40);
		buttons.put(kE, bE);
		keys.add(kE);
		
		bE.activated().registerSignalListener(new SignalListener<Void>() {
			@Override
			public void onSignal(Void data) {
				onButton(kE);
			}
		});
		pane.addWidget(bE, 10 + 2 * 50, 10 + 3 * 50);
		
		
		updateButtons();
		
	}
	
	public NumblockKeyboard() {
		super(new AbsolutePane(), false);
		
		position().set(PopupPosition.BOTTOM_RIGHT);
		squeezeViewport().set(true);
		
		Container area = getContent().getAreaNode();
		
		AbsolutePane pane = (AbsolutePane) getContent();
		
		area.width().set(160);
		area.height().set(210);
		
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
		
	}
	
	
}
