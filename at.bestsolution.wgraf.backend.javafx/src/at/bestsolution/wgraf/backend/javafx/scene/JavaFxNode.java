package at.bestsolution.wgraf.backend.javafx.scene;

import at.bestsolution.wgraf.events.MouseEventSupport;
import at.bestsolution.wgraf.events.MouseEventSupport.MouseCoords;
import at.bestsolution.wgraf.events.TapEvent;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.Signal;
import at.bestsolution.wgraf.properties.TransitionProperty;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.properties.simple.SimpleSignal;
import at.bestsolution.wgraf.properties.simple.SimpleTransitionProperty;
import at.bestsolution.wgraf.scene.BackingNode;
import at.bestsolution.wgraf.scene.BackingContainer;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.MouseEvent;

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
				System.err.println("HANDLE MOuSE on " + node + " eventX" + event.getX());
				support.mouseDragged().signal(new MouseCoords(event.getX(), event.getY()));
//				event.consume();
			}
		});
	}
	
	private TransitionProperty<Double> x = null;
	@Override
	public TransitionProperty<Double> x() {
		if (x == null) {
			x = new SimpleTransitionProperty<Double>(node.getLayoutX());
			x.registerChangeListener(new ChangeListener<Double>() {
				@Override
				public void onChange(Double oldValue, final Double newValue) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							node.setLayoutX(newValue);
						}
					});
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
			y.registerChangeListener(new ChangeListener<Double>() {
				@Override
				public void onChange(Double oldValue, Double newValue) {
					node.setLayoutY(newValue);
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
