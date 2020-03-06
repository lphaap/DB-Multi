package chat;

import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.wrappers.items.Item;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import client.ClientThread;
import client.ThreadController;

public class TelegramHandler extends TelegramLongPollingBot{

	private ClientThread script;
	private ThreadController controller;
	
	private Long chatID;
	
	public TelegramHandler(ThreadController controller, ClientThread script) {
		this.script = script;
		this.controller = controller;
	}
	
	@Override
	public String getBotUsername() {
		return "DreamMultiBot";
	}

	@Override
	public void onUpdateReceived(Update update) {
		 if (update.hasMessage() && update.getMessage().hasText() && this.script != null) {
		        SendMessage message = new SendMessage()
		                .setChatId(update.getMessage().getChatId());
		        String text = update.getMessage().getText();
		        
		        if(text.startsWith("#connect")) {
		        	message.setText("Not Implemented Yet");
		        	try {
						execute(message);
					} catch (TelegramApiException e) {}/*
		        	this.chatID = update.getMessage().getChatId();
		        	message.setText("Bot Connected");
		        	this.script.setMessenger(this);
		        	 try {
				            execute(message); // Call method to send the message
				        } catch (TelegramApiException e) {
				            e.printStackTrace();
				        }
				        */
		        }
		        else if(text.startsWith("#send")) {
		        	message.setText("Send msg: " + text.substring(6));
		        	try {
						execute(message);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
		        	controller.getInGameMsgHandler().sendMsgInGame(text.substring(6));
		        }
		        else if(text.startsWith("#kill")) {
		        	message.setText("Killing client");
		        	try {
						execute(message);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
		        	controller.killClient();
		        	
		        }
		        else if(text.startsWith("#next")) {
		        	message.setText("Swapping Module");
		        	try {
						execute(message);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
		        	controller.nextModule();
		        }
		        else if(text.startsWith("#stop")) {
		        	message.setText("Stopping Script");
		        	try {
						execute(message);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
		        	controller.killBot();
		        }
		        else if(text.startsWith("#hop")) {
		        	message.setText("Hopping Worlds");
		        	try {
						execute(message);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
		        	controller.hopWorlds();
		        }
		        else if(text.startsWith("#current")) {
		        	message.setText(controller.getCurrentActionPrint());
		        	try {
						execute(message);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
		        }
		        else if(text.startsWith("#timeScript")) {
		      
		        	message.setText("Time left in script: " + controller.timeLeftInScript());
		        	try {
						execute(message);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
		        }
		        else if(text.startsWith("#timePause")) {
				
		        	message.setText("Time left until pause: " + controller.timeLeftUntillPause());
		        	try {
						execute(message);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
		        }
		        else if(text.startsWith("#autoreact")) {
		        	
		        	message.setText("Set AutoReact to: " + controller.getInGameMsgHandler().toggleAutoReact());
		        	try {
						execute(message);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
		        }
		        else if(text.startsWith("#players")) {
		        	message.setText("Players in area: " + script.getPlayerCount());
		        	try {
						execute(message);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
		        }
		       
		        else if(text.startsWith("#restartModule")) {
		        	//TODO:
		        	message.setText("Nor Implemented Yet");
		        	try {
						execute(message);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
					/*
		        	if(script.getCurrent().setupModule()) {
		        		message.setText("Module setUp Completed");
			        	try {
							execute(message);
						} catch (TelegramApiException e) {
							e.printStackTrace();
						}
		        	}
		        	else {
		        		message.setText("Module setUp FAILED");
			        	try {
							execute(message);
						} catch (TelegramApiException e) {
							e.printStackTrace();
						}
		        	}*/
		        }
		        else if(text.startsWith("#location")) {
		        	message.setText("Cuurent Player Location - X: " + script.getLocalPlayer().getTile().getX() +" Y: " + script.getLocalPlayer().getTile().getY());
		        	try {
						execute(message);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
		        }
		        else if(text.startsWith("#hp")) {
		        	message.setText("Current Player Hitpoints: " + script.getSkills().getBoostedLevels(Skill.HITPOINTS));
		        	try {
						execute(message);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
		        }
		        else if(text.startsWith("#prayer")) {
		        	message.setText("Current Player Prayerpoints: " + script.getSkills().getBoostedLevels(Skill.PRAYER));
		        	try {
						execute(message);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
		        }
		        else if(text.startsWith("#gear")) {
		        	String msg = "Current Gear on Player:" + "\r\n";
		   
		        	for(Item i : script.getEquipment().all()) {
		        		if(i != null) {
		        			msg = msg + i.getName() + "\r\n";
		        		}
		        	}
		        	message.setText(msg);
		        	try {
						execute(message);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
		        }
		        else if(text.startsWith("#inventory")) {
		        	String msg = "Current Inventory on Player:" + "\r\n";
		        	
		        	for(Item i : script.getInventory().all()) {
		        		if(i != null) {
		        			msg = msg + i.getName() + "\r\n";
		        		}
		        	}
		        	message.setText(msg);
		        	try {
						execute(message);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
		        }
		        else if(text.startsWith("#")){
		        	message.setText("Available Commands:"+"\r\n"+"#connect"+"\r\n"+"#kill"+"\r\n"+"#stop"+"\r\n"+"#next"+"\r\n"+"#send"+"\r\n"+"#timeScript"+"\r\n"+"#timePause"+"\r\n"+"#current"+ "\r\n"+"#autoreact" +"\r\n"+"#players"+
		        					 "\r\n"+"#restartModule"+ "\r\n"+"#hp"+ "\r\n"+"#prayer"+ "\r\n"+"#gear" + "\r\n"+"#inventory"+"\r\n"+"#location");
//		        	this.script.setMessenger(this);
		        	 try {
				            execute(message); // Call method to send the message
				        } catch (TelegramApiException e) {
				            e.printStackTrace();
				        }
		        	
		        }
		        
		    }
		
	}

	@Override
	public String getBotToken() {
		return "908102297:AAFHsAmBgYyTxBuxxYJzs29AAVokQZErrZM";
		
	}
	
	public void sendMessage(String msg) {
		 SendMessage message = new SendMessage()
	                .setChatId(chatID)
	                .setText(msg);
	        try {
	            execute(message); // Call method to send the message
	        } catch (TelegramApiException e) {
	            e.printStackTrace();
	        }
	}


}

