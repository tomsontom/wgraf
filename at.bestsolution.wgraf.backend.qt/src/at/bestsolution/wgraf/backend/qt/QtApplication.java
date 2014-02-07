package at.bestsolution.wgraf.backend.qt;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import at.bestsolution.wgraf.BackingApplication;
import at.bestsolution.wgraf.backend.qt.scene.QtContainer;
import at.bestsolution.wgraf.backend.qt.scene.TapEventReceiver;
import at.bestsolution.wgraf.events.FlingEvent;
import at.bestsolution.wgraf.events.MouseEventSupport;
import at.bestsolution.wgraf.events.MouseEventSupport.MouseCoords;
import at.bestsolution.wgraf.events.ScrollEvent;
import at.bestsolution.wgraf.events.TapEvent;
import at.bestsolution.wgraf.properties.Binder;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.scene.Container;

import com.trolltech.qt.core.QPointF;
import com.trolltech.qt.core.QRectF;
import com.trolltech.qt.core.QTimer;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QGraphicsItemInterface;
import com.trolltech.qt.gui.QGraphicsScene;
import com.trolltech.qt.gui.QGraphicsSceneMouseEvent;
import com.trolltech.qt.gui.QGraphicsSimpleTextItem;
import com.trolltech.qt.gui.QGraphicsView;
import com.trolltech.qt.gui.QGraphicsView.ViewportUpdateMode;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QPainter.RenderHint;
import com.trolltech.qt.gui.QPixmapCache;
import com.trolltech.qt.gui.QResizeEvent;

public class QtApplication implements BackingApplication {

	private static final boolean debugTapEvents = false;
	
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

	private Property<Boolean> fullscreen;
	@Override
	public Property<Boolean> fullscreen() {
		if (fullscreen == null) {
			fullscreen = new SimpleProperty<Boolean>(false);
		}
		return fullscreen;
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
			
			private long frames = 0;
			
			private long begin = -1;
			
			
			private void countFrame() { 
				if (begin == -1) {
					begin = System.currentTimeMillis();
				}
				long now = System.currentTimeMillis();
				long passed = now - begin;
				if (passed > 1000) {
					System.err.println("repaints this second: " + frames);
					begin = now;
					frames = 0;
				}
				frames ++;
			}
			
			private MouseEventSupport eventSupport = new MouseEventSupport() {
				@Override
				protected Object lookupTarget(MouseCoords coords) {
					final QPointF point = new QPointF(coords.x, coords.y);
					return items(point);
				}
			};
			{
				eventSupport.tap().registerSignalListener(new SignalListener<TapEvent>() {
					@Override
					public void onSignal(TapEvent data) {
						// find nodes under tap position
						final QPointF point = new QPointF(data.x, data.y);
						List<QGraphicsItemInterface> items = (List<QGraphicsItemInterface>) eventSupport.getTarget(); //items(point);
						if (debugTapEvents) System.err.println("Tap event " + data + " on");
						
						for (QGraphicsItemInterface item : items) {
							if (debugTapEvents) System.err.println(" * " + item + ":");
							if (item instanceof TapEventReceiver) {
								final AtomicBoolean consumed = new AtomicBoolean(false);
								final QPointF itemLocalPoint = item.mapFromScene(point);
								if (debugTapEvents) System.err.println(" => sending tap " + itemLocalPoint);
								((TapEventReceiver) item).sendTap(new TapEvent(itemLocalPoint.x(), itemLocalPoint.y()) {
									@Override
									public void consume() {
										consumed.set(true);
									}
								});
								if (consumed.get()) {
									if (debugTapEvents) System.err.println(" => was consumed, stopping propagation");
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
						List<QGraphicsItemInterface> items =  (List<QGraphicsItemInterface>) eventSupport.getTarget(); //items(point);
						if (debugTapEvents) System.err.println("LongTap event " + data + " on");
						
						for (QGraphicsItemInterface item : items) {
							if (debugTapEvents) System.err.println(" * " + item + ":");
							if (item instanceof TapEventReceiver) {
								final AtomicBoolean consumed = new AtomicBoolean(false);
								final QPointF itemLocalPoint = item.mapFromScene(point);
								if (debugTapEvents) System.err.println(" => sending longtap " + itemLocalPoint);
								((TapEventReceiver) item).sendLongTap(new TapEvent(itemLocalPoint.x(), itemLocalPoint.y()) {
									@Override
									public void consume() {
										consumed.set(true);
									}
								});
								if (consumed.get()) {
									if (debugTapEvents) System.err.println(" => was consumed, stopping propagation");
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
						List<QGraphicsItemInterface> items =  (List<QGraphicsItemInterface>) eventSupport.getTarget(); //items(beginPoint);
						if (debugTapEvents) System.err.println("Scroll event " + data + " on");
						
						for (QGraphicsItemInterface item : items) {
							if (debugTapEvents) System.err.println(" * " + item + ":");
							if (item instanceof TapEventReceiver) {
								final AtomicBoolean consumed = new AtomicBoolean(false);
								final QPointF itemLocalBeginPoint = item.mapFromScene(beginPoint);
								final QPointF itemLocalPoint = item.mapFromScene(point);
								if (debugTapEvents) System.err.println(" => sending scroll " + itemLocalPoint);
								((TapEventReceiver) item).sendScroll(new ScrollEvent(itemLocalBeginPoint.x(), itemLocalBeginPoint.y(), 
										itemLocalPoint.x(), itemLocalPoint.y(), data.scrollLock, data.deltaX, data.deltaY) {
									@Override
									public void consume() {
										consumed.set(true);
									}
								});
								if (consumed.get()) {
									if (debugTapEvents) System.err.println(" => was consumed, stopping propagation");
									break;
								}
							}
							
							
						}
						
					}
				});
				
				eventSupport.fling().registerSignalListener(new SignalListener<FlingEvent>() {
					@Override
					public void onSignal(FlingEvent data) {
						// find nodes under tap position
						final QPointF beginPoint = new QPointF(data.beginX, data.beginY);
						List<QGraphicsItemInterface> items =  (List<QGraphicsItemInterface>) eventSupport.getTarget(); //items(beginPoint);
						if (debugTapEvents) System.err.println("Scroll event " + data + " on");
						
						for (QGraphicsItemInterface item : items) {
							if (debugTapEvents) System.err.println(" * " + item + ":");
							if (item instanceof TapEventReceiver) {
								final AtomicBoolean consumed = new AtomicBoolean(false);
								
								if (debugTapEvents) System.err.println(" => sending fling");
								final QPointF itemLocalBeginPoint = item.mapFromScene(beginPoint);
								((TapEventReceiver) item).sendFling(new FlingEvent(data.type, itemLocalBeginPoint.x(), itemLocalBeginPoint.y(), data.velocityX, data.velocityY) {
									@Override
									public void consume() {
										consumed.set(true);
									}
								});
								if (consumed.get()) {
									if (debugTapEvents) System.err.println(" => was consumed, stopping propagation");
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
			protected void drawBackground(QPainter painter, QRectF rect) {
				
				countFrame();
//				System.err.println("SCENE DRAW BACKGROUND");
				super.drawBackground(painter, rect);
			}
		};
		
		view = new QGraphicsView(scene) {
			@Override
			protected void resizeEvent(QResizeEvent event) {
				scene.setSceneRect(0, 0, event.size().width(), event.size().height());
				root().get().width().set(event.size().width());
				root().get().height().set(event.size().height());
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
		
		//view.setOptimizationFlag(OptimizationFlag.IndirectPainting);
		
		view.setRenderHints(
				//RenderHint.HighQualityAntialiasing, 
				RenderHint.Antialiasing, 
				RenderHint.TextAntialiasing
				);
		
		view.setViewportUpdateMode(ViewportUpdateMode.MinimalViewportUpdate);
		view.setVisible(true);
		
		QtBinder.uniBind(fullscreen(), new QtBinder.QtSetter<Boolean>() {
			@Override
			public void doSet(Boolean value) {
				if (value) {
					view.showFullScreen();
				}
				else  {
					view.showNormal();
				}
			}
		});
		
		view.setStyleSheet( "QGraphicsView { border-style: none; }" );
		
		scene.addItem(((QtContainer)root().get().internal_getBackend()).getNode());
		
//		scene.changed.connect(this, "change(List)");
		
		scene.setSceneRect(0, 0, w, h);
		
//		fps = scene.addSimpleText("fps");
		
		
//		redrawHammer.timeout.connect(this, "render()");
//		redrawHammer.setInterval(16);
//		redrawHammer.start();
		
		
		
		
		QApplication.exec();

	}

	private List<QRectF> changes = new ArrayList<QRectF>();
	
//	private QTimer redrawHammer = new QTimer();
	
	private long frame = 0;
	private long begin = -1;
	
//	private QGraphicsSimpleTextItem fps;
	
	public void render() {
		final long now = System.currentTimeMillis();
		if (begin == -1) {
			begin = now;
		}
		long duration = now - begin;
		
		
		if (duration > 5000) {
			float fps = frame / (float) duration * 1000;
//			this.fps.setText("fps: " + new DecimalFormat("##0").format(fps));
			begin = now;
			frame = 0;
		}
		if (changes.size() > 0) {
			List<QRectF> x = new ArrayList<QRectF>(changes);
			changes.clear();
//			System.err.println("frame: " + x.size());
//			System.err.println(x);
			QRectF union = new QRectF();
			for (QRectF r : x) {
				union = union.united(r);
			}
//			System.err.println("union: " + union);
			view.viewport().update(union.toAlignedRect());
		}
		frame++;
	}
	
	public void change(List<QRectF> o) {
		
//		System.err.println("CHANGE" + o);
		
		changes.addAll(o);
	}
	
	
	private Runnable init;
	
	@Override
	public void setInit(Runnable init) {
		this.init = init;
	}


	@Override
	public void stop() {
		QApplication.exit();
	}

}
