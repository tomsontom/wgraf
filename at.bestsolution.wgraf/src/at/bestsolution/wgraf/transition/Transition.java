package at.bestsolution.wgraf.transition;

public interface Transition<Type> {
	/**
	 * starts the transition
	 * @param updater
	 * @param newValue
	 */
	void start(ValueUpdater<Type> updater, ValueReader<Type> reader, Type newValue);
	/**
	 * 
	 * @param time
	 * @return true if the transition is finished
	 */
	boolean update(long time);
	void startIncrement(ValueUpdater<Double> updater, ValueReader<Double> reader, double delta);
}
