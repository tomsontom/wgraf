package at.bestsolution.wgraf.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.bestsolution.wgraf.events.TapEvent;
import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.paint.LinearGradient;
import at.bestsolution.wgraf.paint.LinearGradient.CoordMode;
import at.bestsolution.wgraf.paint.LinearGradient.Spread;
import at.bestsolution.wgraf.paint.LinearGradient.Stop;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.scene.Container;
import at.bestsolution.wgraf.scene.Text;
import at.bestsolution.wgraf.style.Background;
import at.bestsolution.wgraf.style.CornerRadii;
import at.bestsolution.wgraf.style.FillBackground;
import at.bestsolution.wgraf.style.Font;
import at.bestsolution.wgraf.style.Insets;
import at.bestsolution.wgraf.transition.LinearDoubleTransition;

public class TabPane extends StackPane {

	
	private double tabHeight = 50;
	private double tabWidth = 100;
	
	private Container marker = new Container();
	private Map<Widget, Container> labelNodes = new HashMap<Widget, Container>();
	private Map<Widget, Text> labelNodesText = new HashMap<Widget, Text>();
	
	private Map<Widget, String> labels = new HashMap<Widget, String>();
	
	private List<Widget> tabs = new ArrayList<Widget>();
	
	public TabPane() {
		
		marker.parent().set(area);
		marker.x().setTransition(new LinearDoubleTransition(300));
		
		Background focus = new FillBackground(
				new LinearGradient(0, 0, 1, 1, CoordMode.OBJECT_BOUNDING, Spread.PAD,
						new Stop(0, new Color(225,0,0,150)),
						new Stop(0.4, new Color(255,30,30,150)),
						new Stop(1, new Color(255,30,30,150))
						),
						new CornerRadii(0), new Insets(0)
				);
		marker.background().set(focus);
	}
	
	public void setLabel(Widget w, String label) {
		labels.put(w, label);
		
		updateTabs();
	}
	
	private void updateTabs() {
		Font font = Font.UBUNTU;
		double maxW = 0;
		for (Widget w : tabs) {
			String label = labels.get(w);
			maxW = Math.max(maxW, font.stringExtent(label).x);
		}
		
		tabWidth = maxW;
		
		double pos = 0;
		
		for (final Widget w : tabs) {
			String label = labels.get(w);
			Container labelNode = labelNodes.get(w);
			if (labelNode == null) {
				labelNode = new Container();
				Text text = new Text();
				text.parent().set(labelNode);
				text.font().set(font);
				text.x().set(10);
				text.y().set(10);
				labelNode.parent().set(area);
				labelNodes.put(w, labelNode);
				labelNodesText.put(w, text);
				labelNode.acceptTapEvents().set(true);
				labelNode.onTap().registerSignalListener(new SignalListener<TapEvent>() {
					@Override
					public void onSignal(TapEvent data) {
						current().set(w);
						data.consume();
					}
				});
			}
			
			labelNode.x().set(pos);
			labelNode.width().set(20 + tabWidth);
			labelNode.height().set(tabHeight);
			labelNode.y().set(0);
			labelNodesText.get(w).text().set(label);
			
			pos += 20 + tabWidth;
		}
		
		marker.width().set(20 + tabWidth);
		marker.height().set(tabHeight);
		marker.y().set(0);
	}
	
	protected void updateChildWidth(double width) {
		System.err.println("update w");
		if (tabs == null) return;
		for (Widget w : tabs) {
			w.width().set(width);
		}
	}
	
	protected void updateChildHeight(double height) {
		System.err.println("update h");
		if (tabs == null) return;
		for (Widget w : tabs) {
			w.height().set(height - tabHeight);
		}
	}
	
	@Override
	protected void updateCurrent(Widget oldWidget, Widget newWidget)  {
		System.err.println("updateCurrent: " + labels.get(newWidget));
		double width = width().get();
		
		int oldIdx = tabs.indexOf(oldWidget);
		int newIdx = tabs.indexOf(newWidget);
		
		int dir;
		
		
		if (oldIdx <= newIdx) {
			dir = 1;
		}
		else {
			dir = -1;
		}
		
		// hide all others
		for (Widget w : tabs) {
			if (w != oldWidget && w != newWidget) {
				w.x().set(-width);
			}
		}
		
		
		if (oldWidget != null && tabs.contains(oldWidget)) {
			oldWidget.area.x().setTransition(new LinearDoubleTransition(300));
			oldWidget.area.x().setDynamic(dir * width);
		}
		
		if (newWidget != null && tabs.contains(newWidget)) {
			newWidget.area.y().set(tabHeight);
			newWidget.area.x().setTransition(new LinearDoubleTransition(300));
			newWidget.area.x().set(dir * -width);
			newWidget.area.x().setDynamic(0);
			
			int widgetIdx = tabs.indexOf(newWidget);
			marker.x().setDynamic((20 + tabWidth) * widgetIdx);
			
		}
	}
	
	public void addWidget(String label, Widget w) {
		super.addWidget(w);
		tabs.add(w);
		w.width().set(width().get());
		w.height().set(height().get() - tabHeight);
		setLabel(w, label);
	}
}
