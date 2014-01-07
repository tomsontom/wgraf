package at.bestsolution.wgraf.properties.simple;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.ValueUpdate;

public class SimpleProperty<Type> implements Property<Type> {

	protected volatile Type value;
	
	private List<ChangeListener<Type>> listeners = new CopyOnWriteArrayList<ChangeListener<Type>>();
	
	public SimpleProperty() {
	}
	
	public SimpleProperty(Type initialValue) {
		this.value = initialValue;
	}
	
	protected void notify(Type oldValue, Type newValue) {
		if (oldValue != newValue) {
			for (ChangeListener<Type> listener : listeners) {
				try {
					listener.onChange(oldValue, newValue);
				}
				catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void set(Type value) {
		notify(this.value, this.value = value);
	}
	
	@Override
	public void update(ValueUpdate<Type> update) {
		notify(this.value, this.value = update.update(value));
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

}
