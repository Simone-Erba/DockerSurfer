package servlet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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

import data.Popular;


/**
 * Servlet implementation class Servlet1
 */
@WebServlet("/MainServlet")
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static GraphDatabaseService graph;
	Transaction t;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainServlet() {
        super();
        File f=new File("E:/neo4jdatabase");
        graph = new GraphDatabaseFactory().newEmbeddedDatabase( f );
		
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		 t=graph.beginTx();
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("E:/DockerSurferWebApp/WebContent/data.json", "UTF-8");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedWriter bf = new BufferedWriter(writer);
		@SuppressWarnings("deprecation")
		Label myLabel = DynamicLabel.label("Image");
		
		HttpSession session = request.getSession(true);
		String tag=request.getParameter("tag");
		String cerca=request.getParameter("repo");
		String user=request.getParameter("user");
		String pop= request.getParameter("popular");
		//String query= request.getParameter("query");
		String query=null;
		ResourceIterable<Node> a3=graph.getAllNodes();
		Iterator<Node> i3=a3.iterator();
		while(i3.hasNext())
		{
			System.out.println(i3.next().getId());
		}
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

					s = s + "{\"data\":{\"id\":\"" + im.getId() + "\",\"tag\":\"" + im.getProperty("tag")
							+ "\",\"type\":\"child\"}, \"position\": { \"x\": " + x + ", \"y\": " + y + " }},";
					x += 100;
					if (x == 800) {
						x = 100;
						y += 100;
					}
				}
			}
			s = s.substring(0, s.length() - 1);
			s = s + "]";
			bf.write(s);
			response.sendRedirect("./cyto.html");
		}
		else
		{
			if(!(tag==null||tag.equals("")||tag.equals("null")))
			{
				data=tag;
				Node c=graph.findNode(myLabel, "tag", tag);
				String stringaInizio = "[";
				String stringafine = "]";
				stringaInizio = stringaInizio + "{\"data\":{\"id\":\"" + c.getId() + "\",\"tag\":\""
						+ c.getProperty("tag") + "\",\"type\":\"searched\"}, \"position\": { \"x\": 400, \"y\": 200 }}";
				
				List<Node> f = getNodes(c,Direction.INCOMING);
				if(!f.isEmpty())
				{
				Node father=f.iterator().next();
					stringaInizio = stringaInizio + ",{\"data\":{\"id\":\"" + father.getId() + "\",\"tag\":\""
							+ father.getProperty("tag")
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

						stringaInizio = stringaInizio + ",{\"data\":{\"id\":\"" + i.getId() + "\",\"tag\":\""
								+ i.getProperty("tag") + "\",\"type\":\"child\"}, \"position\": { \"x\": " + x
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
				bf.write(stringaInizio + stringafine);
				response.sendRedirect("./cyto.html");
			}
			else
			{
				if(!(cerca==null||cerca.equals("")||cerca.equals("null")))
				{
					data=cerca;
					
					ResourceIterator<Node> i=graph.findNodes(myLabel, "name", cerca);
					if (i.hasNext()) {
						int x = 0;
						int y = 0;
						String s = "[";
						while (i.hasNext()) {
							Node im = i.next();
							s = s + "{\"data\":{\"id\":\"" + im.getId() + "\",\"tag\":\"" + im.getProperty("tag")
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
						bf.write(s);
						response.sendRedirect("./cyto.html");
					}
				}
				else
				{
					data=user;
					
					ResourceIterator<Node> i=graph.findNodes(myLabel, "user", user);
					//while(i.hasNext())
					//{
						List<String> repos=new ArrayList<String>();
						while(i.hasNext())
						{
							Node n=i.next();
							//repos.add("\""+n.getProperty("name")+"\"");
							repos.add((String)n.getProperty("name"));
						}
						//rmove duplicates
						Set<String> hs = new HashSet<String>();
						hs.addAll(repos);
						repos.clear();
						repos.addAll(hs);
						System.out.println(repos.toString());
						/*JSONArray j = null;
						try {
							j = new JSONArray(repos.toString());
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						String res=j.toString();*/
					       Scanner scan = new Scanner(repos.toString());

					        String result = "";
					        while(scan.hasNext()) {
					            result += scan.next();
					        }
					        String res="";
					        res=result;
					        res = res.replaceAll("\"","");
					//	String res=repos.toString();
					        //call repo
					        response.sendRedirect("./Repo.html?message="+res.substring(1).substring(0,res.length()-2));
					//}
				}
			}
		}
		System.out.println("data   "+data);
		/*System.out.println(cerca);
		String a=" { \"nodes\":[{ \"value\":\"a\",\"type\": \"image\",\"id\": 1 }, \"type\": \"image\", \"id\": 2}],\"edges\": [[ {\"source\": 1,\"target\": 2,\"caption\": \"DEPENDENCY\" }]}";
		Genson genson = new Genson();
		File f=new File("E:\\a");
		graph = new GraphDatabaseFactory().newEmbeddedDatabase( f );
		Transaction t=graph.beginTx();
		Iterable<Node> o=graph.getAllNodes();
		List<Map<String, Object>> person = new ArrayList<Map<String, Object>>();
		Map<String, Object> m=new HashMap<String, Object>();
		m.put("name", "Foo");
		m.put("age", 28);
		person.add(m);
		String l=genson.serialize(person);
		System.out.println(l);
		//SpringApplication.run(Application.class, args);
		t.success();
		t.close();
		graph.shutdown();*/
		//SpringApplication.run(Application.class,data);
		//FARE JSON
		/*if(pass==true)
		{
			String content = "";
			final String FILENAME = "E:/workspace/Docker_Surfer/src/main/webapp/data.json";


				BufferedReader br = null;
				FileReader fr = null;

				try {

					fr = new FileReader(FILENAME);
					br = new BufferedReader(fr);

					String sCurrentLine;
					
					br = new BufferedReader(new FileReader(FILENAME));

					while ((sCurrentLine = br.readLine()) != null) {
						content+=sCurrentLine;
					}

				} catch (IOException e) {

					e.printStackTrace();

				} finally {

					try {

						if (br != null)
							br.close();

						if (fr != null)
							fr.close();

					} catch (IOException ex) {

						ex.printStackTrace();

					}

				}
				PrintWriter out = response.getWriter();
				System.out.println(content);
			response.sendRedirect("./Repo.html?message="+content);
		}
		else
		{
			response.sendRedirect("./cyto.html");
		}*/
		//session.setAttribute("json", content);
		
		/*request.setAttribute("message", l);
		RequestDispatcher dispatcher =
				getServletContext().getRequestDispatcher("/cyto.html");
				dispatcher.forward(request, response);*/
		bf.close();
		writer.flush();
		writer.close();
		t.success();
		t.close();
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
