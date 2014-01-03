package at.bestsolution.wgraf.properties.simple;

import at.bestsolution.wgraf.properties.TransitionProperty;
import at.bestsolution.wgraf.properties.ValueUpdate;
import at.bestsolution.wgraf.transition.Transition;
import at.bestsolution.wgraf.transition.ValueReader;
import at.bestsolution.wgraf.transition.ValueUpdater;

public class SimpleTransitionProperty<Type> extends SimpleProperty<Type> implements TransitionProperty<Type> {

	private Transition<Type> transition;
	
	private final ValueUpdater<Type> myUpdater = new ValueUpdater<Type>() {
		@Override
		public void updateValue(Type value) {
			SimpleTransitionProperty.this.notify(SimpleTransitionProperty.this.value, SimpleTransitionProperty.this.value = value);
		}
	};
	
	private final ValueReader<Type> myReader = new ValueReader<Type>() {
		@Override
		public Type read() {
			return value;
		}
	};
	
	public SimpleTransitionProperty() {
	}
	
	public SimpleTransitionProperty(Type initialValue) {
		this.value = initialValue;
	}
	
	@Override
	public void setDynamic(Type value) {
		if (transition == null) {
			notify(this.value, this.value = value);
		}
		else {
			transition.start(myUpdater, myReader, value);
		}
	}
	
	@Override
	public void updateDynamic(ValueUpdate<Type> update) {
		if (transition == null) {
			notify(this.value, this.value = update.update(value));
		}
		else {
			transition.startUpdate(myUpdater, myReader, update);
		}
	}
	
	
	
	@Override
	public void setTransition(Transition<Type> transition) {
		this.transition = transition;
	}


}
