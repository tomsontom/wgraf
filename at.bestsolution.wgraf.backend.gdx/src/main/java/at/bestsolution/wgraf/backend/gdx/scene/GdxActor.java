package at.bestsolution.wgraf.backend.gdx.scene;

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
import at.bestsolution.wgraf.properties.binding.Binder;
import at.bestsolution.wgraf.properties.binding.Setter;
import at.bestsolution.wgraf.properties.simple.SimpleDoubleTransitionProperty;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.properties.simple.SimpleSignal;
import at.bestsolution.wgraf.scene.BackingContainer;
import at.bestsolution.wgraf.scene.BackingNode;
import at.bestsolution.wgraf.style.Effect;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

public class GdxActor<A extends Actor> implements BackingNode {

	private A actor;
	
	public GdxActor(A actor) {
		this.actor = actor;
	}
	
	public A getActor() {
		return actor;
	}
	
	private DoubleTransitionProperty x;
	@Override
	public DoubleTransitionProperty x() {
		if (x == null) {
			x = new SimpleDoubleTransitionProperty();
			Binder.uniBind(x, new Setter<Double>() {
				@Override
				public void set(Double value) {
					System.err.println("setting x to " + value + "  " + actor+ "#" + System.identityHashCode(actor));
					actor.setX(value.floatValue());
				}
			});
		}
		return x;
	}

	private DoubleTransitionProperty y;
	@Override
	public DoubleTransitionProperty y() {
		if (y == null) {
			y = new SimpleDoubleTransitionProperty();
			Binder.uniBind(y, new Setter<Double>() {
				@Override
				public void set(Double value) {
					System.err.println("setting y to " + value + " on " + actor + "#" + System.identityHashCode(actor));
					actor.setY(value.floatValue());
				}
			});
		}
		return y;
	}

	private DoubleTransitionProperty opacity;
	@Override
	public DoubleTransitionProperty opacity() {
		if (opacity == null) {
			opacity = new SimpleDoubleTransitionProperty();
		}
		return opacity;
	}

	private Property<Shape> clippingShape;
	@Override
	public Property<Shape> clippingShape() {
		if (clippingShape == null) {
			clippingShape = new SimpleProperty<Shape>();
		}
		return clippingShape;
	}

	@Override
	public void setParent(BackingContainer parent) {
		Group p = ((GdxContainer)parent).getActor();
		p.addActor(actor);
	}

	@Override
	public void setEventSupport(MouseEventSupport support) {
		// TODO Auto-generated method stub

	}

	private Property<Boolean> acceptTapEvents;
	@Override
	public Property<Boolean> acceptTapEvents() {
		if (acceptTapEvents == null) {
			acceptTapEvents = new SimpleProperty<Boolean>();
			// TODO
		}
		return acceptTapEvents;
	}

	private Property<Boolean> acceptFocus;
	@Override
	public Property<Boolean> acceptFocus() {
		if (acceptFocus == null) {
			acceptFocus = new SimpleProperty<Boolean>();
		}
		return acceptFocus;
	}

	private Property<Effect> effect;
	@Override
	public Property<Effect> effect() {
		if (effect == null) {
			effect = new SimpleProperty<Effect>();
		}
		return effect;
	}

	private Property<Boolean> cache;
	@Override
	public Property<Boolean> cache() {
		if (cache == null) {
			cache = new SimpleProperty<Boolean>();
		}
		return cache;
	}

	private Signal<KeyEvent> onKeyPress;
	@Override
	public Signal<KeyEvent> onKeyPress() {
		if (onKeyPress == null) {
			onKeyPress = new SimpleSignal<KeyEvent>();
		}
		return onKeyPress;
	}

	private Property<Boolean> focus;
	@Override
	public ReadOnlyProperty<Boolean> focus() {
		// TODO Auto-generated method stub
		return null;
	}

	private Signal<TapEvent> onTap;
	@Override
	public Signal<TapEvent> onTap() {
		if (onTap == null) {
			onTap = new SimpleSignal<TapEvent>();
		}
		return onTap;
	}

	private Signal<TapEvent> onLongTap;
	@Override
	public Signal<TapEvent> onLongTap() {
		if (onLongTap == null) {
			onLongTap = new SimpleSignal<TapEvent>();
		}
		return onLongTap;
	}

	private Signal<ScrollEvent> onScroll;
	@Override
	public Signal<ScrollEvent> onScroll() {
		if (onScroll == null) {
			onScroll = new SimpleSignal<ScrollEvent>();
		}
		return onScroll;
	}

	private Signal<FlingEvent> onFling;
	@Override
	public Signal<FlingEvent> onFling() {
		if (onFling == null) {
			onFling = new SimpleSignal<FlingEvent>();
		}
		return onFling;
	}

}
