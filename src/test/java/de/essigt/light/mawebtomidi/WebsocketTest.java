package de.essigt.light.mawebtomidi;

import java.net.URI;

import org.junit.Test;

public class WebsocketTest {
	
	@Test	
	public void testWS() {
		String uri = "ws://demos.kaazing.com:80/echo";
		ExampleClientEndpoint ep = new ExampleClientEndpoint(URI.create(uri));
		System.out.println("IsOpen: " + ep.userSession.isOpen());
		

		ep.addMessageHandler( m -> System.out.println("Message received: " + m));
		
		System.out.println("Sending message...");
		ep.sendMessage("Test");
		
		
		
		 try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
