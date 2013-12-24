package at.bestsolution.wgraf.backend.qt;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QApplication;

public class QtSync {

	public static class QSyncEvent extends QEvent {

		public final Runnable r;
		
		public QSyncEvent(Runnable r) {
			super(Type.User);
			this.r = r;
		}
		
	}
	
	public static class QSyncObject extends QObject {
		@Override
		protected void customEvent(QEvent e) {
			if (e instanceof QSyncEvent) {
				((QSyncEvent)e).r.run();
			}
		}
	}
	
	private static QSyncObject obj;
	
	public synchronized static void init() {
		obj = new QSyncObject();
	}
	
	public static void runLater(Runnable r) {
		if (obj != null) {
			QApplication.postEvent(obj, new QSyncEvent(r));
		}
		else {
			System.err.println("runlater obj is null!");
		}
	}
	
}
