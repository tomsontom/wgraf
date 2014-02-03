package at.bestsolution.wgraf.widgets;

import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.Signal;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.properties.simple.SimpleSignal;


public class Popup {

	public final boolean modal;
	
	private Pane content;
	
	public static enum PopupPosition {
		BOTTOM_CENTER,
		BOTTOM_RIGHT,
		CENTER
	}
	
	private Signal<Void> onCancel = new SimpleSignal<Void>();
	public Signal<Void> onCancel() {
		return onCancel;
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
	
	public Popup(Pane content, boolean modal) {
		this.content = content;
		this.modal = modal;
	}
	
	
	public Pane getContent() {
		return content;
	}
}
