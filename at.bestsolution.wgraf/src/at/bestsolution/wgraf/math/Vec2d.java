package at.bestsolution.wgraf.math;

import java.text.MessageFormat;

public class Vec2d {
	public final double x;
	public final double y;
	
	public Vec2d(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		return MessageFormat.format("Vec2d({0}, {1})", x, y);
	}
}
