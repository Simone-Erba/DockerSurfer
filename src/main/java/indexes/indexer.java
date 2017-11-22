package indexes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import au.com.bytecode.opencsv.CSVWriter;
import searcher.GraphOperations;

/**
 * A runnable class that updates node rank and betweeness centrality values for all the graph
 * @author Simone-Erba
 *
 *         
 */
public class indexer extends Thread {
	static int i = 0;
	static int j = 0;
	static Transaction t;
	static GraphDatabaseService graph = null;

	/**
	 * every hour, update the values
	 */
	@Override
	public void start() {
		while (true) {
			graph = GraphOperations.getInstance().getGraph();
			t = graph.beginTx();
			nodesRank();
			t.success();
			t.close();
			t = graph.beginTx();
			nodesBeetweness();

			t.success();
			t.close();
			graph.shutdown();
			try {
				Thread.sleep(3600000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * for every node without father, calculate betweeness centrality values for
	 * every node in its tree
	 */
	private static void nodesBeetweness() {
		// TODO Auto-generated method stub
		ResourceIterable<Node> nodes = graph.getAllNodes();
		ResourceIterator<Node> i = nodes.iterator();
		while (i.hasNext()) {
			Node node = i.next();
			if (node.getDegree(Direction.INCOMING) == 0) {
				nodeBeetweness(node, 0);
			}
		}
	}

	/**
	 * set the node betweeness value with the given value, then set children
	 * betweeness values to the given value plus one every 100k nodes, commit
	 * the transaction to avoid too big transactions
	 * 
	 * @param node
	 *            The node
	 * @param n
	 *            betweeness centrality value
	 */
	private static void nodeBeetweness(Node node, int n) {
		j++;
		if (j % 100000 == 0) {
			t.success();
			t.close();
			t = graph.beginTx();
		}
		// TODO Auto-generated method stub
		node.setProperty("betweeness", n);
		List<Node> figli = getNodes(node, Direction.OUTGOING);
		Iterator<Node> it = figli.iterator();
		while (it.hasNext()) {
			Node no = it.next();
			nodeBeetweness(no, n + 1);
		}
	}

	/**
	 * for every node without father, calculate node rank values for every node
	 * in its tree
	 */
	public static void nodesRank() {
		ResourceIterable<Node> nodes = graph.getAllNodes();
		ResourceIterator<Node> i = nodes.iterator();
		while (i.hasNext()) {
			Node node = i.next();
			if (node.getDegree(Direction.INCOMING) == 0) {
				nodeRank(node);
			}
		}
	}

	/**
	 * A method for getting the near nodes of a node in a given direction
	 * 
	 * @param n
	 *            The node
	 * @param d
	 *            The Direction
	 * @return The list of nodes
	 */
	public static List<Node> getNodes(Node n, Direction d) {
		List<Node> r = new ArrayList<Node>();
		Iterable<Relationship> l = n.getRelationships(d);
		Iterator<Relationship> i = l.iterator();
		while (i.hasNext()) {
			Relationship e = i.next();
			Node node = e.getOtherNode(n);
			r.add(node);
		}
		return r;
	}

	/**
	 * Recursive method set the node page rank value to one if the node has no
	 * children otherwise, set the node page rank value to the sum of the
	 * children values plus one every 100k nodes, commit the transaction to
	 * avoid too big transactions
	 * 
	 * @param node
	 *            The node
	 */
	private static int nodeRank(Node node) {
		i++;
		if (i % 100000 == 0) {
			t.success();
			t.close();
			t = graph.beginTx();
		}
		// TODO Auto-generated method stub
		int n = 1;
		if (node.getDegree(Direction.OUTGOING) == 0) {
			node.setProperty("nodeRank", n);
			return n;
		} else {
			List<Node> figli = getNodes(node, Direction.OUTGOING);
			Iterator<Node> it = figli.iterator();
			while (it.hasNext()) {
				Node no = it.next();
				n = n + nodeRank(no);
			}
		}
		node.setProperty("nodeRank", n);
		return n;
	}
}
