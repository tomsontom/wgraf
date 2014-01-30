package at.bestsolution.wgraf.events;

public class FlingEvent extends InputEvent {

	public static enum Type {
		FLING, STOP;
	}
	
	public final Type type;
	
	public final double beginX;
	public final double beginY;
	
	public final double velocityX;
	public final double velocityY;
	
	public FlingEvent(Type type, double beginX, double beginY, double velocityX, double velocityY) {
		this.type = type;
		this.beginX = beginX;
		this.beginY = beginY;
		this.velocityX = velocityX;
		this.velocityY = velocityY;
	}
	
}
