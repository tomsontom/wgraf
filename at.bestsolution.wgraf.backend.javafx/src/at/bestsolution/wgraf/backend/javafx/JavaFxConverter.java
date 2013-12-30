package at.bestsolution.wgraf.backend.javafx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.CycleMethod;
import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.paint.LinearGradient;
import at.bestsolution.wgraf.paint.LinearGradient.CoordMode;
import at.bestsolution.wgraf.paint.LinearGradient.Spread;
import at.bestsolution.wgraf.paint.LinearGradient.Stop;
import at.bestsolution.wgraf.paint.Paint;
import at.bestsolution.wgraf.style.Background;
import at.bestsolution.wgraf.style.Backgrounds;
import at.bestsolution.wgraf.style.BaseBackground;
import at.bestsolution.wgraf.style.CornerRadii;
import at.bestsolution.wgraf.style.FillBackground;
import at.bestsolution.wgraf.style.Font;
import at.bestsolution.wgraf.style.Insets;

public class JavaFxConverter {

	public static javafx.scene.text.Font convert(Font font) {
		if (font == null) return null;
		return javafx.scene.text.Font.font(font.name, font.pointSize/0.75);
	}
	
	public static javafx.scene.paint.Color convert(Color color) {
		return new javafx.scene.paint.Color(color.red/255.0, color.green/255.0, color.blue/255.0, color.alpha/255.0);
	}
	
	public static javafx.scene.paint.Paint convert(Paint paint) {
		if (paint instanceof Color) {
			return convert((Color)paint);
		}
		else if (paint instanceof LinearGradient) {
			return convert((LinearGradient) paint);
		}
		
		throw new UnsupportedOperationException("unkown paint type: " + paint);
	}
	
	public static CycleMethod convert(Spread spread) {
		switch (spread) {
		case PAD: return CycleMethod.NO_CYCLE;
		case REFLECT: return CycleMethod.REFLECT;
		case REPEAT: return CycleMethod.REPEAT;
		}
		
		throw new UnsupportedOperationException("spread not supported: " + spread);
	}
	
	public static List<javafx.scene.paint.Stop> convert(Stop[] stops) {
		List<javafx.scene.paint.Stop> result = new ArrayList<javafx.scene.paint.Stop>();
		for (Stop s : stops) {
			result.add(new javafx.scene.paint.Stop(s.at, convert(s.color)));
		}
		return result;
	}
	
	public static javafx.scene.paint.LinearGradient convert(LinearGradient gradient) {
		javafx.scene.paint.LinearGradient g = new javafx.scene.paint.LinearGradient(gradient.startX, gradient.startY, gradient.stopX, gradient.stopY, gradient.coordMode == CoordMode.OBJECT_BOUNDING, convert(gradient.spread), convert(gradient.stops));
		return g;
	}
	
	public static javafx.scene.layout.BackgroundFill convert(FillBackground fillBg) {
		return new javafx.scene.layout.BackgroundFill(convert(fillBg.fill), convert(fillBg.cornerRadii), convert(fillBg.insets));
	}

	public static javafx.geometry.Insets convert(Insets insets) {
		return new javafx.geometry.Insets(insets.top, insets.right, insets.bottom, insets.left);
	}

	public static javafx.scene.layout.CornerRadii convert(CornerRadii cornerRadii) {
		return new javafx.scene.layout.CornerRadii(cornerRadii.topLeftHorizontalRadius, cornerRadii.topLeftVerticalRadius, cornerRadii.topRightVerticalRadius, cornerRadii.topRightHorizontalRadius, cornerRadii.bottomRightHorizontalRadius, cornerRadii.bottomRightVerticalRadius, cornerRadii.bottomLeftVerticalRadius, cornerRadii.bottomLeftHorizontalRadius, cornerRadii.topLeftHorizontalRadiusAsPercent, cornerRadii.topLeftVerticalRadiusAsPercent, cornerRadii.topRightVerticalRadiusAsPercent, cornerRadii.topRightHorizontalRadiusAsPercent, cornerRadii.bottomRightHorizontalRadiusAsPercent, cornerRadii.bottomRightVerticalRadiusAsPercent, cornerRadii.bottomLeftVerticalRadiusAsPercent, cornerRadii.bottomLeftHorizontalRadiusAsPercent);
	}

	public static javafx.scene.layout.Background convert(Background background) {
		if (background == null) {
			return null;
		}
		else if (background instanceof Backgrounds) {
			List<BackgroundFill> bfs = new ArrayList<BackgroundFill>();
			for (BaseBackground b : ((Backgrounds) background).backgrounds) {
				if (b instanceof FillBackground) {
					BackgroundFill bf = convert((FillBackground)b);
					System.err.println(bf.getInsets());
					System.err.println(bf.getFill());
					bfs.add(bf);
				}
			}
			Collections.reverse(bfs);
			return new javafx.scene.layout.Background(bfs, Collections.EMPTY_LIST);
		}
		else if (background instanceof FillBackground) {
			return new javafx.scene.layout.Background(convert((FillBackground)background));
		}
		
		throw new UnsupportedOperationException("background not supported: " + background);
	}
}
