package at.bestsolution.wgraf.geom.shape;

public class Rectangle extends Shape {

	public final double x;
	public final double y;
	public final double width;
	public final double height;

	public final double r;


	public Rectangle(double x, double y, double width, double height) {
		this.x = x;
		this.width = width;
		this.y = y;
		this.height = height;

		this.r = 0;
	}

	public Rectangle(double x, double y, double width, double height, double r) {
		this.x = x;
		this.width = width;
		this.y = y;
		this.height = height;

		this.r = r;
	}

	@Override
	public String toString() {
		return "Rectangle [x=" + x + ", y=" + y + ", width=" + width
				+ ", height=" + height + ", r=" + r + "]";
	}
}
