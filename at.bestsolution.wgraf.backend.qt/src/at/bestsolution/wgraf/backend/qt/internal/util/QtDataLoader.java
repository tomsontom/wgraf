package at.bestsolution.wgraf.backend.qt.internal.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.bestsolution.wgraf.style.FontSource;

import com.trolltech.qt.core.QByteArray;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QFontDatabase;

public class QtDataLoader {

	
	// TODO cache the stuff
	public static byte[] loadURI(URI uri) throws IOException {
		InputStream in = uri.toURL().openStream();
		ByteArrayOutputStream o = new ByteArrayOutputStream();
		int nRead;
		byte[] data = new byte[1024];
		while ((nRead = in.read(data, 0, data.length)) != -1) {
		  o.write(data, 0, nRead);
		}
		in.close();
		return o.toByteArray();
	}
	
	private static Map<URI, Integer> loadedFonts = new HashMap<URI, Integer>();
	
	public static int loadFont(FontSource source) {
		if (loadedFonts.containsKey(source.source)) {
			return loadedFonts.get(source.source);
		}
		try {
			byte[] data = QtDataLoader.loadURI(source.source);
			QByteArray arry = new QByteArray(data);
			int fontHandle = QFontDatabase.addApplicationFontFromData(arry);
			loadedFonts.put(source.source, fontHandle);
			return fontHandle;
		}
		catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}
}
