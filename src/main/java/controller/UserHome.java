package controller;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import data.Backend;

public class UserHome implements SessionAware{
	private String email;
	private String mess;
	 private Map<String,Object> session;

	 
	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public String getMess() {
		return mess;
	}

	public void setMess(String mess) {
		this.mess = mess;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public String execute()
	{
		Backend b=new Backend();
        email = (String) session.get("loginUser");
		mess="\"" +b.getRealUserImages(email).replace("\"", "\\\"")+"\"";
		if(email==null)
		{
			email="\"" +session.get("loginUser").toString().replace("\"", "\\\"")+"\"";
		}
		else
		{
			email="\"" +email.replace("\"", "\\\"")+"\"";
		}
		return "SUCCESS";
	}


}
