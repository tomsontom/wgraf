package at.bestsolution.wgraf.properties.simple;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.DoubleChangeListener;
import at.bestsolution.wgraf.properties.DoubleTransitionProperty;
import at.bestsolution.wgraf.properties.TransitionProperty;
import at.bestsolution.wgraf.transition.Transition;
import at.bestsolution.wgraf.transition.ValueReader;
import at.bestsolution.wgraf.transition.ValueUpdater;

public class SimpleDoubleTransitionProperty implements DoubleTransitionProperty {

	private double value;
	private Transition<Double> transition;
	private List<DoubleChangeListener> listeners = new CopyOnWriteArrayList<DoubleChangeListener>();
	
	private final ValueUpdater<Double> myUpdater = new ValueUpdater<Double>() {
		@Override
		public void updateValue(Double value) {
			SimpleDoubleTransitionProperty.this.notify(SimpleDoubleTransitionProperty.this.value, SimpleDoubleTransitionProperty.this.value = value);
		}
	};
	
	private final ValueReader<Double> myReader = new ValueReader<Double>() {
		@Override
		public Double read() {
			return value;
		}
	};
	
	public SimpleDoubleTransitionProperty() {
	}
	
	public SimpleDoubleTransitionProperty(double initialValue) {
		this.value = initialValue;
	}
	
	protected void notify(double oldValue, double newValue) {
		for (DoubleChangeListener listener : listeners) {
			try {
				listener.onChange(oldValue, newValue);
			}
			catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}
	
	@Override
	public void set(double value) {
		if (transition == null) {
			notify(this.value, this.value = value);
		}
		else {
			transition.start(myUpdater, myReader, value);
		}
	}
	
	@Override
	public void increment(double delta) {
		if (transition == null) {
			notify(this.value, this.value += delta);
		}
		else {
			transition.startIncrement(myUpdater, myReader, delta);
		}
		
	}

	@Override
	public double get() {
		return value;
	}

	@Override
	public void registerChangeListener(DoubleChangeListener listener) {
		listeners.add(listener);
	}

	@Override
	public void unregisterChangeListener(DoubleChangeListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void setTransition(Transition<Double> transition) {
		this.transition = transition;
	}

}
