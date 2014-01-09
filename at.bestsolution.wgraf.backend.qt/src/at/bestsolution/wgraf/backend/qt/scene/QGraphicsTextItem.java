package at.bestsolution.wgraf.backend.qt.scene;

import at.bestsolution.wgraf.backend.qt.QtConverter;
import at.bestsolution.wgraf.backend.qt.QtDebug;
import at.bestsolution.wgraf.events.FlingEvent;
import at.bestsolution.wgraf.events.KeyEvent;
import at.bestsolution.wgraf.events.ScrollEvent;
import at.bestsolution.wgraf.events.TapEvent;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.ReadOnlyProperty;
import at.bestsolution.wgraf.properties.Signal;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.properties.simple.SimpleSignal;

import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFocusEvent;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QGraphicsSimpleTextItem;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QPainterPath;
import com.trolltech.qt.gui.QPen;
import com.trolltech.qt.gui.QStyleOptionGraphicsItem;
import com.trolltech.qt.gui.QWidget;


public class QGraphicsTextItem extends QGraphicsSimpleTextItem implements QGraphicsItemInterfaceWithTapEventReceiver{

	private QPainterPath shape;
	
	public void setShape(QPainterPath shape) {
		this.shape = shape;
	}
	
	
	@Override
	public void paint(QPainter painter, QStyleOptionGraphicsItem option,
			QWidget widget) {
		super.paint(painter, option, widget);
		renderDebug(painter);
	}
	
	QColor dbg0 = new QColor(255, 0, 0, 50);
	QColor dbg1 = new QColor(255, 255, 0, 50);
	
	boolean dbgChooser = false;
	long dbgDrawCount = 0;
	
	private void renderDebug(QPainter painter) {
//		painter.setPen(QPen.NoPen);
//		painter.setBrush(dbgChooser ? dbg0 : dbg1);
//		painter.drawRect(boundingRect());
//		dbgChooser = !dbgChooser;
		
		if (QtDebug.showDrawCount) {
			painter.setPen(QColor.black);
			painter.setFont(new QFont("Sans", 6));
			painter.drawText(0, (int)boundingRect().height(), "dc " + dbgDrawCount);
			dbgDrawCount ++;
		}
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
			onKeyPress.signal(new KeyEvent(QtConverter.convertKeyCode(event.key()), event.text()));
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
			focus = new SimpleProperty<Boolean>(false);
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

	@Override
	public void sendTap(TapEvent e) {
		if (onTap != null) onTap.signal(e);
	}
	
	private Signal<TapEvent> onTap = null;
	@Override
	public Signal<TapEvent> onTap() {
		if (onTap == null) {
			onTap = new SimpleSignal<TapEvent>();
		}
		return onTap;
	}
	
	@Override
	public void sendLongTap(TapEvent e) {
		if (onLongTap != null) onLongTap.signal(e);
	}
	
	private Signal<TapEvent> onLongTap = null;
	@Override
	public Signal<TapEvent> onLongTap() {
		if (onLongTap == null) {
			onLongTap = new SimpleSignal<TapEvent>();
		}
		return onLongTap;
	}
	
	@Override
	public void sendScroll(ScrollEvent e) {
		if (onScroll != null) onScroll.signal(e);
	}
	
	private Signal<ScrollEvent> onScroll = null;
	@Override
	public Signal<ScrollEvent> onScroll() {
		if (onScroll == null) {
			onScroll = new SimpleSignal<ScrollEvent>();
		}
		return onScroll;
	}
	
	@Override
	public void sendFling(FlingEvent e) {
		if (onFling != null) onFling.signal(e);
	}
	
	private Signal<FlingEvent> onFling = null;
	@Override
	public Signal<FlingEvent> onFling() {
		if (onFling == null) {
			onFling = new SimpleSignal<FlingEvent>();
		}
		return onFling;
	}
	
	@Override
	public String toString() {
		return "QGraphicsTextItem("+boundingRect()+")@" + System.identityHashCode(this);
	}
}
