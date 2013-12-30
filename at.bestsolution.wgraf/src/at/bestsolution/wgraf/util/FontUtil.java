package at.bestsolution.wgraf.util;

import at.bestsolution.wgraf.BackendFactory;
import at.bestsolution.wgraf.math.Vec2d;
import at.bestsolution.wgraf.style.Font;

public abstract class FontUtil {
	
	public abstract double getBaseLine(Font font);
	
	public abstract Vec2d getStringExtent(Font font, String string);
	
	private static FontUtil util;
	
	public static FontUtil get() {
		if (util == null) {
			util = BackendFactory.get().create(FontUtil.class);
		}
		return util;
	}
}
