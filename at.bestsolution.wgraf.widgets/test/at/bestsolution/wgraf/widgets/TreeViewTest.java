package at.bestsolution.wgraf.widgets;

import java.util.UUID;

import at.bestsolution.wgraf.Application;
import at.bestsolution.wgraf.math.Vec2d;
import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.paint.LinearGradient;
import at.bestsolution.wgraf.paint.LinearGradient.CoordMode;
import at.bestsolution.wgraf.paint.LinearGradient.Spread;
import at.bestsolution.wgraf.paint.LinearGradient.Stop;
import at.bestsolution.wgraf.properties.ChangeListener;
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
import at.bestsolution.wgraf.widgets.ListView.DefaultCellFactory;
import at.bestsolution.wgraf.widgets.ListView.SimpleLabelProvider;
import at.bestsolution.wgraf.widgets.TreeView.SimpleModel;
import at.bestsolution.wgraf.widgets.VirtualFlow.Cell;
import at.bestsolution.wgraf.widgets.VirtualFlow.Factory;

public class TreeViewTest extends Application {

	@Override
	protected void initialize() {
		
		SimpleModel root = new SimpleModel();
		root.data = "ROOT";
		
		for (int m = 1; m < 20; m++) {
			SimpleModel m1 = new SimpleModel();
			m1.data = "M" + m;
			m1.parent = root;
			root.childs.add(m1);
			
			for (int n = 1; n < 20; n++) {
				SimpleModel n1 = new SimpleModel();
				n1.data = "M" + m + " N" + n;
				n1.parent = m1;
				m1.childs.add(n1);
			}
		}
		
		title().set("Hallo Tree");
		
		AbsolutePane pane = new AbsolutePane();
		pane.getAreaNode().width().set(400d);
		pane.getAreaNode().height().set(500d);
	
		Font font = new Font("Sans", 22);
		
		{
			final TreeView<SimpleModel> myFirstTree = new TreeView<SimpleModel>();
			
			SimpleLabelProvider<SimpleModel> labelProvider = new SimpleLabelProvider<SimpleModel>() {
				@Override
				public String convert(SimpleModel value) {
					return "" + value.data;
				}
			};
			Factory<? extends Cell<? extends Node<?>, SimpleModel>> cellFactory = new DefaultCellFactory<SimpleModel>(labelProvider);
			
			myFirstTree.setCellFactory(cellFactory);
			
			myFirstTree.childConverter = new TreeView.SimpleChildConverter();
			myFirstTree.parentConverter = new TreeView.SimpleParentConverter();
			
			myFirstTree.root().set(root);
			
			myFirstTree.area.width().set(380);
			myFirstTree.area.height().set(430);
			
			myFirstTree.selection().registerChangeListener(new ChangeListener<SingleSelectionModel<SimpleModel>>() {
				
				@Override
				public void onChange(SingleSelectionModel<SimpleModel> oldValue,
						SingleSelectionModel<SimpleModel> newValue) {
					System.err.println("CURRENT TREE SELECTION : " + (newValue.isEmptySelection() ? "empty" : newValue.getSingleSelection().data));
					 
				}
			});
			pane.addWidget(myFirstTree, 10, 10);
//			
//			
//			Button addBtn = new Button();
//			addBtn.font().set(font);
//			addBtn.text().set("Add");
//			addBtn.width().set(100);
//			addBtn.height().set(40);
//			addBtn.activated().registerSignalListener(new SignalListener<Void>() {
//				@Override
//				public void onSignal(Void data) {
//					myFirstList.model().add("Added: " + UUID.randomUUID().toString());
//				}
//			});
//			pane.addWidget(addBtn, 10, 430);
//			
//			Button removeBtn = new Button();
//			removeBtn.font().set(font);
//			removeBtn.text().set("Remove");
//			removeBtn.width().set(100);
//			removeBtn.height().set(40);
//			removeBtn.activated().registerSignalListener(new SignalListener<Void>() {
//				@Override
//				public void onSignal(Void data) {
//					myFirstList.model().remove(myFirstList.model().size()-1);
//				}
//			});
//			pane.addWidget(removeBtn, 10 + 380/2, 430);
			
		}
		root().set(pane.getAreaNode());
	}
	
	
	public static void main(String[] args) {
		TreeViewTest app = new TreeViewTest();
		app.start(args);
	}
}
