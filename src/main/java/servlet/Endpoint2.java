package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
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
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;

import data.GraphOperations;

@Path("/json")
public class Endpoint2 {
 @GET
 @Path("{fulltag}")
 @Produces("application/json")
 public String getItem(@PathParam("fulltag") String fulltag, @Context HttpServletRequest request)
 {
	 String fixed=fulltag.replaceAll("replacementforbackslash", "/");
	 Transaction t=GraphOperations.getInstance().getGraph().beginTx();
	 Index<Node> index=GraphOperations.getInstance().getGraph().index().forNodes("indexTag");
	 System.out.println("oh");
	 System.out.println(fixed);
	 IndexHits<Node> n=index.get("tag", fixed);
	 ResourceIterator<Node> ind=n.iterator();
	 String stringaInizio = "[";
	 String stringafine = "]";
	 if(ind.hasNext())
	 {
		 System.out.println("siiiiiiiiiiii");
		Node c=ind.next();
		stringaInizio = stringaInizio + "{\"data\":{\"id\":\"" + c.getId() + "\",\"user\":\"" + c.getProperty("user")
		+ "\",\"name\":\"" + c.getProperty("name")
		+ "\",\"fulltag\":\"" + c.getProperty("fulltag")
		+ "\",\"pageRank\":\"" + c.getProperty("nodeRank")
		+ "\",\"betweeness\":\"" + c.getProperty("betweeness")
		+"\",\"tag\":\"" + c.getProperty("tag")
				+"\",\"type\":\"searched\"}, \"position\": { \"x\": 400, \"y\": 200 }}";
		
		List<Node> f = GraphOperations.getInstance().getNodes(c,Direction.INCOMING);
		if(!f.isEmpty())
		{
		Node father=f.iterator().next();
			stringaInizio = stringaInizio + ",{\"data\":{\"id\":\"" + father.getId() + "\",\"user\":\"" + father.getProperty("user")
			+ "\",\"name\":\"" + father.getProperty("name")
			+ "\",\"fulltag\":\"" + father.getProperty("fulltag")
			+ "\",\"pageRank\":\"" + father.getProperty("nodeRank")
			+ "\",\"betweeness\":\"" + father.getProperty("betweeness")
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
				+ "\",\"pageRank\":\"" + i.getProperty("nodeRank")
				+ "\",\"betweeness\":\"" + i.getProperty("betweeness")
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
		}
		else
		{
			System.out.println("tag not found");
		}
		t.success();
		t.close();
		return stringaInizio + stringafine;
 }
 @GET
 @Path("pr/{fulltag}")
 @Produces(MediaType.TEXT_PLAIN)
 public String getItem2(@PathParam("fulltag") String fulltag, @Context HttpServletRequest request)
 {
	 
	 return "";
 }
 @GET
 @Path("btw/{fulltag}")
 @Produces(MediaType.TEXT_PLAIN)
 public String getItem3(@PathParam("image") String user, @Context HttpServletRequest request)
 {
	 
	 return "";
 }
}
