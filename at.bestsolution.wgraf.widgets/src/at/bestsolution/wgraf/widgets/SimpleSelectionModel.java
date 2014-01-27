package at.bestsolution.wgraf.widgets;

import java.util.Arrays;
import java.util.List;

public class SimpleSelectionModel<Model> implements MultiSelectionModel<Model>, SingleSelectionModel<Model>{
	
	private final List<Model> selection;
	
	public SimpleSelectionModel(Model ...selection) {
		this.selection = Arrays.asList(selection);
	}
	
	
	public Model getSingleSelection() {
		return selection.get(0);
	}
	
	public List<Model> getSelection() {
		return selection;
	}
	
	public boolean isEmptySelection() {
		return selection.isEmpty();
	}
	
	public boolean isSingleSelection() {
		return selection.size() == 1;
	}
	
	public boolean isMultiSelection() {
		return selection.size() > 1;
	}

}
