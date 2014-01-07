package at.bestsolution.wgraf.scene;

import at.bestsolution.wgraf.properties.DoubleTransitionProperty;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.style.Background;
import at.bestsolution.wgraf.style.Border;

public interface BackingContainer extends BackingNode {

	DoubleTransitionProperty width();
	DoubleTransitionProperty height();
	Property<Background> background();
	Property<Border> border();

}
