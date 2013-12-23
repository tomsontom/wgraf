package at.bestsolution.wgraf.scene.shapes;

import at.bestsolution.wgraf.properties.Property;

public class Rectangle extends Shape<BackingRectangle> {
	
	public final Property<Double> width() { return backend.width(); }
	public final Property<Double> height() { return backend.height(); }
	public final Property<Double> arcWidth() { return backend.arcWidth(); }
	public final Property<Double> arcHeight() { return backend.arcHeight(); }
	
	@Override
	protected Class<BackingRectangle> internal_getBackendType() {
		return BackingRectangle.class;
	}
}
