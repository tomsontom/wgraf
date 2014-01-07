package at.bestsolution.wgraf.widgets;

import at.bestsolution.wgraf.math.Vec2d;
import at.bestsolution.wgraf.properties.ValueUpdate;
import at.bestsolution.wgraf.transition.LinearDoubleTransition;
import at.bestsolution.wgraf.transition.Transition;
import at.bestsolution.wgraf.transition.ValueReader;
import at.bestsolution.wgraf.transition.ValueUpdater;


public class PopupPane extends AbsolutePane {

	Widget currentPopup;
	
	public PopupPane() {
		
	}
	
	public void showModal(Vec2d offset, Widget popup) {
		if (currentPopup == null) {
			currentPopup = popup;
			currentPopup.area.x().set(offset.x);
			currentPopup.area.y().set(offset.y);
			
			double height = currentPopup.area.height().get();
			currentPopup.area.height().set(0);
			currentPopup.area.height().setTransition(new LinearDoubleTransition(200));
			currentPopup.area.parent().set(area);
			currentPopup.area.height().setDynamic(height);
		}
	}
	
	@Override
	public void showPopup(Vec2d offset, Widget popup) {
		showModal(offset, popup);
	}
	
	@Override
	public void hidePopup() {
		hide();
	}
	
	@Override
	public boolean isPopupVisible() {
		return currentPopup != null;
	}
	
	public void hide() {
		if (currentPopup != null) {
			currentPopup.area.parent().set(null);
			currentPopup = null;
		}
	}
}
