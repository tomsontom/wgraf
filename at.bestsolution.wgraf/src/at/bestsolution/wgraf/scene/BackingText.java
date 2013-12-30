package at.bestsolution.wgraf.scene;

import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.style.Font;

public interface BackingText extends BackingNode {

	Property<String> text();

	Property<Double> fontSize();

	Property<Font> font();
}
