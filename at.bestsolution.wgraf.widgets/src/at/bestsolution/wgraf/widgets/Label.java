package at.bestsolution.wgraf.widgets;

import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.scene.Text;
import at.bestsolution.wgraf.style.Font;

public class Label extends Widget {

	protected final Text text;
	
	public Label() {
		text = new Text();
		text.setParent(area);
	}
	
	public Property<String> text() {
		return text.text();
	}
	
	public Property<Font> font() {
		return text.font();
	}
	
	
}
