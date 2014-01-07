package at.bestsolution.wgraf.style;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class Backgrounds extends Background {

	// TODO add guava and use ImmutableList here!!!!
	public final List<BaseBackground> backgrounds;
	
	public Backgrounds(BaseBackground...backgrounds) {
		this.backgrounds = Arrays.asList(backgrounds);
	}
	
	@Override
	public byte[] getHash() {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			for (BaseBackground b : backgrounds) {
				byte[] bgHash = b.getHash();
				md.update(bgHash);
			}
			return md.digest();
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	
}
