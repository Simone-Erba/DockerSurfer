package data;

import java.util.List;

/**
 * a data class for represent a Docker Repository
 * @author Simone-Erba
 */
public class Repository {
	/**
	 * full repo name: <username>/<reponame>
	 */
	String name;
	/**
	 * The link on the Dockerhub
	 */
	String dockerhub;
	/**
	 * Repository tags
	 */
	String user;
	String repo;
	List<Tag> tags;
	/**
	 * the popularity for all the tag of the image
	 */
	int popularity;

	public int getPopularity() {
		return popularity;
	}

	public void setPopularity(int popularity) {
		this.popularity = popularity;
	}

	protected List<Tag> getTags() {
		return tags;
	}

	protected void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public Repository(String name, List<Tag> tags, int pop) {
		super();
		this.user = name.substring(0, name.indexOf("/"));
		this.repo = name.substring(name.indexOf("/") + 1);
		this.name = name;
		this.dockerhub = "https://hub.docker.com/r/" + user + "/" + repo + "/";
		this.tags = tags;
		popularity = pop;
	}

}
