package chat;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.wrappers.items.Item;

import antiban.RandomProvider;
import client.ClientThread;
import client.ThreadController;
import net.dv8tion.jda.api.entities.Invite.Channel;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Discord extends ListenerAdapter{

	private ClientThread script;
	private ThreadController controller;
	private MessageChannel channel;
	private boolean connected;

	public Discord(ClientThread client, ThreadController controller) {
		this.script = client;
		this.controller = controller;
		
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		 if (true) {
		        
		        String text = event.getMessage().getContentRaw();
		        
		        if(text.startsWith("!connect")) {
		        	this.channel = event.getChannel();
		        	connected = true;
		        	
		        }
		        if(connected) {
			        if(text.startsWith("!send")) {	   
			        	controller.getMsgHandler().sendMsgInGame(text.substring(6));
			        }
			        else if(text.startsWith("!kill")) {
			        	channel.sendMessage("Killing client").queue();
			        	controller.killClient();
			        }
			        else if(text.startsWith("!next")) {
			        	channel.sendMessage("Swapping Module").queue();

			        	//TODO:
			        }
			        else if(text.startsWith("!stop")) {
			        	channel.sendMessage("Stopping Script").queue();
			        	controller.killBot();
			        }
			        else if(text.startsWith("!hop")) {
			        	channel.sendMessage("Hopping Worlds").queue();
			        	
			        	//TODO:
			        }
			        else if(text.startsWith("!current")) {
			        	channel.sendMessage(controller.getCurrentActionPrint()).queue();
			        	
			        }
			        else if(text.startsWith("!timeScript")) {
			        	channel.sendMessage(controller.getGraphicHandler().getScriptTimer()).queue();
			        	
			        }
			        else if(text.startsWith("!timePause")) {
			        	channel.sendMessage(controller.getGraphicHandler().getPauseTimer()).queue();
			        	
			        }
			        else if(text.startsWith("!autoreact")) {
			        	channel.sendMessage("Set AutoReact to: " + controller.getMsgHandler().toggleAutoReact()).queue();
			        	
			        }
			        else if(text.startsWith("!players")) {
			        	channel.sendMessage("Players in area: " + script.getPlayerCount()).queue();
			        }
			        else if(text.startsWith("!pause")) {
			        	channel.sendMessage("Logging Out...").queue();
			        	controller.pauseBot();
			        }
			        else if(text.startsWith("!restartModule")) {
			        	channel.sendMessage("Restarting Current Module").queue();
			        	controller.restartModule();
			        }
			        else if(text.startsWith("!location")) {
			        	channel.sendMessage("Cuurent Player Location - X: " + script.getLocalPlayer().getTile().getX() +" Y: " + script.getLocalPlayer().getTile().getY()).queue();
			        	
			        }
			        else if(text.startsWith("!hp")) {
			        	channel.sendMessage("Current Player Hitpoints: " + script.getSkills().getBoostedLevels(Skill.HITPOINTS)).queue();
			        	
			        }
			        else if(text.startsWith("!prayer")) {
			        	channel.sendMessage("Current Player Prayerpoints: " + script.getSkills().getBoostedLevels(Skill.PRAYER)).queue();
			        	
			        }
			        else if(text.startsWith("!gear")) {
			        	String msg = "Current Gear on Player:" + "\r\n";
			   
			        	for(Item i : script.getEquipment().all()) {
			        		if(i != null) {
			        			msg = msg + i.getName() + "\r\n";
			        		}
			        	}
			        	channel.sendMessage(msg).queue();
			        	
			        }
			        else if(text.startsWith("!inventory")) {
			        	String msg = "Current Inventory on Player:" + "\r\n";
			        	
			        	for(Item i : script.getInventory().all()) {
			        		if(i != null) {
			        			msg = msg + i.getName() + "\r\n";
			        		}
			        	}
			        	channel.sendMessage(msg).queue();
			        	
			        }
			        else if(text.startsWith("!") || text.startsWith("!help")){
			        	channel.sendMessage("Available Commands:"+"\r\n"+"#connect"+"\r\n"+"#kill"+"\r\n"+"#stop"+"\r\n"+"#next"+"\r\n"+"#send"+"\r\n"+"#timeScript"+"\r\n"+"#timePause"+"\r\n"+"#current"+"\r\n"+"#react <int>"+ "\r\n"+"#autoreact" +"\r\n"+"#players"+
			        					"\r\n"+"#logOut" + "\r\n"+ "#logIn"+ "\r\n"+"#togglePause"+ "\r\n"+"#restartModule"+ "\r\n"+"#hp"+ "\r\n"+"#prayer"+ "\r\n"+"#gear" + "\r\n"+"#inventory"+"\r\n"+"#location").queue();
			        	
			        	
			        }
		        }
		 
		 }
	
	}
	
	public void sendMessage(String msg) {
		if(connected) {
			channel.sendMessage(msg).queue();
		}
	}
	
	public void setScript(ClientThread script) {
		this.script = script;
	}


}
