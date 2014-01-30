package at.bestsolution.wgraf.widgets;

import at.bestsolution.wgraf.properties.binding.Binder;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.binding.Setter;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.transition.LinearDoubleTransition;

public class StackPane extends Pane {

	private Property<Widget> current = new SimpleProperty<Widget>();
	
	public Property<Widget> current() {
		return current;
	}
	
	protected void updateChildWidth(double width) {
		for (Widget w : widgets) {
			w.width().set(width);
		}
	}
	
	protected void updateChildHeight(double height) {
		for (Widget w : widgets) {
			w.height().set(height);
		}
	}
	
	protected void updateCurrent(Widget oldWidget, Widget newWidget)  {
		double width = width().get();
		
		// hide all others
		for (Widget w : widgets) {
			if (w != oldWidget && w != newWidget) {
				w.x().set(-width);
			}
		}
		
		if (oldWidget != null && widgets.contains(oldWidget)) {
			oldWidget.area.x().setTransition(new LinearDoubleTransition(500));
			oldWidget.area.x().setDynamic(width);
		}
		
		if (newWidget != null && widgets.contains(newWidget)) {
			newWidget.area.x().setTransition(new LinearDoubleTransition(500));
			newWidget.area.x().set(-width);
			newWidget.area.x().setDynamic(0);
		}
	}
	
	public StackPane() {
		
		Binder.uniBind(width(), new Setter<Double>() {
			@Override
			public void set(Double value) {
				updateChildWidth(value);
			}
		});
		
		Binder.uniBind(height(), new Setter<Double>() {
			@Override
			public void set(Double value) {
				updateChildWidth(value);
			}
		});
		
		current.registerChangeListener(new ChangeListener<Widget>() {
			@Override
			public void onChange(Widget oldValue, Widget newValue) {
				updateCurrent(oldValue, newValue);
			}
		});
		
	}
	
//	@Override
//	public void addWidget(Widget w) {
//		super.addWidget(w);
//		w.x().set(-width().get());
//		w.y().set(-height().get());
//	}
	
}
