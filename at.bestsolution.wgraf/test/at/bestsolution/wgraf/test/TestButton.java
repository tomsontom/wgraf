package at.bestsolution.wgraf.test;

import at.bestsolution.wgraf.scene.Container;
import at.bestsolution.wgraf.scene.shapes.Rectangle;

public class TestButton extends Container {

	private Rectangle background;
	
	public TestButton() {
		background = new Rectangle();
		background.setParent(this);
		
	}
}
