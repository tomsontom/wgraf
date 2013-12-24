package at.bestsolution.wgraf.backend.qt;

import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.Property;

import com.trolltech.qt.gui.QApplication;

public class QtBinder {

	public static interface Setter<Type> {
		void set(Type value);
	}
	
	public abstract static class QtSetter<Type> implements Setter<Type> {
		@Override
		public final void set(final Type value) {
			if (QApplication.instance().thread() == Thread.currentThread()) {
				doSet(value);
			}
			else {
				QtSync.runLater(new Runnable() {
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
	
}
