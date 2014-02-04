package at.bestsolution.wgraf.widgets;

import java.util.UUID;

import at.bestsolution.wgraf.Application;
import at.bestsolution.wgraf.math.Vec2d;
import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.paint.LinearGradient;
import at.bestsolution.wgraf.paint.LinearGradient.CoordMode;
import at.bestsolution.wgraf.paint.LinearGradient.Spread;
import at.bestsolution.wgraf.paint.LinearGradient.Stop;
import at.bestsolution.wgraf.properties.DoubleChangeListener;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.properties.binding.Binding;
import at.bestsolution.wgraf.scene.Container;
import at.bestsolution.wgraf.scene.Node;
import at.bestsolution.wgraf.scene.Text;
import at.bestsolution.wgraf.style.CornerRadii;
import at.bestsolution.wgraf.style.FillBackground;
import at.bestsolution.wgraf.style.Font;
import at.bestsolution.wgraf.style.Insets;
import at.bestsolution.wgraf.widgets.VirtualFlow.Cell;
import at.bestsolution.wgraf.widgets.VirtualFlow.Factory;

public class ListTest extends Application {

	@Override
	protected void initialize() {
		
		title().set("Hallo List");
		
		AbsolutePane pane = new AbsolutePane();
		pane.getAreaNode().width().set(400d);
		pane.getAreaNode().height().set(500d);
	
		Font font = Font.UBUNTU.resize(22);
		
		{
			final ListView<String> myFirstList = new ListView<String>();
			
			myFirstList.area.width().set(380);
			myFirstList.area.height().set(430);
			
			
			for (int i = 0; i < 10; i ++)
				myFirstList.model().add("Line " + i);
			
			pane.addWidget(myFirstList, 10, 10);
			
			
			Button addBtn = new Button();
			addBtn.font().set(font);
			addBtn.text().set("Add");
			addBtn.width().set(100);
			addBtn.height().set(40);
			addBtn.activated().registerSignalListener(new SignalListener<Void>() {
				@Override
				public void onSignal(Void data) {
					myFirstList.model().add("Added: " + UUID.randomUUID().toString());
				}
			});
			pane.addWidget(addBtn, 10, 430);
			
			Button removeBtn = new Button();
			removeBtn.font().set(font);
			removeBtn.text().set("Remove");
			removeBtn.width().set(100);
			removeBtn.height().set(40);
			removeBtn.activated().registerSignalListener(new SignalListener<Void>() {
				@Override
				public void onSignal(Void data) {
					myFirstList.model().remove(myFirstList.model().size()-1);
				}
			});
			pane.addWidget(removeBtn, 10 + 380/2, 430);
			
		}
		root().set(pane.getAreaNode());
	}
	
	
	public static void main(String[] args) {
		ListTest app = new ListTest();
		app.start(args);
	}
}
