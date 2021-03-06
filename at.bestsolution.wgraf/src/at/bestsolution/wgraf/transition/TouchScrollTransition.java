package at.bestsolution.wgraf.transition;

import at.bestsolution.wgraf.interpolator.Interpolator;
import at.bestsolution.wgraf.properties.ValueUpdate;

public class TouchScrollTransition implements Transition<Double> {

	private ValueUpdater<Double> updater;
	private ValueReader<Double> reader;
	
	protected double targetValue;
	private double damping = 0.9;
	
	private static final double DIFF = 0.1;
	
	@Override
	public void start(ValueUpdater<Double> updater, ValueReader<Double> reader, Double newValue) {
		this.updater = updater;
		this.reader = reader;
		this.targetValue = newValue;
		Interpolator.Access.getInterpolator().interpolate(this);
	}
	
	@Override
	public void startUpdate(ValueUpdater<Double> updater, ValueReader<Double> reader, ValueUpdate<Double> update) {
		this.updater = updater;
		this.reader = reader;
		this.targetValue = update.update(this.targetValue);
		Interpolator.Access.getInterpolator().interpolate(this);
	}

	@Override
	public boolean update(long time) {
		final double value = reader.read();
		
		final double result = damping * value + (1 - damping) * targetValue;
		
//		System.err.println("update " + value + " -> " + result + " (" + targetValue + ")");
		
		// snap
		if (Math.abs(result - targetValue) < DIFF) {
			updater.updateValue(targetValue);
			return true; // finished
		}
		updater.updateValue(result);
		return false;
	}
	
	@Override
	public void valueSet(double value) {
		targetValue = value;
	}
	
	

}
