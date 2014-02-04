package at.bestsolution.wgraf;

import at.bestsolution.wgraf.properties.ChangeListener;
import at.bestsolution.wgraf.properties.Converter;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.properties.simple.SimpleProperty;
import at.bestsolution.wgraf.scene.Container;
import at.bestsolution.wgraf.scene.Node;
import at.bestsolution.wgraf.util.NodeIterator;

public class Application extends Frontend<BackingApplication> {
	
	private static ThreadLocal<Application> app = new ThreadLocal<Application>();
	
	public Application() {
		backend.setInit(new Runnable() {
			@Override
			public void run() {
				app.set(Application.this);
				initialize();
			}
		});
		
		focusNode.registerChangeListener(new ChangeListener<Node<?>>() {
			@Override
			public void onChange(Node<?> oldValue, Node<?> newValue) {
				if (oldValue != null) {
					Property<Boolean> oldFocus = (Property<Boolean>) oldValue.focus();
					oldFocus.set(false);
				}
				
				if (newValue != null) {
					Property<Boolean> newFocus = (Property<Boolean>) newValue.focus();
					newFocus.set(true);
				}
			}
		});
	}
	
	public Property<Boolean> fullscreen() {
		return backend.fullscreen();
	}
	
	private Property<Node<?>> focusNode = new SimpleProperty<Node<?>>(null);
	
	public Property<Node<?>> focusNode() {
		return focusNode;
	}
	
	public void requestFocus(Node<?> node) {
		if (node.acceptFocus().get()) {
			focusNode.set(node);
		}
	}
	
	public void focusNextNode() {
		Node<?> focusNode = Application.get().focusNode().get();
		System.err.println("Current Focus node: " + focusNode);
		
		NodeIterator it = focusNode.iterator(new Converter<Node<?>, Boolean>() {
			@Override
			public Boolean convert(Node<?> value) {
				return value.acceptFocus().get();
			}
		});
		
		if (it.hasNext()) {
			Node<?> next = it.next();
			System.err.println("NExt Focus node: " + next);
			next.requestFocus();
		}
	}
	
	public void focusPrevNode() {
		Node<?> focusNode = Application.get().focusNode().get();
		System.err.println("Current Focus node: " + focusNode);
		
		NodeIterator it = focusNode.iterator(new Converter<Node<?>, Boolean>() {
			@Override
			public Boolean convert(Node<?> value) {
				return value.acceptFocus().get();
			}
		});
		
		if (it.hasPrev()) {
			Node<?> next = it.prev();
			System.err.println("PRev Focus node: " + next);
			next.requestFocus();
		}
	}
	
	@Override
	protected Class<BackingApplication> internal_getBackendType() {
		return BackingApplication.class;
	}
	
	public final Property<String> title() { return backend.title(); }
	public final Property<Container> root() { return backend.root(); }
	
	public final void start(String[] args) { backend.start(args); }
	
	
	protected void initialize() {
	}
	
	
	public static Application get() {
		return app.get();
	}
}
