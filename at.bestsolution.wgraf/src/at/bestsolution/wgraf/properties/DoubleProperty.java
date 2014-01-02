package at.bestsolution.wgraf.properties;

/**
 * 
 * implementations must be thread safe
 * 
 * @author Christoph Caks
 *
 * @param <Type>
 */
public interface DoubleProperty {

	void set(double value);
	double get();
	
	void registerChangeListener(DoubleChangeListener listener);
	void unregisterChangeListener(DoubleChangeListener listener);
	
	void increment(double delta);
}
