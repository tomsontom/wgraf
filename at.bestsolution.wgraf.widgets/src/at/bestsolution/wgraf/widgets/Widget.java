package at.bestsolution.wgraf.widgets;

import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.scene.Container;

public abstract class Widget {

	protected final Container area;
	
	public Widget() {
		area = new Container();
	}
	
	
	public Container getAreaNode() {
		return area;
	}
	
	public Property<Double> width() {
		return null;
	}
	
	public Property<Double> height() {
		return null;
	}
}
