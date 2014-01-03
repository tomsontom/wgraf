package at.bestsolution.wgraf.widgets;

import at.bestsolution.wgraf.events.ScrollEvent;
import at.bestsolution.wgraf.geom.shape.Rectangle;
import at.bestsolution.wgraf.math.Vec2d;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.DoubleChangeListener;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.transition.TouchScrollTransition;

public class ScrollPane extends Pane {

	private Widget content;
	
	private ScrollBar hBar;
	private ScrollBar vBar;
	
	private boolean fitVertically = false;
	private boolean fitHorizontally = false;
	
	private void updateScrollBars() {
		if (content != null) {
			double contentWidth = content.area.width().get();
			double contentHeight = content.area.height().get();
			
			double windowWidth = area.width().get();
			double windowHeight = area.height().get();
			
			
			hBar.sliderSizeFactor().set(contentWidth / windowWidth);
			vBar.sliderSizeFactor().set(contentHeight / windowHeight);
			
			hBar.minValue().set(0);
			hBar.maxValue().set(windowWidth - contentWidth);
			
			vBar.minValue().set(0);
			vBar.maxValue().set(windowHeight - contentHeight);
		}
	}
	
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
					content.area.x().increment(-data.deltaX);
					content.area.y().increment(-data.deltaY);;
				}
			}
		});
		
	}
	
	public void setContent(Widget content) {
		this.content = content;
		
		content.getAreaNode().setParent(area);
		content.getAreaNode().x().setTransition(new TouchScrollTransition());
		content.getAreaNode().y().setTransition(new TouchScrollTransition());
		
		final Widget c = content;
		content.getAreaNode().x().registerChangeListener(new DoubleChangeListener() {
			@Override
			public void onChange(double oldValue, double newValue) {
				if (content != null) {
					double space = area.width().get() - c.area.width().get();
					hBar.value().set(space - newValue);
				}
			}
		});
		content.getAreaNode().y().registerChangeListener(new DoubleChangeListener() {
			@Override
			public void onChange(double oldValue, double newValue) {
				if (content != null) {
					double space = area.height().get() - c.area.height().get();
					vBar.value().set(space - newValue);
				}
			}
		});
		
		hBar.area.setParent(area);
		vBar.area.setParent(area);
		
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
			
		}
		
		vBar.area.height().set(parentHeight);
		vBar.area.width().set(20);
		vBar.area.x().set(parentWidth - 20);
		
		hBar.area.width().set(parentWidth);
		hBar.area.height().set(20);
		hBar.area.y().set(parentHeight - 20);
		
		updateScrollBars();
	}
}
