package at.bestsolution.wgraf.scene;

import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.style.ImageSource;

public class Image extends Node<BackingImage> {

	public Property<ImageSource> image() {
		return backend.image();
	}
	
			
			
	@Override
	protected Class<BackingImage> internal_getBackendType() {
		return BackingImage.class;
	}
	
	

}
