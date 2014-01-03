package at.bestsolution.wgraf.widgets;

import at.bestsolution.wgraf.events.KeyEvent;
import at.bestsolution.wgraf.events.TapEvent;
import at.bestsolution.wgraf.math.Vec2d;
import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.properties.Binder;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.SignalListener;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.scene.Container;
import at.bestsolution.wgraf.style.Backgrounds;
import at.bestsolution.wgraf.style.CornerRadii;
import at.bestsolution.wgraf.style.FillBackground;
import at.bestsolution.wgraf.style.Font;
import at.bestsolution.wgraf.style.Insets;

public class Text extends Widget {
	
	protected final at.bestsolution.wgraf.scene.Text nodeText;
	protected final Container nodeCursor;
	
	private final Property<Integer> cursorIndex = new SimpleProperty<Integer>();
	
	private static enum Cmp {
		LESSER,
		GREATER,
		EQUAL
	}
	
	private static class Letter {
		public int index;
		public String letter;
		public double beginX;
		public double endX;
		public Cmp cmp;
	}
	
	private static Letter getLetter(Font font, String text, int index) {
		final Letter result = new Letter();
		result.index = index;
		
		final String str0 = text.substring(0, index);
		final String str1 = text.substring(0, Math.min(text.length(), index+1));
		
		result.beginX = font.stringExtent(str0).x;
		result.endX = font.stringExtent(str1).x;
		result.cmp = Cmp.EQUAL;
		return result;
	}
	
	private static Letter compare(Font font, String text, int letterIdx, double xPos) {
		final Letter result = new Letter();
		result.index = letterIdx;
		
		final String str0 = text.substring(0, letterIdx);
		final String str1 = text.substring(0, Math.min(text.length(), letterIdx+1));
		
		result.beginX = font.stringExtent(str0).x;
		result.endX = font.stringExtent(str1).x;
		
		if (xPos < result.beginX) {
			result.cmp = Cmp.LESSER;
			return result;
		}
		else if (xPos > result.endX && text.length() > letterIdx + 1) {
			result.cmp = Cmp.GREATER;
			return result;
		}
		else {
			result.letter = text.substring(letterIdx, letterIdx + 1);
			result.cmp = Cmp.EQUAL;
			return result;
		}
		
	}
	
	private static Letter binSearch(Font font, String text, int firstIdx, int lastIdx, double xPos) {
		int curIdx = firstIdx + (lastIdx-firstIdx)/2;
		Letter r = compare(font, text, curIdx, xPos);
		switch (r.cmp) {
		case EQUAL:
			return r;
		case LESSER:
			return binSearch(font, text, firstIdx, curIdx, xPos);
		case GREATER:
			return binSearch(font, text, curIdx, lastIdx, xPos);
		}
		
		// ERROR?
		return null;
	}
	
	public Text() {
		
		area.background().set(new Backgrounds(
				new FillBackground(new Color(255, 255, 255, 210), new CornerRadii(7), new Insets(3,3,3,3)),
				new FillBackground(new Color(55, 55, 55, 100), new CornerRadii(10), new Insets(0,0,0,0))
				));
		
		area.acceptFocus().set(true);
		area.acceptTapEvents().set(true);
		
		area.width().set(200d);
		area.height().set(40d);
		
		nodeText = new at.bestsolution.wgraf.scene.Text();
		nodeText.setParent(area);
		nodeText.x().set(10);
		
		font().registerChangeListener(new ChangeListener<Font>() {
			@Override
			public void onChange(Font oldValue, Font newValue) {
				double height = newValue.stringExtent("Asdy").y;
				nodeText.y().set(40/2d - height/2);
				nodeCursor.y().set(40/2d - height/2);
				nodeCursor.height().set(height);
			}
		});
		
		nodeCursor = new Container();
		nodeCursor.setParent(area);
		nodeCursor.background().set(new FillBackground(new Color(0, 255, 255, 100), new CornerRadii(0), new Insets(0,0,0,0)));
		nodeCursor.width().set(3d);
		nodeCursor.height().set(30d);
		
		area.onTap().registerSignalListener(new SignalListener<TapEvent>() {
			@Override
			public void onSignal(TapEvent data) {
				final Font currentFont = font().get();
				final String currentText = text().get();
				
				final int firstIdx = 0;
				final int lastIdx = Math.max(0, currentText.length());
				
				final double offsetX = data.x - nodeText.x().get();
				Letter result = binSearch(currentFont, currentText, firstIdx, lastIdx, offsetX);
				
				if ((result.endX - result.beginX) / 2 < offsetX - result.beginX) {
					cursorIndex.set(result.index + 1);
				}
				else {
					cursorIndex.set(result.index);
				}
				
				
			}
		});
		System.err.println("TextField: " + area.onKeyPress());
		area.onKeyPress().registerSignalListener(new SignalListener<KeyEvent>() {
			@Override
			public void onSignal(KeyEvent data) {
				System.err.println("KEY PRESS: ");
				System.err.println(data.keyCode + " - " + data.key);
				
				// letter
				final int idx = cursorIndex.get();
				String currentText = text().get();
				String headText = currentText.substring(0, idx);
				String tailText = currentText.substring(idx, currentText.length());
				String result = headText + data.key + tailText;
				text().set(result);
				cursorIndex.set(idx+1);
			}
		});
		
		cursorIndex.registerChangeListener(new ChangeListener<Integer>() {
			@Override
			public void onChange(Integer oldValue, Integer newValue) {
				Letter letter = getLetter(font().get(), text().get(), newValue);
				nodeCursor.x().set(nodeText.x().get() + letter.beginX);
			}
		});
	}
	
	@Override
	public Vec2d computePreferredSize() {
		return font().get().stringExtent(text.get());
	}
	
	private Property<String> text = null;
	public Property<String> text() {
		if (text == null) {
			text = new SimpleProperty<String>("");
			Binder.uniBind(text, nodeText.text());
		}
		return text;
	}
	
	public Property<Font> font() {
		return nodeText.font();
	}
}
