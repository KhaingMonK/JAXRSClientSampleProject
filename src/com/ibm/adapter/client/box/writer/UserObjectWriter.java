package com.ibm.adapter.client.box.writer;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;

import com.ibm.adapter.entities.User;

/**
 * MessageBoxWriter for box user object Which write java object to json object.
 * 
 * @author Kyawkm
 *
 */
public class UserObjectWriter implements MessageBodyWriter<User> {

	@Override
	public long getSize(User arg0, Class<?> arg1, Type arg2, Annotation[] arg3, MediaType arg4) {
		// As of JAX-RS 2.0, the method has been deprecated and the
		// value returned by the method is ignored by a JAX-RS runtime.
		// All MessageBodyWriter implementations are advised to return -1 from
		// the method.
		return -1;
	}

	@Override
	public boolean isWriteable(Class<?> arg0, Type arg1, Annotation[] arg2, MediaType arg3) {
		return User.class.isAssignableFrom(arg0);
	}

	@Override
	public void writeTo(User user, Class<?> arg1, Type arg2, Annotation[] arg3, MediaType arg4,
			MultivaluedMap<String, Object> arg5, OutputStream out) throws IOException, WebApplicationException {
		JsonGeneratorFactory factory = Json.createGeneratorFactory(null);
		JsonGenerator gen = factory.createGenerator(out);
		gen.writeStartObject().write("name", user.getUsername()).write("login", user.getEmail());
		if (user.getLanguage() != null)
			gen.write("language", user.getLanguage());
		if (user.getAddress() != null)
			gen.write("address", user.getAddress());
		if (user.getPassword() != null)
			gen.write("password", user.getPassword());
		if (user.getStatus() != null)
			gen.write("status", user.getStatus());
		if (user.getPhone() != null)
			gen.write("phone", user.getPhone());
		gen.writeEnd();
		gen.flush();
	}

}
