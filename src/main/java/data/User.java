package data;

import java.util.List;
/**
 * 
 * @author Simone-Erba
 *
 *a data class for represent a Docker User
 */
public class User {
	String name;
	List<Repository> l;
	String dockerhub;
	/**
	 * the popularity of all images of the user
	 */
	int popularity;
	
	public User(String name, List<Repository> l)
	{
		this.name=name;
		this.l=l;
		this.dockerhub="https://hub.docker.com/u/"+name;
	}
}