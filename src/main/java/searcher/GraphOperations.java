package searcher;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import org.json.JSONArray;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;

import data.IdManager;
import data.RealUser;
import data.Relazione;


enum MyRelationshipTypes implements RelationshipType {
	DEPENDENCY, FOLLOW
}
enum MyLabels implements Label { Image, User }
/**
 *Singleton class to acces the database. Some detail on the graph:
 *
 *         Nodes are of only 1 type: Image Every nodes has the following
 *         attributes: <br> user: User name, for example library <br> 
 *         name: Repositoiry <br>
 *         name, for example tomcat. Not really used <br>
 *         tag: tag name, for example latest. Not really used <br>
 *         date: last time that the image was updated,
 *         format is yyyy/MM/dd HH:mm:ss. Soon the date will represent the last
 *         time that I checked if there was any update in its layers<br>
 *          fullname: for example library/tomcat, used for image searches<br>
 *            fulltag for example library/tomcat:latest, used for
 *         image searches<br> layers: List of
 *         layer codes, for example "sha256:ewgfey23w32tw43243f,
 *         sha256:ewftgfe34yw3243243f"<br>
 *          history: Commands wrote in the
 *         dockerfile, not every node has this attribute because it's not very
 *         useful <br> nodeRank: Page Rank value betweeness: Betweeness Centrality
 *         Value<br><br>
 *
 *
 *         Relationship are of only one type: dependency. They are oriented from
 *         the father node to the child node. Every node has 0 or 1 father, but
 *         can have several children. The graph is a forest of trees
 *<br><br>
 *
 *         A better database structure can be made, for examle nodes to
 *         represent users and repositories can be added
 * @author Simone-Erba Some details on the graph structure:

 *
 */
public class GraphOperations {
	/**
	 * a map with layers of an image as keys and the father of that image as
	 * values MAYBE A MAP.DB IS REQUIRED TO SAVE MEMORY
	 * 
	 */
	ConcurrentMap<String, Node> map;
	Users s;
	IdManager id;
	LoggerUpdater log;
	static GraphDatabaseService graph;
	private static GraphOperations istanza = null;


	static Direction[] directions = Direction.values();// BOTH IN OUT

	private GraphOperations(String path) {
		super();
		File f = new File(path);
		graph = new GraphDatabaseFactory().newEmbeddedDatabase(f);
	}

	public ConcurrentMap<String, Node> getMap() {
		return map;
	}

	public void setS(Users s) {
		this.s = s;
	}

	public static synchronized GraphOperations getInstance() {
		if (istanza == null) {
			istanza = new GraphOperations("E:/neo4jdatabase");
		}
		
		return istanza;
	}

	public GraphDatabaseService getGraph() {
		return graph;
	}

	/**
	 * A method that returns all the adiacent nodes of node n in the direction d
	 * 
	 * @param n
	 *            The node
	 * @param d
	 *            The Direction
	 * @return All the adiacent nodes in that direction
	 */
	public List<Node> getNodes(Node n, Direction d, RelationshipType ty) {
		Transaction t =graph.beginTx();
		List<Node> r = new ArrayList<Node>();
		Iterable<Relationship> l = n.getRelationships(d,ty);
		Iterator<Relationship> i = l.iterator();
		while (i.hasNext()) {
			Relationship e = i.next();
			Node node = e.getOtherNode(n);
			r.add(node);
		}
		t.success();
		t.close();
		return r;
	}

	public Node cercaPadre(Node v, Node padre, List<Node> l) {
		if (l.contains(padre)) {
			System.out.println("aborted..");
			Iterator<Node> iter = l.iterator();
			Node del = null;
			while (iter.hasNext()) {
				del = iter.next();
			}

			if (del.hasRelationship(Direction.INCOMING)) {
				this.delete(del);
				l.remove(del);
				cercaPadre(v, padre, l);
			} else {
				return null;
			}

		}
		l.add(padre);
		List<Node> possibiliPadri = getNodes(padre, Direction.OUTGOING,MyRelationshipTypes.DEPENDENCY);
		Iterator<Node> possibiliPadriIterator = possibiliPadri.iterator();
		while (possibiliPadriIterator.hasNext()) {
			Node pp = possibiliPadriIterator.next();
			Relazione r = relazione(extract(pp.getProperty("layers").toString()),
					extract(v.getProperty("layers").toString()));
			if (v.getProperty("layers").toString().equals(pp.getProperty("layers").toString()))
			{
				return padre;
			}
			if (r != null && r.isPadre())// i è figlio
			{
				return cercaPadre(v, pp, l);
			}
		}
		return padre;
	}

	/**
	 * A method to convert a String in a list of String Used because layers in
	 * the database are saved as a single string
	 * 
	 * @param s
	 * @return list of String
	 */
	public List<String> extract(String s) {
		List<String> list = Arrays.asList(s.toString().split("\\s*,\\s*"));
		Iterator<String> it = list.iterator();
		int count = 0;
		while (it.hasNext()) {
			String a = it.next();
			if (count == 0) {
				int index = a.length();
				a = a.substring(1, index);
				list.set(0, a);
			}
			if (count == list.size() - 1) {
				int index = a.length();
				a = a.substring(0, index - 1);
				list.set(count, a);
			}
			count++;
		}
		return list;
	}

	/**
	 * delete everything on the graph
	 */
	public void pulisci() {
		Transaction t =graph.beginTx();
		Iterable<Relationship> l2 = graph.getAllRelationships();
		Iterator<Relationship> i = l2.iterator();
		while (i.hasNext()) {
			Relationship v = i.next();
			v.delete();
		}
		Iterable<Node> l = graph.getAllNodes();
		Iterator<Node> i2 = l.iterator();
		while (i2.hasNext()) {
			Node v = i2.next();
			v.delete();
		}
		t.close();
	}

	/**
	 * Remove the last element from a list
	 * 
	 * @param l
	 *            the list
	 * @return the list without the last element
	 */
	public List<String> removeLastFromList(List<String> l) {
		List<String> l2 = new ArrayList<String>();
		Iterator<String> i = l.iterator();
		for (int j = 0; j < l.size() - 1; j++) {
			l2.add(i.next());
		}
		return l2;
	}

	/**
	 * A method to check if two images are in relation
	 * 
	 * @param a
	 *            layers of the first image
	 * @param b
	 *            layers of the second image
	 * @return an object of type Relazione that represent the relationship
	 * @see Relazione
	 */
	public static Relazione relazione(List<String> a, List<String> b) {
		Relazione r;
		int layers = 0;
		Iterator<String> it1 = a.iterator();
		Iterator<String> it2 = b.iterator();
		boolean diversi = false;
		while (diversi == false && it1.hasNext() && it2.hasNext()) {
			if (it1.next().equals(it2.next())) {
				layers++;
			} else {
				diversi = true;
			}
		}
		if (layers == 0) {
			return null;
		}
		// fratelli
		if (diversi == true)// sono fratelli
		{
			r = new Relazione(layers, false, false, true);
			return r;// chiamare sul padre di i
		}
		if (!it2.hasNext() && !it1.hasNext()) {
			r = new Relazione(layers, false, false, true);
			return r;
		}
		if (!it2.hasNext()) {
			// this è il figlio
			r = new Relazione(layers, false, true, false);
			return r;// chiamare sul padre di i
		} else {
			// padre
			r = new Relazione(layers, true, false, false);
			return r;

		}
	}

	/**
	 * Insert a new node in the graph. To find the father, a map with layers as
	 * keys is used. To find children, if a father it's not found, it's required
	 * to test the node with all the root nodes
	 * 
	 * @param name
	 * @param surname
	 * @param tag
	 * @param date
	 * @param layers2
	 * @param history
	 */
	public void insertSingle(String name, String surname, String tag, String date, JSONArray layers2,
			JSONArray history) {
		Transaction t =graph.beginTx();
		ConcurrentLinkedQueue<Node> roots = s.getRoots();
		// TODO Auto-generated method stub
		List<String> layers = new ArrayList<String>();
		List<String> story = new ArrayList<String>();
		if (layers2.length() > 0) {
			for (int i = layers2.length() - 1; i >= 0; i--) {
				String a = layers2.getJSONObject(i).getString("blobSum");
				layers.add(a);
				// System.out.println(a);
			}
			for (int i = history.length() - 1; i >= 0; i--) {
				String a = history.getJSONObject(i).getString("v1Compatibility");
				story.add(a);
				// System.out.println(a);
			}

			Node n = this.getGraph().createNode();
			n.setProperty("tag", tag);
			n.setProperty("date", date);
			n.setProperty("name", surname);
			n.setProperty("user", name);
			n.setProperty("fullname", name + "/" + surname);
			n.setProperty("fulltag", name + "/" + surname + ":" + tag);
			n.setProperty("layers", layers.toString());
			n.setProperty("history", story.toString());
			n.setProperty("nodeRank", 0);
			n.setProperty("betweeness", -1);
			n.addLabel(MyLabels.Image);
			String i = layersToInts(layers.toString());
			List<String> l = extract(i);
			boolean finito = false;
			// FIND FATHER
			Node padre = null;
			while (l.size() > 0 && finito == false) {
				if (map.get(l.toString()) != null) {
					padre = (Node) map.get(l.toString());
					log.getInstance().write("father is " + padre.getProperty("fulltag"));
					this.getMap().putIfAbsent(l.toString(), padre);
					finito = true;
				} else {
					l = removeLastFromList(l);
				}
			}
			// FIND CHILDREN
			if (padre == null) {
				log.getInstance().write("didnt find a father for: " + n.getProperty("fulltag"));
				Iterator<Node> iter2 = roots.iterator();
				while (iter2.hasNext()) {
					Node node = iter2.next();
					Relazione r = relazione(extract(node.getProperty("layers").toString()),
							extract(n.getProperty("layers").toString()));
					if (r != null && r.isFiglio()) {
						n.createRelationshipTo(node, MyRelationshipTypes.DEPENDENCY);
						String a = layersToInts(node.getProperty("layers").toString());
						this.getMap().put(a, n);
						s.remove(node);
						log.getInstance().write("CREATED RELATIONSHIP from " + n.getProperty("fulltag") + "TO "
								+ node.getProperty("fulltag"));
					}
				}
				s.add(n);
			} else {
				List<Node> figli = getNodes(padre, directions[2],MyRelationshipTypes.DEPENDENCY);
				Iterator<Node> i4 = figli.iterator();
				Node figlio = null;
				while (i4.hasNext()) {
					Node node = i4.next();
					Relazione r = relazione(layers, extract(node.getProperty("layers").toString()));
					if (r != null && r.isFiglio()) {
						figlio = node;
						if (figlio.hasRelationship(directions[1])) {
							figlio.getSingleRelationship(MyRelationshipTypes.DEPENDENCY, directions[1]).delete();
						}
						Relationship arco = n.createRelationshipTo(figlio, MyRelationshipTypes.DEPENDENCY);
						arco.setProperty("value", "dependency");
						String a = layersToInts(node.getProperty("layers").toString());
						this.getMap().put(a, n);
					}

				}
				Relationship arco2 = padre.createRelationshipTo(n, MyRelationshipTypes.DEPENDENCY);
				arco2.setProperty("value", "dependency");
				log.getInstance().write("INSERTED " + name + "    " + tag);
			}
		}
		t.success();
		t.close();
	}

	/**
	 * used to delete a node from the graph. useful if an image change layers
	 * 
	 * @param n
	 *            the node
	 */
	public void delete(Node n) {
		// TODO Auto-generated method stub
		Transaction t =graph.beginTx();
		Node padre = getNodes(n, Direction.INCOMING,MyRelationshipTypes.DEPENDENCY).iterator().next();
		if (padre != null) {
			Relationship d = n.getSingleRelationship(MyRelationshipTypes.DEPENDENCY, Direction.INCOMING);
			d.delete();
		}
		List<Node> figli = getNodes(n, Direction.OUTGOING,MyRelationshipTypes.DEPENDENCY);
		Iterable<Relationship> rel = n.getRelationships(Direction.OUTGOING);
		Iterator<Relationship> itrel = rel.iterator();
		while (itrel.hasNext()) {
			itrel.next().delete();
		}
		Iterator<Node> it2 = figli.iterator();
		while (it2.hasNext()) {
			Relationship r = padre.createRelationshipTo(it2.next(), MyRelationshipTypes.DEPENDENCY);
			r.setProperty("value", "dependency");
		}
		n.delete();
		t.success();
		t.close();
	}

	// Convert layers to list of numbers, the convert list of numbers in string
	public void insertMap(Node im, Node padre) {
		// TODO Auto-generated method stub
		String layers = im.getProperty("layers").toString();
		String str = layersToInts(layers);
		this.getMap().putIfAbsent(str, padre);
	}

	private String layersToInts(String layers) {
		List<String> s = extract(layers);
		Iterator<String> i = s.iterator();
		List<Integer> li = new ArrayList<Integer>();
		while (i.hasNext()) {
			String st = i.next();
			Integer in = new Integer(id.getNumero(st));
			li.add(in);
		}
		Iterator<Integer> i2 = li.iterator();
		String str = "";
		while (i2.hasNext()) {
			str += i2.next();
			if (i2.hasNext()) {
				str = str + ";";
			}
		}
		return str;
	}
	public void insertRealUser(String password, String name, String city, String email, String country, String occupation)
	{
		Transaction t =graph.beginTx();
		Index<Node> users = graph.index().forNodes("indexRealUser");
		Node n = this.getGraph().createNode();
		n.setProperty("name", name);
		n.setProperty("password", password);
		n.setProperty("city", city);
		n.setProperty("email", email);
		n.setProperty("country", country);
		n.setProperty("occupation", occupation);
		n.addLabel(MyLabels.User);
		users.add(n,"email",email);
		System.out.println("inserito "+name);
		t.success();
		t.close();
	}
	public String userFollow(String user, String image)
	{
		GraphDatabaseService graph=GraphOperations.getInstance().getGraph();
		Transaction t =graph.beginTx();
		Index<Node> index = graph.index().forNodes("indexTag");
		IndexHits<Node> n = index.get("tag", image);
		Index<Node> users = graph.index().forNodes("indexRealUser");
		IndexHits<Node> u = users.get("email", user);
		if(u.size()==1&&n.size()==1)
		{
			Node userGraph=u.iterator().next();
			Node imageGraph=n.iterator().next();
			Iterable<Relationship> list=userGraph.getRelationships(Direction.OUTGOING, MyRelationshipTypes.FOLLOW);
			boolean trovato=false;
			Iterator<Relationship> i=list.iterator();
			while(i.hasNext()&&trovato==false)
			{
				Relationship r=i.next();
				if(r.getEndNode().equals(imageGraph))
				{
					trovato=true;
				}
			}
			if(trovato==false)
			{
				userGraph.createRelationshipTo(imageGraph, MyRelationshipTypes.FOLLOW);
			}
			else
			{
				return null;
			}
			t.success();
			t.close();
			return "successful";
		}
		else
		{
			t.success();
			t.close();
			return null;
		}
	}

	public Node getRealUser(String user) {
		Transaction t =GraphOperations.graph.beginTx();
		Index<Node> users = GraphOperations.graph.index().forNodes("indexRealUser");
		IndexHits<Node> u = users.get("email", user);
		Node n=null;
		if(u.hasNext()&&u.size()==1)
		{
			n=u.next();
		}
		t.success();
		t.close();
		return n;
	}
	public List<Node> getRealUserImages(String email)
	{
		System.out.println("null pointer "+email);
		GraphDatabaseService graph=GraphOperations.getInstance().getGraph();
		Transaction t=graph.beginTx();
		Index<Node> users = graph.index().forNodes("indexRealUser");
		IndexHits<Node> u = users.get("email", email);
		List<Node> images=null;
		if(u.hasNext()&&u.size()==1)
		{
			Node n=u.next();
			images=GraphOperations.getInstance().getNodes(n, Direction.OUTGOING,MyRelationshipTypes.FOLLOW);
		}
		t.success();
		t.close();
		return images;
	}
}