package at.bestsolution.wgraf.widgets;

import java.util.HashMap;
import java.util.Map;

import at.bestsolution.wgraf.math.Rect;
import at.bestsolution.wgraf.math.Vec2d;
import at.bestsolution.wgraf.properties.DoubleProperty;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.ReadOnlyProperty;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
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
	
	private Property<Boolean> enabled = new SimpleProperty<Boolean>(true);
	public Property<Boolean> enabled() {
		return enabled;
	}
	
	
	// bounds
	public DoubleProperty width() {
		return area.width();
	}
	
	public DoubleProperty height() {
		return area.height();
	}
	
	public DoubleProperty x() {
		return area.x();
	}
	
	public DoubleProperty y() {
		return area.y();
	}
	
	// TODO add bounds
//	public Property<Rect> bounds() {
//		return area.bounds();
//	}
	
	
	public Container getAreaNode() {
		return area;
	}
	
	public Vec2d computePreferredSize() {
		return area.computePreferredSize();
	}
	
	public void showPopup(Popup popup) {
		if (parent != null) {
//			offset = new Vec2d(offset.x + area.x().get(), offset.y + area.y().get());
			parent.showPopup(popup);
		}
		else {
			System.err.println("PARENT IS NULL: " + this);
		}
	}
	
	public boolean isPopupVisible(Popup popup) {
		if (parent != null) {
			return parent.isPopupVisible(popup);
		}
		else {
			System.err.println("PARENT IS NULL: " + this);
			return false;
		}
	}
	
	public void hidePopup(Popup popup) {
		if (parent != null) {
			parent.hidePopup(popup);
		}
		else {
			System.err.println("PARENT IS NULL: " + this);
		}
	}
	
	public void hideAllPopups() {
		if (parent != null) {
			parent.hideAllPopups();
		}
		else {
			System.err.println("PARENT IS NULL: " + this);
		}
	}
	
	public void setBounds(double x, double y, double width, double height) {
		area.x().set(x);
		area.y().set(y);
		area.width().set(width);
		area.height().set(height);
	}
	
	@Override
	public String toString() {
		String r = getClass().getSimpleName();
		if (id != null) {
			r+= "#" + id;
		}
		return r;
	}
	
	public void layout() {
		// do nothing by default
	}
	
	protected void restrictViewport(Rect viewport) {
	}
}
