package at.bestsolution.wgraf.properties;

import at.bestsolution.wgraf.transition.Transition;

public interface DoubleTransitionProperty extends DoubleProperty {
	void setTransition(Transition<Double> transition);
}
