package at.bestsolution.wgraf.scene;

import at.bestsolution.wgraf.events.KeyEvent;
import at.bestsolution.wgraf.events.MouseEventSupport;
import at.bestsolution.wgraf.geom.shape.Shape;
import at.bestsolution.wgraf.properties.DoubleTransitionProperty;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.ReadOnlyProperty;
import at.bestsolution.wgraf.properties.Signal;

public interface BackingNode {

	DoubleTransitionProperty x();
	DoubleTransitionProperty y();
	
	Property<Shape> clippingShape();
	
	void setParent(BackingContainer parent);
	
	void setEventSupport(MouseEventSupport support);
	
	Property<Boolean> acceptTapEvents();
	Property<Boolean> acceptFocus();
	
	
	Signal<KeyEvent> onKeyPress();
	
	ReadOnlyProperty<Boolean> focus();
}
