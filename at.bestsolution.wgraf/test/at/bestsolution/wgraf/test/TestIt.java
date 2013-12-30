package at.bestsolution.wgraf.test;

import at.bestsolution.wgraf.Application;
import at.bestsolution.wgraf.events.TapEvent;
import at.bestsolution.wgraf.math.Vec2d;
import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.paint.LinearGradient;
import at.bestsolution.wgraf.paint.LinearGradient.CoordMode;
import at.bestsolution.wgraf.paint.LinearGradient.Spread;
import at.bestsolution.wgraf.paint.LinearGradient.Stop;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.scene.Container;
import at.bestsolution.wgraf.scene.Region;
import at.bestsolution.wgraf.scene.Text;
import at.bestsolution.wgraf.scene.shapes.Rectangle;
import at.bestsolution.wgraf.style.Backgrounds;
import at.bestsolution.wgraf.style.CornerRadii;
import at.bestsolution.wgraf.style.FillBackground;
import at.bestsolution.wgraf.style.Font;
import at.bestsolution.wgraf.style.Insets;
import at.bestsolution.wgraf.transition.LinearDoubleTransition;

public class TestIt {

	public static void main(String[] args) {
		final Application app = new Application() {
			@Override
			protected void initialize() {
				System.err.println("initialize");
				Container region = new Container();
				
				region.width().set(300.0);
				region.height().set(300.0);
				
				CheckBox test = new CheckBox();
				test.setParent(region);
				test.setX(10);
				test.setY(10);
				
				{
					Container solidFill = new Container();
					solidFill.setParent(region);
					
					solidFill.x().set(75.0);
					solidFill.y().set(40.0);
					
					solidFill.width().set(150.0);
					solidFill.height().set(40.0);
					
					Color green100 = new Color(0, 255, 0, 100);
					
					solidFill.background().set(new FillBackground(green100, new CornerRadii(20.0), new Insets(0, 0, 0, 0)));
					
//					solidFill.arcHeight().set(40.0);
//					solidFill.arcWidth().set(40.0);
//					solidFill.fill().set(green100);
					
				}
				
				{
					Container solidFill = new Container();
					solidFill.setParent(region);
					
					solidFill.x().set(75.0);
					solidFill.y().set(80.0);
					
					solidFill.width().set(150.0);
					solidFill.height().set(40.0);
					
					Color green100 = new Color(0, 255, 0, 100);
					
					Color blue100 = new Color(0, 0, 255, 100);
					
					solidFill.background().set(new Backgrounds(
							new FillBackground(green100, new CornerRadii(20.0), new Insets(0, 50, 0, 0)),
							new FillBackground(blue100, new CornerRadii(20.0), new Insets(0, 0, 0, 50))
							));
					
//					solidFill.arcHeight().set(40.0);
//					solidFill.arcWidth().set(40.0);
//					solidFill.fill().set(green100);
					
				}
				
				{
					Container solidFill = new Container();
					solidFill.setParent(region);
					
					solidFill.x().set(75.0);
					solidFill.y().set(120.0);
					
					solidFill.width().set(150.0);
					solidFill.height().set(40.0);
					
					Color green100 = new Color(0, 255, 0, 100);
					Color blue100 = new Color(0, 0, 255, 100);
					Color red100 = new Color(255, 0, 0, 100);
					
					solidFill.background().set(new Backgrounds(
							new FillBackground(green100, new CornerRadii(20.0), new Insets(0, 0, 0, 50)),
							new FillBackground(blue100, new CornerRadii(20.0), new Insets(0, 50, 0, 0))
							));
					
//					solidFill.arcHeight().set(40.0);
//					solidFill.arcWidth().set(40.0);
//					solidFill.fill().set(green100);
					
				}
				
				{
					Container linearGradient = new Container();
					linearGradient.setParent(region);
					
					linearGradient.x().set(75.0);
					linearGradient.y().set(160.0);
					
					linearGradient.width().set(150.0);
					linearGradient.height().set(40.0);
//					linearGradient.arcHeight().set(40.0);
//					linearGradient.arcWidth().set(40.0);
					
					Color red100 = new Color(255, 0, 0, 100);
					Color red200 = new Color(255, 0, 0, 200);
					
					LinearGradient g = new LinearGradient(0.0, 0.0, 0.0, 1.0, CoordMode.OBJECT_BOUNDING, Spread.PAD, new Stop(0.0, red100), new Stop(1.0, red200));
					
					linearGradient.background().set(
							new FillBackground(g, new CornerRadii(20.0), new Insets(0, 0, 0, 0))
							);
					
					
//					linearGradient.fill().set(new LinearGradient(0.0, 0.0, 0.0, 1.0, CoordMode.OBJECT_BOUNDING, Spread.PAD, new Stop(0.0, red100), new Stop(1.0, red200)));
					
				}
				
				{
					Font myFont = new Font("Arial", 20);
					Vec2d stringExtent = myFont.stringExtent("Hallo yj");
					
					Container r = new Container();
					r.x().set(10.0);
					r.y().set(200.0);
					r.width().set(stringExtent.x);
					r.height().set(stringExtent.y);
					r.setParent(region);
					r.background().set(new FillBackground(new Color(255, 0, 0, 50), new CornerRadii(0), new Insets(0, 0, 0, 0)));
					
					Text text = new Text();
					
					text.fontSize().set(20.0);
					
					text.x().set(10.0);
					text.y().set(200.0);
					
					text.text().set("Hallo yj");
					
					text.setParent(region);
					
				}
				
				final Container r = new Container();
				r.setParent(region);
				
				r.x().set(75.0);
				r.y().set(200.0);
				
				r.width().set(150.0);
				r.height().set(40.0);
//				r.arcHeight().set(40.0);
//				r.arcWidth().set(40.0);
				
				Color red100 = new Color(255, 0, 0, 100);
				Color red200 = new Color(255, 0, 0, 200);
				
				LinearGradient g = new LinearGradient(0.0, 0.0, 0.0, 1.0, CoordMode.OBJECT_BOUNDING, Spread.PAD, new Stop(0.0, red100), new Stop(1.0, red200));
				
				r.background().set(
						new FillBackground(g, new CornerRadii(20.0), new Insets(0, 0, 0, 0))
						);
				
				
//				r.fill().set(new LinearGradient(0.0, 0.0, 0.0, 1.0, CoordMode.OBJECT_BOUNDING, Spread.PAD, new Stop(0.0, red100), new Stop(1.0, red200)));
				
				r.height().setTransition(new LinearDoubleTransition(500));
				
				r.onTap().registerSignalListner(new SignalListener<TapEvent>() {
					
					@Override
					public void onSignal(TapEvent data) {
						System.err.println("TAP");
						if (r.height().get() > 60) {
							r.height().set(40d);
						}
						else {
							r.height().set(80d);
						}
						
					}
				});
				
				root().set(region);
				
			}
		};
		
			

				
		
		app.start(args);
	}

}
