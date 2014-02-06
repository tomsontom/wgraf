package at.bestsolution.wgraf.scene;

import at.bestsolution.wgraf.paint.Paint;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.style.Font;

public interface BackingText extends BackingNode {

	Property<String> text();

	Property<Font> font();

	Property<Paint> fill();
	
	void mirror();
}
