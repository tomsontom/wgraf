package at.bestsolution.wgraf.widgets;

public class AbsolutePane extends Pane {

	public void addWidget(Widget widget, double x, double y) {
		addWidget(widget);
		widget.getAreaNode().x().set(x);
		widget.getAreaNode().y().set(y);
	}
}
