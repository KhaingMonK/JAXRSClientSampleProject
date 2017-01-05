package com.ibm.adapter.client.box;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.ibm.adapter.client.box.reader.UserObjectReader;
import com.ibm.adapter.client.box.writer.UserObjectWriter;
import com.ibm.adapter.entities.User;

/**
 * JAX-RS client to manage Box end-point user resources
 * 
 * @author Kyawkm
 *
 */
public class BoxClient {

	private static final String ACCESS_TOKEN = "XYcMwavyaOzItwx98tFC9YGr2KCnRAX5";
	private static final String API_URL = "https://api.box.com/2.0/";
	private static final String REQUEST_TYPE = "application/json";

	/**
	 * Getting current login user
	 * 
	 * @return as user object return format depends on which type you give in
	 *         readEntity method
	 */
	public User retrieveCurrentUser() {
		Client client = ClientBuilder.newClient();
		client.register(UserObjectReader.class);
		client.register(UserObjectWriter.class);
		User userString = null;
		System.out.println("Get Current User...");
		Response response = client.target(API_URL).path("users/me").request(REQUEST_TYPE)
				.header("Authorization", "Bearer " + ACCESS_TOKEN).get();

		System.out.println(response.getStatus());
		userString = response.readEntity(User.class);
		client.close();
		return userString;
	}

	/**
	 * Getting get the list of users
	 * 
	 * @return as json string return format depends on which type you give in
	 *         readEntity method
	 */
	public String retrieveUsers() {
		Client client = ClientBuilder.newClient();
		String usersString = null;
		System.out.println("Get Users...");
		Response response = client.target(API_URL).path("users").request(REQUEST_TYPE)
				.header("Authorization", "Bearer " + ACCESS_TOKEN).get();

		System.out.println(response.getStatus());
		usersString = response.readEntity(String.class);
		client.close();
		return usersString;
	}

	/**
	 * Creating a user by providing userJson String
	 * "{\"login\": \"eddard@user.com\", \"name\": \"Ned Stark\"}"
	 * 
	 * @return user json string
	 */
	public String createUser(String userJson) {
		Client client = ClientBuilder.newClient();
		String createdUser = null;
		System.out.println("Create User");
		Response response = client.target(API_URL).path("users").request(REQUEST_TYPE).accept("application/json")
				.header("Authorization", "Bearer " + ACCESS_TOKEN).post(Entity.json(userJson));

		System.out.println(response.getStatus());
		try {
			createdUser = response.readEntity(String.class);
		} finally {
			response.close();
		}
		client.close();
		return createdUser;
	}

	/**
	 * creating user providing user object
	 * 
	 * @param user
	 * @return user object
	 */
	public User createUserWithEntity(User user) {
		MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
		Client client = ClientBuilder.newClient();
		client.register(UserObjectReader.class);
		client.register(UserObjectWriter.class);
		User createdUser = null;
		headers.add("Content-Type", "application/json");
		headers.add("Authorization", "Bearer " + ACCESS_TOKEN);
		headers.add("Accept", "application/json");
		Entity<User> userEntity = Entity.entity(user, MediaType.APPLICATION_JSON);

		System.out.println("Create User...");
		WebTarget target = client.target(API_URL).path("users");
		Builder builder = target.request().headers(headers);
		Response response = builder.post(userEntity);
		System.out.println(response.getStatus());
		try {
			createdUser = response.readEntity(User.class);
		} finally {
			response.close();
		}

		client.close();
		return createdUser;
	}

	public User modifyUser(User user) {
		Client client = ClientBuilder.newClient();
		client.register(UserObjectReader.class);
		client.register(UserObjectWriter.class);
		User modifiedUser = null;
		MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
		headers.add("Content-Type", "application/json");
		headers.add("Authorization", "Bearer " + ACCESS_TOKEN);
		headers.add("Accept", "application/json");
		Entity<User> userEntity = Entity.entity(user, MediaType.APPLICATION_JSON);
		System.out.println("Modify User...");
		WebTarget target = client.target(API_URL).path("users");
		Builder builder = target.request().headers(headers);
		Response response = builder.post(userEntity);
		System.out.println(response.getStatus());
		try {
			modifiedUser = response.readEntity(User.class);
		} finally {
			response.close();
		}
		client.close();
		return modifiedUser;
	}

	public User modifyUserAttributes(String userAttributesJson, String userId) {
		Client client = ClientBuilder.newClient();
		client.register(UserObjectReader.class);
		client.register(UserObjectWriter.class);
		User modifiedUser = null;
		MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
		headers.add("Content-Type", "application/json");
		headers.add("Authorization", "Bearer " + ACCESS_TOKEN);
		headers.add("Accept", "application/json");
		Entity<String> userEntity = Entity.entity(userAttributesJson, MediaType.APPLICATION_JSON);
		System.out.println("Modify User...");
		WebTarget target = client.target(API_URL).path("users/" + userId);
		Builder builder = target.request().headers(headers);
		Response response = builder.put(userEntity);
		System.out.println(response.getStatus());
		try {
			modifiedUser = response.readEntity(User.class);
		} finally {
			response.close();
		}

		client.close();
		return modifiedUser;
	}

	public static void main(String[] args) {
		BoxClient userClient = new BoxClient();
		User userString = userClient.retrieveCurrentUser();
		System.out.println(userString.toString());
		String usersString = userClient.retrieveUsers();
		System.out.println(usersString);
		User user = new User("eddard10", "eddard10@mail.com", "en", "USA", null, "inactive", null);
		User createdUser = userClient.createUserWithEntity(user);
		System.out.println(createdUser.toString());
		String userAttributesJson = "{\"address\": \"Changi Business Park 1\"}";
		User modifiedUser = userClient.modifyUserAttributes(userAttributesJson, "270244221");
		System.out.println(modifiedUser.toString());
	}

}
