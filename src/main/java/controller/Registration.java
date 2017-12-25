package controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

import data.Backend;
import data.RealUser;
import searcher.GraphOperations;

public class Registration{

	private String password;
	private String name;
	private String city;
	private String email;
	private String country;
	private String occupation;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	// all struts logic here
	public String execute() {
		GraphDatabaseService g=GraphOperations.getInstance().getGraph();
		Transaction t=g.beginTx();
		Backend b=new Backend();
		List<RealUser> users=b.getRealUsers();
		Iterator<RealUser> i=users.iterator();
		boolean trovato=false;
		while(i.hasNext()&&trovato==false)
		{
			RealUser r=i.next();
			if(r.getEmail().equals(email))
			{
				trovato=true;
			}
		}
		if(trovato==false)
		{
			MessageDigest md5 = null;
			try {
				md5 = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String hex = (new HexBinaryAdapter()).marshal(md5.digest(password.getBytes()));
			GraphOperations.getInstance().insertRealUser(hex, name, city, email, country, occupation);
			t.success();
			t.close();
			return "SUCCESS";
		}
		else
		{
			t.success();
			t.close();
			return "FAIL";
		}
		

	}
}