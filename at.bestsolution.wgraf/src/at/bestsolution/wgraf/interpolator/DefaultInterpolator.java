package at.bestsolution.wgraf.interpolator;

import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArraySet;

import at.bestsolution.wgraf.transition.Transition;

public class DefaultInterpolator implements Interpolator {
	
	private Set<Transition<?>> activeTransistions = new CopyOnWriteArraySet<Transition<?>>();
	
	private Timer interpol = new Timer();
	
	public DefaultInterpolator() {
		startInterpolator();
	}
	
	private void startInterpolator() {
		interpol.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				step();
			}
		}, 0, 10);
	}
	
	private long lastStepTime = -1;
	
	private void step() {
		final long now = System.currentTimeMillis();
		final long time = lastStepTime == -1 ? 0 : now -lastStepTime;
		for (Transition<?> t : activeTransistions) {
			if (t.update(time)) {
				activeTransistions.remove(t);
			}
		}
		
		lastStepTime = now;
	}
	
	@Override
	public void interpolate(Transition<?> transition) {
		activeTransistions.add(transition);
	}
	
	
	
}
