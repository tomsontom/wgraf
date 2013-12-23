package at.bestsolution.wgraf.events;

public class ScrollEvent {
	public final double x;
	public final double y;
	
	public final ScrollLock scrollLock;
	
	public final double deltaX;
	public final double deltaY;
	
	public ScrollEvent(double x, double y, ScrollLock scrollLock, double deltaX, double deltaY) {
		this.x = x;
		this.y = y;
		
		this.scrollLock = scrollLock;
		
		this.deltaX = deltaX;
		this.deltaY = deltaY;
	}
}
