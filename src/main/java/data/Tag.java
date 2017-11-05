package data;

import java.util.Date;
import java.util.List;
/**
 * 
 * @author Simone-Erba
 *
 *a data class for represent a Docker Tag
 */
public class Tag {
	String name;
	String d;
	int pagerank;
	int betweeness;
	/**
	 * The link on the Dockerhub
	 */
	String dockerhub;
	/**
	 * The link on the imagelayers
	 */
	String imagelayers;
	Tag father;
	List<Tag> children;
	/**
	 * The link to the cytoscape representation of the dependencies
	 */
	String cyto;

	public Tag(String name, String d, int pagerank, int betweeness, Tag father,
			List<Tag> children) {
		super();
		this.name = name;
		this.d = d;
		this.pagerank = pagerank;
		this.betweeness = betweeness;
		this.dockerhub="http:\\";
		this.imagelayers = "http:\\";
		this.father = father;
		this.children = children;
		this.cyto="../../../../cyto.jsp?name="+name;
	}
	
	
}
