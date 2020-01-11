package scripts;
import java.util.Random;

import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;

import init.MainLooper;
import movement.Location;
import movement.Locations;

public class FishingModule extends ScriptModule {
	private int actionsCompleted;
	private String thrashFish;
	private boolean thrashFishTest;
	private Location location;
	private Random random;
	private int limit;
	private MainLooper script;
	private int delay;
	private boolean bankTheFish;
	private String interactWith;
	private String fishingSpot;
	private String bait;
	private boolean useBait;
	private String fishingGear;
	private String payFish;
	private Locations locationEnum;
	private Fish fishEnum;
	private int error;
	private String defineSpot;
	
	
	
	
	public FishingModule(MainLooper script, FishingModule.Fish fish, int limit, boolean bank) {
		this.limit = limit;
		this.script = script;
		this.bankTheFish = bank;
		this.random = new Random();
		setModuleVariables(fish);
		this.fishEnum = fish;
		this.error = 0;
		this.moduleName = "FishingModule: " + fish;
	}
	
	@Override
	public int onLoop() {
		
		random.setSeed(random.nextLong());
		delay = random.nextInt(1000) + 2000;
		
		if(limit <= this.actionsCompleted) {
			script.nextModule();
			script.sleep(2000);
			return delay;
		}
		if(this.error > 5) {
			script.nextModule();
			script.getMessenger().sendMessage("Fishing location NOT FOUND error");
			script.sleep(2000);
			return delay;
		}
		
		if(!script.getInventory().contains(f -> f != null && f.getName().equals(fishingGear))
			||  (!script.getInventory().contains(f -> f != null && f.getName().equals(bait)) && useBait)) {
			script.nextModule();
			script.sleep(2000);
			return delay;
		}
		
		if(!script.getLocalPlayer().isAnimating()) {
			if(script.getInventory().isFull()) {
				if(this.bankTheFish) {
					if(this.thrashFishTest) {
						if(script.getInventory().count(f -> f != null && f.getName().equals(payFish)) >= 23) {
							bank();
						}
						else {
							script.setInfoText("Fisher: Dropping Thrash Fish");
							script.getInventory().dropAllExcept(f -> f != null && (f.getName().equals(payFish) || f.getName().equals(fishingGear) 
									|| f.getName().equals(bait)));
						}
					}
					else {
						bank();
					}
				}
				else {
					script.setInfoText("Fisher: Dropping Inventory");
					script.getInventory().dropAll();
					this.actionsCompleted++;
				}
			}
			
			else if(!location.inArea()) {
				script.setInfoText("Fisher: Walking to Location - " + this.locationEnum);
				location.travel();
			}
			
			else if(!script.getLocalPlayer().isAnimating()) {
				script.setReact(1);
				script.setInfoText("Fisher: Fishing - " + this.fishEnum);
				NPC fishSpot = script.getNpcs().closest(f -> f != null && f.getName().equals(fishingSpot) && f.hasAction(this.defineSpot));
				if(fishSpot != null) {
					fishSpot.interact(interactWith);
					script.getMouse().move();
					this.error = 0;
				}
				else {
					script.getWalking().walk(location.randomTragetTile());
					script.sleep(random.nextInt(1000)+2000);
					error++;
				}
			}
			
		}
		
		return delay;
	}
	
	public void bank() {
		script.setReact(0);
		script.setInfoText("Fisher: Inventory Full - Banking");
		this.actionsCompleted++;
		location.travelToBank();
		script.sleep(random.nextInt(1000)+1000);
		script.getBank().depositAllExcept(f -> f != null && (f.getName().equals(bait) || f.getName().equals(fishingGear)));
		script.sleep(random.nextInt(1000)+1000);
		script.getBank().close();
		script.sleep(random.nextInt(1000)+1000);
		script.getMouse().move();
		
	}

	public enum Fish {
		HERRING, TROUT, TROUT_SALMON, SALMON, TUNA, LOBSTER, SWORDFISH, TUNA_SWORDFISH, SHARK
	}
	
	/**
	 * Sets: location, trashFish, fishingSpot
	 */
	public void setModuleVariables(Fish fish) {
		if(fish == Fish.HERRING) {
			location = new Location(script, Locations.FISHING_AL_KHARID);
			this.locationEnum = Locations.FISHING_AL_KHARID;
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
			location = new Location(script, Locations.FISHING_BARBARIAN_VILLAGE);
			this.locationEnum = Locations.FISHING_BARBARIAN_VILLAGE;
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
			location = new Location(script, Locations.FISHING_BARBARIAN_VILLAGE);
			this.locationEnum = Locations.FISHING_BARBARIAN_VILLAGE;
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
			location = new Location(script, Locations.FISHING_BARBARIAN_VILLAGE);
			this.locationEnum = Locations.FISHING_BARBARIAN_VILLAGE;
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
			location = new Location(script, Locations.FISHING_CATHERBY);
			this.locationEnum = Locations.FISHING_CATHERBY;
			this.useBait = false;
			this.bait = "";
			this.thrashFishTest = false;
			this.fishingSpot = "Fishing spot";
			this.fishingGear = "Lobster pot";
			this.interactWith = "Cage";
			this.defineSpot = this.interactWith;
		}
		else if(fish == Fish.TUNA) {
			location = new Location(script, Locations.FISHING_CATHERBY);
			this.locationEnum = Locations.FISHING_CATHERBY;
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
			location = new Location(script, Locations.FISHING_CATHERBY);
			this.locationEnum = Locations.FISHING_CATHERBY;
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
			location = new Location(script, Locations.FISHING_CATHERBY);
			this.locationEnum = Locations.FISHING_CATHERBY;
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
			location = new Location(script, Locations.FISHING_CATHERBY);
			this.locationEnum = Locations.FISHING_CATHERBY;
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
	public int actionsCompleted() {
		return this.actionsCompleted;
	}

	@Override
	public boolean setupModule() {
		this.location.teleportToLocation();
		
		script.setReact(0);
		script.setInfoText("Fisher: Setting Up Module");
		if(!script.getInventory().contains(f -> f != null && f.getName().equals(fishingGear))
				||  (!script.getInventory().contains(f -> f != null && f.getName().equals(bait) && useBait))){
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
			if(script.getBank().contains(f -> f != null && f.getName().equals(this.fishingGear) )){
				script.getBank().withdraw(f -> f != null && f.getName().equals(this.fishingGear));
				script.sleep(random.nextInt(750)+ 1000);
				if(script.getBank().contains(f -> f != null && f.getName().equals(bait))){
					script.getBank().withdrawAll(f -> f != null && f.getName().equals(bait));
				}
				else if(useBait){
					return false;
				}
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
		return Skill.FISHING;
	}

	@Override
	public void errorTest() {
		// TODO Auto-generated method stub
		
	}

}
