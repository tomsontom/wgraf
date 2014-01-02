package at.bestsolution.wgraf.backend.qt;

import java.nio.channels.UnsupportedAddressTypeException;

import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.paint.LinearGradient;
import at.bestsolution.wgraf.paint.LinearGradient.CoordMode;
import at.bestsolution.wgraf.paint.LinearGradient.Spread;
import at.bestsolution.wgraf.paint.LinearGradient.Stop;
import at.bestsolution.wgraf.paint.Paint;
import at.bestsolution.wgraf.style.Font;

import com.trolltech.qt.core.Qt.BrushStyle;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QGradient.CoordinateMode;
import com.trolltech.qt.gui.QLinearGradient;

public class QtConverter {

	public static QColor convert(Color color) {
		return new QColor(color.red, color.green, color.blue, color.alpha);
	}
	
	public static CoordinateMode convert(CoordMode mode) {
		switch (mode) {
		case LOGICAL: return CoordinateMode.LogicalMode;
		case OBJECT_BOUNDING: return CoordinateMode.ObjectBoundingMode;
		}
		
		throw new UnsupportedOperationException("mode not supported: " + mode);
	}
	
	public static com.trolltech.qt.gui.QGradient.Spread convert(Spread spread) {
		switch (spread) {
		case PAD: return com.trolltech.qt.gui.QGradient.Spread.PadSpread;
		case REFLECT: return com.trolltech.qt.gui.QGradient.Spread.ReflectSpread;
		case REPEAT: return com.trolltech.qt.gui.QGradient.Spread.RepeatSpread;
		}
		
		throw new UnsupportedOperationException("spread not supported: " + spread);
	}
	
	public static QLinearGradient convert(LinearGradient gradient) {
		QLinearGradient g = new QLinearGradient();
		g.setCoordinateMode(convert(gradient.coordMode));
		g.setSpread(convert(gradient.spread));
		g.setStart(gradient.startX, gradient.startY);
		g.setFinalStop(gradient.stopX, gradient.stopY);
		for (Stop s : gradient.stops) {
			g.setColorAt(s.at, convert(s.color));
		}
		return g;
	}
	
	public static QBrush convert(Paint paint) {
		if (paint instanceof Color) {
			QBrush brush = new QBrush();
			brush.setStyle(BrushStyle.SolidPattern);
			brush.setColor(convert((Color)paint));
			return brush;
		}
		else if (paint instanceof LinearGradient) {
			QBrush brush = new QBrush(convert((LinearGradient) paint));
			return brush;
		}
	
		throw new UnsupportedOperationException("paint not supported: " + paint);
	}

	public static QFont convert(Font font) {
		if (font == null) return null;
		QFont f = new QFont(font.name);
		f.setPointSizeF(font.pointSize);
		return f;
	}
	
	
}
