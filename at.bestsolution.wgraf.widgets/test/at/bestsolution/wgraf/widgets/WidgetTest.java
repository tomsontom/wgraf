package at.bestsolution.wgraf.widgets;

import at.bestsolution.wgraf.Application;
import at.bestsolution.wgraf.properties.DoubleChangeListener;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.style.Font;

public class WidgetTest extends Application {

	@Override
	protected void initialize() {
		
		title().set("Hallo Widgets");
		
		AbsolutePane pane = new AbsolutePane();
		pane.getAreaNode().width().set(400d);
		pane.getAreaNode().height().set(300d);
	
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
			pane.add(bar, 170, 220);
			
		}
		root().set(pane.getAreaNode());
	}
	
	
	public static void main(String[] args) {
		WidgetTest app = new WidgetTest();
		app.start(args);
	}
}
