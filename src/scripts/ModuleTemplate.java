package scripts;

import org.dreambot.api.methods.skills.Skill;

import antiban.RandomProvider;
import client.ClientThread;
import client.ThreadController;
import movement.LocationFactory;
import utilities.GearHandler.Gear;

public class ModuleTemplate extends ScriptModule {
	//CLASS OBJECTS HERE:
	
	private ThreadController controller;
	private ClientThread client;

	private boolean killThread;
	
	private int limit;
	private int actionsCompleted;
	
	private LocationFactory.GameLocation locationEnum;
	
	//MODULE CONSTRUCTOR:
	public ModuleTemplate(ThreadController controller, ClientThread client, LocationFactory.GameLocation locationEnum,  int limit) { //<-- ADD REQUIRED OBJECTS HERE
		this.controller = controller; 
		this.client = client;
		this.limit = limit;
		this.locationEnum = locationEnum;
	}
	
	@Override
	public void run() {
		
		while(!killThread) {
			//TODO: MODULE LOOP HERE
			
			/*
				TEMPLATE:
				
				if(!client.getLocalPlayer().isAnimating()) {
					if(WHEN TO BANK) { 
						//BANKING CODE:
					}
					else if(!controller.getMovementHandler().isPlayerInLocation()) {
						this.controller.getMovementHandler().moveToLocation();
					}
					else {
						//WHAT TO DO:
					}
				}
		
			*/
			
			
			/*
			 ALWAYS REQUEST MOUSE AND KEYBOARD ACCESS WHEN USED:
			 
			 	while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
				while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
				
			 AND RETURN IT AFTER:
			 
				controller.returnKeyboardAccess();
				controller.returnMouseAccess();
			*/
		}
		
	}
	
	@Override
	public boolean setupModule() {//Return true if setup completed, otherwise false IE. bank dosent have required items
		
		this.controller.getMovementHandler().newLocation(this.locationEnum);
		
		this.controller.getMovementHandler().teleportToLocation();
		
		this.controller.getGearHandler().handleGearSwap(Gear.UTILITY); //TODO: SWAP TO CORRECT GEAR
		
		controller.getGraphicHandler().setInfo("Module-X: Setting up module"); //TODO: CUSTOMIZE MESSAGE
		
		//TODO: MODULE SETUP HERE
		
		return false;
	}
	
	@Override
	public Skill getSkillToHover() {
		return Skill.HITPOINTS; //TODO: SWAP TO CORRECT SKILL
	}
	
	@Override
	public void killThread() {
		this.killThread = true;
	}

	@Override
	public boolean isAlive() {
		return !killThread;
	}

	@Override
	public boolean isReady() {
		if(this.limit <= this.actionsCompleted) {
			return true;
		}
		else { 
			return  false; 
		}
	
	}




}
