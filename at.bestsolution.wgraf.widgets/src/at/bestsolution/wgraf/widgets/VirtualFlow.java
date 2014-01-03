package at.bestsolution.wgraf.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import at.bestsolution.wgraf.events.ScrollEvent;
import at.bestsolution.wgraf.geom.shape.Rectangle;
import at.bestsolution.wgraf.properties.Binder;
import at.bestsolution.wgraf.properties.Converter;
import at.bestsolution.wgraf.properties.DoubleChangeListener;
import at.bestsolution.wgraf.properties.DoubleTransitionProperty;
import at.bestsolution.wgraf.properties.ListChangeListener;
import at.bestsolution.wgraf.properties.ListProperty;
import at.bestsolution.wgraf.properties.Setter;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.properties.simple.SimpleDoubleTransitionProperty;
import at.bestsolution.wgraf.properties.simple.SimpleListProperty;
import at.bestsolution.wgraf.scene.Node;
import at.bestsolution.wgraf.scene.Text;
import at.bestsolution.wgraf.style.Font;
import at.bestsolution.wgraf.transition.TouchScrollTransition;

public class VirtualFlow<Model> extends Widget {

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
			System.err.println("visiblitiy: " + firstVisible + " -> " + lastVisible);
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
	
	private Factory<Node<?>> cellFactory;
	
	private ListProperty<Model> model = new SimpleListProperty<Model>();
	
	private VisualRange verticalRange = new VisualRange(model.size(), cellHeight, area.height().get());
	
	private Map<Integer, Node<?>> assignedCells = new ConcurrentHashMap<Integer, Node<?>>();
	
	private Queue<Node<?>> freeCells = new ConcurrentLinkedQueue<Node<?>>();
	
	private Set<Integer> usedCells = Collections.synchronizedSet(new HashSet<Integer>());
	
	private void freeCells() {
		Iterator<Entry<Integer, Node<?>>> iterator = assignedCells.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Integer, Node<?>> entry = iterator.next();
			if (!usedCells.contains(entry.getKey())) {
//				System.err.println("freeing cell");
				freeCells.add(entry.getValue());
				// send offscreen
				entry.getValue().y().set(-100);
				iterator.remove();
			}
		}
		usedCells.clear();
	}
	
	private Node<?> getFreeCell() {
		Node<?> c = freeCells.poll();
		if (c != null) {
//			System.err.println("-> FREE");
			return c;
		}
		return createCell();
	}
	
	private Node<?> getCellByIdx(int idx) {
		Node<?> n = assignedCells.get(idx);
		if (n != null) {
//			System.err.println("-> ASSIGNED");
			usedCells.add(idx);
			return n;
		}
		
		n = getFreeCell();
		assignedCells.put(idx, n);
		usedCells.add(idx);
		return n;
	}
	
	private Node<?> createCell() {
		Node<?> newNode;
		if (cellFactory != null) {
			newNode = cellFactory.create();
		}
		else {
			Text text = new Text();
			text.font().set(new Font("Sans", 30));
			text.text().set("Cell");
			newNode = text;
		}
		newNode.setParent(area);
		return newNode;
	}
	
	public void setCellFactory(Factory<Node<?>> cellFactory) {
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
					Node<?> c = getCellByIdx(e.idx);
					c.y().set(e.offset);
					((Text)c).text().set(model.get(e.idx).toString());
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
				int s = model.size();
				verticalRange.setSize(s);
			}
			
		});
		
		area.acceptTapEvents().set(true);
		area.onScroll().registerSignalListener(new SignalListener<ScrollEvent>() {
			@Override
			public void onSignal(ScrollEvent data) {
				verticalRange.offset.incrementDynamic(data.deltaY);
			}
		});
	}
	
	private void resize() {
		double height = area.height().get();
		double width = area.width().get();
		area.clippingShape().set(new Rectangle(2, 2, width-4, height-4));
	}
	
	public ListProperty<Model> model() {
		return model;
	}
}
