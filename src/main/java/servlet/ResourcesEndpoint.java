package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
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

import org.json.JSONObject;
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
 /**
  * Call the backend and forward the request to jsp with the answer
  * @param user User name
  * @param request
  * @param response
  */
 public void getUser(@PathParam("user") String user, @Context HttpServletRequest request, @Context HttpServletResponse response) {	 
	 Transaction t=GraphOperations.getInstance().getGraph().beginTx();
	 Backend b=new Backend();
	 String s=b.getUser(user);
	 t.success();
	 t.close();
	// String s=user(user);
	 JSONObject j=new JSONObject(s);
	 if(j.getInt("code")==404)
	 {
		 request.setAttribute("message", "\""+j.getString("error").replace("\"", "\\\"")+"\"");
		 RequestDispatcher dispatcher =
					request.getRequestDispatcher("/error.jsp");
					try {
						dispatcher.forward(request, response);
					} catch (ServletException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	 }
	 else
	 {
		 if(j.getInt("code")==200)
		 {
			 response.setCharacterEncoding("UTF-8");
				request.setAttribute("message", "\""+j.getJSONObject("data").toString().replace("\"", "\\\"")+"\"");
				RequestDispatcher dispatcher =
							request.getRequestDispatcher("/printUser.jsp");
							try {
								dispatcher.forward(request, response);
							} catch (ServletException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} 
		 }
	 }
	
 }
 @GET
 @Path("{user}/{repo}")
 @Produces("text/html")
 /**
  * Call the backend and forward the request to jsp with the answer
  * @param user User name
  * @param repo Repository name
  * @param request
  * @param response
  */
 public void getRepository(@PathParam("user") String user,@PathParam("repo") String repo, @Context HttpServletRequest request, @Context HttpServletResponse response) {	 
	 Transaction t=GraphOperations.getInstance().getGraph().beginTx();
	 Backend b=new Backend();
	 String s=b.getRepository(user+"/"+repo);
	 t.success();
	 t.close();
	 JSONObject j=new JSONObject(s);
	 if(j.getInt("code")==404)
	 {
		 request.setAttribute("message", "\""+j.getString("error").replace("\"", "\\\"")+"\"");
		 RequestDispatcher dispatcher =
					request.getRequestDispatcher("/error.jsp");
					try {
						dispatcher.forward(request, response);
					} catch (ServletException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	 }
	 else
	 {
		 if(j.getInt("code")==200)
		 {
			 response.setCharacterEncoding("UTF-8");
				request.setAttribute("message", "\""+j.getJSONObject("data").toString().replace("\"", "\\\"")+"\"");
				RequestDispatcher dispatcher =
							request.getRequestDispatcher("/printRepo.jsp");
							try {
								dispatcher.forward(request, response);
							} catch (ServletException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} 
		 }
	 }
 }
 @GET
 @Path("{user}/{repo}/{tag}")
 @Produces("text/html")
 /**
  * Call the backend and forward the request to jsp with the answer
  * @param user User name
  * @param repo Repository name
  * @param tag Tag name
  * @param request
  * @param response
  */
 public void getTag(@PathParam("user") String user,@PathParam("repo") String repo,@PathParam("tag") String tag, @Context HttpServletRequest request, @Context HttpServletResponse response) {	 
	 Transaction t=GraphOperations.getInstance().getGraph().beginTx();
	 Backend b=new Backend();
	 String s=b.getTag(user+"/"+repo+":"+tag);
	 t.success();
	 t.close();
	 JSONObject j=new JSONObject(s);
	 if(j.getInt("code")==404)
	 {
		 request.setAttribute("message", "\""+j.getString("error").replace("\"", "\\\"")+"\"");
		 RequestDispatcher dispatcher =
					request.getRequestDispatcher("/error.jsp");
					try {
						dispatcher.forward(request, response);
					} catch (ServletException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	 }
	 else
	 {
		 if(j.getInt("code")==200)
		 {
			 response.setCharacterEncoding("UTF-8");
				request.setAttribute("message", "\""+j.getJSONObject("data").toString().replace("\"", "\\\"")+"\"");
				RequestDispatcher dispatcher =
							request.getRequestDispatcher("/printTag.jsp");
							try {
								dispatcher.forward(request, response);
							} catch (ServletException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} 
		 }
	 }
}
}