package at.bestsolution.wgraf.properties;

public interface Signal<Type> {

	void signal(Type data);
	
	void registerSignalListener(SignalListener<Type> listener);
	void unregisterSignalListener(SignalListener<Type> listener);
	
}
