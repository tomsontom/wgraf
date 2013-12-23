package at.bestsolution.wgraf.style;

import at.bestsolution.wgraf.paint.Paint;

public class FillBackground extends BaseBackground {
	
	public final Paint fill;
	
	public FillBackground(Paint fill, CornerRadii radii, Insets insets) {
		super(radii, insets);
		this.fill = fill;
	}
	
}
