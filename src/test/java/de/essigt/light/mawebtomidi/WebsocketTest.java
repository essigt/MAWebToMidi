package de.essigt.light.mawebtomidi;

import java.io.StringReader;
import java.net.URI;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

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
	
	
	
	@Test	
	public void testMAWS() {
		String uri = "ws://localhost/:80/?ma=1";
		ExampleClientEndpoint ep = new ExampleClientEndpoint(URI.create(uri));
		System.out.println("IsOpen: " + ep.userSession.isOpen());
		ep.addMessageHandler( this::processMessage );
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//TODO: Login and use correct session
		
		while(true ) {

			System.out.println("Sending message...");
			//ep.sendMessage("{\"requestType\":\"playbacks\",\"startIndex\":0,\"itemsCount\":5,\"pageIndex\":1,\"itemsType\":1,\"execButtonViewMode\":1,\"buttonsViewMode\":0,\"session\":3}");
			ep.sendMessage("{\"requestType\":\"playbacks\",\"startIndex\":100,\"itemsCount\":60,\"pageIndex\":0,\"itemsType\":2,\"execButtonViewMode\":2,\"buttonsViewMode\":0,\"session\":1}");
			
			
			//System.out.println("Update Fader Value");
			//ep.sendMessage("{\"worldIndex\":0}");
			//ep.sendMessage("{\"requestType\":\"playbacks_userInput\",\"execIndex\":0,\"pageIndex\":1,\"faderValue\":0.7903361344537815,\"type\":1,\"session\":3}");
			
			
			 try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void processMessage(String m) {
		JsonReader jsonReader = Json.createReader(new StringReader(m));
		JsonObject jsonObject = jsonReader.readObject();
		System.out.println("Size: " + jsonObject.getJsonArray("items").size());
		
		JsonArray itemsArray = jsonObject.getJsonArray("items");
		for(int i=0; i < itemsArray.size(); i++) {
			JsonArray blockArray = itemsArray.getJsonArray(i);
			for(int block=0; block < blockArray.size(); block++) {
				JsonObject jsonExecuter = blockArray.getJsonObject(block);
				
				if(jsonExecuter.getJsonObject("i").getJsonString("t").getString().equals("127")) {
					String bgColor = jsonExecuter.getJsonObject("cues").getJsonArray("items").getJsonObject(0).getJsonObject("pgs").getJsonString("bC").getString();
					
					
					
					if(bgColor.equals("#0000FF")) {
						System.err.println("127 is running");
					} else {
						System.err.println("127 is NOT running");
					}
				}
				/*if(!jsonExecuter.getJsonObject("tt").getJsonString("t").getString().isEmpty()) {
					System.out.println("Exectuer: " + jsonExecuter.getJsonObject("i").getJsonString("t").getString() );
					System.out.println(" -> Name: " + jsonExecuter.getJsonObject("tt").getJsonString("t").getString() );
				}*/
				
			}
		}
	}
	
	@Test
	public void testJSON() {
		JsonReader jsonReader = Json.createReader(new StringReader("{\"responseType\":\"playbacks\",\"iPage\":1,\"iExecOff\":100,\"cntPages\":10000,\"itemsType\":2,\"items\":[[{\"i\":{\"t\":\"101\",\"c\":\"#C0C0C0\"},\"oType\":{\"t\":\"  LAS\",\"c\":\"#FFFFFF\"},\"oI\":{\"t\":\"2\",\"c\":\"#FFFFFF\"},\"tt\":{\"t\":\"Run Down\",\"c\":\"#FFFFFF\"},\"bC\":\"#000000\",\"bdC\":\"#FF80FF\",\"cues\":{\"bC\":\"#3F203F\",\"items\":[{\"t\":\"60.0 BPM\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":0.267,\"bC\":\"#808080\"}},{\"t\":\"0.0 s\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":1.000,\"bC\":\"#808080\"}},{\"pgs\":{\"bC\":\"#808080\"}}]},\"combinedItems\":1,\"iExec\":100,\"bottomButtons\":{\"items\":[{\"fader\":{\"v\":1.000,\"bC\":\"#808000\"},\"n\":{\"t\":\"Toggle\",\"c\":\"#C0C0C0\"}}]}},{\"i\":{\"t\":\"102\",\"c\":\"#C0C0C0\"},\"oType\":{\"t\":\"  LAS\",\"c\":\"#FFFFFF\"},\"oI\":{\"t\":\"3\",\"c\":\"#FFFFFF\"},\"tt\":{\"t\":\"Run Up\",\"c\":\"#FFFFFF\"},\"bC\":\"#000000\",\"bdC\":\"#FF80FF\",\"cues\":{\"bC\":\"#3F203F\",\"items\":[{\"t\":\"60.0 BPM\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":0.267,\"bC\":\"#808080\"}},{\"t\":\"0.0 s\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":1.000,\"bC\":\"#808080\"}},{\"pgs\":{\"bC\":\"#808080\"}}]},\"combinedItems\":1,\"iExec\":101,\"bottomButtons\":{\"items\":[{\"fader\":{\"v\":1.000,\"bC\":\"#808000\"},\"n\":{\"t\":\"Toggle\",\"c\":\"#C0C0C0\"}}]}},{\"i\":{\"t\":\"103\",\"c\":\"#C0C0C0\"},\"oType\":{\"t\":\"  LAS\",\"c\":\"#FFFFFF\"},\"oI\":{\"t\":\"4\",\"c\":\"#FFFFFF\"},\"tt\":{\"t\":\"Stars\",\"c\":\"#FFFFFF\"},\"bC\":\"#000000\",\"bdC\":\"#FF80FF\",\"cues\":{\"bC\":\"#3F203F\",\"items\":[{\"t\":\"60.0 BPM\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":0.267,\"bC\":\"#808080\"}},{\"t\":\"0.0 s\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":1.000,\"bC\":\"#808080\"}},{\"pgs\":{\"bC\":\"#808080\"}}]},\"combinedItems\":1,\"iExec\":102,\"bottomButtons\":{\"items\":[{\"fader\":{\"v\":1.000,\"bC\":\"#808000\"},\"n\":{\"t\":\"Toggle\",\"c\":\"#C0C0C0\"}}]}},{\"i\":{\"t\":\"104\",\"c\":\"#C0C0C0\"},\"oType\":{\"t\":\"  LAS\",\"c\":\"#FFFFFF\"},\"oI\":{\"t\":\"6\",\"c\":\"#FFFFFF\"},\"tt\":{\"t\":\"Phaser Down\",\"c\":\"#FFFFFF\"},\"bC\":\"#000000\",\"bdC\":\"#FF80FF\",\"cues\":{\"bC\":\"#3F203F\",\"items\":[{\"t\":\"60.0 BPM\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":0.267,\"bC\":\"#808080\"}},{\"t\":\"0.0 s\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":1.000,\"bC\":\"#808080\"}},{\"pgs\":{\"bC\":\"#808080\"}}]},\"combinedItems\":1,\"iExec\":103,\"bottomButtons\":{\"items\":[{\"fader\":{\"v\":1.000,\"bC\":\"#808000\"},\"n\":{\"t\":\"Toggle\",\"c\":\"#C0C0C0\"}}]}},{\"i\":{\"t\":\"105\",\"c\":\"#C0C0C0\"},\"oType\":{\"t\":\"  LAS\",\"c\":\"#FFFFFF\"},\"oI\":{\"t\":\"7\",\"c\":\"#FFFFFF\"},\"tt\":{\"t\":\"Phaser Up\",\"c\":\"#FFFFFF\"},\"bC\":\"#000000\",\"bdC\":\"#FF80FF\",\"cues\":{\"bC\":\"#3F203F\",\"items\":[{\"t\":\"60.0 BPM\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":0.267,\"bC\":\"#808080\"}},{\"t\":\"0.0 s\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":1.000,\"bC\":\"#808080\"}},{\"pgs\":{\"bC\":\"#808080\"}}]},\"combinedItems\":1,\"iExec\":104,\"bottomButtons\":{\"items\":[{\"fader\":{\"v\":1.000,\"bC\":\"#808000\"},\"n\":{\"t\":\"Toggle\",\"c\":\"#C0C0C0\"}}]}}],[{\"i\":{\"t\":\"106\",\"c\":\"#C0C0C0\"},\"oType\":{\"t\":\"  LAS\",\"c\":\"#FFFFFF\"},\"oI\":{\"t\":\"9\",\"c\":\"#FFFFFF\"},\"tt\":{\"t\":\"R Sin\",\"c\":\"#FFFFFF\"},\"bC\":\"#000000\",\"bdC\":\"#FF80FF\",\"cues\":{\"bC\":\"#3F203F\",\"items\":[{\"t\":\"60.0 BPM\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":0.267,\"bC\":\"#808080\"}},{\"t\":\"0.0 s\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":1.000,\"bC\":\"#808080\"}},{\"pgs\":{\"bC\":\"#808080\"}}]},\"combinedItems\":1,\"iExec\":105,\"bottomButtons\":{\"items\":[{\"fader\":{\"v\":1.000,\"bC\":\"#808000\"},\"n\":{\"t\":\"Toggle\",\"c\":\"#C0C0C0\"}}]}},{\"i\":{\"t\":\"107\",\"c\":\"#C0C0C0\"},\"oType\":{\"t\":\"  LAS\",\"c\":\"#FFFFFF\"},\"oI\":{\"t\":\"10\",\"c\":\"#FFFFFF\"},\"tt\":{\"t\":\"G Sin\",\"c\":\"#FFFFFF\"},\"bC\":\"#000000\",\"bdC\":\"#FF80FF\",\"cues\":{\"bC\":\"#3F203F\",\"items\":[{\"t\":\"60.0 BPM\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":0.267,\"bC\":\"#808080\"}},{\"t\":\"0.0 s\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":1.000,\"bC\":\"#808080\"}},{\"pgs\":{\"bC\":\"#808080\"}}]},\"combinedItems\":1,\"iExec\":106,\"bottomButtons\":{\"items\":[{\"fader\":{\"v\":1.000,\"bC\":\"#808000\"},\"n\":{\"t\":\"Toggle\",\"c\":\"#C0C0C0\"}}]}},{\"i\":{\"t\":\"108\",\"c\":\"#C0C0C0\"},\"oType\":{\"t\":\"  LAS\",\"c\":\"#FFFFFF\"},\"oI\":{\"t\":\"11\",\"c\":\"#FFFFFF\"},\"tt\":{\"t\":\"B Sin\",\"c\":\"#FFFFFF\"},\"bC\":\"#000000\",\"bdC\":\"#FF80FF\",\"cues\":{\"bC\":\"#3F203F\",\"items\":[{\"t\":\"60.0 BPM\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":0.267,\"bC\":\"#808080\"}},{\"t\":\"0.0 s\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":1.000,\"bC\":\"#808080\"}},{\"pgs\":{\"bC\":\"#808080\"}}]},\"combinedItems\":1,\"iExec\":107,\"bottomButtons\":{\"items\":[{\"fader\":{\"v\":1.000,\"bC\":\"#808000\"},\"n\":{\"t\":\"Toggle\",\"c\":\"#C0C0C0\"}}]}},{\"i\":{\"t\":\"109\",\"c\":\"#C0C0C0\"},\"oType\":{\"t\":\"  \",\"c\":\"#FFFFFF\"},\"oI\":{\"t\":\"3\",\"c\":\"#FFFFFF\"},\"tt\":{\"t\":\"PixelBars Off\",\"c\":\"#FFFFFF\"},\"bC\":\"#000000\",\"bdC\":\"#FF0000\",\"cues\":{\"bC\":\"#3F0000\",\"items\":[{\"c\":\"#FFFFFF\",\"pgs\":{}}]},\"combinedItems\":1,\"iExec\":108,\"bottomButtons\":{\"items\":[{\"n\":{\"t\":\"Go\",\"c\":\"#C0C0C0\"}}]}},{\"i\":{\"t\":\"110\",\"c\":\"#000000\"},\"oType\":{\"t\":\"\"},\"oI\":{\"t\":\"\"},\"tt\":{\"t\":\"\"},\"bC\":\"#404040\",\"bdC\":\"#3D3D3D\",\"cues\":{},\"combinedItems\":1,\"iExec\":109}],[{\"i\":{\"t\":\"111\",\"c\":\"#000000\"},\"oType\":{\"t\":\"\"},\"oI\":{\"t\":\"\"},\"tt\":{\"t\":\"\"},\"bC\":\"#404040\",\"bdC\":\"#3D3D3D\",\"cues\":{},\"combinedItems\":1,\"iExec\":110},{\"i\":{\"t\":\"112\",\"c\":\"#000000\"},\"oType\":{\"t\":\"\"},\"oI\":{\"t\":\"\"},\"tt\":{\"t\":\"\"},\"bC\":\"#404040\",\"bdC\":\"#3D3D3D\",\"cues\":{},\"combinedItems\":1,\"iExec\":111},{\"i\":{\"t\":\"113\",\"c\":\"#000000\"},\"oType\":{\"t\":\"\"},\"oI\":{\"t\":\"\"},\"tt\":{\"t\":\"\"},\"bC\":\"#404040\",\"bdC\":\"#3D3D3D\",\"cues\":{},\"combinedItems\":1,\"iExec\":112},{\"i\":{\"t\":\"114\",\"c\":\"#000000\"},\"oType\":{\"t\":\"\"},\"oI\":{\"t\":\"\"},\"tt\":{\"t\":\"\"},\"bC\":\"#404040\",\"bdC\":\"#3D3D3D\",\"cues\":{},\"combinedItems\":1,\"iExec\":113},{\"i\":{\"t\":\"115\",\"c\":\"#000000\"},\"oType\":{\"t\":\"\"},\"oI\":{\"t\":\"\"},\"tt\":{\"t\":\"\"},\"bC\":\"#404040\",\"bdC\":\"#3D3D3D\",\"cues\":{},\"combinedItems\":1,\"iExec\":114}],[{\"i\":{\"t\":\"116\",\"c\":\"#000000\"},\"oType\":{\"t\":\"\"},\"oI\":{\"t\":\"\"},\"tt\":{\"t\":\"\"},\"bC\":\"#404040\",\"bdC\":\"#3D3D3D\",\"cues\":{},\"combinedItems\":1,\"iExec\":115},{\"i\":{\"t\":\"117\",\"c\":\"#000000\"},\"oType\":{\"t\":\"\"},\"oI\":{\"t\":\"\"},\"tt\":{\"t\":\"\"},\"bC\":\"#404040\",\"bdC\":\"#3D3D3D\",\"cues\":{},\"combinedItems\":1,\"iExec\":116},{\"i\":{\"t\":\"118\",\"c\":\"#000000\"},\"oType\":{\"t\":\"\"},\"oI\":{\"t\":\"\"},\"tt\":{\"t\":\"\"},\"bC\":\"#404040\",\"bdC\":\"#3D3D3D\",\"cues\":{},\"combinedItems\":1,\"iExec\":117},{\"i\":{\"t\":\"119\",\"c\":\"#C0C0C0\"},\"oType\":{\"t\":\"  \",\"c\":\"#FFFFFF\"},\"oI\":{\"t\":\"4\",\"c\":\"#FFFFFF\"},\"tt\":{\"t\":\"Stairville Off\",\"c\":\"#FFFFFF\"},\"bC\":\"#000000\",\"bdC\":\"#FF0000\",\"cues\":{\"bC\":\"#3F0000\",\"items\":[{\"c\":\"#FFFFFF\",\"pgs\":{}}]},\"combinedItems\":1,\"iExec\":118,\"bottomButtons\":{\"items\":[{\"n\":{\"t\":\"Go\",\"c\":\"#C0C0C0\"}}]}},{\"i\":{\"t\":\"120\",\"c\":\"#000000\"},\"oType\":{\"t\":\"\"},\"oI\":{\"t\":\"\"},\"tt\":{\"t\":\"\"},\"bC\":\"#404040\",\"bdC\":\"#3D3D3D\",\"cues\":{},\"combinedItems\":1,\"iExec\":119}],[{\"i\":{\"t\":\"121\",\"c\":\"#C0C0C0\"},\"oType\":{\"t\":\"  LAS\",\"c\":\"#FFFFFF\"},\"oI\":{\"t\":\"32\",\"c\":\"#FFFFFF\"},\"tt\":{\"t\":\"R Sin\",\"c\":\"#FFFFFF\"},\"bC\":\"#000000\",\"bdC\":\"#FF80FF\",\"cues\":{\"bC\":\"#3F203F\",\"items\":[{\"t\":\"60.0 BPM\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":0.267,\"bC\":\"#808080\"}},{\"t\":\"0.0 s\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":1.000,\"bC\":\"#808080\"}},{\"pgs\":{\"bC\":\"#808080\"}}]},\"combinedItems\":1,\"iExec\":120,\"bottomButtons\":{\"items\":[{\"fader\":{\"v\":1.000,\"bC\":\"#808000\"},\"n\":{\"t\":\"Toggle\",\"c\":\"#C0C0C0\"}}]}},{\"i\":{\"t\":\"122\",\"c\":\"#C0C0C0\"},\"oType\":{\"t\":\"  LAS\",\"c\":\"#FFFFFF\"},\"oI\":{\"t\":\"33\",\"c\":\"#FFFFFF\"},\"tt\":{\"t\":\"G Sin\",\"c\":\"#FFFFFF\"},\"bC\":\"#000000\",\"bdC\":\"#FF80FF\",\"cues\":{\"bC\":\"#3F203F\",\"items\":[{\"t\":\"60.0 BPM\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":0.267,\"bC\":\"#808080\"}},{\"t\":\"0.0 s\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":1.000,\"bC\":\"#808080\"}},{\"pgs\":{\"bC\":\"#808080\"}}]},\"combinedItems\":1,\"iExec\":121,\"bottomButtons\":{\"items\":[{\"fader\":{\"v\":1.000,\"bC\":\"#808000\"},\"n\":{\"t\":\"Toggle\",\"c\":\"#C0C0C0\"}}]}},{\"i\":{\"t\":\"123\",\"c\":\"#C0C0C0\"},\"oType\":{\"t\":\"  LAS\",\"c\":\"#FFFFFF\"},\"oI\":{\"t\":\"34\",\"c\":\"#FFFFFF\"},\"tt\":{\"t\":\"B Sin\",\"c\":\"#FFFFFF\"},\"bC\":\"#000000\",\"bdC\":\"#FF80FF\",\"cues\":{\"bC\":\"#3F203F\",\"items\":[{\"t\":\"60.0 BPM\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":0.267,\"bC\":\"#808080\"}},{\"t\":\"0.0 s\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":1.000,\"bC\":\"#808080\"}},{\"pgs\":{\"bC\":\"#808080\"}}]},\"combinedItems\":1,\"iExec\":122,\"bottomButtons\":{\"items\":[{\"fader\":{\"v\":1.000,\"bC\":\"#808000\"},\"n\":{\"t\":\"Toggle\",\"c\":\"#C0C0C0\"}}]}},{\"i\":{\"t\":\"124\",\"c\":\"#000000\"},\"oType\":{\"t\":\"\"},\"oI\":{\"t\":\"\"},\"tt\":{\"t\":\"\"},\"bC\":\"#404040\",\"bdC\":\"#3D3D3D\",\"cues\":{},\"combinedItems\":1,\"iExec\":123},{\"i\":{\"t\":\"125\",\"c\":\"#000000\"},\"oType\":{\"t\":\"\"},\"oI\":{\"t\":\"\"},\"tt\":{\"t\":\"\"},\"bC\":\"#404040\",\"bdC\":\"#3D3D3D\",\"cues\":{},\"combinedItems\":1,\"iExec\":124}],[{\"i\":{\"t\":\"126\",\"c\":\"#C0C0C0\"},\"oType\":{\"t\":\"  LAS\",\"c\":\"#FFFFFF\"},\"oI\":{\"t\":\"36\",\"c\":\"#FFFFFF\"},\"tt\":{\"t\":\"R Toggle\",\"c\":\"#FFFFFF\"},\"bC\":\"#000000\",\"bdC\":\"#FF80FF\",\"cues\":{\"bC\":\"#3F203F\",\"items\":[{\"t\":\"60.0 BPM\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":0.267,\"bC\":\"#808080\"}},{\"t\":\"0.0 s\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":1.000,\"bC\":\"#808080\"}},{\"pgs\":{\"bC\":\"#808080\"}}]},\"combinedItems\":1,\"iExec\":125,\"bottomButtons\":{\"items\":[{\"fader\":{\"v\":1.000,\"bC\":\"#808000\"},\"n\":{\"t\":\"Toggle\",\"c\":\"#C0C0C0\"}}]}},{\"i\":{\"t\":\"127\",\"c\":\"#C0C0C0\"},\"oType\":{\"t\":\"  LAS\",\"c\":\"#FFFFFF\"},\"oI\":{\"t\":\"37\",\"c\":\"#FFFFFF\"},\"tt\":{\"t\":\"G Toggle\",\"c\":\"#FFFFFF\"},\"bC\":\"#000000\",\"bdC\":\"#FF80FF\",\"cues\":{\"bC\":\"#3F203F\",\"items\":[{\"t\":\"60.0 BPM\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":0.267,\"bC\":\"#0000FF\"}},{\"t\":\"0.0 s\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":1.000,\"bC\":\"#0000FF\"}},{\"pgs\":{\"v\":0.000,\"bC\":\"#0000FF\"}}]},\"combinedItems\":1,\"iExec\":126,\"bottomButtons\":{\"items\":[{\"fader\":{\"v\":1.000,\"bC\":\"#808000\"},\"n\":{\"t\":\"Toggle\",\"c\":\"#C0C0C0\"}}]}},{\"i\":{\"t\":\"128\",\"c\":\"#C0C0C0\"},\"oType\":{\"t\":\"  LAS\",\"c\":\"#FFFFFF\"},\"oI\":{\"t\":\"38\",\"c\":\"#FFFFFF\"},\"tt\":{\"t\":\"B Toggle\",\"c\":\"#FFFFFF\"},\"bC\":\"#000000\",\"bdC\":\"#FF80FF\",\"cues\":{\"bC\":\"#3F203F\",\"items\":[{\"t\":\"60.0 BPM\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":0.267,\"bC\":\"#808080\"}},{\"t\":\"0.0 s\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":1.000,\"bC\":\"#808080\"}},{\"pgs\":{\"bC\":\"#808080\"}}]},\"combinedItems\":1,\"iExec\":127,\"bottomButtons\":{\"items\":[{\"fader\":{\"v\":1.000,\"bC\":\"#808000\"},\"n\":{\"t\":\"Toggle\",\"c\":\"#C0C0C0\"}}]}},{\"i\":{\"t\":\"129\",\"c\":\"#C0C0C0\"},\"oType\":{\"t\":\"  \",\"c\":\"#FFFFFF\"},\"oI\":{\"t\":\"5\",\"c\":\"#FFFFFF\"},\"tt\":{\"t\":\"LED Par Off\",\"c\":\"#FFFFFF\"},\"bC\":\"#000000\",\"bdC\":\"#FF0000\",\"cues\":{\"bC\":\"#3F0000\",\"items\":[{\"c\":\"#FFFFFF\",\"pgs\":{}}]},\"combinedItems\":1,\"iExec\":128,\"bottomButtons\":{\"items\":[{\"n\":{\"t\":\"Go\",\"c\":\"#C0C0C0\"}}]}},{\"i\":{\"t\":\"130\",\"c\":\"#000000\"},\"oType\":{\"t\":\"\"},\"oI\":{\"t\":\"\"},\"tt\":{\"t\":\"\"},\"bC\":\"#404040\",\"bdC\":\"#3D3D3D\",\"cues\":{},\"combinedItems\":1,\"iExec\":129}],[{\"i\":{\"t\":\"131\",\"c\":\"#C0C0C0\"},\"oType\":{\"t\":\"  LAS\",\"c\":\"#FFFFFF\"},\"oI\":{\"t\":\"47\",\"c\":\"#FFFFFF\"},\"tt\":{\"t\":\"R Sin\",\"c\":\"#FFFFFF\"},\"bC\":\"#000000\",\"bdC\":\"#FF80FF\",\"cues\":{\"bC\":\"#3F203F\",\"items\":[{\"t\":\"60.0 BPM\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":0.267,\"bC\":\"#808080\"}},{\"t\":\"0.0 s\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":1.000,\"bC\":\"#808080\"}},{\"pgs\":{\"bC\":\"#808080\"}}]},\"combinedItems\":1,\"iExec\":130,\"bottomButtons\":{\"items\":[{\"fader\":{\"v\":1.000,\"bC\":\"#808000\"},\"n\":{\"t\":\"Toggle\",\"c\":\"#C0C0C0\"}}]}},{\"i\":{\"t\":\"132\",\"c\":\"#C0C0C0\"},\"oType\":{\"t\":\"  LAS\",\"c\":\"#FFFFFF\"},\"oI\":{\"t\":\"48\",\"c\":\"#FFFFFF\"},\"tt\":{\"t\":\"G Sin\",\"c\":\"#FFFFFF\"},\"bC\":\"#000000\",\"bdC\":\"#FF80FF\",\"cues\":{\"bC\":\"#3F203F\",\"items\":[{\"t\":\"60.0 BPM\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":0.267,\"bC\":\"#808080\"}},{\"t\":\"0.0 s\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":1.000,\"bC\":\"#808080\"}},{\"pgs\":{\"bC\":\"#808080\"}}]},\"combinedItems\":1,\"iExec\":131,\"bottomButtons\":{\"items\":[{\"fader\":{\"v\":1.000,\"bC\":\"#808000\"},\"n\":{\"t\":\"Toggle\",\"c\":\"#C0C0C0\"}}]}},{\"i\":{\"t\":\"133\",\"c\":\"#C0C0C0\"},\"oType\":{\"t\":\"  LAS\",\"c\":\"#FFFFFF\"},\"oI\":{\"t\":\"49\",\"c\":\"#FFFFFF\"},\"tt\":{\"t\":\"B Sin\",\"c\":\"#FFFFFF\"},\"bC\":\"#000000\",\"bdC\":\"#FF80FF\",\"cues\":{\"bC\":\"#3F203F\",\"items\":[{\"t\":\"60.0 BPM\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":0.267,\"bC\":\"#808080\"}},{\"t\":\"0.0 s\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":1.000,\"bC\":\"#808080\"}},{\"pgs\":{\"bC\":\"#808080\"}}]},\"combinedItems\":1,\"iExec\":132,\"bottomButtons\":{\"items\":[{\"fader\":{\"v\":1.000,\"bC\":\"#808000\"},\"n\":{\"t\":\"Toggle\",\"c\":\"#C0C0C0\"}}]}},{\"i\":{\"t\":\"134\",\"c\":\"#000000\"},\"oType\":{\"t\":\"\"},\"oI\":{\"t\":\"\"},\"tt\":{\"t\":\"\"},\"bC\":\"#404040\",\"bdC\":\"#3D3D3D\",\"cues\":{},\"combinedItems\":1,\"iExec\":133},{\"i\":{\"t\":\"135\",\"c\":\"#C0C0C0\"},\"oType\":{\"t\":\"  LAS\",\"c\":\"#FFFFFF\"},\"oI\":{\"t\":\"51\",\"c\":\"#FFFFFF\"},\"tt\":{\"t\":\"Dimmer Chase\",\"c\":\"#FFFFFF\"},\"bC\":\"#000000\",\"bdC\":\"#FF80FF\",\"cues\":{\"bC\":\"#3F203F\",\"items\":[{\"t\":\"60.0 BPM\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":0.267,\"bC\":\"#808080\"}},{\"t\":\"0.0 s\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":1.000,\"bC\":\"#808080\"}},{\"pgs\":{\"bC\":\"#808080\"}}]},\"combinedItems\":1,\"iExec\":134,\"bottomButtons\":{\"items\":[{\"fader\":{\"v\":1.000,\"bC\":\"#808000\"},\"n\":{\"t\":\"Toggle\",\"c\":\"#C0C0C0\"}}]}}],[{\"i\":{\"t\":\"136\",\"c\":\"#C0C0C0\"},\"oType\":{\"t\":\"  LAS\",\"c\":\"#FFFFFF\"},\"oI\":{\"t\":\"52\",\"c\":\"#FFFFFF\"},\"tt\":{\"t\":\"Dimmer Random\",\"c\":\"#FFFFFF\"},\"bC\":\"#000000\",\"bdC\":\"#FF80FF\",\"cues\":{\"bC\":\"#3F203F\",\"items\":[{\"t\":\"60.0 BPM\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":0.267,\"bC\":\"#808080\"}},{\"t\":\"0.0 s\",\"c\":\"#FFFFFF\",\"pgs\":{\"v\":1.000,\"bC\":\"#808080\"}},{\"pgs\":{\"bC\":\"#808080\"}}]},\"combinedItems\":1,\"iExec\":135,\"bottomButtons\":{\"items\":[{\"fader\":{\"v\":1.000,\"bC\":\"#808000\"},\"n\":{\"t\":\"Toggle\",\"c\":\"#C0C0C0\"}}]}},{\"i\":{\"t\":\"137\",\"c\":\"#000000\"},\"oType\":{\"t\":\"\"},\"oI\":{\"t\":\"\"},\"tt\":{\"t\":\"\"},\"bC\":\"#404040\",\"bdC\":\"#3D3D3D\",\"cues\":{},\"combinedItems\":1,\"iExec\":136},{\"i\":{\"t\":\"138\",\"c\":\"#000000\"},\"oType\":{\"t\":\"\"},\"oI\":{\"t\":\"\"},\"tt\":{\"t\":\"\"},\"bC\":\"#404040\",\"bdC\":\"#3D3D3D\",\"cues\":{},\"combinedItems\":1,\"iExec\":137},{\"i\":{\"t\":\"139\",\"c\":\"#C0C0C0\"},\"oType\":{\"t\":\"  \",\"c\":\"#FFFFFF\"},\"oI\":{\"t\":\"6\",\"c\":\"#FFFFFF\"},\"tt\":{\"t\":\"LED Floor Off\",\"c\":\"#FFFFFF\"},\"bC\":\"#000000\",\"bdC\":\"#FF0000\",\"cues\":{\"bC\":\"#3F0000\",\"items\":[{\"c\":\"#FFFFFF\",\"pgs\":{}}]},\"combinedItems\":1,\"iExec\":138,\"bottomButtons\":{\"items\":[{\"n\":{\"t\":\"Go\",\"c\":\"#C0C0C0\"}}]}},{\"i\":{\"t\":\"140\",\"c\":\"#000000\"},\"oType\":{\"t\":\"\"},\"oI\":{\"t\":\"\"},\"tt\":{\"t\":\"\"},\"bC\":\"#404040\",\"bdC\":\"#3D3D3D\",\"cues\":{},\"combinedItems\":1,\"iExec\":139}],[{\"i\":{\"t\":\"141\",\"c\":\"#000000\"},\"oType\":{\"t\":\"\"},\"oI\":{\"t\":\"\"},\"tt\":{\"t\":\"\"},\"bC\":\"#404040\",\"bdC\":\"#3D3D3D\",\"cues\":{},\"combinedItems\":1,\"iExec\":140},{\"i\":{\"t\":\"142\",\"c\":\"#000000\"},\"oType\":{\"t\":\"\"},\"oI\":{\"t\":\"\"},\"tt\":{\"t\":\"\"},\"bC\":\"#404040\",\"bdC\":\"#3D3D3D\",\"cues\":{},\"combinedItems\":1,\"iExec\":141},{\"i\":{\"t\":\"143\",\"c\":\"#000000\"},\"oType\":{\"t\":\"\"},\"oI\":{\"t\":\"\"},\"tt\":{\"t\":\"\"},\"bC\":\"#404040\",\"bdC\":\"#3D3D3D\",\"cues\":{},\"combinedItems\":1,\"iExec\":142},{\"i\":{\"t\":\"144\",\"c\":\"#000000\"},\"oType\":{\"t\":\"\"},\"oI\":{\"t\":\"\"},\"tt\":{\"t\":\"\"},\"bC\":\"#404040\",\"bdC\":\"#3D3D3D\",\"cues\":{},\"combinedItems\":1,\"iExec\":143},{\"i\":{\"t\":\"145\",\"c\":\"#000000\"},\"oType\":{\"t\":\"\"},\"oI\":{\"t\":\"\"},\"tt\":{\"t\":\"\"},\"bC\":\"#404040\",\"bdC\":\"#3D3D3D\",\"cues\":{},\"combinedItems\":1,\"iExec\":144}],[{\"i\":{\"t\":\"146\",\"c\":\"#000000\"},\"oType\":{\"t\":\"\"},\"oI\":{\"t\":\"\"},\"tt\":{\"t\":\"\"},\"bC\":\"#404040\",\"bdC\":\"#3D3D3D\",\"cues\":{},\"combinedItems\":1,\"iExec\":145},{\"i\":{\"t\":\"147\",\"c\":\"#000000\"},\"oType\":{\"t\":\"\"},\"oI\":{\"t\":\"\"},\"tt\":{\"t\":\"\"},\"bC\":\"#404040\",\"bdC\":\"#3D3D3D\",\"cues\":{},\"combinedItems\":1,\"iExec\":146},{\"i\":{\"t\":\"148\",\"c\":\"#000000\"},\"oType\":{\"t\":\"\"},\"oI\":{\"t\":\"\"},\"tt\":{\"t\":\"\"},\"bC\":\"#404040\",\"bdC\":\"#3D3D3D\",\"cues\":{},\"combinedItems\":1,\"iExec\":147},{\"i\":{\"t\":\"149\",\"c\":\"#000000\"},\"oType\":{\"t\":\"\"},\"oI\":{\"t\":\"\"},\"tt\":{\"t\":\"\"},\"bC\":\"#404040\",\"bdC\":\"#3D3D3D\",\"cues\":{},\"combinedItems\":1,\"iExec\":148},{\"i\":{\"t\":\"150\",\"c\":\"#000000\"},\"oType\":{\"t\":\"\",\"c\":\"#FFFFFF\"},\"oI\":{\"t\":\"\",\"c\":\"#FFFFFF\"},\"tt\":{\"t\":\"\",\"c\":\"#FFFFFF\"},\"bC\":\"#404040\",\"bdC\":\"#404040\",\"cues\":{},\"combinedItems\":1,\"iExec\":149,\"bottomButtons\":{\"items\":[{\"fader\":{\"bC\":\"#808000\"},\"n\":{\"t\":\"Go\",\"c\":\"#C0C0C0\"}}]}}],[{\"i\":{\"t\":\"151\",\"c\":\"#000000\"},\"oType\":{\"t\":\"\"},\"oI\":{\"t\":\"\"},\"tt\":{\"t\":\"\"},\"bC\":\"#404040\",\"bdC\":\"#3D3D3D\",\"cues\":{},\"combinedItems\":1,\"iExec\":150},{\"i\":{\"t\":\"152\",\"c\":\"#000000\"},\"oType\":{\"t\":\"\"},\"oI\":{\"t\":\"\"},\"tt\":{\"t\":\"\"},\"bC\":\"#404040\",\"bdC\":\"#3D3D3D\",\"cues\":{},\"combinedItems\":1,\"iExec\":151},{\"i\":{\"t\":\"153\",\"c\":\"#000000\"},\"oType\":{\"t\":\"\"},\"oI\":{\"t\":\"\"},\"tt\":{\"t\":\"\"},\"bC\":\"#404040\",\"bdC\":\"#3D3D3D\",\"cues\":{},\"combinedItems\":1,\"iExec\":152},{\"i\":{\"t\":\"154\",\"c\":\"#000000\"},\"oType\":{\"t\":\"\"},\"oI\":{\"t\":\"\"},\"tt\":{\"t\":\"\"},\"bC\":\"#404040\",\"bdC\":\"#3D3D3D\",\"cues\":{},\"combinedItems\":1,\"iExec\":153},{\"i\":{\"t\":\"155\",\"c\":\"#000000\"},\"oType\":{\"t\":\"\"},\"oI\":{\"t\":\"\"},\"tt\":{\"t\":\"\"},\"bC\":\"#404040\",\"bdC\":\"#3D3D3D\",\"cues\":{},\"combinedItems\":1,\"iExec\":154}],[{\"i\":{\"t\":\"156\",\"c\":\"#000000\"},\"oType\":{\"t\":\"\"},\"oI\":{\"t\":\"\"},\"tt\":{\"t\":\"\"},\"bC\":\"#404040\",\"bdC\":\"#3D3D3D\",\"cues\":{},\"combinedItems\":1,\"iExec\":155},{\"i\":{\"t\":\"157\",\"c\":\"#000000\"},\"oType\":{\"t\":\"\"},\"oI\":{\"t\":\"\"},\"tt\":{\"t\":\"\"},\"bC\":\"#404040\",\"bdC\":\"#3D3D3D\",\"cues\":{},\"combinedItems\":1,\"iExec\":156},{\"i\":{\"t\":\"158\",\"c\":\"#000000\"},\"oType\":{\"t\":\"\"},\"oI\":{\"t\":\"\"},\"tt\":{\"t\":\"\"},\"bC\":\"#404040\",\"bdC\":\"#3D3D3D\",\"cues\":{},\"combinedItems\":1,\"iExec\":157},{\"i\":{\"t\":\"159\",\"c\":\"#000000\"},\"oType\":{\"t\":\"\"},\"oI\":{\"t\":\"\"},\"tt\":{\"t\":\"\"},\"bC\":\"#404040\",\"bdC\":\"#3D3D3D\",\"cues\":{},\"combinedItems\":1,\"iExec\":158},{\"i\":{\"t\":\"160\",\"c\":\"#000000\"},\"oType\":{\"t\":\"\"},\"oI\":{\"t\":\"\"},\"tt\":{\"t\":\"\"},\"bC\":\"#404040\",\"bdC\":\"#3D3D3D\",\"cues\":{},\"combinedItems\":1,\"iExec\":159}]],\"worldIndex\":0}"));
		JsonObject jsonObject = jsonReader.readObject();
		System.out.println("Size: " + jsonObject.getJsonArray("items").size());
		
		JsonArray itemsArray = jsonObject.getJsonArray("items");
		for(int i=0; i < itemsArray.size(); i++) {
			JsonArray blockArray = itemsArray.getJsonArray(i);
			for(int block=0; block < blockArray.size(); block++) {
				JsonObject jsonExecuter = blockArray.getJsonObject(block);
				
				System.out.println("Exectuer: " + jsonExecuter.getJsonObject("i").getJsonString("t").getString() );
				System.out.println(" -> Name: " + jsonExecuter.getJsonObject("tt").getJsonString("t").getString() );
				
			}
		}
		
	}
	

}
