package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;


public class SimpleServer extends AbstractServer {
	private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();
	private int joined = 0;
	private final String[][] XO = new String[3][3]; // Matrix to save the X-O cells
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
			joined ++;
            if(SubscribersList.size() == 2 && joined == 2){
				// Reset the matrix
				for (int i = 0; i < XO.length; i++) {         // Loop through rows
					for (int j = 0; j < XO[i].length; j++) {  // Loop through columns
						XO[i][j] = null;                      // Set each cell to null
					}
				}
				turn = 1; // Reset turn counter
				joined = 0; // Reset the number of joined players
				// Giving X or O to the players randomly by changing their places in the list according to randomIndex
				Random rand = new Random();
				int randomIndex = rand.nextInt(2); // Generates a random number: 0 or 1

				// If randomIndex is 0, the object at index 1 goes to index 0, otherwise it goes to index 1
				if (randomIndex == 0) {
					SubscribedClient temp = SubscribersList.get(1);
					SubscribersList.set(1, SubscribersList.get(0));
					SubscribersList.set(0, temp);
				}
				sendToAllClients("The game is ready");
				handleGameTurns();
            }
        }
		// The choice from the Player
		else if(msgString.length() == 1){
			for (int i = 0; i < SubscribersList.size(); i++) {
				SubscribedClient subscribedClient = SubscribersList.get(i);
				if(subscribedClient.getClient().equals(client)) {
					int cell = Integer.parseInt(msgString) -1;
					int col = cell % 3;
					int row = cell / 3;
					if(i==0){
						XO[row][col] = "X";
						sendToAllClients("Chosen " + msgString + " X");
					}
					else if(i==1){
						XO[row][col] = "O";
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
						SubscribersList.get(1).getClient().sendToClient("You Lose!");
                    }
					else{
						SubscribersList.getFirst().getClient().sendToClient("You Lose!");
						SubscribersList.get(1).getClient().sendToClient("You Won!");
                    }
                    return;
                }
			}
			if(turn == 10){
				sendToAllClients("Draw");
				return;
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
