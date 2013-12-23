package at.bestsolution.wgraf.backend.qt;

import java.util.Queue;
import java.util.LinkedList;

import at.bestsolution.wgraf.BackingApplication;
import at.bestsolution.wgraf.backend.qt.scene.QtContainer;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.scene.Container;

import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QGraphicsScene;
import com.trolltech.qt.gui.QGraphicsView;
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
		
		if (init != null) {
			init.run();
		}
		
		scene = new QGraphicsScene();
		
		view = new QGraphicsView(scene) {
			@Override
			protected void resizeEvent(QResizeEvent event) {
				scene.setSceneRect(0, 0, event.size().width(), event.size().height());
			}
		};
		view.setWindowTitle(title().get());
		
		
		int w = (int)Math.round(root().get().width().get());
		int h = (int)Math.round(root().get().height().get());
		view.resize(w, h);
		
//		QGraphicsRectItem rect = new QGraphicsRectItem();
//		
//		scene.addItem(rect);
		
		view.setRenderHints(RenderHint.Antialiasing, RenderHint.TextAntialiasing);
		
		view.setVisible(true);
		
		view.setStyleSheet( "QGraphicsView { border-style: none; }" );
		
		scene.addItem(((QtContainer)root().get().internal_getBackend()).getNode());
		
		scene.setSceneRect(0, 0, w, h);
		
		
//		QGraphicsRectItem x = new QGraphicsRectItem();
//		x.setRect(0, 0, 100, 100);
//		x.setX(10);
//		x.setY(10);
//		
//		QGraphicsRectItem x1 = new QGraphicsRectItem();
//		x1.setRect(0,0, 20, 20);
//		x1.setParentItem(x);
//		x1.setX(20);
//		x1.setY(20);
		
//		scene.addItem(x);
		QApplication.exec();

	}
	
	private Runnable init;
	
	@Override
	public void setInit(Runnable init) {
		this.init = init;
	}

}
