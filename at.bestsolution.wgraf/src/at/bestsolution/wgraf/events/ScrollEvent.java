package at.bestsolution.wgraf.events;

public class ScrollEvent extends InputEvent {
	
	public final double beginX;
	public final double beginY;
	
	public final double x;
	public final double y;
	
	public final ScrollLock scrollLock;
	
	public final double deltaX;
	public final double deltaY;
	
	public ScrollEvent(double beginX, double beginY, double x, double y, ScrollLock scrollLock, double deltaX, double deltaY) {
		this.beginX = beginX;
		this.beginY = beginY;
		
		this.x = x;
		this.y = y;
		
		this.scrollLock = scrollLock;
		
		this.deltaX = deltaX;
		this.deltaY = deltaY;
	}
	
	@Override
	public String toString() {
		return "ScrollEvent("+beginX + ", " + beginY + ", "+x +"," + y + ", " + scrollLock + ", " + deltaX + ", " + deltaY+")";
	}
}
