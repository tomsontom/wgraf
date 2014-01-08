package at.bestsolution.wgraf.style;


public class Insets {

	public final double top;
	public final double left;
	public final double right;
	public final double bottom;
	
	public Insets(double top, double right, double bottom, double left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

	public Insets(int width) {
		this(width, width, width, width);
	}

	@Override
	public String toString() {
		return "Insets [top=" + top + ", left=" + left + ", right=" + right
				+ ", bottom=" + bottom + "]";
	}
}
