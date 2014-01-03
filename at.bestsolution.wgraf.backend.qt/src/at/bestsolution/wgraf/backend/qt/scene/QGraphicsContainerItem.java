package at.bestsolution.wgraf.backend.qt.scene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import at.bestsolution.wgraf.backend.qt.QtConverter;
import at.bestsolution.wgraf.events.KeyEvent;
import at.bestsolution.wgraf.events.MouseEventSupport;
import at.bestsolution.wgraf.events.MouseEventSupport.MouseCoords;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.ReadOnlyProperty;
import at.bestsolution.wgraf.properties.Signal;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.properties.simple.SimpleSignal;
import at.bestsolution.wgraf.style.Background;
import at.bestsolution.wgraf.style.Backgrounds;
import at.bestsolution.wgraf.style.BaseBackground;
import at.bestsolution.wgraf.style.FillBackground;

import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QGraphicsItem.GraphicsItemFlag;
import com.trolltech.qt.gui.QGraphicsRectItem;
import com.trolltech.qt.gui.QGraphicsSceneMouseEvent;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QPainter.CompositionMode;
import com.trolltech.qt.gui.QFocusEvent;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QPainterPath;
import com.trolltech.qt.gui.QPen;
import com.trolltech.qt.gui.QStyleOptionGraphicsItem;
import com.trolltech.qt.gui.QWidget;

public class QGraphicsContainerItem extends QGraphicsRectItem {

	private QPainterPath shape;
	
	private Background background;
	
	public QGraphicsContainerItem() {
//		setFlag(GraphicsItemFlag.ItemSendsGeometryChanges, true);
//		setFlag(GraphicsItemFlag.ItemIsFocusable, true);
//		setFlag(GraphicsItemFlag.ItemIsSelectable, true);
//		setFlag(GraphicsItemFlag.ItemClipsChildrenToShape, true);
		
		setPen(QPen.NoPen);
	}
	
	private double yRadius = 0;
	private double xRadius = 0;
	
	public void setXRadius(double xRadius) {
		this.xRadius = xRadius;
	}
	
	public void setYRadius(double yRadius) {
		this.yRadius = yRadius;
	}
	
	@Override
	public void paint(QPainter painter, QStyleOptionGraphicsItem option, QWidget widget) {
		painter.setBrush(brush());
		painter.setPen(pen());
		painter.drawRoundedRect(rect(), xRadius, yRadius);
		
		
		
		// render background
		if (background != null) {
			if (background instanceof Backgrounds) {
				List<BaseBackground> bg = new ArrayList<BaseBackground>(((Backgrounds) background).backgrounds);
				Collections.reverse(bg);
				for (BaseBackground b : bg) {
					renderBackground(painter, b);
				}
			}
			else if (background instanceof BaseBackground ){
				renderBackground(painter, (BaseBackground)background);
			}
			
		}
		
	}

	private void renderBackground(QPainter painter, BaseBackground bg) {
		if (bg instanceof FillBackground) {
			renderBackground(painter, (FillBackground)bg);
		}
	}
	
	private void renderBackground(QPainter painter, FillBackground bg) {
		QBrush brush = QtConverter.convert(bg.fill);
		
		painter.setCompositionMode(CompositionMode.CompositionMode_SourceOver);
		painter.setBrush(brush);
		
		
//		painter.setPen(QColor.black);
		
		double width = boundingRect().width();
		double height = boundingRect().height();
		
		double left = bg.insets.left;
		double right = width - bg.insets.right;
		
		double top = bg.insets.top;
		double bottom = height - bg.insets.bottom;
		
		QPainterPath path = new QPainterPath();
		path.moveTo(left + bg.cornerRadii.topLeftHorizontalRadius, top);
		path.lineTo(right - bg.cornerRadii.topRightHorizontalRadius, top);
		path.arcTo(right - bg.cornerRadii.topRightHorizontalRadius * 2, top, bg.cornerRadii.topRightHorizontalRadius * 2, bg.cornerRadii.topRightVerticalRadius * 2, 90, -90);
		path.lineTo(right, bottom - bg.cornerRadii.bottomRightVerticalRadius);
		path.arcTo(right - bg.cornerRadii.bottomRightHorizontalRadius * 2, bottom - bg.cornerRadii.bottomRightVerticalRadius*2, bg.cornerRadii.bottomRightHorizontalRadius * 2, bg.cornerRadii.bottomRightVerticalRadius*2,  0, -90);
		path.lineTo(left + bg.cornerRadii.bottomLeftHorizontalRadius, bottom);
		
		path.arcTo(left, bottom - bg.cornerRadii.bottomLeftVerticalRadius*2, bg.cornerRadii.bottomLeftHorizontalRadius*2, bg.cornerRadii.bottomLeftVerticalRadius*2, -90, -90);
		path.lineTo(left, top + bg.cornerRadii.topLeftVerticalRadius);
		
		path.arcTo(left, top, bg.cornerRadii.topLeftHorizontalRadius*2, bg.cornerRadii.topLeftVerticalRadius*2, -180, -90);
		
		
		painter.drawPath(path);
		
//		QRect rect = new QRect(
//				(int)Math.round(bg.insets.left),
//				(int)Math.round(bg.insets.top),
//				(int)Math.round(boundingRect().width() - bg.insets.left - bg.insets.right),
//				(int)Math.round(boundingRect().height() - bg.insets.top - bg.insets.bottom)
//				);
		
		
		
		// no corner radii
//		painter.drawRect(rect);
		
		
		// symmetric corner radii
//		painter.drawRoundedRect(rect, xRadius, yRadius);
	}

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

	public void setBackground(Background background) {
		this.background = background;
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
