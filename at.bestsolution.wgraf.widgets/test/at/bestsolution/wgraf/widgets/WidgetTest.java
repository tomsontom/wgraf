package at.bestsolution.wgraf.widgets;

import at.bestsolution.wgraf.Application;
import at.bestsolution.wgraf.style.Font;

public class WidgetTest extends Application {

	@Override
	protected void initialize() {
		
		title().set("Hallo Widgets");
		
		
		Label test = new Label();
		test.text().set("Label");
		test.font().set(new Font("Times New Roman", 22));
		root().set(test.getAreaNode());
		
		
		
	}
	
	
	public static void main(String[] args) {
		WidgetTest app = new WidgetTest();
		app.start(args);
	}
}
