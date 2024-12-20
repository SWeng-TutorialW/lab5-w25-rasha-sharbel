package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Platform;
import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import java.io.IOException;
import org.greenrobot.eventbus.Subscribe;

public class SimpleClient extends AbstractClient {

	private static SimpleClient client = null;

	private SimpleClient(String host, int port) {
		super(host, port);
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		String message = msg.toString();
		System.out.println(message);
		if (msg.getClass().equals(Warning.class)) {
			EventBus.getDefault().post(new WarningEvent((Warning) msg));
		}
		else if(message.equals("The game is ready")){
			EventBus.getDefault().post("Start");
		}
		else if(message.startsWith("Chosen")){
			EventBus.getDefault().post(message);
		}
		else if(message.equals("Terminate")){
			EventBus.getDefault().post(message);
		}
		else if(message.equals("Play")){
			EventBus.getDefault().post(message);
		}
		else if(message.equals("Wait")){
			EventBus.getDefault().post(message);
		}
		else if(message.equals("You Won!") || message.equals("You Lose!")){
			EventBus.getDefault().post(message);
		}
		else{
			System.out.println(message);
		}
	}

	@Subscribe
	public void choice(Object obj){
		if(!(obj instanceof Character)){
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