package at.bestsolution.wgraf.paint;

public class Color extends Paint {

	public final int red;
	public final int green;
	public final int blue;
	public final int alpha;
	
	public Color(int red, int green, int blue, int alpha) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}
	
}
