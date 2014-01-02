package at.bestsolution.wgraf.scene;

import at.bestsolution.wgraf.Frontend;
import at.bestsolution.wgraf.events.FlingEvent;
import at.bestsolution.wgraf.events.KeyEvent;
import at.bestsolution.wgraf.events.MouseEventSupport;
import at.bestsolution.wgraf.events.ScrollEvent;
import at.bestsolution.wgraf.events.TapEvent;
import at.bestsolution.wgraf.geom.shape.Shape;
import at.bestsolution.wgraf.math.Vec2d;
import at.bestsolution.wgraf.properties.DoubleTransitionProperty;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.Signal;
import at.bestsolution.wgraf.properties.TransitionProperty;

public abstract class Node<Backend extends BackingNode> extends Frontend<Backend>{

	private MouseEventSupport events = new MouseEventSupport();
	
	public Node() {
		backend.setEventSupport(events);
	}
	
	public final Property<Shape> clippingShape() {
		return backend.clippingShape();
	}
	
	private Container parent;
	
	public final DoubleTransitionProperty x() { return backend.x(); }
	public final DoubleTransitionProperty y() { return backend.y(); }
	
	
	public final Property<Boolean> acceptTapEvents() { return backend.acceptTapEvents(); }
	public final Property<Boolean> acceptFocus() { return backend.acceptFocus(); }
	
	public Vec2d computePreferredSize() {
		return new Vec2d(10, 10);
	}
	
	public final Signal<TapEvent> onTap() { return events.tap(); }
	public final Signal<TapEvent> onLongTap() { return events.longTap(); }
	public final Signal<ScrollEvent> onScroll() { return events.scroll(); }
	public final Signal<FlingEvent> onFling() { return events.fling(); }
	
	public final Signal<KeyEvent> onKeyPress() { return backend.onKeyPress(); }
	
	public final Signal<Boolean> focus() { return backend.focus(); }
	
	public Container getParent() {
		return parent;
	}
	
	public void setParent(Container parent) {
		this.parent = parent;
		if (this.parent != null) {
			this.parent.getChildren().remove(this);
		}
		parent.getChildren().add(this);
		
		backend.setParent(parent.backend);
	}
	
	
}
