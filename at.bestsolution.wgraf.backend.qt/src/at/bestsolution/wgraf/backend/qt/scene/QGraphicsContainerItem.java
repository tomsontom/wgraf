package at.bestsolution.wgraf.backend.qt.scene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import at.bestsolution.wgraf.backend.qt.QtConverter;
import at.bestsolution.wgraf.backend.qt.QtDebug;
import at.bestsolution.wgraf.events.FlingEvent;
import at.bestsolution.wgraf.events.KeyEvent;
import at.bestsolution.wgraf.events.MouseEventSupport;
import at.bestsolution.wgraf.events.MouseEventSupport.MouseCoords;
import at.bestsolution.wgraf.events.ScrollEvent;
import at.bestsolution.wgraf.events.TapEvent;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.ReadOnlyProperty;
import at.bestsolution.wgraf.properties.Signal;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.properties.simple.SimpleSignal;
import at.bestsolution.wgraf.style.Background;
import at.bestsolution.wgraf.style.Backgrounds;
import at.bestsolution.wgraf.style.BaseBackground;
import at.bestsolution.wgraf.style.Border;
import at.bestsolution.wgraf.style.BorderStroke;
import at.bestsolution.wgraf.style.FillBackground;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFocusEvent;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QGraphicsRectItem;
import com.trolltech.qt.gui.QGraphicsSceneMouseEvent;
import com.trolltech.qt.gui.QImage;
import com.trolltech.qt.gui.QTransform;
import com.trolltech.qt.gui.QImage.Format;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QPainter.CompositionMode;
import com.trolltech.qt.gui.QPainterPath;
import com.trolltech.qt.gui.QPen;
import com.trolltech.qt.gui.QStyleOptionGraphicsItem;
import com.trolltech.qt.gui.QWidget;

public class QGraphicsContainerItem extends QGraphicsRectItem implements QGraphicsItemInterfaceWithTapEventReceiver {

	private static Map<String, QImage> backgroundCache = new WeakHashMap<String, QImage>();
	
	private QPainterPath shape;
	
	private Background background;
	private Border border;
	
	public QGraphicsContainerItem() {
//		setFlag(GraphicsItemFlag.ItemSendsGeometryChanges, true);
//		setFlag(GraphicsItemFlag.ItemIsFocusable, true);
//		setFlag(GraphicsItemFlag.ItemIsSelectable, true);
//		setFlag(GraphicsItemFlag.ItemClipsChildrenToShape, true);
		
		setPen(QPen.NoPen);
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
	public boolean sceneEvent(QEvent event) {
//		System.err.println(event.type());
//		System.err.println(event.getClass());
		return super.sceneEvent(event);
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
	
//		System.err.println("pet: "+painter.paintEngine().type()+" / colorCount: " + painter.device().colorCount() + " / depth: " + painter.device().depth());
//		painter.setBrush(brush());
//		painter.setPen(pen());
//		painter.drawRoundedRect(rect(), xRadius, yRadius);
		
		// render background
		renderBackground(painter);
		
		
		// renderBorder
		renderBorder(painter);
		
		renderDebug(painter);
	}
	
	QColor dbg0 = new QColor(255, 0, 0, 50);
	QColor dbg1 = new QColor(255, 255, 0, 50);
	
	boolean dbgChooser = false;
	long dbgDrawCount = 0;
	
	
	private void renderDebug(QPainter painter) {
//		painter.setBrush(dbgChooser ? dbg0 : dbg1);
//		painter.setPen(QPen.NoPen);
//		painter.drawRect(boundingRect());
//		dbgChooser = !dbgChooser;
		
		if (QtDebug.showDrawCount) {
			painter.setPen(QColor.black);
			painter.setFont(new QFont("Sans", 6));
			painter.drawText(0, (int)boundingRect().height(), "dc " + dbgDrawCount);
			dbgDrawCount ++;
		}
	}
	
	private void renderBackground(QPainter painter) {
		if (background != null) {
//			long begin = System.nanoTime();
//			final String hash = background.getHexHash();
//			
//			QImage image = null;
//			if (backgroundCache.containsKey(hash)) {
//				System.err.println("from cache");
//				 image = backgroundCache.get(hash);
//			}
//			else {
//				System.err.println("new");
//				image = new QImage(new QSize((int)Math.ceil(rect().width()), (int)Math.ceil(rect().height())), Format.Format_ARGB32);
//				QPainter imagePainter = new QPainter(image);
//				
				painter.setPen(QPen.NoPen);
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
//				imagePainter.end();
//				imagePainter.dispose();
//				
//				backgroundCache.put(hash, image);
//			}
//			
//			painter.drawImage(0, 0, image);
//			
//			long duration = (System.nanoTime() - begin) / 1000000;
//			System.err.println(this + " duration = " + duration );
		}
	}
	
	private void renderBorder(QPainter painter) {
		if (border != null) {
			for (BorderStroke s : border.strokes) {
				renderBorderStroke(painter, s);
			}
		}
	}
	
	private void renderBorderStroke(QPainter painter, BorderStroke stroke) {
		QBrush brush = QtConverter.convert(stroke.paint);
		
		painter.setCompositionMode(CompositionMode.CompositionMode_SourceOver);
		painter.setBrush(QBrush.NoBrush);
		
		QPen pen = new QPen();
		pen.setBrush(brush);
		pen.setWidthF(stroke.widths.top);
		
		painter.setPen(pen);
		
		double width = boundingRect().width() -1;
		double height = boundingRect().height() -1;
		
		double left = stroke.insets.left;
		double right = width - stroke.insets.right;
		
		double top = stroke.insets.top;
		double bottom = height - stroke.insets.bottom;
		if (
		Double.isNaN(width) ||
		Double.isNaN(height) ||
		Double.isNaN(left) ||
		Double.isNaN(right) ||
		Double.isNaN(top) ||
		Double.isNaN(bottom)
		) {
			System.err.println("nan: width=" + width + ", height="+height + ", left=" + left + ", right=" + right + ", top="+top + ", bottom="+ bottom);
			throw new RuntimeException("NaN value!");
		}
		
		if (
		Double.isNaN(stroke.cornerRadii.bottomLeftHorizontalRadius) ||
		Double.isNaN(stroke.cornerRadii.bottomRightHorizontalRadius) ||
		Double.isNaN(stroke.cornerRadii.topLeftHorizontalRadius) ||
		Double.isNaN(stroke.cornerRadii.topRightHorizontalRadius) ||
		Double.isNaN(stroke.cornerRadii.bottomLeftVerticalRadius) ||
		Double.isNaN(stroke.cornerRadii.bottomRightVerticalRadius) ||
		Double.isNaN(stroke.cornerRadii.topLeftVerticalRadius) ||
		Double.isNaN(stroke.cornerRadii.topRightVerticalRadius)
		) {
			System.err.println("nan: " + stroke.cornerRadii);
			throw new RuntimeException("NaN value!");
		}
		
		QPainterPath path = new QPainterPath();
		path.moveTo(left + stroke.cornerRadii.topLeftHorizontalRadius, top);
		path.lineTo(right - stroke.cornerRadii.topRightHorizontalRadius, top);
		path.arcTo(right - stroke.cornerRadii.topRightHorizontalRadius * 2, top, stroke.cornerRadii.topRightHorizontalRadius * 2, stroke.cornerRadii.topRightVerticalRadius * 2, 90, -90);
		path.lineTo(right, bottom - stroke.cornerRadii.bottomRightVerticalRadius);
		path.arcTo(right - stroke.cornerRadii.bottomRightHorizontalRadius * 2, bottom - stroke.cornerRadii.bottomRightVerticalRadius*2, stroke.cornerRadii.bottomRightHorizontalRadius * 2, stroke.cornerRadii.bottomRightVerticalRadius*2,  0, -90);
		path.lineTo(left + stroke.cornerRadii.bottomLeftHorizontalRadius, bottom);
		
		path.arcTo(left, bottom - stroke.cornerRadii.bottomLeftVerticalRadius*2, stroke.cornerRadii.bottomLeftHorizontalRadius*2, stroke.cornerRadii.bottomLeftVerticalRadius*2, -90, -90);
		path.lineTo(left, top + stroke.cornerRadii.topLeftVerticalRadius);
		
		path.arcTo(left, top, stroke.cornerRadii.topLeftHorizontalRadius*2, stroke.cornerRadii.topLeftVerticalRadius*2, -180, -90);
		
		
		painter.drawPath(path);
		
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
		
		if (
		Double.isNaN(width) ||
		Double.isNaN(height) ||
		Double.isNaN(left) ||
		Double.isNaN(right) ||
		Double.isNaN(top) ||
		Double.isNaN(bottom)
		) {
			System.err.println("nan: width=" + width + ", height="+height + ", left=" + left + ", right=" + right + ", top="+top + ", bottom="+ bottom);
			throw new RuntimeException("NaN value!");
		}
		
		if (
		Double.isNaN(bg.cornerRadii.bottomLeftHorizontalRadius) ||
		Double.isNaN(bg.cornerRadii.bottomRightHorizontalRadius) ||
		Double.isNaN(bg.cornerRadii.topLeftHorizontalRadius) ||
		Double.isNaN(bg.cornerRadii.topRightHorizontalRadius) ||
		Double.isNaN(bg.cornerRadii.bottomLeftVerticalRadius) ||
		Double.isNaN(bg.cornerRadii.bottomRightVerticalRadius) ||
		Double.isNaN(bg.cornerRadii.topLeftVerticalRadius) ||
		Double.isNaN(bg.cornerRadii.topRightVerticalRadius)
		) {
			System.err.println("nan: " + bg.cornerRadii);
			throw new RuntimeException("NaN value!");
		}
		
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
	
	public void setBackground(Background background) {
		this.background = background;
		update(rect());
	}
	
	public void setBorder(Border border) {
		this.border = border;
		update(rect());
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
	public String toString() {
		return "QGraphicsContainerItem("+rect()+")@" + System.identityHashCode(this);
	}


	
	
}
