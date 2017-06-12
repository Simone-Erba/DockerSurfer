package data;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class GraphOperations {
	static GraphDatabaseService graph;
	static GraphOperations instance;
	Transaction t;
	public static GraphOperations getInstance()
	{
		if(instance==null)
		{
			instance=new GraphOperations();
			return instance;
		}
		else
		{
			return instance;
		}
	}
	private GraphOperations() {
		
		File f=new File( "E:////index//neo4jdatabase");
		graph = new GraphDatabaseFactory().newEmbeddedDatabase( f );
		t=graph.beginTx();
	}
	public void newT()
	{
		t.success();
		t.close();
		t=graph.beginTx();
	}

	public Transaction getT() {
		return t;
	}

	public GraphDatabaseService getGraph() {
		return graph;
	}
	public void close()
	{
		System.out.println("s1");
		t.success();
		System.out.println("s2");
		t.close();
		System.out.println("s3");
		graph.shutdown();
	}
	public List<Node> getNodes(Node n,Direction d)
	{
		List<Node> r=new ArrayList<Node>();
		Iterable<Relationship> l=n.getRelationships(d);
		Iterator<Relationship> i=l.iterator();
		while(i.hasNext())
		{
			Relationship e=i.next();
			Node node=e.getOtherNode(n);
			r.add(node);
		}
		return r;
	}
}
