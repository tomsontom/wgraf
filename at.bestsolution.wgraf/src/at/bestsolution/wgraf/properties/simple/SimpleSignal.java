package at.bestsolution.wgraf.properties.simple;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import at.bestsolution.wgraf.properties.Signal;
import at.bestsolution.wgraf.properties.SignalListener;

public class SimpleSignal<Type> implements Signal<Type> {

	private List<SignalListener<Type>> listeners = new CopyOnWriteArrayList<SignalListener<Type>>();
	
	@Override
	public void signal(Type data) {
		for (SignalListener<Type> listener : listeners) {
			try {
				listener.onSignal(data);
			}
			catch (Throwable t) {
				t.printStackTrace();
			}
		}
		
	}

	@Override
	public void registerSignalListner(SignalListener<Type> listener) {
		listeners.add(listener);
	}

	@Override
	public void unregisterSignalListener(SignalListener<Type> listener) {
		listeners.remove(listener);
	}

}
