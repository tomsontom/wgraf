package at.bestsolution.wgraf.widgets;

import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;


public class Popup {

	private Pane content;
	
	public static enum PopupPosition {
		BOTTOM_CENTER,
		CENTER
	}
	
	private Property<Boolean> squeezeViewport = null;
	public Property<Boolean> squeezeViewport() {
		if (squeezeViewport == null) {
			squeezeViewport = new SimpleProperty<Boolean>(false);
		}
		return squeezeViewport;
	}
	
	private Property<PopupPosition> position = null;
	public Property<PopupPosition> position() {
		if (position == null) {
			position = new SimpleProperty<Popup.PopupPosition>(PopupPosition.CENTER);
		}
		return position;
	}
	
	public Popup(Pane content) {
		this.content = content;
	}
	
	
	public Pane getContent() {
		return content;
	}
}
