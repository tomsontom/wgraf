package at.bestsolution.wgraf.widgets;

import java.util.List;

import at.bestsolution.wgraf.math.Rect;

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
	
	@Override
	protected void restrictViewport(Rect viewport) {
		for (Widget widget : widgets) {
			
			Rect viewportInWidgetSpace = viewport.translate(-widget.x().get(), -widget.y().get());
			Rect widgetAreaInWidgetSpace = new Rect(0, 0, widget.width().get(), widget.height().get());
			Rect widgetViewport = widgetAreaInWidgetSpace.intersect(viewportInWidgetSpace);
			
			widget.restrictViewport(widgetViewport);
		}
	}
}
