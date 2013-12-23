package at.bestsolution.wgraf.scene.shapes;

import at.bestsolution.wgraf.paint.Paint;
import at.bestsolution.wgraf.properties.Property;
import at.bestsolution.wgraf.scene.Node;

public abstract class Shape<Backend extends BackingShape> extends Node<Backend> {


	public Property<Paint> fill() { return backend.fill(); }
}
