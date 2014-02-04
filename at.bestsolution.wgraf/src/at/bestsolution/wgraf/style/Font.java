package at.bestsolution.wgraf.style;

import at.bestsolution.wgraf.math.Vec2d;
import at.bestsolution.wgraf.util.FontUtil;

public class Font {

	public final static Font UBUNTU = new Font(FontSource.UBUNTU, 18);
	
	public final String name;
	public final FontSource source;
	public final double pointSize;
	
	public Font(String name, double pointSize) {
		this(null, name, pointSize);
	}
	
	public Font(FontSource source, double pointSize) {
		this(source, null, pointSize);
	}
	
	protected Font(FontSource source, String fontName, double pointSize) {
		this.name = fontName;
		this.source = source;
		this.pointSize = pointSize;
	}
	
	public Font resize(double pointSize) {
		return new Font(source, name, pointSize);
	}
	
	public Font resizeBy(double factor) {
		return new Font(source, name, pointSize * factor);
	}
	
	public Vec2d stringExtent(String string) {
		return FontUtil.get().getStringExtent(this, string);
	}
	
	public double getBaseline() {
		return FontUtil.get().getBaseLine(this);
	}
}
