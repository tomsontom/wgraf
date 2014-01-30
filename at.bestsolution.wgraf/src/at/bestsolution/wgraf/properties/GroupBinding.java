package at.bestsolution.wgraf.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import at.bestsolution.wgraf.properties.binding.Binding;


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

	public void registerBindings(Binding ...bindings) {
		this.bindings.addAll(Arrays.asList(bindings));
	}

}
