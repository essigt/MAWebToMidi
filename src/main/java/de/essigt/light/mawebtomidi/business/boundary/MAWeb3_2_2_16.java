package de.essigt.light.mawebtomidi.business.boundary;

import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.xml.bind.DatatypeConverter;

import de.essigt.light.mawebtomidi.business.controll.JsonParser;
import de.essigt.light.mawebtomidi.business.entity.Executer;

public class MAWeb3_2_2_16 {
	
	private Logger logger = Logger.getLogger("MAWeb3_2_2_16");

	private ExampleClientEndpoint endpoint;
	private JsonParser parser;
	
	private boolean loggedIn = false;
	private int session = 0;
	private String username;
	private String password;
	
	
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
		this.username = username;
		this.password = password;
		
		endpoint = new ExampleClientEndpoint(URI.create("ws://" + url + ":80?ma=1"));
		endpoint.addMessageHandler(this::messageHandler);
		
		//TODO: Wait for result
		return true;
	}
	

	/**
	 * 
	 * @return
	 */
	private String buildSessionRequestMessage() {
		return "{\"session\":0}";
	}
	
	
	/**
	 * 
	 * @param msg
	 */
	private void messageHandler(String msg) {
		if(msg.equals("server ready")) {
			handleServerReady();
			return;
		}
		
		JsonObject jsonObject = parser.parseGeneric(msg);
		if(session == 0) {
			handleSessionReceived(jsonObject);
		}
		String responseType = jsonObject.getString("responseType");
		
		logger.info("Responce: " + jsonObject.toString());
		
		//TODO: Enough to decide?
		if(responseType.equals("playbacks") && jsonObject.getInt("itemsType") == 2) {
			List<Executer> executers = parser.parseExecuterButtons(msg);
			for(Executer ex : executers) {
				if(!ex.getName().isEmpty()) {
					logger.info("Executer: " + ex.getId() + "(" + ex.getName() + ") Running=" + ex.isRunning() );
					ExecuterManager.getInstance().updateExecuter(ex);
				}
			}
				
		} else if(responseType.equals("login")) {
			handleLoginResponse(jsonObject);
			
		}
	}

	
	private void handleServerReady() {
		endpoint.sendMessage(buildSessionRequestMessage());
	}
	
	private void handleSessionReceived(JsonObject responce) {
		this.session = responce.getInt("session");
		
		try {
			endpoint.sendMessage( buildLoginMessage(username, password, session));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		byte[] digest = md.digest(password.getBytes());
		String md5Pwd = DatatypeConverter.printHexBinary(digest); 
		
		return Json.createObjectBuilder()
				.add("requestType", "login")
				.add("username", username)
				.add("password", md5Pwd)
				.add("session", session)
				.build().toString();
	}
	
	private String buildRequestExecuterButtonsRequest() {
		//TODO: Remove hardcoded request
		return "{\"requestType\":\"playbacks\",\"startIndex\":100,\"itemsCount\":60,\"pageIndex\":0,\"itemsType\":2,\"execButtonViewMode\":2,\"buttonsViewMode\":0,\"session\":" + session + "}";
	}
	

	private void handleLoginResponse(JsonObject jsonObject) {
		if(jsonObject.getBoolean("result")) {
			loggedIn = true;
			logger.info("Logged in succesfully!!! LoggedIn: " + loggedIn);
		} else {
			//loggedIn = false;
			logger.warning("Login failed");
		}
	}


	public boolean isLoggedIn() {
		return loggedIn;
	}


	public void refreshExecuters() {
		endpoint.sendMessage( buildRequestExecuterButtonsRequest());
	}


	
}
