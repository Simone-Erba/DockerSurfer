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

public class indexer extends Thread {
	static int i=0;
	static int j=0;
	static Transaction t;
//quanti padri ha.partitr da foglie
//quanti figli, nipoti per ogni immagine. partitr da foglie
	static GraphDatabaseService graph=null;
	@Override
	public void start()
{
	while(true)
	{
	graph = GraphOperations.getInstance().getGraph();
	 t=graph.beginTx();
	nodesRank();
	t.success();
	t.close();
	t=graph.beginTx();
	nodesBeetweness();
	
	/* double mediapage=45.49081957213193;
	 double mediabeet=3.144196440383201;
	 double var1= 228.91844864420548;
	 double var2= 1.6442016393350023;
	 List<Integer> q=new ArrayList<Integer>();
	 Map<Integer,Integer> m=new HashMap<Integer,Integer>();
	 ResourceIterable<Node> p=graph.getAllNodes();
	 ResourceIterator<Node> y=p.iterator();
	 double count=0;*/
	 //MEDIE
	/* while(y.hasNext())
	 {
	 	
	 
		 Node n=y.next();
		 if((Integer) n.getProperty("nodeRank")!=1)
		 {
			 mediapage=mediapage+(Integer) n.getProperty("nodeRank");
			 count++;
		 }
	 }	
	 System.out.println(mediapage/count);*/
	 
	 //VARIANZE
	/*while(y.hasNext())
	 {

	 	System.out.println(count);
		 Node n=y.next();
		 if((Integer) n.getProperty("betweeness")!=1)
		 {
		 var1=var1+((Integer) n.getProperty("betweeness")-mediabeet)*((Integer) n.getProperty("betweeness")-mediabeet);
		count++;
		 }
		 //var2=var2+((Integer) n.getProperty("betweeness")-mediabeet)*((Integer) n.getProperty("betweeness")-mediabeet);
	 }
	System.out.println(Math.sqrt(var1/count-1));*/
	 /*while(y.hasNext())
	 {
		 	count++;
		 	System.out.println(count);
			 Node n=y.next();
			 if(!q.contains((Integer)n.getProperty("nodeRank")))
			 {
				 q.add((Integer)n.getProperty("nodeRank"));
			 }
			 if(m.get( n.getProperty("nodeRank"))!=null)
			 {
				m.put((Integer) n.getProperty("nodeRank"), m.get( n.getProperty("nodeRank"))+1);
			 }
			 else
			 {
				 m.put((Integer) n.getProperty("nodeRank"), 1);
			 }
		 }*/
	/* CSVWriter w = null;
	 try {
		 w=new CSVWriter(new FileWriter("C:/Users/leonardo/Desktop/simo2.csv",false),',','\0');
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	 Collections.sort(q);
	 Iterator<Integer> it=q.iterator();
	 //while(it.hasNext())
	// {
		 while(y.hasNext())
		 {

		 	System.out.println(count);
			 Node n=y.next();
			 
			 //var2=var2+((Integer) n.getProperty("betweeness")-mediabeet)*((Integer) n.getProperty("betweeness")-mediabeet);
		 
		 String[] s=new String[1];
		 if(n.getDegree(Direction.INCOMING)==0)
		 {
			 s[0]=String.valueOf(n.getProperty("nodeRank"));
			 // s[1]=(String) n.getProperty("fulltag");
		 	w.writeNext(s);
		 }
		 }
	// }
	 try {
		w.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/
	
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
private static void nodesBeetweness() {
	// TODO Auto-generated method stub
	ResourceIterable<Node> nodes=graph.getAllNodes();
	ResourceIterator<Node> i=nodes.iterator();
	while(i.hasNext())
	{
		Node node=i.next();
		if(node.getDegree(Direction.INCOMING)==0)
		{
			nodeBeetweness(node,0);
		}
	}
}
private static void nodeBeetweness(Node node, int n) {
	System.out.println("betweness   "+j);
	j++;
	if(j%100000==0)
	{
		t.success();
		t.close();
		t=graph.beginTx();
	}
	// TODO Auto-generated method stub
	node.setProperty("betweeness", n);
	List<Node> figli=getNodes(node,Direction.OUTGOING);
	Iterator<Node> it=figli.iterator();
	while(it.hasNext())
	{
		Node no=it.next();
		nodeBeetweness(no,n+1);
	}
}
public static void nodesRank()
{
	ResourceIterable<Node> nodes=graph.getAllNodes();
	ResourceIterator<Node> i=nodes.iterator();
	while(i.hasNext())
	{
		Node node=i.next();
		if(node.getDegree(Direction.INCOMING)==0)
		{			
			nodeRank(node);
		}
	}
}
public static List<Node> getNodes(Node n,Direction d)
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
private static int nodeRank(Node node) {
	System.out.println("rank   "+i);
	i++;
	if(i%100000==0)
	{
		t.success();
		t.close();
		t=graph.beginTx();
	}
	// TODO Auto-generated method stub
	int n=1;
	if(node.getDegree(Direction.OUTGOING)==0)
	{
		node.setProperty("nodeRank", n);
		return n;
	}
	else
	{
		List<Node> figli=getNodes(node,Direction.OUTGOING);
		Iterator<Node> it=figli.iterator();
		while(it.hasNext())
		{
			Node no=it.next();
			n=n+ nodeRank(no);
		}
	}
	node.setProperty("nodeRank", n);
	return n;
}
}
