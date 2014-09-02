package at.bestsolution.wgraf.backend.gdx;

import at.bestsolution.wgraf.BackendFactory;
import at.bestsolution.wgraf.BackingApplication;
import at.bestsolution.wgraf.backend.gdx.scene.GdxContainer;
import at.bestsolution.wgraf.backend.gdx.scene.GdxText;
import at.bestsolution.wgraf.scene.BackingContainer;
import at.bestsolution.wgraf.scene.BackingText;
import at.bestsolution.wgraf.util.FontUtil;

public class GdxBackendFactory extends BackendFactory {

	public GdxBackendFactory() {
		System.err.println("HALLO FACTORY");
	}
	
	@Override
	public <Type> Type create(Class<?> type) {
		if (type == BackingApplication.class) {
			return (Type) new GdxApplication();
		}
		else if (type == BackingContainer.class) {
			return (Type) new GdxContainer();
		}
		else if (type == BackingText.class) {
			return (Type) new GdxText();
		}
		else if (type == FontUtil.class) {
			return (Type) new GdxFontUtil();
		}
		throw new UnsupportedOperationException(type + " is not implemented");
	}

}
