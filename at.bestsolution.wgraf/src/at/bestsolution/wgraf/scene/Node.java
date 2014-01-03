package at.bestsolution.wgraf.scene;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import at.bestsolution.wgraf.Frontend;
import at.bestsolution.wgraf.events.FlingEvent;
import at.bestsolution.wgraf.events.KeyEvent;
import at.bestsolution.wgraf.events.MouseEventSupport;
import at.bestsolution.wgraf.events.ScrollEvent;
import at.bestsolution.wgraf.events.TapEvent;
import at.bestsolution.wgraf.geom.shape.Shape;
import at.bestsolution.wgraf.math.Vec2d;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.DoubleTransitionProperty;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.ReadOnlyProperty;
import at.bestsolution.wgraf.properties.Signal;
import at.bestsolution.wgraf.properties.TransitionProperty;

public abstract class Node<Backend extends BackingNode> extends Frontend<Backend>{

	private List<String> styleClasses = new CopyOnWriteArrayList<String>();
	
	public void addStyleClass(String cls) {
		styleClasses.add(cls);
	}
	public void removeStyleClass(String cls) {
		styleClasses.remove(cls);
	}
	
	private MouseEventSupport events = new MouseEventSupport();
	
	public Node() {
		backend.setEventSupport(events);
		
		// debugging
		focus().registerChangeListener(new ChangeListener<Boolean>() {
			@Override
			public void onChange(Boolean oldValue, Boolean newValue) {
				if (newValue) {
					System.err.println("Focus Control: " + Node.this);
				}
			}
		});
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
	
	public final ReadOnlyProperty<Boolean> focus() { return backend.focus(); }
	
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
	
	@Override
	public String toString() {
		String r = getClass().getSimpleName();
		for (String cls : styleClasses) {
			r += "." + cls;
		}
		r+= "@" + System.identityHashCode(this);
		return  r;
	}
	
	
}
