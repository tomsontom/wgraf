package at.bestsolution.wgraf;

import java.util.Timer;
import java.util.TimerTask;

public abstract class Sync {

	public static interface PulseListener {
		void onPulse();
	}
	
	public abstract void registerPulseListener(PulseListener listener);
	public abstract void unregisterPulseListener(PulseListener listener);
	
	private Timer timer = new Timer("runLater-Timer");
	
	private static Sync INSTANCE;
	
	public static Sync get() {
		if (INSTANCE == null) {
			INSTANCE = BackendFactory.get().create(Sync.class);
		}
		return INSTANCE;
	}
	
	
	/**
	 * executes the given runnable on the ui thread.
	 * @param runnable
	 */
	public abstract void syncExecOnUIThread(Runnable runnable);
	
	
	/**
	 * asynchronously executes the given runnable on the ui thread.
	 * @param runnable
	 */
	public abstract void asyncExecOnUIThread(Runnable runnable);
	
	
	public void execLaterOnUIThread(final Runnable runnable, long timeout) {
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				asyncExecOnUIThread(runnable);
			}
		}, timeout);
	}
}
