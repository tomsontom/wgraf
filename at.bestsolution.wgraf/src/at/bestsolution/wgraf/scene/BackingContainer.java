package at.bestsolution.wgraf.scene;

import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.TransitionProperty;
import at.bestsolution.wgraf.style.Background;

public interface BackingContainer extends BackingNode {

	Property<Double> width();
	TransitionProperty<Double> height();
	Property<Background> background();

}
