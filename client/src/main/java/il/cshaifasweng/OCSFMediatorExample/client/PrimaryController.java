package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;



public class PrimaryController {

	@FXML
	private TextField ipField;

	@FXML
	private TextField portField;

	@FXML
	private Button startButton;

	@FXML
	void sendWarning(ActionEvent event) {
		try {
			SimpleClient.getClient().sendToServer("#warning");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void clientJoinRequest(ActionEvent event) {
		Platform.runLater(() -> {
			String ipv4 = ipField.getText();
			String portNum = portField.getText();
			if (isValidIPv4(ipv4) && isValidPort(portNum)) {
				try {
					// Updating the instance variables
					SimpleClient.port = Integer.parseInt(portNum);
					SimpleClient.ip = ipv4;
					// Create client
					SimpleClient.client = SimpleClient.getClient();
					SimpleClient.client.openConnection();
					SimpleClient.getClient().sendToServer("add client");
					Platform.runLater(() -> {
                        try {
                            App.setRoot();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else{
				ipField.setText("Invalid IPv4 or port number");
			}
		});
	}

	public boolean isValidPort(String portNum){
		try {
			int number = Integer.parseInt(portNum); // Parse string to integer
			return number >= 0 && number <= 65535; // Check if in range
		} catch (NumberFormatException e) {
			// Input is not a valid integer
			return false;
		}
	}

	public boolean isValidIPv4(String ip) {
		String ipv4Pattern = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
		return ip.matches(ipv4Pattern);
	}

	@FXML
	void initialize(){
		Platform.runLater(() -> {
		});
	}
}
