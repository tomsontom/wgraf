package at.bestsolution.wgraf.backend.qt.scene;

import at.bestsolution.wgraf.Sync;
import at.bestsolution.wgraf.backend.qt.QtBinder;
import at.bestsolution.wgraf.backend.qt.QtConverter;
import at.bestsolution.wgraf.events.KeyEvent;
import at.bestsolution.wgraf.events.MouseEventSupport;
import at.bestsolution.wgraf.geom.shape.Rectangle;
import at.bestsolution.wgraf.geom.shape.Shape;
import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.paint.Paint;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.ReadOnlyProperty;
import at.bestsolution.wgraf.properties.Signal;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.scene.BackingText;
import at.bestsolution.wgraf.style.DropShadow;
import at.bestsolution.wgraf.style.Effect;
import at.bestsolution.wgraf.style.Font;

import com.trolltech.qt.core.Qt.MouseButton;
import com.trolltech.qt.core.Qt.MouseButtons;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QGraphicsDropShadowEffect;
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
	public void mirror() {
		getNode().mirror();
	}
	
	@Override
	protected void applyClippingShape(final Shape s) {
		QPainterPath path = null;
		if (s instanceof Rectangle) {
			path = new QPainterPath();
			Rectangle r = (Rectangle)s;
			if (r.r == 0) {
				path.addRect(r.x, r.y, r.width, r.height);
			}
			else {
				path.addRoundedRect(r.x, r.y, r.width,r.height,r.r, r.r);
			}
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
			QtBinder.uniBind(text, new QtBinder.QtSetter<String>() {
				@Override
				public void doSet(String value) {
					node.setText(value);
				}
			});
			
		}
		return text;
	}
	
	
	@Override
	public void setEventSupport(MouseEventSupport support) {
	}
	
	private Property<Paint> fill = null;
	@Override
	public Property<Paint> fill() {
		if (fill == null) {
			fill = new SimpleProperty<Paint>(new Color(0, 0, 0, 255));
			QtBinder.uniBind(fill, new QtBinder.QtSetter<Paint>() {
				@Override
				public void doSet(Paint value) {
					node.setBrush(QtConverter.convert(value));
				}
			});
		}
		return fill;
	}
	
	private Property<Font> font = null;
	@Override
	public Property<Font> font() {
		if (font == null) {
			font = new SimpleProperty<Font>();
			QtBinder.uniBind(font, new QtBinder.QtSetter<Font>() {
				@Override
				public void doSet(Font value) {
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
	
	private Property<Effect> effect = null;
	@Override
	public Property<Effect> effect() {
		if (effect == null) {
			effect = new SimpleProperty<Effect>();
			QtBinder.uniBind(effect, new QtBinder.QtSetter<Effect>() {
				@Override
				public void doSet(Effect value) {
					node.setGraphicsEffect(QtConverter.convert(value));
				}
			});
		}
		return effect;
	}
}
