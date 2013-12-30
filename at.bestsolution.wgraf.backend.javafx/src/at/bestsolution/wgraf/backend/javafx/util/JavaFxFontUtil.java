package at.bestsolution.wgraf.backend.javafx.util;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;

import at.bestsolution.wgraf.backend.javafx.JavaFxConverter;
import at.bestsolution.wgraf.math.Vec2d;
import at.bestsolution.wgraf.style.Font;
import at.bestsolution.wgraf.util.FontUtil;

public class JavaFxFontUtil extends FontUtil {

	
	@Override
	public double getBaseLine(Font font) {
		FontMetrics fm = Toolkit.getToolkit().getFontLoader().getFontMetrics(JavaFxConverter.convert(font));
		return fm.getBaseline();
	}
	
	@Override
	public Vec2d getStringExtent(Font font, String string) {
		FontMetrics fm = Toolkit.getToolkit().getFontLoader().getFontMetrics(JavaFxConverter.convert(font));
		return new Vec2d(fm.computeStringWidth(string), fm.getLineHeight());
	}
}
