/**
 * 
 */
package utilities;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.world.World;

import antiban.RandomProvider;
import client.ClientThread;
import client.KillableHandler;
import client.ThreadController;
import movement.LocationFactory;


public class WorldHandler implements KillableHandler {
	
	private ThreadController controller;
	private ClientThread client;
	
	private int playerLimit;
	private int hopCounter;
	
	private boolean killHandler;
	private boolean active;
	private boolean banking;
	
	//MODULE CONSTRUCTOR:
	public WorldHandler(ThreadController controller, ClientThread client) {
		this.controller = controller; 
		this.client = client;
	}
	
	public void start() {
		new Thread(() ->  {//THREAD1
			
			mainLoop: while(!killHandler) {//MAIN LOOP
				RandomProvider.sleep(4000,5000);
				
				if(active && !banking && hopCounter <= 0) {//IF1
					if(client.getPlayerCount() > playerLimit) {//IF2
						
						while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
						while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
						
						controller.debug("Mouse Control: WorldHandler");
						controller.debug("Keyboard Control: WorldHandler");
						
						hopLoop: while(client.getPlayerCount() > playerLimit) {
							RandomProvider.sleep(1500, 2000);
							loopHopWorlds(); //TODO:
							hopCounter++;
							if(hopCounter >= 5) {
								break hopLoop;
							}
							if(killHandler) {
								controller.returnKeyboardAccess();
								controller.returnMouseAccess();
								break mainLoop;
							}
							
						}
						
						client.getWorldHopper().closeWorldHopper();
						RandomProvider.sleep(1000, 1100);
						
						client.getTabs().open(Tab.INVENTORY);
						client.getMouse().move();
						
						new Thread(() -> {
							int timer = (hopCounter*60*1000);
							RandomProvider.sleep(hopCounter-1000, hopCounter+1000);
							this.hopCounter = 0;
						}).start();//THREAD2
						
						controller.returnKeyboardAccess();
						controller.returnMouseAccess();
						
					}//IF2
					
					
				}//IF1
				
			}//MAIN LOOP
			
		}).start();//THREAD1
	}
	
	private void loopHopWorlds() {
		controller.getGraphicHandler().setInfo("Hopping Worlds..");
		
		World w;
		if(client.getClient().isMembers()) {
			w = client.getWorlds().getRandomWorld(f -> f != null && f.isMembers() && !f.isDeadmanMode() && !f.isPVP() && f.getMinimumLevel() == 0);
		}
		else {
			w = client.getWorlds().getRandomWorld(f -> f != null && !f.isMembers() && !f.isDeadmanMode() && !f.isPVP() && f.getMinimumLevel() == 0);
		}
		
		if(client.getPlayers().localPlayer().isInCombat()) {
			while(client.getPlayers().localPlayer().isInCombat()) {
				RandomProvider.sleep(500, 1000);
			}
			RandomProvider.sleep(12000, 14000);
		}
		
		if(!client.getWorldHopper().isWorldHopperOpen()) {
			client.getWorldHopper().openWorldHopper();
		}
		
		client.getWorldHopper().hopWorld(w);
		RandomProvider.sleep(3000, 4000);
		
	}
	
	public void hopWorlds() {
		
		while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
		while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
		
		controller.debug("Mouse Control: WorldHandler");
		controller.debug("Keyboard Control: WorldHandler");
		controller.getGraphicHandler().setInfo("Hopping Worlds..");
			
			World w;
			if(client.getClient().isMembers()) {
				w = client.getWorlds().getRandomWorld(f -> f != null && f.isMembers() && !f.isDeadmanMode() && !f.isPVP() && f.getMinimumLevel() == 0);
			}
			else {
				w = client.getWorlds().getRandomWorld(f -> f != null && !f.isMembers() && !f.isDeadmanMode() && !f.isPVP() && f.getMinimumLevel() == 0);
			}
			
			if(client.getPlayers().localPlayer().isInCombat()) {
				while(client.getPlayers().localPlayer().isInCombat()) {
					RandomProvider.sleep(500, 1000);
				}
				RandomProvider.sleep(12000, 14000);
			}
			
			client.getWorldHopper().openWorldHopper();
			client.getWorldHopper().hopWorld(w);
			RandomProvider.sleep(3000, 4000);
			
			client.getWorldHopper().closeWorldHopper();
			RandomProvider.sleep(1000, 1100);
			
			client.getTabs().open(Tab.INVENTORY);
			client.getMouse().move();
			
		
		
		controller.returnKeyboardAccess();
		controller.returnMouseAccess();
		
	}
	
	@Override
	public void killHandler() {
		this.killHandler = true;
	}
	//Swap worlds after player count > limit
	public void setPlayerLimit(int limit) {
		this.playerLimit = limit;
	}
	public void resetHandler() {
		this.active = false;
		this.banking = false;
	}
	public boolean isActive() {
		return active;
	}
	public boolean isBanking() {
		return banking;
	}
	public void setToBanking() {
		this.banking = true;
	}
	public void setToActive() {
		this.active = true;
	}
	public void setToUnBanking() {
		this.banking = false;
	}
	public void setToUnActive() {
		this.active = false;
	}
}
