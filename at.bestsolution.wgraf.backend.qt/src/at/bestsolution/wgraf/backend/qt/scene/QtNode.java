package at.bestsolution.wgraf.backend.qt.scene;

import at.bestsolution.wgraf.backend.qt.QtBinder;
import at.bestsolution.wgraf.backend.qt.QtSync;
import at.bestsolution.wgraf.geom.shape.Shape;
import at.bestsolution.wgraf.properties.DoubleTransitionProperty;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.ReadOnlyProperty;
import at.bestsolution.wgraf.properties.simple.SimpleDoubleTransitionProperty;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.scene.BackingContainer;
import at.bestsolution.wgraf.scene.BackingNode;

import com.trolltech.qt.core.Qt.MouseButton;
import com.trolltech.qt.core.Qt.MouseButtons;
import com.trolltech.qt.gui.QGraphicsItem.GraphicsItemFlag;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QGraphicsItemInterface;

public abstract class QtNode<N extends QGraphicsItemInterface> implements BackingNode {

	protected final N node;
	
	public QtNode() {
		node = createNode();
		node.setAcceptedMouseButtons(new MouseButtons());
		
//		node.setFlag(GraphicsItemFlag.ItemIsMovable, true);
	}
	
	protected abstract void applyClippingShape(Shape s);
	
	private Property<Boolean> acceptFocus = null;
	@Override
	public Property<Boolean> acceptFocus() {
		if (acceptFocus == null) {
			acceptFocus = new SimpleProperty<Boolean>(false);
			QtBinder.uniBind(acceptFocus, new QtBinder.QtSetter<Boolean>() {
				@Override
				public void doSet(Boolean value) {
					node.setFlag(GraphicsItemFlag.ItemIsFocusable, value);
				}
			});
		}
		return acceptFocus;
	}
	
	private Property<Boolean> acceptTapEvents = null;
	@Override
	public Property<Boolean> acceptTapEvents() {
		if (acceptTapEvents == null) {
			acceptTapEvents = new SimpleProperty<Boolean>(false);
			QtBinder.uniBind(acceptTapEvents, new QtBinder.QtSetter<Boolean>() {
				@Override
				public void doSet(Boolean value) {
					node.setAcceptedMouseButtons(value ?
							new MouseButtons(MouseButton.LeftButton):
								new MouseButtons());
				}
			});
		}
		return acceptTapEvents;
	}
	
	private Property<Shape> clippingShape = null;
	@Override
	public Property<Shape> clippingShape() {
		if (clippingShape == null) {
			clippingShape = new SimpleProperty<Shape>();
			QtBinder.uniBind(clippingShape, new QtBinder.QtSetter<Shape>() {
				@Override
				public void doSet(Shape value) {
					applyClippingShape(value);
				}
				
			});
		}
		return clippingShape;
	}
	
	
	public N getNode() {
		return node;
	}
	
	protected abstract N createNode();
	
	
	private DoubleTransitionProperty x = null;
	@Override
	public DoubleTransitionProperty x() {
		if (x == null) {
			x = new SimpleDoubleTransitionProperty(node.x());
			QtBinder.uniBind(x, new QtBinder.QtSetter<Double>() {
				@Override
				public void doSet(Double value) {
					node.setX(value);
				}
			});
		}
		return x;
	}
	
	private DoubleTransitionProperty y = null;
	@Override
	public DoubleTransitionProperty y() {
		if (y == null) {
			y = new SimpleDoubleTransitionProperty(node.y());
			QtBinder.uniBind(y, new QtBinder.QtSetter<Double>() {
				@Override
				public void doSet(Double value) {
					node.setY(value);
				}
			});
		}
		return y;
	}
	
	
	@Override
	public void setParent(final BackingContainer parent) {
		if (QApplication.instance().thread() == Thread.currentThread()) {
			node.setParentItem(((QtContainer)parent).node);
		}
		else {
			QtSync.runLater(new Runnable() {
				@Override
				public void run() {
					node.setParentItem(((QtContainer)parent).node);
				}
			});
		}
	}
	
	

	
}
