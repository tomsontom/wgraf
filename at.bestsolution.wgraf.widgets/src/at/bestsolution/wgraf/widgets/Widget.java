package at.bestsolution.wgraf.widgets;

import java.util.HashMap;
import java.util.Map;

import at.bestsolution.wgraf.math.Vec2d;
import at.bestsolution.wgraf.properties.ListProperty;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.ReadOnlyProperty;
import at.bestsolution.wgraf.properties.simple.SimpleListProperty;
import at.bestsolution.wgraf.scene.Container;

public abstract class Widget {

	private String id;
	
	public void setId(String id) {
		this.id = id;
	}
	
	private Map<String, ReadOnlyProperty<Boolean>> pseudoClassStates = new HashMap<String, ReadOnlyProperty<Boolean>>();
	
	protected void registerPseudoClassState(String name, ReadOnlyProperty<Boolean> property) {
		pseudoClassStates.put(name, property);
	}
	
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
	
	public Vec2d computePreferredSize() {
		return area.computePreferredSize();
	}
	
	@Override
	public String toString() {
		String r = getClass().getSimpleName();
		if (id != null) {
			r+= "#" + id;
		}
		return r;
	}
}
