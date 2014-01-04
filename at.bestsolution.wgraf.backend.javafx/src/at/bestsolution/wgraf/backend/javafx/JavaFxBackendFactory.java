package at.bestsolution.wgraf.backend.javafx;

import at.bestsolution.wgraf.BackendFactory;
import at.bestsolution.wgraf.BackingApplication;
import at.bestsolution.wgraf.Sync;
import at.bestsolution.wgraf.backend.javafx.scene.JavaFxContainer;
import at.bestsolution.wgraf.backend.javafx.scene.JavaFxText;
import at.bestsolution.wgraf.backend.javafx.util.JavaFxFontUtil;
import at.bestsolution.wgraf.scene.BackingContainer;
import at.bestsolution.wgraf.scene.BackingText;
import at.bestsolution.wgraf.util.FontUtil;

public class JavaFxBackendFactory extends BackendFactory {

	@Override
	public <Type> Type create(Class<?> type) {
		if (type == Sync.class) {
			return (Type) new JavaFxSync();
		}
		if (type == BackingApplication.class) {
			return (Type) new JavaFxApplication();
		}
		else if (type == BackingContainer.class) {
			return (Type) new JavaFxContainer();
		}
		else if (type == BackingText.class) {
			return (Type) new JavaFxText();
		}
		else if (type == FontUtil.class) {
			return (Type) new JavaFxFontUtil();
		}
		throw new UnsupportedOperationException("not implemented! " + type);
	}

}
