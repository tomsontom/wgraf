package at.bestsolution.wgraf.backend.qt.scene;

import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.TransitionProperty;
import at.bestsolution.wgraf.properties.simple.SimpleTransitionProperty;
import at.bestsolution.wgraf.scene.BackingContainer;
import at.bestsolution.wgraf.scene.BackingNode;

import com.trolltech.qt.core.QEventLoop.ProcessEventsFlag;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QGraphicsItemInterface;
import com.trolltech.qt.gui.QGraphicsItem.GraphicsItemFlag;

public abstract class QtNode<N extends QGraphicsItemInterface> implements BackingNode {

	protected final N node;
	
	public QtNode() {
		node = createNode();
		node.setFlag(GraphicsItemFlag.ItemIsMovable, true);
	}
	
	public N getNode() {
		return node;
	}
	
	protected abstract N createNode();
	
	
	private TransitionProperty<Double> x = null;
	@Override
	public TransitionProperty<Double> x() {
		if (x == null) {
			x = new SimpleTransitionProperty<Double>(node.x());
			x.registerChangeListener(new ChangeListener<Double>() {
				@Override
				public void onChange(Double oldValue, Double newValue) {
					if (QApplication.instance().thread() == Thread.currentThread()) {
						node.setX(newValue);
					}
					else {
						System.err.println("need sync to main thread!");
						node.setX(newValue);
					}
				}
			});
		}
		return x;
	}
	
	private TransitionProperty<Double> y = null;
	@Override
	public TransitionProperty<Double> y() {
		if (y == null) {
			y = new SimpleTransitionProperty<Double>(node.y());
			y.registerChangeListener(new ChangeListener<Double>() {
				@Override
				public void onChange(Double oldValue, Double newValue) {
					node.setY(newValue);
				}
			});
		}
		return y;
	}
	
	
	@Override
	public void setParent(BackingContainer parent) {
		node.setParentItem(((QtContainer)parent).node);
	}

}
