package at.bestsolution.wgraf.backend.qt.scene;

import at.bestsolution.wgraf.backend.qt.QtBinder;
import at.bestsolution.wgraf.backend.qt.QtConverter;
import at.bestsolution.wgraf.events.KeyEvent;
import at.bestsolution.wgraf.events.MouseEventSupport;
import at.bestsolution.wgraf.geom.shape.Rectangle;
import at.bestsolution.wgraf.geom.shape.Shape;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.DoubleTransitionProperty;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.ReadOnlyProperty;
import at.bestsolution.wgraf.properties.Signal;
import at.bestsolution.wgraf.properties.simple.SimpleDoubleTransitionProperty;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.scene.BackingContainer;
import at.bestsolution.wgraf.style.Background;
import at.bestsolution.wgraf.style.Border;
import at.bestsolution.wgraf.style.Effect;

import com.trolltech.qt.gui.QGraphicsItem.GraphicsItemFlag;
import com.trolltech.qt.gui.QPainterPath;

public class QtContainer extends QtNode<QGraphicsContainerItem> implements BackingContainer {

	
	@Override
	protected QGraphicsContainerItem createNode() {
		final QGraphicsContainerItem it = new QGraphicsContainerItem();
		
		return it;
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
	
	@Override
	public Signal<KeyEvent> onKeyPress() {
		return node.onKeyPress();
	}
	
	@Override
	public void setEventSupport(MouseEventSupport support) {
		node.setEventSupport(support);
	}
	
	private Property<Background> background = null;
	@Override
	public Property<Background> background() {
		if (background == null) {
			background = new SimpleProperty<Background>();
			background.registerChangeListener(new ChangeListener<Background>() {
				@Override
				public void onChange(Background oldValue, Background newValue) {
					node.setBackground(newValue);
				}
				
			});
		}
		return background;
	}
	
	private Property<Border> border = null;
	@Override
	public Property<Border> border() {
		if (border == null) {
			border = new SimpleProperty<Border>();
			QtBinder.uniBind(border, new QtBinder.QtSetter<Border>() {
				@Override
				public void doSet(Border value) {
					node.setBorder(value);
				}
			});
		}
		return border;
	}

	private DoubleTransitionProperty width = null;
	@Override
	public DoubleTransitionProperty width() {
		if (width == null) {
			width = new SimpleDoubleTransitionProperty(node.rect().width());
			QtBinder.uniBind(width, new QtBinder.QtSetter<Double>() {
				@Override
				public void doSet(Double value) {
					node.setRect(0, 0, value, node.rect().height());
				}
			});
		}
		return width;
	}
	
	
	private DoubleTransitionProperty height = null;
	@Override
	public DoubleTransitionProperty height() {
		if (height == null) {
			height = new SimpleDoubleTransitionProperty(node.rect().height());
			QtBinder.uniBind(height, new QtBinder.QtSetter<Double>() {
				@Override
				public void doSet(Double value) {
					node.setRect(0, 0, node.rect().width(), value);
				}
			});
		}
		return height;
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
