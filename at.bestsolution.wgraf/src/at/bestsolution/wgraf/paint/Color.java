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

	@Override
	public String toString() {
		return "Color [red=" + red + ", green=" + green + ", blue=" + blue
				+ ", alpha=" + alpha + "]";
	}

	public static Color black() {
		return blackAlpha(255);
	}

	public static Color blackAlpha(int alpha) {
		return new Color(0, 0, 0, alpha);
	}

	public static Color rgb(int r, int g, int b) {
		return rgba(r,g,b,255);
	}

	public static Color rgba(int r, int g, int b, int alpha) {
		return new Color(r, g, b, alpha);
	}
}
