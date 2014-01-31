package at.bestsolution.wgraf.backend.qt.internal.util;

import java.util.concurrent.atomic.AtomicReference;

import at.bestsolution.wgraf.Sync;
import at.bestsolution.wgraf.backend.qt.QtConverter;
import at.bestsolution.wgraf.math.Vec2d;
import at.bestsolution.wgraf.style.Font;
import at.bestsolution.wgraf.util.FontUtil;

import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QFontMetrics;

public class QtFontUtil extends FontUtil {

	private QFontMetrics getMetrics(Font font) {
		QFont qf = QtConverter.convert(font);
		QFontMetrics m = new QFontMetrics(qf);
		return m;
	}
	
	@Override
	public double getBaseLine(Font font) {
		QFontMetrics m = getMetrics(font);
		double baseline = m.ascent();
		m.dispose();
		return baseline;
	}

	@Override
	public Vec2d getStringExtent(final Font font, final String string) {
		if (string == null) throw new NullPointerException();
		QFontMetrics m = getMetrics(font);
		//QRect r = m.boundingRect(string);
		double width;
		if (string.length() > 0) {
			width = m.width(string);
		}
		else {
			width = 0;
		}
		
		int height = m.height();
		m.dispose();
		return new Vec2d(width, height);
	}

}
