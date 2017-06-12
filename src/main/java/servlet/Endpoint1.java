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
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;

import data.GraphOperations;

@Path("/res")
public class Endpoint1 {
 @GET
 @Path("{user}")
 @Produces("text/html")
 public String getItem(@PathParam("user") String user, @Context HttpServletRequest request, @Context HttpServletResponse response) {	 
	 Transaction t=GraphOperations.getInstance().getGraph().beginTx();
	 HttpSession session = request.getSession(true);
	 String s=user(user);
	 t.success();
	 t.close();
	 response.setContentType("text/html");
	 return s;
 }
 @GET
 @Path("{user}/{repo}")
 @Produces("text/html")
 public String getItem(@PathParam("user") String user,@PathParam("repo") String repo, @Context HttpServletRequest request, @Context HttpServletResponse response) {	 
	 Transaction t=GraphOperations.getInstance().getGraph().beginTx();
	 HttpSession session = request.getSession(true);
	 String s=repo(user+"/"+repo);
	 t.success();
	 t.close();
	 response.setContentType("text/html");
	 return s;
 }
 @GET
 @Path("{user}/{repo}/{tag}")
 @Produces("text/html")
 public String getItem(@PathParam("user") String user,@PathParam("repo") String repo,@PathParam("tag") String tag, @Context HttpServletRequest request, @Context HttpServletResponse response) {	 
	 HttpSession session = request.getSession(true);
	 Transaction t=GraphOperations.getInstance().getGraph().beginTx();
	 String s= tag(user,repo+":"+tag);
	 t.success();
	 t.close();
	 response.setContentType("text/html");
	 return s;
 }
 private String tag(String user,String tag) {
		// TODO Auto-generated method stub
		Index<Node> index=GraphOperations.getInstance().getGraph().index().forNodes("indexTag");
		IndexHits<Node> n=index.get("tag", user+"/"+tag);
		ResourceIterator<Node> ind=n.iterator();
		Node c=null;
		if(ind.hasNext())
		{
			c=ind.next();
		}
		String s="";
		/*boolean trovato=false;
		while(trovato==false&&it2.hasNext())
		{
			c=it2.next();
			if(c.getProperty("fulltag").equals(user+"/"+tag))
			{
				trovato=true;
			}
		}*/
		//if(c!=null&&trovato==true)
		if(c!=null)
		{
			List<Node> f = GraphOperations.getInstance().getNodes(c,Direction.INCOMING);
			if(!f.isEmpty())
			{
			Node father=f.iterator().next();
			s=s+"<h1>Father</h1><br>";
			s=s+"<a href=\"/DockerSurferWebApp/rest/res/"+father.getProperty("user")+"/"+father.getProperty("name")+"/"+father.getProperty("tag")+"\">"+father.getProperty("fulltag")+"</a><br>";
			}
			else
			{
				s=s+"<h1>No father</h1><br>";
			}
		
		//poi dopo lui chiama un altro rest che restituisce il json giusto
		List<Node> set = GraphOperations.getInstance().getNodes(c,Direction.OUTGOING);
		s=s+"<h1>Node</h1>";
		s=s+"nodeRank:"+c.getProperty("nodeRank")+"<br>";
		s=s+"betweeness:"+c.getProperty("betweeness")+"<br>";
		if(set.size()<50)
		{

			s=s+"<a href=../../../../cyto.jsp?name="+c.getProperty("fulltag")+"&param=tag"+">draw the graph</a><br>";

			s=s+"<a href=../../../../cyto.jsp?name="+c.getProperty("fulltag")+"&param=pagerank>view page rank</a><br>";
			s=s+"<a href=../../../../cyto.jsp?name="+c.getProperty("fulltag")+"&param=betweeness>view node betweeness</a><br>";
		}
		if (!set.isEmpty()) {
			Iterator<Node> it = set.iterator();
			s=s+"<h1>Children</h1>";
			while (it.hasNext()) {
				Node i = it.next();
				s=s+"<a href=\"/DockerSurferWebApp/rest/res/"+i.getProperty("user")+"/"+i.getProperty("name")+"/"+i.getProperty("tag")+"\">"+i.getProperty("fulltag")+"</a>    betweeness: "+i.getProperty("betweeness").toString()+"   page rank:"+i.getProperty("nodeRank").toString()+"<br>";
				
		}
		}
			else
			{
				s=s+"<br><h1>no children</h1>";
			}
		// {\"data\":{\"id\":\"gigi2\",\"name\":\"nomegigi2\"}},{\"data\":{\"id\":\"q\",\"source\":\"gigi\",\"target\":\"gigi2\"}}]");
		
		}
		else
		{
			s=s+"tag not found";
		}
		return s;
	}

 private String repo(String cerca) {
		// TODO Auto-generated method stub
	 Index<Node> index=GraphOperations.getInstance().getGraph().index().forNodes("indexRepo");
		IndexHits<Node> n=index.get("repo", cerca);
		ResourceIterator<Node> i=n.iterator();
		String s = "";
		if (i.hasNext()) {
			s=s+"<h1>tags</h1>";
			while (i.hasNext()) {
				Node im = i.next();
				
				s=s+"<a href=\"/DockerSurferWebApp/rest/res/"+im.getProperty("user")+"/"+im.getProperty("name")+"/"+im.getProperty("tag")+"\">"+im.getProperty("fulltag")+"</a><br>";

			}
			
		}
		return s;
	}
 
 
 private String user(String user) {
		Index<Node> index=GraphOperations.getInstance().getGraph().index().forNodes("indexUser");
		IndexHits<Node> nodes=index.get("user", user);
		ResourceIterator<Node> i=nodes.iterator();
			String s="";
			List<String> repos=new ArrayList<String>();
			while(i.hasNext())
			{
				Node n=i.next();
				if(!repos.contains(n.getProperty("fullname")))
				{
					repos.add(n.getProperty("fullname").toString());
					s=s+"<a href=\"\\DockerSurferWebApp\\rest\\res\\"+n.getProperty("user")+"\\"+n.getProperty("name")+"\">"+n.getProperty("fullname")+"</a><br>";
				}
			}
			return s;
	}
}