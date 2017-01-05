package com.ibm.adapter.entities;

/**
 * User entity
 * 
 * @author Kyawkm
 *
 */
public class User {
	private String username;
	private String email;
	private String language;
	private String address;
	private String password;
	private String status;
	private String phone;

	public User() {

	}

	public User(String username) {
		super();
		this.username = username;
	}

	public User(String username, String email) {
		this.username = username;
		this.email = email;
	}

	public User(String email, String username, String language, String address) {
		super();
		this.username = username;
		this.email = email;
		this.language = language;
		this.address = address;
	}

	public User(String username, String email, String language, String address, String password, String status,
			String phone) {
		super();
		this.username = username;
		this.email = email;
		this.language = language;
		this.address = address;
		this.password = password;
		this.status = status;
		this.phone = phone;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", email=" + email + ", language=" + language + ", address=" + address
				+ ", password=" + password + ", status=" + status + ", phone=" + phone + "]";
	}

}
