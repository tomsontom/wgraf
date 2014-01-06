package at.bestsolution.wgraf.events;

public class KeyEvent extends InputEvent {

	public final KeyCode code;
	
	public final String key;
	
	public KeyEvent(KeyCode code, String key) {
		this.key = key;
		this.code = code;
	}
}
