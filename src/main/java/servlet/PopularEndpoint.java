package servlet;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import searcher.GraphOperations;
import data.Popular;

@Path("/popular")
public class PopularEndpoint {
	@GET	
	@Path("/")
	 @Produces("text/html")
	 public String getItem(@Context HttpServletRequest request, @Context HttpServletResponse response) {	 
		 HttpSession session = request.getSession(true);
		 Transaction t=GraphOperations.getInstance().getGraph().beginTx();
		 String s=popular();
		 t.success();
		 t.close();
		 response.setContentType("text/html");
		 return s;
	 }
	 private String popular() {
		 String s="";
		// TODO Auto-generated method stub
		 Iterable<Node> a = GraphOperations.getInstance().getGraph().getAllNodes();
			Iterator<Node> i = a.iterator();
			Popular[] m = new Popular[100];
			while (i.hasNext()) {
				Node im = i.next();
				int dim=(int) im.getProperty("nodeRank");
					Popular p = new Popular(im, dim);
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
			for (int j = 0; j < m.length; j++) {
				if (m[j] != null) {
					Node im = m[j].getI();

					s = s +"<a href=\"/rest/res/"+im.getProperty("user")+"/"+im.getProperty("name")+"/"+im.getProperty("tag")+"\""+">"+im.getProperty("fulltag")+"</a><br>";
				}
			}
		return s;
	}
}
