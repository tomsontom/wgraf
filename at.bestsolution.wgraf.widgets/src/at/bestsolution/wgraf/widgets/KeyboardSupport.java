package at.bestsolution.wgraf.widgets;

import at.bestsolution.wgraf.Application;
import at.bestsolution.wgraf.math.Rect;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.scene.Node;
import at.bestsolution.wgraf.widgets.popups.FullKeyboard;

public class KeyboardSupport {

	private boolean keyboardVisible;
	
	private FullKeyboard fullKeyboard;
	
	private Pane anyPane;
	
	public KeyboardSupport(Pane anyPane) {
		this.anyPane = anyPane;
		fullKeyboard = new FullKeyboard();
		
		Application.get().focusNode().registerChangeListener(new ChangeListener<Node<?>>() {
			@Override
			public void onChange(Node<?> oldValue, Node<?> newValue) {
				if (newValue != null) {
					if (newValue.requireKeyboard().get()) {
						showKeyboard();
//						contentArea.scrollIntoViewport(newValue);
					}
					else {
						hideKeyboard();
					}
				}
				else {
					hideKeyboard();
					
				}
			}
		});
	}
	
	private void showKeyboard() {
//		System.err.println("SHOW KEYBOARD " + (area.height().get() - keyboard.area.height().get()));
		if (!keyboardVisible) {
			anyPane.showPopup(fullKeyboard);
//			
//			keyboard.area.y().setDynamic(area.height().get() - keyboard.area.height().get());
//			contentArea.viewport().set(new Rect(0, 0, contentArea.area.width().get(), contentArea.area.height().get() - keyboard.area.height().get()));
//			
			keyboardVisible = true;
		}
	}
	
	private void hideKeyboard() {
			anyPane.hidePopup(fullKeyboard);
//			keyboard.area.y().setDynamic(area.height().get());
//			contentArea.viewport().set(null);
//			
			keyboardVisible = false;
	}
	
	
}