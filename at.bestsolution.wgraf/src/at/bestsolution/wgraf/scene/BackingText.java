package at.bestsolution.wgraf.scene;

import at.bestsolution.wgraf.properties.Property;

public interface BackingText extends BackingNode {

	Property<String> text();

	Property<Double> fontSize();
}
