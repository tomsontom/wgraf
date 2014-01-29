package at.bestsolution.wgraf.widgets;

import java.util.ArrayList;
import java.util.List;

import at.bestsolution.wgraf.math.Rect;
import at.bestsolution.wgraf.scene.Node;

public abstract class Pane extends Widget {
	protected List<Widget> widgets = new ArrayList<Widget>();
	
	public void addWidget(Widget w) {
		widgets.add(w);
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
		System.err.println("Pane#restrictViewport("+viewport+")");
		if (viewport == null) {
			for (Widget widget : widgets) {
				widget.restrictViewport(null);
			}
		}
		else {
			for (Widget widget : widgets) {
				
				Rect viewportInWidgetSpace = viewport.translate(-widget.x().get(), -widget.y().get());
				Rect widgetAreaInWidgetSpace = new Rect(0, 0, widget.width().get(), widget.height().get());
				Rect widgetViewport = widgetAreaInWidgetSpace.intersect(viewportInWidgetSpace);
				
				widget.restrictViewport(widgetViewport);
			}
		}
	}

	@Override
	protected void scrollIntoViewport(Node<?> newValue) {
		for (Widget widget : widgets) {
			widget.scrollIntoViewport(newValue);
		}
	}
}
