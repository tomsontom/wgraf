package at.bestsolution.wgraf.backend.javafx;

import javafx.application.Platform;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.DoubleChangeListener;
import at.bestsolution.wgraf.properties.DoubleProperty;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.Setter;

public class JavaFxBinder {

	public abstract static class JfxSetter<Type> implements Setter<Type> {
		@Override
		public final void set(final Type value) {
			if (Platform.isFxApplicationThread()) {
				doSet(value);
			}
			else {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						doSet(value);
					}
				});
			}
		}
		
		public abstract void doSet(Type value);
	}
	
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
	
}
