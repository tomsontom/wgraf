package at.bestsolution.wgraf.style;

import at.bestsolution.wgraf.math.Vec2d;
import at.bestsolution.wgraf.util.FontUtil;

public class Font {

	public final String name;
	public final double pointSize;
	
	public Font(String name, double pointSize) {
		this.name = name;
		this.pointSize = pointSize;
	}
	
	public Font resize(double pointSize) {
		return new Font(name, pointSize);
	}
	
	public Font resizeBy(double factor) {
		return new Font(name, pointSize * factor);
	}
	
	public Vec2d stringExtent(String string) {
		return FontUtil.get().getStringExtent(this, string);
	}
	
	public double getBaseline() {
		return FontUtil.get().getBaseLine(this);
	}
}
