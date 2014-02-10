package at.bestsolution.wgraf.widgets;

import at.bestsolution.wgraf.Application;
import at.bestsolution.wgraf.math.Rect;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.binding.Binder;
import at.bestsolution.wgraf.properties.binding.GroupBinding;
import at.bestsolution.wgraf.properties.binding.Setter;
import at.bestsolution.wgraf.scene.Node;
import at.bestsolution.wgraf.widgets.popups.FullKeyboard;
import at.bestsolution.wgraf.widgets.popups.NumblockKeyboard;

public class KeyboardSupport {

	private Popup keyboardVisible = null;
	
	private FullKeyboard fullKeyboard;
	private NumblockKeyboard numKeyboard;
	
	private Pane anyPane;
	
	public KeyboardSupport(Pane anyPane) {
		this.anyPane = anyPane;
		fullKeyboard = new FullKeyboard();
		numKeyboard = new NumblockKeyboard();
		
		Application.get().focusNode().registerChangeListener(new ChangeListener<Node<?>>() {
			@Override
			public void onChange(Node<?> oldValue, Node<?> newValue) {
				if (newValue != null) {
					switch(newValue.inputMethod().get()) {
					case NONE: 
						hideKeyboard();
						break;
					
					case NUM_ANY:
					case NUM_DECIMAL:
					case NUM_INTEGER:
						showNumKeyboard();
						KeyboardSupport.this.anyPane.scrollIntoViewport(newValue);
						break;
						
					default:
						showKeyboard();
						KeyboardSupport.this.anyPane.scrollIntoViewport(newValue);
						break;
					}
				}
				else {
					hideKeyboard();
					
				}
			}
		});
	}
	
	GroupBinding vpBinding = new GroupBinding();
	
	private void showNumKeyboard() {
		
		if (keyboardVisible != numKeyboard) {
			if (keyboardVisible != null) {
				anyPane.hidePopup(keyboardVisible);
			}
			anyPane.showPopup(numKeyboard);
//			
//			keyboard.area.y().setDynamic(area.height().get() - keyboard.area.height().get());
			
			vpBinding.registerBindings(
					Binder.uniBind(anyPane.width(), new Setter<Double>() {
						@Override
						public void set(Double value) {
							updateViewport(numKeyboard);
						}
					}),
					Binder.uniBind(anyPane.height(), new Setter<Double>() {
						@Override
						public void set(Double value) {
							updateViewport(numKeyboard);
						}
					}));
			
			keyboardVisible = numKeyboard;
		}
	}
	
	private void showKeyboard() {
//		System.err.println("SHOW KEYBOARD " + (area.height().get() - keyboard.area.height().get()));
		if (keyboardVisible != fullKeyboard) {
			if (keyboardVisible != null) {
				anyPane.hidePopup(keyboardVisible);
			}
			anyPane.showPopup(fullKeyboard);
//			
//			keyboard.area.y().setDynamic(area.height().get() - keyboard.area.height().get());
			
			vpBinding.registerBindings(
					Binder.uniBind(anyPane.width(), new Setter<Double>() {
						@Override
						public void set(Double value) {
							updateViewport(fullKeyboard);
						}
					}),
					Binder.uniBind(anyPane.height(), new Setter<Double>() {
						@Override
						public void set(Double value) {
							updateViewport(fullKeyboard);
						}
					}));
			
			keyboardVisible = fullKeyboard;
		}
	}
	
	private void updateViewport(Popup kb) {
		anyPane.restrictViewport(new Rect(0, 0, anyPane.area.width().get(), anyPane.area.height().get() - kb.getContent().area.height().get()));
	}
	
	public void hideKeyboard() {
		if (keyboardVisible != null) {
			anyPane.hidePopup(keyboardVisible);
//			keyboard.area.y().setDynamic(area.height().get());
			
			vpBinding.dispose();
			anyPane.restrictViewport(null);
//			
			
			
			keyboardVisible = null;
		}
	}
	
	
}
