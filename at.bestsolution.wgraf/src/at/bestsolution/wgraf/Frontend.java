package at.bestsolution.wgraf;


public abstract class Frontend<Backend> {
	protected final Backend backend;
	
	public Frontend() {
		backend = BackendFactory.get().create(internal_getBackendType()); 
	}
	
	protected abstract Class<Backend> internal_getBackendType();
	
	public Backend internal_getBackend() {
		return backend;
	}
	
}
