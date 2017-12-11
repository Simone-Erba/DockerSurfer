package servlet;

public class User {
	String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	// all struts logic here
	public String execute() {

		return "{name:\"ciao\"}";

	}
}
