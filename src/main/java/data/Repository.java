package data;

import java.util.List;

public class Repository {
	String name;
	String dockerhub;
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
