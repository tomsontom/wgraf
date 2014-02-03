package at.bestsolution.wgraf.style;

import java.util.Arrays;
import java.util.List;

import at.bestsolution.wgraf.paint.Color;

public class Border {

//	public final Insets insets;
//	public final Insets outsets;

	public final List<BorderStroke> strokes;

	public Border(BorderStroke...strokes) {
//		this.insets = insets;
//		this.outsets = outsets;
		this.strokes = Arrays.asList(strokes);
	}

	public static Border simpleBorder(Color color, double cornerRadii, int widths, int insets) {
		return new Border(new BorderStroke(color, new CornerRadii(cornerRadii), new BorderWidths(widths), new Insets(insets)));
	}
}