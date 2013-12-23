package at.bestsolution.wgraf.backend.qt.scene;

import at.bestsolution.wgraf.events.MouseEventSupport;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.TransitionProperty;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.properties.simple.SimpleTransitionProperty;
import at.bestsolution.wgraf.scene.BackingContainer;
import at.bestsolution.wgraf.style.Background;

public class QtContainer extends QtNode<QGraphicsContainerItem> implements BackingContainer {

	
	@Override
	protected QGraphicsContainerItem createNode() {
		final QGraphicsContainerItem it = new QGraphicsContainerItem();
		return it;
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

	private Property<Double> width = null;
	@Override
	public Property<Double> width() {
		if (width == null) {
			width = new SimpleProperty<Double>(node.rect().width());
			width.registerChangeListener(new ChangeListener<Double>() {
				@Override
				public void onChange(Double oldValue, Double newValue) {
					node.setRect(0, 0, newValue, node.rect().height());
				}
			});
		}
		return width;
	}
	
	
	private TransitionProperty<Double> height = null;
	@Override
	public TransitionProperty<Double> height() {
		if (height == null) {
			height = new SimpleTransitionProperty<Double>(node.rect().height());
			height.registerChangeListener(new ChangeListener<Double>() {
				@Override
				public void onChange(Double oldValue, Double newValue) {
					node.setRect(0, 0, node.rect().width(), newValue);
				}
			});
		}
		return height;
	}
}
