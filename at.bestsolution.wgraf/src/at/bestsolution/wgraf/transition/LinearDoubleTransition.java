package at.bestsolution.wgraf.transition;

import at.bestsolution.wgraf.interpolator.Interpolator;
import at.bestsolution.wgraf.properties.ValueUpdate;


public class LinearDoubleTransition implements Transition<Double> {

	private double targetValue;
	private ValueUpdater<Double> updater;
	private  ValueReader<Double> reader;

	private long duration;
	
	private double step;
	private double startValue;

	public LinearDoubleTransition(long duration) {
		this.duration = duration;
	}
	
	private long timePassed = 0;
	
	@Override
	public boolean update(long time) {
		timePassed += time;
		updater.updateValue(startValue + step * Math.min(timePassed, duration));
		return timePassed >= duration;
	}

	@Override
	public void start(ValueUpdater<Double> updater, ValueReader<Double> reader, Double newValue) {
		targetValue = newValue;
		this.updater = updater;
		this.reader = reader;
		this.timePassed = 0;
		startValue = reader.read();
		step = (targetValue - startValue) / duration;
		Interpolator.Access.getInterpolator().interpolate(this);
	}
	
	@Override
	public void startUpdate(ValueUpdater<Double> updater, ValueReader<Double> reader, ValueUpdate<Double> update) {
		targetValue = update.update(reader.read());
		this.updater = updater;
		this.reader = reader;
		this.timePassed = 0;
		startValue = reader.read();
		step = (targetValue - startValue) / duration;
		Interpolator.Access.getInterpolator().interpolate(this);
		
	}

	@Override
	public void valueSet(double value) {
		// TODO Auto-generated method stub
	}
}
