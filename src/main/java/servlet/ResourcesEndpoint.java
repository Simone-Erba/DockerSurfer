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
import javax.ws.rs.core.Response;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;

import data.Backend;
import searcher.GraphOperations;

@Path("/res")
public class ResourcesEndpoint {
 @GET
 @Path("{user}")
 public void getItem(@PathParam("user") String user, @Context HttpServletRequest request, @Context HttpServletResponse response) {	 
	 Transaction t=GraphOperations.getInstance().getGraph().beginTx();
	 Backend b=new Backend();
	 String s=b.getUser(user);
	// String s=user(user);
	 t.success();
	 t.close();
	 //divide into pages
	 request.setAttribute("message", s);
	 try {
		response.sendRedirect("/DockerSurferWebApp/print.jsp");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
 }
 @GET
 @Path("{user}/{repo}")
 @Produces("text/html")
 public String getItem(@PathParam("user") String user,@PathParam("repo") String repo, @Context HttpServletRequest request, @Context HttpServletResponse response) {	 
	 Transaction t=GraphOperations.getInstance().getGraph().beginTx();
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
		if(c!=null)
		{
			List<Node> f = GraphOperations.getInstance().getNodes(c,Direction.INCOMING);
			if(!f.isEmpty())
			{
			Node father=f.iterator().next();
			s=s+"<h1>Derived By</h1><br>";
			s=s+"<a href=\"/rest/res/"+father.getProperty("user")+"/"+father.getProperty("name")+"/"+father.getProperty("tag")+"\">"+father.getProperty("fulltag")+"</a><br>";
			}
			else
			{
				s=s+"<h1>Derived from Anyone</h1><br>";
			}
				
		List<Node> set = GraphOperations.getInstance().getNodes(c,Direction.OUTGOING);
		
		s=s+"<h1>Node</h1>";
		s=s+"updated on : "+c.getProperty("date")+"<br>";
		s=s+"nodeRank:"+c.getProperty("nodeRank")+"<br>";
		s=s+"betweeness:"+c.getProperty("betweeness")+"<br>";
		if(set.size()<50)
		{
			s=s+"<a href=../../../../cyto.jsp?name="+c.getProperty("fulltag")+"&param=tag"+">draw the graph</a><br>";
			s=s+"<a href=../../../../cyto.jsp?name="+c.getProperty("fulltag")+"&param=pagerank>view page rank</a><br>";
			s=s+"<a href=../../../../cyto.jsp?name="+c.getProperty("fulltag")+"&param=betweeness>view node betweeness</a><br>";
		}
		if(set.size()==0)
		{
			s=s+"<h1>not used by anyone</h1>";
		}
		else
		{
			if(set.size()>5000)
			{
				s=s+"<h3>This image has too many images that use it. Showing all of them will be expensive. This functionality will be added in future.</h3>";
			}
			else {
				Iterator<Node> it = set.iterator();
				s=s+"<h1>Used by</h1>";
				boolean table=false;
				if(it.hasNext())
				{
					s=s+"<table style=\"width:100%\"><tr><th align=\"left\">name</th><th align=\"left\">betweeness</th><th align=\"left\">node rank</th><th align=\"left\">last updated</th><th align=\"left\">docker hub</th><th align=\"left\">image layers</th></tr>";
					table=true;
				}
				while (it.hasNext()) {
					Node i = it.next();
					String imlay="<a href=\"https://imagelayers.io/?images="+i.getProperty("user")+"%2F"+i.getProperty("name")+":"+i.getProperty("tag")+"\">image layers</a>";
					String link="<a href=\"https://hub.docker.com/r/"+i.getProperty("user")+"/"+i.getProperty("name")+"\">docker hub</a>";
					s=s+"<tr><td><a href=\"/rest/res/"+i.getProperty("user")+"/"+i.getProperty("name")+"/"+i.getProperty("tag")+"\">"+i.getProperty("fulltag")+"</a></td><td>"+i.getProperty("betweeness").toString()+"</td><td>"+i.getProperty("nodeRank").toString()+"</td><td>"+i.getProperty("date").toString()+"</td><td>"+link+"</td><td>"+imlay+"</td></tr>";				
			}
				if(table==true)
				{
				s=s+"</table>";
				}
			}

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
		String s="<table style=\"width:70%\"><tr><th align=\"left\">name</th><th align=\"left\">node rank</th></tr>";
		if (i.hasNext()) {
			s=s+"<h1>tags</h1>";
			while (i.hasNext()) {
				Node im = i.next();
				s=s+"<tr><td><a href=\"/rest/res/"+im.getProperty("user")+"/"+im.getProperty("name")+"/"+im.getProperty("tag")+"\">"+im.getProperty("fulltag")+"</a></td><td>"+im.getProperty("nodeRank")+"</td></tr>";
			}
			
		}
		return s+"</table>";
	}
 
 
 private String user(String user) {
		Index<Node> index=GraphOperations.getInstance().getGraph().index().forNodes("indexUser");
		System.out.println("index: "+index);
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
					s=s+"<a href=\"/rest/res/"+n.getProperty("user")+"/"+n.getProperty("name")+"\">"+n.getProperty("fullname")+"</a><br>";
				}
			}
			System.out.println("finish");
			return s;
	}
}