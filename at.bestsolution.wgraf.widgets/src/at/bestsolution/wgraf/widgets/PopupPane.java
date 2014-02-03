package at.bestsolution.wgraf.widgets;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import at.bestsolution.wgraf.properties.binding.Binder;
import at.bestsolution.wgraf.properties.binding.GroupBinding;
import at.bestsolution.wgraf.properties.binding.Setter;
import at.bestsolution.wgraf.properties.binding.Binding;
import at.bestsolution.wgraf.scene.Container;
import at.bestsolution.wgraf.transition.LinearDoubleTransition;
import at.bestsolution.wgraf.widgets.Popup.PopupPosition;


public class PopupPane extends AbsolutePane {

	Set<Popup> activePopups = new HashSet<Popup>();
	Map<Popup, Binding> activeBindings = new HashMap<Popup, Binding>();
	
	AbsolutePane contentArea;
	Container popupArea;
	Container absolutePopupArea;
	
	
	public PopupPane() {
		contentArea = new AbsolutePane();
		super.addWidget(contentArea, 0, 0);
		
		Binder.uniBind(area.width(), contentArea.area.width());
		Binder.uniBind(area.height(), contentArea.area.height());
		
		
		popupArea = new Container();
		popupArea.parent().set(area);
		popupArea.acceptTapEvents().set(false);
		
		// TODO XXX somehow the popup container 'eats' the javafx mouse events
		
		Binder.uniBind(area.width(), popupArea.width());
		Binder.uniBind(area.height(), popupArea.height());
		
		// TODO a popup which goes into absolutePopupArea hast to set the
		// viewport, also the popupArea needs to be constrainted to the
		// viewport
		
		absolutePopupArea = new Container();
		absolutePopupArea.parent().set(area);
		absolutePopupArea.acceptTapEvents().set(false);
		
		
		Binder.uniBind(area.width(), absolutePopupArea.width());
		Binder.uniBind(area.height(), absolutePopupArea.height());
		
	}
	
	@Override
	public void addWidget(Widget widget, double x, double y) {
		contentArea.addWidget(widget, x, y);
	}
	
//	@Override
//	public void addWidget(Widget w) {
//		contentArea.addWidget(w);
//	}
	
	@Override
	public void showPopup(final Popup popup) {
		System.err.println("showing popup");
		activePopups.add(popup);
		
		final Container pop = popup.getContent().area;
		
		final boolean squeeze = popup.squeezeViewport().get();
		
		final Container target =  squeeze? absolutePopupArea : popupArea;
		
		
		if (popup.position().get() == PopupPosition.BOTTOM_CENTER) {
			
			Setter<Double> xSetter = new Setter<Double>() {
				@Override
				public void set(Double value) {
					pop.x().setDynamic(target.width().get()/2 - pop.width().get()/2);
				}
			};
			Setter<Double> ySetter = new Setter<Double>() {
				@Override
				public void set(Double value) {
					pop.y().setTransition(new LinearDoubleTransition(300));
					pop.y().set(target.height().get());
					pop.y().setDynamic(target.height().get() - pop.height().get());
				}
			};
			
			GroupBinding gp = new GroupBinding();
			gp.registerBindings(
				Binder.uniBind(target.width(), xSetter),
				Binder.uniBind(pop.width(), xSetter),
			
				Binder.uniBind(target.height(), ySetter),
				Binder.uniBind(pop.height(), ySetter));
			
			activeBindings.put(popup, gp);
			
		}
		else if (popup.position().get() == PopupPosition.CENTER){
			
			Setter<Double> xSetter = new Setter<Double>() {
				@Override
				public void set(Double value) {
					pop.x().setDynamic(target.width().get()/2 - pop.width().get()/2);
				}
			};
			Setter<Double> ySetter = new Setter<Double>() {
				@Override
				public void set(Double value) {
					pop.y().setDynamic(target.height().get()/2 - pop.height().get()/2);
				}
			};
			
			GroupBinding gp = new GroupBinding();
			gp.registerBindings(
				Binder.uniBind(target.width(), xSetter),
				Binder.uniBind(pop.width(), xSetter),
			
				Binder.uniBind(target.height(), ySetter),
				Binder.uniBind(pop.height(), ySetter));
			
			activeBindings.put(popup, gp);
		}
		
		pop.parent().set(target);
		
//		if (squeeze) {
//			contentArea.restrictViewport()
//		}
	}
	
	@Override
	public void hidePopup(Popup popup) {
		System.err.println("hiding popup");
		activePopups.remove(popup);
		Binding b = activeBindings.remove(popup);
		if (b != null) b.dispose();
		popup.getContent().area.parent().set(null);
	}
	
	@Override
	public void hideAllPopups() {
		Iterator<Popup> it = activePopups.iterator();
		while (it.hasNext()) {
			Popup c = it.next();
			c.getContent().area.parent().set(null);
			it.remove();
		}
	}
	
	@Override
	public boolean isPopupVisible(Popup popup) {
		return activePopups.contains(popup);
	}
	
}
