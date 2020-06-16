package scripts;
import java.util.Random;

import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;

import antiban.RandomProvider;
import client.ClientThread;
import client.ThreadController;
import movement.Location;
import movement.LocationFactory;
import utilities.GearHandler.Gear;


public class FishingModule extends ScriptModule {
	private ClientThread script;
	private ThreadController controller;
	
	private int actionsCompleted;
	private int limit;
	private int error;
	
	private String thrashFish;
	private String interactWith;
	private String fishingSpot;
	private String bait;
	private String fishingGear;
	private String payFish;
	private String defineSpot;
	
	private boolean thrashFishTest;
	private boolean bankTheFish;
	private boolean useBait;
	private boolean killThread;
	
	private LocationFactory.GameLocation locationEnum;
	private Fish fishEnum;
	
	
	public FishingModule(ThreadController controller, ClientThread script, FishingModule.Fish fish, int limit, boolean bank) {
		this.limit = limit;
		this.script = script;
		this.controller = controller;
		this.bankTheFish = bank;
		this.fishEnum = fish;
		this.error = 0;
		this.moduleName = "FishingModule: " + fish;
		setModuleVariables(fish);
	}
	
	@Override
	public void run() {
		
		while(!this.killThread) {
		
			RandomProvider.sleep(2000, 3000);
			
			if(this.error > 5) {
				this.killThread();
			}
			
			if(!script.getInventory().contains(f -> f != null && f.getName().equals(fishingGear))
				||  (!script.getInventory().contains(f -> f != null && f.getName().equals(bait)) && useBait)) {
				this.killThread();
			}
			
			if(!script.getLocalPlayer().isAnimating()) {
				if(script.getInventory().isFull()) {
					
					if(this.bankTheFish) {
						if(this.thrashFishTest) {
							if(script.getInventory().count(f -> f != null && f.getName().equals(payFish)) >= 23) {
								bank();
							}
							else {
								while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
								while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
								
								controller.getGraphicHandler().setInfo("Fisher: Dropping Thrash Fish");
								script.getInventory().dropAllExcept(f -> f != null && (f.getName().equals(payFish) || f.getName().equals(fishingGear) 
										|| f.getName().equals(bait)));
								
								controller.returnKeyboardAccess();
								controller.returnMouseAccess();
							}
						}
						else {
							bank();
						}
					}
					else {
						while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
						while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
						
						controller.getGraphicHandler().setInfo("Fisher: Dropping Inventory");
						//TODO: TARKISTA TÄMÄ MUUTOS
						script.getInventory().dropAllExcept(f -> f != null && (f.getName().equals(this.bait) || f.getName().equals(this.fishingGear)));
						this.actionsCompleted++;
						
						controller.returnKeyboardAccess();
						controller.returnMouseAccess();
					}
				}
				
				else if(!controller.getMovementHandler().isPlayerInLocation()) {
					controller.getGraphicHandler().setInfo("Fisher: Walking to Location - " + this.locationEnum);
					controller.getMovementHandler().moveToLocation();
				}
				
				else if(!script.getLocalPlayer().isAnimating()) {
					controller.getGraphicHandler().setInfo("Fisher: Fishing - " + this.fishEnum);
					
					while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
					while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
					
					NPC fishSpot = script.getNpcs().closest(f -> f != null && f.getName().equals(fishingSpot) && f.hasAction(this.defineSpot));
					if(fishSpot != null) {
						fishSpot.interact(interactWith);
						script.getMouse().move();
						this.error = 0;
					}
					else {
						script.getWalking().walk(controller.getMovementHandler().getRandomLocationTile());
						RandomProvider.sleep(2000, 3000);
						error++;
					}
					
					controller.returnKeyboardAccess();
					controller.returnMouseAccess();
				}
				
			}
		
		}
		
	}
	
	public void bank() {
		controller.getGraphicHandler().setInfo("Fisher: Inventory Full - Banking");
		this.actionsCompleted++;
		controller.getMovementHandler().moveToBank();
		RandomProvider.sleep(1000, 2000);
		
		while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
		while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
		
		script.getBank().depositAllExcept(f -> f != null && (f.getName().equals(bait) || f.getName().equals(fishingGear)));
		
		RandomProvider.sleep(1000, 2000);
		script.getBank().close();
		RandomProvider.sleep(1000, 2000);
		script.getMouse().move();
		
		controller.returnKeyboardAccess();
		controller.returnMouseAccess();
	}

	public enum Fish {
		HERRING, TROUT, TROUT_SALMON, SALMON, TUNA, LOBSTER, SWORDFISH, TUNA_SWORDFISH, SHARK
	}
	
	/**
	 * Sets: location, trashFish, fishingSpot
	 */
	public void setModuleVariables(Fish fish) {
		if(fish == Fish.HERRING) {
			this.locationEnum = LocationFactory.GameLocation.FISHING_AL_KHARID;
			this.useBait = true;
			this.bait = "Fishing bait";
			this.thrashFishTest = true;
			this.interactWith = "Bait";
			this.defineSpot = this.interactWith;
			this.fishingSpot = "Fishing spot";
			this.fishingGear = "Fishing rod";
			this.payFish = "Raw herring";
			this.interactWith = "Bait";
		}
		else if(fish == Fish.TROUT) {
			this.locationEnum = LocationFactory.GameLocation.FISHING_BARBARIAN_VILLAGE;
			this.useBait = true;
			this.bait = "Feather";
			this.thrashFishTest = true;
			this.interactWith = "Lure";
			this.defineSpot = this.interactWith;
			this.fishingSpot = "Rod Fishing spot";
			this.fishingGear = "Fly fishing rod";
			this.payFish = "Raw trout";
			this.interactWith = "Lure";
		}
		else if(fish == Fish.SALMON) {
			this.locationEnum = LocationFactory.GameLocation.FISHING_BARBARIAN_VILLAGE;
			this.useBait = true;
			this.bait = "Feather";
			this.thrashFishTest = true;
			this.interactWith = "Lure";
			this.defineSpot = this.interactWith;
			this.fishingSpot = "Rod Fishing spot";
			this.fishingGear = "Fly fishing rod";
			this.payFish = "Raw salmon";
			this.interactWith = "Lure";
		}
		else if(fish == Fish.TROUT_SALMON) {
			this.locationEnum = LocationFactory.GameLocation.FISHING_BARBARIAN_VILLAGE;
			this.useBait = true;
			this.bait = "Feather";
			this.thrashFishTest = false;
			this.interactWith = "Lure";
			this.defineSpot = this.interactWith;
			this.fishingSpot = "Rod Fishing spot";
			this.fishingGear = "Fly fishing rod";
			this.interactWith = "Lure";
		}
		else if(fish == Fish.LOBSTER) {
			this.locationEnum = LocationFactory.GameLocation.FISHING_CATHERBY;
			this.useBait = false;
			this.bait = "";
			this.thrashFishTest = false;
			this.fishingSpot = "Fishing spot";
			this.fishingGear = "Lobster pot";
			this.interactWith = "Cage";
			this.defineSpot = this.interactWith;
		}
		else if(fish == Fish.TUNA) {
			this.locationEnum = LocationFactory.GameLocation.FISHING_CATHERBY;
			this.useBait = false;
			this.bait = "";
			this.thrashFishTest = true;
			this.payFish = "Raw tuna";
			this.fishingSpot = "Fishing spot";
			this.fishingGear = "Harpoon";
			this.interactWith = "Harpoon";
			this.defineSpot = "Cage";
		}
		else if(fish == Fish.SWORDFISH) {
			this.locationEnum = LocationFactory.GameLocation.FISHING_CATHERBY;
			this.useBait = false;
			this.bait = "";
			this.thrashFishTest = true;
			this.payFish = "Raw swordfish";
			this.fishingSpot = "Fishing spot";
			this.fishingGear = "Harpoon";
			this.interactWith = "Harpoon";
			this.defineSpot = "Cage";
		}
		else if(fish == Fish.TUNA_SWORDFISH) {
			this.locationEnum = LocationFactory.GameLocation.FISHING_CATHERBY;
			this.useBait = false;
			this.bait = "";
			this.thrashFishTest = false;
			this.payFish = "";
			this.interactWith = "Harpoon";
			this.fishingSpot = "Fishing spot";
			this.fishingGear = "Harpoon";
			this.defineSpot = "Cage";
		}
		else if(fish == Fish.SHARK) {
			this.locationEnum = LocationFactory.GameLocation.FISHING_CATHERBY;
			this.useBait = false;
			this.bait = "";
			this.thrashFishTest = true;
			this.payFish = "Raw shark";
			this.interactWith = "Harpoon";
			this.fishingSpot = "Fishing spot";
			this.fishingGear = "Harpoon";
			this.defineSpot = "Big Net";
		}
	}
	


	@Override
	public boolean setupModule() {
		controller.getMovementHandler().newLocation(this.locationEnum);
		
		controller.getMovementHandler().teleportToLocation();
		
		controller.getGearHandler().handleGearSwap(Gear.UTILITY);
		
		controller.getGraphicHandler().setInfo("Fisher: Setting Up Module");

		if(!script.getInventory().contains(f -> f != null && f.getName().equals(fishingGear))
				||  (!script.getInventory().contains(f -> f != null && f.getName().equals(bait) && useBait))){
			
			while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
			while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
			
			if(!script.getWalking().isRunEnabled() && script.getWalking().getRunEnergy() > 0) {
				script.getWalking().toggleRun();
			}

			while(!script.getBank().isOpen()) {
				script.getBank().open(script.getBank().getClosestBankLocation());
				RandomProvider.sleep(2000, 3000);
			}
			if(!script.getInventory().isEmpty()) {
				script.getBank().depositAllItems();	
			}
			
			RandomProvider.sleep(500, 1250);
			
			if(script.getBank().contains(f -> f != null && f.getName().equals(this.fishingGear) )){
				script.getBank().withdraw(f -> f != null && f.getName().equals(this.fishingGear));
				RandomProvider.sleep(1000, 1750);
				if(script.getBank().contains(f -> f != null && f.getName().equals(bait))){
					script.getBank().withdrawAll(f -> f != null && f.getName().equals(bait));
				}
				
				else if(useBait){
					controller.returnKeyboardAccess();
					controller.returnMouseAccess();
					return false;
				}
				
				RandomProvider.sleep(1000, 1750);
				script.getBank().close();
				
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
		return Skill.FISHING;
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
		if(limit <= actionsCompleted) {
			return true;
		}
		else {
			return false;
		}
	}

}
