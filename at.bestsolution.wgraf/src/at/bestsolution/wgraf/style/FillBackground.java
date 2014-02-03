package at.bestsolution.wgraf.style;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.paint.Paint;

public class FillBackground extends BaseBackground {

	public final Paint fill;

	public FillBackground(Paint fill, CornerRadii radii, Insets insets) {
		super(radii, insets);
		this.fill = fill;
	}

	@Override
	public byte[] getHash() {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(cornerRadii.toString().getBytes("UTF-8"));
			md.update(insets.toString().getBytes("UTF-8"));
			md.update(fill.toString().getBytes("UTF-8"));
			return md.digest();
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static FillBackground simple(Paint fill, double radii, int insets) {
		return new FillBackground(fill, new CornerRadii(radii), new Insets(insets));
	}
}
