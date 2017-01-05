package com.ibm.adapter.client.servicenow;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.ibm.adapter.client.servicenow.reader.UserObjectReader;
import com.ibm.adapter.client.servicenow.writer.UserObjectWriter;
import com.ibm.adapter.entities.User;

/**
 * JAX-RS client to manage ServiceNow end-point user resources
 * 
 * @author Kyawkm
 *
 */
public class ServiceNowClient {
	private static final String API_URL = "https://dev12471.service-now.com/";
	// Basic authorization
	private static final String CREDENTIAL = "YWRtaW46UEBzc3cwcmQxMjM=";
	private static final String REQUEST_TYPE = "application/json";

	/**
	 * Getting current login user
	 * 
	 * @return as user object return format depends on which type you give in
	 *         readEntity method
	 */
	public String retrieveUsers() {
		Client client = ClientBuilder.newClient();
		client.register(UserObjectReader.class);
		client.register(UserObjectWriter.class);
		String userString = null;
		System.out.println("Get Users...");
		Response response = client.target(API_URL).path("api/now/v1/table/sys_user").request(REQUEST_TYPE)
				.header("Authorization", "Basic " + CREDENTIAL).get();
		System.out.println(response.getStatus());
		userString = response.readEntity(String.class);
		client.close();
		return userString;
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
		Response response = client.target(API_URL).path("api/now/v1/table/sys_user").request(REQUEST_TYPE)
				.accept("application/json").header("Authorization", "Basic " + CREDENTIAL).post(Entity.json(userJson));

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
		headers.add("Authorization", "Basic " + CREDENTIAL);
		headers.add("Accept", "application/json");
		Entity<User> userEntity = Entity.entity(user, MediaType.APPLICATION_JSON);

		System.out.println("createUserWithEntity: Create User...");
		WebTarget target = client.target(API_URL).path("api/now/v1/table/sys_user");
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

	public User modifyUserAttributes(String userAttributesJson, String userId) {
		Client client = ClientBuilder.newClient();
		client.register(UserObjectReader.class);
		client.register(UserObjectWriter.class);
		User modifiedUser = null;
		MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
		headers.add("Content-Type", "application/json");
		headers.add("Authorization", "Basic " + CREDENTIAL);
		headers.add("Accept", "application/json");
		Entity<String> userEntity = Entity.entity(userAttributesJson, MediaType.APPLICATION_JSON);
		System.out.println("Modify User password...");
		WebTarget target = client.target(API_URL).path("api/now/v1/table/sys_user/" + userId);
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

	public boolean deleteUser(String userId) {
		boolean deleted = false;
		Client client = ClientBuilder.newClient();
		client.register(UserObjectReader.class);
		client.register(UserObjectWriter.class);
		MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
		headers.add("Content-Type", "application/json");
		headers.add("Authorization", "Basic " + CREDENTIAL);
		headers.add("Accept", "application/json");
		System.out.println("Delete User...");
		Response response = client.target(API_URL).path("api/now/v1/table/sys_user/" + userId).request(REQUEST_TYPE)
				.headers(headers).delete();
		System.out.println(response.getStatus());
		try {
			if(response.getStatus() == 204){
				deleted = true;
			}
		} finally {
			response.close();
		}
		client.close();
		return deleted;
	}
	
	public static void main(String[] args) {
		ServiceNowClient userClient = new ServiceNowClient();
		String responseUserString = userClient.retrieveUsers();
		System.out.println(responseUserString.toString());
		/**
		 * Update user_name to test the createUser(String) 
		 */
//		String userString = "{ \"user_name\" : \"Jane.snAdapterUser\", \"name\" : \"snJaneAdapterUser\",\"email\" : \"jane_adapter_user@mail.com\",\"phone\" : \"123456\"}";
//		String createdUser = userClient.createUser(userString);
//		System.out.println(createdUser);
		String userAttributesJson = "{\"user_password\": \"pswd@123\"}";
		User modifiedUser = userClient.modifyUserAttributes(userAttributesJson, "9725ce624f4032004ef9cab18110c7c8");
		System.out.println(modifiedUser.toString());
		User createdUser2  = userClient.createUserWithEntity(new User("snTestUser4","sntestuser4@mail.com"));
		System.out.println(createdUser2.toString());
		boolean deleted = userClient.deleteUser(createdUser2.getSystemId());
		System.out.println("User deleted? " + deleted);
		
	}

}
