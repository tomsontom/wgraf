package at.bestsolution.wgraf.widgets;

import at.bestsolution.wgraf.Application;
import at.bestsolution.wgraf.properties.DoubleChangeListener;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.style.Font;

public class AppTest extends Application {

	@Override
	protected void initialize() {
		
		title().set("Hallo Touch App");
		
		final Font defaultFont = new Font("Sans", 22);
		
		final KeyboardPane rootPane = new KeyboardPane();
		rootPane.area.width().set(800);
		rootPane.area.height().set(600);
		
	
		final AbsolutePane content = new AbsolutePane();
//		Insets bgInsets = new Insets(0, 0, 0, 0);
//		final FillBackground bg = new FillBackground(
//				new LinearGradient(0, 0, 0, 1, CoordMode.OBJECT_BOUNDING, Spread.PAD, 
//					new Stop(0, new Color(240, 240, 180, 155)),
//					new Stop(1, new Color(255, 255, 230, 155))
//				), 
//				new CornerRadii(4), bgInsets);
//		content.area.background().set(bg);
		content.area.width().set(800);
		content.area.height().set(700);
		
		rootPane.setContent(content);
		
		
		// create widgets:
		
		int num = 15;
		double yOffset = 10;
		
		double w = 250;
		
		for (int i = 0; i < num; i++) {
			
			if (i % 3 == 0) {
				// Checkbox
				Label test = new Label();
				test.text().set("Checkbox " + i);
				test.font().set(defaultFont);
				
				content.add(test, 10, yOffset);
				
				CheckBox box = new CheckBox();
				content.add(box, w + 100, yOffset);
			}
			else if (i % 3 == 1) {
				// Button
				Label test = new Label();
				test.text().set("Button " + i);
				test.font().set(defaultFont);
				
				content.add(test, 10, yOffset);
				
				final Button btn = new Button();
				btn.font().set(defaultFont);
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
				content.add(btn, w, yOffset);
			}
			else if (i % 3 == 2) {
				// Text
				Label test = new Label();
				test.text().set("Text " + i);
				test.font().set(defaultFont);
				
				content.add(test, 10, yOffset);
				
				Text txt = new Text();
				txt.font().set(defaultFont);
				txt.text().set("Hallo Test");
				content.add(txt, w, yOffset);
			}
			
			yOffset += 70;
		}
		
		
		root().set(rootPane.getAreaNode());
	}
	
	
	public static void main(String[] args) {
		AppTest app = new AppTest();
		app.start(args);
	}
}
