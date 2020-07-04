package scripts;
import java.util.Random;

import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.wrappers.interactive.GameObject;

import antiban.RandomProvider;
import client.ClientThread;
import client.ThreadController;
import movement.Location;
import movement.LocationFactory;
import utilities.GearHandler.Gear;


public class CookerModule extends ScriptModule {
	private ClientThread script;
	private ThreadController controller;
	
	private boolean killThread;
	
	private int limit;
	private int actionsCompleted;
	
	private String cookTarget;
	private String cookOn;
	
	private Cook foodEnum;
	private LocationFactory.GameLocation locationEnum;
	
	
	public CookerModule(ThreadController controller, ClientThread script, LocationFactory.GameLocation location, Cook food, int limit) {
		this.script = script;
		this.controller = controller;
		
		this.limit = limit;
		
		foodEnum = food;
		locationEnum = location;
		
		setCookOn(this.locationEnum);
		setCookTarget(food);
		
		this.moduleName = "CookingModule: " + food; 
	}
	
	@Override
	public void run() {
		
		while(!killThread) {
		
			RandomProvider.sleep(2000, 3000);

			if(!script.getLocalPlayer().isAnimating()) {
				
				if(!script.getInventory().contains(f -> f != null && f.getName().equals(cookTarget))) {
					
					controller.getGraphicHandler().setInfo("Cooker: Inventory Done - Banking");
					
					this.actionsCompleted++;
					
					controller.getMovementHandler().moveToBank();
					
					RandomProvider.sleep(700, 1450);
					
					while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
					while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
					
					controller.debug("Mouse control: CookerModule");
					controller.debug("Keyboard control: CookerModule");
					
					script.getBank().depositAllItems();
					
					if(script.getBank().contains(cookTarget)) {
						RandomProvider.sleep(700, 1450);
						script.getBank().withdrawAll(f -> f != null && f.getName().equals(cookTarget));
						RandomProvider.sleep(700, 1450);
						script.getBank().close();
						RandomProvider.sleep(400, 1150);
						script.getMouse().move();
						
						controller.returnKeyboardAccess();
						controller.returnMouseAccess();
					}
					else {
						this.killThread = true;
						controller.returnKeyboardAccess();
						controller.returnMouseAccess();
					}	
				}
				
				if(!this.controller.getMovementHandler().isPlayerInLocation()) {
					controller.getGraphicHandler().setInfo("Cooker: Moving to Area - " + locationEnum);
					controller.getMovementHandler().moveToLocation();
				}
				
				if(!script.getLocalPlayer().isAnimating()) {
					controller.getGraphicHandler().setInfo("Cooker: Cooking - " + foodEnum);
					
					while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
					while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
					
					controller.debug("Mouse control: CookerModule");
					controller.debug("Keyboard control: CookerModule");
					
					GameObject heatSource = script.getGameObjects().closest(f -> f != null && f.getName().equals(cookOn));
					
					if(heatSource != null) {
						int randomizer = RandomProvider.randomInt(2);
						
						if(RandomProvider.fiftyfifty()) {
							script.getCamera().rotateToEntity(heatSource);
							RandomProvider.sleep(750, 1000);
						}
					
					
						if(randomizer == 0) {
							heatSource.interact();
						}
						else {
							heatSource.interact("Cook");
						}
						
						if(RandomProvider.fiftyfifty()) {
							script.getMouse().move();
						}
						
						RandomProvider.sleep(1300, 2050);
						script.getKeyboard().type(1,false);
						
						RandomProvider.sleep(500, 750);
						
						if(RandomProvider.fiftyfifty()) {
							script.getMouse().moveMouseOutsideScreen();
						}
						else {
							script.getMouse().move();
						}
					}
					
					controller.returnKeyboardAccess();
					controller.returnMouseAccess();
				}
				
			}
			
		}
	}


	
	public void setCookOn(LocationFactory.GameLocation location) {
		if(location == LocationFactory.GameLocation.COOKING_AL_KHARID) {
			this.cookOn = "Range";
		}
		else if(location == LocationFactory.GameLocation.COOKING_CATHERBY) {
			this.cookOn = "Range";
		}
	}
	
	public void setCookTarget(Cook food) {
		if(food == Cook.SHRIMP) {
			this.cookTarget = "Raw shrimps";
		}
		else if(food == Cook.HERRING) {
			this.cookTarget = "Raw herring";	
		}
		else if(food == Cook.TROUT) {
			this.cookTarget = "Raw trout";
		}
		else if(food == Cook.SALMON) {
			this.cookTarget = "Raw salmon";
		}
		else if(food == Cook.TUNA) {
			this.cookTarget = "Raw tuna";
		}
		else if(food == Cook.LOBSTER) {
			this.cookTarget = "Raw lobster";
		}
		else if(food == Cook.SWORDFISH) {
			this.cookTarget = "Raw swordfish";
		}
		else if(food == Cook.SHARK) {
			this.cookTarget = "Raw shark";
		}
	}
	
	public enum Cook {
		SHRIMP, HERRING, TROUT, SALMON, TUNA, LOBSTER, SWORDFISH, SHARK
	}

	@Override
	public boolean setupModule() {
		controller.getMovementHandler().newLocation(this.locationEnum);
		
		controller.getMovementHandler().teleportToLocation();
		
		controller.getGearHandler().handleGearSwap(Gear.UTILITY);
		
		controller.getGraphicHandler().setInfo("Cooker: Setting Up Module");
		
		if(!script.getInventory().contains(f -> f != null && f.getName().equals(this.cookTarget))) {
			
			controller.getMovementHandler().locateBank();
			
			while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
			while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
			
			controller.debug("Mouse control: CookerModule");
			controller.debug("Keyboard control: CookerModule");
			
			if(!script.getInventory().isEmpty()) {
				script.getBank().depositAllItems();
				RandomProvider.sleep(1000, 1750);
			}
			
			if(script.getBank().contains(cookTarget)) {
				script.getBank().withdrawAll(f -> f != null && f.getName().equals(cookTarget));
				RandomProvider.sleep(700, 1450);
				script.getBank().close();
				RandomProvider.sleep(400, 1150);
				script.getMouse().move();
				
				controller.returnKeyboardAccess();
				controller.returnMouseAccess();
				
				return true;
			}
			else {
				controller.returnKeyboardAccess();
				controller.returnMouseAccess();
				return false;
			}

		}
		else {
			return true;
		}
	}

	@Override
	public Skill getSkillToHover() {
		return Skill.COOKING;
	}

	@Override
	public void killThread() {
		this.killThread = true;
	}

	@Override
	public boolean isAlive() {
		return !this.killThread;
	}


	@Override
	public boolean isReady() {
		if((this.limit <= this.actionsCompleted)) {
			return true;
		}
		else {
			return false;
		}
	}

}
