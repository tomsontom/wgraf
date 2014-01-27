package at.bestsolution.wgraf.widgets;

public interface SingleSelectionModel<Model> {

	Model getSingleSelection();
	
	boolean isEmptySelection();
}
