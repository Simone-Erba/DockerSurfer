package searcher;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.neo4j.graphdb.Node;

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
