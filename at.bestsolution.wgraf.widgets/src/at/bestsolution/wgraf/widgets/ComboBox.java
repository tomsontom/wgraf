package at.bestsolution.wgraf.widgets;

import java.net.URI;
import java.net.URISyntaxException;

import at.bestsolution.wgraf.events.TapEvent;
import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.paint.LinearGradient;
import at.bestsolution.wgraf.paint.Paint;
import at.bestsolution.wgraf.paint.LinearGradient.CoordMode;
import at.bestsolution.wgraf.paint.LinearGradient.Spread;
import at.bestsolution.wgraf.paint.LinearGradient.Stop;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.Converter;
import at.bestsolution.wgraf.properties.ListProperty;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.binding.Setter;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.properties.binding.Binder;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.style.Background;
import at.bestsolution.wgraf.style.Backgrounds;
import at.bestsolution.wgraf.style.Border;
import at.bestsolution.wgraf.style.BorderStroke;
import at.bestsolution.wgraf.style.BorderWidths;
import at.bestsolution.wgraf.style.CornerRadii;
import at.bestsolution.wgraf.style.FillBackground;
import at.bestsolution.wgraf.style.Font;
import at.bestsolution.wgraf.style.ImageSource;
import at.bestsolution.wgraf.style.Insets;
import at.bestsolution.wgraf.widgets.Text.ButtonPosition;
import at.bestsolution.wgraf.widgets.Text.TextButton;

public class ComboBox<Model> extends Widget {

	private AbsolutePane popupPane;
	private Popup popup;

	private ListView<Model> list;
	private Text text;
	private Label caption;


	private Button okButton;
	public Converter<Model, String> labelProvider;


	private Property<String> label = new SimpleProperty<String>("");
	public Property<String> label() {
		return label;
	}


	private String getLabel(Model model) {
		String label = null;
		if (labelProvider != null) {
			label = labelProvider.convert(model);
		}
		else {
			if (model != null) {
				label = model.toString();
			}
		}

		if (label == null) label = "null";
		return label;
	}

	private Property<Model> selection = new SimpleProperty<Model>();
	public Property<Model> selection() {
		return selection;
	}

	public ComboBox() {
		final Font font = Font.UBUNTU;

		area.width().set(200);
		area.height().set(40);

		text = new Text();
		text.area.parent().set(area);
//		text.area.width().set(160);
		text.area.height().set(40);
		text.font().set(font);

		text.editable().set(false);

		final TextButton popbutton = text.addButton(ButtonPosition.RIGHT, true);
		try {
			popbutton.icon().set(new ImageSource(new URI("platform:/plugin/at.bestsolution.wgraf.widgets/images/arrowdown-black.png")));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		// TODO this is conditional styling -> needs css
		popbutton.active().registerChangeListener(new ChangeListener<Boolean>() {
			@Override
			public void onChange(Boolean oldValue, Boolean newValue) {
				if (newValue) {
					try {
						popbutton.icon().set(new ImageSource(new URI("platform:/plugin/at.bestsolution.wgraf.widgets/images/arrowdown.png")));
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
				}
				else {
					try {
						popbutton.icon().set(new ImageSource(new URI("platform:/plugin/at.bestsolution.wgraf.widgets/images/arrowdown-black.png")));
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
				}
			}
		});

		popbutton.activated().registerSignalListener(new SignalListener<Void>() {
			@Override
			public void onSignal(Void data) {
				if (isPopupVisible(popup)) {
					hidePopup(popup);
				}
				else {
					Model s = selection.get();

					SimpleSelectionModel<Model> x = s == null ? new SimpleSelectionModel<Model>() : new SimpleSelectionModel<Model>(s);
					list.selection().set(x);
					list.scrollIntoView(s);
					
					showPopup(popup);
					
				}
			}
		});



		Binder.uniBind(width(), text.width());

		int hh = 40;
		int fh = 55;

		int w = 400;
		int h = 300;

		int outerPadding = 15;
		int innerPadding = 5;

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
//		up.text().set("^");
//		up.font().set(font);
		up.area.width().set(40);
		up.area.height().set(40);
		up.area.focusProxy().set(popupPane.area);

		up.activated().registerSignalListener(new SignalListener<Void>() {
			@Override
			public void onSignal(Void data) {
				MultiSelectionModel<Model> cur = list.selection().get();
				if (cur.isSingleSelection()) {
					Model ss = cur.getSingleSelection();
					int idx = list.model().indexOf(ss);
					int prevIdx = Math.max(idx - 1, 0);
					
					Model m = list.model().get(prevIdx);
					list.selection().set(new SimpleSelectionModel<Model>(m));
					list.scrollIntoView(m);
				}
			}
		});

		try {
			up.icon().set(new ImageSource(new URI("platform:/plugin/at.bestsolution.wgraf.widgets/images/arrowup.png")));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}


		popupPane.addWidget(up, w - outerPadding - innerPadding - 40, hh + outerPadding + innerPadding);

		final Button down = new Button();
//		down.text().set("v");
//		down.font().set(font);
		down.area.width().set(40);
		down.area.height().set(40);
		down.area.focusProxy().set(popupPane.area);

		down.activated().registerSignalListener(new SignalListener<Void>() {
			@Override
			public void onSignal(Void data) {
				MultiSelectionModel<Model> cur = list.selection().get();
				if (cur.isSingleSelection()) {
					Model ss = cur.getSingleSelection();
					int idx = list.model().indexOf(ss);
					int nextIdx = Math.min(idx + 1, list.model().size()-1);
					
					Model m = list.model().get(nextIdx);
					list.selection().set(new SimpleSelectionModel<Model>(m));
					list.scrollIntoView(m);
				}
			}
		});
		
		try {
			down.icon().set(new ImageSource(new URI("platform:/plugin/at.bestsolution.wgraf.widgets/images/arrowdown.png")));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}


		popupPane.addWidget(down, w - outerPadding - innerPadding - 40, h - fh - outerPadding - innerPadding - 40);

		caption = new Label();
		caption.font().set(font);
		Binder.uniBind(label(), caption.text());

		popupPane.addWidget(caption, 10, 10);



		popup = new Popup(popupPane, true);


		list = new ListView<Model>();
		list.setCellFactory(new ListView.DefaultCellFactory<Model>(new ListView.SimpleLabelProvider<Model>() {
			@Override
			public String convert(Model value) {
				return getLabel(value);
			}
		}));
		list.area.width().set(w - 2 * outerPadding - 3 * innerPadding - 40);
		list.area.height().set(h - 2 * outerPadding - 2 * innerPadding - hh - fh);
		list.onTap().registerSignalListener(new SignalListener<Model>() {
			@Override
			public void onSignal(Model data) {
				text.text().set(getLabel(data));
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

		popupPane.addWidget(list, outerPadding + innerPadding, hh + outerPadding + innerPadding);

		okButton = new Button() {
			@Override
			protected void initDefaultStyle() {
				// this should come from css:

				CornerRadii radii = new CornerRadii(3);
				Insets insets = new Insets(0, 0, 0, 0);

				Paint normalColor = Skin.COMBO_OK.get();
				Color activeColor = new Color(34,139,34, 255);
				Color disabledColor = new Color(251, 251, 251, 255);

				final Background normalBackground = new FillBackground(normalColor, radii, insets);
				final Background activeBackground = new FillBackground(activeColor, radii, insets);
				final Background disabledBackground = new FillBackground(disabledColor, radii, insets);

				final Border normalBorder = new Border(new BorderStroke(activeColor, radii, new BorderWidths(1, 1, 1, 1), insets));

				//area.border().set(normalBorder);

				Binder.uniBind(enabled(), new Setter<Boolean>() {
					@Override
					public void set(Boolean value) {
						if (value) {
							area.background().set(normalBackground);
						}
						else {
							area.background().set(disabledBackground);
						}
					}
				});
				Binder.uniBind(active(), new Setter<Boolean>() {
					@Override
					public void set(Boolean value) {
						if (value) {
							area.background().set(activeBackground);
						}
						else {
							area.background().set(normalBackground);
						}
					}
				});

//						new FillBackground(
//						new LinearGradient(0, 0, 1, 1, CoordMode.OBJECT_BOUNDING, Spread.PAD,
//							new Stop(0, new Color(180, 240, 180, 255)),
//							new Stop(1, new Color(34,139,34, 255))
//						),
//						radii, insets);


//				final FillBackground disabled = new FillBackground(
//						new LinearGradient(0, 0, 0, 1, CoordMode.OBJECT_BOUNDING, Spread.PAD,
//							new Stop(0, new Color(251, 251, 251, 255)),
//							new Stop(1, new Color(194, 194, 194, 255))
//						),
//						radii, bgInsets);
//
//
//				final FillBackground active = new FillBackground(
//						new LinearGradient(0, 0, 1, 1, CoordMode.OBJECT_BOUNDING, Spread.PAD,
//							new Stop(0, new Color(34,139,34, 255)),
//							new Stop(1, new Color(180, 240, 180, 255))
//						),
//						radii, bgInsets);


				// white text
				nodeText.fill().set(new Color(255,255,255,255));

				area.cache().set(true);
				nodeText.cache().set(true);
			}
		};
		okButton.text().set("Ok");
		okButton.font().set(font);
		okButton.width().set(60);
		okButton.height().set(40);

		popupPane.addWidget(okButton, w - 60 - 15 , h - 40 - 15);


		okButton.activated().registerSignalListener(new SignalListener<Void>() {
			@Override
			public void onSignal(Void data) {
				MultiSelectionModel<Model> sel = list.selection().get();
				Model m = sel.getSingleSelection();
				selection().set(m);
				hidePopup(popup);
			}
		});

		Binder.uniBind(selection(), new Setter<Model>() {
			@Override
			public void set(Model value) {
				text.text().set(getLabel(value));
			}
		});

		initDefaultStyle();
	}

	private void initDefaultStyle() {

		int hh = 40;
		int fh = 55;
		int outerPadding = 15;
		int innerPadding = 5;

		Color borderColor = new Color(200, 200, 200, 255);
		Paint focusColor = Skin.HIGHLIGHT.get();

		double r = 4;


		Insets listInsets = new Insets(hh + outerPadding, outerPadding, fh + outerPadding, outerPadding);
		Insets popupInsets = new Insets(0, 0, 0, 0);


		CornerRadii radii10 = new CornerRadii(4);


		LinearGradient focus = new LinearGradient(0, 0, 1, 1, CoordMode.OBJECT_BOUNDING, Spread.PAD,
						new Stop(0, new Color(225,0,0,150)),
						new Stop(0.4, new Color(255,30,30,150)),
						new Stop(1, new Color(255,30,30,150))
						);

		Paint background = new Color(255, 255, 255, 255);

		LinearGradient bg = new LinearGradient(0, 0, 0, 1, CoordMode.OBJECT_BOUNDING, Spread.PAD,
				new Stop(0, new Color(255, 255, 255, 200)),
				new Stop(1, new Color(250, 250, 250, 200))
			);
		LinearGradient header = new LinearGradient(0, 0, 0, 1, CoordMode.OBJECT_BOUNDING, Spread.PAD,
				new Stop(0, new Color(255, 105, 105, 200)),
				new Stop(1, new Color(255, 80, 80, 200))
			);

		Paint headerPaint = new LinearGradient(0, 0, 1, 1, CoordMode.OBJECT_BOUNDING, Spread.PAD,
				new Stop(0, new Color(220,220,220,150)),
				new Stop(0.4, new Color(250,250,250,150)),
				new Stop(1, new Color(250,250,250,150))
				);

		CornerRadii headerRadii = new CornerRadii(r, r, r, r, 0, 0, 0, 0);
		Insets headerInsets = new Insets(0, 0, 300 - 40, 0);


		popupPane.area.background().set(new Backgrounds(
				new FillBackground(headerPaint, headerRadii, headerInsets),
				//new FillBackground(background, radii10, listInsets),
				new FillBackground(background, radii10, popupInsets))
				);


		popupPane.area.border().set(new Border(
				new BorderStroke(borderColor, radii10, new BorderWidths(2), Insets.NO_INSETS),
				new BorderStroke(borderColor, CornerRadii.NO_CORNER_RADII, new BorderWidths(1), listInsets)
				));


//		popupPane.area.border().set(new Border(
////				new BorderStroke(border, radii0, bw, listInsets),
//				new BorderStroke(focusColor, radii10, bw, popupInsets)
//				));


		caption.text.fill().set(focusColor);

	}

	public ListProperty<Model> model() {
		return list.model();
	}

	public TextButton addButton(ButtonPosition pos) {
		return text.addButton(pos);
	}
	
	public TextButton addButton(ButtonPosition pos, Converter<Void, TextButton> factory) {
		return text.addButton(pos, factory);
	}

}
