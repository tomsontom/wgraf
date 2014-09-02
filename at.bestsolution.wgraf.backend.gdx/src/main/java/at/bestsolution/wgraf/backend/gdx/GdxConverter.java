package at.bestsolution.wgraf.backend.gdx;

import java.net.URI;

import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.paint.Paint;
import at.bestsolution.wgraf.style.Font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;


public class GdxConverter {

	
	public static com.badlogic.gdx.graphics.Color convert(Paint value) {
		if (value instanceof Color) {
			Color c = (Color) value;
			return new com.badlogic.gdx.graphics.Color(c.red / 255f, c.green / 255f, c.blue / 255f, c.alpha / 255f);
		}
		else {
			throw new UnsupportedOperationException("paint " + value + " not yet supported");
		}
	}
	
	public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>";
	
	
	// XXX bitmap fonts must be cached!!!
	public static BitmapFont convert(Font font) {
		if (font == null) {
			// return default
			return new BitmapFont();
		}
		// TODO resource system needed to access font file
		// android has also issues here
		FileHandle ubuntu = Gdx.files.internal("Ubuntu-R.ttf");
		
		//FileHandle ubuntu = Gdx.files.absolute("/home/jackyinthebox/git/wgraf/at.bestsolution.wgraf/fonts/Ubuntu-R.ttf");
		FreeTypeFontGenerator g = new FreeTypeFontGenerator(ubuntu);
		
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.characters = FONT_CHARACTERS;
		parameter.flip = true;
		parameter.size = (int)Math.round(font.pointSize/0.75);
		BitmapFont f =  g.generateFont(parameter);
		f.setUseIntegerPositions(false);
		return f;
	}

}
