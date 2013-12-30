package at.bestsolution.wgraf.scene;

import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.style.Font;



public class Text extends Node<BackingText> {

	
	public final Property<Font> font() { return null; }
	
	public final Property<Double> fontSize() { return backend.fontSize(); }
	
	public final Property<String> text() {return backend.text(); }
	
	
	@Override
	protected Class internal_getBackendType() {
		return BackingText.class;
	}

}
