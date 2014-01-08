package at.bestsolution.wgraf.widgets;

import at.bestsolution.wgraf.events.ScrollEvent;
import at.bestsolution.wgraf.events.ScrollLock;
import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.properties.DoubleChangeListener;
import at.bestsolution.wgraf.properties.DoubleProperty;
import at.bestsolution.wgraf.properties.DoubleTransitionProperty;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.properties.simple.SimpleDoubleTransitionProperty;
import at.bestsolution.wgraf.scene.Container;
import at.bestsolution.wgraf.style.CornerRadii;
import at.bestsolution.wgraf.style.FillBackground;
import at.bestsolution.wgraf.style.Insets;
import at.bestsolution.wgraf.transition.TouchScrollTransition;

public class ScrollBar extends Widget {

	private final Container slider;
	
	private final Orientation orientation;
	
	private DoubleProperty minValue = new SimpleDoubleTransitionProperty();
	private DoubleProperty maxValue = new SimpleDoubleTransitionProperty();
	
	private DoubleTransitionProperty value = new SimpleDoubleTransitionProperty();
	private DoubleProperty sliderSizeFactor = new SimpleDoubleTransitionProperty();
	
	private double offset = 0;
	
	private double valueFactor = 0;
	
	public ScrollBar(Orientation orientation) {
		this.orientation = orientation;
		slider = new Container();
		slider.parent().set(area);
		
		area.acceptTapEvents().set(true);
		
		if (orientation == Orientation.HORIZONTAL) {
			slider.x().setTransition(new TouchScrollTransition());
		}
		else {
			slider.y().setTransition(new TouchScrollTransition());
		}
		
		value.registerChangeListener(new DoubleChangeListener() {
			@Override
			public void onChange(double oldValue, double newValue) {
				updatePosition(newValue);
			}
		});
		
		minValue.registerChangeListener(new DoubleChangeListener() {
			@Override
			public void onChange(double oldValue, double newValue) {
				relayout();
			}
		});
		maxValue.registerChangeListener(new DoubleChangeListener() {
			@Override
			public void onChange(double oldValue, double newValue) {
				relayout();
			}
		});
		sliderSizeFactor.registerChangeListener(new DoubleChangeListener() {
			@Override
			public void onChange(double oldValue, double newValue) {
				relayout();
			}
		});
		area.height().registerChangeListener(new DoubleChangeListener() {
			@Override
			public void onChange(double oldValue, double newValue) {
				relayout();
			}
		});
		area.width().registerChangeListener(new DoubleChangeListener() {
			@Override
			public void onChange(double oldValue, double newValue) {
				relayout();
			}
		});
		
		area.onScroll().registerSignalListener(new SignalListener<ScrollEvent>() {
			@Override
			public void onSignal(ScrollEvent data) {
				onScroll(data);
			}
		});
		
		initDefaultSkin();
	}
	
	private void initDefaultSkin() {
//		area.background().set(new FillBackground(new Color(130, 130, 130, 30), new CornerRadii(3), new Insets(0, 0, 0, 0)));
		slider.background().set(new FillBackground(new Color(130, 130, 130, 50), new CornerRadii(3), new Insets(2, 2, 2, 2)));
		sliderSizeFactor.registerChangeListener(new DoubleChangeListener() {
			@Override
			public void onChange(double oldValue, double newValue) {
				if (newValue == 1) {
					slider.background().set(null);
				}
				else {
					slider.background().set(new FillBackground(new Color(130, 130, 130, 50), new CornerRadii(3), new Insets(2, 2, 2, 2)));
				}
			}
		});
	}
	
	protected void relayout() {
		double minValue = this.minValue.get();
		double maxValue = this.maxValue.get();
		double valueRange = Math.abs(maxValue - minValue);
		
		double sliderSizeFactor = this.sliderSizeFactor.get();
		
		double sliderSpace;
		
		if (orientation == Orientation.VERTICAL) {
			double sliderSize = sliderSizeFactor * area.height().get();
			sliderSpace = area.height().get() - sliderSize;
			slider.height().set(sliderSize);
			slider.width().set(area.width().get());
			
		} else {
			double sliderSize = sliderSizeFactor * area.width().get();
			sliderSpace = area.width().get()- sliderSize;
			slider.width().set(sliderSize);
			slider.height().set(area.height().get());
		}
		if (sliderSpace == 0) {
			valueFactor = 0;
		}
		else {
			valueFactor = valueRange / sliderSpace;
		}
	}
	
	private void onScroll(ScrollEvent data) {
		double minValue = this.minValue.get();
		double maxValue = this.maxValue.get();
		if (orientation == Orientation.HORIZONTAL) {
			if (data.scrollLock == ScrollLock.HORIZONTAL) {
				double newX = offset - data.deltaX;
				
				double newValue = minValue + newX * valueFactor;
				
				// clamp
				newValue = Math.max(minValue, newValue);
				newValue = Math.min(maxValue, newValue);
				
				value.set(newValue);
				
				data.consume();
			}
		}
		else {
			if (data.scrollLock == ScrollLock.VERTICAL) {
				double newY = offset - data.deltaY;
				
				double newValue = minValue + newY * valueFactor;
				
				// clamp
				newValue = Math.max(minValue, newValue);
				newValue = Math.min(maxValue, newValue);
				
				value.set(newValue);
				
				data.consume();
			}
		}
	}
	
	private void updatePosition(double value) {
		if (valueFactor == 0) {
			offset = 0;
		}
		else {
			double minValue = this.minValue.get();
			offset = (value - minValue) / valueFactor;
		}
		if (orientation == Orientation.HORIZONTAL) {
			slider.x().setDynamic(offset);
		}
		else {
			slider.y().setDynamic(offset);
		}
	}
	
	public ScrollBar() {
		this(Orientation.HORIZONTAL);
	}
	
	public DoubleProperty value() {
		return value;
	}
	
	public DoubleProperty maxValue() {
		return maxValue;
	}
	
	public DoubleProperty minValue() {
		return minValue;
	}
	
	public DoubleProperty sliderSizeFactor() {
		return sliderSizeFactor;
	}
}
