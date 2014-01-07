package at.bestsolution.wgraf.widgets;

import at.bestsolution.wgraf.Application;
import at.bestsolution.wgraf.math.Vec2d;
import at.bestsolution.wgraf.paint.Color;
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
		
		
		final PopupPane pane = new PopupPane();
		pane.getAreaNode().width().set(800);
		pane.getAreaNode().height().set(600);
	
		Font font = new Font("Sans", 22);
		
		double yOffset = 10;
		{
			Label test = new Label();
			test.text().set("Checkbox");
			test.font().set(font);
			
			pane.add(test, 10, yOffset);
			
			CheckBox box = new CheckBox();
			pane.add(box, 170 + 100, yOffset);
		}
		yOffset += 70;
		{
			Label test = new Label();
			test.text().set("Text");
			test.font().set(font);
			
			pane.add(test, 10, yOffset);
			
			Text txt = new Text();
			txt.font().set(font);
			txt.text().set("Hallo Test");
			pane.add(txt, 170, yOffset);
			
		}
		yOffset += 70;
		{
			Label test = new Label();
			test.text().set("Combobox");
			test.font().set(font);
			
			pane.add(test, 10, yOffset);
			
			final ComboBox<String> box = new ComboBox<String>();
			box.model().add("Test0");
			box.model().add("Test1");
			box.model().add("Test2");
			box.model().add("Test3");
			
			
			pane.add(box, 170, yOffset);
			
		}
		yOffset += 70;
		{
			Label test = new Label();
			test.text().set("Button");
			test.font().set(font);
			
			pane.add(test, 10, yOffset);
			
			final Button btn = new Button();
			btn.font().set(font);
			btn.text().set("Press me!");
			btn.activated().registerSignalListener(new SignalListener<Void>() {
				
				@Override
				public void onSignal(Void data) {
					if (btn.text().get().equals("Press me!")) {
						btn.text().set("Thanks!");
						
						Pane pop = new AbsolutePane();
						pop.area.background().set(new FillBackground(new Color(255,  0,  0, 50), new CornerRadii(2), new Insets(0, 0, 0, 0)));
						
						pop.area.width().set(100);
						pop.area.height().set(100);
						
						pane.showModal(new Vec2d(0,0), pop);
						
						
					}
					else {
						btn.text().set("Press me!");
						
						pane.hide();
					}
				}
			});
			pane.add(btn, 170, yOffset);
			
		}
		yOffset += 70;
		{
			Label test = new Label();
			test.text().set("ScrollBar");
			test.font().set(font);
			
			pane.add(test, 10, yOffset);
			
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
			
			pane.add(bar, 170, yOffset);
			
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
			Label test = new Label();
			test.text().set("ScrollPane");
			test.font().set(font);
			
			pane.add(test, 400, yOffset);
			
			ScrollPane scrollPane = new ScrollPane();
			scrollPane.area.width().set(400);
			scrollPane.area.height().set(350);
			
//			scrollPane.area.background().set(new FillBackground(new Color(255,  0,  0, 50), new CornerRadii(2), new Insets(0, 0, 0, 0)));
			pane.add(scrollPane, 400, yOffset + 40);
			
			
			SimpleWidgets w = new SimpleWidgets();
			w.area.width().set(400);
			w.area.height().set(500);
			scrollPane.setContent(w);
		}
		
		
		final TouchKeyboard kb = new TouchKeyboard();
		
		pane.add(kb, 0, 400);
		
		kb.area.y().set(400);
		kb.area.y().setTransition(new LinearDoubleTransition(200));
		
		Application.get().focusNode().registerChangeListener(new ChangeListener<Node<?>>() {
			@Override
			public void onChange(Node<?> oldValue, Node<?> newValue) {
				if (newValue != null) {
					if (newValue.requireKeyboard().get()) {
						
						// show keyboard
						kb.area.y().setDynamic(400);
					}
					else {
						// hide keyboard
						kb.area.y().setDynamic(600);
					}
				}
				else {
					// hide keyboard
					kb.area.y().setDynamic(600);
				}
			}
		});
		
		root().set(pane.getAreaNode());
	}
	
	
	public static void main(String[] args) {
		WidgetTest app = new WidgetTest();
		app.start(args);
	}
}
