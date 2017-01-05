package com.ibm.adapter.client.box.reader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;

import com.ibm.adapter.entities.User;

/**
 * MessageBodyReader for box user object Which read json object to java object
 * 
 * @author Kyawkm
 *
 */
public class UserObjectReader implements MessageBodyReader<User> {

	@Override
	public boolean isReadable(Class<?> arg0, Type arg1, Annotation[] arg2, MediaType arg3) {
		return User.class.isAssignableFrom(arg0);
	}

	@Override
	public User readFrom(Class<User> arg0, Type arg1, Annotation[] arg2, MediaType arg3,
			MultivaluedMap<String, String> arg4, InputStream in) throws IOException, WebApplicationException {
		User user = new User();
		JsonParser parser = Json.createParser(in);
		while (parser.hasNext()) {
			switch (parser.next()) {
			case KEY_NAME:
				String key = parser.getString();
				parser.next();
				switch (key) {
				case "name":
					user.setUsername(parser.getString());
					;
					break;
				case "login":
					user.setEmail(parser.getString());
					break;
				case "language":
					user.setLanguage(parser.getString());
					break;
				case "address":
					user.setAddress(parser.getString());
					break;
				case "password":
					user.setPassword(parser.getString());
					break;
				case "status":
					user.setStatus(parser.getString());
					break;
				case "phone":
					user.setPhone(parser.getString());
					break;
				default:
					break;
				}
				break;
			default:
				break;
			}
		}
		return user;
	}

}
