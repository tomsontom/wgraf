package at.bestsolution.wgraf.properties.simple;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.TransitionProperty;
import at.bestsolution.wgraf.transition.Transition;
import at.bestsolution.wgraf.transition.ValueReader;
import at.bestsolution.wgraf.transition.ValueUpdater;

public class SimpleTransitionProperty<Type> implements TransitionProperty<Type> {

	private Type value;
	
	private Transition<Type> transition;
	
	private List<ChangeListener<Type>> listeners = new CopyOnWriteArrayList<ChangeListener<Type>>();
	
	private final ValueUpdater<Type> myUpdater = new ValueUpdater<Type>() {
		@Override
		public void updateValue(Type value) {
			SimpleTransitionProperty.this.notify(SimpleTransitionProperty.this.value, SimpleTransitionProperty.this.value = value);
		}
	};
	
	private final ValueReader<Type> myReader = new ValueReader<Type>() {
		@Override
		public Type read() {
			return value;
		}
	};
	
	public SimpleTransitionProperty() {
	}
	
	public SimpleTransitionProperty(Type initialValue) {
		this.value = initialValue;
	}
	
	protected void notify(Type oldValue, Type newValue) {
		for (ChangeListener<Type> listener : listeners) {
			try {
				listener.onChange(oldValue, newValue);
			}
			catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}
	
	@Override
	public void set(Type value) {
		if (transition == null) {
			notify(this.value, this.value = value);
		}
		else {
			transition.start(myUpdater, myReader, value);
		}
	}
	
	@Override
	public void setWithoutTransition(Type value) {
		notify(this.value, this.value = value);
	}

	@Override
	public Type get() {
		return value;
	}

	@Override
	public void registerChangeListener(ChangeListener<Type> listener) {
		listeners.add(listener);
	}

	@Override
	public void unregisterChangeListener(ChangeListener<Type> listener) {
		listeners.remove(listener);
	}

	@Override
	public void setTransition(Transition<Type> transition) {
		this.transition = transition;
	}

}
