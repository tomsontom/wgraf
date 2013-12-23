package at.bestsolution.wgraf.backend.javafx;

import at.bestsolution.wgraf.Application;
import at.bestsolution.wgraf.BackendFactory;
import at.bestsolution.wgraf.BackingApplication;
import at.bestsolution.wgraf.backend.javafx.scene.JavaFxContainer;
import at.bestsolution.wgraf.backend.javafx.scene.JavaFxText;
import at.bestsolution.wgraf.backend.javafx.scene.shape.JavaFxRectangle;
import at.bestsolution.wgraf.scene.BackingContainer;
import at.bestsolution.wgraf.scene.BackingText;
import at.bestsolution.wgraf.scene.Container;
import at.bestsolution.wgraf.scene.shapes.BackingRectangle;
import at.bestsolution.wgraf.scene.shapes.Rectangle;

public class JavaFxBackendFactory extends BackendFactory {

	@Override
	public <Backend> Backend create(Class<?> frontendType) {
		if (frontendType == BackingApplication.class) {
			return (Backend) new JavaFxApplication();
		}
		else if (frontendType == BackingContainer.class) {
			return (Backend) new JavaFxContainer();
		}
		else if (frontendType == BackingRectangle.class) {
			return (Backend) new JavaFxRectangle();
		}
		else if (frontendType == BackingText.class) {
			return (Backend) new JavaFxText();
		}
		throw new UnsupportedOperationException("not implemented! " + frontendType);
	}

}
