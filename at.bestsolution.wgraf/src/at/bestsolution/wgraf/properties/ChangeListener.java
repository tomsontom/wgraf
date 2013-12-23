package at.bestsolution.wgraf.properties;

public interface ChangeListener<Type> {
	void onChange(Type oldValue, Type newValue);
}
