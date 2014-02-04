package at.bestsolution.wgraf.style;

import java.net.URI;
import java.net.URISyntaxException;

public class FontSource {
	public final URI source;
	
	public FontSource(URI source) {
		this.source = source;
	}
	
	public static final FontSource UBUNTU;
	static {
		FontSource u;
		try {
			u = new FontSource(new URI("platform:/plugin/at.bestsolution.wgraf/fonts/Ubuntu-R.ttf"));
		} catch (URISyntaxException e) {
			u = null;
		}
		UBUNTU = u;
	}
}
