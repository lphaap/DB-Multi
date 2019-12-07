import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.wrappers.items.Item;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramChat extends TelegramLongPollingBot{

	private MainLooper script;
	
	private Long chatID;
	
	public TelegramChat(MainLooper script) {
		this.script = script;
		script.setMessenger(this);
		script.setMessenger(this);
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
		        	this.chatID = update.getMessage().getChatId();
		        	message.setText("Bot Connected");
		        	this.script.setMessenger(this);
		        	 try {
				            execute(message); // Call method to send the message
				        } catch (TelegramApiException e) {
				            e.printStackTrace();
				        }
		        }
		        else if(text.startsWith("#send")) {
		        	message.setText("Send msg: " + text.substring(6));
		        	try {
						execute(message);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
		        	script.getKeyboard().type(text.substring(6));
		        }
		        else if(text.startsWith("#kill")) {
		        	message.setText("Killing client");
		        	try {
						execute(message);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
		        	script.stop();
		        	System.exit(0);
		        }
		        else if(text.startsWith("#next")) {
		        	message.setText("Swapping Module");
		        	try {
						execute(message);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
		        	script.nextModule();
		        }
		        else if(text.startsWith("#stop")) {
		        	message.setText("Stopping Script");
		        	try {
						execute(message);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
		        	script.stop();
		        }
		        else if(text.startsWith("#hop")) {
		        	message.setText("Hopping Worlds");
		        	try {
						execute(message);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
		        	script.hop();
		        }
		        else if(text.startsWith("#current")) {
		        	message.setText(script.getCurrentAction());
		        	try {
						execute(message);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
		        }
		        else if(text.startsWith("#timeScript")) {
		        	message.setText(script.getTimer());
		        	try {
						execute(message);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
		        }
		        else if(text.startsWith("#timePause")) {
		        	message.setText(script.getPauseTimer());
		        	try {
						execute(message);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
		        }
		        else if(text.startsWith("#autoreact")) {
		        	message.setText("Set AutoReact to: " + script.swapAutoReact());
		        	try {
						execute(message);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
		        }
		        else if(text.startsWith("#react")) {
		        	message.setText("Setting react to: " + text.substring(7));
		        	try {
						execute(message);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
		        	script.setReact(Integer.parseInt(text.substring(7)));
		        }
		        else if(text.startsWith("#players")) {
		        	message.setText("Players in area: " + script.getPlayerCount());
		        	try {
						execute(message);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
		        	script.setReact(Integer.parseInt(text.substring(7)));
		        }
		        else if(text.startsWith("#logOut")) {
		        	message.setText("Logging Out...");
		        	try {
						execute(message);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
		        	script.logOut();
		        }
		        else if(text.startsWith("#logIn")) {
		        	message.setText("Logging In...");
		        	try {
						execute(message);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
		        	script.logIn();
		        }
		        else if(text.startsWith("#togglePause")) {
		        	message.setText("Pausing Script...");
		        	try {
						execute(message);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
		        	if(script.togglePause()) {
		        		message.setText("Script Paused");
		        	}
		        	else {
		        		message.setText("Script Resumed");
		        	}
		        	try {
						execute(message);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
		        }
		        else if(text.startsWith("#restartModule")) {
		        	message.setText("Restarting Current Module");
		        	try {
						execute(message);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
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
		        	}
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
		        	message.setText("Available Commands:"+"\r\n"+"#connect"+"\r\n"+"#kill"+"\r\n"+"#stop"+"\r\n"+"#next"+"\r\n"+"#send"+"\r\n"+"#timeScript"+"\r\n"+"#timePause"+"\r\n"+"#current"+"\r\n"+"#react <int>"+ "\r\n"+"#autoreact" +"\r\n"+"#players"+
		        					"\r\n"+"#logOut" + "\r\n"+ "#logIn"+ "\r\n"+"#togglePause"+ "\r\n"+"#restartModule"+ "\r\n"+"#hp"+ "\r\n"+"#prayer"+ "\r\n"+"#gear" + "\r\n"+"#inventory"+"\r\n"+"#location");
		        	this.script.setMessenger(this);
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
