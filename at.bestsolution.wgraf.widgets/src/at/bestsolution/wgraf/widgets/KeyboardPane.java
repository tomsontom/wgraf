package at.bestsolution.wgraf.widgets;

import at.bestsolution.wgraf.Application;
import at.bestsolution.wgraf.math.Rect;
import at.bestsolution.wgraf.properties.binding.Binder;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.DoubleChangeListener;
import at.bestsolution.wgraf.scene.Node;
import at.bestsolution.wgraf.transition.LinearDoubleTransition;

@Deprecated
public class KeyboardPane extends AbsolutePane {

	private ScrollPane contentArea;
	
	private TouchKeyboard keyboard;
	
	public KeyboardPane() {
		contentArea = new ScrollPane();
		contentArea.area.parent().set(area);
		contentArea.area.x().set(0);
		contentArea.area.y().set(0);
		
		Binder.uniBind(area.width(), contentArea.area.width());
		Binder.uniBind(area.height(), contentArea.area.height());
		
		keyboard = new TouchKeyboard();
		keyboard.area.parent().set(area);
		keyboard.area.y().setTransition(new LinearDoubleTransition(200));
		Application.get().focusNode().registerChangeListener(new ChangeListener<Node<?>>() {
			@Override
			public void onChange(Node<?> oldValue, Node<?> newValue) {
				if (newValue != null) {
//					if (newValue.inputMethod().get()) {
//						showKeyboard();
//						contentArea.scrollIntoViewport(newValue);
//					}
//					else {
//						hideKeyboard();
//					}
				}
				else {
					hideKeyboard();
					
				}
			}
		});
		
//		hideKeyboard();
		area.height().registerChangeListener(new DoubleChangeListener() {
			@Override
			public void onChange(double oldValue, double newValue) {
				hideKeyboard();
			}
		});
		
		area.width().registerChangeListener(new DoubleChangeListener() {
			@Override
			public void onChange(double oldValue, double newValue) {
				keyboard.area.x().set(newValue / 2 - keyboard.area.width().get() / 2);
			}
		});
		
	}
	
	private boolean keyboardVisible = false;
	
	private void showKeyboard() {
		System.err.println("SHOW KEYBOARD " + (area.height().get() - keyboard.area.height().get()));
		if (!keyboardVisible) {
			
			
			keyboard.area.y().setDynamic(area.height().get() - keyboard.area.height().get());
			contentArea.viewport().set(new Rect(0, 0, contentArea.area.width().get(), contentArea.area.height().get() - keyboard.area.height().get()));
			
			keyboardVisible = true;
		}
	}
	
	private void hideKeyboard() {
			keyboard.area.y().setDynamic(area.height().get());
			contentArea.viewport().set(null);
			
			keyboardVisible = false;
	}

	public void setContent(Widget content) {
		contentArea.setContent(content);
	}
	
}
