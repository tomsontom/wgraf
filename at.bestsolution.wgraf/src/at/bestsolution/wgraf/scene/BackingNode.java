package at.bestsolution.wgraf.scene;

import at.bestsolution.wgraf.events.MouseEventSupport;
import at.bestsolution.wgraf.properties.TransitionProperty;

public interface BackingNode {

	TransitionProperty<Double> x();
	TransitionProperty<Double> y();
	
	void setParent(BackingContainer parent);
	
	void setEventSupport(MouseEventSupport support);
	
}
