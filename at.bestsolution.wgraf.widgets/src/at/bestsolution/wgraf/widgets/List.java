package at.bestsolution.wgraf.widgets;

import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.properties.Binder;
import at.bestsolution.wgraf.properties.Binding;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.Converter;
import at.bestsolution.wgraf.properties.DoubleProperty;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.Setter;
import at.bestsolution.wgraf.properties.Signal;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.properties.simple.SimpleSignal;
import at.bestsolution.wgraf.scene.Container;
import at.bestsolution.wgraf.scene.Node;
import at.bestsolution.wgraf.scene.Text;
import at.bestsolution.wgraf.style.CornerRadii;
import at.bestsolution.wgraf.style.FillBackground;
import at.bestsolution.wgraf.style.Font;
import at.bestsolution.wgraf.style.Insets;

// TODO implement item selection
public class List<Model> extends VirtualFlow<Model> {

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
		
		private final SimpleLabelProvider<Model> simpleLabelProvider;
		private final PropertyLabelProvider<Model> propertyLabelProvider;
		
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

		private Container cell;
		private Text label;
		
		
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
		
		private void init() {
			cell = new Container();
			label = new Text();
			label.font().set(new Font("Sans", 20));
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
		
		
		private void initDefaultStyle() {
			cell.background().set(new FillBackground(new Color(222,222,222,255), new CornerRadii(0), new Insets(0)));
			
			Binder.uniBind(rowIdx, new Setter<Integer>() {
				@Override
				public void set(Integer value) {
					if (active.get()) {
						cell.background().set(new FillBackground(new Color(255,200,200,255), new CornerRadii(0), new Insets(0)));
					}
					else {
						if (value % 2 == 0) {
							// event
							cell.background().set(new FillBackground(new Color(255,255,255,255), new CornerRadii(0), new Insets(0)));
							
						}
						else {
							// odd
							cell.background().set(new FillBackground(new Color(220,220,220,255), new CornerRadii(0), new Insets(0)));
						}
					}
				}
			});
			
			Binder.uniBind(active, new Setter<Boolean>() {

				@Override
				public void set(Boolean value) {
					System.err.println("active change!");
					if (value) {
						cell.background().set(new FillBackground(new Color(255,200,200,255), new CornerRadii(0), new Insets(0)));
					}
					else {
						if (rowIdx.get() % 2 == 0) {
							// event
							cell.background().set(new FillBackground(new Color(255,255,255,255), new CornerRadii(0), new Insets(0)));
							
						}
						else {
							// odd
							cell.background().set(new FillBackground(new Color(220,220,220,255), new CornerRadii(0), new Insets(0)));
						}
					}
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
	
	private Property<Cell<? extends Node<?>, Model>> activeCell = new SimpleProperty<VirtualFlow.Cell<? extends Node<?>,Model>>();
	
	public List() {
		
		setCellFactory(new DefaultCellFactory<Model>());
		area.addStyleClass("list");
		
		onCellTap.registerSignalListener(new SignalListener<Cell<? extends Node<?>, Model>>() {

			@Override
			public void onSignal(Cell<? extends Node<?>, Model> data) {
				activeCell.set(data);
//				if (onTap != null) {
//					onTap.signal(model().get(data.rowIdx.get()));
//				}
			}
			
		});
		
		activeCell.registerChangeListener(new ChangeListener<VirtualFlow.Cell<? extends Node<?>,Model>>() {
			@Override
			public void onChange(
					at.bestsolution.wgraf.widgets.VirtualFlow.Cell<? extends Node<?>, Model> oldValue,
					at.bestsolution.wgraf.widgets.VirtualFlow.Cell<? extends Node<?>, Model> newValue) {
				System.err.println("activeCell change: " + oldValue + " -> " + newValue);
				
				if (oldValue != null) {
					oldValue.active.set(false);
				}
				
				if (newValue != null) {
					newValue.active.set(true);
					selection.set(model().get(newValue.rowIdx.get()));
				}
				else {
					selection.set(null);
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
	
	private Property<Model> selection = new SimpleProperty<Model>();
	public Property<Model> selection() {
		return selection;
	}
	
	
	
}
