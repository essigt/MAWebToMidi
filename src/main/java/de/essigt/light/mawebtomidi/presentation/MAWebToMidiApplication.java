package de.essigt.light.mawebtomidi.presentation;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import de.essigt.light.mawebtomidi.business.boundary.ExecuterManager;
import de.essigt.light.mawebtomidi.business.boundary.MAWeb3_2_2_16;
import de.essigt.light.mawebtomidi.business.controll.JsonParser;
import de.essigt.light.mawebtomidi.business.entity.Executer;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 * 
 * @author essigt
 *
 */
public class MAWebToMidiApplication extends Application {

	private JsonParser parser = new JsonParser();
	
	private ExecuterManager executerManager = new ExecuterManager();

	private Timeline timeline;


	@FXML
	private TextField txtUsername;
	
	@FXML
	private TextField txtPassword;
	
	@FXML
	private TextField txtIP;
	
	@FXML
	private Label lblStatus;
	
	private MAWeb3_2_2_16 maWeb = new MAWeb3_2_2_16();

	private Parent root;


	@FXML
	public void handleTestConnectionButtonAction(ActionEvent event) {
				
		System.out.println("Connect to: " + txtIP.getText() + " as " + txtUsername.getText());
		
		lblStatus.setText("Connecting...");
		boolean result = maWeb.connect("localhost", txtUsername.getText(), txtPassword.getText());
		
		
		
		System.out.println("Connection: " + result);
		
	}
	
	@Override
	public void init() {
		
	}


	@Override
	public void start(Stage stage) throws IOException {
		root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
	    
        Scene scene = new Scene(root, 600, 400);
    
        stage.setTitle("MAWebToMidi");
        stage.setScene(scene);
        stage.show();

		
		timeline = new Timeline(new KeyFrame(
				Duration.millis(2500),
				ae -> doSomething()));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}


	private void doSomething() {
		List<Executer> executers;
		try {
			//TODO: Use result from WS
			executers = parser.parseExecuterButtons(Files.readAllLines(Paths.get("/media/tim/9E48542648540007/Users/tessi/Documents/GitHub/MAWebToMidi/src/test/resources/", "responce_execzterbuttons_127running.json")).stream().collect(Collectors.joining()));

			

			for (Executer exec : executers) {
				if (!exec.getName().isEmpty()) {
					System.out.println("Executer: " + exec.getId() + "(" + exec.getName() + "): running=" + exec.isRunning());
					if(executerManager.containsConfiguration(exec.getId())) {
						executerManager.updateExecuter(exec);
					}
					
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	@Override
	public void stop() {
		timeline.stop();
	}


	public static void main(String[] parameters) {
		launch(parameters);
	}
}