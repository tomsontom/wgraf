package at.bestsolution.wgraf.events;

public class KeyEvent {

	public final KeyCode code;
	
	public final String key;
	
	public final int keyCode;
	
	public KeyEvent(int keyCode, String key) {
		this.key = key;
		this.keyCode = keyCode;
		this.code = null; // TODO add keycode mappings!!
	}
}
