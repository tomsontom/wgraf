package at.bestsolution.wgraf.backend.javafx.scene;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import at.bestsolution.wgraf.backend.javafx.JavaFxBinder;
import at.bestsolution.wgraf.backend.javafx.JavaFxConverter;
import at.bestsolution.wgraf.events.KeyEvent;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.Setter;
import at.bestsolution.wgraf.properties.Signal;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.properties.simple.SimpleSignal;
import at.bestsolution.wgraf.scene.BackingText;
import at.bestsolution.wgraf.style.Font;

public class JavaFxText extends JavaFxNode<javafx.scene.text.Text> implements BackingText {
	
	public JavaFxText() {
		node.setOnKeyTyped(new EventHandler<javafx.scene.input.KeyEvent>() {
			@Override
			public void handle(javafx.scene.input.KeyEvent arg0) {
				if (onKeyPress != null) {
					onKeyPress.signal(new KeyEvent(arg0.getCode().ordinal(), arg0.getText()));
				}
			}
		});
		node.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0,
					Boolean arg1, Boolean arg2) {
				if (focus != null) {
					focus.signal(arg2);
				}
			}
		});
	}
	
	private Property<Font> font = null;
	@Override
	public Property<Font> font() {
		if (font == null) {
			font = new SimpleProperty<Font>();
			JavaFxBinder.uniBind(font, new Setter<Font>()  {
				@Override
				public void set(Font value) {
					node.setFont(JavaFxConverter.convert(value));
				}
			});
		}
		return font;
	}
	
	private Property<String> text = null;
	@Override
	public Property<String> text() {
		if (text == null) {
			text = new SimpleProperty<String>();
			JavaFxBinder.uniBind(text, new JavaFxBinder.JfxSetter<String>() {
				@Override
				public void doSet(String value) {
					System.err.println("setting text to " + value);
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
//		text.setFontSmoothingType(FontSmoothingType.LCD);
		return text;
	}

	
	private Signal<KeyEvent> onKeyPress = null;
	public Signal<KeyEvent> onKeyPress() {
		if (onKeyPress == null) {
			onKeyPress = new SimpleSignal<KeyEvent>();
		}
		return onKeyPress;
	}
	
	private Signal<Boolean> focus = null;
	@Override
	public Signal<Boolean> focus() {
		if (focus == null) {
			focus = new SimpleSignal<Boolean>();
		}
		return focus;
	}
}
