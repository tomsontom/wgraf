package at.bestsolution.wgraf.backend.qt.internal.util;

import com.trolltech.qt.core.QRect;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QFontMetrics;

import at.bestsolution.wgraf.backend.qt.QtConverter;
import at.bestsolution.wgraf.math.Vec2d;
import at.bestsolution.wgraf.style.Font;
import at.bestsolution.wgraf.util.FontUtil;

public class QtFontUtil extends FontUtil {

	private QFontMetrics getMetrics(Font font) {
		QFont qf = QtConverter.convert(font);
		QFontMetrics m = new QFontMetrics(qf);
		return m;
	}
	
	@Override
	public double getBaseLine(Font font) {
		QFontMetrics m = getMetrics(font);
		return m.ascent();
	}

	@Override
	public Vec2d getStringExtent(Font font, String string) {
		QFontMetrics m = getMetrics(font);
		QRect r = m.boundingRect(string);
		int width = m.width(string);
		int height = m.height();
		return new Vec2d(width, height);
	}

}
