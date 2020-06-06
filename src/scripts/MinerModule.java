package scripts;

import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.wrappers.interactive.Entity;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.Player;

import antiban.RandomProvider;
import client.ClientThread;
import client.ThreadController;
import movement.Location;
import movement.LocationFactory;
import utilities.GearHandler.Gear;


public class MinerModule extends ScriptModule{
	private ClientThread script;
	private ThreadController controller;
	
	private LocationFactory.GameLocation locationEnum;
	
	private int oreID;
	private int limit;
	private int completedActions;
	
	private Player thief;
	private GameObject takenOre;
	private Ore ore;
	 
	private boolean error;
	private boolean killThread;

	
	public MinerModule(ClientThread script, ThreadController controller, LocationFactory.GameLocation gamelocation, MinerModule.Ore ore, int limit) {
		this.script = script;
		this.controller = controller;
		
		this.locationEnum = gamelocation;
		
		this.oreID = getOreID(this.ore);
		this.limit = limit;
		 
		this.ore = ore;
		this.locationEnum = gamelocation;

		this.thief = null;
		this.takenOre = null;
		
		this.error = false;
		this.moduleName = "MiningModule: " + ore;
	}
	
	@Override
	public void run(){
		
		while(!killThread) {
		
		RandomProvider.sleep(2500, 3500);
		
		if(!script.getLocalPlayer().isAnimating()) {
			if(script.getInventory().isFull()) {
				controller.getGraphicHandler().setInfo("Miner: Inventory Full - Banking");
				
				controller.getMovementHandler().moveToBank();
				
				while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
				while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
				
				sleep(RandomProvider.randomInt(750)+ 1000);
				script.getBank().depositAllExcept(f -> f != null && f.getName().contains("pickaxe"));
				sleep(RandomProvider.randomInt(750)+ 1000);
				script.getBank().close();
				sleep(RandomProvider.randomInt(750)+ 500);
				script.getMouse().move();
				completedActions++;
				
				controller.returnKeyboardAccess();
				controller.returnMouseAccess();
			}
			else if(!controller.getMovementHandler().isPlayerInLocation()) {
				
				controller.getGraphicHandler().setInfo("Miner: Walking to Mining Area - " + locationEnum);
				
				controller.getMovementHandler().moveToLocation();
					
			}
			else if(!script.getLocalPlayer().isAnimating()) {
				
				if(!script.getInventory().contains(f -> f != null && f.getName().toLowerCase().contains("pickaxe"))) {
					controller.getTelegramHandler().sendMessage("Miner - Module ERROR");
					controller.getTelegramHandler().sendMessage("Trying to Restart Module...");
					if(!setupModule() || error) {
						controller.getTelegramHandler().sendMessage("Miner - Module ERROR");
						controller.getTelegramHandler().sendMessage("Changing Module.");
						this.killThread = true;
					}
					controller.getTelegramHandler().sendMessage("Restart Completed");
					this.error = true;
				}
				
				controller.getGraphicHandler().setInfo("Miner: Mining - " + ore);
				boolean playerTest = true;
				GameObject ore = null;

				
				thief = script.getPlayers().closest(f -> f != null);
				
				while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
				while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
				
				if(thief != null) {
					ore = script.getGameObjects().closest(gameObject -> gameObject != null && gameObject.getName().equals("Rocks") 
															&& gameObject.getModelColors() != null && gameObject.getModelColors()[0] == this.oreID
															&& !gameObject.getSurroundingArea(1).contains(thief));
				}
				else {
					ore = script.getGameObjects().closest(gameObject -> gameObject != null && gameObject.getName().equals("Rocks") 
							&& gameObject.getModelColors() != null && gameObject.getModelColors()[0] == this.oreID);
				}
				
				int randomizer = RandomProvider.randomInt(2);
				if(randomizer == 0) {
					ore.interact("Mine");
					script.getMouse().move();
				}
				else {
					ore.interact();
					script.getMouse().move();
				}
				
				controller.returnKeyboardAccess();
				controller.returnMouseAccess();
			}

			
			}
		}
		
	}

	
	public enum Ore {
		COPPER_ORE, TIN_ORE, IRON_ORE, GOLD_ORE, COAL, ADAMANTITE_ORE, RUNITE_ORE
	}
	
	public int getOreID(MinerModule.Ore ore) {
		if(ore == Ore.TIN_ORE) {
			return 53;
		}
		else if(ore == Ore.COPPER_ORE) {
			return 4645;
		}
		else if(ore == Ore.IRON_ORE) {
			return 2576;
		}
		else if(ore == Ore.COAL) {
			return 10508;
		}
		else if(ore == Ore.GOLD_ORE) {
			return 8885;
		}
		else if(ore == Ore.ADAMANTITE_ORE) {
			return 21662;
		}
		else {
			return -1;
		}
		
	} 
	

	@Override
	public boolean setupModule() {
		controller.getMovementHandler().newLocation(this.locationEnum);
		
		controller.getMovementHandler().teleportToLocation();
		
		controller.getGearHandler().handleGearSwap(Gear.UTILITY);
		
		controller.getGraphicHandler().setInfo("Miner: Setting Up Module");
		
		if(!script.getInventory().contains(f -> f != null && f.getName().toLowerCase().contains("pickaxe") )) {
			while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
			while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
			
			if(!script.getWalking().isRunEnabled() && script.getWalking().getRunEnergy() > 0) {
				script.getWalking().toggleRun();
			}

			Area debugArea = null;
			int failsafe = 0;
			while(!script.getBank().isOpen()) {
				script.getBank().open(script.getBank().getClosestBankLocation());
				
				RandomProvider.sleep(2000, 3000);
				
				if(debugArea == null || !debugArea.contains(script.getLocalPlayer())) {
					debugArea = script.getLocalPlayer().getTile().getArea(6);
					failsafe = 0;
				}
				else {
					failsafe++;
				}
				//TODO: Test if failsafe lvl is alright
				if(failsafe > 15) {
					controller.returnKeyboardAccess();
					controller.returnMouseAccess();
					return false;
				}
			}
			if(!script.getInventory().isEmpty()) {
				script.getBank().depositAllItems();	
			}
			
			RandomProvider.sleep(500, 1250);
			
			if(script.getBank().contains(f -> f != null && f.getName().toLowerCase().contains("pickaxe") )){
				script.getBank().withdraw(f -> f != null && f.getName().toLowerCase().contains("pickaxe"));
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
		return Skill.MINING;
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
		if(limit < completedActions) {
			return true;
		}
		else {
			return false;
		}
	}
	
}
