package scripts;
import java.awt.AWTException;
import java.awt.Robot;
import java.util.Random;

import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.wrappers.interactive.Entity;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.Player;

import init.ClientThread;
import movement.Location;
import movement.Locations;

public class MinerModule extends ScriptModule{
	private ClientThread script;
	private Location location;
	private int oreID;
	private int delay;
	private Random random;
	private Robot robot;
	private int completedActions;
	private GameObject takenOre;
	private Ore ore;
	private Locations locationEnum;
	private int limit;
	private Player thief;
	private boolean error;

	
	public MinerModule(ClientThread script, Locations location, MinerModule.Ore ore, int limit) {
		this.script = script;
		this.limit = limit; 
		this.ore = ore;
		this.locationEnum = location;
		this.location = new Location(script, location);
		this.oreID = getOreID(this.ore);
		if(oreID == -1) {
			script.stop();
		}
		this.thief = null;
		//script.log("Ore ID: "+oreID);
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		random = new Random();
		this.takenOre = null;
		this.error = false;
		this.moduleName = "MiningModule: " + ore;
	}
	
	@Override
	public int onLoop(){
		if(limit < completedActions) {
			script.nextModule();
			script.sleep(2000);
			return delay;
		}
		
		random.setSeed(random.nextLong());
		delay = random.nextInt(1000) + 2500;
		if(!script.getLocalPlayer().isAnimating()) {
			if(script.getInventory().isFull()) {
				script.setInfoText("Miner: Inventory Full - Banking");
				script.setReact(0);
				
				location.travelToBank();
				
				script.sleep(random.nextInt(750)+ 1000);
				script.getBank().depositAllExcept(f -> f != null && f.getName().contains("pickaxe"));
				script.sleep(random.nextInt(750)+ 1000);
				script.getBank().close();
				script.sleep(random.nextInt(750)+ 500);
				script.getMouse().move();
				completedActions++;
			}
			else if(!location.inArea()) {
				script.setReact(0);
				script.setInfoText("Miner: Walking to Mining Area - " + locationEnum);
				
				location.travel();
					
			}
			else if(!script.getLocalPlayer().isAnimating()) {
				
				if(!script.getInventory().contains(f -> f != null && f.getName().toLowerCase().contains("pickaxe"))) {
					script.getMessenger().sendMessage("Miner - Module ERROR");
					script.getMessenger().sendMessage("Trying to Restart Module...");
					if(!setupModule() || error) {
						script.getMessenger().sendMessage("Miner - Module ERROR");
						script.getMessenger().sendMessage("Changing Module.");
						script.nextModule();
					}
					script.getMessenger().sendMessage("Restart Completed");
					this.error = true;
				}
				
				script.setReact(1);
				script.setInfoText("Miner: Mining - " + ore);
				boolean playerTest = true;
				GameObject ore = null;
		
				thief = script.getPlayers().closest(f -> f != null);
				if(thief != null) {
					ore = script.getGameObjects().closest(gameObject -> gameObject != null && gameObject.getName().equals("Rocks") 
															&& gameObject.getModelColors() != null && gameObject.getModelColors()[0] == this.oreID
															&& !gameObject.getSurroundingArea(1).contains(thief));
				}
				else {
					ore = script.getGameObjects().closest(gameObject -> gameObject != null && gameObject.getName().equals("Rocks") 
							&& gameObject.getModelColors() != null && gameObject.getModelColors()[0] == this.oreID);
				}
				int randomizer = random.nextInt(3);
				if(randomizer == 0){
					randomizer = random.nextInt(2);
					if(randomizer == 0) {
						script.getCamera().keyboardRotateToTile(ore.getSurroundingArea(1).getRandomTile());
					}
					else {
						script.getCamera().mouseRotateToTile(ore.getSurroundingArea(1).getRandomTile());
					}
				}
				
				int tester = random.nextInt(2);
				if(tester == 0) {
					ore.interact("Mine");
					script.getMouse().move();
				}
				else {
					ore.interact();
					script.getMouse().move();
				}
			}

			
				
			
		}
		
		return delay;
	}

	@Override
	public int actionsCompleted() {		
		return completedActions;
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
		this.location.teleportToLocation();
		
		script.setReact(0);
		script.setInfoText("Miner: Setting Up Module");
		if(!script.getInventory().contains(f -> f != null && f.getName().toLowerCase().contains("pickaxe") )) {
			if(!script.getWalking().isRunEnabled() && script.getWalking().getRunEnergy() > 0) {
				script.getWalking().toggleRun();
			}

			while(!script.getBank().isOpen()) {
				script.getBank().open(script.getBank().getClosestBankLocation());
				script.sleep(random.nextInt(1000)+2000);
			}
			if(!script.getInventory().isEmpty()) {
				script.getBank().depositAllItems();	
			}
			script.sleep(random.nextInt(750)+ 500);
			if(script.getBank().contains(f -> f != null && f.getName().toLowerCase().contains("pickaxe") )){
				script.getBank().withdraw(f -> f != null && f.getName().toLowerCase().contains("pickaxe"));
				script.sleep(random.nextInt(750)+ 1000);
				script.getBank().close();
				return true;
			}
			else {
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
	public void errorTest() {
		// TODO Auto-generated method stub
		
	}
	
}
