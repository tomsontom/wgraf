package at.bestsolution.wgraf.scene;

import at.bestsolution.wgraf.properties.Property;



public class Text extends Node<BackingText> {

	
	public final Property<String> text() {return backend.text(); }
	public final Property<Double> fontSize() { return backend.fontSize(); }
	
	@Override
	protected Class internal_getBackendType() {
		return BackingText.class;
	}

}
