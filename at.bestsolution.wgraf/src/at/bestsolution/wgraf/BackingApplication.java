package at.bestsolution.wgraf;

import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.scene.Container;

public interface BackingApplication {

	Property<String> title();
	Property<Container> root();
	void start(String[] args);
	void setInit(Runnable init);
}
