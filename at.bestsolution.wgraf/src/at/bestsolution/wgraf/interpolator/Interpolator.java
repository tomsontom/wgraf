package at.bestsolution.wgraf.interpolator;

import at.bestsolution.wgraf.transition.Transition;

public interface Interpolator {

	void interpolate(Transition<?> transition);
	
	public static class Access {
		private static Interpolator instance;
		
		public static Interpolator getInterpolator() {
			if (instance == null) {
				instance = new DefaultInterpolator();
			}
			return instance;
		}
	}
}
