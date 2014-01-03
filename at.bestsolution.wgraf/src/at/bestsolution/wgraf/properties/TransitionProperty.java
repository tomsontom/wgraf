package at.bestsolution.wgraf.properties;

import at.bestsolution.wgraf.transition.Transition;

public interface TransitionProperty<Type> extends Property<Type> {
	
	/**
	 * sets the transition.
	 * @param transition the transition or null.
	 */
	void setTransition(Transition<Type> transition);
	
	/**
	 * sets the property dynamically to the given value.
	 * <p>This means if a transition is configured, the value
	 * is dynamically updated by the transition<p>
	 * @param value
	 */
	void setDynamic(Type value);
	
	void updateDynamic(ValueUpdate<Type> update);
}
