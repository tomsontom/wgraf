package at.bestsolution.wgraf.backend.qt.scene;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import com.trolltech.qt.gui.QPixmap;

import at.bestsolution.wgraf.events.KeyEvent;
import at.bestsolution.wgraf.events.MouseEventSupport;
import at.bestsolution.wgraf.geom.shape.Shape;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.ReadOnlyProperty;
import at.bestsolution.wgraf.properties.Signal;
import at.bestsolution.wgraf.properties.binding.Binder;
import at.bestsolution.wgraf.properties.binding.Setter;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.scene.BackingImage;
import at.bestsolution.wgraf.style.Effect;
import at.bestsolution.wgraf.style.ImageSource;

public class QtImage extends QtNode<QGraphicsImageItem> implements BackingImage {

	public QtImage() {
		
		Binder.uniBind(image, new Setter<ImageSource>() {
			@Override
			public void set(ImageSource value) {
				if (value == null) {
					getNode().setPixmap(null);
				}
				else {
					try {
						// load the pixmap
						QPixmap pixmap = new QPixmap();
						
						InputStream in = value.source.toURL().openStream();
						ByteArrayOutputStream o = new ByteArrayOutputStream();
						int nRead;
						byte[] data = new byte[1024];
						while ((nRead = in.read(data, 0, data.length)) != -1) {
						  o.write(data, 0, nRead);
						}
						in.close();
						
						pixmap.loadFromData(o.toByteArray());
						
						getNode().setPixmap(pixmap);
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
		});
		
	}
	
	
	@Override
	public void setEventSupport(MouseEventSupport support) {
	}

	private Property<Effect> effect = null;
	@Override
	public Property<Effect> effect() {
		if (effect == null) {
			effect = new SimpleProperty<Effect>(null);
		}
		return effect;
	}

	
	@Override
	public Signal<KeyEvent> onKeyPress() {
		return node.onKeyPress();
	}

	@Override
	public ReadOnlyProperty<Boolean> focus() {
		return node.focus();
	}

	private Property<ImageSource> image = new SimpleProperty<ImageSource>();
	
	@Override
	public Property<ImageSource> image() {
		return image;
	}

	@Override
	protected void applyClippingShape(Shape s) {
		
	}

	@Override
	protected QGraphicsImageItem createNode() {
		return new QGraphicsImageItem();
	}


}
