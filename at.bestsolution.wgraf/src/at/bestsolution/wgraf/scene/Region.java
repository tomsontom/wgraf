package at.bestsolution.wgraf.scene;

import java.util.List;

import at.bestsolution.wgraf.properties.Property;

public class Region extends Node<BackingRegion> {

	public final Property<Double> width() { return backend.width(); }
	public final Property<Double> height() { return backend.height(); }
	
	@Override
	protected Class<BackingRegion> internal_getBackendType() {
		return BackingRegion.class;
	}
	
	private List<Node> children;
	
	public List<Node> getChildren() {
		return children;
	}
	
}
