package de.essigt.light.mawebtomidi.business.controll;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import de.essigt.light.mawebtomidi.business.entity.Executer;

public class JsonParser {

	
	
	public List<Executer> parseExecuterButtons(String m) {
		List<Executer> execs = new ArrayList<>();
		
		JsonReader jsonReader = Json.createReader(new StringReader(m));
		JsonObject jsonObject = jsonReader.readObject();
			
		JsonArray itemsArray = jsonObject.getJsonArray("items");
		for(int i=0; i < itemsArray.size(); i++) {
			JsonArray blockArray = itemsArray.getJsonArray(i);
			for(int block=0; block < blockArray.size(); block++) {
				JsonObject jsonExecuter = blockArray.getJsonObject(block);
				String id = jsonExecuter.getJsonObject("i").getJsonString("t").getString();
				String name = jsonExecuter.getJsonObject("tt").getJsonString("t").getString();
				
				JsonObject jsonCues = jsonExecuter.getJsonObject("cues");
				
				boolean running = false;
				if(jsonCues.size() > 0) {
					JsonObject jsonPgs = jsonCues.getJsonArray("items").getJsonObject(0).getJsonObject("pgs");
					if(jsonPgs.containsKey("bC")) {
						String bgColor = jsonPgs.getJsonString("bC").getString();
						running = bgColor.equals("#0000FF");
					}
				}
				execs.add( new Executer( Integer.valueOf(id), name, running));

			}
		}
		
		return execs;
	}

	/**
	 * 
	 * @param msg
	 */
	public JsonObject parseGeneric(String msg) {
		try(JsonReader jsonReader = Json.createReader(new StringReader(msg))){
			return jsonReader.readObject();
		}
	}
}
