package at.bestsolution.wgraf.widgets;

import at.bestsolution.wgraf.math.Vec2d;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.DoubleChangeListener;

public class ScrollPane extends Pane {

	private Widget content;
	
	private ScrollBar bar;
	
	private boolean fitVertically = false;
	private boolean fitHorizontally = false;
	
	public void setContent(Widget content) {
		this.content = content;
		
		content.getAreaNode().setParent(area);
		
		area.width().registerChangeListener(new DoubleChangeListener() {
			@Override
			public void onChange(double oldValue, double newValue) {
				resize();
			}
		});
		area.height().registerChangeListener(new DoubleChangeListener() {

			@Override
			public void onChange(double oldValue, double newValue) {
				resize();
			}
		});
	}
	
	
	private void resize() {
		double parentWidth = area.width().get();
		double parentHeight = area.height().get();
		
		Vec2d contentSize = content.computePreferredSize();
		
		
		double width = fitHorizontally ?
				contentSize.x < parentWidth ? parentWidth : contentSize.x :
					contentSize.x;
		
		double height = fitVertically ?
				contentSize.y < parentHeight ? parentHeight : contentSize.y : 
					contentSize.y;
		
		content.getAreaNode().width().set(width);
		content.getAreaNode().height().set(height);
	}
}
