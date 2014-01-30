package at.bestsolution.wgraf.widgets;

import at.bestsolution.wgraf.events.ScrollEvent;
import at.bestsolution.wgraf.events.ScrollLock;
import at.bestsolution.wgraf.events.TapEvent;
import at.bestsolution.wgraf.geom.shape.Rectangle;
import at.bestsolution.wgraf.math.Vec2d;
import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.paint.LinearGradient;
import at.bestsolution.wgraf.paint.LinearGradient.CoordMode;
import at.bestsolution.wgraf.paint.LinearGradient.Spread;
import at.bestsolution.wgraf.paint.LinearGradient.Stop;
import at.bestsolution.wgraf.properties.ClampedDoubleIncrement;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.ReadOnlyProperty;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.properties.binding.Binder;
import at.bestsolution.wgraf.properties.binding.Setter;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.scene.Container;
import at.bestsolution.wgraf.scene.Text;
import at.bestsolution.wgraf.style.Border;
import at.bestsolution.wgraf.style.BorderStroke;
import at.bestsolution.wgraf.style.BorderWidths;
import at.bestsolution.wgraf.style.CornerRadii;
import at.bestsolution.wgraf.style.FillBackground;
import at.bestsolution.wgraf.style.Font;
import at.bestsolution.wgraf.style.Insets;
import at.bestsolution.wgraf.transition.TouchScrollTransition;


// TODO snap to on / off -> this needs a scroll-end-event
// TODO remove fixed sizes
// TODO fix selected model
public class CheckBox extends Widget {

	private Container slider;
	private Text onText;
	private Text offText;
	
	private final static String TEXT_ON = "on";
	private final static String TEXT_OFF = "off";
	
	private double minPos;
	private double maxPos;
	
	private boolean isSelected() {
		return slider.x().get() > (maxPos - minPos) / 2d;
	}
	
	private void setSelected(boolean value) {
		if (value) {
			slider.x().setDynamic(maxPos);
		}
		else {
			slider.x().setDynamic(minPos);
		}
		selected.set(value);
	}
	
	private Property<Boolean> selected = new SimpleProperty<Boolean>(true);
	
	public Property<Boolean> selected() {
		return selected;
	}
	
	public CheckBox() {
		
		maxPos = 56;
		minPos = 2;
		
		area.width().set(100d);
		area.height().set(40d);
		area.acceptFocus().set(true);
		area.acceptTapEvents().set(true);
		
		area.cache().set(true);
		
		Rectangle clipRect = new Rectangle(2, 2, 96, 36, 0);
		
		final Container sliderClip = new Container();
		sliderClip.parent().set(area);
		sliderClip.clippingShape().set(clipRect);
		sliderClip.width().set(100d);
		sliderClip.height().set(40d);
		sliderClip.cache().set(true);
		
		slider = new Container();
		slider.cache().set(true);
		slider.x().setTransition(new TouchScrollTransition());
		slider.parent().set(sliderClip);
		slider.width().set(40d);
		slider.height().set(40d);
		
		// init state
		slider.x().set(maxPos);
		
		Font font = new Font("Sans", 20);
		
		Vec2d onExtent = font.stringExtent(TEXT_ON);
		Vec2d offExtent = font.stringExtent(TEXT_OFF);
		
		onText = new Text();
		onText.cache().set(true);
		onText.font().set(font);
		onText.text().set(TEXT_ON);
		onText.x().set(-60/2d - onExtent.x/2);
		onText.y().set(40/2d - onExtent.y/2);
		onText.parent().set(slider);
		
		onText.onTap().registerSignalListener(new SignalListener<TapEvent>() {
			@Override
			public void onSignal(TapEvent data) {
				System.err.println("on tap");
			}
		});
		
		offText = new Text();
		offText.cache().set(true);
		offText.font().set(font);
		offText.text().set(TEXT_OFF);
		offText.x().set(40 + 60/2d - offExtent.x/2);
		offText.y().set(40/2d - offExtent.y/2);
		
		offText.parent().set(slider);
		
		area.onTap().registerSignalListener(new SignalListener<TapEvent>() {

			@Override
			public void onSignal(TapEvent data) {
				setSelected(!isSelected());
			}
		});
		
		area.onScroll().registerSignalListener(new SignalListener<ScrollEvent>() {
			@Override
			public void onSignal(ScrollEvent data) {
				if (data.scrollLock == ScrollLock.HORIZONTAL) {
					slider.x().updateDynamic(new ClampedDoubleIncrement(-data.deltaX, minPos, maxPos));
					// we consume horizontal scroll events
					data.consume();
				}
			}
		});
		
		initDefaultStyle();
	}
	
	private void initDefaultStyle() {
		
		Insets bgInsets = new Insets(0, 0, 0, 0);
		final FillBackground off = new FillBackground(
				new LinearGradient(0, 0, 0, 1, CoordMode.OBJECT_BOUNDING, Spread.PAD, 
					new Stop(0, new Color(255, 180, 180, 255)),
					new Stop(1, new Color(255, 230, 230, 255))
				), 
				new CornerRadii(4), bgInsets);
		
		final FillBackground on = new FillBackground(
				new LinearGradient(0, 0, 0, 1, CoordMode.OBJECT_BOUNDING, Spread.PAD, 
					new Stop(0, new Color(180, 240, 180, 255)),
					new Stop(1, new Color(230, 255, 230, 255))
				), 
				new CornerRadii(4), bgInsets);
		
		
		area.border().set(new Border(new BorderStroke( new Color(200, 200, 200, 255), new CornerRadii(4), new BorderWidths(1, 1, 1, 1), bgInsets)));
		
		// this should come from css:
		Binder.uniBind(selected, new Setter<Boolean>() {
			@Override
			public void set(Boolean value) {
				setSelected(value);
				area.background().set(value ? on : off);
			}
		});

		final FillBackground sliderBg = new FillBackground(
				new LinearGradient(0, 0, 0, 1, CoordMode.OBJECT_BOUNDING, Spread.PAD, 
					new Stop(0, new Color(200, 200, 200, 255)),
					new Stop(1, new Color(124, 124, 124, 255))
				), 
				new CornerRadii(6), bgInsets);
		
		slider.background().set(sliderBg);
		Color lineA = new Color(0, 0, 0, 50);
		Color lineB = new Color(255, 255, 255, 50);
		
		double w = 40;
		double lineSpace = 5;
		double btop = 8;
		double bbottom = 8;
		double bleft = 4;
		slider.border().set(new Border(
				new BorderStroke(lineA, new CornerRadii(0), new BorderWidths(1, 1, 1, 1), new Insets(btop-1, w - bleft - 0 * lineSpace - 1, bbottom, bleft + 0*lineSpace)),
				new BorderStroke(lineA, new CornerRadii(0), new BorderWidths(1, 1, 1, 1), new Insets(btop-1, w - bleft - 1 * lineSpace - 1, bbottom, bleft + 1*lineSpace)),
				new BorderStroke(lineA, new CornerRadii(0), new BorderWidths(1, 1, 1, 1), new Insets(btop-1, w - bleft - 2 * lineSpace - 1, bbottom, bleft + 2*lineSpace)),
//				new BorderStroke(lineA, new CornerRadii(0), new BorderWidths(1, 1, 1, 1), new Insets(btop, w - bleft - 3 * lineSpace -1, bbottom, b + 3*lineSpace)),
				new BorderStroke(lineB, new CornerRadii(1), new BorderWidths(1, 1, 1, 1), new Insets(btop, w - bleft - 0 * lineSpace - 1 - 1, bbottom-1, bleft + 0*lineSpace + 1)),
				new BorderStroke(lineB, new CornerRadii(1), new BorderWidths(1, 1, 1, 1), new Insets(btop, w - bleft - 1 * lineSpace - 1  - 1, bbottom-1, bleft + 1*lineSpace + 1)),
				new BorderStroke(lineB, new CornerRadii(1), new BorderWidths(1, 1, 1, 1), new Insets(btop, w - bleft - 2 * lineSpace - 1  - 1, bbottom-1, bleft + 2*lineSpace + 1))
//				new BorderStroke(lineB, new CornerRadii(0), new BorderWidths(1, 1, 1, 1), new Insets(btop, w - bleft - 3 * lineSpace -1 - 1, bbottom-1, bleft + 3*lineSpace + 1))
				
				));
//		slider.effect().set(new DropShadow());
//		focus().registerChangeListener(new ChangeListener<Boolean>() {
//			@Override
//			public void onChange(Boolean oldValue, Boolean newValue) {
//				if (newValue) {
//					area.background().set(new Backgrounds(
//							new FillBackground(new Color(255, 255, 255, 255), new CornerRadii(10), new Insets(2, 2, 2, 2)),
//							new FillBackground(new Color(255, 255, 0, 144), new CornerRadii(10), new Insets(0, 0, 0, 0))
//							));
//				}
//				else {
//					area.background().set(null);
//
//				}
//			}
//		});
	}
	
	public void setX(double x) {
		area.x().set(x);
	}
	
	public void setY(double y) {
		area.x().set(y);
	}
	
	public ReadOnlyProperty<Boolean> focus() {
		return area.focus();
	}
}
