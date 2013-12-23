package at.bestsolution.wgraf.backend.qt.scene.shapes;

import at.bestsolution.wgraf.backend.qt.QtConverter;
import at.bestsolution.wgraf.backend.qt.scene.QGraphicsContainerItem;
import at.bestsolution.wgraf.backend.qt.scene.QtNode;
import at.bestsolution.wgraf.events.MouseEventSupport;
import at.bestsolution.wgraf.paint.Paint;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.scene.shapes.BackingRectangle;

public class QtRectangle extends QtNode<QGraphicsContainerItem> implements BackingRectangle {

	private Property<Paint> fill = null;
	
	@Override
	public Property<Paint> fill() {
		if (fill == null) {
			fill = new SimpleProperty<Paint>();
			fill.registerChangeListener(new ChangeListener<Paint>() {
				@Override
				public void onChange(Paint oldValue, Paint newValue) {
					node.setBrush(QtConverter.convert(newValue));
				}
			});
		}
		return fill;
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
	
	
	private Property<Double> height = null;
	@Override
	public Property<Double> height() {
		if (height == null) {
			height = new SimpleProperty<Double>(node.rect().height());
			height.registerChangeListener(new ChangeListener<Double>() {
				@Override
				public void onChange(Double oldValue, Double newValue) {
					node.setRect(0, 0, node.rect().width(), newValue);
				}
			});
		}
		return height;
	}
	
	
	private Property<Double> arcWidth = null;
	@Override
	public Property<Double> arcWidth() {
		if (arcWidth == null) {
			arcWidth = new SimpleProperty<Double>();
			arcWidth.registerChangeListener(new ChangeListener<Double>() {
				@Override
				public void onChange(Double oldValue, Double newValue) {
					node.setXRadius(newValue/2.0);
				}
			});
		}
		return arcWidth;
	}

	private Property<Double> arcHeight = null;
	@Override
	public Property<Double> arcHeight() {
		if (arcHeight == null) {
			arcHeight = new SimpleProperty<Double>();
			arcHeight.registerChangeListener(new ChangeListener<Double>() {
				@Override
				public void onChange(Double oldValue, Double newValue) {
					node.setYRadius(newValue/2.0);
				}
			});
		}
		return arcHeight;
	}
	@Override
	protected QGraphicsContainerItem createNode() {
		QGraphicsContainerItem it = new QGraphicsContainerItem();
		return it;
	}
	@Override
	public void setEventSupport(MouseEventSupport support) {
		// TODO Auto-generated method stub
		
	}

}
