package at.bestsolution.wgraf.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import at.bestsolution.wgraf.events.ScrollEvent;
import at.bestsolution.wgraf.events.TapEvent;
import at.bestsolution.wgraf.geom.shape.Rectangle;
import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.properties.Binder;
import at.bestsolution.wgraf.properties.Binding;
import at.bestsolution.wgraf.properties.ClampedDoubleIncrement;
import at.bestsolution.wgraf.properties.Converter;
import at.bestsolution.wgraf.properties.DoubleChangeListener;
import at.bestsolution.wgraf.properties.DoubleTransitionProperty;
import at.bestsolution.wgraf.properties.ListChangeListener;
import at.bestsolution.wgraf.properties.ListProperty;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.Setter;
import at.bestsolution.wgraf.properties.Signal;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.properties.simple.SimpleDoubleTransitionProperty;
import at.bestsolution.wgraf.properties.simple.SimpleListProperty;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.properties.simple.SimpleSignal;
import at.bestsolution.wgraf.scene.Container;
import at.bestsolution.wgraf.scene.Node;
import at.bestsolution.wgraf.scene.Text;
import at.bestsolution.wgraf.style.CornerRadii;
import at.bestsolution.wgraf.style.FillBackground;
import at.bestsolution.wgraf.style.Font;
import at.bestsolution.wgraf.style.Insets;
import at.bestsolution.wgraf.transition.TouchScrollTransition;

// TODO item resizing
// TODO some converters like model -> label
// TODO support multiple columns
// TODO cell disposing & cell binding cleanup
public class VirtualFlow<Model> extends Widget {

	public static abstract class Cell<Type extends Node<?>, Model> {
		public abstract Type getNode();
		public abstract Binding bind(Model model);
		
		public int colIdx;
		public final Property<Integer> rowIdx = new SimpleProperty<Integer>(-1);
		public final Property<Boolean> active = new SimpleProperty<Boolean>(false);
		
	}
	
	public static interface Factory<Type> {
		Type create();
	}
	
	public static class VisibleElement {
		public VisibleElement(int idx, double offset) {
			this.idx = idx; 
			this.offset = offset;
		}
		public final int idx;
		public final double offset;
	}
	
	public static interface VisualRangeChangeListener {
		void onChange(List<VisibleElement> newValue);
	}
	
	public static class VisualRange {
		private VisualRangeChangeListener listener;
		
		private int size;
		
		private double windowSize;
		
		private Converter<Integer, Double> elementSize;
		
		private DoubleTransitionProperty offset = new SimpleDoubleTransitionProperty(0);
		
		public VisualRange(final int size, final Converter<Integer, Double> elementSize, final double windowSize) {
			this.size = size;
			this.elementSize = elementSize;
			this.windowSize = windowSize;
			
			offset.registerChangeListener(new DoubleChangeListener() {
				@Override
				public void onChange(double oldValue, double newValue) {
					recalc(newValue);
				}
			});
			offset.setTransition(new TouchScrollTransition());
		}
		
		public void setSize(int size) {
			this.size = size;
			recalc(offset.get());
		}
		
		public void setElementSize(Converter<Integer, Double> elementSize) {
			this.elementSize = elementSize;
			recalc(offset.get());
		}
		
		public void setWindowSize(double windowSize) {
			this.windowSize = windowSize;
			recalc(offset.get());
		}
		
		private void recalc(double newOffset) {
			boolean first = false;
			
			int firstVisible = 0;
			int lastVisible = 0;
			
			double firstOffset = 0;
			
			double lastElementOffset = newOffset;
			double elementOffset = 0;
			for (int i = 0; i < size; i++) {
				elementOffset += elementSize.convert(i);
				
				if (!first && elementOffset > newOffset) {
					firstVisible = i;
					firstOffset = lastElementOffset;
					first = true;
				}
				
				if (elementOffset > newOffset + windowSize) {
					lastVisible = Math.min(i + 1, size - 1);
					break;
				}
				
				lastElementOffset = newOffset - elementOffset;
			}
			
			if (lastVisible == 0) {
				lastVisible = Math.max(0, size - 1);
			}
//			System.err.println("visiblitiy: " + firstVisible + " -> " + lastVisible);
//			System.err.println("firstOffset: " + firstOffset);
			
			List<VisibleElement> result = new ArrayList<VisibleElement>();
			if (first) {
				double offset = -firstOffset;
				for (int i = firstVisible; i <= lastVisible; i++) {
					
					// set offset to i
	//				System.err.println("idx " + i + " has " + offset);
					
					result.add(new VisibleElement(i, offset));
					
					offset += elementSize.convert(i);
				}
			}
			listener.onChange(result);
		}
		
		
		public void setListener(VisualRangeChangeListener l )  {
			this.listener = l;
		}
	}
	
	
	private Converter<Integer, Double> cellHeight = new Converter<Integer, Double>() {
		@Override
		public Double convert(Integer value) {
			return 40d;
		}
	};
	
	protected Factory<? extends Cell<? extends Node<?>, Model>> cellFactory;
	
	private ListProperty<Model> model = new SimpleListProperty<Model>();
	
	private VisualRange verticalRange = new VisualRange(model.size(), cellHeight, area.height().get());
	
	private Map<Integer, Cell<? extends Node<?>, Model>> assignedCells = new ConcurrentHashMap<Integer, Cell<? extends Node<?>, Model>>();
	
	private Queue<Cell<? extends Node<?>, Model>> freeCells = new ConcurrentLinkedQueue<Cell<? extends Node<?>, Model>>();
	
	private Set<Integer> usedCells = Collections.synchronizedSet(new HashSet<Integer>());
	
	private Set<Integer> activeRows = new HashSet<Integer>();
	
	protected void activateRow(int idx) {
		System.err.println("ACTIVATE " + idx);
		activeRows.add(idx);
		Cell<? extends Node<?>, Model> cell = assignedCells.get(idx);
		if (cell != null) {
			cell.active.set(true);
		}
	}
	
	protected void deactivateRow(int idx) {
		System.err.println("DEACTIVATE " + idx);
		activeRows.remove(idx);
		Cell<? extends Node<?>, Model> cell = assignedCells.get(idx);
		if (cell != null) {
			cell.active.set(false);
		}
	}
	
	private void freeCells() {
		Iterator<Entry<Integer, Cell<? extends Node<?>, Model>>> iterator = assignedCells.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Integer, Cell<? extends Node<?>, Model>> entry = iterator.next();
			if (!usedCells.contains(entry.getKey())) {
//				System.err.println("freeing cell");
				freeCells.add(entry.getValue());
				// send offscreen
				// clean up cell state
				
				// offscrren debug
//				if (entry.getValue().getNode() instanceof Container) {
//					((Container) entry.getValue().getNode()).background().set(new FillBackground(new Color(0, 255, 0, 100), new CornerRadii(0), new Insets(0)));
//				}
				
				entry.getValue().active.set(false);
				entry.getValue().getNode().y().set(-100);
				iterator.remove();
			}
		}
		usedCells.clear();
	}
	
	private Cell<? extends Node<?>, Model> getFreeCell() {
		Cell<? extends Node<?>, Model> c = freeCells.poll();
		if (c != null) {
//			System.err.println("-> FREE");
			return c;
		}
		return createCell();
	}
	
	private Cell<? extends Node<?>, Model> getCellByIdx(int idx) {
		Cell<? extends Node<?>, Model> n = assignedCells.get(idx);
		if (n != null) {
//			System.err.println("-> ASSIGNED");
			usedCells.add(idx);
			n.rowIdx.set(idx);
			return n;
		}
		
		n = getFreeCell();
		assignedCells.put(idx, n);
		usedCells.add(idx);
		n.rowIdx.set(idx);
		n.active.set(activeRows.contains(idx));
		return n;
	}
	
	private void onCellTap(Cell<? extends Node<?>, Model> cell) {
		System.err.println("tapped on " + cell.rowIdx);
		onCellTap.signal(cell);
	}
	
	protected Signal<Cell<? extends Node<?>, Model>> onCellTap = new SimpleSignal<Cell<? extends Node<?>, Model>>();
	
	private Cell<? extends Node<?>, Model> createCell() {
		Cell<? extends Node<?>, Model> newNode;
		if (cellFactory != null) {
			newNode = cellFactory.create();
			final Cell<? extends Node<?>, Model> n = newNode;
			if (newNode.getNode() instanceof Container) {
				Container co = (Container)newNode.getNode();
				// TODO bind?
				// TODO unbind?
				
				Binder.uniBind(area.width(), co.width());
				//co.width().set(area.width().get());
			}
			newNode.getNode().acceptTapEvents().set(true);
			newNode.getNode().onTap().registerSignalListener(new SignalListener<TapEvent>() {
				@Override
				public void onSignal(TapEvent data) {
					onCellTap(n);
					data.consume();
				}
			});
		}
		else {
			final Text text = new Text();
			text.font().set(new Font("Sans", 30));
			text.text().set("No cell Factory!");
			newNode = new Cell<Node<?>, Model>() {
				@Override
				public Node<?> getNode() {
					return text;
				}
				@Override
				public Binding bind(Model model) {
					return null;
				}
			};
		}
		newNode.getNode().parent().set(area);
		return newNode;
	}
	
	public void setCellFactory(Factory<? extends Cell<? extends Node<?>, Model>> cellFactory) {
		this.cellFactory = cellFactory;
	}
	
	public VirtualFlow() {

		area.height().registerChangeListener(new DoubleChangeListener() {
			@Override
			public void onChange(double oldValue, double newValue) {
				resize();
			}
		});
		
		area.width().registerChangeListener(new DoubleChangeListener() {
			@Override
			public void onChange(double oldValue, double newValue) {
				resize();
			}
		});
		
		verticalRange.setListener(new VisualRangeChangeListener() {
			@Override
			public void onChange(List<VisibleElement> newValue) {
				for (VisibleElement e : newValue) {
					Cell<? extends Node<?>, Model> c = getCellByIdx(e.idx);
					c.getNode().y().set(e.offset);
					if (c.getNode() instanceof Container) {
						Container co = (Container)c.getNode();
						co.height().set(cellHeight.convert(e.idx));
					}
					c.bind(model.get(e.idx));
				}
				freeCells();
			}
		});
		
		Binder.uniBind(area.height(), new Setter<Double>() {
			@Override
			public void set(Double value) {
				verticalRange.setWindowSize(value);
			}
		});
		
		model.registerChangeListener(new ListChangeListener<Model>() {
			@Override
			public void onChange(List<DeltaEntry<Model>> changes) {
				for (DeltaEntry<Model> d : changes) {
					if (d.type == EntryType.REMOVED) {
						// if entry was removed we drop the focus
						deactivateRow(d.oldIdx);
					}
				}
				
				int s = model.size();
				verticalRange.setSize(s);
			}
			
		});
		
		area.acceptTapEvents().set(true);
		area.onScroll().registerSignalListener(new SignalListener<ScrollEvent>() {
			@Override
			public void onSignal(ScrollEvent data) {
				verticalRange.offset.update(new ClampedDoubleIncrement(data.deltaY, 0, calculateMaxYOffset()));
				data.consume();
			}
		});
	}
	
	private double calculateMaxYOffset() {
		double result = 0;
		for (int i = 0; i < model.size(); i++) {
			result += verticalRange.elementSize.convert(i);
		}
		result -= verticalRange.windowSize;
		return result;
	}
	
	private void resize() {
		double height = area.height().get();
		double width = area.width().get();
		// TODO fix item size
		area.clippingShape().set(new Rectangle(2, 2, width-4, height-4));
	}
	
	public ListProperty<Model> model() {
		return model;
	}
}
