package at.bestsolution.wgraf.properties.binding;

import at.bestsolution.wgraf.properties.Binding;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.DoubleChangeListener;
import at.bestsolution.wgraf.properties.DoubleProperty;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.style.ImageSource;

public class Binder {
	
	
	public static <Type> void uniBind(Property<Type> property, final Setter<Type> setter) {
		property.registerChangeListener(new ChangeListener<Type>() {
			@Override
			public void onChange(Type oldValue, Type newValue) {
				setter.set(newValue);
			}
		});
		setter.set(property.get());
	}
	
	public static void uniBind(DoubleProperty property, final Setter<Double> setter) {
		property.registerChangeListener(new DoubleChangeListener() {
			@Override
			public void onChange(double oldValue, double newValue) {
				setter.set(newValue);
			}
		});
		setter.set(property.get());
	}
	
	public static void uniBind(DoubleProperty source, final DoubleProperty target) {
		source.registerChangeListener(new DoubleChangeListener() {
			@Override
			public void onChange(double oldValue, double newValue) {
				target.set(newValue);
			}
		});
		target.set(source.get());
	}

	public static Binding uniBind(final Property<ImageSource> source, final Property<ImageSource> target) {
		final ChangeListener<ImageSource> listener = new ChangeListener<ImageSource>() {
			@Override
			public void onChange(ImageSource oldValue, ImageSource newValue) {
				target.set(newValue);
			}
		};
		source.registerChangeListener(listener);
		target.set(source.get());
		return new Binding() {
			@Override
			public void dispose() {
				source.unregisterChangeListener(listener);
			}
		};
	}
}
