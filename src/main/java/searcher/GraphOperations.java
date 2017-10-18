package searcher;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

//import org.mapdb.DB;
//import org.mapdb.DBMaker;
import javax.imageio.event.IIOReadProgressListener;

import org.json.JSONArray;
import org.json.JSONObject;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import data.IdManager;
import data.Relazione;

enum MyRelationshipTypes implements RelationshipType
{
    DEPENDENCY
}
@SuppressWarnings("deprecation")
public class GraphOperations {
	ConcurrentMap map;
	//DB db;
	Users s;
	IdManager id;
	LoggerUpdater log;
	static GraphDatabaseService graph;
	private static GraphOperations istanza = null;
	//static Transaction t;
	@SuppressWarnings("deprecation")
	Label myLabel = DynamicLabel.label("Image");
	static Direction[] directions=Direction.values();//BOTH IN OUT
	
	private GraphOperations(String path) {
		super();
		File f=new File(path);
		graph = new GraphDatabaseFactory().newEmbeddedDatabase( f );
		//t=graph.beginTx();
		//db = DBMaker.memoryDB().make();
		//map= db.hashMap("map").createOrOpen();
	}
	public ConcurrentMap<String, Node> getMap() {
		return map;
}
	public void setS(Users s) {
		this.s = s;
	}

	public static synchronized GraphOperations getInstance() {
		if (istanza == null) {
			istanza = new GraphOperations("/data/neo4jdatabase");
			///data/neojdatabase
			//t=graph.beginTx();
			}
		return istanza;
	}
	/*public synchronized void newT()
	{
		t.success();
		t.close();
		t=graph.beginTx();
	}*/


	/*public Transaction getT() {
		return t;
	}*/

	public GraphDatabaseService getGraph() {
		return graph;
	}
	public Node cercaFigli(Node v, Node figlio)
	{
		//System.out.println("                 CERCA FIGLI");
		//System.out.println(v.getProperty("value").toString());
		//System.out.println(figlio.getProperty("value").toString());
		Iterable<Node> possibiliFigli=getNodes(figlio,directions[1]);
		
		//figlio.getVertices(directions[1], "dependency");
		Iterator<Node> possibiliPadriIterator=possibiliFigli.iterator();
		while(possibiliPadriIterator.hasNext())
		{
			Node pp=possibiliPadriIterator.next();
			Relazione r=relazione(extract(pp.getProperty("layers").toString()),extract(v.getProperty("layers").toString()));
			//System.out.println(pp.getProperty("value").toString());
			if(v.getProperty("layers").toString().equals(pp.getProperty("layers").toString()))//se sono arrivato alla stessa immagine
			{
				return figlio;
			}
			if(r!=null&&r.isFiglio())//i è figlio
			{
				return cercaFigli(v,pp);	
			}	
		}
		return figlio;
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
	public Node cercaPadre(Node v, Node padre, List<Node> l)
	{
	if(l.contains(padre))
		{
			System.out.println("aborted..");
			Iterator<Node> iter=l.iterator();
			Node del=null;
			while(iter.hasNext())
			{
				del=iter.next();
			}
			
			if(del.hasRelationship(Direction.INCOMING))
			{
				this.delete(del);
				l.remove(del);
				cercaPadre(v,padre,l);
			}
			else
			{
				return null;
			}
			
		}
		l.add(padre);
		//System.out.println("                 CERCA padre");
		//System.out.println(v.getProperty("value").toString());
		//System.out.println(padre.getProperty("value").toString());
			List<Node> possibiliPadri=getNodes(padre,Direction.OUTGOING);
		//padre.getVertices(directions[2], "dependency");
		Iterator<Node> possibiliPadriIterator=possibiliPadri.iterator();
		while(possibiliPadriIterator.hasNext())
		{
			Node pp=possibiliPadriIterator.next();
			Relazione r=relazione(extract(pp.getProperty("layers").toString()),extract(v.getProperty("layers").toString()));
			//System.out.println(pp.getProperty("value").toString());
			if(v.getProperty("layers").toString().equals(pp.getProperty("layers").toString()))//se sono arrivato alla stessa immagine
			{
				return padre;
			}
			if(r!=null&&r.isPadre())//i è figlio
			{
				return cercaPadre(v,pp,l);	
			}
		}
		return padre;
	}
	public List<String> extract(String s) 
	{
		List<String> list = Arrays.asList(s.toString().split("\\s*,\\s*"));
		//List<Integer> l=new ArrayList<Integer>();
		Iterator<String> it=list.iterator();
		int count=0;
		while(it.hasNext())
		{
			String a=it.next();
			if(count==0)
			{
				int index=a.length();
				a=a.substring(1, index);
				list.set(0, a);
			}
			if(count==list.size()-1)
			{
				int index=a.length();
				a= a.substring(0, index-1);
				list.set(count,a);
			}
			count++;
		}
		return list;
	}
	public void pulisci()
	{
		Iterable<Relationship> l2= graph.getAllRelationships();
		Iterator<Relationship> i=l2.iterator();
		while(i.hasNext())
		{
			Relationship v=i.next();
			v.delete();
		}
				Iterable<Node> l= graph.getAllNodes();
				Iterator<Node> i2=l.iterator();
				while(i2.hasNext())
				{
					Node v=i2.next();
					v.delete();
				}
	}
	public void stampa()
	{
		//STAMPA ARCHI	
				Iterable<Node> l= graph.getAllNodes();
				Iterator<Node> i2=l.iterator();
				//System.out.println("VERTICI");
				while(i2.hasNext())
				{
					Node v=i2.next();
				//	System.out.println(v.getProperty("value"));
				}
				//STAMPA VERTICI	
				Iterable<Relationship> l2= graph.getAllRelationships();
				Iterator<Relationship> i=l2.iterator();
				//System.out.println("ARCHI");
				while(i.hasNext())
				{
					Relationship v=i.next();
					//System.out.println(v.getProperty("value"));
				}
	}
	/*public void close()
	{
		System.out.println("s1");
		t.success();
		System.out.println("s2");
		t.close();
		System.out.println("s3");
	}*/
	
	public List<String> removeLastFromList(List<String> l) {
		List<String> l2=new ArrayList<String>();
		Iterator<String> i=l.iterator();
		for(int j=0;j<l.size()-1;j++)
		{
			l2.add(i.next());
		}
		return l2;
	}

	
	public static Relazione relazione(List<String> a,List<String> b) 
	{
		Relazione r;
		int layers=0;
		Iterator<String> it1=a.iterator();
		Iterator<String> it2=b.iterator();
		boolean diversi=false;
		while(diversi==false&&it1.hasNext()&&it2.hasNext())
		{
			if(it1.next().equals(it2.next()))
			{
				layers++;
			}
			else
			{
				diversi=true;
			}
		}
		if(layers==0)
		{
			return null;
		}
		//fratelli
		if(diversi==true)//sono fratelli
		{
			r=new Relazione(layers,false,false,true);
			return r;//chiamare sul padre di i
		}
		if(!it2.hasNext()&&!it1.hasNext())
		{
			r=new Relazione(layers,false,false,true);
			return r;
		}
		if(!it2.hasNext())
		{
			//this è il figlio
			r=new Relazione(layers,false,true,false);
			return r;//chiamare sul padre di i
		}
		else
		{
				//padre
				r=new Relazione(layers,true,false,false);
				return r;
			
		}
	}
	public void insertSingle(String name, String surname, String tag, String date, JSONArray layers2, JSONArray history) {
		ConcurrentLinkedQueue<Node> roots=s.getRoots();
		// TODO Auto-generated method stub
		List<String> layers=new ArrayList<String>();
		List<String> story=new ArrayList<String>();
		if(layers2.length()>0)
		{
		for(int i=layers2.length()-1;i>=0;i--)
		{
			String a=layers2.getJSONObject(i).getString("blobSum");
			layers.add(a);
			//System.out.println(a);
		}
		for(int i=history.length()-1;i>=0;i--)
		{
			String a=history.getJSONObject(i).getString("v1Compatibility");
			story.add(a);
			//System.out.println(a);
		}
		Node n=this.getGraph().createNode();
		n.setProperty("tag", tag);
		n.setProperty("date", date);
		n.setProperty("name", surname);
		n.setProperty("user", name);
		n.setProperty("fullname", name+"/"+surname);
		n.setProperty("fulltag", name+"/"+surname+":"+tag);
		n.setProperty("layers", layers.toString());
		n.setProperty("history", story.toString());
		n.setProperty("nodeRank", 0);
		n.setProperty("betweeness", -1);
		n.addLabel(myLabel);
		String i=layersToInts(layers.toString());
		List<String> l=extract(i);
		boolean finito=false;
		//FIND FATHER
		Node padre=null;
		while(l.size()>0&&finito==false)
		{
			if(map.get(l.toString())!=null)
			{
				padre=(Node) map.get(l.toString());
				log.getInstance().write("father is "+padre.getProperty("fulltag"));
				this.getMap().putIfAbsent(l.toString(), padre);
				finito=true;
			}
			else
			{
				l=removeLastFromList(l);
			}
		}
		//FIND CHILDREN
		if(padre==null)
		{
			log.getInstance().write("didnt find a father for: "+n.getProperty("fulltag"));
			Iterator<Node> iter2=roots.iterator();
			while(iter2.hasNext())
			{
				Node node=iter2.next();
				Relazione r=relazione(extract(node.getProperty("layers").toString()),extract(n.getProperty("layers").toString()));
				if(r!=null&&r.isFiglio())
				{
					n.createRelationshipTo(node, MyRelationshipTypes.DEPENDENCY);
					String a=layersToInts(node.getProperty("layers").toString());
					this.getMap().put(a, n);
					s.remove(node);
					log.getInstance().write("CREATED RELATIONSHIP from "+n.getProperty("fulltag")+"TO "+node.getProperty("fulltag"));
				}
			}
			s.add(n);
		}
		else
		{
			List<Node> figli=getNodes(padre,directions[2]);
			Iterator<Node> i4=figli.iterator();
			Node figlio=null;
			while(i4.hasNext())
			{
				Node node=i4.next();
				Relazione r=relazione(layers,extract(node.getProperty("layers").toString()));
				if(r!=null&&r.isFiglio())
				{
					figlio=cercaFigli(n,node);
					if(figlio.hasRelationship(directions[1]))
					{
						figlio.getSingleRelationship(MyRelationshipTypes.DEPENDENCY, directions[1]).delete();
					}
					Relationship arco =n.createRelationshipTo(figlio,  MyRelationshipTypes.DEPENDENCY);
					arco.setProperty("value", "dependency");
					String a=layersToInts(node.getProperty("layers").toString());
					this.getMap().put(a, n);
				}
				
			}
			Relationship arco2 =padre.createRelationshipTo(n, MyRelationshipTypes.DEPENDENCY);
			arco2.setProperty("value", "dependency");
			log.getInstance().write("INSERTED "+name+"    "+tag);
		}
		}
	}
	public void delete(Node n) {
		// TODO Auto-generated method stub
		Node padre=getNodes(n,Direction.INCOMING).iterator().next();
		if(padre!=null)
		{
			Relationship d=n.getSingleRelationship( MyRelationshipTypes.DEPENDENCY, Direction.INCOMING);
			d.delete();
		}
		List<Node> figli=getNodes(n,Direction.OUTGOING);
		Iterable<Relationship> rel=n.getRelationships(Direction.OUTGOING);
		Iterator<Relationship> itrel=rel.iterator();
		while(itrel.hasNext())
		{
			itrel.next().delete();
		}
		Iterator<Node> it2=figli.iterator();
		while(it2.hasNext())
		{
			Relationship r=padre.createRelationshipTo(it2.next(),MyRelationshipTypes.DEPENDENCY);
			r.setProperty("value", "dependency");
		}
		n.delete();
	}
	//Convert layers to list of numbers, the convert list of numbers in string
	public void insertMap(Node im, Node padre) {
		// TODO Auto-generated method stub
		String layers=im.getProperty("layers").toString();
		
		String str=layersToInts(layers);
		this.getMap().putIfAbsent(str, padre);
	}
	private String layersToInts(String layers)
	{
		List<String> s=extract(layers);
		Iterator<String> i=s.iterator();
		List<Integer> li=new ArrayList<Integer>();
		while(i.hasNext())
		{
			String st=i.next();
			Integer in=new Integer(id.getNumero(st));
			li.add(in);
		}
		Iterator<Integer> i2=li.iterator();
		String str="";
		while(i2.hasNext())
		{
			str+=i2.next();
			if(i2.hasNext())
			{
				str=str+";";
			}
		}
		return str;
	}
}