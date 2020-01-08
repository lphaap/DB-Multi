import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.wrappers.items.Item;

import net.dv8tion.jda.api.entities.Invite.Channel;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Discord extends ListenerAdapter{

	private MainLooper script;
	private MessageChannel channel;
	private boolean connected;

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
			        	//message.setText("Send msg: " + text.substring(6));
			        	
			        	script.getKeyboard().type(text.substring(6));
			        }
			        else if(text.startsWith("!kill")) {
			        	channel.sendMessage("Killing client").queue();
			        	script.stop();
			        	System.exit(0);
			        }
			        else if(text.startsWith("!next")) {
			        	channel.sendMessage("Swapping Module").queue();

			        	script.nextModule();
			        }
			        else if(text.startsWith("!stop")) {
			        	channel.sendMessage("Stopping Script").queue();
			        	
			        	script.stop();
			        }
			        else if(text.startsWith("!hop")) {
			        	channel.sendMessage("Hopping Worlds").queue();
			        	
			        	script.hop();
			        }
			        else if(text.startsWith("!current")) {
			        	channel.sendMessage(script.getCurrentAction()).queue();
			        	
			        }
			        else if(text.startsWith("!timeScript")) {
			        	channel.sendMessage(script.getTimer()).queue();
			        	
			        }
			        else if(text.startsWith("!timePause")) {
			        	channel.sendMessage(script.getPauseTimer()).queue();
			        	
			        }
			        else if(text.startsWith("!autoreact")) {
			        	channel.sendMessage("Set AutoReact to: " + script.swapAutoReact()).queue();
			        	
			        }
			        else if(text.startsWith("!react")) {
			        	channel.sendMessage("Setting react to: " + text.substring(7)).queue();
			     
			        	script.setReact(Integer.parseInt(text.substring(7)));
			        }
			        else if(text.startsWith("!players")) {
			        	channel.sendMessage("Players in area: " + script.getPlayerCount()).queue();
			        	
			        	script.setReact(Integer.parseInt(text.substring(7)));
			        }
			        else if(text.startsWith("!logOut")) {
			        	channel.sendMessage("Logging Out...").queue();
			        	
			        	script.logOut();
			        }
			        else if(text.startsWith("!logIn")) {
			        	channel.sendMessage("Logging In...").queue();
			        	
			        	script.logIn();
			        }
			        else if(text.startsWith("!togglePause")) {
			        	channel.sendMessage("Pausing Script...").queue();
			        	
			        	if(script.togglePause()) {
			        		channel.sendMessage("Script Paused").queue();
			        	}
			        	else {
			        		channel.sendMessage("Script Resumed").queue();
			        	}
			        	
			        }
			        else if(text.startsWith("!restartModule")) {
			        	channel.sendMessage("Restarting Current Module").queue();
			        	
			        	if(script.getCurrent().setupModule()) {
			        		channel.sendMessage("Module setUp Completed").queue();
				        	
			        	}
			        	else {
			        		channel.sendMessage("Module setUp FAILED").queue();
				        	
			        	}
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
		channel.sendMessage(msg).queue();
	}
	
	public void setScript(MainLooper script) {
		this.script = script;
	}


}
