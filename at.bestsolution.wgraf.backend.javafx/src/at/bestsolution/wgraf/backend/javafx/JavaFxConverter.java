package at.bestsolution.wgraf.backend.javafx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.scene.effect.BlurType;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.paint.CycleMethod;
import at.bestsolution.wgraf.events.KeyCode;
import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.paint.LinearGradient;
import at.bestsolution.wgraf.paint.LinearGradient.CoordMode;
import at.bestsolution.wgraf.paint.LinearGradient.Spread;
import at.bestsolution.wgraf.paint.LinearGradient.Stop;
import at.bestsolution.wgraf.paint.Paint;
import at.bestsolution.wgraf.style.Background;
import at.bestsolution.wgraf.style.Backgrounds;
import at.bestsolution.wgraf.style.BaseBackground;
import at.bestsolution.wgraf.style.Border;
import at.bestsolution.wgraf.style.BorderStroke;
import at.bestsolution.wgraf.style.BorderWidths;
import at.bestsolution.wgraf.style.CornerRadii;
import at.bestsolution.wgraf.style.DropShadow;
import at.bestsolution.wgraf.style.Effect;
import at.bestsolution.wgraf.style.FillBackground;
import at.bestsolution.wgraf.style.Font;
import at.bestsolution.wgraf.style.Insets;

public class JavaFxConverter {

	public static KeyCode convert(javafx.scene.input.KeyCode jfxCode) {
		System.err.println("convert jfxCode " + jfxCode);
		switch (jfxCode) {
		case ENTER: return KeyCode.ENTER;
		case DELETE: return KeyCode.DELETE;
		case TAB: return KeyCode.TAB;
		case SPACE: return KeyCode.SPACE;
		case BACK_SPACE: return KeyCode.BACKSPACE;
		case UP: return KeyCode.UP;
		case DOWN: return KeyCode.DOWN;
		case LEFT: return KeyCode.LEFT;
		case RIGHT: return KeyCode.RIGHT;
		}
		return null;
	}
	
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
	
	public static javafx.scene.effect.Effect convert(Effect effect) {
		if (effect == null) {
			return null;
		}
		if (effect instanceof DropShadow) {
			DropShadow d = (DropShadow) effect;
			javafx.scene.effect.DropShadow e = new javafx.scene.effect.DropShadow();
			e.setOffsetX(d.offset.x);
			e.setOffsetY(d.offset.y);
			e.setRadius(d.radius);
			e.setBlurType(BlurType.GAUSSIAN);
			e.setColor(convert(d.color));
			return e;
		}
		
		throw new UnsupportedOperationException("effect not supported: " + effect);
	}

	public static javafx.scene.layout.BorderWidths convert(BorderWidths widths) {
		return new javafx.scene.layout.BorderWidths(widths.top, widths.right, widths.bottom, widths.left);
	}
	
	public static javafx.scene.layout.BorderStroke convert(BorderStroke stroke) {
		return new javafx.scene.layout.BorderStroke(convert(stroke.paint), BorderStrokeStyle.SOLID, convert(stroke.cornerRadii), convert(stroke.widths), convert(stroke.insets));
	}
	
	public static javafx.scene.layout.Border convert(Border value) {
		if (value == null) {
			return null;
		}
		List<javafx.scene.layout.BorderStroke> strokes = new ArrayList<javafx.scene.layout.BorderStroke>();
		
		for (BorderStroke s : value.strokes) {
			strokes.add(convert(s));
		}
		return new javafx.scene.layout.Border(strokes, null);
	}
}
