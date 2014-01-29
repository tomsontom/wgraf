package at.bestsolution.wgraf.widgets;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import at.bestsolution.wgraf.events.TapEvent;
import at.bestsolution.wgraf.properties.Binding;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.Converter;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.properties.binding.Binder;
import at.bestsolution.wgraf.properties.binding.Setter;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.scene.Container;
import at.bestsolution.wgraf.scene.Image;
import at.bestsolution.wgraf.scene.Node;
import at.bestsolution.wgraf.style.Font;
import at.bestsolution.wgraf.style.ImageSource;
import at.bestsolution.wgraf.widgets.ListView.DefaultCell;
import at.bestsolution.wgraf.widgets.ListView.PropertyLabelProvider;
import at.bestsolution.wgraf.widgets.ListView.SimpleLabelProvider;
import at.bestsolution.wgraf.widgets.VirtualFlow.Cell;
import at.bestsolution.wgraf.widgets.VirtualFlow.Factory;

public class TreeView<Model> extends Widget {

	public static class DefaultCellFactory<Model> extends ListView.DefaultCellFactory<Model> {
		private TreeView<Model> treeView;
		public void setTreeView(TreeView<Model> treeView) {
			this.treeView = treeView;
		}
		
		public DefaultCellFactory(SimpleLabelProvider<Model> p) {
			super(p);
		}
		
		public DefaultCellFactory(PropertyLabelProvider<Model> p) {
			super(p);
		}
		
		@Override
		public at.bestsolution.wgraf.widgets.ListView.DefaultCell<Model> create() {
			DefaultCell<Model> c;
			if (simpleLabelProvider == null) {
				 c = new DefaultCell<Model>(propertyLabelProvider);
			}
			else {
				c = new DefaultCell<Model>(simpleLabelProvider);
			}
			c.setTreeView(treeView);
			return c;
		}
	}
	
	public static class DefaultCell<Model> extends ListView.DefaultCell<Model> {
		
		private TreeView<Model> treeView;
		public void setTreeView(TreeView<Model> treeView) {
			this.treeView = treeView;
		}
		
		public DefaultCell(SimpleLabelProvider<Model> p) {
			super(p);
		}
		
		public DefaultCell(PropertyLabelProvider<Model> p) {
			super(p);
		}
		
		
		private ImageSource arrowRight;
		
		protected Image childIcon;
		
		@Override
		protected void init() {
			super.init();
			// TODO platform uri will not work outside osgi
			try {
				childIcon = new Image();
				
				if (arrowRight == null) {
					arrowRight = new ImageSource(new URI("platform:/plugin/at.bestsolution.wgraf.widgets/images/treearrow.png"));
				}
				
				childIcon.image().set(arrowRight);
			
				childIcon.parent().set(cell);
				Binder.uniBind(cell.width(), new Setter<Double>() {
					@Override
					public void set(Double value) {
						childIcon.x().set(value - 30);
					}
				});
				Binder.uniBind(cell.height(), new Setter<Double>() {
					@Override
					public void set(Double value) {
						childIcon.y().set(value/2 - 20/2);
					}
				});
				
			
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		@Override
		public Binding bind(Model model) {
			Binding superBinding = super.bind(model);
			
			List<Model> childs = treeView.childConverter.convert(model);
			if (childs.isEmpty()) {
				childIcon.image().set(null);
			}
			else {
				childIcon.image().set(arrowRight);
			}
			
			
			return superBinding;
		}
	}
	
	
	public static class SimpleModel {
		public Object data;
		public final List<SimpleModel> childs = new ArrayList<SimpleModel>();
		public SimpleModel parent;
	}
	
	public static class SimpleChildConverter implements Converter<SimpleModel, List<SimpleModel>> {
		@Override
		public List<SimpleModel> convert(SimpleModel value) {
			return value.childs;
		}
	}
	
	public static class SimpleParentConverter implements Converter<SimpleModel, SimpleModel> {
		@Override
		public SimpleModel convert(SimpleModel value) {
			return value.parent;
		}
	}
	
	private Button upButton = new Button(); 
	
	private Cell<? extends Node<?>, Model> current;
	
	private ListView<Model> listView = new ListView<Model>();
	
	public Converter<Model, List<Model>> childConverter;
	public Converter<Model, Model> parentConverter;
	
	
	private Property<Model> root = new SimpleProperty<Model>();
	public Property<Model> root() {
		return root;
	}
	
	private Model currentNode = null;
	
	private void internalSetCurrent(Model model) {
		if (currentNode != model) {
			System.err.println("TreeView: internalSetCurrent: " + model);
			currentNode = model;
			current.bind(model);
			listView.model().clear();
			if (model != null) {
				List<Model> children = childConverter.convert(model);
				listView.model().addAll(children);
			}
			selection.set(new SimpleSelectionModel<Model>(model));
		}
	}
	
	private Factory<? extends Cell<? extends Node<?>, Model>> cellFactory;
	
	public void setCellFactory(Factory<? extends Cell<? extends Node<?>, Model>> factory) {
		this.cellFactory = factory;
		if (cellFactory instanceof DefaultCellFactory) {
			((DefaultCellFactory<Model>)cellFactory).setTreeView(this);
		}
		listView.setCellFactory(cellFactory);
		
		recreateCurrent();
	}
	
	private void recreateCurrent() {
		if (current != null) {
			current.getNode().parent().set(null);
		}
		current = cellFactory.create();
		current.getNode().parent().set(area);
		current.getNode().x().set(40);
		current.getNode().y().set(0);
		if (current.getNode() instanceof Container) {
			final Container c = (Container) current.getNode();
			c.height().set(40);
			Binder.uniBind(area.width(), new Setter<Double>() {
				@Override
				public void set(Double value) {
					c.width().set(value - 40);
				}
			});
			
		}
		current.getNode().onTap().registerSignalListener(new SignalListener<TapEvent>() {
			@Override
			public void onSignal(TapEvent data) {
				selection.set(new SimpleSelectionModel<Model>(currentNode));
//				selectedNode.set(currentNode);
//				// clear the selection on the list
//				listView.selection().set(new SimpleSelectionModel<Model>());
			}
		});
	}
	
//	private Property<Model> selectedNode = new SimpleProperty<Model>(null);
	
	public TreeView() {
		
		selection.registerChangeListener(new ChangeListener<SingleSelectionModel<Model>>() {
			@Override
			public void onChange(SingleSelectionModel<Model> oldValue,
					SingleSelectionModel<Model> newValue) {
				
				if (newValue.isEmptySelection()) {
					current.active.set(false);
					listView.selection().set(new SimpleSelectionModel<Model>());
				}
				else {
					if (newValue.getSingleSelection() == currentNode) {
						current.active.set(true);
						listView.selection().set(new SimpleSelectionModel<Model>());
					}
					else {
						current.active.set(false);
						listView.selection().set(new SimpleSelectionModel<Model>(newValue.getSingleSelection()));
					}
					
				}
			}
		});
		
//		upButton.font().set(new Font("Sans", 18));
//		upButton.text().set("up");
		try {
			upButton.icon().set(new ImageSource(new URI("platform:/plugin/at.bestsolution.wgraf.widgets/images/treeuparrow.png")));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		upButton.area.parent().set(area);
		upButton.width().set(40);
		upButton.height().set(40);
		upButton.x().set(0);
		upButton.y().set(0);
		
		upButton.activated().registerSignalListener(new SignalListener<Void>() {
			@Override
			public void onSignal(Void data) {
				if (currentNode != null) {
					Model parent = parentConverter.convert(currentNode);
					if (parent != null) {
						internalSetCurrent(parent);
					}
				}
			}
		});
		
		listView.selection().registerChangeListener(new ChangeListener<MultiSelectionModel<Model>>() {
			@Override
			public void onChange(MultiSelectionModel<Model> oldValue, MultiSelectionModel<Model> newValue) {
				if (newValue != null) {
					if (newValue.isEmptySelection()) {
						//selectedNode.set(null);
					}
					else {
						Model selection = newValue.getSingleSelection();
						if (selection != currentNode) {
							List<Model> childs = childConverter.convert(selection);
							if (!childs.isEmpty()) {
								internalSetCurrent(selection);
							}
							else {
								// XXX ugly loop prevention
								if (TreeView.this.selection.get().getSingleSelection() != selection) {
									TreeView.this.selection.set(new SimpleSelectionModel<Model>(selection));
								}
							}
						}
						
						//selectedNode.set(selection);
					}
				}
			}
		});
		
		current = listView.cellFactory.create();
		current.getNode().parent().set(area);
		current.getNode().x().set(40);
		current.getNode().y().set(0);
		if (current.getNode() instanceof Container) {
			final Container c = (Container) current.getNode();
			c.height().set(40);
			Binder.uniBind(area.width(), new Setter<Double>() {
				@Override
				public void set(Double value) {
					c.width().set(value - 40);
				}
			});
			
		}
		
//		selectedNode.registerChangeListener(new ChangeListener<Model>() {
//			@Override
//			public void onChange(Model oldValue, Model newValue) {
//				if (newValue == currentNode) {
//					current.active.set(true);
//				}
//				else {
//					current.active.set(false);
//				}
//			}
//		});
		
		listView.area.x().set(0);
		listView.area.y().set(40);
		listView.area.parent().set(area);
		
		Binder.uniBind(area.width(), listView.area.width());
		Binder.uniBind(area.height(), new Setter<Double>() {
			@Override
			public void set(Double value) {
				listView.area.height().set(value - 40);
			}
		});
		
		
		Binder.uniBind(root(), new Setter<Model>() {
			@Override
			public void set(Model value) {
				internalSetCurrent(value);
			}
		});
	}
	
	private Property<SingleSelectionModel<Model>> selection = new SimpleProperty<SingleSelectionModel<Model>>(new SimpleSelectionModel<Model>());
	public Property<SingleSelectionModel<Model>> selection() {
		return selection;
	}
}
