package at.bestsolution.wgraf.backend.qt;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import at.bestsolution.wgraf.Sync;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QTimer;
import com.trolltech.qt.gui.QApplication;

public class QtSync extends Sync {

	
	@Override
	public void registerPulseListener(PulseListener listener) {
		obj.pulseListeners.add(listener);
	}
	
	@Override
	public void unregisterPulseListener(PulseListener listener) {
		obj.pulseListeners.remove(listener);
	}
	
	static QTimer timer;
	
	public static class QSyncEvent extends QEvent {

		public final Runnable r;
		
		public QSyncEvent(Runnable r) {
			super(Type.User);
			this.r = r;
		}
		
	}
	
	public static class QSyncObject extends QObject {
		
		public List<PulseListener> pulseListeners = new CopyOnWriteArrayList<PulseListener>();
		
		@Override
		protected void customEvent(QEvent e) {
			if (e instanceof QSyncEvent) {
				((QSyncEvent)e).r.run();
			}
		}
		
		public void onPulse() {
			for (PulseListener p : pulseListeners) {
				p.onPulse();
			}
		}
	}
	
	private static QSyncObject obj;
	
	public synchronized static void init() {
		obj = new QSyncObject();
		
//		timer = new QTimer(obj);
//		
//		timer.timeout.connect(obj, "onPulse()");
//		
//		timer.setInterval(35);
//		timer.start();
	}
	
	public static void runLater(Runnable r) {
		if (obj != null) {
			QApplication.postEvent(obj, new QSyncEvent(r));
		}
		else {
			System.err.println("runlater obj is null! QtSync needs to be initialized!");
		}
	}
	
	@Override
	public void syncExecOnUIThread(final Runnable runnable) {
		if (QApplication.instance().thread() == Thread.currentThread()) {
			runnable.run();
		}
		else {
//			final Object w = new Object();
			runLater(new Runnable() {
				@Override
				public void run() {
					runnable.run();
//					w.notify();
				}
			});
//			try {
//				w.wait();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
	}
	
	@Override
	public void asyncExecOnUIThread(Runnable runnable) {
		runLater(runnable);
	}
	
	
	
}
