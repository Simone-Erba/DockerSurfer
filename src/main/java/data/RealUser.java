package data;

import java.util.List;

public class RealUser {
	public String password;
	public String name;
	public String city;
	public String email;
	public String country;
	public String occupation;
	public List<Tag> followed;
	
	
	public List<Tag> getFollowed() {
		return followed;
	}
	public void setFollowed(List<Tag> followed) {
		this.followed = followed;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
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
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public RealUser(String password, String name, String city, String email, String country, String occupation) {
		super();
		this.password = password;
		this.name = name;
		this.city = city;
		this.email = email;
		this.country = country;
		this.occupation = occupation;
	}
	public RealUser(String password, String name, String city, String email, String country, String occupation,
			List<Tag> followed) {
		super();
		this.password = password;
		this.name = name;
		this.city = city;
		this.email = email;
		this.country = country;
		this.occupation = occupation;
		this.followed = followed;
	}
	

}
