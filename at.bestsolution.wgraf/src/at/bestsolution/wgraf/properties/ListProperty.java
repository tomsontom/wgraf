package at.bestsolution.wgraf.properties;

import java.util.List;



public interface ListProperty<Type> extends List<Type> {

	
	
	void registerChangeListener(ListChangeListener<Type> listener);
	void unregisterChangeListener(ListChangeListener<Type> listener);
	
}
