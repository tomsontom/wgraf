package at.bestsolution.wgraf.widgets;

import at.bestsolution.wgraf.events.ScrollEvent;
import at.bestsolution.wgraf.geom.shape.Rectangle;
import at.bestsolution.wgraf.math.Rect;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.ClampedDoubleIncrement;
import at.bestsolution.wgraf.properties.DoubleChangeListener;
import at.bestsolution.wgraf.properties.DoubleProperty;
import at.bestsolution.wgraf.properties.GroupBinding;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.properties.ValueUpdate;
import at.bestsolution.wgraf.properties.binding.Binder;
import at.bestsolution.wgraf.properties.binding.Setter;
import at.bestsolution.wgraf.properties.simple.SimpleDoubleProperty;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.scene.Node;
import at.bestsolution.wgraf.transition.TouchScrollTransition;

// TODO scrollbar visibility - hide scrollbars and only show them while scrolling
// TODO allow only vertical or horizontal scrollbar
public class ScrollPane extends Pane {

	protected Widget content;
	
	private ScrollBar hBar;
	private ScrollBar vBar;
	
	private boolean fitVertically = false;
	private boolean fitHorizontally = false;
	
	private GroupBinding contentBinding = new GroupBinding();
	
	private double minOffsetHorizontal;
	private double maxOffsetHorizontal;
	private double minOffsetVertical;
	private double maxOffsetVertical;
	
	private DoubleProperty viewportX = new SimpleDoubleProperty();
	private DoubleProperty viewportY = new SimpleDoubleProperty();
	private DoubleProperty viewportWidth = new SimpleDoubleProperty();
	private DoubleProperty viewportHeight = new SimpleDoubleProperty();
	
	private Property<Rect> realViewport = new SimpleProperty<Rect>();
	
	private Property<Rect> viewport = new SimpleProperty<Rect>(null);
	/**
	 * the viewport of the scroll pane.
	 * <p>if set to null, the viewport is the whole pane</p>
	 * @return
	 */
	public Property<Rect> viewport() {
		return viewport;
	}
	
	private Rect getBoundingRect() {
		return new Rect(0, 0, area.width().get(), area.height().get());
	}
	
	private GroupBinding widgetBindings = new GroupBinding();
	
	public void scrollIntoViewport(final Rect r) {
		
		content.area.x().updateDynamic(new ValueUpdate<Double>() {
			@Override
			public Double update(Double current) {
				double xOffset = current;
				double vpX = viewportX.get();
				double vpWidth = viewportWidth.get();
				
				double left = vpX - xOffset;
				double right = vpX + vpWidth - xOffset;
				
				System.err.println("left = " + left);
				System.err.println("right = " + right);
				
				double resultX = -xOffset;
				if (left > r.x) resultX = r.x;
				if (right < r.x + r.width) resultX = r.x + r.width - vpWidth;
				
				return -resultX;
			}
		});
		
		content.area.y().updateDynamic(new ValueUpdate<Double>() {
			@Override
			public Double update(Double current) {
		
				double yOffset = current;
				double vpY = viewportY.get();
				double vpHeight = viewportHeight.get();
				
				double top = vpY - yOffset;
				double bottom = vpY + vpHeight - yOffset;
				
				System.err.println("top = " + top);
				System.err.println("bottom = " + bottom);
				
				double resultY = -yOffset;
				if (top > r.y) resultY = r.y;
				if (bottom < r.y + r.height) resultY = r.y + r.height - vpHeight;
				return -resultY;
			}
		});
		
		
	}
	
	public void scrollIntoViewport(Node<?> w) {
		Node<?> lookup = w;
		double xpos = 0;
		double ypos = 0;
		while (lookup != null && lookup.parent().get() != this.area) {
			xpos += lookup.x().get();
			ypos += lookup.y().get();
			lookup = lookup.parent().get();
		}
		
		if (lookup != null) {
			// its our child
			System.err.println("found child at " + xpos + ", " + ypos);
			scrollIntoViewport(new Rect(xpos, ypos, 50, 50));
		}
	}
	
	public void scrollIntoViewport(Widget w) {
		scrollIntoViewport(w.area);
	}
	
	
	public ScrollPane() {
		
		widgetBindings.registerBinding(Binder.bidiBindRect(viewportX, viewportY, viewportWidth, viewportHeight, realViewport));
		
		widgetBindings.registerBinding(
		Binder.uniBind(viewport(), new Setter<Rect>() {
			@Override
			public void set(Rect value) {
				if (value == null) {
					realViewport.set(getBoundingRect());
				}
				else {
					realViewport.set(value);
				}
			}
		}));
		
		area.height().registerChangeListener(new DoubleChangeListener() {
			@Override
			public void onChange(double oldValue, double newValue) {
				if (viewport().get() == null) {
					realViewport.set(getBoundingRect());
				}
				resize();
				if (content != null) updateRange(realViewport.get(), content);
			}
		});
		
		area.width().registerChangeListener(new DoubleChangeListener() {
			@Override
			public void onChange(double oldValue, double newValue) {
				if (viewport().get() == null) {
					realViewport.set(getBoundingRect());
				}
				resize();
				if (content != null) updateRange(realViewport.get(), content);
			}
		});
		
		
		realViewport.registerChangeListener(new ChangeListener<Rect>() {
			@Override
			public void onChange(Rect oldValue, Rect viewport) {
				if (content != null) {
					updateRange(viewport, content);
				}
			}
		});
		
		
		hBar = new ScrollBar(Orientation.HORIZONTAL);
		vBar = new ScrollBar(Orientation.VERTICAL);
		
		hBar.area.addStyleClass("hbar");
		hBar.sliderSizeFactor().set(0.2);
		hBar.minValue().set(0);
		hBar.maxValue().set(100);
		hBar.value().set(0);
		
		vBar.area.addStyleClass("vbar");
		vBar.sliderSizeFactor().set(0.2);
		vBar.minValue().set(0);
		vBar.maxValue().set(100);
		vBar.value().set(0);
		
		hBar.area.height().set(20);
		vBar.area.width().set(20);
		
		area.acceptTapEvents().set(true);
		
		area.onScroll().registerSignalListener(new SignalListener<ScrollEvent>() {
			@Override
			public void onSignal(ScrollEvent data) {
				if (content != null) {
					if (minOffsetHorizontal < maxOffsetHorizontal) {
						content.area.x().updateDynamic(new ClampedDoubleIncrement(-data.deltaX, minOffsetHorizontal, maxOffsetHorizontal));
					}
					if (minOffsetVertical < maxOffsetVertical) {
						content.area.y().updateDynamic(new ClampedDoubleIncrement(-data.deltaY, minOffsetVertical, maxOffsetVertical));
					}
					data.consume();
				}
			}
		});
		
	}
	
	private void updateRange(Rect viewport, Widget content) {
		maxOffsetVertical = viewport.x;
		maxOffsetHorizontal = viewport.y;
		minOffsetVertical = Math.min(0, viewport.height - content.area.height().get());
		minOffsetHorizontal = Math.min(0, viewport.width - content.area.width().get());
		
		System.err.println("offsetX: from " + minOffsetHorizontal + " to " + maxOffsetHorizontal);
		System.err.println("offsetY: from " + minOffsetVertical + " to " + maxOffsetVertical);
	
		// clamp
		content.area.x().updateDynamic(new ClampedDoubleIncrement(0, minOffsetHorizontal, maxOffsetHorizontal));
		content.area.y().updateDynamic(new ClampedDoubleIncrement(0, minOffsetVertical, maxOffsetVertical));
	}
	
	private Property<Boolean> showHorizontalScrollBar = null;
	public Property<Boolean> showHorizontalScrollBar() {
		if (showHorizontalScrollBar == null) {
			showHorizontalScrollBar = new SimpleProperty<Boolean>(true);
			showHorizontalScrollBar.registerChangeListener(new ChangeListener<Boolean>() {
				@Override
				public void onChange(Boolean oldValue, Boolean newValue) {
					if (newValue) {
						hBar.area.parent().set(area);
					}
					else {
						hBar.area.parent().set(null);
					}
				}
			});
		}
		return showHorizontalScrollBar;
	}
	
	private Property<Boolean> showVerticalScrollBar = null;
	public Property<Boolean> showVerticalScrollBar() {
		if (showVerticalScrollBar == null) {
			showVerticalScrollBar = new SimpleProperty<Boolean>(true);
			showVerticalScrollBar.registerChangeListener(new ChangeListener<Boolean>() {
				@Override
				public void onChange(Boolean oldValue, Boolean newValue) {
					if (newValue) {
						vBar.area.parent().set(area);
					}
					else {
						System.err.println("REMOVE VBAR");
						vBar.area.parent().set(null);
					}
				}
			});
		}
		return showHorizontalScrollBar;
	}
	
	public void setContent(Widget content) {
		if (this.content != null) {
			this.content.parent = null;
			this.content.getAreaNode().parent().set(null);
			this.content.getAreaNode().x().setTransition(null);
			this.content.getAreaNode().y().setTransition(null);
			
		}
		this.content = content;
		
		addWidget(content);
//		content.getAreaNode().parent().set(area);
		content.getAreaNode().x().setTransition(new TouchScrollTransition());
		content.getAreaNode().y().setTransition(new TouchScrollTransition());
		
		contentBinding.dispose();
		
		contentBinding.registerBinding(WidgetBinder.bindScrollBar(hBar, content.area.x(), viewportX, viewportWidth, content.area.width()));
		contentBinding.registerBinding(WidgetBinder.bindScrollBar(vBar, content.area.y(), viewportY, viewportHeight, content.area.height()));
		
		
		if (showHorizontalScrollBar().get()) {
			hBar.area.parent().set(area);
		}
		if (showVerticalScrollBar().get()) {
			vBar.area.parent().set(area);
		}
		
//		final DoubleChangeListener contentLimitX = new DoubleChangeListener() {
//			@Override
//			public void onChange(double oldValue, double newValue) {
//				final double max = ScrollPane.this.content.area.width().get() - area.width().get();
//				if (newValue > 0) {
//					ScrollPane.this.content.area.x().set(0);
//				}
//				else if (newValue < max) {
//					ScrollPane.this.content.area.x().set(max);
//				}
//			}
//		};
//		content.area.x().registerChangeListener(contentLimitX);
		
//		final DoubleChangeListener contentLimitY = new DoubleChangeListener() {
//			@Override
//			public void onChange(double oldValue, double newValue) {
//				final double max = ScrollPane.this.content.area.height().get() - area.height().get();
//				if (newValue > 0) {
//					ScrollPane.this.content.area.y().set(0);
//				}
//				else if (newValue < max) {
//					ScrollPane.this.content.area.y().set(max);
//				}
//			} 
//		};
//		content.area.y().registerChangeListener(contentLimitY);
		
//		contentBinding.registerBinding(new Binding() {
//			@Override
//			public void dispose() {
//				ScrollPane.this.content.area.x().unregisterChangeListener(contentLimitX);
//				ScrollPane.this.content.area.y().unregisterChangeListener(contentLimitY);
//			}
//		});
		
		resize();
		updateRange(viewport.get()==null?getBoundingRect():viewport.get(), content);
	}
	
	
	private void resize() {
		double parentWidth = area.width().get();
		double parentHeight = area.height().get();
		
		area.clippingShape().set(new Rectangle(0, 0, parentWidth, parentHeight));
		
		// udpate scroll bar positions
		vBar.area.height().set(parentHeight - 20);
		vBar.area.width().set(20);
		vBar.area.x().set(parentWidth - 20);
		
		hBar.area.width().set(parentWidth - 20);
		hBar.area.height().set(20);
		hBar.area.y().set(parentHeight - 20);
		
	}
	
	@Override
	protected void restrictViewport(Rect viewport) {
		viewport().set(viewport);
	}
}
