package at.bestsolution.wgraf.properties;

import at.bestsolution.wgraf.transition.Transition;

public interface TransitionProperty<Type> extends Property<Type> {
	void setTransition(Transition<Type> transition);
	void setWithoutTransition(Type value);
}
