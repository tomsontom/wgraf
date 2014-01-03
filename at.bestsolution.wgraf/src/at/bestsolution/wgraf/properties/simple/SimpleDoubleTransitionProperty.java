package at.bestsolution.wgraf.properties.simple;

import at.bestsolution.wgraf.properties.DoubleTransitionProperty;
import at.bestsolution.wgraf.properties.DoubleValueUpdate;
import at.bestsolution.wgraf.properties.ValueUpdate;
import at.bestsolution.wgraf.transition.Transition;
import at.bestsolution.wgraf.transition.ValueReader;
import at.bestsolution.wgraf.transition.ValueUpdater;

public class SimpleDoubleTransitionProperty extends SimpleDoubleProperty implements DoubleTransitionProperty {

	protected static class IncrementUpdate implements ValueUpdate<Double> {

		private double increment;
		
		public IncrementUpdate(double value) {
			this.increment = value;
		}
		
		@Override
		public Double update(Double current) {
			return current + increment;
		}
		
	}
	
	private Transition<Double> transition;
	
	private final ValueUpdater<Double> myUpdater = new ValueUpdater<Double>() {
		@Override
		public void updateValue(Double value) {
			SimpleDoubleTransitionProperty.this.notify(SimpleDoubleTransitionProperty.this.value, SimpleDoubleTransitionProperty.this.value = value);
		}
	};
	
	private final ValueReader<Double> myReader = new ValueReader<Double>() {
		@Override
		public Double read() {
			return value;
		}
	};
	
	public SimpleDoubleTransitionProperty() {
	}
	
	public SimpleDoubleTransitionProperty(double initialValue) {
		this.value = initialValue;
	}
	
	@Override
	public void setDynamic(double value) {
		if (transition == null) {
			notify(this.value, this.value = value);
		}
		else {
			transition.start(myUpdater, myReader, value);
		}
	}
	
	@Override
	public void updateDynamic(ValueUpdate<Double> update) {
		if (transition == null) {
			notify(this.value, this.value = update.update(value));
		}
		else {
			transition.startUpdate(myUpdater, myReader, update);
		}
	}
	
	@Override
	public void incrementDynamic(double delta) {
		if (transition == null) {
			notify(this.value, this.value += delta);
		}
		else {
			transition.startUpdate(myUpdater, myReader, new IncrementUpdate(delta));
		}
		
	}
	

	@Override
	public void setTransition(Transition<Double> transition) {
		this.transition = transition;
	}

}
