package at.bestsolution.wgraf.backend.qt;

import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

import at.bestsolution.wgraf.BackingApplication;
import at.bestsolution.wgraf.backend.qt.scene.QtContainer;
import at.bestsolution.wgraf.backend.qt.scene.TapEventReceiver;
import at.bestsolution.wgraf.events.MouseEventSupport;
import at.bestsolution.wgraf.events.ScrollEvent;
import at.bestsolution.wgraf.events.TapEvent;
import at.bestsolution.wgraf.events.MouseEventSupport.MouseCoords;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.scene.Container;

import com.trolltech.qt.core.QPointF;
import com.trolltech.qt.core.QRectF;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QGraphicsEllipseItem;
import com.trolltech.qt.gui.QGraphicsItemInterface;
import com.trolltech.qt.gui.QGraphicsScene;
import com.trolltech.qt.gui.QGraphicsSceneMouseEvent;
import com.trolltech.qt.gui.QGraphicsView;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QGraphicsView.OptimizationFlag;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QStyleOptionGraphicsItem;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QApplication.ColorSpec;
import com.trolltech.qt.gui.QPainter.RenderHint;
import com.trolltech.qt.gui.QResizeEvent;

public class QtApplication implements BackingApplication {

	private QGraphicsView view;
	private QGraphicsScene scene;
	
	private Property<String> title = null;
	@Override
	public Property<String> title() {
		if (title == null) {
			title = new SimpleProperty<String>();
			title.registerChangeListener(new ChangeListener<String>() {
				@Override
				public void onChange(String oldValue, String newValue) {
					if (view != null) {
						view.setWindowTitle(newValue);
					}
				}
			});
		}
		return title;
	}

	private Property<Container> root = null;
	@Override
	public Property<Container> root() {
		if (root == null) {
			root = new SimpleProperty<Container>();
			root.registerChangeListener(new ChangeListener<Container>() {
				@Override
				public void onChange(Container oldValue, Container newValue) {
					if (scene != null) {
						scene.clear();
						scene.addItem(((QtContainer)newValue.internal_getBackend()).getNode());
					}
					
				}
			});
		}
		return root;
	}
	
	
	@Override
	public void start(String[] args) {
		QApplication.initialize(args);
		
		QtSync.init();
		
		if (init != null) {
			init.run();
		}
		
		scene = new QGraphicsScene() {
			
			private MouseEventSupport eventSupport = new MouseEventSupport();
			{
				eventSupport.tap().registerSignalListener(new SignalListener<TapEvent>() {
					@Override
					public void onSignal(TapEvent data) {
						// find nodes under tap position
						final QPointF point = new QPointF(data.x, data.y);
						List<QGraphicsItemInterface> items = items(point);
						System.err.println("Tap event " + data + " on");
						
						for (QGraphicsItemInterface item : items) {
							System.err.println(" * " + item + ":");
							if (item instanceof TapEventReceiver) {
								final AtomicBoolean consumed = new AtomicBoolean(false);
								final QPointF itemLocalPoint = item.mapFromScene(point);
								System.err.println(" => sending tap " + itemLocalPoint);
								((TapEventReceiver) item).sendTap(new TapEvent(itemLocalPoint.x(), itemLocalPoint.y()) {
									@Override
									public void consume() {
										consumed.set(true);
									}
								});
								if (consumed.get()) {
									System.err.println(" => was consumed, stopping propagation");
									break;
								}
							}
							
							
						}
						
					}
				});
				
				eventSupport.longTap().registerSignalListener(new SignalListener<TapEvent>() {
					@Override
					public void onSignal(TapEvent data) {
						// find nodes under tap position
						final QPointF point = new QPointF(data.x, data.y);
						List<QGraphicsItemInterface> items = items(point);
						System.err.println("LongTap event " + data + " on");
						
						for (QGraphicsItemInterface item : items) {
							System.err.println(" * " + item + ":");
							if (item instanceof TapEventReceiver) {
								final AtomicBoolean consumed = new AtomicBoolean(false);
								final QPointF itemLocalPoint = item.mapFromScene(point);
								System.err.println(" => sending longtap " + itemLocalPoint);
								((TapEventReceiver) item).sendLongTap(new TapEvent(itemLocalPoint.x(), itemLocalPoint.y()) {
									@Override
									public void consume() {
										consumed.set(true);
									}
								});
								if (consumed.get()) {
									System.err.println(" => was consumed, stopping propagation");
									break;
								}
							}
							
							
						}
						
					}
				});
				
				eventSupport.scroll().registerSignalListener(new SignalListener<ScrollEvent>() {
					@Override
					public void onSignal(ScrollEvent data) {
						// find nodes under tap position
						final QPointF beginPoint = new QPointF(data.beginX, data.beginY);
						final QPointF point = new QPointF(data.x, data.y);
						List<QGraphicsItemInterface> items = items(beginPoint);
						System.err.println("Scroll event " + data + " on");
						
						for (QGraphicsItemInterface item : items) {
							System.err.println(" * " + item + ":");
							if (item instanceof TapEventReceiver) {
								final AtomicBoolean consumed = new AtomicBoolean(false);
								final QPointF itemLocalBeginPoint = item.mapFromScene(beginPoint);
								final QPointF itemLocalPoint = item.mapFromScene(point);
								System.err.println(" => sending scroll " + itemLocalPoint);
								((TapEventReceiver) item).sendScroll(new ScrollEvent(itemLocalBeginPoint.x(), itemLocalBeginPoint.y(), 
										itemLocalPoint.x(), itemLocalPoint.y(), data.scrollLock, data.deltaX, data.deltaY) {
									@Override
									public void consume() {
										consumed.set(true);
									}
								});
								if (consumed.get()) {
									System.err.println(" => was consumed, stopping propagation");
									break;
								}
							}
							
							
						}
						
					}
				});
			}
			
			@Override
			protected void mousePressEvent(QGraphicsSceneMouseEvent event) {
				eventSupport.mousePressed(new MouseCoords(event.scenePos().x(), event.scenePos().y()), null);
				// for now we allow the 'normal' event propagation to get the focus set
				super.mousePressEvent(event);
			}
			
			@Override
			protected void mouseReleaseEvent(QGraphicsSceneMouseEvent event) {
				eventSupport.mouseReleased(new MouseCoords(event.scenePos().x(), event.scenePos().y()), null);
				// for now we allow the 'normal' event propagation to get the focus set
				super.mouseReleaseEvent(event);
			}
			
			@Override
			protected void mouseMoveEvent(QGraphicsSceneMouseEvent event) {
				eventSupport.mouseDragged(new MouseCoords(event.scenePos().x(), event.scenePos().y()), null);
				// for now we allow the 'normal' event propagation to get the focus set
				super.mouseMoveEvent(event);
			}
			
			@Override
			protected void drawItems(QPainter painter,
					QGraphicsItemInterface[] items,
					QStyleOptionGraphicsItem[] options, QWidget widget) {
				
//				System.err.println("SCENE DRAW ITEMS " + Arrays.toString(items));
				super.drawItems(painter, items, options, widget);
			}
			
			@Override
			protected void drawBackground(QPainter painter, QRectF rect) {
//				System.err.println("SCENE DRAW BACKGROUND");
				super.drawBackground(painter, rect);
			}
			
			protected void drawForeground(QPainter painter, QRectF rect) {
//				System.err.println("SCENE DRAW FOREGROUND");
				super.drawForeground(painter, rect);
			}
		};
		
		view = new QGraphicsView(scene) {
			@Override
			protected void resizeEvent(QResizeEvent event) {
				scene.setSceneRect(0, 0, event.size().width(), event.size().height());
			}
			
			
			@Override
			protected void drawItems(QPainter painter,
					QGraphicsItemInterface[] items,
					QStyleOptionGraphicsItem[] options) {
				
//				System.err.println("VIEW DRAW ITEMS " + Arrays.toString(items));
				super.drawItems(painter, items, options);
			}
			
			@Override
			protected void drawBackground(QPainter painter, QRectF rect) {
//				System.err.println("VIEW DRAW BACKGROUND");
				super.drawBackground(painter, rect);
			}
			
			protected void drawForeground(QPainter painter, QRectF rect) {
//				System.err.println("VIEW DRAW FOREGROUND");
				super.drawForeground(painter, rect);
			}
		};
		view.setWindowTitle(title().get());
		
		
		int w = (int)Math.round(root().get().width().get());
		int h = (int)Math.round(root().get().height().get());
		view.resize(w, h);
		
//		QGraphicsRectItem rect = new QGraphicsRectItem();
//		
		
		// this flag allows to hook the drawItems method in GraphicsView / GraphicsScene
		// TODO find out how it impacts performance
		// https://qt.gitorious.org/qt/qt/source/319d4ad467364525a788c827ae04934ef4722eef:src/gui/graphicsview/qgraphicsview.cpp#L3385
		
		view.setOptimizationFlag(OptimizationFlag.IndirectPainting);
		
		view.setRenderHints(RenderHint.HighQualityAntialiasing, RenderHint.Antialiasing, RenderHint.TextAntialiasing);
		
		view.setVisible(true);
		
		view.setStyleSheet( "QGraphicsView { border-style: none; }" );
		
		scene.addItem(((QtContainer)root().get().internal_getBackend()).getNode());
		
		scene.setSceneRect(0, 0, w, h);
		
		QApplication.exec();

	}
	
	private Runnable init;
	
	@Override
	public void setInit(Runnable init) {
		this.init = init;
	}

}
