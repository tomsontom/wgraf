package at.bestsolution.wgraf.style;

public class BorderWidths {

	public final double top;
	public final double left;
	public final double right;
	public final double bottom;
	
	public BorderWidths(double top, double right, double bottom, double left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

	public BorderWidths(int width) {
		this(width, width, width, width);
	}
}
