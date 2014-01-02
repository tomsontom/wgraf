package at.bestsolution.wgraf.transition;

import at.bestsolution.wgraf.interpolator.Interpolator;

public class TouchScrollTransition implements Transition<Double> {

	private ValueUpdater<Double> updater;
	private ValueReader<Double> reader;
	
	private double targetValue;
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
	public void startIncrement(ValueUpdater<Double> updater, ValueReader<Double> reader, double delta) {
		this.updater = updater;
		this.reader = reader;
		this.targetValue += delta;
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
	
	

}
