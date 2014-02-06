package at.bestsolution.wgraf.widgets;

import at.bestsolution.wgraf.Sync;
import at.bestsolution.wgraf.events.KeyCode;
import at.bestsolution.wgraf.events.KeyEvent;
import at.bestsolution.wgraf.events.TapEvent;
import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.paint.LinearGradient;
import at.bestsolution.wgraf.paint.Paint;
import at.bestsolution.wgraf.paint.LinearGradient.CoordMode;
import at.bestsolution.wgraf.paint.LinearGradient.Spread;
import at.bestsolution.wgraf.paint.LinearGradient.Stop;
import at.bestsolution.wgraf.properties.binding.Binder;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.DoubleChangeListener;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.ReadOnlyProperty;
import at.bestsolution.wgraf.properties.Signal;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.properties.binding.Setter;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.properties.simple.SimpleSignal;
import at.bestsolution.wgraf.style.Background;
import at.bestsolution.wgraf.style.Backgrounds;
import at.bestsolution.wgraf.style.CornerRadii;
import at.bestsolution.wgraf.style.DropShadow;
import at.bestsolution.wgraf.style.FillBackground;
import at.bestsolution.wgraf.style.Font;
import at.bestsolution.wgraf.style.Insets;

public class ToggleButton extends Widget {

	private final at.bestsolution.wgraf.scene.Text nodeText;
	
	public ToggleButton() {
		
		area.width().set(200d);
		area.height().set(40d);
		
		
		// this should come from css:
		area.background().set(new FillBackground(new Color(255, 0, 0, 144), new CornerRadii(10), new Insets(2, 2, 2, 2)));
		focus().registerChangeListener(new ChangeListener<Boolean>() {
			@Override
			public void onChange(Boolean oldValue, Boolean newValue) {
				if (newValue) {
					if (active().get()) {
						area.background().set(new Backgrounds(
								new FillBackground(new Color(0, 0, 0, 144), new CornerRadii(10), new Insets(2, 2, 2, 2)),
								new FillBackground(new Color(255, 255, 255, 255), new CornerRadii(10), new Insets(2, 2, 2, 2)),
								new FillBackground(new Color(255, 255, 0, 144), new CornerRadii(10), new Insets(0, 0, 0, 0))
								));
					}
					else {
						area.background().set(new Backgrounds(
								new FillBackground(new Color(255, 0, 0, 144), new CornerRadii(10), new Insets(2, 2, 2, 2)),
								new FillBackground(new Color(255, 255, 255, 255), new CornerRadii(10), new Insets(2, 2, 2, 2)),
								new FillBackground(new Color(255, 255, 0, 144), new CornerRadii(10), new Insets(0, 0, 0, 0))
								));
					}
				}
				else {
					if (active().get()) {
						area.background().set(new Backgrounds(
								new FillBackground(new Color(0, 0, 0, 144), new CornerRadii(10), new Insets(2, 2, 2, 2))
								));
					}
					else {
						area.background().set(new FillBackground(new Color(255, 0, 0, 144), new CornerRadii(10), new Insets(2, 2, 2, 2)));
					}

				}
			}
		});
		selected().registerChangeListener(new ChangeListener<Boolean>() {
			@Override
			public void onChange(Boolean oldValue, Boolean newValue) {
				if (newValue) {
					if (focus().get()) {
						area.background().set(new Backgrounds(
								new FillBackground(new Color(0, 0, 0, 144), new CornerRadii(10), new Insets(2, 2, 2, 2)),
								new FillBackground(new Color(255, 255, 255, 255), new CornerRadii(10), new Insets(2, 2, 2, 2)),
								new FillBackground(new Color(255, 255, 0, 144), new CornerRadii(10), new Insets(0, 0, 0, 0))
								));
					}
					else {
						area.background().set(new Backgrounds(
								new FillBackground(new Color(0, 0, 0, 144), new CornerRadii(10), new Insets(2, 2, 2, 2))
								));
					}
				}
				else {
					if (focus().get()) {
						area.background().set(new Backgrounds(
								new FillBackground(new Color(255, 0, 0, 144), new CornerRadii(10), new Insets(2, 2, 2, 2)),
								new FillBackground(new Color(255, 255, 255, 255), new CornerRadii(10), new Insets(2, 2, 2, 2)),
								new FillBackground(new Color(255, 255, 0, 144), new CornerRadii(10), new Insets(0, 0, 0, 0))
								));
					}
					else {
						area.background().set(new FillBackground(new Color(255, 0, 0, 144), new CornerRadii(10), new Insets(2, 2, 2, 2)));

					}
				}
			}
			
		});
		
		area.acceptFocus().set(true);
		area.acceptTapEvents().set(true);
		
		area.addStyleClass("button");
		
		nodeText = new at.bestsolution.wgraf.scene.Text();
		nodeText.parent().set(area);
		
		nodeText.addStyleClass("label");
		
		area.onTap().registerSignalListener(new SignalListener<TapEvent>() {
			@Override
			public void onSignal(TapEvent data) {
				triggerActivated();
			}
		});
		
		area.onKeyPress().registerSignalListener(new SignalListener<KeyEvent>() {
			@Override
			public void onSignal(KeyEvent data) {
				if (data.code == KeyCode.SPACE || data.code == KeyCode.ENTER) {
					triggerActivated();
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
		
		
	
		
		registerPseudoClassState("focus", focus());
		
		initDefaultStyle();
	}
	
	private void initDefaultStyle() {
		// this should come from css:
		
		final CornerRadii cr = new CornerRadii(4);
		final Paint normalPaint = new Color(124, 124, 124, 255);
		final Paint disabledPaint = new Color(251, 251, 251, 255);
		final Paint activePaint = new Color(181, 181, 181, 255);
		
		final Background normal = new FillBackground(normalPaint, cr, Insets.NO_INSETS);
		final Background disabled = new FillBackground(disabledPaint, cr, Insets.NO_INSETS);
		final Background active = new FillBackground(activePaint, cr, Insets.NO_INSETS);

		
		Binder.uniBind(selected(), new Setter<Boolean>() {
			@Override
			public void set(Boolean value) {
				if (value) {
					area.background().set(active);
					nodeText.fill().set(normalPaint);
				}
				else {
					area.background().set(normal);
					nodeText.fill().set(new Color(255,255,255,255));
				}
			}
		});
		
		
	}
	
	private void triggerActivated() {
		selected.set(!selected.get());
		active.set(true);
		Sync.get().execLaterOnUIThread(new Runnable() {
			@Override
			public void run() {
				active.set(false);
				if (activated != null) {
					activated.signal(null);
				}
			}
		}, 100);
	}
	
	
	private Property<Boolean> selected = new SimpleProperty<Boolean>(false);
	public Property<Boolean> selected() {
		return selected;
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
			nodeText.x().set(area.width().get() / 2 - width / 2);
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
