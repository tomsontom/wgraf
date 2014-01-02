package at.bestsolution.wgraf.scene;

import at.bestsolution.wgraf.properties.DoubleTransitionProperty;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.style.Background;

public interface BackingContainer extends BackingNode {

	DoubleTransitionProperty width();
	DoubleTransitionProperty height();
	Property<Background> background();

}
