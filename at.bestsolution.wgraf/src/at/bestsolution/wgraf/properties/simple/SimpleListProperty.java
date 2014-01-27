package at.bestsolution.wgraf.properties.simple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

import at.bestsolution.wgraf.properties.ListChangeListener;
import at.bestsolution.wgraf.properties.ListChangeListener.DeltaEntry;
import at.bestsolution.wgraf.properties.ListChangeListener.EntryType;
import at.bestsolution.wgraf.properties.ListProperty;

// TODO build delta for notify
public class SimpleListProperty<Type> implements ListProperty<Type> {

	private List<Type> value = new ArrayList<Type>();
	private List<ListChangeListener<Type>> listeners = new CopyOnWriteArrayList<ListChangeListener<Type>>();
	
	private DeltaEntry<Type> createAddedEntry(int idx, Type newValue) {
		return new DeltaEntry<Type>(EntryType.ADDED, -1, idx, null, newValue);
	}
	
	private DeltaEntry<Type> createRemovedEntry(int idx, Type oldValue) {
		return new DeltaEntry<Type>(EntryType.REMOVED, idx, -1, oldValue, null);
	}
	
	private DeltaEntry<Type> createUpdatedEntry(int idx, Type oldValue, Type newValue) {
		return new DeltaEntry<Type>(EntryType.UPDATED, idx, idx, oldValue, newValue);
	}
	
	private DeltaEntry<Type> createPermutatedEntry(int oldIdx, int newIdx, Type value) {
		return new DeltaEntry<Type>(EntryType.PERMUTATED, oldIdx, newIdx, value, value);
	}
	
	
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
		try {
			return value.add(e);
		}
		finally {
			notifyChange(createAddedEntry(value.indexOf(e), e));
		}
	}

	@Override
	public boolean remove(Object o) {
		final List<Type> oldValue = new ArrayList<Type>(value);
		try {
			return value.remove(o);
		}
		finally {
			// TODO do we need to have a specific order on the delta entries =?
			int removeIdx = oldValue.indexOf(o);
			final List<DeltaEntry<Type>> entries = new ArrayList<DeltaEntry<Type>>();
			entries.add(createRemovedEntry(removeIdx, (Type) o));
			// track permutation changes
			for (int i=0; i < oldValue.size(); i++) {
				if (removeIdx == i) {
					continue;
				}
				int actualIdx = i;
				if (removeIdx < i) {
					actualIdx --;
				}
				if (actualIdx != i) {
					entries.add(createPermutatedEntry(i, actualIdx, oldValue.get(i)));
				}
			}
			notifyChange(entries);
		}
		
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return value.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends Type> c) {
		// TODO notify
		int idx = value.size();
		try {
			return value.addAll(c);
		}
		finally {
			List<DeltaEntry<Type>> entries = new ArrayList<DeltaEntry<Type>>();
			for (Type t : c) {
				entries.add(createAddedEntry(idx, t));
				idx ++;
			}
			notifyChange(entries);
		}
	}

	@Override
	public boolean addAll(int index, Collection<? extends Type> c) {
		// first we ensure that the added collection will be ordered
		final List<Type> toAdd = new ArrayList<Type>(c);
		
		final List<Type> beforeAdd = new ArrayList<Type>(value);
		final int insertIdx = index;
		final int insertSize = toAdd.size();
	
		try {
			return value.addAll(index, toAdd);
		}
		finally {
			// TODO do we need to have a specific order on the delta entries =?
			final List<DeltaEntry<Type>> entries = new ArrayList<DeltaEntry<Type>>();
			for (int addIdx = insertIdx; addIdx < insertIdx + insertSize; addIdx++) {
				final Type addValue = toAdd.get(addIdx);
				entries.add(createAddedEntry(addIdx, addValue));
			}
			for (int moveIdx = insertIdx; moveIdx < beforeAdd.size(); moveIdx++) {
				final Type moveValue = beforeAdd.get(moveIdx);
				entries.add(createPermutatedEntry(moveIdx, moveIdx + insertSize, moveValue));
			}
			notifyChange(entries);
		}
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		final List<Type> oldValue = new ArrayList<Type>(value);
		try {
			return value.removeAll(c);
		}
		finally {
			// TODO do we need to have a specific order on the delta entries =?
			final List<DeltaEntry<Type>> entries = new ArrayList<DeltaEntry<Type>>();
			List<Integer> deletedIndices = new ArrayList<Integer>();
			for (int i = 0; i < oldValue.size(); i++) {
				Type old = oldValue.get(i);
				if (c.contains(i)) {
					// was deleted
					entries.add(createRemovedEntry(i, old));
					deletedIndices.add(i);
				}
			}
			// track permutation changes
			for (int i=0; i < oldValue.size(); i++) {
				if (deletedIndices.contains(i)) {
					continue;
				}
				int actualIdx = i;
				for (Integer deleted : deletedIndices) {
					if (deleted < i) {
						actualIdx --;
					}
				}
				if (actualIdx != i) {
					entries.add(createPermutatedEntry(i, actualIdx, oldValue.get(i)));
				}
			}
			notifyChange(entries);
		}
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		final List<Type> oldValue = new ArrayList<Type>(value);
		try {
			value.clear();
		}
		finally {
			// for now we send a removed delta for each item
			final List<DeltaEntry<Type>> entries = new ArrayList<DeltaEntry<Type>>();
			for (Type old : oldValue) {
				int oldIdx = oldValue.indexOf(old);
				entries.add(createRemovedEntry(oldIdx, old));
			}
			notifyChange(entries);
		}
	}

	@Override
	public Type get(int index) {
		return value.get(index);
	}

	@Override
	public Type set(int index, Type element) {
		Type oldValue = value.get(index);
		try {
			return value.set(index, element);
		}
		finally {
			notifyChange(createUpdatedEntry(index, oldValue, element));
		}
	}

	@Override
	public void add(int index, Type element) {
		final Map<Integer, Type> beforeAdd = new HashMap<Integer, Type>();
		int idx = index;
		final Iterator<Type> it = value.subList(index, value.size()).iterator();
		while (it.hasNext()) {
			Type value = it.next();
			beforeAdd.put(idx, value);
			idx++;
		}
	
		final Type oldValue = value.get(index);
		try {
			value.add(index, element);
		}
		finally {
			// TODO do we need to have a specific order on the delta entries =?
			final List<DeltaEntry<Type>> entries = new ArrayList<DeltaEntry<Type>>();
			entries.add(createUpdatedEntry(index, oldValue, element));
			for (Entry<Integer, Type> e : beforeAdd.entrySet()) {
				entries.add(createPermutatedEntry(e.getKey(), e.getKey() + 1, e.getValue()));
			}
			notifyChange(entries);
		}
	}

	@Override
	public Type remove(int index) {
		final Map<Integer, Type> beforeAdd = new HashMap<Integer, Type>();
		int idx = index + 1;
		final Iterator<Type> it = value.subList(index, value.size()).iterator();
		while (it.hasNext()) {
			Type value = it.next();
			beforeAdd.put(idx, value);
			idx++;
		}
		final Type oldValue = value.get(index);
		try {
			return value.remove(index);
		}
		finally {
			// TODO do we need to have a specific order on the delta entries =?
			final List<DeltaEntry<Type>> entries = new ArrayList<DeltaEntry<Type>>();
			entries.add(createRemovedEntry(index, oldValue));
			for (Entry<Integer, Type> e : beforeAdd.entrySet()) {
				entries.add(createPermutatedEntry(e.getKey(), e.getKey() - 1, e.getValue()));
			}
			notifyChange(entries);
		}
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
	
	private void notifyChange(List<DeltaEntry<Type>> changes) {
		for (ListChangeListener<Type> l : listeners) {
			l.onChange(changes);
		}
	}
	
	private void notifyChange(DeltaEntry<Type> change) {
		notifyChange(Collections.singletonList(change));
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
