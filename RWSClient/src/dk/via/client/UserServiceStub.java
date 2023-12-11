package dk.via.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import dk.via.User.JSONUtil;
import dk.via.User.User;

public class UserServiceStub {
	public static String ENDPOINT = "httpS://localhost:9443/UserRWS/";
	
	private URL endpoint;
	
	public UserServiceStub() {
		try {
			this.endpoint = new URL(ENDPOINT);
		} catch (MalformedURLException e) {
			// This shouldn't really happen
			e.printStackTrace();
		}
	}
	
	public UserServiceStub(String endpoint) throws MalformedURLException {
		this.endpoint = new URL(endpoint);
	}
	
	public List<User> readAllUsers() throws IOException {
		URL serviceUrl = new URL(endpoint, "users");
		HttpURLConnection connection = (HttpURLConnection) serviceUrl.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();
		InputStream inputStream = connection.getInputStream();
		return JSONUtil.readList(inputStream, User.class);
	}
	
	public User readUser(int id) throws IOException {
		URL serviceUrl = new URL(endpoint, "users/" + id);
		HttpURLConnection connection = (HttpURLConnection) serviceUrl.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();
		InputStream inputStream = connection.getInputStream();
		return JSONUtil.read(inputStream, User.class);
	}
	
	public User createUser(String name, String email) throws IOException {
		URL serviceUrl = new URL(endpoint, "users");
		HttpURLConnection connection = (HttpURLConnection) serviceUrl.openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		JSONUtil.write(connection.getOutputStream(), new User(-1, name, email));
		connection.connect();
		InputStream inputStream = connection.getInputStream();
		return JSONUtil.read(inputStream, User.class);
	}
	
	public User createOrUpdateUser(User user) throws IOException {
		URL serviceUrl = new URL(endpoint, "users/" + user.getId());
		HttpURLConnection connection = (HttpURLConnection) serviceUrl.openConnection();
		connection.setRequestMethod("PUT");
		connection.setDoOutput(true);
		JSONUtil.write(connection.getOutputStream(), user);
		connection.connect();
		return JSONUtil.read(connection.getInputStream(), User.class);
	}
	
	public User delete(int id) throws IOException {
		URL serviceUrl = new URL(endpoint, "users/" + id);
		HttpURLConnection connection = (HttpURLConnection) serviceUrl.openConnection();
		connection.setRequestMethod("DELETE");
		connection.connect();
		return JSONUtil.read(connection.getInputStream(), User.class);
	}
}
