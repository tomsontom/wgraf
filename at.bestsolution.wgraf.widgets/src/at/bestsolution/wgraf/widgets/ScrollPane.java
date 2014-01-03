package at.bestsolution.wgraf.widgets;

import at.bestsolution.wgraf.events.ScrollEvent;
import at.bestsolution.wgraf.geom.shape.Rectangle;
import at.bestsolution.wgraf.math.Vec2d;
import at.bestsolution.wgraf.properties.Binding;
import at.bestsolution.wgraf.properties.ClampedDoubleIncrement;
import at.bestsolution.wgraf.properties.DoubleChangeListener;
import at.bestsolution.wgraf.properties.GroupBinding;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.transition.TouchScrollTransition;

public class ScrollPane extends Pane {

	private Widget content;
	
	private ScrollBar hBar;
	private ScrollBar vBar;
	
	private boolean fitVertically = false;
	private boolean fitHorizontally = false;
	
	private GroupBinding contentBinding = new GroupBinding();
	
	private double minOffsetHorizontal;
	private double maxOffsetHorizontal;
	private double minOffsetVertical;
	private double maxOffsetVertical;
	
	
	public ScrollPane() {
		hBar = new ScrollBar(Orientation.HORIZONTAL);
		vBar = new ScrollBar(Orientation.VERTICAL);
		
		hBar.sliderSizeFactor().set(0.2);
		hBar.minValue().set(0);
		hBar.maxValue().set(100);
		hBar.value().set(0);
		
		vBar.sliderSizeFactor().set(0.2);
		vBar.minValue().set(0);
		vBar.maxValue().set(100);
		vBar.value().set(0);
		
		hBar.area.height().set(20);
		vBar.area.width().set(20);
		
		area.height().registerChangeListener(new DoubleChangeListener() {
			@Override
			public void onChange(double oldValue, double newValue) {
				resize();
			}
		});
		area.width().registerChangeListener(new DoubleChangeListener() {
			@Override
			public void onChange(double oldValue, double newValue) {
				resize();
			}
		});
		
		area.acceptTapEvents().set(true);
		
		area.onScroll().registerSignalListener(new SignalListener<ScrollEvent>() {
			@Override
			public void onSignal(ScrollEvent data) {
				if (content != null) {
					content.area.x().updateDynamic(new ClampedDoubleIncrement(-data.deltaX, minOffsetHorizontal, maxOffsetHorizontal));
					content.area.y().updateDynamic(new ClampedDoubleIncrement(-data.deltaY, minOffsetVertical, maxOffsetVertical));
				}
			}
		});
		
	}
	
	public void setContent(Widget content) {
		this.content = content;
		
		content.getAreaNode().setParent(area);
		content.getAreaNode().x().setTransition(new TouchScrollTransition());
		content.getAreaNode().y().setTransition(new TouchScrollTransition());
		
		hBar.area.setParent(area);
		vBar.area.setParent(area);
		
		contentBinding.dispose();
		contentBinding.registerBinding(WidgetBinder.bindScrollBar(hBar, content.area.x(), area.width(), content.area.width()));
		contentBinding.registerBinding(WidgetBinder.bindScrollBar(vBar, content.area.y(), area.height(), content.area.height()));
		
		final DoubleChangeListener contentLimitX = new DoubleChangeListener() {
			@Override
			public void onChange(double oldValue, double newValue) {
				final double max = ScrollPane.this.content.area.width().get() - area.width().get();
				if (newValue > 0) {
					ScrollPane.this.content.area.x().set(0);
				}
				else if (newValue < max) {
					ScrollPane.this.content.area.x().set(max);
				}
			}
		};
//		content.area.x().registerChangeListener(contentLimitX);
		
		final DoubleChangeListener contentLimitY = new DoubleChangeListener() {
			@Override
			public void onChange(double oldValue, double newValue) {
				final double max = ScrollPane.this.content.area.height().get() - area.height().get();
				if (newValue > 0) {
					ScrollPane.this.content.area.y().set(0);
				}
				else if (newValue < max) {
					ScrollPane.this.content.area.y().set(max);
				}
			} 
		};
//		content.area.y().registerChangeListener(contentLimitY);
		
		contentBinding.registerBinding(new Binding() {
			@Override
			public void dispose() {
				ScrollPane.this.content.area.x().unregisterChangeListener(contentLimitX);
				ScrollPane.this.content.area.y().unregisterChangeListener(contentLimitY);
			}
		});
		
		resize();
	}
	
	
	private void resize() {
		double parentWidth = area.width().get();
		double parentHeight = area.height().get();
		
		area.clippingShape().set(new Rectangle(0, 0, parentWidth, parentHeight));
		
		if (content != null) {
			Vec2d contentSize = content.computePreferredSize();
			
			double contentWidth = fitHorizontally ?
					contentSize.x < parentWidth ? parentWidth : contentSize.x :
						contentSize.x;
			
			double contentHeight = fitVertically ?
					contentSize.y < parentHeight ? parentHeight : contentSize.y : 
						contentSize.y;
			
			content.getAreaNode().width().set(contentWidth);
			content.getAreaNode().height().set(contentHeight);
			
			maxOffsetVertical = 0;
			maxOffsetHorizontal = 0;
			minOffsetVertical = ScrollPane.this.content.area.height().get() - area.height().get();
			minOffsetHorizontal = ScrollPane.this.content.area.width().get() - area.width().get();
		}
		
		vBar.area.height().set(parentHeight - 20);
		vBar.area.width().set(20);
		vBar.area.x().set(parentWidth - 20);
		
		hBar.area.width().set(parentWidth - 20);
		hBar.area.height().set(20);
		hBar.area.y().set(parentHeight - 20);
		
//		updateScrollBars();
	}
}
