package at.bestsolution.wgraf.style;

public abstract class BaseBackground extends Background {

	public final CornerRadii cornerRadii;
	
	public final Insets insets;
	
	
	public BaseBackground(CornerRadii radii, Insets insets) {
		this.cornerRadii = radii;
		this.insets = insets;
	}
	
}
