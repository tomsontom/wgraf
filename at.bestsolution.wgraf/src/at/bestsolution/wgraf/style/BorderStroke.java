package at.bestsolution.wgraf.style;

import at.bestsolution.wgraf.paint.Paint;

public class BorderStroke {

	public final Paint paint;
	public final CornerRadii cornerRadii;
	public final BorderWidths widths;
	public final Insets insets;
	
	public BorderStroke(Paint paint, CornerRadii radii, BorderWidths widths, Insets insets) {
		this.paint = paint;
		this.cornerRadii = radii;
		this.widths = widths;
		this.insets = insets;
	}
}
