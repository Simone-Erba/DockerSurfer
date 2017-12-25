package data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.traversal.TraversalDescription;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import searcher.GraphOperations;
enum MyLabels implements Label { Image, User; }
enum MyRelationshipTypes implements RelationshipType {
	DEPENDENCY, FOLLOW
}
/**
 * A class with only methods to access the backend
 * @author Simone-Erba
 */
public class Backend {
	/**
	 * Return a JSON representation of the User for the frontend The JSON has
	 * Repository names but not Repositories details
	 * 
	 * @param name
	 *            The user name
	 * @return JSON representation of the User object
	 * @see Repository
	 * @see User
	 */
	public String getUser(String name) {
		Index<Node> index = GraphOperations.getInstance().getGraph().index().forNodes("indexUser");
		IndexHits<Node> nodes = index.get("user", name);
		if (nodes.size() == 0) {
			return "{\"code\":\"404\",\"error\":\"User not found\"}";
		}
		ResourceIterator<Node> i = nodes.iterator();
		List<Repository> repos = new ArrayList<Repository>();
		List<String> list = new ArrayList<String>();
		while (i.hasNext()) {
			Node n = i.next();
			String repo = n.getProperty("fullname").toString();
			if (!list.contains(repo)) {
				Repository r = new Repository(repo, null, 0);
				repos.add(r);
				list.add(repo);
			}
		}
		User u = new User(name, repos);
		Gson gson = new GsonBuilder().create();
		System.out.println(gson.toJson(u));
		return "{\"code\":\"200\",\"data\":" + gson.toJson(u) + "}";
	}

	/**
	 * Return a JSON representation of the Repository for the frontend The JSON
	 * has tag names but not tag details
	 * 
	 * @param name
	 *            The user name
	 * @return JSON representation of the Repository object
	 * @see Repository
	 * @see Tag
	 */
	public String getRepository(String name) {
		Index<Node> index = GraphOperations.getInstance().getGraph().index().forNodes("indexRepo");
		IndexHits<Node> n = index.get("repo", name);
		if (n.size() == 0) {
			return "{\"code\":\"404\",\"error\":\"Repository not found\"}";
		}
		ResourceIterator<Node> i = n.iterator();
		List<Tag> l = new ArrayList<Tag>();
		int pop = 0;
		if (i.hasNext()) {
			while (i.hasNext()) {
				Node im = i.next();
				Tag t = new Tag(im.getProperty("fulltag").toString(), null,
						Integer.valueOf(im.getProperty("nodeRank").toString()),
						Integer.valueOf(im.getProperty("betweeness").toString()), null, null);
				pop += Integer.valueOf(im.getProperty("nodeRank").toString());
				l.add(t);
			}
		}
		Repository r = new Repository(name, l, pop);
		Gson gson = new GsonBuilder().create();
		return "{\"code\":\"200\",\"data\":" + gson.toJson(r) + "}";
	}

	/**
	 * Return a JSON representation of the Tag for the frontend The JSON has all
	 * the attributes for the searched Tag but not all the details for children
	 * or father tags
	 * 
	 * @param name
	 *            The user name
	 * @return JSON representation of the Tag object
	 * @see Tag
	 */
	public String getTag(String name) {
		Index<Node> index = GraphOperations.getInstance().getGraph().index().forNodes("indexTag");
		IndexHits<Node> n = index.get("tag", name);
		if (n.size() == 0) {
			return "{\"code\":\"404\",\"error\":\"Tag not found\"}";
		}
		if (n.size() >= 2) {
			return "{\"code\":\"404\",\"error\":\"Tag name is not unique\"}";
		}
		ResourceIterator<Node> ind = n.iterator();
		Node c = null;
		Tag tag = null;
		if (ind.hasNext()) {
			c = ind.next();
		}
		if (c != null) {
			List<Node> f = GraphOperations.getInstance().getNodes(c, Direction.INCOMING,MyRelationshipTypes.DEPENDENCY);
			Tag t = null;
			// 1 father per node should be a property for this graph
			if (f.size() == 1) {
				Node father = f.iterator().next();
				t = new Tag(father.getProperty("fulltag").toString(), null,
						Integer.valueOf(father.getProperty("nodeRank").toString()).intValue(),
						Integer.valueOf(father.getProperty("betweeness").toString()).intValue(), null, null);
			} else {
				if (f.size() != 0) {
					return "{\"code\":\"404\",\"error\":\"there is an error in the db structure node:" + name + " has "
							+ f.size() + " fathers\"}";
				}
			}

			List<Node> set = GraphOperations.getInstance().getNodes(c, Direction.OUTGOING,MyRelationshipTypes.DEPENDENCY);
			Iterator<Node> it = set.iterator();
			List<Tag> l = new ArrayList<Tag>();
			// getting children information
			while (it.hasNext()) {
				Node i = it.next();
				Tag t2 = new Tag(i.getProperty("fulltag").toString(), null,
						Integer.valueOf(i.getProperty("nodeRank").toString()).intValue(),
						Integer.valueOf(i.getProperty("betweeness").toString()).intValue(), t, null);
				l.add(t2);
			}

			tag = new Tag(c.getProperty("fulltag").toString(), c.getProperty("date").toString(),
					Integer.valueOf(c.getProperty("nodeRank").toString()).intValue(),
					Integer.valueOf(c.getProperty("betweeness").toString()).intValue(), t, l);
		}

		Gson gson = new GsonBuilder().create();
		return "{\"code\":\"200\",\"data\":" + gson.toJson(tag) + "}";
	}
	/**
	 * 
	 * @return the 100 most popular images
	 */
	public String popular() {
		// TODO Auto-generated method stub
		 //TraversalDescription td;
		 //td.
		
		
		ResourceIterator<Node> i = GraphOperations.getInstance().getGraph().findNodes(MyLabels.User);
		Tag[] m = new Tag[100];
		while (i.hasNext()) {
			Node im = i.next();
			int dim = (int) im.getProperty("nodeRank");
			Tag p = new Tag(im.getProperty("fulltag").toString(),null, dim,0,null,null);
			boolean finito = false;
			int j = 0;
			while (finito == false && j < m.length) {
				if (m[j] == null) {
					m[j] = p;
					finito = true;
				} else {

					if (m[j].getPagerank() < p.getPagerank()) {
						for (int k = m.length - 1; k > j; k--) {
							m[k] = m[k - 1];
						}
						m[j] = p;
						finito = true;

					}
				}
				j++;

			}

		
		}
		Gson gson = new GsonBuilder().create();
		return "{\"code\":\"200\",\"data\":" + gson.toJson(m) + "}";
	}
	public List<RealUser> getRealUsers()
	{
		List<RealUser> result=new ArrayList<RealUser>();
		GraphDatabaseService graph=GraphOperations.getInstance().getGraph();
		ResourceIterator<Node> iterator=graph.findNodes(MyLabels.User);
		int i=0;
		while(iterator.hasNext())
		{
			Node n=iterator.next();
			String email=n.getProperty("email").toString();
			String password=n.getProperty("password").toString();
			String country=n.getProperty("country").toString();
			String city=n.getProperty("city").toString();
			String name=n.getProperty("name").toString();
			String occupation=n.getProperty("occupation").toString();
			RealUser ru=new RealUser(password, name, city, email, country, occupation);
			result.add(ru);
			System.out.println("user number:" +i);
			System.out.println(email);
			System.out.println(password);
			System.out.println(country);
			System.out.println(city);
			System.out.println(name);
			System.out.println(occupation);


		}
		return result;
	}
	public String getRealUsersJson()
	{
		Gson gson = new GsonBuilder().create();
		return "{\"code\":\"200\",\"data\":" + gson.toJson(getRealUsers()) + "}"; 
	}
	public RealUser getRealUser(String user)
	{
		Node n=GraphOperations.getInstance().getRealUser(user);
		String email=n.getProperty("email").toString();
		String password=n.getProperty("password").toString();
		String country=n.getProperty("country").toString();
		String city=n.getProperty("city").toString();
		String name=n.getProperty("name").toString();
		String occupation=n.getProperty("occupation").toString();
		RealUser ru=new RealUser(password, name, city, email, country, occupation);
		return ru;
	}
	public String getRealUserImages(String email)
	{
		Transaction tr=GraphOperations.getInstance().getGraph().beginTx();
		List<Node> images=GraphOperations.getInstance().getRealUserImages(email);
		Iterator<Node> i=images.iterator();
		List<Tag> images2=new ArrayList<Tag>();
		while(i.hasNext())
		{
			Node im = i.next();
			Tag t = new Tag(im.getProperty("fulltag").toString(), null,
					Integer.valueOf(im.getProperty("nodeRank").toString()),
					Integer.valueOf(im.getProperty("betweeness").toString()), null, null);
			images2.add(t);
		}
		tr.success();
		tr.close();
		Gson gson = new GsonBuilder().create();
		return "{\"code\":\"200\",\"data\":" + gson.toJson(images2) + "}"; 
	}
}
