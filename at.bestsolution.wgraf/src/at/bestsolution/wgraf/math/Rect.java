package at.bestsolution.wgraf.math;

public class Rect {
	public final double x;
	public final double y;
	public final double width;
	public final double height;
	
	public Rect(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public Rect translate(double x, double y) {
		return new Rect(this.x + x, this.y + y, this.width, this.height);
	}

	public Rect intersect(Rect other) {
		// TODO
		return null;
	}
}
