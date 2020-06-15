package movement;
import java.util.Random;

import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;

import antiban.RandomProvider;
import client.ClientThread;

public class Location {
	private Area phase1;
	private Area phase2;
	private Area phase3;
	private Area rePhase2;
	private Area rePhase3; 
	private String obs1Action;
	private String obs2Action;
	private ClientThread script;
	private boolean phase1Complete;
	private boolean phase2Complete;
	private boolean phase3Complete;
	private boolean rePhase1Complete;
	private boolean rePhase2Complete;
	private boolean rePhase3Complete;
	private boolean teleportInProgress;
	private Obstacle obstacle2; //Used in phase 2
	private Obstacle obstacle3; //Used in phase 3
	private Teleporter teleporter;
	
	private boolean killCurrentAction;
	
	/**
	 * Create Location object
	 * Phase 1 is ALWAYS final location
	 * Phase3? > Obstacle3 > Phase2? > Obstacle2 > Phase1 < Final Location
	 * @.pre script != null
	 * @.post RESULT == new Location
	 */
	
	/**
	 * Teleports player to the initialized location
	 * @.pre true
	 * @.post teleporter.teleport()
	 */
	public void teleportToLocation() {
		this.teleportInProgress = true;
		if(script.getClient().isMembers()) {
			teleporter.teleport();
		}
		this.teleportInProgress = false;
	}
	
	/**
	 * Walks the player to the initialized location
	 * @.pre (Player cannot be in a location that is not mapped / cannot walk in)
	 * @.post (Player will move to the location)
	 */
	public void travelPhase1() {
		int runEnergyTest = RandomProvider.randomInt(10) + 1;
		this.killCurrentAction = false;
		
		
		if(!this.inArea()) {
			this.phase1Complete = false;
		}
		
		if(phase1 != null && !this.phase1Complete) {
			script.sleep(RandomProvider.randomInt(1000)+2500);
			while(!phase1.contains(script.getWalking().getDestination()) && !phase1.contains(script.getLocalPlayer()) && !killCurrentAction) {
				script.getWalking().walk(phase1.getRandomTile());
				script.sleep(RandomProvider.randomInt(1000)+2000);
				
				if(script.getWalking().getRunEnergy() >= runEnergyTest && !script.getWalking().isRunEnabled()) {
					script.getWalking().toggleRun();
					script.sleep(RandomProvider.randomInt(1000)+500);
					runEnergyTest = RandomProvider.randomInt(10) + 1;
				}
			}
			this.phase1Complete = true;
			this.rePhase1Complete = false;
		}
		
	}
	
	public void travelPhase2() {
		int runEnergyTest = RandomProvider.randomInt(10) + 1;
		this.killCurrentAction = false;
		
		if(phase2 != null && !this.phase2Complete) {
			int failSafe1 = 0;
	
			
			while(!phase2.contains(script.getWalking().getDestination()) && !phase2.contains(script.getLocalPlayer()) && !killCurrentAction) {
				script.getWalking().walk(phase2.getRandomTile());
				script.sleep(RandomProvider.randomInt(1000)+2000);
				if(script.getWalking().getRunEnergy() >= runEnergyTest && !script.getWalking().isRunEnabled()) {
					script.getWalking().toggleRun();
					script.sleep(RandomProvider.randomInt(1000)+2000);
					runEnergyTest = RandomProvider.randomInt(10) + 1;
				}
			}
			
			script.sleep(RandomProvider.randomInt(1000)+4000);
			while(!obstacle2.interactBefore() && failSafe1 <= 3 ) {
				script.sleep(RandomProvider.randomInt(1000)+2000);
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
			script.sleep(RandomProvider.randomInt(1000)+1500);
			this.rePhase2 = script.getLocalPlayer().getTile().getArea(3);
			script.log(""+ rePhase2.getCenter().getX() + " - " + rePhase2.getCenter().getY());
			
			this.phase2Complete = true;
			this.rePhase2Complete = false;
			
		
			
		}
		
	}

	public void travelPhase3() {
		int runEnergyTest = RandomProvider.randomInt(10) + 1;
		this.killCurrentAction = false;
		
		if(phase3 != null && !this.phase3Complete) {
			int failSafe2 = 0;
			
			while(!phase3.contains(script.getWalking().getDestination()) && !phase3.contains(script.getLocalPlayer()) && !killCurrentAction) {
				script.getWalking().walk(phase3.getRandomTile());
				script.sleep(RandomProvider.randomInt(1000)+2000);
				if(script.getWalking().getRunEnergy() >= runEnergyTest && !script.getWalking().isRunEnabled()) {
					script.getWalking().toggleRun();
					script.sleep(RandomProvider.randomInt(1000)+2000);
					runEnergyTest = RandomProvider.randomInt(10) + 1;
				}
			}
			script.sleep(RandomProvider.randomInt(1000)+4000);
			while(obstacle3.interactBefore() && failSafe2 <= 3 ) {
				script.sleep(RandomProvider.randomInt(1000)+2000);
				failSafe2++;
			}
			if(failSafe2 >= 5) {
				script.stop();
				script.log("ERROR: Travel to location failed");
			}
			while(!obstacle3.afterObjectExists()) {
				script.sleep(1000);
			}
			script.sleep(RandomProvider.randomInt(1000)+1500);
			this.rePhase3 = script.getLocalPlayer().getTile().getArea(3);
			
			this.phase3Complete = true;
			this.rePhase3Complete = false;
			
		}
		
	}
	
	public void reTravelToBank(){
		int runEnergyTest = RandomProvider.randomInt(10) + 1;
		script.sleep(RandomProvider.randomInt(1000)+2000);
		while(!script.getBank().isOpen() && !killCurrentAction) {
			script.getBank().open(script.getBank().getClosestBankLocation());
			script.sleep(RandomProvider.randomInt(1000)+2000);
			if(script.getWalking().getRunEnergy() >= runEnergyTest && !script.getWalking().isRunEnabled()) {
				script.getWalking().toggleRun();
				script.sleep(RandomProvider.randomInt(1000)+2000);
				runEnergyTest = RandomProvider.randomInt(10) + 1;
			}
		}
		this.phase1Complete = false;
		this.rePhase1Complete = true;
	}
	
	public void reTravelPhase2() {
		int runEnergyTest = RandomProvider.randomInt(10) + 1;
		if(phase2 != null && rePhase2 != null) {
			script.sleep(RandomProvider.randomInt(1000)+2000);
			int failSafe1 = 0;
			while(!rePhase2.contains(script.getWalking().getDestination()) && !rePhase2.contains(script.getLocalPlayer()) && !killCurrentAction) {
				script.getWalking().walk(rePhase2.getRandomTile());
				script.sleep(RandomProvider.randomInt(1000)+2000);
				if(script.getWalking().getRunEnergy() >= runEnergyTest && !script.getWalking().isRunEnabled()) {
					script.getWalking().toggleRun();
					script.sleep(RandomProvider.randomInt(1000)+2000);
					runEnergyTest = RandomProvider.randomInt(10) + 1;
				}
			}
			script.sleep(RandomProvider.randomInt(1000)+4000);
			while(!obstacle2.interactAfter() && failSafe1 <= 3 ) {
				script.sleep(RandomProvider.randomInt(1000)+2000);
				failSafe1++;
			}
			if(failSafe1 >= 5) {
				script.stop();
			}
			this.phase2Complete = false;
			this.rePhase2Complete = true;
			
		}
	}
	public void reTravelPhase3() {
		int runEnergyTest = RandomProvider.randomInt(10) + 1;
		if(phase3 != null && rePhase3 != null) {
			script.sleep(RandomProvider.randomInt(1000)+2000);
			int failSafe2 = 0;
			while(!rePhase3.contains(script.getWalking().getDestination()) && !rePhase3.contains(script.getLocalPlayer()) && !killCurrentAction) {
				script.getWalking().walk(rePhase3.getRandomTile());
				script.sleep(RandomProvider.randomInt(1000)+2000);
				if(script.getWalking().getRunEnergy() >= runEnergyTest && !script.getWalking().isRunEnabled()) {
					script.getWalking().toggleRun();
					script.sleep(RandomProvider.randomInt(1000)+2000);
					runEnergyTest = RandomProvider.randomInt(10) + 1;
				}
			}
			script.sleep(RandomProvider.randomInt(1000)+4000);
			while(!obstacle3.interactAfter() && failSafe2 <= 3 ) {
				script.sleep(RandomProvider.randomInt(1000)+2000);
				failSafe2++;
			}
			if(failSafe2 >= 5) {
				script.stop();
				script.log("ERROR: Travel to location failed");
			}
			this.phase3Complete = false;
			this.rePhase3Complete = true;
		}
	}

	
	/* Depcerated
	public void travel() {
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
	Depcerated */ 
	
	/**
	 * Travels to the closest bank and opens it
	 * @.pre (Player cannot be in a location that is not mapped / cannot walk in)
	 * @.post (Player will move to the closest bank)
	 
	public void travelToBank() {

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
		
	}*/
	
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
	
	public void killCurrentAction() {
		this.killCurrentAction = true;
	}
	
	public void setPhase1(Area inGameArea) {
		this.phase1 = inGameArea;
	}
	public void setPhase2(Area inGameArea) {
		this.phase2 = inGameArea;
	}
	public void setPhase3(Area inGameArea) {
		this.phase3 = inGameArea;
	}
	public void setTeleporter(Teleporter teleporter) {
		this.teleporter = teleporter;
	}
	public void setClient(ClientThread client) {
		this.script = client;
	}
	public void setObstacle2(Obstacle obstacle) {
		this.obstacle2 = obstacle;
	}
	public void setObstacle3(Obstacle obstacle) {
		this.obstacle3 = obstacle;
	}
	public boolean isPhase1Done() {
		if(this.phase1 != null) {
			return this.phase1Complete;
		}
		else {
			return true;
		}
	}
	public boolean isPhase2Done() {
		if(this.phase2 != null) {
			return this.phase2Complete;
		}
		else {
			return true;
		}
	}
	public boolean isPhase3Done() {
		if(this.phase3 != null) {
			return this.phase3Complete;
		}
		else {
			return true;
		}
	}
	public boolean isReBankingDone() {
		
			return this.phase1Complete;
	
	}
	public boolean isRePhase2Done() {
		if(this.rePhase2 != null) {
			return this.rePhase2Complete;
		}
		else {
			return true;
		}
	}
	public boolean isTeleportInProgress() {
		return this.teleportInProgress;
	}
	public boolean isRePhase3Done() {
		if(this.rePhase3 != null) {
			return this.rePhase3Complete;
		}
		else {
			return true;
		}
	}
	
	
}
