package data;

import java.util.Date;
import java.util.List;

public class Tag {
	String name;
	String d;
	int pagerank;
	int betweeness;
	String dockerhub;
	String imagelayers;
	Tag father;
	List<Tag> children;
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
		this.cyto="../../../../cyto.jsp?name=";
	}
	
	
}
