package at.bestsolution.wgraf.backend.qt.scene;

import at.bestsolution.wgraf.events.FlingEvent;
import at.bestsolution.wgraf.events.ScrollEvent;
import at.bestsolution.wgraf.events.TapEvent;
import at.bestsolution.wgraf.properties.Signal;

public interface TapEventReceiver {
	Signal<TapEvent> onTap();
	Signal<ScrollEvent> onScroll();
	Signal<TapEvent> onLongTap();
	Signal<FlingEvent> onFling();
	
	void sendTap(TapEvent e);
	void sendLongTap(TapEvent e);
	void sendScroll(ScrollEvent e);
	void sendFling(FlingEvent e);
}
