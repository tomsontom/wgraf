package at.bestsolution.wgraf.backend.qt;

import at.bestsolution.wgraf.BackendFactory;
import at.bestsolution.wgraf.BackingApplication;
import at.bestsolution.wgraf.backend.qt.scene.QtContainer;
import at.bestsolution.wgraf.backend.qt.scene.QtText;
import at.bestsolution.wgraf.backend.qt.scene.shapes.QtRectangle;
import at.bestsolution.wgraf.scene.BackingNode;
import at.bestsolution.wgraf.scene.BackingContainer;
import at.bestsolution.wgraf.scene.BackingText;
import at.bestsolution.wgraf.scene.shapes.BackingRectangle;

public class QtBackendFactory extends BackendFactory{

	@Override
	public <Backend> Backend create(Class<?> frontendType) {
		if (frontendType == BackingApplication.class) {
			return (Backend) new QtApplication();
		}
		else if (frontendType == BackingContainer.class) {
			return (Backend) new QtContainer();
		}
		else if (frontendType == BackingRectangle.class) {
			return (Backend) new QtRectangle();
		}
		else if (frontendType == BackingText.class) {
			return (Backend) new QtText();
		}
		throw new UnsupportedOperationException(frontendType + " not supported");
	}

	
}
