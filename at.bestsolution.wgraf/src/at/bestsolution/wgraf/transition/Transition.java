package at.bestsolution.wgraf.transition;

import at.bestsolution.wgraf.properties.ValueUpdate;


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
	
	void startUpdate(ValueUpdater<Type> myUpdater, ValueReader<Type> myReader, ValueUpdate<Type> update);
}
