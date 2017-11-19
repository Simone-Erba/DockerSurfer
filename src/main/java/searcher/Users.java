package searcher;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.neo4j.graphdb.Node;
/**
 * A class to manage user names and root nodes. It is useful to know names of all users in the Docker Registry,
 * because it's then easy to make API calls and get all the images on the Docker Registry. Root nodes are useful,
 * because they are used for the insertion of new nodes. If a node is not compatible with a root node,
 * it will surely not be in relation with the nodes under the root node.
 * The class is Thread Safe, because it has synchronized methods and ConcurrentLinkedQueues
 * @see GraphOperations to an expanation of the graph structure
 * @author Simone-Erba
 *
 */
public class Users {
	ConcurrentLinkedQueue<String> users=new ConcurrentLinkedQueue<String>();
	ConcurrentLinkedQueue<Node> roots=new ConcurrentLinkedQueue<Node>();
	
	public ConcurrentLinkedQueue<Node> getRoots() {
		ConcurrentLinkedQueue<Node> copy=roots;
		return copy;
	}
	public synchronized void remove(Node s)
	{
		roots.remove(s);
	}
	public synchronized void add(Node s)
	{
		if(!roots.contains(s))
		{
			roots.add(s);
		}
	}
	public synchronized void addUser(String s)
	{
		if(!users.contains(s))
		{
			users.add(s);
		}
	}
	public void setRoots(ConcurrentLinkedQueue<Node> roots) {
		this.roots = roots;
	}
	public String get()
	{
		String s= users.poll();
		users.add(s);
		return s;
	}
	public Users(ConcurrentLinkedQueue<String> c,ConcurrentLinkedQueue<Node> roots) {
		super();
		this.users = c;
		this.roots=roots;
	}
	public boolean contains(String s)
	{
		return users.contains(s);
	}
	public ConcurrentLinkedQueue<String> getC() {
		ConcurrentLinkedQueue<String> copy=users;
		return copy;
	}
	
}
