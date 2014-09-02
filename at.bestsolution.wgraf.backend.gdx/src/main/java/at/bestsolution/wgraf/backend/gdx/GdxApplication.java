package at.bestsolution.wgraf.backend.gdx;

import at.bestsolution.wgraf.BackingApplication;
import at.bestsolution.wgraf.backend.gdx.scene.GdxContainer;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.scene.Container;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GdxApplication extends ApplicationAdapter implements BackingApplication {

	private Runnable init;
	
	private Property<String> title;
	@Override
	public Property<String> title() {
		if (title == null) {
			title = new SimpleProperty<String>();
		}
		return title;
	}

	private Property<Container> root;
	@Override
	public Property<Container> root() {
		if (root == null) {
			root = new SimpleProperty<Container>();
		}
		return root;
	}

	private Property<Boolean> fullscreen;
	@Override
	public Property<Boolean> fullscreen() {
		if (fullscreen == null) {
			fullscreen = new SimpleProperty<Boolean>();
		}
		return fullscreen;
	}

	private Stage stage;
	private Viewport viewport;
	
	@Override
	public void create() {
		init.run();
		Container rootContainer = root().get();
		if (rootContainer != null) {
			GdxContainer gdxContainer = (GdxContainer) rootContainer.internal_getBackend();
			Group rootGroup = gdxContainer.getActor();
			
			
			OrthographicCamera camera= new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			
			viewport = new ScreenViewport(camera);
			stage = new Stage(viewport, new PolygonSpriteBatch());
			stage.addActor(rootGroup);
			
			Gdx.input.setInputProcessor(stage);
		
			stage.setDebugAll(false);
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		}
		
	}
	
	@Override
	public void resize(int width, int height) {
		if (viewport != null) {
//			viewport.setScreenSize(width, height);
			viewport.update(width, height, true);
		}
	}
	
	@Override
	public void render() {
		Gdx.gl20.glClearColor(1f, 1f, 1f, 1.0f);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		if (stage != null) {
			stage.draw();
		}
	}
	
	
	@Override
	public void setInit(Runnable init) {
		this.init = init;
	}

	
	@Override
	public void stop() {
		
	}
	
	@Override
	public void start(String[] arg0) {
		
	}

}
