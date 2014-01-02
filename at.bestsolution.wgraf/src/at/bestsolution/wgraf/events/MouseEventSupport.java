package at.bestsolution.wgraf.events;

import java.util.Timer;
import java.util.TimerTask;

import at.bestsolution.wgraf.properties.Signal;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.properties.simple.SimpleSignal;

public class MouseEventSupport {

	private boolean debug = false;
	
	
	private final static int TAP_TIMEOUT = 500;
	private final static int LONG_TAP_TIMEOUT = 1000;
	private final static int TAP_RADIUS = 20;
	
	Timer t = new Timer();
	private VelocityTracker tracker = new VelocityTracker();
	
	private long beginTime;
	private MouseCoords beginEvent;
	private boolean withinTapBounds = false;
	private boolean isLongTap = false;
	private boolean tapDone = false;
	
	private MouseCoords moveEvent;
	private ScrollLock moveLock;
	
	public MouseEventSupport() {
		// wire events
		mousePressed().registerSignalListener(new SignalListener<MouseEventSupport.MouseCoords>() {
			@Override
			public void onSignal(MouseCoords event) {
				final long now = System.currentTimeMillis();
				
				beginEvent = event;
				withinTapBounds = true;
				isLongTap = false;
				tapDone = false;
				
				tracker.addMovement(now, (float) event.x, (float) event.y);
				
				scheduleLongTapCheck(beginEvent);
			}
		});
		
		mouseReleased().registerSignalListener(new SignalListener<MouseEventSupport.MouseCoords>() {
			@Override
			public void onSignal(MouseCoords e) {
				final long now = System.currentTimeMillis();
				
				beginEvent = e;
				if (beginEvent != null) {
					tracker.addMovement(now, (float)e.x, (float)e.y);
					
					if (isWithinTapBounds(e) && !isLongTap) {
						tapDone = true;
						emitTap(e);
					}
					else {
						// fling
						tracker.computeCurrentVelocity(1000);
						float velocityX = tracker.getXVelocity(), velocityY = tracker.getYVelocity();
						
						emitTFling(e, velocityX, velocityY);
					}
					if (beginEvent != null) {
						MouseCoords eBegin = beginEvent;
						beginEvent = null;
						moveEvent = null;
//						emitTapEnd(createTapEvent(eBegin), createTapEvent(e));
					}
				}
			}
			
		});
		
		mouseDragged().registerSignalListener(new SignalListener<MouseEventSupport.MouseCoords>() {

			@Override
			public void onSignal(MouseCoords e) {
				final long now = System.currentTimeMillis();
				
				if (beginEvent != null) {
					
					tracker.addMovement(now, (float)e.x, (float)e.y);
					
					if (!isWithinTapBounds(e)) {
//						TapEvent moveEv = createTapEvent(e);
//						moveEv.scrollLock = moveLock;
						emitTScroll(e, moveLock, calcX(e), calcY(e));
					}
					moveEvent = e;
				}
			}
		});
	}
	
	private double calcDistance(MouseCoords from, MouseCoords to) {
		final double deltaX = from.x - to.x;
		final double deltaY = from.y - to.y;
		return Math.sqrt(deltaX*deltaX + deltaY*deltaY);
	}
	
	private ScrollLock calcScrollLock(MouseCoords from, MouseCoords to) {
		final double x = from.x - to.x;
		final double y = from.y - to.y;
		
		final double way = Math.sqrt(x*x + y*y);
		
		final double a = Math.acos(x / way) * 180 / Math.PI;
			
		if (a < 22.5 || a > 157.5) {
			return ScrollLock.HORIZONTAL;
		}
		else if ( a > 67.5 && a < 112.5) {
			return ScrollLock.VERTICAL;
		}
		else {
			return ScrollLock.BOTH;
		}
	}
	
	private boolean isWithinTapBounds(MouseCoords e) {
		if (withinTapBounds) {
			withinTapBounds = calcDistance(beginEvent, e) < TAP_RADIUS;
			if (!withinTapBounds) {
				moveLock = calcScrollLock(beginEvent, e);
			}
		}
		return withinTapBounds;
	}
	
	private double calcX(MouseCoords e) {
		return (moveEvent != null ? moveEvent : beginEvent).x - e.x;
	}

	private double calcY(MouseCoords e) {
		return (moveEvent != null ? moveEvent : beginEvent).y - e.y;
	}
	
	private void scheduleLongTapCheck(final MouseCoords event) {
		t.schedule(new TimerTask() {
			@Override
			public void run() {
//				Platform.runLater(new Runnable() {
//					
//					@Override
//					public void run() {
						if (beginEvent != event) return;
						if (!withinTapBounds) return;
						if (tapDone) return;
						
						final MouseCoords last = moveEvent!=null?moveEvent:beginEvent;
						
						if (last != null) {
//							TapEvent end = new TapEvent(last);
							isLongTap = true;
							emitLongTap(event);
						}
//					}
//				});
				
			}
		}, 5 + LONG_TAP_TIMEOUT);
		
	}
	
	private void emitTap(MouseCoords e) {
		if (debug) System.err.println("emitTap");
		tap().signal(new TapEvent(e.x, e.y));
	}
	
	private void emitLongTap(MouseCoords e) {
		if (debug) System.err.println("emitLongTap");
		longTap().signal(new TapEvent(e.x, e.y));
	}
	
	private void emitTScroll(MouseCoords e, ScrollLock l, double x, double y) {
		if (debug) System.err.println("emitTScroll " + l + ", " + x + ", " + y);
		scroll().signal(new ScrollEvent(e.x, e.x, l, x, y));
		
//		if (getOnTScroll() != null) {
//			getOnTScroll().handle(new TScrollEvent(e, l, x, y));
//		}
	}
	
	private void emitTFling(MouseCoords e, float velocityX, float velocityY) {
		if (debug) System.err.println("emitTFling " + velocityX + ", " + velocityY);
		fling().signal(new FlingEvent());
//		if (getOnTFling() != null) {
//			getOnTFling().handle(new TFlingEvent(e, velocityX, velocityY));
//		}
	}
	
	

	
	public static class MouseCoords {
		public final double x;
		public final double y;
		public MouseCoords(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}
	
	private Signal<MouseCoords> mousePressed = null;
	public Signal<MouseCoords> mousePressed() {
		if (mousePressed == null) {
			mousePressed = new SimpleSignal<MouseEventSupport.MouseCoords>();
		}
		return mousePressed;
	}
	
	private Signal<MouseCoords> mouseReleased = null;
	public Signal<MouseCoords> mouseReleased() {
		if (mouseReleased == null) {
			mouseReleased = new SimpleSignal<MouseEventSupport.MouseCoords>();
		}
		return mouseReleased;
	}
	
	private Signal<MouseCoords> mouseDragged = null;
	public Signal<MouseCoords> mouseDragged() {
		if (mouseDragged == null) {
			mouseDragged = new SimpleSignal<MouseEventSupport.MouseCoords>();
		}
		return mouseDragged;
	}
	
	private Signal<TapEvent> tap = null;
	public Signal<TapEvent> tap() {
		if (tap == null) {
			tap = new SimpleSignal<TapEvent>();
		}
		return tap;
	}
	
	private Signal<TapEvent> longTap = null;
	public Signal<TapEvent> longTap() {
		if (longTap == null) {
			longTap = new SimpleSignal<TapEvent>();
		}
		return longTap;
	}
	
	private Signal<ScrollEvent> scroll = null;
	public Signal<ScrollEvent> scroll() {
		if (scroll == null) {
			scroll = new SimpleSignal<ScrollEvent>();
		}
		return scroll;
	}
	
	private Signal<FlingEvent> fling = null;
	public Signal<FlingEvent> fling() {
		if (fling == null) {
			fling = new SimpleSignal<FlingEvent>();
		}
		return fling;
	}
}
