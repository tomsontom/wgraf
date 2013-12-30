package at.bestsolution.wgraf.backend.javafx.scene;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import at.bestsolution.wgraf.backend.javafx.JavaFxBinder;
import at.bestsolution.wgraf.backend.javafx.JavaFxConverter;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.TransitionProperty;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.properties.simple.SimpleTransitionProperty;
import at.bestsolution.wgraf.scene.BackingContainer;
import at.bestsolution.wgraf.style.Background;


public class JavaFxContainer extends JavaFxNode<javafx.scene.layout.Region> implements BackingContainer {

	private static class FxRegion extends Region {
		
		public FxRegion() {
			
			
			Image img = new Image("https://d3oaxc4q5k2d6q.cloudfront.net/m/c4f05e63beb6/img/language-avatars/java_64.png");
			
			ImageView imgView = new ImageView();
			imgView.setImage(img);
			
			imgView.setLayoutX(10);
			imgView.setLayoutY(100);
			
			getChildren().add(imgView);
			
//			final Rectangle r = new Rectangle();
//			this.setClip(new Rectangle(getWidth(), getHeight()));
//			r.setLayoutX(10);
//			r.setLayoutY(10);
//			r.setWidth(150);
//			r.setHeight(100);
//			
//			r.setFill(new LinearGradient(0, 0, 75, 0, false, CycleMethod.REPEAT, Arrays.asList(
//					new Stop(0, Color.RED), 
//					new Stop(1, Color.BLUE))));
//			
//			r.setOnMouseClicked(new EventHandler<MouseEvent>() {
//
//				@Override
//				public void handle(MouseEvent event) {
//					Image image = new Image("http://wwwbruegge.informatik.tu-muenchen.de/twiki/pub/Lehrstuhl/SysiphusGoesEclipse/eclipse.png");
//					
//					ImagePattern pattern = new ImagePattern(image);
//				
//					r.setFill(pattern);
//				}
//			});
//			
//			getChildren().add(r);
			
////			setOnMouseClicked(new EventHandler<MouseEvent>() {
////
////				@Override
////				public void handle(MouseEvent event) {
////					Font font = Font.font("DejaVu Serif", 11);
////					System.err.println(font);
////					Text text = new Text();
////					text.setFont(font);
////					text.setStyle("-fx-font-size: 20pt");
////					System.err.println(text.getFont());
////					text.setTextOrigin(VPos.TOP);
//////					text.setBoundsType(TextBoundsType.);
////					text.setText("Lorem Ipsum");
////					
////					text.setLayoutX(10);
////					text.setLayoutY(10);
////					
////					getChildren().add(text);
////					
////				}
//				
//				
//				
//			});
			
			
		
		}
		
		@Override
		public ObservableList<Node> getChildren() {
			return super.getChildren();
		}
	}
	
	@Override
	protected Region createNode() {
		return new FxRegion();
	}
	
	private Property<Background> background = null;
	@Override
	public Property<Background> background() {
		if (background == null) {
			background = new SimpleProperty<Background>();
			JavaFxBinder.uniBind(background, new JavaFxBinder.JfxSetter<Background>() {
				@Override
				public void doSet(Background value) {
					node.setBackground(JavaFxConverter.convert(value));
				}
			});
		}
		return background;
	}
	
	private Property<Double> width = null;
	@Override
	public Property<Double> width() {
		if (width == null) {
			width = new SimpleProperty<Double>(node.getWidth());
			JavaFxBinder.uniBind(width, new JavaFxBinder.JfxSetter<Double>() {
				@Override
				public void doSet(Double value) {
					node.setMinWidth(value);
					node.setMaxWidth(value);
				}
			});
		}
		return width;
	}
	
	
	private TransitionProperty<Double> height = null;
	@Override
	public TransitionProperty<Double> height() {
		if (height == null) {
			height = new SimpleTransitionProperty<Double>(node.getHeight());
			JavaFxBinder.uniBind(height, new JavaFxBinder.JfxSetter<Double>() {
				@Override
				public void doSet(Double value) {
					node.setMinHeight(value);
					node.setMaxHeight(value);
				}
			});
		}
		return height;
	}
	
	public void addChild(javafx.scene.Node child) {
		((FxRegion)node).getChildren().add(child);
	}
	

}
