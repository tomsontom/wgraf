package at.bestsolution.wgraf.widgets;

public class AbsolutePane extends Pane {

	public void add(Widget widget, double x, double y) {
		widget.getAreaNode().setParent(area);
		widget.getAreaNode().x().set(x);
		widget.getAreaNode().y().set(y);
	}

}
