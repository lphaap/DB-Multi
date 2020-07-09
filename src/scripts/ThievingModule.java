package scripts;

import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.wrappers.interactive.NPC;

import antiban.RandomProvider;
import client.ClientThread;
import client.ThreadController;
import movement.LocationFactory;
import utilities.GearHandler.Gear;

public class ThievingModule extends ScriptModule {
	//CLASS OBJECTS HERE:
	
	private ThreadController controller;
	private ClientThread client;

	private boolean killThread;
	
	private int limit;
	private int actionsCompleted;

	private String targetName;
	
	private LocationFactory.GameLocation locationEnum;
	private ThievingTarget targetEnum;
	
	//MODULE CONSTRUCTOR:
	public ThievingModule(ThreadController controller, ClientThread client, LocationFactory.GameLocation locationEnum, 
						 ThievingTarget target, int limit) { //<-- ADD REQUIRED OBJECTS HERE
		this.controller = controller; 
		this.client = client;
		this.limit = limit;
		this.locationEnum = locationEnum;
		this.targetEnum = target;
		this.setTargetVariables(target);
	}
	
	@Override
	public void run() {
		
		while(!killThread) {
			RandomProvider.sleep(1000,1500);

			if(!client.getLocalPlayer().isAnimating()){
				controller.getGraphicHandler().setInfo("ThievingModule: Thieving");
				NPC npc = client.getNpcs().closest( f -> f != null && f.getName().equals(this.targetName));
				
				npc.interact("Pickpocket");
				client.getMouse().move();
				

			}

		}
		
	}
	
	@Override
	public boolean setupModule() {//Return true if setup completed, otherwise false IE. bank dosent have required items
		
		this.controller.getMovementHandler().newLocation(this.locationEnum);
		
		this.controller.getMovementHandler().teleportToLocation();
		
		this.controller.getGearHandler().handleGearSwap(Gear.UTILITY); //TODO: SWAP TO CORRECT GEAR
		
		controller.getGraphicHandler().setInfo("ThievingModule: Setting up module"); //TODO: CUSTOMIZE MESSAGE
		
		return true;
	}
	
	@Override
	public Skill getSkillToHover() {
		return Skill.THIEVING; //TODO: SWAP TO CORRECT SKILL
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

	private void setTargetVariables(ThievingTarget target){
		if(target == ThievingTarget.MAN){

		}
		else if(target == ThievingTarget.FARMER){

		}
		else if(target == ThievingTarget.MASTER_FARMER){

		}
	}

	public enum ThievingTarget{
		MAN, FARMER, MASTER_FARMER;
	}


}
