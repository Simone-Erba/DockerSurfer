package data;

import java.util.List;

public class User {
	String name;
	List<Repository> l;
	String dockerhub;
	
	public User(String name, List<Repository> l)
	{
		this.name=name;
		this.l=l;
		this.dockerhub="http://";
	}
}