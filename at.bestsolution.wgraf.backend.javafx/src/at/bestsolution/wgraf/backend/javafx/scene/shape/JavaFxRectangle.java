package at.bestsolution.wgraf.backend.javafx.scene.shape;

import javafx.scene.shape.Rectangle;
import at.bestsolution.wgraf.backend.javafx.JavaFxConverter;
import at.bestsolution.wgraf.backend.javafx.scene.JavaFxNode;
import at.bestsolution.wgraf.events.TapEvent;
import at.bestsolution.wgraf.paint.Paint;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.Signal;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.scene.shapes.BackingRectangle;

public class JavaFxRectangle extends JavaFxNode<javafx.scene.shape.Rectangle> implements BackingRectangle {

	private Property<Double> width = null;
	private Property<Double> height = null;
	private Property<Double> arcWidth = null;
	private Property<Double> arcHeight = null;
	
	private Property<Paint> fill = null;
	
	@Override
	public Property<Paint> fill() {
		if (fill == null) {
			fill = new SimpleProperty<Paint>();
			fill.registerChangeListener(new ChangeListener<Paint>() {
				@Override
				public void onChange(Paint oldValue, Paint newValue) {
					node.setFill(JavaFxConverter.convert(newValue));
				}
			});
		}
		return fill;
	}
	
	@Override
	public Property<Double> width() {
		if (width == null) {
			width = new SimpleProperty<Double>(node.getWidth());
			width.registerChangeListener(new ChangeListener<Double>() {
				@Override
				public void onChange(Double oldValue, Double newValue) {
					node.setWidth(newValue);
				}
			});
			
		}
		return width;
	}

	@Override
	public Property<Double> height() {
		if (height == null) {
			height = new SimpleProperty<Double>(node.getHeight());
			height.registerChangeListener(new ChangeListener<Double>() {
				@Override
				public void onChange(Double oldValue, Double newValue) {
					node.setHeight(newValue);
				}
			});
		}
		return height;
	}

	@Override
	public Property<Double> arcWidth() {
		if (arcWidth == null) {
			arcWidth = new SimpleProperty<Double>(node.getArcWidth());
			arcWidth.registerChangeListener(new ChangeListener<Double>() {
				@Override
				public void onChange(Double oldValue, Double newValue) {
					node.setArcWidth(newValue);
				}
			});
		}
		return arcWidth;
	}

	@Override
	public Property<Double> arcHeight() {
		if (arcHeight == null) {
			arcHeight = new SimpleProperty<Double>(node.getArcHeight());
			arcHeight.registerChangeListener(new ChangeListener<Double>() {
				@Override
				public void onChange(Double oldValue, Double newValue) {
					node.setArcHeight(newValue);
				}
			});
		}
		return arcHeight;
	}

	@Override
	protected Rectangle createNode() {
		return new javafx.scene.shape.Rectangle();
	}

	
}
