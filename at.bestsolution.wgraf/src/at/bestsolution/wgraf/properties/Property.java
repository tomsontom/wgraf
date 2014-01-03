package at.bestsolution.wgraf.properties;

/**
 * 
 * implementations must be thread safe
 * 
 * @author Christoph Caks
 *
 * @param <Type>
 */
public interface Property<Type> extends ReadOnlyProperty<Type> {

	void set(Type value);
	Type get();
	
	void registerChangeListener(ChangeListener<Type> listener);
	void unregisterChangeListener(ChangeListener<Type> listener);
	
	void update(ValueUpdate<Type> update);
}
