package at.bestsolution.wgraf.backend.javafx.scene;

import javafx.geometry.VPos;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import at.bestsolution.wgraf.backend.javafx.JavaFxBinder;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.scene.BackingText;

public class JavaFxText extends JavaFxNode<javafx.scene.text.Text> implements BackingText {

	private Property<String> text = null;
	@Override
	public Property<String> text() {
		if (text == null) {
			text = new SimpleProperty<String>();
			JavaFxBinder.uniBind(text, new JavaFxBinder.JfxSetter<String>() {
				@Override
				public void doSet(String value) {
					node.setText(value);
				}
			});
		}
		return text;
	}
	
	private Property<Double> fontSize = null;
	public Property<Double> fontSize() {
		if (fontSize == null) {
			fontSize = new SimpleProperty<Double>();
			JavaFxBinder.uniBind(fontSize, new JavaFxBinder.JfxSetter<Double>() {
				@Override
				public void doSet(Double value) {
					node.setStyle("-fx-font-size: " + value + "pt");
				}
			});
		}
		return fontSize;
	};
	
	@Override
	protected Text createNode() {
		javafx.scene.text.Text text =  new javafx.scene.text.Text();
		text.setTextOrigin(VPos.TOP);
		text.setFontSmoothingType(FontSmoothingType.LCD);
		return text;
	}

}
