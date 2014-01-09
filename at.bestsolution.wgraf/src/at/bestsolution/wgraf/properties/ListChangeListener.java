package at.bestsolution.wgraf.properties;

import java.util.List;

public interface ListChangeListener<Type> {

	public static enum EntryType {
		ADDED,
		REMOVED,
		UPDATED
	}
	
	public static class DeltaEntry<Type> {
		EntryType type;
		int idx;
		Type value;
	}
	
	void onChange(List<DeltaEntry<Type>> changes);
}
