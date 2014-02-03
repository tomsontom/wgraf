package at.bestsolution.wgraf.widgets;

import java.util.Arrays;
import java.util.List;

import at.bestsolution.wgraf.Application;
import at.bestsolution.wgraf.math.Rect;
import at.bestsolution.wgraf.math.Vec2d;
import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.paint.LinearGradient;
import at.bestsolution.wgraf.paint.LinearGradient.CoordMode;
import at.bestsolution.wgraf.paint.LinearGradient.Spread;
import at.bestsolution.wgraf.paint.LinearGradient.Stop;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.DoubleChangeListener;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.scene.Node;
import at.bestsolution.wgraf.style.CornerRadii;
import at.bestsolution.wgraf.style.FillBackground;
import at.bestsolution.wgraf.style.Font;
import at.bestsolution.wgraf.style.Insets;
import at.bestsolution.wgraf.transition.LinearDoubleTransition;

public class WidgetTest extends Application {

	@Override
	protected void initialize() {
		
		title().set("Hallo Widgets");
		
		final ScrollPane scrollPane = new ScrollPane();
		final PopupPane pane = new PopupPane();
		pane.getAreaNode().width().set(800);
		pane.getAreaNode().height().set(600);
	
		KeyboardSupport keyboardSupport = new KeyboardSupport(pane);
		
		Font font = new Font("Sans", 22);
		
		double yOffset = 30;
		{
			Label test = new Label();
			test.text().set("Checkbox");
			test.font().set(font);
			
			pane.addWidget(test, 10, yOffset);
			
			CheckBox box = new CheckBox();
			pane.addWidget(box, 170 + 100, yOffset);
			
			box.selected().registerChangeListener(new ChangeListener<Boolean>() {
				@Override
				public void onChange(Boolean oldValue, Boolean newValue) {
					System.err.println(newValue);
					
					if (newValue) {
						scrollPane.viewport().set(new Rect(50, 50, 200, 200));
					}
					else {
						scrollPane.viewport().set(null);
					}
				}
			});
			
		}
		yOffset += 70;
		{
			Label test = new Label();
			test.text().set("Text");
			test.font().set(font);
			
			pane.addWidget(test, 10, yOffset);
			
			Text txt = new Text();
			txt.font().set(font);
			txt.text().set("Hallo Test");
			pane.addWidget(txt, 170, yOffset);
			
		}
		yOffset += 70;
		{
			Label test = new Label();
			test.text().set("Combobox");
			test.font().set(font);
			
			pane.addWidget(test, 10, yOffset);
			
			final ComboBox<String> box = new ComboBox<String>();
			box.model().add("Test0");
			box.model().add("Test1");
			box.model().add("Test2");
			box.model().add("Test3");
			
			
			pane.addWidget(box, 170, yOffset);
			
		}
		yOffset += 70;
		{
			Label test = new Label();
			test.text().set("Button");
			test.font().set(font);
			
			pane.addWidget(test, 10, yOffset);
			
			final Button btn = new Button();
			btn.font().set(font);
			btn.text().set("Press me!");
			btn.activated().registerSignalListener(new SignalListener<Void>() {
				
				private Popup p;
				
				@Override
				public void onSignal(Void data) {
					if (btn.text().get().equals("Press me!")) {
						btn.text().set("Thanks!");
						if (p == null) {
							Pane pop = new AbsolutePane();
							pop.area.background().set(new FillBackground(new Color(255,  0,  0, 50), new CornerRadii(2), new Insets(0, 0, 0, 0)));
							
							pop.setBounds(0, 0, 100, 100);
							p = new Popup(pop, false);
						}
						
						
						pane.showPopup(p);
						
						
					}
					else {
						btn.text().set("Press me!");
						
						pane.hidePopup(p);
					}
				}
			});
			pane.addWidget(btn, 170, yOffset);
			
		}
		yOffset += 70;
		{
			Label test = new Label();
			test.text().set("ScrollBar");
			test.font().set(font);
			
			pane.addWidget(test, 10, yOffset);
			
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
			bar.setBounds(0, 0, 200, 40);
			
			pane.addWidget(bar, 170, yOffset);
			
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
		
		{
			String lorem = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
			final List<String> loremList = Arrays.asList(lorem.split(" "));
			
			Label test = new Label();
			test.text().set("ScrollPane");
			test.font().set(font);
			
			pane.addWidget(test, 390, yOffset);
			
			scrollPane.setBounds(0, 0, 390, 350);
			
//			scrollPane.area.background().set(new FillBackground(new Color(255,  0,  0, 50), new CornerRadii(2), new Insets(0, 0, 0, 0)));
			pane.addWidget(scrollPane, 390, yOffset + 40);
			
			AbsolutePane content = new AbsolutePane() {
				@Override
				public Vec2d computePreferredSize() {
					return new Vec2d(390, 400);
				}
			};
			Insets bgInsets = new Insets(0, 0, 0, 0);
			final FillBackground bg = new FillBackground(
					new LinearGradient(0, 0, 0, 1, CoordMode.OBJECT_BOUNDING, Spread.PAD, 
						new Stop(0, new Color(180, 240, 180, 155)),
						new Stop(1, new Color(230, 255, 230, 155))
					), 
					new CornerRadii(4), bgInsets);
			content.area.background().set(bg);
		
			content.setBounds(0, 0, 390, 400);
			
//			AbsolutePane content = new AbsolutePane() {
//				@Override
//				public Vec2d computePreferredSize() {
//					return new Vec2d(390, 20 + 50 * (1 + loremList.size()));
//				}
//			};
//		
//			content.area.width().set(410);
//			
//			double y = 10;
//			for (String lore : loremList) {
//				Label label = new Label();
//				label.font().set(font);
//				label.text().set(lore);
//				
//				content.add(label, 10, y);
//				
//				CheckBox box = new CheckBox();
//				
//				content.add(box, 250, y);
//				
//				y += 50;
//			}
//			
//			content.area.height().set(10 + y);
			
//			SimpleWidgets w = new SimpleWidgets();
//			w.area.width().set(400);
//			w.area.height().set(500);
			
			
			
			scrollPane.setContent(content);
		}
		
		
//		final TouchKeyboard kb = new TouchKeyboard();
//		
//		pane.add(kb, 0, 400);
//		
//		kb.area.y().set(400);
//		kb.area.y().setTransition(new LinearDoubleTransition(200));
//		
//		Application.get().focusNode().registerChangeListener(new ChangeListener<Node<?>>() {
//			@Override
//			public void onChange(Node<?> oldValue, Node<?> newValue) {
//				if (newValue != null) {
//					if (newValue.requireKeyboard().get()) {
//						
//						// show keyboard
//						kb.area.y().setDynamic(400);
//						scrollPane.area.height().set(350);
//					}
//					else {
//						// hide keyboard
//						kb.area.y().setDynamic(600);
//						scrollPane.area.height().set(550);
//					}
//				}
//				else {
//					// hide keyboard
//					kb.area.y().setDynamic(600);
//				}
//			}
//		});
		
		root().set(pane.getAreaNode());
	}
	
	
	public static void main(String[] args) {
		WidgetTest app = new WidgetTest();
		app.start(args);
	}
}
