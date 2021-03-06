package at.bestsolution.wgraf.widgets;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

import at.bestsolution.wgraf.Application;
import at.bestsolution.wgraf.events.TapEvent;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.properties.binding.Binder;
import at.bestsolution.wgraf.style.Font;

public class StackTest extends Application {

	@Override
	protected void initialize() {
		title().set("Hallo StackPane");
		
		final PopupPane rootPane = new PopupPane();
		rootPane.width().set(800);
		rootPane.height().set(600);
		
		final StackPane stack = new StackPane();
		Binder.uniBind(rootPane.width(), stack.width());
		Binder.uniBind(rootPane.height(), stack.height());
		
		rootPane.addWidget(stack);
		
		
		
		final ListView<String> listA = new ListView<String>();
		for (int i = 0; i < 100; i ++)
			listA.model().add("List A " + i);
		
		stack.addWidget(listA);
		
		final ListView<String> listB = new ListView<String>();
		for (int i = 0; i < 100; i ++)
			listB.model().add("List B " + i);
		
		stack.addWidget(listB);
		
		stack.current().set(listA);
		
		root().set(rootPane.getAreaNode());
		
		Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask() {
			private Widget next = listB;
			@Override
			public void run() {
				stack.current().set(next);
				
				if (next == listA) {
					next = listB;
				}
				else {
					next = listA;
				}
			}
		}, 1000, 1000);
	}
	
	
	public static void main(String[] args) {
		StackTest app = new StackTest();
		app.start(args);
	}
}
