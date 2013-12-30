package at.bestsolution.wgraf.widgets;

import at.bestsolution.wgraf.Application;

public class WidgetTest extends Application {

	@Override
	protected void initialize() {
		
		title().set("Hallo Widgets");
		
		
		Label test = new Label();
		test.text().set("Label");
		
		root().set(test.getAreaNode());
		
		
		
	}
	
	
	public static void main(String[] args) {
		WidgetTest app = new WidgetTest();
		app.start(args);
	}
}
