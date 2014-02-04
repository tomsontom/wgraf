package at.bestsolution.wgraf.test.ui;

import at.bestsolution.wgraf.Application;
import at.bestsolution.wgraf.math.Vec2d;
import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.scene.Container;
import at.bestsolution.wgraf.scene.Text;
import at.bestsolution.wgraf.style.CornerRadii;
import at.bestsolution.wgraf.style.FillBackground;
import at.bestsolution.wgraf.style.Font;
import at.bestsolution.wgraf.style.Insets;

public class TextNode extends Application {

	@Override
	protected void initialize() {
		
		Container region = new Container();
		
		region.width().set(300.0);
		region.height().set(300.0);
		
		String text0 = "JxYzF";
		String text1 = "Lorem";
		
		String text2 = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
		
		{
			Font font0 = Font.UBUNTU.resize(60);
		
			{
				Vec2d stringExtent = font0.stringExtent(text0);
				
				System.err.println("text width: " + stringExtent.x);
				
				Container r = new Container();
				r.x().set(10.0);
				r.y().set(10.0);
				
				r.width().set(stringExtent.x);
				r.height().set(stringExtent.y);
				
				
				r.parent().set(region);
				r.background().set(new FillBackground(new Color(255, 0, 0, 50), new CornerRadii(0), new Insets(0, 0, 0, 0)));
				
				Text text = new Text();
				text.font().set(font0);
				
				text.x().set(10.0);
				text.y().set(10.0);
				
				text.text().set(text0);
				
				text.parent().set(region);
				
			}
			
			{
				Vec2d stringExtent = font0.stringExtent(text1);
				
				Container r = new Container();
				r.x().set(10.0);
				r.y().set(150.0);
				
				r.width().set(stringExtent.x);
				r.height().set(stringExtent.y);
				
				
				r.parent().set(region);
				r.background().set(new FillBackground(new Color(255, 0, 0, 50), new CornerRadii(0), new Insets(0, 0, 0, 0)));
				
				Text text = new Text();
				
				text.font().set(font0);
//				text.fontSize().set(20.0);
				
				text.x().set(10.0);
				text.y().set(150.0);
				
				text.text().set(text1);
				
				text.parent().set(region);
				
			}
		}
		
		
		root().set(region);
		
	}
		
	public static void main(String[] args) {
		new TextNode().start(args);
	}
}
