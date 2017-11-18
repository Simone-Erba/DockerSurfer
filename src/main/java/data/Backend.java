package data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import searcher.GraphOperations;
/**
 * 
 * @author Simone-Erba
 *
 *A class  with only methods to access the backend
 */
public class Backend {
/**
 * Return a JSON representation of the User for the frontend
 * The JSON has Repository names but not Repositories details
 * @param name The user name
 * @return JSON representation of the User object
 * @see Repository
 * @see User
 */
	public String getUser(String name)
	{
		Index<Node> index=GraphOperations.getInstance().getGraph().index().forNodes("indexUser");
		IndexHits<Node> nodes=index.get("user", name);
		if(nodes.size()==0)
		{
			return "{\"code\":\"404\",\"error\":\"User not found\"}";
		}
		ResourceIterator<Node> i=nodes.iterator();
		List<Repository> repos=new ArrayList<Repository>();
		List<String> list=new ArrayList<String>();
		while(i.hasNext())
		{
			Node n=i.next();
			String repo=n.getProperty("fullname").toString();
			if(!list.contains(repo))
			{
				Repository r=new Repository(repo,null,0);
				repos.add(r);
				list.add(repo);
			}       
		}
		User u=new User(name,repos);
		Gson gson = new GsonBuilder().create();
		System.out.println(gson.toJson(u));
		return "{\"code\":\"200\",\"data\":"+gson.toJson(u)+"}";
	}
	/**
	 * Return a JSON representation of the Repository for the frontend
	 * The JSON has tag names but not tag details
	 * @param name The user name
	 * @return JSON representation of the Repository object
	 * @see Repository
	 * @see Tag
	 */
	public String getRepository(String name)
	{
		Index<Node> index=GraphOperations.getInstance().getGraph().index().forNodes("indexRepo");
		IndexHits<Node> n=index.get("repo", name);
		if(n.size()==0)
		{
			return "{\"code\":\"404\",\"error\":\"Repository not found\"}";
		}
		ResourceIterator<Node> i=n.iterator();
		List<Tag> l=new ArrayList<Tag>();
		int pop=0;
		if (i.hasNext())
		{
			while (i.hasNext())
			{
				Node im = i.next();
				Tag t=new Tag(im.getProperty("fulltag").toString(),null,Integer.valueOf(im.getProperty("nodeRank").toString()),Integer.valueOf(im.getProperty("betweeness").toString()),null,null);
				pop+=Integer.valueOf(im.getProperty("nodeRank").toString());
				l.add(t);
			}
		}
		Repository r=new Repository(name,l,pop);
		Gson gson = new GsonBuilder().create();
		return "{\"code\":\"200\",\"data\":"+gson.toJson(r)+"}";
	}
	/**
	 * Return a JSON representation of the Tag for the frontend
	 * The JSON has all the attributes for the searched Tag but not all the details for children or father tags
	 * @param name The user name
	 * @return JSON representation of the Repository object
	 * @see Tag
	 */
	public String getTag(String name)
	{
		Index<Node> index=GraphOperations.getInstance().getGraph().index().forNodes("indexTag");
		IndexHits<Node> n=index.get("tag", name);
		if(n.size()==0)
		{
			return "{\"code\":\"404\",\"error\":\"Tag not found\"}";
		}
		if(n.size()>=2)
		{
			return "{\"code\":\"404\",\"error\":\"Tag name is not unique\"}";
		}
		ResourceIterator<Node> ind=n.iterator();
		Node c=null;
		Tag tag=null;
		if(ind.hasNext())
		{
			c=ind.next();
		}
		if(c!=null)
		{
			List<Node> f = GraphOperations.getInstance().getNodes(c,Direction.INCOMING);
			Tag t=null;
			//1 father per node should be a property for this graph
			if(f.size()==1)
			{
				Node father=f.iterator().next();
				t=new Tag(father.getProperty("fulltag").toString(),null, Integer.valueOf(father.getProperty("nodeRank").toString()).intValue(),Integer.valueOf(father.getProperty("betweeness").toString()).intValue(),null,null);
			}
			else
			{
				if(f.size()!=0)
				{
					return "{\"code\":\"404\",\"error\":\"there is an error in the db structure node:"+name+" has "+f.size()+" fathers\"}";
				}
			}

			List<Node> set = GraphOperations.getInstance().getNodes(c,Direction.OUTGOING);
			Iterator<Node> it = set.iterator();
			List<Tag> l=new ArrayList<Tag>();
			//getting children information
			while (it.hasNext())
			{
				Node i = it.next();
				Tag t2=new Tag(i.getProperty("fulltag").toString(),null,Integer.valueOf(i.getProperty("nodeRank").toString()).intValue(),Integer.valueOf(i.getProperty("betweeness").toString()).intValue(),t,null);
				l.add(t2);
			}

			tag=new Tag(c.getProperty("fulltag").toString(),c.getProperty("date").toString(),Integer.valueOf(c.getProperty("nodeRank").toString()).intValue(),Integer.valueOf(c.getProperty("betweeness").toString()).intValue(),t,l);
		}

		Gson gson = new GsonBuilder().create();
		return "{\"code\":\"200\",\"data\":"+gson.toJson(tag)+"}";
	}
}
