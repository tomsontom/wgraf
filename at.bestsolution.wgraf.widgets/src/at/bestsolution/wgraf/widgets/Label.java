package at.bestsolution.wgraf.widgets;

import at.bestsolution.wgraf.paint.Paint;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.scene.Text;
import at.bestsolution.wgraf.style.Font;

public class Label extends Widget {

	protected final Text text;

	public Label() {
		text = new Text();
		text.parent().set(area);
		text.cache().set(true);
		area.cache().set(true);
	}

	public Property<String> text() {
		return text.text();
	}

	public Property<Font> font() {
		return text.font();
	}

	public Property<Paint> fill() {
		return text.fill();
	}

	public Label setText(String text) {
		text().set(text);
		return this;
	}

	public Label setFont(Font font) {
		font().set(font);
		return this;
	}

	public Label setFill(Paint paint) {
		fill().set(paint);
		return this;
	}
}
