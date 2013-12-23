package at.bestsolution.wgraf.style;

import java.util.Arrays;
import java.util.List;

public class Backgrounds extends Background {

	// TODO add guava and use ImmutableList here!!!!
	public final List<BaseBackground> backgrounds;
	
	public Backgrounds(BaseBackground...backgrounds) {
		this.backgrounds = Arrays.asList(backgrounds);
	}
}
