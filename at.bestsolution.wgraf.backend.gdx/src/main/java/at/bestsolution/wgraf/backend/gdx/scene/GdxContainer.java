package at.bestsolution.wgraf.backend.gdx.scene;

import at.bestsolution.wgraf.properties.binding.Binder;
import at.bestsolution.wgraf.properties.DoubleChangeListener;
import at.bestsolution.wgraf.properties.DoubleTransitionProperty;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.binding.Setter;
import at.bestsolution.wgraf.properties.simple.SimpleDoubleTransitionProperty;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.scene.BackingContainer;
import at.bestsolution.wgraf.style.Background;
import at.bestsolution.wgraf.style.Border;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Group;

public class GdxContainer extends GdxActor<SimpleContainer> implements BackingContainer {

	public GdxContainer() {
		super(new SimpleContainer());
	}

	private DoubleTransitionProperty width;
	@Override
	public DoubleTransitionProperty width() {
		if (width == null) {
			width = new SimpleDoubleTransitionProperty();
			Binder.uniBind(width, new Setter<Double>() {
				@Override
				public void set(Double value) {
					getActor().setWidth(value.floatValue());
				}
			});
		}
		return width;
	}

	private DoubleTransitionProperty height;
	@Override
	public DoubleTransitionProperty height() {
		if (height == null) {
			height = new SimpleDoubleTransitionProperty();
			Binder.uniBind(height, new Setter<Double>() {
				@Override
				public void set(Double value) {
					getActor().setHeight(value.floatValue());
				}
			});
		}
		return height;
	}

	private Property<Background> background;
	@Override
	public Property<Background> background() {
		if (background == null) {
			background = new SimpleProperty<Background>();
			Binder.uniBind(background, new Setter<Background>() {
				@Override
				public void set(Background value) {
					getActor().setBackground(value);
				}
			});
		}
		return background;
	}

	private Property<Border> border;
	@Override
	public Property<Border> border() {
		if (border == null) {
			border = new SimpleProperty<Border>();
			Binder.uniBind(border, new Setter<Border>() {
				@Override
				public void set(Border value) {
					getActor().setBorder(value);
				}
			});
		}
		return border;
	}

	

}
