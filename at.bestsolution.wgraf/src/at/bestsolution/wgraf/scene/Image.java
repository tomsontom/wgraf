package at.bestsolution.wgraf.scene;

public class Image extends Node<BackingImage> {

	@Override
	protected Class<BackingImage> internal_getBackendType() {
		return BackingImage.class;
	}

}
