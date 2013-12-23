package at.bestsolution.wgraf.scene;

import java.util.ArrayList;
import java.util.List;

import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.TransitionProperty;
import at.bestsolution.wgraf.style.Background;

public class Container extends Node<BackingContainer> {

	public final Property<Double> width() { return backend.width(); }
	public final TransitionProperty<Double> height() { return backend.height(); }
	
	public final Property<Background> background() { return backend.background(); }
	
	
	@Override
	protected Class<BackingContainer> internal_getBackendType() {
		return BackingContainer.class;
	}
	
	private List<Node> children = new ArrayList<Node>();
	
	public List<Node> getChildren() {
		return children;
	}
	
}
