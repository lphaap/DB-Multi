package scripts;

import java.util.ArrayList;

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
	private int nullCounter;
	
	private Player thief;
	private GameObject takenOre;
	
	private Ore oreEnum;
	private ArrayList<GameObject> badOres = new ArrayList<GameObject>();
	 
	private boolean error;
	private boolean killThread;
	private boolean bankOres;
	
	private String pickName;

	
	public MinerModule(ClientThread script, ThreadController controller, LocationFactory.GameLocation gamelocation, MinerModule.Ore ore, boolean bankOres, int limit) {
		this.script = script;
		this.controller = controller;
		
		this.oreEnum = ore;
		this.locationEnum = gamelocation;
		
		this.oreID = getOreID(this.oreEnum);
		this.limit = limit;

		this.thief = null;
		this.takenOre = null;
		
		this.error = false;
		this.bankOres = bankOres;
		this.moduleName = "MiningModule: " + ore;
	}
	
	@Override
	public void run(){
		
		mainLoop: while(!killThread) {
		
			RandomProvider.sleep(1500, 2500);
			
			if(!script.getLocalPlayer().isAnimating()) {
				if(script.getInventory().isFull()) {
					if(bankOres) {
						controller.getGraphicHandler().setInfo("Miner: Inventory Full - Banking");
						
						controller.getMovementHandler().moveToBank();
						
						while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
						while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
						
						controller.debug("Mouse control: MinerModule");
						controller.debug("Keyboard control: MinerModule");
						
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
					else {
						controller.getGraphicHandler().setInfo("Miner: Inventory Full - Dropping Ores");
						
						while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
						while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
						
						script.getInventory().dropAllExcept(f -> f != null && f.getName().toLowerCase().contains("pickaxe"));
						this.completedActions++;
						
						controller.returnKeyboardAccess();
						controller.returnMouseAccess();
						
					}
				}
				else if(!controller.getMovementHandler().isPlayerInLocation()) {
					
					controller.getGraphicHandler().setInfo("Miner: Walking to Mining Area - " + locationEnum);
					
					controller.getMovementHandler().moveToLocation();
						
				}
				else if(!script.getLocalPlayer().isAnimating()) {
					
					if(!script.getInventory().contains(f -> f != null && f.getName().toLowerCase().contains("pickaxe"))) {
						controller.debug("Miner - Module ERROR");
						controller.debug("Trying to Restart Module...");
						if(!setupModule() || error) {
							controller.debug("Miner - Module ERROR");
							controller.debug("Changing Module.");
							this.killThread = true;
						}
						controller.debug("Restart Completed");
						this.error = true;
					}
					
					controller.getGraphicHandler().setInfo("Miner: Mining - " + oreEnum);
					boolean playerTest = true;
					GameObject ore = null;
					
					while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
					while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
					
					controller.debug("Mouse control: MinerModule");
					controller.debug("Keyboard control: MinerModule");
					

					ore = script.getGameObjects().closest(gameObject -> gameObject != null && gameObject.getName().equals("Rocks") 
							&& gameObject.getModelColors() != null && gameObject.getModelColors()[0] == this.oreID && !this.badOres.contains(gameObject));
					
					if(ore == null) {
						this.nullCounter++;
						
						//controller.debug("Null");
						
						if(nullCounter > 10) {
							controller.debug("ERROR: Ore Not Found");
							this.killThread();
						}
						
						controller.returnKeyboardAccess();
						controller.returnMouseAccess();
						continue mainLoop;
					}
					else if(!tileCheckN(ore.getTile()) || !tileCheckS(ore.getTile()) || 
							!tileCheckW(ore.getTile()) || !tileCheckE(ore.getTile())) {
							
							//controller.debug("Thief");
						
							badOres.add(ore);
							
							controller.returnKeyboardAccess();
							controller.returnMouseAccess();
							continue mainLoop;
							
						
					}
					
					
					int randomizer = RandomProvider.randomInt(2);
					if(randomizer == 0) {
						if(ore != null) {
							ore.interact("Mine");
							script.getMouse().move();
							nullCounter = 0;
							this.badOres.clear();
						}
					}
					else {
						if(ore != null) {
							ore.interact();
							script.getMouse().move();
							nullCounter = 0;
							this.badOres.clear();
						}
					}
					
					controller.returnKeyboardAccess();
					controller.returnMouseAccess();
				}
	
				
				}
		}
		
	}
	
	public boolean tileCheckN(Tile oreTile) {
		Player p = null;
		p = script.getPlayers().closest(f -> f != null && !f.equals(script.getLocalPlayer()) && f.getTile().equals(new Tile(oreTile.getX(),oreTile.getY()+1)) && f.isAnimating());
		if(p != null) {
			this.thief = p;
			return false;
		}
		else {
			return true;
		}
	}
	public boolean tileCheckS(Tile oreTile) {
		Player p = null;
		p = script.getPlayers().closest(f -> f != null && !f.equals(script.getLocalPlayer()) && f.getTile().equals(new Tile(oreTile.getX(),oreTile.getY()-1)) && f.isAnimating());
		if(p != null) {
			this.thief = p;
			return false;
		}
		else {
			return true;
		}
	}
	public boolean tileCheckW(Tile oreTile) {
		Player p = null;
		p = script.getPlayers().closest(f -> f != null && !f.equals(script.getLocalPlayer()) && f.getTile().equals(new Tile(oreTile.getX()-1,oreTile.getY())) && f.isAnimating());
		if(p != null) {
			this.thief = p;
			return false;
		}
		else {
			return true;
		}
	}
	public boolean tileCheckE(Tile oreTile) {
		Player p = null;
		p = script.getPlayers().closest(f -> f != null && !f.equals(script.getLocalPlayer()) && f.getTile().equals(new Tile(oreTile.getX()+1,oreTile.getY())) && f.isAnimating());
		if(p != null) {
			this.thief = p;
			return false;
		}
		else {
			return true;
		}
	}



	
	public enum Ore {
		COPPER_ORE, TIN_ORE, IRON_ORE, GOLD_ORE, COAL, ADAMANTITE_ORE, RUNITE_ORE, SILVER_ORE
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
		else if(ore == Ore.SILVER_ORE) {
			return 74;
		}
		else {
			return -1;
		}
		
	} 
	
	public void choosePickaxe() {
		int lvl = script.getSkills().getRealLevel(Skill.MINING);
		
		if(lvl >= 6 && lvl < 11) {
			this.pickName = "Steel pickaxe";
		}
		else if(lvl >= 11 && lvl < 21) {
			this.pickName = "Black pickaxe";
		}
		else if(lvl >= 21 && lvl < 31) {
			this.pickName = "Mithril pickaxe";
		}
		else if(lvl >= 31 && lvl < 41) {
			this.pickName = "Adamant pickaxe";
		}
		else if(lvl >= 41) {
			this.pickName = "Rune pickaxe";
		}
		else {
			this.pickName = "Iron pickaxe";
		}
		
		controller.debug("Set Pickaxe: " + this.pickName);
	}
	
	public boolean canUse(String pickaxeName) {
		int lvl = script.getSkills().getRealLevel(Skill.MINING);
		
		if(pickaxeName.equals("Iron pickaxe")) {
			return true;
		}
		else if(pickaxeName.equals("Steel pickaxe") && lvl >= 6) {
			return true;
		}
		else if(pickaxeName.equals("Black pickaxe") && lvl >= 11) {
			return true;
		}
		else if(pickaxeName.equals("Mithril pickaxe") && lvl >= 21) {
			return true;
		}
		else if(pickaxeName.equals("Adamant pickaxe") && lvl >= 31) {
			return true;
		}
		else if(pickaxeName.equals("Rune pickaxe") && lvl >= 41) {
			return true;
		}
		else {
			return false;
		}
	}
	

	@Override
	public boolean setupModule() {
		
		controller.getMovementHandler().newLocation(this.locationEnum);
		
		controller.getMovementHandler().teleportToLocation();
		
		controller.getGearHandler().handleGearSwap(Gear.UTILITY);
		
		controller.getGraphicHandler().setInfo("Miner: Setting Up Module");
		
		choosePickaxe(); //NOTE! Doesn't give a choice for
		
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
			
			if(script.getBank().contains(f -> f != null && f.getName().equals(this.pickName))){
				script.getBank().withdraw(f -> f != null && f.getName().equals(this.pickName));
				RandomProvider.sleep(1000, 1750);
				script.getBank().close();
				controller.returnKeyboardAccess();
				controller.returnMouseAccess();
				return true;
			}
			else if(script.getBank().contains(f -> f != null && f.getName().toLowerCase().contains("pickaxe") )){
				script.getBank().withdraw(f -> f != null && f.getName().toLowerCase().contains("pickaxe"));
				RandomProvider.sleep(1500, 2000);
				if(!canUse(script.getInventory().get(f -> f != null && f.getName().toLowerCase().contains("pickaxe")).getName())) {
					controller.returnKeyboardAccess();
					controller.returnMouseAccess();
					controller.debug("ERROR: Can't Find Usable Pickaxe");
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
		if(limit <= completedActions) {
			return true;
		}
		else {
			return false;
		}
	}
	
}
