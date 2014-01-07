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

	Pane parent;
	
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
	
	public void showPopup(Vec2d offset, Widget popup) {
		if (parent != null) {
			offset = new Vec2d(offset.x + area.x().get(), offset.y + area.y().get());
			parent.showPopup(offset, popup);
		}
		else {
			System.err.println("PARENT IS NULL: " + this);
		}
	}
	
	public boolean isPopupVisible() {
		if (parent != null) {
			return parent.isPopupVisible();
		}
		else {
			System.err.println("PARENT IS NULL: " + this);
			return false;
		}
	}
	
	public void hidePopup() {
		if (parent != null) {
			parent.hidePopup();
		}
		else {
			System.err.println("PARENT IS NULL: " + this);
		}
	}
	
	public void showModalPopup(Widget popup) {
		
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
