
package searcher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;

/**
 * A class that starts the updating of the database
 * 
 * @author Simone-Erba
 *
 */
public class Application extends Thread {
	@Override
	/**
	 * Get all the user names and root nodes, the starts NewImages, ScanRegistry
	 * and NamespacePuller Threads
	 * 
	 * @see NewImages
	 * @see ScanRegistry
	 * @see NamespacePuller
	 * 
	 */
	public void start() {
		GraphOperations g = GraphOperations.getInstance();
		Transaction t = g.getGraph().beginTx();
		ConcurrentLinkedQueue<Node> roots = new ConcurrentLinkedQueue<Node>();
		ResourceIterable<Node> a = g.getGraph().getAllNodes();
		/*
		 * Iterator<Node> ot2=a.iterator(); int pj=0; while(ot2.hasNext())
		 * //REMOVE ERRORS { Node p=ot2.next(); Relationship
		 * r=p.getSingleRelationship(MyRelationshipTypes.DEPENDENCY,
		 * Direction.INCOMING); System.out.println("deleting "+pj); pj++;
		 * if(r!=null) {
		 * 
		 * Node p2=r.getOtherNode(p);
		 * if(p2.getProperty("layers").toString().length()>p.getProperty(
		 * "layers").toString().length()) {
		 * System.out.println("deleting between "+p.getProperty("fulltag")
		 * +" and "+p2.getProperty("fulltag")); r.delete();
		 * 
		 * } } }
		 */

		// System.out.println(nn.getProperty("layers"));

		/*
		 * System.out.println("IN"); //REMOVE CYCLES Iterable<Relationship>
		 * jh=nn.getRelationships(Direction.INCOMING); Iterator<Relationship>
		 * iy=jh.iterator(); while(iy.hasNext()) {
		 * System.out.println(iy.next().getOtherNode(nn).getProperty("fulltag"))
		 * ; } System.out.println("OUT"); Iterable<Relationship>
		 * jh3=nn.getRelationships(Direction.OUTGOING); Iterator<Relationship>
		 * iy3=jh3.iterator(); while(iy3.hasNext()) {
		 * //System.out.println(iy3.next().getOtherNode(nn).getProperty(
		 * "fulltag")); }
		 */

		ResourceIterator<Node> i = a.iterator();
		List<String> users = new ArrayList<String>();
		int o = 0;
		while (i.hasNext()) {
			System.out.println(o);
			o++;
			Node im = i.next();
			String user = (String) im.getProperty("user");
			if (!users.contains(user)) {
				users.add(user);
			}
			if (im.getDegree(Direction.INCOMING) == 0) {
				roots.add(im);
			}
			if (im.hasRelationship(MyRelationshipTypes.DEPENDENCY, Direction.INCOMING)) {
				g.insertMap(im,
						im.getSingleRelationship(MyRelationshipTypes.DEPENDENCY, Direction.INCOMING).getOtherNode(im));
			} else {
				g.insertMap(im, null);
			}
		}
		t.success();
		t.close();
		ConcurrentLinkedQueue<String> users2 = new ConcurrentLinkedQueue<String>();
		users2.addAll(users);
		Users u = new Users(users2, roots);
		g.setS(u);
		newImages n = new newImages(u);
		n.start();
		scanRegistry sc = new scanRegistry(u);
		sc.start();
		NamespacePuller us = new NamespacePuller(u);
		us.start();
	}
}