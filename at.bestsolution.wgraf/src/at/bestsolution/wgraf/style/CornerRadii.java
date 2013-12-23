package at.bestsolution.wgraf.style;

public class CornerRadii {
	public final double topLeftHorizontalRadius;
	public final double topLeftVerticalRadius;
	
	public final double topRightHorizontalRadius;
	public final double topRightVerticalRadius;
	
	public final double bottomLeftHorizontalRadius;
	public final double bottomLeftVerticalRadius;
	
	public final double bottomRightHorizontalRadius;
	public final double bottomRightVerticalRadius;
	
	public final boolean topLeftHorizontalRadiusAsPercent;
	public final boolean topLeftVerticalRadiusAsPercent;
	
	public final boolean topRightHorizontalRadiusAsPercent;
	public final boolean topRightVerticalRadiusAsPercent;
	
	public final boolean bottomLeftHorizontalRadiusAsPercent;
	public final boolean bottomLeftVerticalRadiusAsPercent;
	
	public final boolean bottomRightHorizontalRadiusAsPercent;
	public final boolean bottomRightVerticalRadiusAsPercent;
	
	
	public CornerRadii(
			double topLeftHorizontalRadius,
			double topLeftVerticalRadius,
			
			double topRightHorizontalRadius,
			double topRightVerticalRadius,
			
			double bottomLeftHorizontalRadius,
			double bottomLeftVerticalRadius,
			
			double bottomRightHorizontalRadius,
			double bottomRightVerticalRadius) {
		this.topLeftHorizontalRadius = topLeftHorizontalRadius;
		this.topLeftVerticalRadius = topLeftVerticalRadius;
		
		this.topRightHorizontalRadius = topRightHorizontalRadius;
		this.topRightVerticalRadius = topRightVerticalRadius;
		
		this.bottomLeftHorizontalRadius = bottomLeftHorizontalRadius;
		this.bottomLeftVerticalRadius = bottomLeftVerticalRadius;
		
		this.bottomRightHorizontalRadius = bottomRightHorizontalRadius;
		this.bottomRightVerticalRadius = bottomRightVerticalRadius;
		
		// TODO implement percent!!
		this.topLeftHorizontalRadiusAsPercent = false;
		this.topLeftVerticalRadiusAsPercent = false;
		
		this.topRightHorizontalRadiusAsPercent = false;
		this.topRightVerticalRadiusAsPercent = false;
		
		this.bottomLeftHorizontalRadiusAsPercent = false;
		this.bottomLeftVerticalRadiusAsPercent = false;
		
		this.bottomRightHorizontalRadiusAsPercent = false;
		this.bottomRightVerticalRadiusAsPercent = false;
	}


	public CornerRadii(double d) {
		topLeftHorizontalRadius =
		topLeftVerticalRadius =
		topRightHorizontalRadius =
		topRightVerticalRadius =
		bottomLeftHorizontalRadius =
		bottomLeftVerticalRadius =
		bottomRightHorizontalRadius =
		bottomRightVerticalRadius = d;
		topLeftHorizontalRadiusAsPercent =
		topLeftVerticalRadiusAsPercent =
		topRightHorizontalRadiusAsPercent =
		topRightVerticalRadiusAsPercent =
		bottomLeftHorizontalRadiusAsPercent =
		bottomLeftVerticalRadiusAsPercent =
		bottomRightHorizontalRadiusAsPercent =
		bottomRightVerticalRadiusAsPercent = false;
	}
}
