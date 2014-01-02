package at.bestsolution.wgraf;



public abstract class BackendFactory {

	private static String factory = System.getProperty("wgraf.factory");
	
	public abstract <Type> Type create(Class<?> type);
	
	public static BackendFactory get() {
		try {
			Class<?> forName = Class.forName(factory);
			return (BackendFactory) forName.newInstance();
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
		throw new RuntimeException("No wgraf factory available -> specify one with -Dwgraf.factory=");
	}
}
