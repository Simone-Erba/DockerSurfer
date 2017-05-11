package data;

import org.neo4j.graphdb.Node;

public class Popular {
	Node i;
	int children;
	public Node getI() {
		return i;
	}
	public void setI(Node i) {
		this.i = i;
	}
	public int getChildren() {
		return children;
	}
	public void setChildren(int children) {
		this.children = children;
	}
	public Popular(Node i, int children) {
		super();
		this.i = i;
		this.children = children;
	}
	
}