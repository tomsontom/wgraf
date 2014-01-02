package at.bestsolution.wgraf.properties;


public class Binder {

	
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
	
	// TODO this wont work - this will loop endlessly -.-
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
		
		return new Binding() {
			@Override
			public void dispose() {
				sourceProperty.unregisterChangeListener(listener);
			}
		};
	}
	
}
