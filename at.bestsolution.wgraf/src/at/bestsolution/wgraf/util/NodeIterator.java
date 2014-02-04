package at.bestsolution.wgraf.util;

import at.bestsolution.wgraf.scene.Node;

public interface NodeIterator {

	public Node<?> next();
	public Node<?> prev();
	
	public boolean hasNext();
	public boolean hasPrev();
	
}
