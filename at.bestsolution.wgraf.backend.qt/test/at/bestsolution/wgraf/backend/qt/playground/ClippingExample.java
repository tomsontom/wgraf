package at.bestsolution.wgraf.backend.qt.playground;

import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QGraphicsItem.GraphicsItemFlag;
import com.trolltech.qt.gui.QGraphicsRectItem;
import com.trolltech.qt.gui.QGraphicsScene;
import com.trolltech.qt.gui.QGraphicsView;
import com.trolltech.qt.gui.QPaintEngine;
import com.trolltech.qt.gui.QPainter.RenderHint;
import com.trolltech.qt.gui.QPainterPath;

public class ClippingExample {

	public static class MyItem extends QGraphicsRectItem {

		public MyItem() {
			setBrush(new QBrush(QColor.red));
			setRect(0, 0, 100, 100);
			setFlag(GraphicsItemFlag.ItemClipsToShape, true);
		}
		
		@Override
		public QPainterPath shape() {
			QPainterPath p = new QPainterPath();
			p.addEllipse(0, 0, 50, 50);
			return p;
		}
		
	}
	
	
	public static void main(String[] args) {
		QApplication.initialize(args);
		QGraphicsView myView = new QGraphicsView() {
		};
		QGraphicsScene myScene = new QGraphicsScene(myView);
		myView.setScene(myScene);
		myScene.addItem(new MyItem());
		
		myView.setRenderHint(RenderHint.HighQualityAntialiasing);
		myView.resize(300, 300);
		myView.show();
		
		QApplication.exec();
	}
}
