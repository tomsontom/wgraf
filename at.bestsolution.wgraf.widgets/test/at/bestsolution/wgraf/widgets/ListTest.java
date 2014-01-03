package at.bestsolution.wgraf.widgets;

import at.bestsolution.wgraf.Application;
import at.bestsolution.wgraf.math.Vec2d;
import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.properties.DoubleChangeListener;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.scene.Node;
import at.bestsolution.wgraf.scene.Text;
import at.bestsolution.wgraf.style.CornerRadii;
import at.bestsolution.wgraf.style.FillBackground;
import at.bestsolution.wgraf.style.Font;
import at.bestsolution.wgraf.style.Insets;
import at.bestsolution.wgraf.widgets.VirtualFlow.Factory;

public class ListTest extends Application {

	@Override
	protected void initialize() {
		
		title().set("Hallo List");
		
		AbsolutePane pane = new AbsolutePane();
		pane.getAreaNode().width().set(400d);
		pane.getAreaNode().height().set(500d);
	
		Font font = new Font("Sans", 22);
		
		{
			List<String> myFirstList = new List<String>();
			
			myFirstList.area.width().set(380);
			myFirstList.area.height().set(480);
			myFirstList.area.background().set(new FillBackground(new Color(255,  255,  0, 25), new CornerRadii(2), new Insets(0, 0, 0, 0)));
			myFirstList.setCellFactory(new Factory<Node<?>>() {
				@Override
				public Node<?> create() {
					Text text = new Text();
					text.font().set(new Font("Sans", 25));
					return text;
				}
				
			});
			for (int i = 0; i < 100; i ++)
				myFirstList.model().add("Line " + i);
			
			pane.add(myFirstList, 10, 10);
		}
		root().set(pane.getAreaNode());
	}
	
	
	public static void main(String[] args) {
		ListTest app = new ListTest();
		app.start(args);
	}
}
