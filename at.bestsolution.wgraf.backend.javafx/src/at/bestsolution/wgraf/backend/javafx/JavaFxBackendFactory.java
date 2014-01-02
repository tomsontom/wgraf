package at.bestsolution.wgraf.backend.javafx;

import at.bestsolution.wgraf.BackendFactory;
import at.bestsolution.wgraf.BackingApplication;
import at.bestsolution.wgraf.backend.javafx.scene.JavaFxContainer;
import at.bestsolution.wgraf.backend.javafx.scene.JavaFxText;
import at.bestsolution.wgraf.backend.javafx.util.JavaFxFontUtil;
import at.bestsolution.wgraf.scene.BackingContainer;
import at.bestsolution.wgraf.scene.BackingText;
import at.bestsolution.wgraf.util.FontUtil;

public class JavaFxBackendFactory extends BackendFactory {

	@Override
	public <Backend> Backend create(Class<?> frontendType) {
		if (frontendType == BackingApplication.class) {
			return (Backend) new JavaFxApplication();
		}
		else if (frontendType == BackingContainer.class) {
			return (Backend) new JavaFxContainer();
		}
		else if (frontendType == BackingText.class) {
			return (Backend) new JavaFxText();
		}
		else if (frontendType == FontUtil.class) {
			return (Backend) new JavaFxFontUtil();
		}
		throw new UnsupportedOperationException("not implemented! " + frontendType);
	}

}
