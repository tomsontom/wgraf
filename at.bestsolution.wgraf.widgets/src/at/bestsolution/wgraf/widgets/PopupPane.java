package at.bestsolution.wgraf.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import at.bestsolution.wgraf.events.TapEvent;
import at.bestsolution.wgraf.geom.shape.Rectangle;
import at.bestsolution.wgraf.math.Rect;
import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.InvalidValueException;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.properties.binding.Binder;
import at.bestsolution.wgraf.properties.binding.GroupBinding;
import at.bestsolution.wgraf.properties.binding.Setter;
import at.bestsolution.wgraf.properties.binding.Binding;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.scene.Container;
import at.bestsolution.wgraf.style.Background;
import at.bestsolution.wgraf.style.CornerRadii;
import at.bestsolution.wgraf.style.FillBackground;
import at.bestsolution.wgraf.style.Insets;
import at.bestsolution.wgraf.transition.LinearDoubleTransition;
import at.bestsolution.wgraf.widgets.Popup.PopupPosition;


public class PopupPane extends AbsolutePane {

	Stack<Popup> modalPopups = new Stack<Popup>();
	
	Set<Popup> activePopups = new HashSet<Popup>();
	Map<Popup, Binding> activeBindings = new HashMap<Popup, Binding>();
	
	AbsolutePane contentArea;
	Container popupArea;
	Container absolutePopupArea;
	
	private SignalListener<TapEvent> tapCatch = new SignalListener<TapEvent>() {
		@Override
		public void onSignal(TapEvent data) {
			if (!modalPopups.isEmpty()) {
				Popup cur = modalPopups.peek();
				hidePopup(cur);
				cur.onCancel().signal(null);
				
			}
			data.consume();
		}
	};
	
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
	
	private Property<Rect> popupAreaViewport = new SimpleProperty<Rect>(null);
	
	@Override
	protected void restrictViewport(Rect viewport) {
		super.restrictViewport(viewport);
		
		// restrict viewport for popupArea
		popupAreaViewport.set(viewport);;
	}
	
	private void updateViewport() {
		
	}
	
	@Override
	public void showPopup(final Popup popup) {
		System.err.println("showing popup");
		activePopups.add(popup);
		
		final Container pop = popup.getContent().area;
		
		final boolean squeeze = popup.squeezeViewport().get();
		
		final Container target =  squeeze? absolutePopupArea : popupArea;
		
		pop.opacity().setTransition(new LinearDoubleTransition(300));
		pop.opacity().set(0);
		pop.opacity().setDynamic(1);
		
		if (popup.modal) {
			if (modalPopups.isEmpty()) {
				popupArea.background().set(new FillBackground(new Color(255, 255, 255, 200), CornerRadii.NO_CORNER_RADII, Insets.NO_INSETS));
				popupArea.onTap().registerSignalListener(tapCatch);
			}
			modalPopups.push(popup);
		}
		
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
		else if (popup.position().get() == PopupPosition.BOTTOM_RIGHT) {

			Setter<Double> xSetter = new Setter<Double>() {
				@Override
				public void set(Double value) {
					pop.x().setDynamic(target.width().get() - pop.width().get());
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
			
			final Setter<Double> xSetter = new Setter<Double>() {
				@Override
				public void set(Double value) {
					Rect vp = popupAreaViewport.get();
					if (vp == null) {
						pop.x().setDynamic(target.width().get()/2 - pop.width().get()/2);
					}
					else {
						pop.x().setDynamic(vp.x + vp.width/2 - pop.width().get()/2);
					}
				}
			};
			final Setter<Double> ySetter = new Setter<Double>() {
				@Override
				public void set(Double value) {
					Rect vp = popupAreaViewport.get();
					if (vp == null) {
						pop.y().setDynamic(target.height().get()/2 - pop.height().get()/2);
					}
					else {
						pop.y().setDynamic(vp.y + vp.height/2 - pop.height().get()/2);
					}
				}
			};
			Setter<Rect> viewPortSetter = new Setter<Rect>() {
				@Override
				public void set(Rect value) {
					xSetter.set(0d);
					ySetter.set(0d);
				}
			};
			
			GroupBinding gp = new GroupBinding();
			gp.registerBindings(
				Binder.uniBind(target.width(), xSetter),
				Binder.uniBind(pop.width(), xSetter),
			
				Binder.uniBind(target.height(), ySetter),
				Binder.uniBind(pop.height(), ySetter));
			
				Binder.uniBind(popupAreaViewport, viewPortSetter);
			activeBindings.put(popup, gp);
		}
		
		pop.parent().set(target);
		
		pop.y().setTransition(new LinearDoubleTransition(300));
		pop.x().setTransition(new LinearDoubleTransition(300));
		
//		if (squeeze) {
//			contentArea.restrictViewport()
//		}
	}
	
	@Override
	public void hidePopup(final Popup popup) {
		if (activePopups.contains(popup)) {
			System.err.println("hiding popup");
			
			popup.getContent().getAreaNode().opacity().setTransition(new LinearDoubleTransition(300));
			popup.getContent().getAreaNode().opacity().set(1);
			popup.getContent().getAreaNode().opacity().setDynamic(0);
			
			popup.getContent().getAreaNode().opacity().transitionActive().registerChangeListener(new ChangeListener<Boolean>() {
				@Override
				public void onChange(Boolean oldValue, Boolean newValue)
						throws InvalidValueException {
					if (!newValue) {
						popup.getContent().area.parent().set(null);
					}
					
				}
			});
			if (popup.modal) {
				modalPopups.remove(popup);
				if (modalPopups.isEmpty()) {
					popupArea.background().set(null);
					popupArea.onTap().unregisterSignalListener(tapCatch);
					
				}
			}
			activePopups.remove(popup);
			Binding b = activeBindings.remove(popup);
			if (b != null) b.dispose();
		}
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
