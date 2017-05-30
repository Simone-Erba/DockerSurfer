package servlet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.schema.Schema;

import data.Popular;


/**
 * Servlet implementation class Servlet1
 */
@WebServlet("/MainServlet")
public class MainServlet extends HttpServlet {
	@SuppressWarnings("deprecation")
	Label myLabel = DynamicLabel.label("Image");
	private static final long serialVersionUID = 1L;
	static GraphDatabaseService graph;
	Transaction t;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainServlet() {
        super();
        File f=new File("/data/neo4jdatabase");
        graph = new GraphDatabaseFactory().newEmbeddedDatabase( f );
		
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		 t=graph.beginTx();
		 System.out.println("ciao");
	/*	String st="/Docker/data.json"; 
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(st, "UTF-8");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedWriter bf = new BufferedWriter(writer);
*/
		
		HttpSession session = request.getSession(true);
		String tag=request.getParameter("tag");
		String cerca=request.getParameter("repo");
		String user=request.getParameter("user");
		String pop= request.getParameter("popular");
		//String query= request.getParameter("query");
		String query=null;
		if(query!=null)
		{
			List<String> list=new ArrayList<String>();
			ResourceIterable<Node> a=graph.getAllNodes();
			Iterator<Node> i2=a.iterator();
			while(i2.hasNext())
			{
				String s=(String) i2.next().getProperty("name");
				if(s.matches(".*"+query+".*"))
				{
					System.out.println(s);
				}
			}
			String res=list.toString();
			response.sendRedirect("./Repo.html?message="+res.substring(1).substring(0,res.length()-2));
		}
		String data=null;
		System.out.println("tag   "+tag);
		System.out.println("repo   "+cerca);
		System.out.println("user   "+user);
		System.out.println("popular   "+pop);
		if(pop!=null)
		{
			data="popular";
			
			Iterable<Node> a = graph.getAllNodes();
			Iterator<Node> i = a.iterator();
			Popular[] m = new Popular[100];
			while (i.hasNext()) {
				Node im = i.next();
				List<Node> c = getNodes(im,Direction.OUTGOING);
				if (c != null) {
					Popular p = new Popular(im, c.size());
					boolean finito = false;
					int j = 0;
					while (finito == false && j < m.length) {
						if (m[j] == null) {
							m[j] = p;
							finito = true;
						} else {
							if (m[j].getChildren() < p.getChildren()) {
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

			}
			String s = "[";
			int x = 0;
			int y = 0;
			for (int j = 0; j < m.length; j++) {
				if (m[j] != null) {
					Node im = m[j].getI();

					s = s + "{\"data\":{\"id\":\"" + im.getId() 
						+ "\",\"user\":\"" + im.getProperty("user")
						+ "\",\"fulltag\":\"" + im.getProperty("fulltag")
						+ "\",\"name\":\"" + im.getProperty("name")
						+"\",\"tag\":\"" + im.getProperty("tag")
							+"\",\"type\":\"child\"}, \"position\": { \"x\": " + x + ", \"y\": " + y + " }},";
					x += 100;
					if (x == 800) {
						x = 100;
						y += 100;
					}
				}
			}
			s = s.substring(0, s.length() - 1);
			s = s + "]";
		//	bf.write(s);
			session.setAttribute("object", s);	
			//response.setContentType("application/json");
			response.sendRedirect("./cyto.jsp");
				}
		else
		{
			boolean tagb=!(tag==null||tag.equals("")||tag.equals("null"));
			boolean cercab=!(cerca==null||cerca.equals("")||cerca.equals("null"));
			boolean userb=!(user==null||user.equals("")||user.equals("null"));
			if(tagb&&cercab&&userb)
			{
				String s=tag(user,cerca+":"+tag);
				session.setAttribute("object", s);				
				response.sendRedirect("./cyto.jsp");
			}
			else
			{
				if(cercab&&userb)
				{	
					String s=repo(user+"/"+cerca);
					session.setAttribute("object", s);					
					response.sendRedirect("./cyto.jsp");
				}
				else
				{
					if(userb)
					{
						
						user(user,response,session);
						
					}
				}
			}
		}
		//bf.close();
		//writer.flush();
		//writer.close();
		t.success();
		t.close();
	}

	private void user(String user, HttpServletResponse response, HttpSession session) {
		// TODO Auto-generated method stub
		ResourceIterator<Node> i=graph.findNodes(myLabel, "user", user);
		//while(i.hasNext())
		//{
			String s="[";
			List<String> repos=new ArrayList<String>();
			while(i.hasNext())
			{
				Node n=i.next();
				//repos.add("\""+n.getProperty("name")+"\"");
				s+="{\""+(String)n.getProperty("name")+"\"},";
			}
			/*JSONArray j = null;
			try {
				j = new JSONArray(repos.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String res=j.toString();*/
		        try {
		        	session.setAttribute("object", s);	
		        	response.sendRedirect("./Repo.jsp?user="+user);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		//	String res=repos.toString();
		        //call repo
		//}
	}

	private String repo(String cerca) {
		// TODO Auto-generated method stub
		String s = "[";
		ResourceIterator<Node> i=graph.findNodes(myLabel, "fullname", cerca);
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

	private String tag(String user,String tag) {
		// TODO Auto-generated method stub
		ResourceIterator<Node> it2=graph.findNodes(myLabel, "user", user);
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
		
		List<Node> f = getNodes(c,Direction.INCOMING);
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
		List<Node> set = getNodes(c,Direction.OUTGOING);
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	public List<Node> getNodes(Node n,Direction d)
	{
		List<Node> r=new ArrayList<Node>();
		Iterable<Relationship> l=n.getRelationships(d);
		Iterator<Relationship> i=l.iterator();
		while(i.hasNext())
		{
			Relationship e=i.next();
			Node node=e.getOtherNode(n);
			r.add(node);
		}
		return r;
	}
}
