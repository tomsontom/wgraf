package at.bestsolution.wgraf.widgets;

import at.bestsolution.wgraf.properties.Binder;
import at.bestsolution.wgraf.properties.Binding;
import at.bestsolution.wgraf.properties.Converter;
import at.bestsolution.wgraf.properties.DoubleChangeListener;
import at.bestsolution.wgraf.properties.DoubleProperty;

public class WidgetBinder {

	
	public static Binding bindScrollBar(final ScrollBar bar, final DoubleProperty offset, final DoubleProperty windowSize, final DoubleProperty contentSize) {
		
		// configure bar
		bar.minValue().set(0.0);
		bar.maxValue().set(1.0);
		
		
		final Binding offsetBinding = Binder.bidiBind(offset, bar.value(), new Converter<Double, Double>() {
			@Override
			public Double convert(Double value) {
				double content = contentSize.get();
				double window = windowSize.get();
				double maxOffset = -(content - window);
				double curOffset = value;
				double barValue = -(curOffset / maxOffset);
				return barValue;
			}
		}, new Converter<Double, Double>() {
			
			@Override
			public Double convert(Double value) {
				double content = contentSize.get();
				double window = windowSize.get();
				double maxOffset = -(content - window);
				
				double newOffset = -(value * maxOffset);
				
				return newOffset;
			}
		});
		
//		final Binding offsetBinding = Binder.uniBind(offset, new Setter<Double>() {
//			@Override
//			public void set(Double value) {
//				double content = contentSize.get();
//				double window = windowSize.get();
//				double maxOffset = -(content - window);
//				double curOffset = value;
//				double barValue = -(curOffset / maxOffset);
//				bar.value().set(barValue);
//			}
//		});
//		
//		final Binding valueBinding = Binder.uniBind(bar.value(), new Setter<Double>() {
//			@Override
//			public void set(Double value) {
//				double content = contentSize.get();
//				double window = windowSize.get();
//				double maxOffset = -(content - window);
//				
//				double newOffset = value * maxOffset;
//				
//				offset.set(newOffset);
//			}
//		});
		
		final DoubleChangeListener resizeListener = new DoubleChangeListener() {
			@Override
			public void onChange(double oldValue, double newValue) {
				
				double content = contentSize.get();
				double window = windowSize.get();
				
				double sliderSize = content / window;
				sliderSize = Math.min(1.0, sliderSize);
				sliderSize = Math.max(0.2, sliderSize);
				bar.sliderSizeFactor().set(sliderSize);
			}
		};
		
		windowSize.registerChangeListener(resizeListener);
		contentSize.registerChangeListener(resizeListener);
		
		
		return new Binding() {
			@Override
			public void dispose() {
				offsetBinding.dispose();
				windowSize.unregisterChangeListener(resizeListener);
				contentSize.unregisterChangeListener(resizeListener);
			}
		};
	}
}
