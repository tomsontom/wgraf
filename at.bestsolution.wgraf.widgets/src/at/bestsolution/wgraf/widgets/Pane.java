package at.bestsolution.wgraf.widgets;

import java.util.List;

public abstract class Pane extends Widget {
	private List<Widget> widgets;
	
	public void addWidget(Widget w) {
		w.getAreaNode().parent().set(area);
		w.parent = this;
	}
	
	public void layout() {
		for( Widget w : widgets ) {
			w.layout();
		}
	}
}
