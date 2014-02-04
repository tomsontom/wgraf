package at.bestsolution.wgraf.widgets;

import java.util.Stack;

import at.bestsolution.wgraf.Application;
import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.paint.LinearGradient;
import at.bestsolution.wgraf.paint.LinearGradient.CoordMode;
import at.bestsolution.wgraf.paint.LinearGradient.Spread;
import at.bestsolution.wgraf.paint.LinearGradient.Stop;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.scene.Container;
import at.bestsolution.wgraf.scene.Node;
import at.bestsolution.wgraf.scene.Text;
import at.bestsolution.wgraf.style.CornerRadii;
import at.bestsolution.wgraf.style.FillBackground;
import at.bestsolution.wgraf.style.Font;
import at.bestsolution.wgraf.style.Insets;

public class CheckBoxTest extends Application {

	private Stack<Node<?>> nodes = new Stack<Node<?>>();
	
	@Override
	protected void initialize() {
		
		title().set("CheckBox");
		
		
		final PopupPane pane = new PopupPane();
		pane.getAreaNode().width().set(800);
		pane.getAreaNode().height().set(600);
	
		final Font font = Font.UBUNTU.resize(22);
		
		double yOffset = 10;
		{
			Label test = new Label();
			test.text().set("Checkbox");
			test.font().set(font);
			
			pane.addWidget(test, 10, yOffset);
			
			CheckBox box = new CheckBox();
			pane.addWidget(box, 170 + 100, yOffset);
		}
		
		
		final Button add = new Button();
		add.text().set("+");
		add.font().set(font);
		add.area.width().set(40);
		add.area.height().set(40);
		pane.addWidget(add, 450, 10);
		
		final Button remove = new Button();
		remove.text().set("-");
		remove.font().set(font);
		remove.area.width().set(40);
		remove.area.height().set(40);
		pane.addWidget(remove, 500, 10);
		
		root().set(pane.getAreaNode());
		
		final Label counter = new Label();
		counter.font().set(font);
		counter.text().set("0");
		
		pane.addWidget(counter, 550, 10);
		
		Insets bgInsets = new Insets(0, 0, 0, 0);
		final FillBackground bg = new FillBackground(
				new LinearGradient(0, 0, 0, 1, CoordMode.OBJECT_BOUNDING, Spread.PAD, 
					new Stop(0, new Color(180, 240, 180, 255)),
					new Stop(1, new Color(230, 255, 230, 255))
				), 
				new CornerRadii(4), bgInsets);
		
		add.activated().registerSignalListener(new SignalListener<Void>() {
			@Override
			public void onSignal(Void data) {
				for (int i = 0; i < 10; i++) {
//					CheckBox box = new CheckBox();
//					at.bestsolution.wgraf.widgets.Text box = new at.bestsolution.wgraf.widgets.Text();
					
					Button box = new Button();
					box.text().set("" + nodes.size());
					box.font().set(font);
					
					box.area.x().set(10 + Math.random() * 400);
					box.area.y().set(100 + Math.random() * 400);
					box.area.parent().set(pane.area);
				
//					Container x = new Container();
//					x.background().set(bg);
//					x.width().set(50);
//					x.height().set(50);
//					x.x().set(10 + Math.random() * 400);
//					x.y().set(100 + Math.random() * 400);
//					
//					x.parent().set(pane.getAreaNode());
//					
//					Text t = new Text();
//					t.font().set(font);
//					t.text().set("" + nodes.size());
//					t.parent().set(x);
//					
					nodes.push(box.area);
					counter.text().set(""+nodes.size());
				}
			}
		});
		
		remove.activated().registerSignalListener(new SignalListener<Void>() {
			@Override
			public void onSignal(Void data) {
				for (int i = 0; i < 10; i++) {
					if (!nodes.isEmpty()) {
						Node<?> pop = nodes.pop();
						pop.parent().set(null);
						counter.text().set(""+nodes.size());
					}
				}
			}
		});
	}
	
	
	public static void main(String[] args) {
		CheckBoxTest app = new CheckBoxTest();
		app.start(args);
	}
}
