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
		double tx1 = this.x;
        double ty1 = this.y;
        double rx1 = other.x;
        double ry1 = other.y;
        double tx2 = tx1; tx2 += this.width;
        double ty2 = ty1; ty2 += this.height;
        double rx2 = rx1; rx2 += other.width;
        double ry2 = ry1; ry2 += other.height;
        if (tx1 < rx1) tx1 = rx1;
        if (ty1 < ry1) ty1 = ry1;
        if (tx2 > rx2) tx2 = rx2;
        if (ty2 > ry2) ty2 = ry2;
        tx2 -= tx1;
        ty2 -= ty1;
        // tx2,ty2 will never overflow (they will never be
        // larger than the smallest of the two source w,h)
        // they might underflow, though...
        if (tx2 < Integer.MIN_VALUE) tx2 = Integer.MIN_VALUE;
        if (ty2 < Integer.MIN_VALUE) ty2 = Integer.MIN_VALUE;
        return new Rect(tx1, ty1, tx2, ty2);
	}
	
	@Override
	public String toString() {
		return "Rect(" + x + ", " + y + ", " + width + ", " + height + ")";
	}
}
