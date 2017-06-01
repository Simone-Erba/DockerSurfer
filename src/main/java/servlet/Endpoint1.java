package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;

import data.GraphOperations;

@Path("/res")
public class Endpoint1 {
 @GET
 @Path("{user}")
 @Produces(MediaType.TEXT_PLAIN)
 public String getItem(@PathParam("user") String user, @Context HttpServletRequest request) {	 
	return user(user);
 }
 @GET
 @Path("{user}/{repo}")
 @Produces(MediaType.TEXT_PLAIN)
 public String getItem(@PathParam("user") String user,@PathParam("repo") String repo, @Context HttpServletRequest request) {	 
	return repo(user+"/"+repo);
 }
 @GET
 @Path("{user}/{repo}/{tag}")
 @Produces(MediaType.TEXT_PLAIN)
 public String getItem(@PathParam("user") String user,@PathParam("repo") String repo,@PathParam("tag") String tag, @Context HttpServletRequest request) {	 
	 HttpSession session = request.getSession(true);
	return tag(user,repo+":"+tag);
 }
 private String tag(String user,String tag) {
		// TODO Auto-generated method stub
	 Label myLabel=GraphOperations.getInstance().getGraph().getAllLabels().iterator().next();

		ResourceIterator<Node> it2=GraphOperations.getInstance().getGraph().findNodes(myLabel, "user", user);
		Node c=null;
		String stringaInizio = "[";
		String stringafine = "]";
		boolean trovato=false;
		while(trovato==false&&it2.hasNext())
		{
			c=it2.next();
			if(c.getProperty("fulltag").equals(user+"/"+tag))
			{
				trovato=true;
			}
		}
		if(c!=null&&trovato==true)
		{
		
		stringaInizio = stringaInizio + "{\"data\":{\"id\":\"" + c.getId() + "\",\"user\":\"" + c.getProperty("user")
		+ "\",\"name\":\"" + c.getProperty("name")
		+ "\",\"fulltag\":\"" + c.getProperty("fulltag")
		+"\",\"tag\":\"" + c.getProperty("tag")
				+"\",\"type\":\"searched\"}, \"position\": { \"x\": 400, \"y\": 200 }}";
		
		List<Node> f = GraphOperations.getInstance().getNodes(c,Direction.INCOMING);
		if(!f.isEmpty())
		{
		Node father=f.iterator().next();
			stringaInizio = stringaInizio + ",{\"data\":{\"id\":\"" + father.getId() + "\",\"user\":\"" + father.getProperty("user")
			+ "\",\"name\":\"" + father.getProperty("name")
			+ "\",\"fulltag\":\"" + father.getProperty("fulltag")
			+"\",\"tag\":\"" + father.getProperty("tag")
					+ "\",\"type\":\"father\"}, \"position\": { \"x\": 400, \"y\": 100 }}";
			stringafine = ",{\"data\":{\"id\":\"" + "r" + father.getId() + c.getId()
					+ "\",\"source\":\"" + father.getId() + "\",\"target\":\"" + c.getId() + "\"}}"
					+ stringafine;
		
		}
		List<Node> set = GraphOperations.getInstance().getNodes(c,Direction.OUTGOING);
		if (!set.isEmpty()) {
			Iterator<Node> it = set.iterator();
			int x = 100;
			int y = 300;
			while (it.hasNext()) {
				Node i = it.next();

				stringaInizio = stringaInizio + ",{\"data\":{\"id\":\"" + i.getId() + "\",\"user\":\"" + i.getProperty("user")
				+ "\",\"fulltag\":\"" + i.getProperty("fulltag")
				+ "\",\"name\":\"" + i.getProperty("name")
				+"\",\"tag\":\"" + i.getProperty("tag")+ "\",\"type\":\"child\"}, \"position\": { \"x\": " + x
						+ ", \"y\": " + y + " }}";
				stringafine = ",{\"data\":{\"id\":\"" + "r" + c.getId() + i.getId() + "\",\"source\":\""
						+ c.getId() + "\",\"target\":\"" + i.getId() + "\"}}" + stringafine;
				x += 100;
				if (x == 800) {
					x = 100;
					y += 100;
				}
			}
		}

		// {\"data\":{\"id\":\"gigi2\",\"name\":\"nomegigi2\"}},{\"data\":{\"id\":\"q\",\"source\":\"gigi\",\"target\":\"gigi2\"}}]");
		
		}
		else
		{
			System.out.println("tag not found");
		}
		return stringaInizio + stringafine;
	}

 
 
 
 
 private String repo(String cerca) {
		// TODO Auto-generated method stub
	 Label myLabel=GraphOperations.getInstance().getGraph().getAllLabels().iterator().next();

		String s = "[";
		ResourceIterator<Node> i=GraphOperations.getInstance().getGraph().findNodes(myLabel, "fullname", cerca);
		if (i.hasNext()) {
			int x = 0;
			int y = 0;
			while (i.hasNext()) {
				Node im = i.next();
				s = s + "{\"data\":{\"id\":\"" + im.getId() + 
						"\",\"user\":\"" + im.getProperty("user")
						+ "\",\"fulltag\":\"" + im.getProperty("fulltag")
						+ "\",\"name\":\"" + im.getProperty("name")
						+"\",\"tag\":\"" + im.getProperty("tag")
						+ "\",\"type\":\"child\"}, \"position\": { \"x\": " + x + ", \"y\": " + y
						+ " }},";
				x += 100;
				if (x == 800) {
					x = 100;
					y += 100;
				}
			}
			if (s.length() > 2) {
				s = s.substring(0, s.length() - 1);
			}
			s = s + "]";
			
		}
		return s;
	}
 
 
 private String user(String user) {
	 Label myLabel=GraphOperations.getInstance().getGraph().getAllLabels().iterator().next();

		ResourceIterator<Node> i=GraphOperations.getInstance().getGraph().findNodes(myLabel, "user", user);
			String s="[";
			List<String> repos=new ArrayList<String>();
			while(i.hasNext())
			{
				Node n=i.next();
				s+="{\""+(String)n.getProperty("name")+"\"},";
			}
			return s;
	}
 
 
 
 
}