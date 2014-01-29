package at.bestsolution.wgraf;



public abstract class BackendFactory {

	private static String factory = System.getProperty("wgraf.factory");
	
	protected static BackendFactory INSTANCE;
	
	public abstract <Type> Type create(Class<?> type);
	
	public static BackendFactory get() {
		if( INSTANCE != null ) {
			return INSTANCE;
		}
		
		String f = factory;
		
		if( f == null ) {
			f = "at.bestsolution.wgraf.backend.javafx.JavaFxBackendFactory";
		}
		
		try {
			Class<?> forName = Class.forName(f);
			return (BackendFactory) forName.newInstance();
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
		throw new RuntimeException("No wgraf factory available -> specify one with -Dwgraf.factory=");
	}
}
