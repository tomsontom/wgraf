package at.bestsolution.wgraf.properties;

import at.bestsolution.wgraf.transition.Transition;

public interface DoubleTransitionProperty extends DoubleProperty {
	void setTransition(Transition<Double> transition);
	
	/**
	 * sets the property dynamically to the given value.
	 * <p>This means if a transition is configured, the value
	 * is dynamically updated by the transition<p>
	 * @param value
	 */
	void setDynamic(double value);
	
	
	/**
	 * incremets the value dynamically by delta.
	 * <p>This means if a transition is configured, the value
	 * is dynamically updated by the transition<p>
	 * @param delta
	 */
	void incrementDynamic(double delta);
	
	
	void updateDynamic(ValueUpdate<Double> update);
	
	ReadOnlyProperty<Boolean> transitionActive();
}
