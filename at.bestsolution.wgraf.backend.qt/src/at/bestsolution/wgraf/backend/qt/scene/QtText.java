package at.bestsolution.wgraf.backend.qt.scene;

import at.bestsolution.wgraf.backend.qt.QtBinder;
import at.bestsolution.wgraf.backend.qt.QtConverter;
import at.bestsolution.wgraf.events.KeyEvent;
import at.bestsolution.wgraf.events.MouseEventSupport;
import at.bestsolution.wgraf.geom.shape.Rectangle;
import at.bestsolution.wgraf.geom.shape.Shape;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.ReadOnlyProperty;
import at.bestsolution.wgraf.properties.Signal;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.scene.BackingText;
import at.bestsolution.wgraf.style.Font;

import com.trolltech.qt.core.Qt.MouseButton;
import com.trolltech.qt.core.Qt.MouseButtons;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QPainterPath;
import com.trolltech.qt.gui.QGraphicsItem.GraphicsItemFlag;

public class QtText extends QtNode<QGraphicsTextItem> implements BackingText {

	@Override
	protected QGraphicsTextItem createNode() {
		QGraphicsTextItem node = new QGraphicsTextItem();
		node.setAcceptedMouseButtons(new MouseButtons());
		node.setAcceptHoverEvents(false);
		return node;
	}
	
	@Override
	protected void applyClippingShape(Shape s) {
		QPainterPath path = null;
		if (s instanceof Rectangle) {
			path = new QPainterPath();
			path.addRoundedRect(((Rectangle) s).x, ((Rectangle) s).y, ((Rectangle) s).width, ((Rectangle) s).height, ((Rectangle) s).r, ((Rectangle) s).r);
		}
		node.setFlag(GraphicsItemFlag.ItemClipsToShape, path != null);
		node.setFlag(GraphicsItemFlag.ItemClipsChildrenToShape, path != null);
		node.setShape(path);
	}

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
					QFont font = node.font();
					font.setPointSizeF(newValue);
					node.setFont(font);
				}
			});
		}
		return fontSize;
	}
	
	@Override
	public void setEventSupport(MouseEventSupport support) {
	}
	
	private Property<Font> font = null;
	@Override
	public Property<Font> font() {
		if (font == null) {
			font = new SimpleProperty<Font>();
			QtBinder.uniBind(font, new QtBinder.Setter<Font>() {
				@Override
				public void set(Font value) {
					node.setFont(QtConverter.convert(value));
				}
			});
		}
		return font;
	};
	
	@Override
	public Signal<KeyEvent> onKeyPress() {
		return node.onKeyPress();
	}

	@Override
	public ReadOnlyProperty<Boolean> focus() {
		return node.focus();
	}
}
