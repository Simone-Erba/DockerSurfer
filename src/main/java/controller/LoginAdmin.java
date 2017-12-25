package controller;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.neo4j.graphdb.Transaction;

import com.opensymphony.xwork2.ActionSupport;

import data.Backend;
import searcher.GraphOperations;

public class LoginAdmin  extends ActionSupport implements SessionAware{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
String password;
String users;
String data;

public String getData() {
	return data;
}

public void setData(String data) {
	this.data = data;
}
private Map<String, Object> session;

public Map<String, Object> getSession() {
	return session;
}

public void setSession(Map<String, Object> session) {
	this.session = session;
}

public String getUsers() {
	return users;
}

public void setUsers(String users) {
	this.users = users;
}

public String getPassword() {
	return password;
}

public void setPassword(String password) {
	this.password = password;
}
public String logout() {
    session.remove("loginAdmin");
    addActionMessage("You have been Successfully Logged Out");
    return SUCCESS;
}
public String home()
{
	Transaction t = GraphOperations.getInstance().getGraph().beginTx();
	Backend b=new Backend();
	data="\"" +b.getRealUsersJson().replace("\"", "\\\"")+"\"";
	t.success();
	t.close();
	return "SUCCESS";
}
public String login() {
	if(password.equals("a"))
	{
		
		session.put("loginAdmin", "admin");
		session.remove("loginUser");
		return "SUCCESS";
	}
	else
	{
		return "FAIL";
	}
}
}
