package at.bestsolution.wgraf.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import at.bestsolution.wgraf.properties.Converter;
import at.bestsolution.wgraf.scene.Container;
import at.bestsolution.wgraf.scene.Node;

public class FilterNodeIterator implements NodeIterator {

	private final Node<?> begin;
	private final Converter<Node<?>, Boolean> filter;
	
	private Node<?> current;
	private Node<?> next = null;
	private Node<?> prev = null;
	
	public FilterNodeIterator(Node<?> begin, Converter<Node<?>, Boolean> filter) {
		this.begin = begin;
		this.filter = filter;
	}
	
	private Node<?> nextSearch(Node<?> from, Node<?> callingChild) {
		System.err.println("nextSearch: " + from);
		if (from instanceof Container) {
			Container c = (Container) from;
			List<Node> children = c.getChildren();
			System.err.println(" children: " + children);
			if (callingChild != null) {
				int size = children.size();
				int callingChildIdx = Math.min(children.indexOf(callingChild)+1, size);
				System.err.println(" reducing childs: " + callingChildIdx + " - " + size);
				children = children.subList(callingChildIdx, size);
				System.err.println(" children: " + children);
			}
			for (Node <?> n : children) {
				System.err.println(" child: " + n);
				if (filter == null || filter.convert(n)) {
					System.err.println("filter on: " + n + " -> " + filter.convert(n));
					return n;
				}
				else {
					System.err.println(" DOWN");
					return nextSearch(n, null);
				}
			}
		}
		if (from.parent().get() != null) {
			Container parent = from.parent().get();
			System.err.println(" UP");
			return nextSearch(parent, from);
		}
		System.err.println(" end -.-");
		return null;
	}
	
	private Node<?> prevSearch(Node<?> from, Node<?> callingChild) {
		if (from instanceof Container) {
			Container c = (Container) from;
			List<Node> children = new ArrayList<Node>(c.getChildren());
			if (callingChild != null) {
				int size = children.size();
				int callingChildIdx = children.indexOf(callingChild);
				System.err.println(" reducing childs: " + 0 + " - " + callingChildIdx);
				children = children.subList(0, callingChildIdx);
				System.err.println(" children: " + children);
			}
			Collections.reverse(children);
			for (Node <?> n : children) {
				if (filter == null || filter.convert(n)) {
					return n;
				}
				else {
					return prevSearch(n, null);
				}
			}
		}
		if (from.parent().get() != null) {
			Container parent = from.parent().get();
			return prevSearch(parent, from);
		}
		return null;
	}
	
	private Node<?> findNext() {
		Node<?> start = current != null ? current : begin;
		return nextSearch(start, null);
	}
	
	private Node<?> findPrev() {
		Node<?> start = current != null ? current : begin;
		return prevSearch(start, null);
	}
	
	@Override
	public Node<?> next() {
		if (next == null) {
			next = findNext();
		}
		Node<?> n = next;
		this.prev = null;
		this.next = null;
		return n;
	}

	@Override
	public Node<?> prev() {
		if (prev == null) {
			prev = findPrev();
		}
		Node<?> p = prev;
		this.prev = null;
		this.next = null;
		return p;
	}

	@Override
	public boolean hasNext() {
		if (next == null) {
			next = findNext();
		}
		return next != null;
	}

	@Override
	public boolean hasPrev() {
		if (prev == null) {
			prev = findPrev();
		}
		return prev != null;
	}

}
