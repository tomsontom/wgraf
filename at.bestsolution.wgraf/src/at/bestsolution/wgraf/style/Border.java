package at.bestsolution.wgraf.style;

import java.util.Arrays;
import java.util.List;

public class Border {

//	public final Insets insets;
//	public final Insets outsets;
	
	public final List<BorderStroke> strokes;
	
	public Border(BorderStroke...strokes) {
//		this.insets = insets;
//		this.outsets = outsets;
		this.strokes = Arrays.asList(strokes);
	}
}
