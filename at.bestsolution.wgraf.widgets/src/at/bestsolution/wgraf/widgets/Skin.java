package at.bestsolution.wgraf.widgets;

import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.paint.LinearGradient;
import at.bestsolution.wgraf.paint.Paint;
import at.bestsolution.wgraf.paint.LinearGradient.CoordMode;
import at.bestsolution.wgraf.paint.LinearGradient.Spread;
import at.bestsolution.wgraf.paint.LinearGradient.Stop;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.style.FillBackground;

public class Skin {
	public static Property<Paint> HIGHLIGHT = new SimpleProperty<Paint>(Color.rgb(0, 122, 255));
	public static Property<Paint> HIGHLIGHT_30 = new SimpleProperty<Paint>(Color.rgba(0, 122, 255,30));
	public static Property<Paint> HIGHLIGHT_150 = new SimpleProperty<Paint>(Color.rgba(0, 122, 255,150));

	public static Property<Paint> TEXT_CURSOR = new SimpleProperty<Paint>(Color.rgba(0, 122, 255,150));
	public static Property<Paint> COMBO_OK = new SimpleProperty<Paint>(Color.rgba(0, 122, 255,150));
//	public static Property<Paint> LIST_SELECTION = new SimpleProperty<Paint>(new LinearGradient(0, 0, 1, 1, CoordMode.OBJECT_BOUNDING, Spread.PAD,
//			new Stop(0, new Color(225,0,0,150)),
//			new Stop(0.4, new Color(255,30,30,150)),
//			new Stop(1, new Color(255,30,30,150))
//			));

	public static Property<Paint> LIST_SELECTION = new SimpleProperty<Paint>(Color.rgb(220, 220, 220));
	
	public static final Property<Paint> BUTTON_BACKGROUND_NORMAL = new SimpleProperty<Paint>(new Color(124, 124, 124, 255));
	public static final Property<Paint> BUTTON_BACKGROUND_ACTIVE = new SimpleProperty<Paint>(new Color(181, 181, 181, 255));
	public static final Property<Paint> BUTTON_BACKGROUND_DISABLED = new SimpleProperty<Paint>(new Color(251, 251, 251, 255));
	
	public static final Property<Paint> BUTTON_TEXT_NORMAL = new SimpleProperty<Paint>(new Color(255,255,255,255));
	public static final Property<Paint> BUTTON_TEXT_ACTIVE = new SimpleProperty<Paint>(new Color(124, 124, 124, 255));
	public static final Property<Paint> BUTTON_TEXT_DISABLED = new SimpleProperty<Paint>(new Color(200, 200, 200, 255));
	
	
}
