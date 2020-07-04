package movement;

import java.util.ArrayList;
import java.util.Collections;

import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;

import antiban.RandomProvider;
import client.ClientThread;

public class Location {
	private ClientThread script;
	
	private Teleporter teleporter; 
	
	private ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
	private ArrayList<Obstacle> obstaclesReversed = new ArrayList<Obstacle>();
	
	private Area targetArea;
	
	private boolean teleportInProgress;
	private boolean movementInProgress;
	private boolean killAction;
	private boolean error;
	
	private int indexOfMovement;
	
	public boolean travelToLocation() {
		this.movementInProgress = true;
		
		for(Obstacle obstacle : obstacles) {
			this.indexOfMovement = obstacles.indexOf(obstacle);
			if(killAction) {
				return false;
			}
			moveToArea(obstacle.getGoingLocation());
			if(!obstacle.handleBeforeInteraction()) {
				return false;
			}
			RandomProvider.sleep(100,220);
		}
		this.indexOfMovement = -1;

		moveToArea(targetArea);
		
		this.movementInProgress = false;
		return true;
	}
	
	public boolean travelToBank() {
		this.movementInProgress = true;
		for(Obstacle obstacle : obstaclesReversed) {
			this.indexOfMovement = obstaclesReversed.indexOf(obstacle);
			if(killAction) {
				return false;
			}
			moveToArea(obstacle.getReturnLocation());
			if(!obstacle.handleAfterInteraction()) {
				return false;
			}
			RandomProvider.sleep(100,220);
		}
		this.indexOfMovement = -1;
		moveToBank();
		this.movementInProgress = false;
		return true;
	}
	
	private void moveToBank() {
		int runEnergyTest = RandomProvider.randomInt(10) + 1;
		script.sleep(RandomProvider.randomInt(1000)+2000);
		while(!script.getBank().isOpen()) {
			script.getBank().open(script.getBank().getClosestBankLocation());
			script.sleep(RandomProvider.randomInt(1000)+2000);
			
			if(script.getWalking().getRunEnergy() >= runEnergyTest && !script.getWalking().isRunEnabled()) {
				script.getWalking().toggleRun();
				script.sleep(RandomProvider.randomInt(1000)+2000);
				runEnergyTest = RandomProvider.randomInt(10) + 1;
			}
			
			if(killAction) {
				break;
			}
		}
	}
	
	private void moveToArea(Area area) {
			int runEnergyTest = RandomProvider.randomInt(10) + 1;
			script.sleep(RandomProvider.randomInt(1000)+2500);
			while(!area.contains(script.getWalking().getDestination()) && !area.contains(script.getLocalPlayer())) {
				script.getWalking().walk(area.getRandomTile());
				
				script.sleep(RandomProvider.randomInt(1000)+2000);
				
				if(script.getWalking().getRunEnergy() >= runEnergyTest && !script.getWalking().isRunEnabled()) {
					script.getWalking().toggleRun();
					script.sleep(RandomProvider.randomInt(1000)+500);
					runEnergyTest = RandomProvider.randomInt(10) + 1;
				}
				
				if(killAction) {
					return;
				}
				
			}
		
	}
	
	/*
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
	
	public boolean isTeleportInProgress() {
		return this.teleportInProgress;
	}
	
	public boolean inArea() {
		if(this.targetArea != null) {
			return this.targetArea.contains(script.getLocalPlayer());
		}
		else {
			return false;
		}
	}
	
	public Area getMainArea() {
		return this.targetArea;
	}
	
	public void killCurrentAction() {
		this.killAction = true;
	}
	
	public void setClient(ClientThread client) {
		this.script = client;
	}

	public void setObstacles(ArrayList<Obstacle> obstacles) {
		this.obstacles = obstacles;
		this.obstaclesReversed = (ArrayList<Obstacle>)this.obstacles.clone();
		Collections.reverse(obstaclesReversed);
	}
	
	public Tile randomTragetTile() {
		if(this.targetArea != null) {
			return this.targetArea.getRandomTile();
		}
		else {
			return null;
		}
	}

	public void setTeleporter(Teleporter t) {
		this.teleporter = t;
	}
	
	public void setTargetArea(Area targetArea) {
		this.targetArea = targetArea;
	}
	
	public int getIndexOfMovement() {
		return this.indexOfMovement;
	}
	
	public boolean isMovementInProgress() {
		return this.movementInProgress;
	}
	
	
}
