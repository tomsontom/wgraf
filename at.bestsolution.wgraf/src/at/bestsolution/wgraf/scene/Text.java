package at.bestsolution.wgraf.scene;

import at.bestsolution.wgraf.paint.Paint;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.style.Font;



public class Text extends Node<BackingText> {

	
	public final Property<Paint> fill() { return backend.fill(); }
	
	public final Property<Font> font() { return backend.font(); }
	
	public final Property<String> text() {return backend.text(); }
	
	
	@Override
	protected Class internal_getBackendType() {
		return BackingText.class;
	}

}
