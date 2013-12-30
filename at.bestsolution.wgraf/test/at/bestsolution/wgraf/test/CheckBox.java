package at.bestsolution.wgraf.test;

import at.bestsolution.wgraf.events.ScrollEvent;
import at.bestsolution.wgraf.events.TapEvent;
import at.bestsolution.wgraf.math.Vec2d;
import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.scene.Container;
import at.bestsolution.wgraf.scene.Text;
import at.bestsolution.wgraf.style.Backgrounds;
import at.bestsolution.wgraf.style.CornerRadii;
import at.bestsolution.wgraf.style.FillBackground;
import at.bestsolution.wgraf.style.Font;
import at.bestsolution.wgraf.style.Insets;
import at.bestsolution.wgraf.transition.LinearDoubleTransition;

public class CheckBox {

	private Container area;
	private Container slider;
	private Text onText;
	private Text offText;
	
	private boolean isSelected() {
		return slider.x().get() > -30;
	}
	
	private void setSelected(boolean value) {
		if (value) {
			slider.x().set(0d);
		}
		else {
			slider.x().set(-60d);
		}
	}
	
	
	public CheckBox() {
		
		area = new Container();
		area.width().set(100d);
		area.height().set(40d);
		
		slider = new Container();
		slider.x().setTransition(new LinearDoubleTransition(50));
		slider.setParent(area);
		slider.width().set(160d);
		slider.height().set(40d);
		slider.background().set(new Backgrounds(
				// slider
				new FillBackground(new Color(125, 125, 125, 255), new CornerRadii(10d, 10d, 10d, 10d, 10d, 10d, 10d, 10d), new Insets(2, 60, 2, 60)),
				// on color
				new FillBackground(new Color(0, 255, 0, 100), new CornerRadii(10d, 10d, 0d, 0d, 10d, 10d, 0d, 0d), new Insets(2, 90, 2, 2)),
				// off color
				new FillBackground(new Color(255, 0, 0, 100), new CornerRadii(0d, 0d, 10d, 10d, 0d, 0d, 10d, 10d), new Insets(2, 2, 2, 90))
				
				));
		
		onText = new Text();
		onText.text().set("on");
		onText.x().set(60/2d);
		onText.y().set(40/2d);
		
		onText.setParent(slider);
		
		offText = new Text();
		offText.text().set("off");
		offText.x().set(100 + 60/2d);
		offText.y().set(40/2d);
		
		offText.setParent(slider);
		
		
		slider.onTap().registerSignalListner(new SignalListener<TapEvent>() {

			@Override
			public void onSignal(TapEvent data) {
				System.err.println("slider ontap");
				setSelected(!isSelected());
			}
		});
		
		area.onScroll().registerSignalListner(new SignalListener<ScrollEvent>() {
			@Override
			public void onSignal(ScrollEvent data) {
				slider.x().setWithoutTransition(slider.x().get() - data.deltaX);
			}
		});
	}
	
	public void setX(double x) {
		area.x().set(x);
	}
	
	public void setY(double y) {
		area.x().set(y);
	}
	
	public void setParent(Container parent) {
		area.setParent(parent);
	}
	
	
}
