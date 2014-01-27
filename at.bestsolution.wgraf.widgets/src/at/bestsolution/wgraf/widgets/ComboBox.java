package at.bestsolution.wgraf.widgets;

import at.bestsolution.wgraf.events.TapEvent;
import at.bestsolution.wgraf.math.Vec2d;
import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.paint.LinearGradient;
import at.bestsolution.wgraf.paint.LinearGradient.CoordMode;
import at.bestsolution.wgraf.paint.LinearGradient.Spread;
import at.bestsolution.wgraf.paint.LinearGradient.Stop;
import at.bestsolution.wgraf.properties.Binding;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.ListProperty;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.scene.Container;
import at.bestsolution.wgraf.scene.Node;
import at.bestsolution.wgraf.style.Backgrounds;
import at.bestsolution.wgraf.style.Border;
import at.bestsolution.wgraf.style.BorderStroke;
import at.bestsolution.wgraf.style.BorderWidths;
import at.bestsolution.wgraf.style.CornerRadii;
import at.bestsolution.wgraf.style.FillBackground;
import at.bestsolution.wgraf.style.Font;
import at.bestsolution.wgraf.style.Insets;
import at.bestsolution.wgraf.widgets.VirtualFlow.Cell;
import at.bestsolution.wgraf.widgets.VirtualFlow.Factory;

public class ComboBox<Model> extends Widget {

	private AbsolutePane popupPane;
	private Popup popup;
	
	private ListView<Model> list;
	private Button button;
	private Text text;
	private Label caption;
	
	
	private Button okButton;
	
	
	public ComboBox() {
		final Font font = new Font("Sans", 20);
		
		area.width().set(200);
		area.height().set(40);
		
		text = new Text();
		text.area.parent().set(area);
		text.area.width().set(160);
		text.area.height().set(40);
		text.font().set(font);
		
		button = new Button();
		button.area.parent().set(area);
		button.area.width().set(40);
		button.area.height().set(40);
		button.area.x().set(160);
		
		button.activated().registerSignalListener(new SignalListener<Void>() {
			@Override
			public void onSignal(Void data) {
				if (isPopupVisible(popup)) {
					hidePopup(popup);
				}
				else {
					showPopup(popup);
					
				}
			}
		});
		
		int hh = 40;
		
		int w = 300;
		int h = 250;
		
		popupPane = new AbsolutePane();
		popupPane.area.onTap().registerSignalListener(new SignalListener<TapEvent>() {
			@Override
			public void onSignal(TapEvent data) {
				data.consume();
			}
		});
		popupPane.area.width().set(w);
		popupPane.area.height().set(h);
		
		final Button up = new Button();
		up.text().set("^");
		up.font().set(font);
		up.area.width().set(40);
		up.area.height().set(40);
		popupPane.addWidget(up, w - 25 - 40, hh + 25);

		final Button down = new Button();
		down.text().set("v");
		down.font().set(font);
		down.area.width().set(40);
		down.area.height().set(40);
		popupPane.addWidget(down, w - 25 - 40, h - 40 - 25 - 40);

		caption = new Label();
		caption.font().set(font);
		caption.text().set("Caption");
		
		popupPane.addWidget(caption, 10, 10);
		
		
		
		popup = new Popup(popupPane);
		
		list = new ListView<Model>();
		list.area.width().set(w - 2 * 25 - 20 - 40);
		list.area.height().set(h - 2 * 25 - 2 * hh);
		list.onTap().registerSignalListener(new SignalListener<Model>() {
			@Override
			public void onSignal(Model data) {
				text.text().set(data.toString());
				hidePopup(popup);
			}
		});
		
		popupPane.area.acceptFocus().set(true);
		popupPane.area.focus().registerChangeListener(new ChangeListener<Boolean>() {
			@Override
			public void onChange(Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					hidePopup(popup);
				}
			}
		});
		
		popupPane.addWidget(list, 25, hh + 25);
		
		okButton = new Button();
		okButton.text().set("Ok");
		okButton.font().set(font);
		okButton.width().set(60);
		okButton.height().set(40);
		
		popupPane.addWidget(okButton, w - 20 - 60, h - 10 - 40);
		
		
		okButton.activated().registerSignalListener(new SignalListener<Void>() {
			@Override
			public void onSignal(Void data) {
				MultiSelectionModel<Model> sel = list.selection().get();
				Model m = sel.getSingleSelection();
				text.text().set(m==null?"":m.toString());
				
				hidePopup(popup);
			}
		});
		
		initDefaultStyle();
	}
	
	private void initDefaultStyle() {
		Insets listInsets = new Insets(40 + 20, 20, 40 + 20, 20);
		Insets popupInsets = new Insets(0, 0, 0, 0);
		CornerRadii radii10 = new CornerRadii(10);
		CornerRadii radii0 = new CornerRadii(0);
		
		LinearGradient bg = new LinearGradient(0, 0, 0, 1, CoordMode.OBJECT_BOUNDING, Spread.PAD, 
				new Stop(0, new Color(0, 0, 0, 200)),
				new Stop(1, new Color(50, 50, 50, 200))
			);
		LinearGradient header = new LinearGradient(0, 0, 0, 1, CoordMode.OBJECT_BOUNDING, Spread.PAD, 
				new Stop(0, new Color(255, 0, 0, 200)),
				new Stop(1, new Color(255, 50, 50, 200))
			);
		
		CornerRadii headerRadii = new CornerRadii(10, 10, 10, 10, 0, 0, 0, 0);
		Insets headerInsets = new Insets(0, 0, 250 - 40, 0);
		
		Color border = new Color(200, 200, 200, 255);
		Color white = new Color(255, 255, 255, 255);
		BorderWidths bw = new BorderWidths(3);
		
		popupPane.area.background().set(new Backgrounds(
				new FillBackground(header, headerRadii, headerInsets),
				new FillBackground(white, radii0, listInsets),
				new FillBackground(bg, radii10, popupInsets))
				);
		popupPane.area.border().set(new Border(
				new BorderStroke(border, radii0, bw, listInsets),
				new BorderStroke(border, radii10, bw, popupInsets)
				));
		
		
		caption.text.fill().set(white);
		
	}
	
	public ListProperty<Model> model() {
		return list.model();
	}
	
}
