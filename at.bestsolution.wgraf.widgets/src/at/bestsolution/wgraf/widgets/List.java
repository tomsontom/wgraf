package at.bestsolution.wgraf.widgets;

import at.bestsolution.wgraf.properties.Signal;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.properties.simple.SimpleSignal;
import at.bestsolution.wgraf.scene.Node;

// TODO implement item selection
public class List<Model> extends VirtualFlow<Model> {

	//private final DoubleTransitionProperty offset = new SimpleDoubleTransitionProperty(0);
	//private final ListProperty<Model> model = new SimpleListProperty<Model>();
	
	
	//public ListProperty<Model> model() {
	//	return model;
	//}
	
	public List() {
		area.addStyleClass("list");
		
		onCellTap.registerSignalListener(new SignalListener<Cell<Node<?>, Model>>() {

			@Override
			public void onSignal(Cell<Node<?>, Model> data) {
				if (onTap != null) {
					onTap.signal(model().get(data.rowIdx));
				}
			}
			
		});
		
	}

	private Signal<Model> onTap;
	public Signal<Model> onTap() {
		if (onTap == null) {
			onTap = new SimpleSignal<Model>();
		}
		return onTap;
	}
	
	
	
}
