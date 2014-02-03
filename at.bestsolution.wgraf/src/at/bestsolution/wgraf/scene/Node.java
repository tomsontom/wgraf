package at.bestsolution.wgraf.scene;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import at.bestsolution.wgraf.Application;
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
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.style.Effect;

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
		
		parent.registerChangeListener(new ChangeListener<Container>() {
			@Override
			public void onChange(Container oldValue, Container newValue) {
				if (oldValue != null) {
					oldValue.getChildren().remove(this);
				}
				
				if (newValue != null) {
					newValue.getChildren().add(Node.this);
					backend.setParent(newValue.backend);
				}
				else {
					backend.setParent(null);
				}
			}
		});
		
		
	}
	
	public final Property<Shape> clippingShape() {
		return backend.clippingShape();
	}
	
	public final Property<Effect> effect() { return backend.effect(); }
	
	public final DoubleTransitionProperty x() { return backend.x(); }
	public final DoubleTransitionProperty y() { return backend.y(); }
	
	public final DoubleTransitionProperty opacity() { return backend.opacity(); }
	
	public final Property<Boolean> acceptTapEvents() { return backend.acceptTapEvents(); }
	
	private SignalListener<TapEvent> focusOnTap = new SignalListener<TapEvent>() {
		@Override
		public void onSignal(TapEvent data) {
			requestFocus();
		}
	};
	
	private Property<Boolean> acceptFocus = null;
	public final Property<Boolean> acceptFocus() { 
		if (acceptFocus == null) {
			acceptFocus = new SimpleProperty<Boolean>(false);
			acceptFocus.registerChangeListener(new ChangeListener<Boolean>() {
				@Override
				public void onChange(Boolean oldValue, Boolean newValue) {
					if (newValue)  {
						onTap().registerSignalListener(focusOnTap);
					}
					else {
						onTap().unregisterSignalListener(focusOnTap);
					}
				}
			});
		}
		return acceptFocus;
//		return backend.acceptFocus();
	}
	
	public Vec2d computePreferredSize() {
		return new Vec2d(10, 10);
	}
	
	public final Property<Boolean> cache() { return backend.cache(); }
	
	public final Signal<TapEvent> onTap() { return backend.onTap(); }
	public final Signal<TapEvent> onLongTap() { return backend.onLongTap(); }
	public final Signal<ScrollEvent> onScroll() { return backend.onScroll(); }
	public final Signal<FlingEvent> onFling() { return backend.onFling(); }
	
	public final Signal<KeyEvent> onKeyPress() { return backend.onKeyPress(); }
	
	private Property<Boolean> requireKeyboard = null;
	public final Property<Boolean> requireKeyboard() { 
		if (requireKeyboard == null) {
			requireKeyboard = new SimpleProperty<Boolean>(false);
		}
		return requireKeyboard;
	}
	
	
	private Property<Boolean> focus = null;
	public final ReadOnlyProperty<Boolean> focus() { 
		if (focus == null) {
			focus = new SimpleProperty<Boolean>(false);
		}
		return focus;
		//return backend.focus();
	}
	
	public final void requestFocus() {
		Application.get().requestFocus(this);
	}
	
	
	private Property<Container> parent = new SimpleProperty<Container>(null);
	public Property<Container> parent() {
		return parent;
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
