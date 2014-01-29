package at.bestsolution.wgraf.backend.qt;

import at.bestsolution.wgraf.BackendFactory;
import at.bestsolution.wgraf.BackingApplication;
import at.bestsolution.wgraf.Sync;
import at.bestsolution.wgraf.backend.qt.internal.util.QtFontUtil;
import at.bestsolution.wgraf.backend.qt.scene.QtContainer;
import at.bestsolution.wgraf.backend.qt.scene.QtText;
import at.bestsolution.wgraf.scene.BackingContainer;
import at.bestsolution.wgraf.scene.BackingText;
import at.bestsolution.wgraf.util.FontUtil;

public class QtBackendFactory extends BackendFactory{
	public QtBackendFactory() {
		INSTANCE = this;
	}
	
	@Override
	public <Type> Type create(Class<?> type) {
		if (type == Sync.class) {
			return (Type) new QtSync();
		}
		if (type == BackingApplication.class) {
			return (Type) new QtApplication();
		}
		else if (type == BackingContainer.class) {
			return (Type) new QtContainer();
		}
		else if (type == BackingText.class) {
			return (Type) new QtText();
		}
		else if (type == FontUtil.class) {
			return (Type) new QtFontUtil();
		}
		throw new UnsupportedOperationException(type + " not supported");
	}

	
}
