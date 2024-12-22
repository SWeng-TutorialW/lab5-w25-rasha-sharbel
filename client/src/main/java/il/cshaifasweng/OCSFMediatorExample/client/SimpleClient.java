package il.cshaifasweng.OCSFMediatorExample.client;

import org.greenrobot.eventbus.EventBus;
import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;

public class SimpleClient extends AbstractClient {

	public static SimpleClient client = null;
	public static int port = 3000;
	public static String ip = "local host";

	private SimpleClient(String host, int port) {
		super(host, port);
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
		else if(message.equals("Start") || message.equals("Terminate")){
			EventBus.getDefault().post(message);
		}
		else if(message.startsWith("Chosen")){
			EventBus.getDefault().post(message);
		}
		else if(message.equals("Play")){
			EventBus.getDefault().post(message);
		}
		else if(message.equals("Wait")){
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

	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient(ip, port);
		}
		return client;
	}

}