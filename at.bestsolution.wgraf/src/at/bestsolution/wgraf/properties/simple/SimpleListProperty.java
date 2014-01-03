package at.bestsolution.wgraf.properties.simple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

import at.bestsolution.wgraf.properties.ListChangeListener;
import at.bestsolution.wgraf.properties.ListProperty;

// TODO build delta for notify
public class SimpleListProperty<Type> implements ListProperty<Type> {

	private List<Type> value = new ArrayList<Type>();
	private List<ListChangeListener<Type>> listeners = new CopyOnWriteArrayList<ListChangeListener<Type>>();
	
	@Override
	public int size() {
		return value.size();
	}

	@Override
	public boolean isEmpty() {
		return value.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return value.contains(o);
	}

	@Override
	public Iterator<Type> iterator() {
		return value.iterator();
	}

	@Override
	public Object[] toArray() {
		return value.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return value.toArray(a);
	}

	@Override
	public boolean add(Type e) {
		// TODO notify
		try {
			return value.add(e);
		}
		finally {
			notifyChange();
		}
	}

	@Override
	public boolean remove(Object o) {
		// TODO notify
		notifyChange();
		return value.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return value.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends Type> c) {
		// TODO notify
		notifyChange();
		return value.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends Type> c) {
		// TODO notify
		notifyChange();
		return value.addAll(index, c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO notify
		notifyChange();
		return value.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO notify
		notifyChange();
		value.clear();
	}

	@Override
	public Type get(int index) {
		return value.get(index);
	}

	@Override
	public Type set(int index, Type element) {
		// TODO notify
		notifyChange();
		return value.set(index, element);
	}

	@Override
	public void add(int index, Type element) {
		// TODO notify
		notifyChange();
		value.add(index, element);
	}

	@Override
	public Type remove(int index) {
		// TODO notify
		notifyChange();
		return value.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return value.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return value.lastIndexOf(o);
	}

	@Override
	public ListIterator<Type> listIterator() {
		return value.listIterator();
	}

	@Override
	public ListIterator<Type> listIterator(int index) {
		return value.listIterator(index);
	}

	@Override
	public List<Type> subList(int fromIndex, int toIndex) {
		return value.subList(fromIndex, toIndex);
	}
	
	private void notifyChange() {
		for (ListChangeListener<Type> l : listeners) {
			l.onChange(Collections.EMPTY_LIST);
		}
	}

	@Override
	public void registerChangeListener(ListChangeListener<Type> listener) {
		listeners.add(listener);
	}

	@Override
	public void unregisterChangeListener(ListChangeListener<Type> listener) {
		listeners.remove(listener);
	}

}
