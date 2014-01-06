package at.bestsolution.wgraf.widgets;

import at.bestsolution.wgraf.Application;
import at.bestsolution.wgraf.math.Vec2d;
import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.properties.DoubleChangeListener;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.style.CornerRadii;
import at.bestsolution.wgraf.style.FillBackground;
import at.bestsolution.wgraf.style.Font;
import at.bestsolution.wgraf.style.Insets;

public class WidgetTest extends Application {

	@Override
	protected void initialize() {
		
		title().set("Hallo Widgets");
		
		AbsolutePane pane = new AbsolutePane();
		pane.getAreaNode().width().set(400d);
		pane.getAreaNode().height().set(500d);
	
		Font font = new Font("Sans", 22);
		
		{
			Label test = new Label();
			test.text().set("Checkbox");
			test.font().set(font);
			
			pane.add(test, 10, 10);
			
			CheckBox box = new CheckBox();
			pane.add(box, 170, 10);
		}
		
		{
			Label test = new Label();
			test.text().set("Text");
			test.font().set(font);
			
			pane.add(test, 10, 80);
			
			Text txt = new Text();
			txt.font().set(font);
			txt.text().set("Hallo Test");
			pane.add(txt, 170, 80);
			
		}
		
		{
			Label test = new Label();
			test.text().set("Button");
			test.font().set(font);
			
			pane.add(test, 10, 150);
			
			final Button btn = new Button();
			btn.font().set(font);
			btn.text().set("Press me!");
			btn.activated().registerSignalListener(new SignalListener<Void>() {
				
				@Override
				public void onSignal(Void data) {
					if (btn.text().get().equals("Press me!")) {
						btn.text().set("Thanks!");
					}
					else {
						btn.text().set("Press me!");
					}
				}
			});
			pane.add(btn, 170, 150);
			
		}
		
		{
			Label test = new Label();
			test.text().set("ScrollBar");
			test.font().set(font);
			
			pane.add(test, 10, 220);
			
			ScrollBar bar = new ScrollBar();
			bar.sliderSizeFactor().set(0.2);
			bar.minValue().set(0);
			bar.maxValue().set(100);
			bar.value().registerChangeListener(new DoubleChangeListener() {
				
				@Override
				public void onChange(double oldValue, double newValue) {
					System.err.println(newValue);
				}
			});
			bar.area.width().set(200d);
			bar.area.height().set(40d);
			
			pane.add(bar, 170, 220);
			
		}
		
//		{
//			Label test = new Label();
//			test.text().set("ScrollPane");
//			test.font().set(font);
//			
//			pane.add(test, 10, 290);
//			
//			ScrollPane scrollPane = new ScrollPane();
//			scrollPane.area.width().set(200);
//			scrollPane.area.height().set(200);
//			
//			scrollPane.area.background().set(new FillBackground(new Color(255,  0,  0, 50), new CornerRadii(2), new Insets(0, 0, 0, 0)));
//			pane.add(scrollPane, 170, 290);
//			
//			
//			Label label = new Label();
//			label.font().set(new Font("Sans", 200));
//			label.text().set("Scrolling?!");
//			
//			Vec2d s = label.computePreferredSize();
//			label.area.width().set(s.x);
//			label.area.height().set(s.y);
//			
//			scrollPane.setContent(label);
//		}
		
		{
			Label test = new Label();
			test.text().set("ScrollPane");
			test.font().set(font);
			
			pane.add(test, 10, 290);
			
			ScrollPane scrollPane = new ScrollPane();
			scrollPane.area.width().set(200);
			scrollPane.area.height().set(200);
			
//			scrollPane.area.background().set(new FillBackground(new Color(255,  0,  0, 50), new CornerRadii(2), new Insets(0, 0, 0, 0)));
			pane.add(scrollPane, 170, 290);
			
			
			SimpleWidgets w = new SimpleWidgets();
			w.area.width().set(400);
			w.area.height().set(500);
			scrollPane.setContent(w);
		}
		root().set(pane.getAreaNode());
	}
	
	
	public static void main(String[] args) {
		WidgetTest app = new WidgetTest();
		app.start(args);
	}
}
