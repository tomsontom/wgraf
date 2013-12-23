package at.bestsolution.wgraf.backend.javafx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.effect.BlendMode;
import javafx.stage.Stage;
import at.bestsolution.wgraf.BackingApplication;
import at.bestsolution.wgraf.backend.javafx.scene.JavaFxContainer;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.scene.BackingContainer;
import at.bestsolution.wgraf.scene.Container;

public class JavaFxApplication implements BackingApplication {

	private Runnable init = null;
	
	private static JavaFxApplication instance;
	
	public static class RealApp extends Application {
		
		private Scene initScene() {
			Scene scenex;
			if (instance.root().get() != null) {
				scenex = new Scene(((JavaFxContainer)instance.root().get().internal_getBackend()).getNode());
			}
			else {
				scenex = new Scene(new javafx.scene.layout.Region());
			}
			
			
			
			return scenex;
		}
		
		@Override
		public void start(final Stage primaryStage) throws Exception {
			final Scene scene = new Scene(new javafx.scene.layout.Region());
			
			// call initialize
			if (instance.init != null) instance.init.run();
			
			primaryStage.setScene(scene);
			primaryStage.show();
			
			
			instance.title().registerChangeListener(new ChangeListener<String>() {
				@Override
				public void onChange(String oldValue, String newValue) {
					primaryStage.setTitle(newValue);
				}
			});
			primaryStage.setTitle(instance.title().get());
			
			instance.root().registerChangeListener(new ChangeListener<Container>() {
				@Override
				public void onChange(Container oldValue, Container newValue) {
					if (newValue != null) {
						System.err.println("root changed");
						JavaFxContainer r = (JavaFxContainer) newValue.internal_getBackend();
						javafx.scene.layout.Region node = r.getNode();
						scene.setRoot(node);
						primaryStage.setWidth(r.width().get());
						primaryStage.setHeight(r.height().get());
					}
				}
			});
			JavaFxContainer r = (JavaFxContainer) instance.root().get().internal_getBackend();
			javafx.scene.layout.Region node = r.getNode();
			System.err.println("setting root to " + node);
			System.err.println("kids: " + node.getChildrenUnmodifiable());
			scene.setRoot(node);
			primaryStage.setWidth(r.width().get());
			primaryStage.setHeight(r.height().get());
		}
	}
	
	public JavaFxApplication() {
		JavaFxApplication.instance = this;
	}

	
	private Property<String> title = null;
	
	@Override
	public Property<String> title() {
		if (title == null) {
			title = new SimpleProperty<String>();
		}
		return title;
	}
	
	private Property<Container> root = null;
	
	@Override
	public Property<Container> root() {
		if (root == null) {
			root = new SimpleProperty<Container>();
		}
		return root;
	}
	
	@Override
	public void start(String[] args) {
		Application.launch(RealApp.class, args);
	}
	
	
	public void setInit(Runnable init) {
		this.init = init;
	}
	

}
