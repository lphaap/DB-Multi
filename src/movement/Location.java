package movement;
import java.util.Random;

import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;

import init.MainLooper;

public class Location {
	private Area phase1;
	private Area phase2;
	private Area phase3;
	private Area rePhase2;
	private Area rePhase3; 
	private String obs1Action;
	private String obs2Action;
	private MainLooper script;
	private Random random;
	private boolean phase2Complete;
	private boolean phase3Complete;
	private Obstacle obstacle2; //Used in phase 2
	private Obstacle obstacle3; //Used in phase 3
	private Teleporter teleporter;
	
	/**
	 * Create Location object
	 * Phase 1 is ALWAYS final location
	 * Phase3? > Obstacle3 > Phase2? > Obstacle2 > Phase1 < Final Location
	 * @.pre script != null && l != null
	 * @.post RESULT == new Location
	 */
	public Location(MainLooper script, Locations l) {
		this.random = new Random();
		this.script = script;
		
		if(l == Locations.MINER_EAST_VARROCK) {
			phase1 = new Tile(3285,3365).getArea(4);
			teleporter = new Teleporter(script, phase1);
		}
		else if(l == Locations.MINER_LUMBRIDGE) {
			phase1 = new Tile(3226,3147).getArea(6);
			teleporter = new Teleporter(script, phase1);
		}
		else if(l== Locations.MINER_WEST_VARROCK) {
			phase1 = new Tile(3182,3370).getArea(10);
			teleporter = new Teleporter(script, phase1);
		}
		else if(l== Locations.MINER_DWARVEN_MINE_COAL) {
			phase2 = new Tile(3017,3450).getArea(1);
			//this.obstacle2 = "Trapdoor";
			phase1 = new Tile(3039,9802).getArea(5);
			script.nextModule();
		}
		else if(l == Locations.SMELTER_AL_KHARID) {
			phase1 = new Tile(3276,3186).getArea(2);
			teleporter = new Teleporter(script, phase1);
		}
		else if(l == Locations.SMITHING_WEST_VARROCK) {
			phase1 = new Tile(3187,3425).getArea(2);
			teleporter = new Teleporter(script, phase1);
		}
		else if(l == Locations.FISHING_AL_KHARID) {
			phase1 = new Tile(3267,3149).getArea(9);
			teleporter = new Teleporter(script, phase1);
		}
		else if(l == Locations.COOKING_AL_KHARID) {
			phase1 = new Tile(3273,3180).getArea(2);
			teleporter = new Teleporter(script, phase1);
		}
		else if(l == Locations.FISHING_BARBARIAN_VILLAGE) {
			phase1 = new Tile(3105,3430).getArea(7);
			teleporter = new Teleporter(script, phase1);
		}
		else if(l == Locations.COMBAT_GIANT_FROG) {
			phase1 = new Tile(3198,3176).getArea(10);
			teleporter = new Teleporter(script, phase1);
		}
		else if(l == Locations.COMBAT_BARBARIAN) {
			phase2 = new Tile(3079, 3433).getArea(2);
			this.obstacle2 = new Obstacle(script, "Longhall door", "Open"); 
			phase1 = new Tile(3078,3440).getArea(5);
			teleporter = new Teleporter(script, phase2);
		}
		else if(l == Locations.MINER_CRAFTING_GUILD_GOLD) {
			phase2 = new Tile(2933,3290).getArea(1);
			this.obstacle2 = new Obstacle(script, "Guild Door", "Open"); 
			phase1 = new Tile(2940,3279).getArea(3);
			teleporter = new Teleporter(script, phase2);
		}
		else if(l == Locations.SPLASHING_BEAR) {
			phase1 = new Tile(3225,3498).getArea(4);
			teleporter = new Teleporter(script, phase1);
		}
		else if(l == Locations.SMELTER_EDGEVILLE) {
			phase1 = new Tile(3108,3499).getArea(1);
			teleporter = new Teleporter(script, phase1);
		}
		else if(l == Locations.FISHING_CATHERBY) {
			phase1 = new Area(2832,3435, 2862,3424);
			teleporter = new Teleporter(script, phase1);
			
		}
		else if(l == Locations.COMBAT_EXPERIMENTS) {
			phase2 = new Tile(3588,3533).getArea(2);
			this.obstacle2 = new Obstacle(script, "Memorial", "Ladder", "Push",  "Climb-up"); 
			phase1 = new Tile(3483,9938).getArea(15);
			teleporter = new Teleporter(script, phase2);
		}
		else if(l == Locations.COOKING_CATHERBY) {
			phase1 = new Tile(2817,3443).getArea(1);
			teleporter = new Teleporter(script, phase1);
		}
		else {
			script.log("");
		}
	}
	
	/**
	 * Teleports player to the initialized location
	 * @.pre true
	 * @.post teleporter.teleport()
	 */
	public void teleportToLocation() {
		if(script.getClient().isMembers()) {
			teleporter.teleport();
		}
	}
	
	/**
	 * Walks the player to the initialized location
	 * @.pre (Player cannot be in a location that is not mapped / cannot walk in)
	 * @.post (Player will move to the location)
	 */
	public void travel() {
		script.setReact(0);
		int runEnergyTest = random.nextInt(10) + 1;
		
		if(phase3 != null && !this.phase3Complete) {
			int failSafe2 = 0;
			
			while(!phase3.contains(script.getWalking().getDestination()) && !phase3.contains(script.getLocalPlayer())) {
				script.getWalking().walk(phase3.getRandomTile());
				script.sleep(random.nextInt(1000)+2000);
				if(script.getWalking().getRunEnergy() >= runEnergyTest && !script.getWalking().isRunEnabled()) {
					script.getWalking().toggleRun();
					script.sleep(random.nextInt(1000)+2000);
					runEnergyTest = random.nextInt(10) + 1;
				}
			}
			
			script.sleep(random.nextInt(1000)+4000);
			while(obstacle3.interactBefore() && failSafe2 <= 3 ) {
				script.sleep(random.nextInt(1000)+2000);
				failSafe2++;
			}
			if(failSafe2 >= 5) {
				script.stop();
				script.log("ERROR: Travel to location failed");
			}
			while(!obstacle3.afterObjectExists()) {
				script.sleep(1000);
			}
			script.sleep(random.nextInt(1000)+1500);
			this.rePhase3 = script.getLocalPlayer().getTile().getArea(3);
			this.phase3Complete = true;
		}
		
		if(phase2 != null && !this.phase2Complete) {
			int failSafe1 = 0;
	
			
			while(!phase2.contains(script.getWalking().getDestination()) && !phase2.contains(script.getLocalPlayer())) {
				script.getWalking().walk(phase2.getRandomTile());
				script.sleep(random.nextInt(1000)+2000);
				if(script.getWalking().getRunEnergy() >= runEnergyTest && !script.getWalking().isRunEnabled()) {
					script.getWalking().toggleRun();
					script.sleep(random.nextInt(1000)+2000);
					runEnergyTest = random.nextInt(10) + 1;
				}
			}
			
			script.sleep(random.nextInt(1000)+4000);
			while(!obstacle2.interactBefore() && failSafe1 <= 3 ) {
				script.sleep(random.nextInt(1000)+2000);
				failSafe1++;
			}
			if(failSafe1 >= 5) {
				script.stop();
			}
			while(!obstacle2.afterObjectExists()) {
				script.sleep(1000);
				script.log("Not found!");
			}
			script.log("Sleeping..");
			script.sleep(random.nextInt(1000)+1500);
			this.rePhase2 = script.getLocalPlayer().getTile().getArea(3);
			script.log(""+ rePhase2.getCenter().getX() + " - " + rePhase2.getCenter().getY());
			this.phase2Complete = true;
			
		}
		
		if(phase1 != null) {
			script.sleep(random.nextInt(1000)+2500);
			while(!phase1.contains(script.getWalking().getDestination()) && !phase1.contains(script.getLocalPlayer())) {
				script.getWalking().walk(phase1.getRandomTile());
				script.sleep(random.nextInt(1000)+2000);
				
				if(script.getWalking().getRunEnergy() >= runEnergyTest && !script.getWalking().isRunEnabled()) {
					script.getWalking().toggleRun();
					script.sleep(random.nextInt(1000)+500);
					runEnergyTest = random.nextInt(10) + 1;
				}
			}
			
		}
		
	}
	
	/**
	 * Travels to the closest bank and opens it
	 * @.pre (Player cannot be in a location that is not mapped / cannot walk in)
	 * @.post (Player will move to the closest bank)
	 */
	public void travelToBank() {
		script.setReact(0);
		int runEnergyTest = random.nextInt(10) + 1;
		
		if(phase2 != null && rePhase2 != null) {
			script.sleep(random.nextInt(1000)+2000);
			int failSafe1 = 0;
			while(!rePhase2.contains(script.getWalking().getDestination()) && !rePhase2.contains(script.getLocalPlayer())) {
				script.getWalking().walk(rePhase2.getRandomTile());
				script.sleep(random.nextInt(1000)+2000);
				if(script.getWalking().getRunEnergy() >= runEnergyTest && !script.getWalking().isRunEnabled()) {
					script.getWalking().toggleRun();
					script.sleep(random.nextInt(1000)+2000);
					runEnergyTest = random.nextInt(10) + 1;
				}
			}
			script.sleep(random.nextInt(1000)+4000);
			while(!obstacle2.interactAfter() && failSafe1 <= 3 ) {
				script.sleep(random.nextInt(1000)+2000);
				failSafe1++;
			}
			if(failSafe1 >= 5) {
				script.stop();
			}
			this.phase2Complete = false;
			
		}
		
		if(phase3 != null && rePhase3 != null) {
			script.sleep(random.nextInt(1000)+2000);
			int failSafe2 = 0;
			while(!rePhase3.contains(script.getWalking().getDestination()) && !rePhase3.contains(script.getLocalPlayer())) {
				script.getWalking().walk(rePhase3.getRandomTile());
				script.sleep(random.nextInt(1000)+2000);
				if(script.getWalking().getRunEnergy() >= runEnergyTest && !script.getWalking().isRunEnabled()) {
					script.getWalking().toggleRun();
					script.sleep(random.nextInt(1000)+2000);
					runEnergyTest = random.nextInt(10) + 1;
				}
			}
			script.sleep(random.nextInt(1000)+4000);
			while(!obstacle3.interactAfter() && failSafe2 <= 3 ) {
				script.sleep(random.nextInt(1000)+2000);
				failSafe2++;
			}
			if(failSafe2 >= 5) {
				script.stop();
				script.log("ERROR: Travel to location failed");
			}
			this.phase2Complete = false;
			
		}
		
		script.sleep(random.nextInt(1000)+2000);
		while(!script.getBank().isOpen()) {
			script.getBank().open(script.getBank().getClosestBankLocation());
			script.sleep(random.nextInt(1000)+2000);
		}
		
	}
	
	/**
	 * Gets a random tile from target location (phase1)
	 * @.pre true
	 * @.post RESULT == this.phase1.getRandomTile()
	 */
	public Tile randomTragetTile() {
		return this.phase1.getRandomTile();
	}
	
	/**
	 * Checks if player is in wanted location (phase1)
	 * @.pre true
	 * @.post (RESULT == true && (player is in area)) || RESULT == false
	 */
	public boolean inArea() {
		return phase1.contains(script.getLocalPlayer());
	}
	
}
