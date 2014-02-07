package at.bestsolution.wgraf.widgets;

import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.paint.LinearGradient;
import at.bestsolution.wgraf.paint.LinearGradient.CoordMode;
import at.bestsolution.wgraf.paint.LinearGradient.Spread;
import at.bestsolution.wgraf.paint.LinearGradient.Stop;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.Converter;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.Signal;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.properties.binding.Binder;
import at.bestsolution.wgraf.properties.binding.Binding;
import at.bestsolution.wgraf.properties.binding.Setter;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.properties.simple.SimpleSignal;
import at.bestsolution.wgraf.scene.Container;
import at.bestsolution.wgraf.scene.Node;
import at.bestsolution.wgraf.scene.Text;
import at.bestsolution.wgraf.style.Background;
import at.bestsolution.wgraf.style.CornerRadii;
import at.bestsolution.wgraf.style.FillBackground;
import at.bestsolution.wgraf.style.Font;
import at.bestsolution.wgraf.style.Insets;

// TODO implement item selection
public class ListView<Model> extends VirtualFlow<Model> {

	//private final DoubleTransitionProperty offset = new SimpleDoubleTransitionProperty(0);
	//private final ListProperty<Model> model = new SimpleListProperty<Model>();


	//public ListProperty<Model> model() {
	//	return model;
	//}

	public static interface SimpleLabelProvider<Model> extends Converter<Model, String> {}
	public static interface PropertyLabelProvider<Model> extends Converter<Model, Property<String>> {}
	public static class DefaultLabelProvider<Model> implements SimpleLabelProvider<Model> {
		@Override
		public String convert(Model value) {
			return value==null?"null":value.toString();
		}
	}

	public static class DefaultCellFactory<Model> implements Factory<DefaultCell<Model>> {

		protected final SimpleLabelProvider<Model> simpleLabelProvider;
		protected final PropertyLabelProvider<Model> propertyLabelProvider;

		public DefaultCellFactory() {
			this(new DefaultLabelProvider<Model>());
		}

		public DefaultCellFactory(SimpleLabelProvider<Model> simpleLabelProvider) {
			this.simpleLabelProvider = simpleLabelProvider;
			this.propertyLabelProvider = null;
		}

		public DefaultCellFactory(PropertyLabelProvider<Model> propertyLabelProvider) {
			this.simpleLabelProvider = null;
			this.propertyLabelProvider = propertyLabelProvider;
		}

		@Override
		public DefaultCell<Model> create() {
			if (simpleLabelProvider == null) {
				return new DefaultCell<Model>(propertyLabelProvider);
			}
			else {
				return new DefaultCell<Model>(simpleLabelProvider);
			}
		}

	}

	public static class DefaultCell<Model> extends Cell<Container, Model> {

		protected Container cell;
		protected Text label;


		private final SimpleLabelProvider<Model> simpleLabelProvider;
		private final PropertyLabelProvider<Model> propertyLabelProvider;

		public DefaultCell() {
			this(new DefaultLabelProvider<Model>());

			init();
		}

		public DefaultCell(SimpleLabelProvider<Model> simpleLabelProvider) {
			this.simpleLabelProvider = simpleLabelProvider;
			this.propertyLabelProvider = null;

			init();
		}

		public DefaultCell(PropertyLabelProvider<Model> propertyLabelProvider) {
			this.simpleLabelProvider = null;
			this.propertyLabelProvider = propertyLabelProvider;

			init();
		}

		private static int cellIdCounter = 0;

		protected void init() {
			cell = new Container();
//			Text nfo = new Text();
//			nfo.text().set("" + (++cellIdCounter) + " birth: " + System.currentTimeMillis());
//			nfo.parent().set(cell);
			label = new Text();
			label.font().set(Font.UBUNTU);
			label.parent().set(cell);
			label.x().set(20);
			Binder.uniBind(cell.height(), new Setter<Double>() {
				@Override
				public void set(Double value) {
					final double textH = label.font().get().stringExtent("Aq").y;
					label.y().set(value/2 - textH/2);
				}
			});

			Binder.uniBind(label.font(), new Setter<Font>() {
				@Override
				public void set(Font value) {
					final double textH = value.stringExtent("Aq").y;
					label.y().set(cell.height().get()/2 - textH/2);
				}
			});
			initDefaultStyle();
		}

		private void reSkin() {

			Background even = new FillBackground(
					new LinearGradient(0, 0, 1, 1, CoordMode.OBJECT_BOUNDING, Spread.PAD,
							new Stop(0, new Color(220,220,220,150)),
							new Stop(0.4, new Color(250,250,250,150)),
							new Stop(1, new Color(250,250,250,150))
							),
							new CornerRadii(0), new Insets(0)
					);

			Background odd = new FillBackground(
					new LinearGradient(0, 0, 1, 1, CoordMode.OBJECT_BOUNDING, Spread.PAD,
							new Stop(0, new Color(210,210,210,150)),
							new Stop(0.4, new Color(240,240,240,150)),
							new Stop(1, new Color(240,240,240,150))
							),
							new CornerRadii(0), new Insets(0)
					);

			Background focus = new FillBackground(
					Skin.LIST_SELECTION.get(),
							new CornerRadii(0), new Insets(0)
					);


			final boolean active = this.active.get();
			final int index = this.rowIdx.get();

			String log = "reSkin: skinning cell " + index;
			if (active) {
				log+= " as active";
				cell.background().set(focus);
			}
			else {
				if (index % 2 == 0) {
					// even
					log += " as even";
					cell.background().set(even);

				}
				else {
					// odd
					log += " as odd";
					cell.background().set(odd);
				}
			}
//			System.err.println(log);
		}

		private void initDefaultStyle() {
			cell.background().set(new FillBackground(new Color(222,222,222,255), new CornerRadii(0), new Insets(0)));

			rowIdx.registerChangeListener(new ChangeListener<Integer>() {
				@Override
				public void onChange(Integer oldValue, Integer newValue) {
					reSkin();
				}
			});
			active.registerChangeListener(new ChangeListener<Boolean>() {
				@Override
				public void onChange(Boolean oldValue, Boolean newValue) {
					reSkin();
				}
			});
		}

		@Override
		public Container getNode() {
			return cell;
		}

		@Override
		public Binding bind(Model model) {
			if (simpleLabelProvider == null) {
				return Binder.uniBind(
						propertyLabelProvider.convert(model),
						label.text());
			}
			else {
				label.text().set(simpleLabelProvider.convert(model));
				return null;
			}
		}

	}

//	private Property<Cell<? extends Node<?>, Model>> activeCell = new SimpleProperty<VirtualFlow.Cell<? extends Node<?>,Model>>();

	private Property<Integer> activeRow = new SimpleProperty<Integer>(null);

	public ListView() {
		setCellFactory(new DefaultCellFactory<Model>());
		area.addStyleClass("list");

		onCellTap.registerSignalListener(new SignalListener<Cell<? extends Node<?>, Model>>() {

			@Override
			public void onSignal(Cell<? extends Node<?>, Model> data) {
				System.err.println("ON CELL TAP " + data.rowIdx.get());
				activeRow.set(data.rowIdx.get());

				Model model = model().get(data.rowIdx.get());
				selection.set(new SimpleSelectionModel<Model>(model));
//				activeCell.set(data);
//				if (onTap != null) {
//					onTap.signal(model().get(data.rowIdx.get()));
//				}
			}

		});

		activeRow.registerChangeListener(new ChangeListener<Integer>() {
			@Override
			public void onChange(Integer oldValue, Integer newValue) {
				if (oldValue != null) {
					deactivateRow(oldValue);
				}
				if (newValue != null) {
					activateRow(newValue);
				}
			}
		});

//		activeCell.registerChangeListener(new ChangeListener<VirtualFlow.Cell<? extends Node<?>,Model>>() {
//			@Override
//			public void onChange(
//					at.bestsolution.wgraf.widgets.VirtualFlow.Cell<? extends Node<?>, Model> oldValue,
//					at.bestsolution.wgraf.widgets.VirtualFlow.Cell<? extends Node<?>, Model> newValue) {
//				System.err.println("activeCell change: " + oldValue + " -> " + newValue);
//
//				if (oldValue != null) {
//					oldValue.active.set(false);
//				}
//
//				if (newValue != null) {
//					newValue.active.set(true);
//					selection.set(model().get(newValue.rowIdx.get()));
//				}
//				else {
//					selection.set(null);
//				}
//
//			}
//		});


		selection.registerChangeListener(new ChangeListener<MultiSelectionModel<Model>>() {
			@Override
			public void onChange(MultiSelectionModel<Model> oldValue, MultiSelectionModel<Model> newValue) {
				if (newValue.isEmptySelection()) {
					activeRow.set(null);
				}
				else {
					activeRow.set(model().indexOf(newValue.getSingleSelection()));
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

	private Property<MultiSelectionModel<Model>> selection = new SimpleProperty<MultiSelectionModel<Model>>(new SimpleSelectionModel<Model>());
	public Property<MultiSelectionModel<Model>> selection() {
		return selection;
	}



}
