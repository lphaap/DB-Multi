import org.dreambot.api.wrappers.widgets.message.Message;

public class ChatResponder {
	private TelegramChat telegram;
	private MainLooper script;
	private Main
	
	public ChatResponder(MainLooper script, TelegramChat telegram) {
		this.telegram = telegram;
		this.script = script;
	}
	
	/**
	 * Processes the message produced by client
	 * @.pre origin != null && msg != null
	 * @.post (Actions correct to the given message and type)
	 */
	public void processMessage(MsgOrigin origin, Message msg) {
		String sender = msg.getUsername();
		StringBuffer filter = new StringBuffer(msg.getMessage().toLowerCase());
		while(filter.indexOf(" ") != -1){
			filter.deleteCharAt(filter.indexOf(" "));
		} 
		String message = filter.toString();
		switch(origin) {
		
			case PLAYER:
				processPlayer(message,sender);
				break;
				
			case FRIEND:
				processFriend(message,sender);
				break;
			
			case CLAN:
				processClan(message,sender);
				break;
				
			case GAME:
				processGame(message,sender);
				break;
		}
		
	}
	
	private void processPlayer(String filteredMsg, String sender) {
		
	}
	
	private void processFriend(String filteredMsg, String sender) {
			
	}
	
	private void processGame(String filteredMsg, String sender) {
		
	}
	
	private void processClan(String filteredMsg, String sender) {
		
	}
	
	public enum MsgOrigin{
		PLAYER,FRIEND,CLAN,GAME
	}
}
