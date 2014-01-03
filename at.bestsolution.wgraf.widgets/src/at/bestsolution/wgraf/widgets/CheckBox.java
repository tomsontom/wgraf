package at.bestsolution.wgraf.widgets;

import at.bestsolution.wgraf.events.ScrollEvent;
import at.bestsolution.wgraf.events.TapEvent;
import at.bestsolution.wgraf.geom.shape.Rectangle;
import at.bestsolution.wgraf.math.Vec2d;
import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.properties.ClampedDoubleIncrement;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.scene.Container;
import at.bestsolution.wgraf.scene.Text;
import at.bestsolution.wgraf.style.Backgrounds;
import at.bestsolution.wgraf.style.CornerRadii;
import at.bestsolution.wgraf.style.FillBackground;
import at.bestsolution.wgraf.style.Font;
import at.bestsolution.wgraf.style.Insets;
import at.bestsolution.wgraf.transition.LinearDoubleTransition;
import at.bestsolution.wgraf.transition.TouchScrollTransition;


// TODO snap to on / off
// TODO limit move to on / off
// TODO remove fixed sizes
public class CheckBox extends Widget {

	private Container slider;
	private Text onText;
	private Text offText;
	
	private final static String TEXT_ON = "on";
	private final static String TEXT_OFF = "off";
	
	private double minPos;
	private double maxPos;
	
	private boolean isSelected() {
		return slider.x().get() > -30;
	}
	
	private void setSelected(boolean value) {
		if (value) {
			slider.x().setDynamic(maxPos);
		}
		else {
			slider.x().setDynamic(minPos);
		}
	}
	
	
	public CheckBox() {
		
		maxPos = 0;
		minPos = -60;
		
		area.width().set(100d);
		area.height().set(40d);
		area.acceptFocus().set(true);
		area.acceptTapEvents().set(true);
		
		Rectangle clipRect = new Rectangle(2, 2, 96, 36, 10);
		
		area.clippingShape().set(clipRect);
		
		slider = new Container();
		slider.x().setTransition(new TouchScrollTransition());
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
		
		
		Font font = new Font("Sans", 20);
		
		Vec2d onExtent = font.stringExtent(TEXT_ON);
		Vec2d offExtent = font.stringExtent(TEXT_OFF);
		
		onText = new Text();
		onText.font().set(font);
		onText.text().set(TEXT_ON);
		onText.x().set(60/2d - onExtent.x/2);
		onText.y().set(40/2d - onExtent.y/2);
		onText.setParent(slider);
		
		onText.onTap().registerSignalListener(new SignalListener<TapEvent>() {
			@Override
			public void onSignal(TapEvent data) {
				System.err.println("on tap");
			}
		});
		
		offText = new Text();
		offText.font().set(font);
		offText.text().set(TEXT_OFF);
		offText.x().set(100 + 60/2d - offExtent.x/2);
		offText.y().set(40/2d - offExtent.y/2);
		
		offText.setParent(slider);
		
		area.onTap().registerSignalListener(new SignalListener<TapEvent>() {

			@Override
			public void onSignal(TapEvent data) {
				setSelected(!isSelected());
			}
		});
		
		area.onScroll().registerSignalListener(new SignalListener<ScrollEvent>() {
			@Override
			public void onSignal(ScrollEvent data) {
				slider.x().updateDynamic(new ClampedDoubleIncrement(-data.deltaX, minPos, maxPos));
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
