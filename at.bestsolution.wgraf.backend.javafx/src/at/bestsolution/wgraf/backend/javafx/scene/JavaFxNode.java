package at.bestsolution.wgraf.backend.javafx.scene;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import at.bestsolution.wgraf.backend.javafx.JavaFxBinder;
import at.bestsolution.wgraf.events.MouseEventSupport;
import at.bestsolution.wgraf.events.MouseEventSupport.MouseCoords;
import at.bestsolution.wgraf.properties.TransitionProperty;
import at.bestsolution.wgraf.properties.simple.SimpleTransitionProperty;
import at.bestsolution.wgraf.scene.BackingContainer;
import at.bestsolution.wgraf.scene.BackingNode;

public abstract class JavaFxNode<N extends Node> implements BackingNode {
	
	protected final N node;
	
	public JavaFxNode() {
		node = createNode();
//		node.setBlendMode(BlendMode.COLOR_BURN);
	}
	
	protected abstract N createNode();
	
	public N getNode() {
		return node;
	}
	
	@Override
	public void setEventSupport(final MouseEventSupport support) {
		
		node.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				support.mousePressed().signal(new MouseCoords(event.getX(), event.getY()));
			}
		});
		
		node.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				support.mouseReleased().signal(new MouseCoords(event.getX(), event.getY()));
			}
		});
		
		node.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				support.mouseDragged().signal(new MouseCoords(event.getX(), event.getY()));
			}
		});
	}
	
	private TransitionProperty<Double> x = null;
	@Override
	public TransitionProperty<Double> x() {
		if (x == null) {
			x = new SimpleTransitionProperty<Double>(node.getLayoutX());
			JavaFxBinder.uniBind(x, new JavaFxBinder.JfxSetter<Double>() {
				@Override
				public void doSet(Double value) {
					node.setLayoutX(value);
				}
			});
		}
		return x;
	}
	
	private TransitionProperty<Double> y = null;
	@Override
	public TransitionProperty<Double> y() {
		if (y == null) {
			y = new SimpleTransitionProperty<Double>(node.getLayoutY());
			JavaFxBinder.uniBind(y, new JavaFxBinder.JfxSetter<Double>() {
				@Override
				public void doSet(Double value) {
					node.setLayoutY(value);
				}
			});
		}
		return y;
	}
	
	
	@Override
	public void setParent(BackingContainer parent) {
		JavaFxContainer p = (JavaFxContainer) parent;
		p.addChild(node);
	}

}
