package at.bestsolution.wgraf.properties;

public interface Signal<Type> {

	void signal(Type data);
	
	void registerSignalListner(SignalListener<Type> listener);
	void unregisterSignalListener(SignalListener<Type> listener);
	
}
