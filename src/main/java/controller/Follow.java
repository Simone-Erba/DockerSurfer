package controller;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import data.Backend;
import searcher.GraphOperations;

public class Follow implements SessionAware{
	
	private String image;
	private String email;
	
	
	 private Map<String,Object> session;

	    public void setSession(Map<String,Object> session){ 
	        this.session = session;
	    }
	    
	public Map<String, Object> getSession() {
			return session;
		}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String execute() {
		
		System.out.println(image);
		email=session.get("loginUser").toString();
		System.out.println(email);
		String ris=GraphOperations.getInstance().userFollow(email, image);
		if(ris!=null)
		{
			return "SUCCESS";
		}
		else
		{
			return "FAIL";
		}
	}
}
