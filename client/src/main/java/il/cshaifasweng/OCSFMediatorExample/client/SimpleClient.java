package il.cshaifasweng.OCSFMediatorExample.client;

import org.greenrobot.eventbus.EventBus;
import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import java.io.IOException;
import org.greenrobot.eventbus.Subscribe;

public class SimpleClient extends AbstractClient {

	private static SimpleClient client = null;

	private SimpleClient(String host, int port) {
		super(host, port);
		EventBus.getDefault().register(this);
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		String message = msg.toString();
		if (msg.getClass().equals(Warning.class)) {
			EventBus.getDefault().post(new WarningEvent((Warning) msg));
		}
		else if(message.equals("client added successfully")){
 			System.out.println("You have been added to the server");
 		}
		else if(message.equals("The game is ready")){
			System.out.println("Start");
			EventBus.getDefault().post("Start");
			System.out.println("reached");
		}
		else if(message.startsWith("Chosen")){
			System.out.println("A cell has been chosen");
			EventBus.getDefault().post(message);
		}
		else if(message.equals("Terminate")){
			System.out.println("Terminating");
			EventBus.getDefault().post(message);
		}
		else if(message.equals("Play")){
			try {
				Thread.sleep(500); // Sleep for 500 milliseconds
			} catch (InterruptedException e) {
				// Handle the exception if the thread is interrupted
				e.printStackTrace();
			}
			System.out.println("Your turn");
			EventBus.getDefault().post(message);
		}
		else if(message.equals("Wait")){
			System.out.println("Your opponent turn");
			EventBus.getDefault().post(message);
		}
		else if(message.equals("You Won!") || message.equals("You Lose!") || message.equals("Draw")){
			System.out.println(message);
			EventBus.getDefault().post(message);
		}
		else{
			System.out.println(message);
		}
	}

	@Subscribe
	public void choice(Object obj){
		if(!(obj instanceof String) || (((String) obj).length()) != 1){
			return;
		}
		try {
			client.sendToServer(obj);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3000);
		}
		return client;
	}

}