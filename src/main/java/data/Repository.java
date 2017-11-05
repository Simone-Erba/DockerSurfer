package data;

import java.util.List;
/**
 * 
 * @author Simone-Erba
 *
 *a data class for represent a Docker Repository
 */
public class Repository {
	String name;
	/**
	 * The link on the Dockerhub
	 */
	String dockerhub;
	/**
	 * Repository tags
	 */
	List<Tag> tags;

	protected List<Tag> getTags() {
		return tags;
	}
	protected void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	public Repository(String name, List<Tag> tags) {
		super();
		this.name = name;
		this.dockerhub = "http:\\";
		this.tags = tags;
	}
	
	
}
