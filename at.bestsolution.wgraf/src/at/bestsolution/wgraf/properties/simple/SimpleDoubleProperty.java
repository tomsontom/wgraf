package at.bestsolution.wgraf.properties.simple;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import at.bestsolution.wgraf.properties.DoubleChangeListener;
import at.bestsolution.wgraf.properties.DoubleProperty;
import at.bestsolution.wgraf.properties.DoubleValueUpdate;
import at.bestsolution.wgraf.properties.ValueUpdate;

public class SimpleDoubleProperty implements DoubleProperty {

	protected double value;
	
	private List<DoubleChangeListener> listeners = new CopyOnWriteArrayList<DoubleChangeListener>();
	
	public SimpleDoubleProperty() {
	}
	
	public SimpleDoubleProperty(double initialValue) {
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
		notify(this.value, this.value = value);
	}
	
	@Override
	public void increment(double delta) {
		notify(this.value, this.value += delta);
	}
	
	@Override
	public void update(ValueUpdate<Double> update) {
		notify(this.value, this.value = update.update(value));
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

}
