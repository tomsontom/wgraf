package at.bestsolution.wgraf.scene;

import at.bestsolution.wgraf.events.FlingEvent;
import at.bestsolution.wgraf.events.KeyEvent;
import at.bestsolution.wgraf.events.MouseEventSupport;
import at.bestsolution.wgraf.events.ScrollEvent;
import at.bestsolution.wgraf.events.TapEvent;
import at.bestsolution.wgraf.geom.shape.Shape;
import at.bestsolution.wgraf.properties.DoubleTransitionProperty;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.ReadOnlyProperty;
import at.bestsolution.wgraf.properties.Signal;
import at.bestsolution.wgraf.style.Effect;

public interface BackingNode {

	DoubleTransitionProperty x();
	DoubleTransitionProperty y();
	
	DoubleTransitionProperty opacity();
	
	Property<Shape> clippingShape();
	
	void setParent(BackingContainer parent);
	
	void setEventSupport(MouseEventSupport support);
	
	Property<Boolean> acceptTapEvents();
	Property<Boolean> acceptFocus();
	
	Property<Effect> effect();
	
	Property<Boolean> cache();

	
	Signal<KeyEvent> onKeyPress();
	
	ReadOnlyProperty<Boolean> focus();
	
	
	Signal<TapEvent> onTap();
	Signal<TapEvent> onLongTap();
	Signal<ScrollEvent> onScroll();
	Signal<FlingEvent> onFling();
}
