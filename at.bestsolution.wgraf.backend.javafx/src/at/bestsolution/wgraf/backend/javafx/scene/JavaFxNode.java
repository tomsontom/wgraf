package at.bestsolution.wgraf.backend.javafx.scene;


import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import at.bestsolution.wgraf.backend.javafx.JavaFxBinder;
import at.bestsolution.wgraf.backend.javafx.scene.JavaFxContainer.FxRegion;
import at.bestsolution.wgraf.events.FlingEvent;
import at.bestsolution.wgraf.events.MouseEventSupport;
import at.bestsolution.wgraf.events.MouseEventSupport.MouseCoords;
import at.bestsolution.wgraf.events.ScrollEvent;
import at.bestsolution.wgraf.events.TapEvent;
import at.bestsolution.wgraf.geom.shape.Rectangle;
import at.bestsolution.wgraf.geom.shape.Shape;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.DoubleTransitionProperty;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.ReadOnlyProperty;
import at.bestsolution.wgraf.properties.Signal;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.properties.simple.SimpleDoubleTransitionProperty;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.properties.simple.SimpleSignal;
import at.bestsolution.wgraf.scene.BackingContainer;
import at.bestsolution.wgraf.scene.BackingNode;

public abstract class JavaFxNode<N extends Node> implements BackingNode {
	
	private MouseEventSupport support;
	
	protected final N node;
	
	public JavaFxNode() {
		node = createNode();
//		node.setBlendMode(BlendMode.COLOR_BURN);
		
		node.setFocusTraversable(false);
		
	}
	
	protected abstract N createNode();
	
	public N getNode() {
		return node;
	}
	
	private Property<Boolean> cache = null;
	@Override
	public Property<Boolean> cache() {
		if (cache == null) {
			cache = new SimpleProperty<Boolean>(false);
			JavaFxBinder.uniBind(cache, new JavaFxBinder.JfxSetter<Boolean>() {
				@Override
				public void doSet(Boolean value) {
					node.setCache(value);
					node.setCacheHint(CacheHint.SPEED);
				}
			});
		}
		return cache;
	}
	
	private Property<Boolean> focus = null;
	@Override
	public ReadOnlyProperty<Boolean> focus() {
		if (focus == null) {
			focus = new SimpleProperty<Boolean>();
			node.focusedProperty().addListener(new javafx.beans.value.ChangeListener<Boolean>() {
				@Override
				public void changed(
						ObservableValue<? extends Boolean> observable,
						Boolean oldValue, Boolean newValue) {
					focus.set(newValue);
				}
			});
		}
		return focus;
	}
	
	private Property<Boolean> acceptFocus = null;
	@Override
	public Property<Boolean> acceptFocus() {
		if (acceptFocus == null) {
			acceptFocus = new SimpleProperty<Boolean>(false);
			JavaFxBinder.uniBind(acceptFocus, new JavaFxBinder.JfxSetter<Boolean>() {
				@Override
				public void doSet(Boolean value) {
					node.setFocusTraversable(value);
				}
			});
		}
		return acceptFocus;
	}
	
	private final SignalListener<TapEvent> focusListener = new SignalListener<TapEvent>() {
		@Override
		public void onSignal(TapEvent data) {
			node.requestFocus();
		}
	};
	
	private Property<Boolean> acceptTapEvents = null;
	
	@Override
	public Property<Boolean> acceptTapEvents() {
		if (acceptTapEvents == null) {
			acceptTapEvents = new SimpleProperty<Boolean>(false);
			JavaFxBinder.uniBind(acceptTapEvents, new JavaFxBinder.JfxSetter<Boolean>() {
				@Override
				public void doSet(Boolean value) {
					node.setMouseTransparent(!value);
				}
			});
			
			acceptTapEvents.registerChangeListener(new ChangeListener<Boolean>() {
				@Override
				public void onChange(Boolean oldValue, Boolean newValue) {
					if (newValue) {
						support.tap().registerSignalListener(focusListener);
					}
					else {
						support.tap().unregisterSignalListener(focusListener);
					}
				}
			});
		}
		return acceptTapEvents;
	}
	
	
	@Override
	public void setEventSupport(final MouseEventSupport support) {
		if (this.support != null) return;
		
		this.support = support;
		node.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(final MouseEvent event) {
				support.mousePressed(new MouseCoords(event.getX(), event.getY()), new Runnable() {
					@Override
					public void run() {
						event.consume();
					}
				});
//				support.mousePressed().signal(new MouseCoords(event.getX(), event.getY()));
			}
		});
		
		node.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(final MouseEvent event) {
//				support.mouseReleased().signal(new MouseCoords(event.getX(), event.getY()));
				support.mouseReleased(new MouseCoords(event.getX(), event.getY()), new Runnable() {
					@Override
					public void run() {
						event.consume();
					}
				});
			}
		});
		
		node.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(final MouseEvent event) {
//				support.mouseDragged().signal(new MouseCoords(event.getX(), event.getY()));
				support.mouseDragged(new MouseCoords(event.getX(), event.getY()), new Runnable() {
					@Override
					public void run() {
						event.consume();
					}
				});
			}
		});
		
	}
	
	@Override
	public Signal<TapEvent> onTap() {
		return support.tap();
	}
	
	@Override
	public Signal<TapEvent> onLongTap() {
		return support.longTap();
	}
	
	@Override
	public Signal<ScrollEvent> onScroll() {
		return support.scroll();
	}
	
	@Override
	public Signal<FlingEvent> onFling() {
		return support.fling();
	}
	
	private DoubleTransitionProperty x = null;
	@Override
	public DoubleTransitionProperty x() {
		if (x == null) {
			x = new SimpleDoubleTransitionProperty(node.getLayoutX());
			JavaFxBinder.uniBind(x, new JavaFxBinder.JfxSetter<Double>() {
				@Override
				public void doSet(Double value) {
					node.setLayoutX(value);
				}
			});
		}
		return x;
	}
	
	private DoubleTransitionProperty y = null;
	@Override
	public DoubleTransitionProperty y() {
		if (y == null) {
			y = new SimpleDoubleTransitionProperty(node.getLayoutY());
			JavaFxBinder.uniBind(y, new JavaFxBinder.JfxSetter<Double>() {
				@Override
				public void doSet(Double value) {
					node.setLayoutY(value);
				}
			});
		}
		return y;
	}
	
	
	@Override
	public void setParent(BackingContainer parent) {
		if (parent == null) {
			FxRegion p = (FxRegion) node.getParent();
			p.getChildren().remove(node);
		}
		else {
			JavaFxContainer p = (JavaFxContainer) parent;
			p.addChild(node);
		}
	}

	
	private Property<Shape> clippingShape = null;
	@Override
	public Property<Shape> clippingShape() {
		if (clippingShape == null) {
			clippingShape = new SimpleProperty<Shape>();
			JavaFxBinder.uniBind(clippingShape, new JavaFxBinder.JfxSetter<Shape>() {
				@Override
				public void doSet(Shape value) {
					if (value == null) {
						node.setClip(null);
					}
					if (value instanceof Rectangle) {
						
						javafx.scene.shape.Rectangle jfx = 
								new javafx.scene.shape.Rectangle(((Rectangle) value).x, ((Rectangle) value).y,
										((Rectangle) value).width, ((Rectangle) value).height);
						jfx.setArcWidth(((Rectangle) value).r*2);
						jfx.setArcHeight(((Rectangle) value).r*2);
						
						node.setClip(jfx);
						
					}
					
				}
			});
		}
		return clippingShape;
	}
}
