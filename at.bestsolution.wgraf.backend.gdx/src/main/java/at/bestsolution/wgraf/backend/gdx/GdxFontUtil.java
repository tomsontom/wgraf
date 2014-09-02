package at.bestsolution.wgraf.backend.gdx;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;

import at.bestsolution.wgraf.math.Vec2d;
import at.bestsolution.wgraf.properties.Converter;
import at.bestsolution.wgraf.style.Font;
import at.bestsolution.wgraf.util.FontUtil;

public class GdxFontUtil extends FontUtil {
	
	@Override
	public double getBaseLine(Font font) {
		BitmapFont bf = GdxConverter.convert(font);
		return -bf.getAscent() + bf.getCapHeight();
	}

	@Override
	public Vec2d getStringExtent(Font font, String string) {
		BitmapFont bf = GdxConverter.convert(font);
		TextBounds bounds = bf.getBounds(string);
		return new Vec2d(bounds.width, -bf.getAscent() + bf.getCapHeight() - bf.getDescent());
	}

}
