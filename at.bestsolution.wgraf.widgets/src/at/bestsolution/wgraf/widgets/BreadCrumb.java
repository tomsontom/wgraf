package at.bestsolution.wgraf.widgets;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import at.bestsolution.wgraf.Sync;
import at.bestsolution.wgraf.events.TapEvent;
import at.bestsolution.wgraf.math.Vec2d;
import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.paint.LinearGradient;
import at.bestsolution.wgraf.paint.Paint;
import at.bestsolution.wgraf.paint.LinearGradient.CoordMode;
import at.bestsolution.wgraf.paint.LinearGradient.Spread;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.Converter;
import at.bestsolution.wgraf.properties.InvalidValueException;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.ReadOnlyProperty;
import at.bestsolution.wgraf.properties.Signal;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.properties.binding.Binder;
import at.bestsolution.wgraf.properties.binding.Setter;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.properties.simple.SimpleSignal;
import at.bestsolution.wgraf.scene.Container;
import at.bestsolution.wgraf.scene.Text;
import at.bestsolution.wgraf.style.CornerRadii;
import at.bestsolution.wgraf.style.FillBackground;
import at.bestsolution.wgraf.style.Font;
import at.bestsolution.wgraf.style.FontAwesome;
import at.bestsolution.wgraf.transition.LinearDoubleTransition;

public class BreadCrumb<M> extends Widget {

	public static class Crumb<M> {
		private M m;

		public Crumb(M m) {
			this.m = m;

			area.onTap().registerSignalListener(new SignalListener<TapEvent>() {
				@Override
				public void onSignal(TapEvent data) {
					active.set(true);
					Sync.get().execLaterOnUIThread(new Runnable() {
						@Override
						public void run() {
							active.set(false);
							activated.signal(getModel());
						}
					}, 50);
				}
			});

		}

		protected final Container area = new Container();

		protected final Signal<M> activated = new SimpleSignal<M>();
		protected final Property<Boolean> active = new SimpleProperty<Boolean>(false);
		public M getModel() {
			return m;
		}
	}

	public static interface CrumbFactory<M> extends Converter<M, Crumb<M>> {}
	public static interface LabelProvider<M> extends Converter<M, String> {}
	public static interface IconProvider<M> extends Converter<M, String> {}

	public LabelProvider<M> labelProvider;
	public IconProvider<M> iconProvider;

	private CrumbFactory<M> crumbFactory = new DefaultCrumbFactory<M>();

	private Container titleArea;
	private Container hackyGradient;
	
	
	private static Color fontColor = new Color(100, 100, 100, 255);

	private static class DefaultCrumb<M> extends Crumb<M> {

		private Text lbl;
		private Text arrow;

		public DefaultCrumb(M m) {
			super(m);

			lbl = new Text();
			lbl.font().set(Font.UBUNTU.resize(22));
			lbl.text().set("" + m);
			lbl.parent().set(area);

			Vec2d ex = lbl.font().get().stringExtent("" + m);

			double w = ex.x;

			if( ! "Home".equals(m+"") ) {
				lbl.x().set(10);
				arrow = new Text();
				arrow.font().set(new Font(FontAwesome.FONTAWESOME, 20));
				arrow.text().set(FontAwesome.MAP.get("fa-angle-right"));
				arrow.parent().set(area);
				Vec2d exA = arrow.font().get().stringExtent(FontAwesome.MAP.get("fa-angle-right"));

				arrow.x().set(0);
				// TODO Auto-generated method stub

				arrow.y().set(ex.y/2-exA.y/2+3);
				area.width().set(w + 5 + exA.x + 5);
			} else {
				area.width().set(w + 5);
			}

			area.height().set(ex.y);

			//area.background().set(new FillBackground(Color.rgb(100, 100, 100), new CornerRadii(4), Insets.NO_INSETS));
			Binder.uniBind(active, new Setter<Boolean>() {
				@Override
				public void set(Boolean value) {
					if (value) {
						lbl.fill().set(new Color(255,30,30, 150));
						if( arrow != null ) {
							arrow.fill().set(new Color(255,30,30, 150));
						}
					}
					else {
						lbl.fill().set(fontColor);
						if( arrow != null ) {
							arrow.fill().set(fontColor);
						}
					}
				}
			});

		}
	}

	public static class DefaultCrumbFactory<M> implements CrumbFactory<M> {
		@Override
		public Crumb<M> convert(M value) {
			return new DefaultCrumb<M>(value);
		}
	}

	private Container backBtnArea;
	private Text backBtnLabel;

	private Map<M, Crumb<M>> crumbs = new HashMap<M, Crumb<M>>();
	private Stack<M> backStack = new Stack<M>();

	private Container crumbArea;

	private Text bigTitleIcon;
	private Text bigTitle;

	private Text titleIcon;
	private Text title;

	private Text titleIconM;
	private Text titleM;

	public BreadCrumb() {

		bigTitleIcon = new Text();
		bigTitleIcon.font().set(new Font(FontAwesome.FONTAWESOME, 60));
		bigTitleIcon.fill().set(new Color(0,122,255,30));
		bigTitleIcon.text().set(FontAwesome.MAP.get("fa-globe"));
		bigTitleIcon.y().set(-10);
		bigTitleIcon.x().set(0);
		//bigTitleIcon.parent().set(area);

		bigTitle = new Text();
		bigTitle.font().set(Font.UBUNTU.resize(60));
		bigTitle.fill().set(new Color(0,122,255,30));
		bigTitle.text().set("Title");
		bigTitle.y().set(-10);
		bigTitle.x().set(70);
		//bigTitle.parent().set(area);


		titleIcon = new Text();
		titleIcon.font().set(new Font(FontAwesome.FONTAWESOME, 30));
		titleIcon.text().set(FontAwesome.MAP.get("fa-globe"));
		titleIcon.fill().set(new Color(0,122,255,150));
		titleIcon.x().set(40);
		titleIcon.parent().set(area);

		bigTitleIcon.text().registerChangeListener(new ChangeListener<String>() {
			@Override
			public void onChange(String oldValue, String newValue) throws InvalidValueException {

				double xI = bigTitleIcon.font().get().stringExtent(newValue).x;

				bigTitle.x().set(xI + 10);
			}
		});



		title = new Text();
		title.font().set(Font.UBUNTU.resize(30));
		title.fill().set(new Color(0,122,255,150));
		title.text().set("Title");
		title.x().set(80);
		title.parent().set(area);

		final Setter<String> update = new Setter<String>() {
			@Override
			public void set(String value) {
				double x0 = title.font().get().stringExtent(title.text().get()).x;
				double x = titleIcon.font().get().stringExtent(titleIcon.text().get()).x;
				title.x().set(width().get() - 40 - x0);
				titleIcon.x().set(width().get() - 40 - x0 - 10 - x);
			}
		};
		Binder.uniBind(titleIcon.text(), update);
		Binder.uniBind(title.text(), update);

		
		hackyGradient = new Container();

		Paint hackyG = new LinearGradient(0, 0, 0, 1, CoordMode.OBJECT_BOUNDING, Spread.PAD,
				new LinearGradient.Stop(0, new Color(245,245,245,0)),
				new LinearGradient.Stop(0.8, new Color(245,245,245,255)),
				new LinearGradient.Stop(1, new Color(245,245,245,255))
				);
		
		hackyGradient.background().set(FillBackground.simple(hackyG, 0, 0));
		
//		Paint colorM = new LinearGradient(0, 0, 0, 1, CoordMode.OBJECT_BOUNDING, Spread.PAD,
//				new LinearGradient.Stop(0, new Color(190,190,190,0)),
//				new LinearGradient.Stop(0.5, new Color(190,190,190,0)),
//				new LinearGradient.Stop(1, new Color(50,50,50,60)));

		titleIconM = new Text();
		titleIconM.font().set(new Font(FontAwesome.FONTAWESOME, 30));
		titleIconM.text().set(FontAwesome.MAP.get("fa-globe"));
		titleIconM.fill().set(new Color(50,50,50,60));
		titleIconM.x().set(40);
		titleIconM.parent().set(area);
		titleIconM.mirror();
		titleIconM.y().set(40);
		titleM = new Text();
		titleM.font().set(Font.UBUNTU.resize(30));
		titleM.fill().set(new Color(50,50,50,60));
		titleM.text().set("Title");
		titleM.x().set(80);
		titleM.parent().set(area);
		titleM.mirror();
		titleM.y().set(40);

		hackyGradient.parent().set(area);
		hackyGradient.y().set(40);
		hackyGradient.height().set(30);
		
		final Setter<String> updateM = new Setter<String>() {
			@Override
			public void set(String value) {
				double x0 = titleM.font().get().stringExtent(titleM.text().get()).x;
				double x = titleIconM.font().get().stringExtent(titleIconM.text().get()).x;
				titleM.x().set(width().get() - 40 - x0);
				titleIconM.x().set(width().get() - 40 - x0 - 10 - x);
				hackyGradient.x().set(titleIconM.x().get());
				hackyGradient.width().set(x0 + x + 10);
				
			}
		};
		Binder.uniBind(titleIconM.text(), updateM);
		Binder.uniBind(titleM.text(), updateM);


		Binder.uniBind(width(), new Setter<Double>() {
			@Override
			public void set(Double value) {
				update.set(null);
				updateM.set(null);
			}
		});
		crumbArea = new Container();
		crumbArea.parent().set(area);
		crumbArea.x().set(40);
		crumbArea.y().set(27);

		backBtnArea = new Container();
		backBtnArea.parent().set(area);
		backBtnArea.y().set(37);
		backBtnArea.width().set(40);
		backBtnArea.height().set(40);

		backBtnLabel = new Text();
		backBtnLabel.font().set(new Font(FontAwesome.FONTAWESOME, 16));
		backBtnLabel.text().set(FontAwesome.MAP.get("fa-chevron-left"));
		backBtnLabel.fill().set(fontColor);

		Vec2d ex = backBtnLabel.font().get().stringExtent(FontAwesome.MAP.get("fa-chevron-left"));

		backBtnLabel.x().set(backBtnArea.width().get()/2 - ex.x/2);
		backBtnLabel.y().set(backBtnArea.height().get()/2 - ex.y/2 - 10);

		backBtnLabel.parent().set(backBtnArea);

		backBtnArea.onTap().registerSignalListener(new SignalListener<TapEvent>() {
			@Override
			public void onSignal(TapEvent data) {
				internalBackActivated();
			}
		});

		current.registerChangeListener(new ChangeListener<M>() {
			@Override
			public void onChange(M oldValue, M newValue) throws InvalidValueException {
				// update the crumbs
				layoutCrumbs();

				// update the title

				String label = labelProvider != null ? labelProvider.convert(newValue) : newValue.toString();
				String icon = iconProvider != null ? iconProvider.convert(newValue) : null;

 				title.text().set(label);
				bigTitle.text().set(label);

				titleM.text().set(label);


				if (icon != null) {
					titleIcon.text().set(icon);
					bigTitleIcon.text().set(icon);

					titleIconM.text().set(icon);

				}


			}
		});

		crumbActivated().registerSignalListener(new SignalListener<M>() {
			@Override
			public void onSignal(M data) {
				crumbActivated(data);
			}
		});
	}

	private void crumbActivated(M model) {
		M cur = null;
		int delay = 0;
		while (!backStack.isEmpty()) {
			cur = backStack.pop();
			if (delay > 0) {
				final M rem = cur;
				Sync.get().execLaterOnUIThread(new Runnable() {
					@Override
					public void run() {
						removeCrumb(rem);
					}
				}, delay);;
			}
			else {
				removeCrumb(cur);
			}
			if (cur == model) {
				break;
			}
			delay += 150;
		}
		current.set(model);

	}

	private void removeCrumb(M model) {
		System.err.println("REMOVING " + model);
		final Crumb<M> c = crumbs.remove(model);
		c.area.y().setTransition(new LinearDoubleTransition(200));
		c.area.y().setDynamic(-100);
		c.area.y().transitionActive().registerChangeListener(new ChangeListener<Boolean>() {
			@Override
			public void onChange(Boolean oldValue, Boolean newValue) throws InvalidValueException {
				if (newValue == false) {
					// drop it
					c.area.parent().set(null);
				}
			}
		});
	}

	private void addCrumbToHistory(M model) {
		System.err.println("ADDING CRUMB TO HISTORY: " + model);
		backStack.push(model);
		Crumb<M> c = crumbFactory.convert(model);
		c.area.x().set(area.width().get());
		c.area.parent().set(crumbArea);
		c.activated.registerSignalListener(new SignalListener<M>() {
			@Override
			public void onSignal(M data) {
				crumbActivated.signal(data);
			}
		});
		crumbs.put(model, c);
		layoutCrumbs();
	}

	private void internalBackActivated() {
		if (!backStack.isEmpty()) {
			M last = backStack.pop();
			removeCrumb(last);
			current.set(last);
			backActivated().signal(last);
		}
	}


	public void addCrumb(M model) {
		if (current.get() != null) {
			addCrumbToHistory(current.get());
		}
		current.set(model);
	}

	public void backTo(M model) {
		if (backStack.contains(model)) {
			crumbActivated().signal(model);
		}
	}

	private void layoutCrumbs() {
		Iterator<M> it = backStack.iterator();
		double x = 0;
		while (it.hasNext()) {
			final M m = it.next();
			final Crumb<M> c = crumbs.get(m);
			c.area.x().setTransition(new LinearDoubleTransition(300));
			c.area.x().setDynamic(x);
			x += c.area.width().get();
		}
	}

	private Property<M> current = new SimpleProperty<M>();
	public ReadOnlyProperty<M> current() {
		return current;
	}

	private Signal<M> crumbActivated = new SimpleSignal<M>();
	public Signal<M> crumbActivated() {
		return crumbActivated;
	}

	private Signal<M> backActivated = new SimpleSignal<M>();
	public Signal<M> backActivated() {
		return backActivated;
	}
}
