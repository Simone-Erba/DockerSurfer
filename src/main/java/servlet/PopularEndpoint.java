package servlet;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.json.JSONObject;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import searcher.GraphOperations;
import data.Backend;
import data.Popular;
import data.Tag;

@Path("/popular")
public class PopularEndpoint {
	@GET
	@Path("/")
	@Produces("text/html")
	public void Popular(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		Transaction t = GraphOperations.getInstance().getGraph().beginTx();
		Backend b = new Backend();
		String s = b.popular();
		t.success();
		t.close();
		JSONObject j = new JSONObject(s);
		if (j.getInt("code") == 404) {
			request.setAttribute("message", "\"" + j.getString("error").replace("\"", "\\\"") + "\"");
			RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
			try {
				dispatcher.forward(request, response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			if (j.getInt("code") == 200) {
				response.setCharacterEncoding("UTF-8");
				request.setAttribute("message", "\"" + j.getJSONArray("data").toString().replace("\"", "\\\"") + "\"");
				RequestDispatcher dispatcher = request.getRequestDispatcher("/popular.jsp");
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
