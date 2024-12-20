package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.io.IOException;
import java.util.ArrayList;

import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;

public class SimpleServer extends AbstractServer {
	private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();
	private String[][] XO = new String[3][3]; // Matrix to save the X-O cells
	private int turn = 1; // turns counter

	public SimpleServer(int port) {
		super(port);
		
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		String msgString = msg.toString();
		if (msgString.startsWith("#warning")) {
			Warning warning = new Warning("Warning from server!");
			try {
				client.sendToClient(warning);
				System.out.format("Sent warning to client %s\n", client.getInetAddress().getHostAddress());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if(msgString.startsWith("add client")){
			SubscribedClient connection = new SubscribedClient(client);
			SubscribersList.add(connection);
			try {
				client.sendToClient("client added successfully");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		else if(msgString.startsWith("#join")){
            if(SubscribersList.size() != 2){
                return;
            }
            else{
                sendToAllClients("The game is ready");
				handleGameTurns();
            }
        }
		// The choice from the Player
		else if(msgString.length() == 1){
			for (int i = 0; i < SubscribersList.size(); i++) {
				SubscribedClient subscribedClient = SubscribersList.get(i);
				if(subscribedClient.getClient().equals(client)) {
					if(i==0){
						sendToAllClients("Chosen " + msgString + " X");
					}
					else if(i==1){
						sendToAllClients("Chosen " + msgString + " O");
					}
					handleGameTurns();
				}
			}
		}
		else if(msgString.startsWith("remove client")){
			if(!SubscribersList.isEmpty()){
				for(SubscribedClient subscribedClient: SubscribersList){
					if(subscribedClient.getClient().equals(client)){
						SubscribersList.remove(subscribedClient);
						if(SubscribersList.size() == 1){
							sendToAllClients("Terminate");
						}
						break;
					}
				}
			}
		}
	}

	public void sendToAllClients(String message) {
		try {
			for (SubscribedClient subscribedClient : SubscribersList) {
				subscribedClient.getClient().sendToClient(message);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	void handleGameTurns(){
		ConnectionToClient clientToPlay;
		ConnectionToClient clientToWait;
		String winnerResult;

		try {
		if(turn >= 5){ // Start check if there is a winner
			winnerResult = checkWinner();
			if(winnerResult != null){
				if(winnerResult.equals("X")){
					SubscribersList.getFirst().getClient().sendToClient("You Won!");
					SubscribersList.getFirst().getClient().sendToClient("You Lose!");
					return;
				}
				else{
					SubscribersList.getFirst().getClient().sendToClient("You Lose!");
					SubscribersList.getFirst().getClient().sendToClient("You Won!");
					return;
				}
			}
		}
		if(turn %2 == 1){
			clientToPlay = SubscribersList.getFirst().getClient();
			clientToWait = SubscribersList.get(1).getClient();
		}
		else {
			clientToPlay = SubscribersList.get(1).getClient();
			clientToWait = SubscribersList.getFirst().getClient();
		}
		turn ++;
			clientToPlay.sendToClient("Play");
			clientToWait.sendToClient("Wait");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String checkWinner(){
		// Check rows
		for (int i = 0; i < 3; i++) {
			if (XO[i][0] != null && XO[i][0].equals(XO[i][1]) && XO[i][1].equals(XO[i][2])) {
				return XO[i][0]; // Return "X" or "O" as the winner
			}
		}

		// Check columns
		for (int i = 0; i < 3; i++) {
			if (XO[0][i] != null && XO[0][i].equals(XO[1][i]) && XO[1][i].equals(XO[2][i])) {
				return XO[0][i]; // Return "X" or "O" as the winner
			}
		}

		// Check diagonal (top-left to bottom-right)
		if (XO[0][0] != null && XO[0][0].equals(XO[1][1]) && XO[1][1].equals(XO[2][2])) {
			return XO[0][0]; // Return "X" or "O" as the winner
		}

		// Check diagonal (top-right to bottom-left)
		if (XO[0][2] != null && XO[0][2].equals(XO[1][1]) && XO[1][1].equals(XO[2][0])) {
			return XO[0][2]; // Return "X" or "O" as the winner
		}

		return null; // no winner
	}
}
