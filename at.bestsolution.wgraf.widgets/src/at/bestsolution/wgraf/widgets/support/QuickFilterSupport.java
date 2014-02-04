package at.bestsolution.wgraf.widgets.support;

import java.util.concurrent.atomic.AtomicReference;

import at.bestsolution.wgraf.Sync;
import at.bestsolution.wgraf.events.InputMethod;
import at.bestsolution.wgraf.events.KeyCode;
import at.bestsolution.wgraf.events.KeyEvent;
import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.paint.LinearGradient;
import at.bestsolution.wgraf.paint.LinearGradient.CoordMode;
import at.bestsolution.wgraf.paint.LinearGradient.Spread;
import at.bestsolution.wgraf.paint.LinearGradient.Stop;
import at.bestsolution.wgraf.properties.binding.Binder;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.InvalidValueException;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.binding.Setter;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.properties.ValueUpdate;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.scene.Container;
import at.bestsolution.wgraf.style.Border;
import at.bestsolution.wgraf.style.BorderStroke;
import at.bestsolution.wgraf.style.BorderWidths;
import at.bestsolution.wgraf.style.CornerRadii;
import at.bestsolution.wgraf.style.FillBackground;
import at.bestsolution.wgraf.style.Font;
import at.bestsolution.wgraf.style.Insets;
import at.bestsolution.wgraf.widgets.AbsolutePane;
import at.bestsolution.wgraf.widgets.Label;
import at.bestsolution.wgraf.widgets.Popup;
import at.bestsolution.wgraf.widgets.Text;
import at.bestsolution.wgraf.widgets.Widget;
import at.bestsolution.wgraf.widgets.Popup.PopupPosition;

public class QuickFilterSupport {

	private Popup quickFilterPopup;
	
	
	private Widget w;
	private Container area;
	private InputMethod m;
	
	private long TIMEOUT = 1500;
	
	public QuickFilterSupport(Widget w, InputMethod m, long resetTimeoutMs) {
		this.w = w;
		this.area = w.getAreaNode();
		this.m = m;
		
		this.TIMEOUT = resetTimeoutMs;
		
		init();
	}
	
	private void init() {
		area.acceptFocus().set(true);
		area.inputMethod().set(m);
		
		area.onKeyPress().registerSignalListener(new SignalListener<KeyEvent>() {
			
			private AtomicReference<Object> validReset = new AtomicReference<Object>();;
			
			@Override
			public void onSignal(final KeyEvent data) {
				if (data.code == KeyCode.BACKSPACE) {
					filter.update(new ValueUpdate<String>() {
						@Override
						public String update(String current) {
							if (current.length() > 0) {
								return current.substring(0, current.length() - 1);
							}
							else return current;
						}
					});
				}
				else if (data.code == null){
					filter.update(new ValueUpdate<String>() {
						@Override
						public String update(String current) {
							return current + data.key;
						}
					});
				}
				
				if (TIMEOUT > 0) {
					final Object reset = new Object();
					this.validReset.set(reset);
					
					Sync.get().execLaterOnUIThread(new Runnable() {
						@Override
						public void run() {
							if (validReset.get() == reset) {
								filter.set("");
							}
						}
					}, TIMEOUT);
				}
			}
		});
		
		filter.registerChangeListener(new ChangeListener<String>() {
			@Override
			public void onChange(String oldValue, String newValue)
					throws InvalidValueException {
				System.err.println("current filter: " + newValue);
			}
		});
		
		
		AbsolutePane popup = new AbsolutePane();
		popup.width().set(220);
		popup.height().set(70);
		Label label = new Label();
		label.font().set(Font.UBUNTU.resize(12));
		label.text().set("QuickFilter: ");
		
		popup.addWidget(label, 10, 10);
		
		Text text = new Text();
		text.text().set("");
		text.font().set(Font.UBUNTU.resize(13));
		text.editable().set(false);
		text.height().set(30);
		
		popup.addWidget(text, 10, 30);		
		
		Binder.uniBind(filter(), text.text());
		
		Binder.uniBind(filter(), new Setter<String>() {
			@Override
			public void set(String value) {
				showPopup();
			}
		});
		
		// STYLE
		label.fill().set(new Color(255,255,255, 255));
		Insets bgInsets = new Insets(0, 0, 0, 0);
		final FillBackground bg = new FillBackground(
				new LinearGradient(0, 0, 0, 1, CoordMode.OBJECT_BOUNDING, Spread.PAD, 
					new Stop(0, new Color(0, 0, 0, 200)),
					new Stop(1, new Color(50, 50, 50, 200))
				), 
				new CornerRadii(10), bgInsets);
		popup.getAreaNode().background().set(bg);
		popup.getAreaNode().border().set(new Border(new BorderStroke(new Color(240, 240, 240, 255), new CornerRadii(10), new BorderWidths(3), bgInsets)));
		
		
		quickFilterPopup = new Popup(popup, false);
		quickFilterPopup.position().set(PopupPosition.CENTER);
	}
	
	private AtomicReference<Object> timeoutObject = new AtomicReference<Object>();
	private void showPopup() {
		final Object timeObj = new Object();
		timeoutObject.set(timeObj);
		
		w.showPopup(quickFilterPopup);
		
		Sync.get().execLaterOnUIThread(new Runnable() {
			@Override
			public void run() {
				if (timeoutObject.get() == timeObj) {
					hidePopup();
				}
			}
		}, 1000);
	}
	
	private void hidePopup()  {
		w.hidePopup(quickFilterPopup);
	}
	
	private Property<String> filter = new SimpleProperty<String>("");
	
	public Property<String> filter() {
		return filter;
	}
}
