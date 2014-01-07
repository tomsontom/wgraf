package at.bestsolution.wgraf.style;

import at.bestsolution.wgraf.math.Vec2d;
import at.bestsolution.wgraf.paint.Color;

public class DropShadow extends Effect {
	public final Color color;
	public final Vec2d offset;
	public final double radius;
	
	public DropShadow() {
		this(new Color(63, 63, 63, 180), new Vec2d(1, 1), 3);
	}
	
	public DropShadow(Color color, Vec2d offset, double radius) {
		this.color = color;
		this.offset = offset;
		this.radius = radius;
	}
}
