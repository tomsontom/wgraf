package at.bestsolution.wgraf.backend.javafx.scene;

import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.text.Text;
import at.bestsolution.wgraf.backend.javafx.JavaFxBinder;
import at.bestsolution.wgraf.backend.javafx.JavaFxConverter;
import at.bestsolution.wgraf.events.KeyEvent;
import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.paint.Paint;
import at.bestsolution.wgraf.properties.DoubleTransitionProperty;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.Setter;
import at.bestsolution.wgraf.properties.Signal;
import at.bestsolution.wgraf.properties.simple.SimpleDoubleTransitionProperty;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.properties.simple.SimpleSignal;
import at.bestsolution.wgraf.scene.BackingText;
import at.bestsolution.wgraf.style.DropShadow;
import at.bestsolution.wgraf.style.Effect;
import at.bestsolution.wgraf.style.Font;

public class JavaFxText extends JavaFxNode<javafx.scene.text.Text> implements BackingText {
	
	public JavaFxText() {
		node.setOnKeyTyped(new EventHandler<javafx.scene.input.KeyEvent>() {
			@Override
			public void handle(javafx.scene.input.KeyEvent arg0) {
				if (onKeyPress != null) {
					onKeyPress.signal(new KeyEvent(JavaFxConverter.convert(arg0.getCode()), arg0.getText()));
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
	
	private Property<Paint> fill = null;
	@Override
	public Property<Paint> fill() {
		if (fill == null) {
			fill = new SimpleProperty<Paint>(new Color(0, 0, 0, 255));
			JavaFxBinder.uniBind(fill, new JavaFxBinder.JfxSetter<Paint>() {
				@Override
				public void doSet(Paint value) {
					node.setFill(JavaFxConverter.convert(value));
				}
			});
		}
		return fill;
	}
	
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
	
	
	private Property<Effect> effect = null;
	@Override
	public Property<Effect> effect() {
		if (effect == null) {
			effect = new SimpleProperty<Effect>();
			JavaFxBinder.uniBind(effect, new JavaFxBinder.JfxSetter<Effect>() {
				@Override
				public void doSet(Effect value) {
					node.setEffect(JavaFxConverter.convert(value));
				}
			});
		}
		return effect;
	}

	private DoubleTransitionProperty opacity = null;
	@Override
	public DoubleTransitionProperty opacity() {
		if (opacity == null) {
			opacity = new SimpleDoubleTransitionProperty();
			JavaFxBinder.uniBind(opacity, new JavaFxBinder.JfxSetter<Double>() {
				@Override
				public void doSet(Double value) {
					node.setStyle("-fx-opacity: " + value);
				}
			});
		}
		return opacity;
	}

	@Override
	public void mirror() {
		// TODO Auto-generated method stub
		// noop =?
	}
}
