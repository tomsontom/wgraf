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
		
		double yOffset = 10;
		{
			Label test = new Label();
			test.text().set("Checkbox");
			test.font().set(defaultFont);
			
			content.add(test, 10, yOffset);
			
			CheckBox box = new CheckBox();
			content.add(box, 170 + 100, yOffset);
		}
		yOffset += 70;
		{
			Label test = new Label();
			test.text().set("Text");
			test.font().set(defaultFont);
			
			content.add(test, 10, yOffset);
			
			Text txt = new Text();
			txt.font().set(defaultFont);
			txt.text().set("Hallo Test");
			content.add(txt, 170, yOffset);
			
		}
		yOffset += 70;
		{
			Label test = new Label();
			test.text().set("Combobox");
			test.font().set(defaultFont);
			
			content.add(test, 10, yOffset);
			
			final ComboBox<String> box = new ComboBox<String>();
			box.model().add("Test0");
			box.model().add("Test1");
			box.model().add("Test2");
			box.model().add("Test3");
			
			
			content.add(box, 170, yOffset);
			
		}
		yOffset += 70;
		{
			Label test = new Label();
			test.text().set("Button");
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
						
//						Pane pop = new AbsolutePane();
//						pop.area.background().set(new FillBackground(new Color(255,  0,  0, 50), new CornerRadii(2), new Insets(0, 0, 0, 0)));
//						
//						pop.area.width().set(100);
//						pop.area.height().set(100);
//						
//						pane.showModal(new Vec2d(0,0), pop);
						
						
					}
					else {
						btn.text().set("Press me!");
						
//						pane.hide();
					}
				}
			});
			content.add(btn, 170, yOffset);
			
		}
		yOffset += 70;
		{
			Label test = new Label();
			test.text().set("ScrollBar");
			test.font().set(defaultFont);
			
			content.add(test, 10, yOffset);
			
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
			
			content.add(bar, 170, yOffset);
			
		}
		
		yOffset += 70;
		{
			Label test = new Label();
			test.text().set("Text");
			test.font().set(defaultFont);
			
			content.add(test, 10, yOffset);
			
			Text txt = new Text();
			txt.font().set(defaultFont);
			txt.text().set("Hallo Test");
			content.add(txt, 170, yOffset);
			
		}
		
		yOffset += 70;
		{
			Label test = new Label();
			test.text().set("Text");
			test.font().set(defaultFont);
			
			content.add(test, 10, yOffset);
			
			Text txt = new Text();
			txt.font().set(defaultFont);
			txt.text().set("Hallo Test");
			content.add(txt, 170, yOffset);
			
		}
		
		yOffset += 70;
		{
			Label test = new Label();
			test.text().set("Text");
			test.font().set(defaultFont);
			
			content.add(test, 10, yOffset);
			
			Text txt = new Text();
			txt.font().set(defaultFont);
			txt.text().set("Hallo Test");
			content.add(txt, 170, yOffset);
			
		}
		
		yOffset += 70;
		{
			Label test = new Label();
			test.text().set("Text");
			test.font().set(defaultFont);
			
			content.add(test, 10, yOffset);
			
			Text txt = new Text();
			txt.font().set(defaultFont);
			txt.text().set("Hallo Test");
			content.add(txt, 170, yOffset);
			
		}
		
		yOffset += 70;
		{
			Label test = new Label();
			test.text().set("Text");
			test.font().set(defaultFont);
			
			content.add(test, 10, yOffset);
			
			Text txt = new Text();
			txt.font().set(defaultFont);
			txt.text().set("Hallo Test");
			content.add(txt, 170, yOffset);
			
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
		yOffset = 0;
//		final ScrollPane scrollPane = new ScrollPane();
//		{
//			String lorem = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.";
//			final List<String> loremList = Arrays.asList(lorem.split(" "));
//			
//			Label test = new Label();
//			test.text().set("ScrollPane");
//			test.font().set(defaultFont);
//			
//			content.add(test, 400, yOffset);
//			
//			scrollPane.area.width().set(390);
//			scrollPane.area.height().set(350);
//			
////			scrollPane.area.background().set(new FillBackground(new Color(255,  0,  0, 50), new CornerRadii(2), new Insets(0, 0, 0, 0)));
//			content.add(scrollPane, 400, yOffset + 40);
//			
//			
//			AbsolutePane innerContent = new AbsolutePane() {
//				@Override
//				public Vec2d computePreferredSize() {
//					return new Vec2d(390, 20 + 50 * loremList.size());
//				}
//			};
//		
//			innerContent.area.width().set(400);
//			
//			double y = 10;
//			for (String lore : loremList) {
//				Label label = new Label();
//				label.font().set(defaultFont);
//				label.text().set(lore);
//				
//				innerContent.add(label, 10, y);
//				
//				CheckBox box = new CheckBox();
//				
//				innerContent.add(box, 250, y);
//				
//				y += 50;
//			}
//			
//			innerContent.area.height().set(10 + y);
//			
////			SimpleWidgets w = new SimpleWidgets();
////			w.area.width().set(400);
////			w.area.height().set(500);
//			
//			
//			scrollPane.setContent(content);
//		}
		
		root().set(rootPane.getAreaNode());
	}
	
	
	public static void main(String[] args) {
		AppTest app = new AppTest();
		app.start(args);
	}
}
