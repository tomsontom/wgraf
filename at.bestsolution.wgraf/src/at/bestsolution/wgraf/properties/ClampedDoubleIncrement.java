package at.bestsolution.wgraf.properties;

public class ClampedDoubleIncrement implements ValueUpdate<Double> {

	private final double increment;
	private final double min;
	private final double max;
	
	public ClampedDoubleIncrement(double increment, double min, double max) {
		this.increment = increment;
		this.min = min;
		this.max = max;
		
	}
	
	@Override
	public Double update(Double current) {
		double newVal = current + increment;
		newVal = Math.min(max, newVal);
		newVal = Math.max(min, newVal);
		return newVal;
	}

}
