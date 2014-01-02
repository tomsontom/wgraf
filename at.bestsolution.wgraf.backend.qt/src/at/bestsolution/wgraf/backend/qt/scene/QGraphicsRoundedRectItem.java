package at.bestsolution.wgraf.backend.qt.scene;

import com.trolltech.qt.gui.QGraphicsRectItem;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QPainterPath;
import com.trolltech.qt.gui.QStyleOptionGraphicsItem;
import com.trolltech.qt.gui.QWidget;

public class QGraphicsRoundedRectItem extends QGraphicsRectItem {

	private QPainterPath shape = null;
	
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
}
