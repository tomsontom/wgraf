package at.bestsolution.wgraf.properties;

import java.util.List;

public interface ListChangeListener<Type> {

	public static enum EntryType {
		/** entry was added 
		 * <p> <code>newIdx</code> and <code>newValue</code> are set </p>
		 */
		ADDED,
		/** entry was removed 
		 * <p> <code>oldIdx</code> and <code>oldValue</code> are set </p>
		 */
		REMOVED,
		/** entry was updated  
		 * <p> all values are set, <code>newIdx</code> and <code>newIdx</code> point to the index, 
		 * while <code>oldValue</code> represents the old value and <code>newValue</code> the new one</p>
		 */
		UPDATED,
		/** entry was moved 
		 * <p> <code>oldIdx</code> shows the oldIdx and <code>newIdx</code> the new one
		 * both <code>oldValue</code> and <code>newValue</code> hold the moved value </p>
		 */
		PERMUTATED
	}
	
	public static class DeltaEntry<Type> {
		public final EntryType type;
		public final int newIdx;
		public final int oldIdx;
		public final Type oldValue;
		public final Type newValue;
		public DeltaEntry(EntryType type, int oldIdx, int newIdx, Type oldValue, Type newValue) {
			this.type = type;
			this.newIdx = newIdx;
			this.oldIdx = oldIdx;
			this.oldValue = oldValue;
			this.newValue = newValue;
		}
	}
	
	void onChange(List<DeltaEntry<Type>> changes);
}
