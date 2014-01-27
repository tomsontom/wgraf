package at.bestsolution.wgraf.widgets;

import java.util.List;

public interface MultiSelectionModel<Model> extends SingleSelectionModel<Model> {

	List<Model> getSelection();
	
	boolean isSingleSelection();
	boolean isMultiSelection();
	
}
