package at.bestsolution.wgraf.backend.javafx.scene;

import javafx.scene.image.ImageView;
import at.bestsolution.wgraf.backend.javafx.JavaFxBinder;
import at.bestsolution.wgraf.backend.javafx.JavaFxConverter;
import at.bestsolution.wgraf.events.KeyEvent;
import at.bestsolution.wgraf.properties.DoubleTransitionProperty;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.Signal;
import at.bestsolution.wgraf.properties.simple.SimpleDoubleTransitionProperty;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.properties.simple.SimpleSignal;
import at.bestsolution.wgraf.scene.BackingImage;
import at.bestsolution.wgraf.style.Effect;
import at.bestsolution.wgraf.style.ImageSource;

public class JavaFxImage extends JavaFxNode<javafx.scene.image.ImageView> implements BackingImage {

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

	
	private Signal<KeyEvent> onKeyPress = null;
	public Signal<KeyEvent> onKeyPress() {
		if (onKeyPress == null) {
			onKeyPress = new SimpleSignal<KeyEvent>();
		}
		return onKeyPress;
	}

	private Property<ImageSource> image = null;
	@Override
	public Property<ImageSource> image() {
		if (image == null) {
			image = new SimpleProperty<ImageSource>();
			JavaFxBinder.uniBind(image, new JavaFxBinder.JfxSetter<ImageSource>() {
				@Override
				public void doSet(ImageSource value) {
					node.setImage(JavaFxConverter.convert(value));
				}
			});
		}
		return image;
	}

	@Override
	protected ImageView createNode() {
		ImageView w = new ImageView();
		return w;
	}

}
