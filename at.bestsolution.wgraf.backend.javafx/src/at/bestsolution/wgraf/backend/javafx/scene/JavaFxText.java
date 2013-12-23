package at.bestsolution.wgraf.backend.javafx.scene;

import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.scene.BackingText;
import javafx.geometry.VPos;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;

public class JavaFxText extends JavaFxNode<javafx.scene.text.Text> implements BackingText {

	private Property<String> text = null;
	@Override
	public Property<String> text() {
		if (text == null) {
			text = new SimpleProperty<String>();
			text.registerChangeListener(new ChangeListener<String>() {
				@Override
				public void onChange(String oldValue, String newValue) {
					node.setText(newValue);
				}
			});
			
		}
		return text;
	}
	
	private Property<Double> fontSize = null;
	public Property<Double> fontSize() {
		if (fontSize == null) {
			fontSize = new SimpleProperty<Double>();
			fontSize.registerChangeListener(new ChangeListener<Double>() {
				@Override
				public void onChange(Double oldValue, Double newValue) {
					node.setStyle("-fx-font-size: " + newValue + "pt");
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
