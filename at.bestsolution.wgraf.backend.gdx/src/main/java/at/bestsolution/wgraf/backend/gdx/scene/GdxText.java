package at.bestsolution.wgraf.backend.gdx.scene;

import at.bestsolution.wgraf.backend.gdx.GdxConverter;
import at.bestsolution.wgraf.backend.gdx.scene.SimpleText.LabelStyle;
import at.bestsolution.wgraf.paint.Paint;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.binding.Binder;
import at.bestsolution.wgraf.properties.binding.Setter;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.scene.BackingText;
import at.bestsolution.wgraf.style.Font;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class GdxText extends GdxActor<SimpleText> implements BackingText {

	private static BitmapFont createFont() {
		BitmapFont f = new BitmapFont();
		return f;
	}
	
	public GdxText() {
		super(new SimpleText("", new LabelStyle(createFont(), Color.GREEN)) {
			private ShapeRenderer renderer = new ShapeRenderer();
			
			@Override
			public void draw(Batch batch, float parentAlpha) {
				BitmapFont font = getStyle().font;
				super.draw(batch, parentAlpha);
				batch.end();
				
				// debug drawing
				float c = font.getCapHeight();
				float a = font.getAscent();
				float d = font.getDescent();
				float x = font.getXHeight();
				
			    renderer.setProjectionMatrix(batch.getProjectionMatrix());
			    renderer.setTransformMatrix(batch.getTransformMatrix());
			    renderer.translate(getX(), getY(), 0);
			    
			    // origin should be ascent line
			    
			    // at cap
			    renderer.setColor(Color.GREEN);
			    renderer.begin(ShapeType.Line);
			    renderer.line(-10, 0, 0, 0);
			    renderer.end();
			    
			    renderer.setColor(Color.MAGENTA);
			    renderer.begin(ShapeType.Line);
			    renderer.line(-10, -a, 0, -a);
			    renderer.end();
			    
			    renderer.setColor(Color.GRAY);
			    renderer.begin(ShapeType.Line);
			    renderer.line(-10, -a+c, 0, -a+c);
			    renderer.end();
			    
			    renderer.setColor(Color.BLACK);
			    renderer.begin(ShapeType.Line);
			    renderer.line(-10, -a+c-x, 0, -a+c-x);
			    renderer.end();
			    
			    
			    renderer.setColor(Color.PURPLE);
			    renderer.begin(ShapeType.Line);
			    renderer.line(-10, -a+c-d, 0, -a+c-d);
			    renderer.end();
			    
			    
			    batch.begin();
			}
			
		});
//		getActor().setAlignment(Align.top | Align.left);
//		getActor().invalidate();
	}

	private Property<String> text;
	@Override
	public Property<String> text() {
		if (text == null) {
			text = new SimpleProperty<String>();
			Binder.uniBind(text, new Setter<String>() {
				@Override
				public void set(String value) {
					getActor().setText(value);
				}
			});
		}
		return text;
	}

	private Property<Font> font;
	@Override
	public Property<Font> font() {
		if (font == null) {
			font = new SimpleProperty<Font>();
			Binder.uniBind(font, new Setter<Font>() {
				@Override
				public void set(Font value) {
					BitmapFont font = GdxConverter.convert(value);
					getActor().setStyle(new LabelStyle(font, GdxConverter.convert(fill().get())));
				}
			});
		}
		return font;
	}

	private Property<Paint> fill;
	@Override
	public Property<Paint> fill() {
		if (fill == null) {
			fill = new SimpleProperty<Paint>(at.bestsolution.wgraf.paint.Color.black());
			Binder.uniBind(fill, new Setter<Paint>() {
				@Override
				public void set(Paint value) {
					getActor().setColor(GdxConverter.convert(value));
					
					getActor().setStyle(new LabelStyle(GdxConverter.convert(font().get()), GdxConverter.convert(value)));
				}
			});
		}
		return fill;
	}

	@Override
	public void mirror() {
		// TODO what was this again?! -> add some javadoc ;-)
	}

}
