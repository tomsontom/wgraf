package at.bestsolution.wgraf.scene.shapes;

import at.bestsolution.wgraf.paint.Paint;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.scene.BackingNode;

public interface BackingShape extends BackingNode {

	Property<Paint> fill();
	
}
