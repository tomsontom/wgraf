package at.bestsolution.wgraf.events;

public class FlingEvent extends InputEvent {

	public final double beginX;
	public final double beginY;
	
	public final double velocityX;
	public final double velocityY;
	
	public FlingEvent(double beginX, double beginY, double velocityX, double velocityY) {
		this.beginX = beginX;
		this.beginY = beginY;
		this.velocityX = velocityX;
		this.velocityY = velocityY;
	}
	
}
