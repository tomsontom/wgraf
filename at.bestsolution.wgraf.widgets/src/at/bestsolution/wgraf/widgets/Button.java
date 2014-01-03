package at.bestsolution.wgraf.widgets;

import at.bestsolution.wgraf.events.KeyCode;
import at.bestsolution.wgraf.events.KeyEvent;
import at.bestsolution.wgraf.events.TapEvent;
import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.DoubleChangeListener;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.Signal;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.properties.simple.SimpleSignal;
import at.bestsolution.wgraf.style.CornerRadii;
import at.bestsolution.wgraf.style.FillBackground;
import at.bestsolution.wgraf.style.Font;
import at.bestsolution.wgraf.style.Insets;

public class Button extends Widget {

	private final at.bestsolution.wgraf.scene.Text nodeText;
	
	public Button() {
		
		area.width().set(200d);
		area.height().set(40d);
		
		area.background().set(new FillBackground(new Color(255, 0, 0, 144), new CornerRadii(10), new Insets(0, 0, 0, 0)));
		
		area.acceptFocus().set(true);
		area.acceptTapEvents().set(true);
		
		area.addStyleClass("button");
		
		nodeText = new at.bestsolution.wgraf.scene.Text();
		nodeText.setParent(area);
		
		nodeText.addStyleClass("label");
		
		area.onTap().registerSignalListener(new SignalListener<TapEvent>() {
			@Override
			public void onSignal(TapEvent data) {
				if (activated != null) {
					activated.signal(null);
				}
			}
		});
		
		area.onKeyPress().registerSignalListener(new SignalListener<KeyEvent>() {
			@Override
			public void onSignal(KeyEvent data) {
				
				if (data.code == KeyCode.SPACE || data.code == KeyCode.ENTER) {
					if (activated != null) {
						activated.signal(null);
					}
				}
				
			}
		});
		
		area.width().registerChangeListener(relayout);
		area.height().registerChangeListener(relayout);
		
		text().registerChangeListener(new ChangeListener<String>() {
			@Override
			public void onChange(String oldValue, String newValue) {
				layoutChilds();
			}
		});
		
		font().registerChangeListener(new ChangeListener<Font>() {
			@Override
			public void onChange(Font oldValue, Font newValue) {
				layoutChilds();
			}
		});
	}
	
	private final DoubleChangeListener relayout = new DoubleChangeListener() {
		@Override
		public void onChange(double oldValue, double newValue) {
			layoutChilds();
		}
	};
	
	private void layoutChilds() {
		final Font font = font().get();
		final double fontHeight = font.stringExtent("Ay").y;
		
		final double width = font.stringExtent(text().get()).x;
		nodeText.y().set(area.height().get() / 2 - fontHeight / 2);
		nodeText.x().set(area.width().get() / 2 - width / 2);
		
	}
	
	
	public final Property<String> text() {
		return nodeText.text();
	}
	
	private Signal<Void> activated = null;
	
	public final Signal<Void> activated() {
		if (activated == null) {
			activated = new SimpleSignal<Void>();
		}
		return activated;
	}

	public Property<Font> font() {
		return nodeText.font();
	}
}
