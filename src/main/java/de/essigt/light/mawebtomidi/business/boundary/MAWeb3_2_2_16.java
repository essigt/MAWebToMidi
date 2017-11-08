package de.essigt.light.mawebtomidi.business.boundary;

import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonObject;

import de.essigt.light.mawebtomidi.business.controll.JsonParser;

public class MAWeb3_2_2_16 {
	
	private Logger logger = Logger.getLogger("MAWeb3_2_2_16");

	private ExampleClientEndpoint endpoint;
	private JsonParser parser;
	
	
	public MAWeb3_2_2_16() {
		parser = new JsonParser();
	}
	
	
	/**
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean connect(String url, String username, String password) {
		endpoint = new ExampleClientEndpoint(URI.create(url));
		endpoint.addMessageHandler(this::messageHandler);
		
		int session = 3; //TODO: Wich session to use?
		
		try {
			endpoint.sendMessage(buildLoginMessage(username, password, session));
		} catch (NoSuchAlgorithmException e) {
			logger.severe("No MD5 Algorithm found: " + e.getMessage());
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @param session
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private String buildLoginMessage(String username, String password, int session) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] thedigest = md.digest(password.getBytes());
		String md5Pwd = new String(thedigest); 
		
		return Json.createObjectBuilder()
				.add("requestType", "login")
				.add("username", username)
				.add("password", md5Pwd)
				.add("session", session)
				.build().toString();
	}


	/**
	 * 
	 * @return
	 */
	public void disconnect() {
		//TODO Necessary?
		
	}
	
	private void messageHandler(String msg) {
		JsonObject jsonObject = parser.parseGeneric(msg);
		System.out.println("Responce: " + jsonObject.toString());
		
		//TODO: Enough to decide?
		if(jsonObject.getString("responseType").equals("playbacks") && jsonObject.getInt("itemsType") == 2) {
			parser.parseExecuterButtons(msg);
		}
	}
}
