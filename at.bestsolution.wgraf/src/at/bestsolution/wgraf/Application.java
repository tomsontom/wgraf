package at.bestsolution.wgraf;

import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.scene.Container;

public class Application extends Frontend<BackingApplication> {
	
	public Application() {
		backend.setInit(new Runnable() {
			@Override
			public void run() {
				initialize();
			}
		});
	}
	
	@Override
	protected Class<BackingApplication> internal_getBackendType() {
		return BackingApplication.class;
	}
	
	public final Property<String> title() { return backend.title(); }
	public final Property<Container> root() { return backend.root(); }
	
	public final void start(String[] args) { backend.start(args); }
	
	
	protected void initialize() {
	}
	
}
