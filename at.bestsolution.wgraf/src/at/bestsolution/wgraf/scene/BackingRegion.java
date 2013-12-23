package at.bestsolution.wgraf.scene;

import at.bestsolution.wgraf.properties.Property;

public interface BackingRegion extends BackingNode {

	Property<Double> width();
	Property<Double> height();

}
