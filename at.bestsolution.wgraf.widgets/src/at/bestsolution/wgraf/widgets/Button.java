package at.bestsolution.wgraf.widgets;

import at.bestsolution.wgraf.Sync;
import at.bestsolution.wgraf.events.KeyCode;
import at.bestsolution.wgraf.events.KeyEvent;
import at.bestsolution.wgraf.events.TapEvent;
import at.bestsolution.wgraf.math.Vec2d;
import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.paint.LinearGradient;
import at.bestsolution.wgraf.paint.LinearGradient.CoordMode;
import at.bestsolution.wgraf.paint.LinearGradient.Spread;
import at.bestsolution.wgraf.paint.LinearGradient.Stop;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.DoubleChangeListener;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.ReadOnlyProperty;
import at.bestsolution.wgraf.properties.Signal;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.properties.binding.Binder;
import at.bestsolution.wgraf.properties.binding.Setter;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.properties.simple.SimpleSignal;
import at.bestsolution.wgraf.scene.Image;
import at.bestsolution.wgraf.style.Background;
import at.bestsolution.wgraf.style.Backgrounds;
import at.bestsolution.wgraf.style.Border;
import at.bestsolution.wgraf.style.BorderStroke;
import at.bestsolution.wgraf.style.BorderWidths;
import at.bestsolution.wgraf.style.CornerRadii;
import at.bestsolution.wgraf.style.DropShadow;
import at.bestsolution.wgraf.style.FillBackground;
import at.bestsolution.wgraf.style.Font;
import at.bestsolution.wgraf.style.ImageSource;
import at.bestsolution.wgraf.style.Insets;


public class Button extends Widget {

	private final at.bestsolution.wgraf.scene.Text nodeText;
	
	
	public int tempLeftLabelOffset = 0;
	
	public Button() {
		
		area.width().set(200d);
		area.height().set(50d);
		
		
		
		area.acceptFocus().set(true);
		area.acceptTapEvents().set(true);
		
		area.addStyleClass("button");
		
		nodeText = new at.bestsolution.wgraf.scene.Text();
		nodeText.parent().set(area);
		
		
		nodeText.addStyleClass("label");
		
		
		area.onTap().registerSignalListener(new SignalListener<TapEvent>() {
			@Override
			public void onSignal(TapEvent data) {
				if (enabled().get()) {
					triggerActivated();
					data.consume();
				}
			}
		});
		
		area.onKeyPress().registerSignalListener(new SignalListener<KeyEvent>() {
			@Override
			public void onSignal(KeyEvent data) {
				if (enabled().get()) {
					if (data.code == KeyCode.SPACE || data.code == KeyCode.ENTER) {
						triggerActivated();
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
		
		registerPseudoClassState("active", active());
		registerPseudoClassState("focus", focus());
		
		initDefaultStyle();
	}
	
	private Property<ImageSource> icon = null;
	public Property<ImageSource> icon() {
		if (icon == null) {
			icon = new SimpleProperty<ImageSource>();
			Image img = new Image();
			img.parent().set(area);
			Binder.uniBind(icon, img.image());
			// TODO this is only a quick hack to get an icon in here
			img.x().set(10);
			img.y().set(10);
		}
		return icon;
	}
	
	private void initDefaultStyle() {
		// this should come from css:
		
		Insets bgInsets = new Insets(0, 0, 0, 0);
		final FillBackground normal = new FillBackground(
				new LinearGradient(0, 0, 0, 1, CoordMode.OBJECT_BOUNDING, Spread.PAD, 
					new Stop(0, new Color(181, 181, 181, 255)),
					new Stop(1, new Color(124, 124, 124, 255))
				), 
				new CornerRadii(4), bgInsets);
		
		final FillBackground disabled = new FillBackground(
				new LinearGradient(0, 0, 0, 1, CoordMode.OBJECT_BOUNDING, Spread.PAD, 
					new Stop(0, new Color(251, 251, 251, 255)),
					new Stop(1, new Color(194, 194, 194, 255))
				), 
				new CornerRadii(4), bgInsets);
		
		final Background normalWithFocus = new Backgrounds(
			normal,
			new FillBackground(new Color(255, 255, 255, 255), new CornerRadii(4), bgInsets),
			new FillBackground(new Color(255, 255, 0, 144), new CornerRadii(4), new Insets(0, 0, 0, 0))
			
		);
		
		final FillBackground active = new FillBackground(
				new LinearGradient(0, 0, 0, 1, CoordMode.OBJECT_BOUNDING, Spread.PAD, 
						new Stop(0, new Color(124, 124, 124, 255)),
						new Stop(1, new Color(181, 181, 181, 255))
					), 
					new CornerRadii(4), bgInsets);
		
		final Background activeWithFocus = new Backgrounds(
				active,
				new FillBackground(new Color(255, 255, 255, 255), new CornerRadii(4), bgInsets),
				new FillBackground(new Color(255, 255, 0, 144), new CornerRadii(4), new Insets(0, 0, 0, 0))
			);
		
		
		area.background().set(normal);
		
		area.border().set(new Border(new BorderStroke(new Color(100,100,100,100), new CornerRadii(4), new BorderWidths(1, 1, 1, 1), new Insets(0, 0, 0, 0))));
		
//		focus().registerChangeListener(new ChangeListener<Boolean>() {
//			@Override
//			public void onChange(Boolean oldValue, Boolean newValue) {
//				if (newValue) {
//					if (active().get()) {
//						area.background().set(activeWithFocus);
//					}
//					else {
//						area.background().set(normalWithFocus);
//					}
//				}
//				else {
//					if (active().get()) {
//						area.background().set(active);
//					}
//					else {
//						area.background().set(normal);
//					}
//
//				}
//			}
//		});
		Binder.uniBind(enabled(), new Setter<Boolean>() {
			@Override
			public void set(Boolean value) {
				if (value) {
					area.background().set(normal);
				}
				else {
					area.background().set(disabled);
				}
			}
		});
		active().registerChangeListener(new ChangeListener<Boolean>() {
			@Override
			public void onChange(Boolean oldValue, Boolean newValue) {
//				area.x().increment(newValue ? 2 : -2);
//				area.y().increment(newValue ? 2 : -2);
				if (newValue) {
//					if (focus().get()) {
//						area.background().set(activeWithFocus);
//					}
//					else {
						area.background().set(active);
//					}
				}
				else {
//					if (focus().get()) {
//						area.background().set(normalWithFocus);
//					}
//					else {
						area.background().set(normal);
//					}
				}
			}
			
		});
		
//		area.effect().set(new DropShadow());
		area.cache().set(true);
		nodeText.fill().set(new Color(255,255,255,255));
		
//		nodeText.effect().set(new DropShadow());
		
		nodeText.cache().set(true);
	}
	
	private void triggerActivated() {
		active.set(true);
//		Sync.get().asyncExecOnUIThread(new Runnable() {
//			@Override
//			public void run() {
//				active.set(false);
//				if (activated != null) {
//					activated.signal(null);
//				}
//			}
//		});
		Sync.get().execLaterOnUIThread(new Runnable() {
			@Override
			public void run() {
				active.set(false);
				if (activated != null) {
					activated.signal(null);
				}
			}
		}, 50);
	}
	
	private final DoubleChangeListener relayout = new DoubleChangeListener() {
		@Override
		public void onChange(double oldValue, double newValue) {
			layoutChilds();
		}
	};
	
	private void layoutChilds() {
		final Font font = font().get();
		
		if (font != null && text().get() != null) {
			final double fontHeight = font.stringExtent("Ay").y;
			
			final double width = font.stringExtent(text().get()).x;
			nodeText.y().set(area.height().get() / 2 - fontHeight / 2);
			nodeText.x().set(tempLeftLabelOffset + (area.width().get()-tempLeftLabelOffset) / 2 - width / 2);
		}
	}
	
	public final Property<Boolean> acceptFocus() {
		return area.acceptFocus();
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
	
	public ReadOnlyProperty<Boolean> focus() {
		return area.focus();
	}
	
	private Property<Boolean> active = new SimpleProperty<Boolean>(false);
	public ReadOnlyProperty<Boolean> active() {
		return active;
	}
}
