package at.bestsolution.wgraf.scene;

import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.style.ImageSource;

public interface BackingImage extends BackingNode {

	public Property<ImageSource> image();
}
