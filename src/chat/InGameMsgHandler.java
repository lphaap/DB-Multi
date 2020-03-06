package chat;
import java.util.ArrayList;

import org.dreambot.api.wrappers.widgets.message.Message;

import antiban.RandomProvider;
import client.ClientThread;
import client.ThreadController;

public class InGameMsgHandler {
	private ClientThread client;
	private ThreadController controller;
	private ArrayList<String> greetingMsg = new ArrayList<String>();
	private ArrayList<String> worldSwapMsg = new ArrayList<String>();
	private boolean autoReact;
	
	
	public InGameMsgHandler(ClientThread client, ThreadController controller) {
		this.client = client;
		this.controller = controller;
	}
	
	/**
	 * Processes the message produced by client
	 * @.pre origin != null && msg != null
	 * @.post (Actions correct to the given message and type)
	 */
	public void processMessage(MsgOrigin origin, Message msg) {
		switch(origin) {
		
			case PLAYER:
				processPlayer(msg);
				break;
				
			case FRIEND:
				//TODO:processFriend(msg);
				break;
			
			case CLAN:
				//TODO:processClan(msg);
				break;
				
			case GAME:
				
				//TODO:processGame(msg);
				break;
		}
		
	}
	
	
	private void processPlayer(Message msg) {
		if(filterSpaceAndCaps(msg.getMessage()).contains("bot")) {
			controller.hopWorlds();
		}
		else if(autoReact) {
			sendMsgInGame(greetingMsg.get(RandomProvider.randomInt(greetingMsg.size())));
			sendMsgInGame(worldSwapMsg.get(RandomProvider.randomInt(greetingMsg.size())));
			controller.hopWorlds();
		}
		else {
			controller.getTelegramHandler().sendMessage("Msg From " + msg.getUsername() + ": " +  msg.getMessage());
		}
	}
	
	private void processFriend(Message msg) {
		System.out.println("NOT IMPLEMENTED YET");
	}
	
	private void processGame(Message msg) {
		System.out.println("NOT IMPLEMENTED YET");
	}
	
	private void processClan(Message msg) {
		System.out.println("NOT IMPLEMENTED YET");
	}
	
	public enum MsgOrigin{
		PLAYER,FRIEND,CLAN,GAME
	}
	
	public void sendMsgInGame(String msg) {
		while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
		client.getKeyboard().type(msg, true, true);
		controller.returnKeyboardAccess();
	}
	
	public String filterSpaceAndCaps(String msg) {
		StringBuffer filter = new StringBuffer(msg.toLowerCase());
		while(filter.indexOf(" ") != -1){
			filter.deleteCharAt(filter.indexOf(" "));
		} 
		return filter.toString();
	}
	
	public void addGreetings() {
		greetingMsg.add("Supp m8");
		greetingMsg.add("Sup m8");
		greetingMsg.add("Sup dude");
		greetingMsg.add("Hello friend");
		greetingMsg.add("Hi");
		greetingMsg.add("Hello");
		greetingMsg.add("Sup");
		greetingMsg.add("Gday");
		greetingMsg.add("Yello");
	}
	
	public void addWorldSwappers() {
		worldSwapMsg.add("I'll swap m8");
		worldSwapMsg.add("Ill swap");
		worldSwapMsg.add("Imma swap");
		worldSwapMsg.add("I will swap");
		worldSwapMsg.add("I'll hop");
		worldSwapMsg.add("Ill Hop m8");
		worldSwapMsg.add("Imma swap worlds");
		worldSwapMsg.add("Imma hop worlds");
		worldSwapMsg.add("Imma hop");
		worldSwapMsg.add("Well ill hop");
		worldSwapMsg.add("Well ill change");
		worldSwapMsg.add("Changing m8");
		worldSwapMsg.add("Hopping m8");
		worldSwapMsg.add("Bruh.");
		worldSwapMsg.add("m8 plox");
		worldSwapMsg.add("Alright.. Ill hop");
		worldSwapMsg.add("...");
		worldSwapMsg.add("....");
		worldSwapMsg.add("Wowee ill hop");
	}
	
	//Returns the new value of autoReact
	public boolean toggleAutoReact() {
		this.autoReact = !autoReact;
		return autoReact;
	}
}
