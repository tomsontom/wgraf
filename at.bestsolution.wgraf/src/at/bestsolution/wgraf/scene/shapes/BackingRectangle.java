package at.bestsolution.wgraf.scene.shapes;

import at.bestsolution.wgraf.properties.Property;

public interface BackingRectangle extends BackingShape {

	Property<Double> width();
	Property<Double> height();
	Property<Double> arcWidth();
	Property<Double> arcHeight();
	
}
