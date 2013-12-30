package at.bestsolution.wgraf.scene;

import at.bestsolution.wgraf.Frontend;
import at.bestsolution.wgraf.events.FlingEvent;
import at.bestsolution.wgraf.events.MouseEventSupport;
import at.bestsolution.wgraf.events.ScrollEvent;
import at.bestsolution.wgraf.events.TapEvent;
import at.bestsolution.wgraf.math.Vec2d;
import at.bestsolution.wgraf.properties.Signal;
import at.bestsolution.wgraf.properties.TransitionProperty;

public abstract class Node<Backend extends BackingNode> extends Frontend<Backend>{

	private MouseEventSupport events = new MouseEventSupport();
	
	public Node() {
		backend.setEventSupport(events);
	}
	
	private Container parent;
	
	public final TransitionProperty<Double> x() { return backend.x(); }
	public final TransitionProperty<Double> y() { return backend.y(); }
	
	
	public Vec2d computePreferredSize() {
		return new Vec2d(10, 10);
	}
	
	public final Signal<TapEvent> onTap() { return events.tap(); }
	public final Signal<TapEvent> onLongTap() { return events.longTap(); }
	public final Signal<ScrollEvent> onScroll() { return events.scroll(); }
	public final Signal<FlingEvent> onFling() { return events.fling(); }
	
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
