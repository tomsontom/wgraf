package at.bestsolution.wgraf.widgets;

import at.bestsolution.wgraf.events.ScrollEvent;
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
		slider.setParent(area);
		
		area.acceptTapEvents().set(true);
		
		area.background().set(new FillBackground(new Color(255, 0, 0, 100), new CornerRadii(5), new Insets(0, 0, 0, 0)));
		slider.background().set(new FillBackground(new Color(255, 0, 0, 150), new CornerRadii(5), new Insets(2, 2, 2, 2)));
		
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
		
	}
	
	private void relayout() {
		double minValue = this.minValue.get();
		double maxValue = this.maxValue.get();
		double valueRange = Math.abs(maxValue - minValue);
		
		double sliderSizeFactor = this.sliderSizeFactor.get();
		
		double sliderSpace;
		
		if (orientation == Orientation.VERTICAL) {
			sliderSpace = area.width().get() - slider.width().get();
			
			double sliderSize = sliderSizeFactor * area.height().get();
			slider.height().set(sliderSize);
			slider.width().set(area.width().get());
			
		} else {
			sliderSpace = area.width().get() - slider.width().get();
			
			double sliderSize = sliderSizeFactor * area.width().get();
			slider.width().set(sliderSize);
			slider.height().set(area.height().get());
		}
		
		valueFactor = valueRange / sliderSpace;
	}
	
	private void onScroll(ScrollEvent data) {
		double minValue = this.minValue.get();
		double maxValue = this.maxValue.get();
		
		if (orientation == Orientation.HORIZONTAL) {
			double newX = offset - data.deltaX;
			
			double newValue = minValue + newX * valueFactor;
			
			// clamp
			newValue = Math.max(minValue, newValue);
			newValue = Math.min(maxValue, newValue);
			
			value.set(newValue);
		}
		else {
			double newY = offset - data.deltaY;
			
			double newValue = minValue + newY * valueFactor;
			
			// clamp
			newValue = Math.max(minValue, newValue);
			newValue = Math.min(maxValue, newValue);
			
			value.set(newValue);
		}
	}
	
	private void updatePosition(double value) {
		double minValue = this.minValue.get();
		
		offset = (value - minValue) / valueFactor;
		if (orientation == Orientation.HORIZONTAL) {
			slider.x().set(offset);
		}
		else {
			slider.y().set(offset);
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
