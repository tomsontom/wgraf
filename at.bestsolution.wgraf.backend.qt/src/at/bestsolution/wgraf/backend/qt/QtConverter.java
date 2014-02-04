package at.bestsolution.wgraf.backend.qt;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import at.bestsolution.wgraf.backend.qt.internal.util.QtDataLoader;
import at.bestsolution.wgraf.events.KeyCode;
import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.paint.LinearGradient;
import at.bestsolution.wgraf.paint.LinearGradient.CoordMode;
import at.bestsolution.wgraf.paint.LinearGradient.Spread;
import at.bestsolution.wgraf.paint.LinearGradient.Stop;
import at.bestsolution.wgraf.paint.Paint;
import at.bestsolution.wgraf.style.DropShadow;
import at.bestsolution.wgraf.style.Effect;
import at.bestsolution.wgraf.style.Font;

import com.trolltech.qt.core.QByteArray;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.BrushStyle;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QFontDatabase;
import com.trolltech.qt.gui.QGraphicsDropShadowEffect;
import com.trolltech.qt.gui.QGraphicsEffect;
import com.trolltech.qt.gui.QGradient.CoordinateMode;
import com.trolltech.qt.gui.QLinearGradient;

public class QtConverter {

	public static KeyCode convertKeyCode(int qtKeyCode) {
		switch (Qt.Key.resolve(qtKeyCode)) {
		case Key_Enter: return KeyCode.ENTER;
		case Key_Delete: return KeyCode.DELETE;
		case Key_Tab: return KeyCode.TAB;
		case Key_Space: return KeyCode.SPACE;
		case Key_Backspace: return KeyCode.BACKSPACE;
		case Key_Up: return KeyCode.UP;
		case Key_Down: return KeyCode.DOWN;
		case Key_Left: return KeyCode.LEFT;
		case Key_Right: return KeyCode.RIGHT;
		}
		return null;
	}
	
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
		if (font.name != null) {
			QFont f = new QFont(font.name);
			f.setPointSizeF(font.pointSize);
			return f;
		}
		else {
			int handle = QtDataLoader.loadFont(font.source);
			List<String> loadedFonts = QFontDatabase.applicationFontFamilies(handle);
			QFont f =  new QFont(loadedFonts.get(0));
			f.setPointSizeF(font.pointSize);
			return f;
		}
	}
	
	public static QGraphicsEffect convert(Effect effect) {
		if (effect == null) {
			return null;
		}
		if (effect instanceof DropShadow) {
			DropShadow d = (DropShadow) effect;
			QGraphicsDropShadowEffect e = new QGraphicsDropShadowEffect();
			e.setOffset(d.offset.x, d.offset.y);
			e.setBlurRadius(d.radius);
			e.setColor(convert(d.color));
			return e;
		}
		
		throw new UnsupportedOperationException("effect not supported: " + effect);
	}
	
}
