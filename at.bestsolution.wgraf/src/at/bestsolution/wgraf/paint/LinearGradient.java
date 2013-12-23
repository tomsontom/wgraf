package at.bestsolution.wgraf.paint;

public class LinearGradient extends Paint {

	public static enum Spread {
		PAD,
		REPEAT,
		REFLECT
	}
	
	public static enum CoordMode {
		LOGICAL,
		OBJECT_BOUNDING
	}
	
	public static class Stop {
		public final double at;
		public final Color color;
		public Stop(double at, Color color) {
			this.at = at;
			this.color = color;
		}
	}
	
	public final double startX;
	public final double startY;
	public final double stopX;
	public final double stopY;
	
	public final CoordMode coordMode;
	public final Spread spread;
	
	public final Stop[] stops;
	
	public LinearGradient(double startX, double startY, double stopX, double stopY, CoordMode coordMode, Spread spread, Stop... stops) {
		this.startX = startX;
		this.startY = startY;
		this.stopX = stopX;
		this.stopY = stopY;
		
		this.coordMode = coordMode;
		this.spread = spread;
		
		this.stops = stops;
	}
	
}
