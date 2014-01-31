package at.bestsolution.wgraf.properties.binding;

import java.util.concurrent.atomic.AtomicBoolean;

import at.bestsolution.wgraf.Sync;
import at.bestsolution.wgraf.math.Rect;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.Converter;
import at.bestsolution.wgraf.properties.DoubleChangeListener;
import at.bestsolution.wgraf.properties.DoubleProperty;
import at.bestsolution.wgraf.properties.InvalidValueException;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.Signal;
import at.bestsolution.wgraf.properties.SignalListener;

public class Binder {
	
	public static <A, B> Binding bidiBind(final Property<A> propertyA, final Property<B> propertyB, final BindingConverter<A, B> converterAB, final BindingConverter<B, A> converterBA) {
		return bidiBindDelayed(propertyA, propertyB, converterAB, converterBA, 0, 0);
	}
	
	public static <A, B> Binding bidiBindDelayed(final Property<A> propertyA, final Property<B> propertyB, final BindingConverter<A, B> converterAB, final BindingConverter<B, A> converterBA, final long delayAB, final long delayBA) {
		final AtomicBoolean ignoreA = new AtomicBoolean(false);
		final AtomicBoolean ignoreB = new AtomicBoolean(false);
		
		final ChangeListener<A> listenerA = new ChangeListener<A>() {
			private Object current = null;
			@Override
			public void onChange(A oldValue, final A newValue) throws InvalidValueException {
				if (!ignoreA.get()) {
					try {
						final B convertedValue = converterAB.convert(newValue);
						
						if (delayAB > 0) {
							final Object currentChange = new Object();
							current = currentChange;
							Sync.get().execLaterOnUIThread(new Runnable() {
								@Override
								public void run() {
									// drop 
									if (current != currentChange) return;
									
									// apply change
									try {
										ignoreB.set(true);
										propertyB.set(convertedValue);
									}
									finally {
										ignoreB.set(false);
									}
								}
							}, delayAB);
						}
						else {
							// apply change
							try {
								ignoreB.set(true);
								propertyB.set(convertedValue);
							}
							finally {
								ignoreB.set(false);
							}
						}
					}
					catch (ConversionException e) {
						// conversion failed!
						throw new InvalidValueException(e.getMessage(), e.getCause());
					}
					
				}
			}
		};
		final ChangeListener<B> listenerB = new ChangeListener<B>() {
			private Object current = null;
			@Override
			public void onChange(B oldValue, final B newValue) throws InvalidValueException {
				if (!ignoreB.get()) {
					try {
						final A convertedValue = converterBA.convert(newValue);
						if (delayBA > 0) {
							final Object currentChange = new Object();
							current = currentChange;
							Sync.get().execLaterOnUIThread(new Runnable() {
								@Override
								public void run() {
									// drop 
									if (current != currentChange) return;
									
									// apply change
									try {
										ignoreA.set(true);
										propertyA.set(convertedValue);
									}
									finally {
										ignoreA.set(false);
									}
								}
							}, delayBA);
						}
						else {
							try {
								ignoreA.set(true);
								propertyA.set(converterBA.convert(newValue));
							}
							finally {
								ignoreA.set(false);
							}
						}
					}
					catch (ConversionException e) {
						// conversion failed!
						throw new InvalidValueException(e.getMessage(), e.getCause());
					}
				}
			}
		};
		
		propertyA.registerChangeListener(listenerA);
		propertyB.registerChangeListener(listenerB);
		
		return new Binding() {
			@Override
			public void dispose() {
				propertyA.unregisterChangeListener(listenerA);
				propertyB.unregisterChangeListener(listenerB);
			}
		};
	}
	
	
	public static <Type> Binding uniBindDelayed(final Property<Type> property, final Setter<Type> setter, final long delay) {
		final ChangeListener<Type> listener = new ChangeListener<Type>() {
			private Object current = null;
			@Override
			public void onChange(Type oldValue, final Type newValue) {
				final Object currentChange = new Object();
				current = currentChange;
				
				Sync.get().execLaterOnUIThread(new Runnable() {
					@Override
					public void run() {
						// drop 
						if (current != currentChange) return;
						setter.set(newValue);
					}
				}, delay);
				
			}
		};
		property.registerChangeListener(listener);
		
		return new Binding() {
			
			@Override
			public void dispose() {
				property.unregisterChangeListener(listener);
			}
		};
	}
	
	public static <Type> Binding uniBindDelayed(final Property<Type> source, final Property<Type> target, final long delay) {
		final ChangeListener<Type> listener = new ChangeListener<Type>() {
			private Object current = null;
			@Override
			public void onChange(Type oldValue, final Type newValue) {
				final Object currentChange = new Object();
				current = currentChange;
				
				Sync.get().execLaterOnUIThread(new Runnable() {
					@Override
					public void run() {
						// drop 
						if (current != currentChange) return;
						target.set(newValue);
					}
				}, delay);
				
			}
		};
		source.registerChangeListener(listener);
		
		return new Binding() {
			@Override
			public void dispose() {
				source.unregisterChangeListener(listener);
			}
		};
	}
	
	public static <Type> Binding uniBind(final Property<Type> property, final Setter<Type> setter) {
		final ChangeListener<Type> listener = new ChangeListener<Type>() {
			@Override
			public void onChange(Type oldValue, Type newValue) {
				setter.set(newValue);
			}
		};
		
		property.registerChangeListener(listener);
		setter.set(property.get());
		
		return new Binding() {
			@Override
			public void dispose() {
				property.unregisterChangeListener(listener);
			}
		};
	}
	
	public static Binding uniBind(final DoubleProperty property, final Setter<Double> setter) {
		final DoubleChangeListener listener = new DoubleChangeListener() {
			
			@Override
			public void onChange(double oldValue, double newValue) {
				setter.set(newValue);
			}
		};
		property.registerChangeListener(listener);
		setter.set(property.get());
		
		return new Binding() {
			@Override
			public void dispose() {
				property.unregisterChangeListener(listener);
			}
		};
	}
	
	public static Binding uniBind(final DoubleProperty propertyA, final DoubleProperty propertyB) {
		final DoubleChangeListener listener = new DoubleChangeListener() {
			@Override
			public void onChange(double oldValue, double newValue) {
				propertyB.set(newValue);
			}
		};
		propertyA.registerChangeListener(listener);
		propertyB.set(propertyA.get());
		return new Binding() {
			@Override
			public void dispose() {
				propertyA.unregisterChangeListener(listener);
			}
		};
	}
	
	public static <A, B> Binding uniBind(final Property<A> propertyA, final Property<B> propertyB, final Converter<A, B> converter) {
		final ChangeListener<A> listener = new ChangeListener<A>() {
			@Override
			public void onChange(A oldValue, A newValue) {
				propertyB.set(converter.convert(newValue));
			}
		};
		propertyA.registerChangeListener(listener);
		
		return new Binding() {
			@Override
			public void dispose() {
				propertyA.unregisterChangeListener(listener);
			}
		};
	}
	
	public static Binding bidiBindRect(
			final DoubleProperty propertyAx,
			final DoubleProperty propertyAy,
			final DoubleProperty propertyAwidth,
			final DoubleProperty propertyAheight,
			final Property<Rect> propertyB) {
		final AtomicBoolean ignoreA = new AtomicBoolean(false);
		final AtomicBoolean ignoreB = new AtomicBoolean(false);
		
		
		final DoubleChangeListener xChange = new DoubleChangeListener() {
			@Override
			public void onChange(double oldValue, double newValue) {
				if (!ignoreA.get()) {
					try {
						ignoreB.set(true);
						propertyB.set(new Rect(newValue, propertyAy.get(), propertyAwidth.get(), propertyAheight.get()));
					}
					finally {
						ignoreB.set(false);
					}
				}
			}
		};
		
		final DoubleChangeListener yChange = new DoubleChangeListener() {
			@Override
			public void onChange(double oldValue, double newValue) {
				if (!ignoreA.get()) {
					try {
						ignoreB.set(true);
						propertyB.set(new Rect(propertyAx.get(), newValue, propertyAwidth.get(), propertyAheight.get()));
					}
					finally {
						ignoreB.set(false);
					}
				}
			}
		};
		
		final DoubleChangeListener widthChange = new DoubleChangeListener() {
			@Override
			public void onChange(double oldValue, double newValue) {
				if (!ignoreA.get()) {
					try {
						ignoreB.set(true);
						propertyB.set(new Rect(propertyAx.get(), propertyAx.get(), newValue, propertyAheight.get()));
					}
					finally {
						ignoreB.set(false);
					}
				}
			}
		};
		
		final DoubleChangeListener heightChange = new DoubleChangeListener() {
			@Override
			public void onChange(double oldValue, double newValue) {
				if (!ignoreA.get()) {
					try {
						ignoreB.set(true);
						propertyB.set(new Rect(propertyAx.get(), propertyAx.get(), propertyAwidth.get(), newValue));
					}
					finally {
						ignoreB.set(false);
					}
				}
			}
		};
		
		final ChangeListener<Rect> rectChange = new ChangeListener<Rect>() {
			@Override
			public void onChange(Rect oldValue, Rect newValue) {
				if (!ignoreB.get()) {
					try {
						ignoreA.set(true);
						propertyAx.set(newValue.x);
						propertyAy.set(newValue.y);
						propertyAwidth.set(newValue.width);
						propertyAheight.set(newValue.height);
					}
					finally {
						ignoreA.set(false);
					}
				}
			}
		};
		
		propertyAx.registerChangeListener(xChange);
		propertyAy.registerChangeListener(yChange);
		propertyAwidth.registerChangeListener(widthChange);
		propertyAheight.registerChangeListener(heightChange);
		propertyB.registerChangeListener(rectChange);
		
		return new Binding() {
			@Override
			public void dispose() {
				propertyAx.unregisterChangeListener(xChange);
				propertyAy.unregisterChangeListener(yChange);
				propertyAwidth.unregisterChangeListener(widthChange);
				propertyAheight.unregisterChangeListener(heightChange);
				propertyB.unregisterChangeListener(rectChange);
			}
		};
	}
			
	
	
	
	public static Binding bidiBind(final DoubleProperty propertyA, final DoubleProperty propertyB, final Converter<Double, Double> converterAB, final Converter<Double, Double> converterBA) {
		final AtomicBoolean ignoreA = new AtomicBoolean(false);
		final AtomicBoolean ignoreB = new AtomicBoolean(false);
		
		final DoubleChangeListener listenerA = new DoubleChangeListener() {
			@Override
			public void onChange(double oldValue, double newValue) {
				if (!ignoreA.get()) {
					try {
						ignoreB.set(true);
//						if (propertyB instanceof DoubleTransitionProperty) {
//							((DoubleTransitionProperty)propertyB).set
//						}
						propertyB.set(converterAB.convert(newValue));
					}
					finally {
						ignoreB.set(false);
					}
				}
			}
		};
		final DoubleChangeListener listenerB = new DoubleChangeListener() {
			@Override
			public void onChange(double oldValue, double newValue) {
				if (!ignoreB.get()) {
					try {
						ignoreA.set(true);
						propertyA.set(converterBA.convert(newValue));
					}
					finally {
						ignoreA.set(false);
					}
				}
			}
		};
		
		propertyA.registerChangeListener(listenerA);
		propertyB.registerChangeListener(listenerB);
		
		return new Binding() {
			@Override
			public void dispose() {
				propertyA.unregisterChangeListener(listenerA);
				propertyB.unregisterChangeListener(listenerB);
			}
		};
	}
	
	
	// TODO method needs to be fixed - see above
	public static <A, B> Binding bidiBind(final Property<A> propertyA, final Property<B> propertyB, final Converter<A, B> converterAB, final Converter<B, A> converterBA) {
		final ChangeListener<A> listenerA = new ChangeListener<A>() {
			@Override
			public void onChange(A oldValue, A newValue) {
				propertyB.set(converterAB.convert(newValue));
			}
		};
		propertyA.registerChangeListener(listenerA);
		
		final ChangeListener<B> listenerB = new ChangeListener<B>() {
			@Override
			public void onChange(B oldValue, B newValue) {
				propertyA.set(converterBA.convert(newValue));
			}
		};
		propertyB.registerChangeListener(listenerB);
		
		return new Binding() {
			@Override
			public void dispose() {
				propertyA.unregisterChangeListener(listenerA);
				propertyB.unregisterChangeListener(listenerB);
			}
		};
	}

	public static <A> Binding uniBind(final Property<A> sourceProperty, final Property<A> targetProperty) {
		final ChangeListener<A> listener = new ChangeListener<A>() {
			@Override
			public void onChange(A oldValue, A newValue) {
				targetProperty.set(newValue);
			}
		};
		sourceProperty.registerChangeListener(listener);
		targetProperty.set(sourceProperty.get());
		
		return new Binding() {
			@Override
			public void dispose() {
				sourceProperty.unregisterChangeListener(listener);
			}
		};
	}


	public static <T> Binding uniBind(final Signal<T> source, final Signal<T> target) {
		final SignalListener<T> listener = new SignalListener<T>() {
			@Override
			public void onSignal(T data) {
				target.signal(data);
			}
		};
		source.registerSignalListener(listener);
		return new Binding() {
			@Override
			public void dispose() {
				source.unregisterSignalListener(listener);
			}
		};
	}
	
//	public static <Type> void uniBind(Property<Type> property, final Setter<Type> setter) {
//		property.registerChangeListener(new ChangeListener<Type>() {
//			@Override
//			public void onChange(Type oldValue, Type newValue) {
//				setter.set(newValue);
//			}
//		});
//		setter.set(property.get());
//	}
	
//	public static void uniBind(DoubleProperty property, final Setter<Double> setter) {
//		property.registerChangeListener(new DoubleChangeListener() {
//			@Override
//			public void onChange(double oldValue, double newValue) {
//				setter.set(newValue);
//			}
//		});
//		setter.set(property.get());
//	}
	
//	public static void uniBind(DoubleProperty source, final DoubleProperty target) {
//		source.registerChangeListener(new DoubleChangeListener() {
//			@Override
//			public void onChange(double oldValue, double newValue) {
//				target.set(newValue);
//			}
//		});
//		target.set(source.get());
//	}

//	public static <Type> Binding uniBind(final Property<Type> source, final Property<Type> target) {
//		final ChangeListener<Type> listener = new ChangeListener<Type>() {
//			@Override
//			public void onChange(Type oldValue, Type newValue) {
//				target.set(newValue);
//			}
//		};
//		source.registerChangeListener(listener);
//		target.set(source.get());
//		return new Binding() {
//			@Override
//			public void dispose() {
//				source.unregisterChangeListener(listener);
//			}
//		};
//	}

}
