package at.bestsolution.wgraf.backend.javafx;

import javafx.application.Platform;
import at.bestsolution.wgraf.Sync;

public class JavaFxSync extends Sync {

	@Override
	public void syncExecOnUIThread(Runnable runnable) {
		if (Platform.isFxApplicationThread()) {
			runnable.run();
		}
		else {
			Platform.runLater(runnable);
		}
	}

	@Override
	public void asyncExecOnUIThread(Runnable runnable) {
		Platform.runLater(runnable);
	}

}
