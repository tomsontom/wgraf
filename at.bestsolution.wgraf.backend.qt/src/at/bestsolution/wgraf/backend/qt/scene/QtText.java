package at.bestsolution.wgraf.backend.qt.scene;

import at.bestsolution.wgraf.events.MouseEventSupport;
import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.scene.BackingText;

import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QGraphicsSimpleTextItem;

public class QtText extends QtNode<QGraphicsSimpleTextItem> implements BackingText {

	@Override
	protected QGraphicsSimpleTextItem createNode() {
		return new QGraphicsSimpleTextItem();
	}

	private Property<String> text = null;
	@Override
	public Property<String> text() {
		if (text == null) {
			text = new SimpleProperty<String>();
			text.registerChangeListener(new ChangeListener<String>() {
				@Override
				public void onChange(String oldValue, String newValue) {
					node.setText(newValue);
				}
			});
			
		}
		return text;
	}
	
	private Property<Double> fontSize = null;
	public Property<Double> fontSize() {
		if (fontSize == null) {
			fontSize = new SimpleProperty<Double>();
			fontSize.registerChangeListener(new ChangeListener<Double>() {
				@Override
				public void onChange(Double oldValue, Double newValue) {
					QFont font = node.font();
					font.setPointSizeF(newValue);
					node.setFont(font);
				}
			});
		}
		return fontSize;
	}
	@Override
	public void setEventSupport(MouseEventSupport support) {
		// TODO Auto-generated method stub
	};

}
