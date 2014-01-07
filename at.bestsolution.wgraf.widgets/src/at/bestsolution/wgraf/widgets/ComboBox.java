package at.bestsolution.wgraf.widgets;

import at.bestsolution.wgraf.math.Vec2d;
import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.ListProperty;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.scene.Container;
import at.bestsolution.wgraf.scene.Node;
import at.bestsolution.wgraf.style.CornerRadii;
import at.bestsolution.wgraf.style.FillBackground;
import at.bestsolution.wgraf.style.Font;
import at.bestsolution.wgraf.style.Insets;
import at.bestsolution.wgraf.widgets.VirtualFlow.Cell;
import at.bestsolution.wgraf.widgets.VirtualFlow.Factory;

public class ComboBox<Model> extends Widget {

	private AbsolutePane popupPane;
	
	private List<Model> list;
	private Button button;
	private Text text;
	
	public ComboBox() {
		
		area.width().set(200);
		area.height().set(40);
		
		text = new Text();
		text.area.parent().set(area);
		text.area.width().set(160);
		text.area.height().set(40);
		text.font().set(new Font("Sans", 25));
		
		button = new Button();
		button.area.parent().set(area);
		button.area.width().set(40);
		button.area.height().set(40);
		button.area.x().set(160);
		
		button.activated().registerSignalListener(new SignalListener<Void>() {
			@Override
			public void onSignal(Void data) {
				if (isPopupVisible()) {
					hidePopup();
				}
				else {
					showPopup(new Vec2d(0, 40), popupPane);
					
				}
			}
		});
		
		popupPane = new AbsolutePane();
		popupPane.area.width().set(200);
		popupPane.area.height().set(200);
		popupPane.area.background().set(new FillBackground(new Color(0,0,0,200), new CornerRadii(10), new Insets(0, 0, 0, 0)));
		
		list = new List<Model>();
		list.area.width().set(180);
		list.area.height().set(180);
		list.setCellFactory(new Factory<VirtualFlow.Cell<Node<?>,Model>>() {
			@Override
			public Cell<Node<?>, Model> create() {
				final Container x = new Container();
				x.width().set(180);
				x.height().set(40);
				x.background().set(new FillBackground(new Color(255,255,255,200), new CornerRadii(10), new Insets(2, 5, 2, 5)));
				
				final at.bestsolution.wgraf.scene.Text t = new at.bestsolution.wgraf.scene.Text();
				t.font().set(new Font("Sans", 25));
				t.x().set(20);
				t.parent().set(x);
				
				Cell<Node<?>, Model> c = new Cell<Node<?>, Model>() {
					@Override
					public Node<?> getNode() {
						return x;
					}

					@Override
					public void bind(Model model) {
						t.text().set(model.toString());
					}
				};
				
				return c;
			}
		});
		list.onTap().registerSignalListener(new SignalListener<Model>() {
			@Override
			public void onSignal(Model data) {
				text.text().set(data.toString());
				hidePopup();
			}
		});
		
		popupPane.area.acceptFocus().set(true);
		popupPane.area.focus().registerChangeListener(new ChangeListener<Boolean>() {
			@Override
			public void onChange(Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					hidePopup();
				}
			}
		});
		
		popupPane.add(list, 10, 10);
		
		
	}
	
	public ListProperty<Model> model() {
		return list.model();
	}
	
}
