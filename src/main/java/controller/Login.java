package controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.apache.struts2.interceptor.SessionAware;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.neo4j.graphdb.Transaction;

import com.opensymphony.xwork2.ActionSupport;

import data.Backend;
import data.RealUser;
import searcher.GraphOperations;

public class Login extends ActionSupport implements SessionAware {

	private static final long serialVersionUID = 1L;
	private String email;
	private String password;
	private Map<String, Object> session;

	public void setSession(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		this.session=arg0;
	}
    public Map<String, Object> getSession() {
        return session;
}
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

    // Log Out user
    public String logout() {
            session.remove("loginUser");
            addActionMessage("You have been Successfully Logged Out");
            return SUCCESS;
    }

	// all struts logic here
	public String login() {
		Transaction t = GraphOperations.getInstance().getGraph().beginTx();
		Backend b=new Backend();
		RealUser r=b.getRealUser(email);
		t.success();
		t.close();
		System.out.println(r);
		if(r!=null)
		{
			System.out.println("diverso da null");
			MessageDigest md5 = null;
			try {
				md5 = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String hex = (new HexBinaryAdapter()).marshal(md5.digest(password.getBytes()));

			if(r.getPassword().equals(hex))
			{
				session.put("loginUser", email);
				session.remove("loginAdmin");
				System.out.println("succes user");
				return "SUCCESS";
			}
			else
			{
				return "FAIL";
			}
		}
		else
		{
			return "FAIL";
		}		

	}



}