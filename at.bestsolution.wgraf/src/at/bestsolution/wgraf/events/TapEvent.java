package at.bestsolution.wgraf.events;

public class TapEvent extends InputEvent {

	public final double x;
	public final double y;
	
	public TapEvent(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "TapEvent(" + x + ", " + y + ")";
	}
}
