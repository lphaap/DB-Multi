package scripts;
import java.util.Random;

import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.wrappers.interactive.GameObject;

import init.ClientThread;
import movement.Location;
import movement.Locations;

public class CookerModule extends ScriptModule {
	private ClientThread script;
	private Random random;
	private Location location;
	private int delay;
	private String cookTarget;
	private String cookOn;
	private int actionsCompleted;
	private Cook foodEnum;
	private Locations locationEnum;
	
	
	public CookerModule(ClientThread script, Locations location, Cook food) {
		this.script = script;
		random = new Random();
		this.location = new Location(script, location);
		setCookTarget(food);
		locationEnum = location;
		foodEnum = food;
		setCookOn(location);
		this.moduleName = "CookingModule: " + food; 
	}
	
	@Override
	public int onLoop() {
		random.setSeed(random.nextLong());
		delay = random.nextInt(1000) + 2000;
		if(!script.getLocalPlayer().isAnimating()) {
			
			if(!script.getInventory().contains(f -> f != null && f.getName().equals(cookTarget))) {
				script.setInfoText("Cooker: Inventory Done - Banking");
				this.actionsCompleted++;
				location.travelToBank();
				script.sleep(random.nextInt(750)+ 700);
				script.getBank().depositAllItems();
				
				if(script.getBank().contains(cookTarget)) {
					script.sleep(random.nextInt(750)+ 700);
					script.getBank().withdrawAll(f -> f != null && f.getName().equals(cookTarget));
					script.sleep(random.nextInt(750)+ 700);
					script.getBank().close();
					script.sleep(random.nextInt(750)+ 400);
					script.getMouse().move();
				}
				else {
					script.nextModule();
					script.sleep(2000);
					return delay;
				}	
			}
			
			if(!location.inArea()) {
				script.setInfoText("Cooker: Moving to Area - " + locationEnum);
				location.travel();
			}
			
			if(!script.getLocalPlayer().isAnimating()) {
				script.setInfoText("Cooker: Cooking - " + foodEnum);
				GameObject heatSource = script.getGameObjects().closest(f -> f != null && f.getName().equals(cookOn));
				int randomizer = random.nextInt(2);
				if(randomizer == 0) {
					heatSource.interact();
				}
				else {
					heatSource.interact("Cook");
				}
				script.getMouse().move();
				script.sleep(random.nextInt(750)+ 1300);
				script.getKeyboard().type(1,false);
			}
			
		}
		
		return delay;
	}

	@Override
	public int actionsCompleted() {
		return this.actionsCompleted;
	}
	
	public void setCookOn(Locations location) {
		if(location == Locations.COOKING_AL_KHARID) {
			this.cookOn = "Range";
		}
		else if(location == Locations.COOKING_CATHERBY) {
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
		this.location.teleportToLocation();
		
		script.setInfoText("Cooker: Setting up module");
		if(!script.getInventory().contains(f -> f != null && f.getName().equals(this.cookTarget))) {
			
			if(!script.getWalking().isRunEnabled() && script.getWalking().getRunEnergy() > 0) {
				script.getWalking().toggleRun();
			}
	
			while(!script.getBank().isOpen()) {
				script.getBank().open(script.getBank().getClosestBankLocation());
				script.sleep(random.nextInt(1000)+2000);
			}
			
			if(!script.getInventory().isEmpty()) {
				script.getBank().depositAllItems();
				script.sleep(random.nextInt(750)+ 1000);
			}
			
			if(script.getBank().contains(cookTarget)) {
				script.getBank().withdrawAll(f -> f != null && f.getName().equals(cookTarget));
				script.sleep(random.nextInt(750)+ 700);
				script.getBank().close();
				script.sleep(random.nextInt(750)+ 400);
				script.getMouse().move();
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
		return Skill.COOKING;
	}

	@Override
	public void errorTest() {
		// TODO Auto-generated method stub
		
	}

}
