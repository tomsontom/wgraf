package at.bestsolution.wgraf.backend.qt.scene;

import com.trolltech.qt.gui.QGraphicsRectItem;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QStyleOptionGraphicsItem;
import com.trolltech.qt.gui.QWidget;

public class QGraphicsRoundedRectItem extends QGraphicsRectItem {

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
	}
}
