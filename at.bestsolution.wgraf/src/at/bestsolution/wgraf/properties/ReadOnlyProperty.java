package at.bestsolution.wgraf.properties;

public interface ReadOnlyProperty<Type> {

	Type get();
	
	void registerChangeListener(ChangeListener<Type> listener);
	void unregisterChangeListener(ChangeListener<Type> listener);
	
}
