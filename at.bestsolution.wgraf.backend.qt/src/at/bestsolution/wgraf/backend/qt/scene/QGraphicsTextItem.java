package at.bestsolution.wgraf.backend.qt.scene;

import at.bestsolution.wgraf.events.KeyEvent;
import at.bestsolution.wgraf.events.MouseEventSupport;
import at.bestsolution.wgraf.events.MouseEventSupport.MouseCoords;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.ReadOnlyProperty;
import at.bestsolution.wgraf.properties.Signal;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.properties.simple.SimpleSignal;
import at.bestsolution.wgraf.style.Background;

import com.trolltech.qt.gui.QFocusEvent;
import com.trolltech.qt.gui.QGraphicsSceneMouseEvent;
import com.trolltech.qt.gui.QGraphicsSimpleTextItem;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QPainterPath;


public class QGraphicsTextItem extends QGraphicsSimpleTextItem {

	private QPainterPath shape;
	private MouseEventSupport support;
	
	public void setEventSupport(MouseEventSupport support) {
		this.support = support;
	}
	
	@Override
	public void mousePressEvent(QGraphicsSceneMouseEvent event) {
		if (support != null) {
			support.mousePressed().signal(new MouseCoords(event.pos().x(), event.pos().y()));
		}
	}
	
	@Override
	public void mouseReleaseEvent(QGraphicsSceneMouseEvent event) {
		if (support != null) {
			support.mouseReleased().signal(new MouseCoords(event.pos().x(), event.pos().y()));
		}
	}
	
	@Override
	public void mouseMoveEvent(QGraphicsSceneMouseEvent event) {
		if (support != null) {
			support.mouseDragged().signal(new MouseCoords(event.pos().x(), event.pos().y()));
		}
	}

	public void setShape(QPainterPath shape) {
		this.shape = shape;
	}
	
	@Override
	public QPainterPath shape() {
		if (shape == null) {
			return super.shape();
		}
		else {
			return shape;
		}
	}
	
	@Override
	public void keyPressEvent(QKeyEvent event) {
		if (onKeyPress != null) {
			onKeyPress.signal(new KeyEvent(event.key(), event.text()));
		}
	}

	private Signal<KeyEvent> onKeyPress = null;
	public Signal<KeyEvent> onKeyPress() {
		if (onKeyPress == null) {
			onKeyPress = new SimpleSignal<KeyEvent>();
		}
		return onKeyPress;
	}
	
	private Property<Boolean> focus = null;
	public ReadOnlyProperty<Boolean> focus() {
		if (focus == null) {
			focus = new SimpleProperty<Boolean>();
		}
		return focus;
	}
	
	@Override
	public void focusInEvent(QFocusEvent event) {
		if (focus != null) {
			focus.set(true);
		}
	}
	
	@Override
	public void focusOutEvent(QFocusEvent event) {
		if (focus != null) {
			focus.set(false);
		}
	}
}
