package at.bestsolution.wgraf.properties;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class GroupBinding implements Binding {

	private List<Binding> bindings = new ArrayList<Binding>();
	
	public void registerBinding(Binding b) {
		bindings.add(b);
	}
	
	@Override
	public void dispose() {
		Iterator<Binding> it = bindings.iterator();
		while (it.hasNext()) {
			Binding next = it.next();
			next.dispose();
			it.remove();
		}
	}

}