package data;

import org.neo4j.graphdb.Node;

/**
 * A data class for associate a node with its popularity
 * @author Simone-Erba
 *
 *         
 */
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