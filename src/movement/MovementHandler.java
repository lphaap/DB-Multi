package movement;

import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;

import antiban.RandomProvider;
import client.ClientThread;
import client.KillableHandler;
import client.KillableThread;
import client.ThreadController;

public class MovementHandler implements KillableHandler {
	private ThreadController controller;
	private Location location;
	private ClientThread client;
	
	private int debugCounter = 0;
	private int debugAreaCounter = 0;
	private Area debugArea;

	private boolean killHandler;
	
	public MovementHandler(ClientThread client, ThreadController controller) {
		this.controller = controller;
	}
	
	/**
	 * Handles own input accsess
	 * @throws IllegalArgumentException
	 */
	public void moveToLocation() {
		while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);};
		while(controller.requestMouseAccess()) {RandomProvider.sleep(10);};
		
		controller.debug("Mouse control: MovementHandler");
		controller.debug("Keyboard control: MovementHandler");
		
		new Thread( () -> location.travelPhase3()).start();
		while(!location.isPhase3Done()) {
			if(monitorMovement()) {
				return;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {e.printStackTrace();}
		}
		resetMonitor();
		
		new Thread( () -> location.travelPhase2()).start();
		while(!location.isPhase2Done()) {
			if(monitorMovement()) {
				return;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {e.printStackTrace();}
		}
		resetMonitor();
		
		new Thread( () -> location.travelPhase1()).start();
		while(!location.isPhase1Done()) {
			if(monitorMovement()) {
				return;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {e.printStackTrace();}
		}
		resetMonitor();
		
		controller.returnKeyboardAccess();
		controller.returnMouseAccess();
		
	}
	
	/**
	 * Handles own input accsess
	 * @throws IllegalArgumentException
	 */
	public void moveToBank() {
		while(controller.requestKeyboardAccess());
		while(controller.requestMouseAccess());
		
		new Thread( () -> location.reTravelPhase2()).start();
		while(!location.isRePhase2Done()) {
			if(monitorMovement()) {
				return;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {e.printStackTrace();}
		}
		resetMonitor();
		
		new Thread( () -> location.reTravelPhase3()).start();
		while(!location.isRePhase3Done()) {
			if(monitorMovement()) {
				return;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {e.printStackTrace();}
		}
		resetMonitor();
		
		new Thread( () -> location.reTravelToBank()).start();
		while(!location.isReBankingDone()) {
			if(monitorMovement()) {
				return;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {e.printStackTrace();}
		}
		resetMonitor();
		
		controller.returnKeyboardAccess();
		controller.returnMouseAccess();
	}
	
	public void teleportToLocation() {
		while(controller.requestKeyboardAccess());
		while(controller.requestMouseAccess());
		
		new Thread( () -> location.teleportToLocation()).start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {e.printStackTrace();}
		while(location.isTeleportInProgress()) {
			if(monitorMovement()) {
				return;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {e.printStackTrace();}
		}
		resetMonitor();
		
		controller.returnKeyboardAccess();
		controller.returnMouseAccess();
		
	}

	
	public void newLocation(LocationFactory.GameLocation location) {
		try {
			this.location = LocationFactory.newLocation(client, location);
		}
		catch(IllegalArgumentException e) {
			controller.nextModule();
		}
	}
	
	/**
	 * Returns true if problem appears
	 */
	private boolean monitorMovement() {
		if(debugArea == null || !debugArea.contains(client.getLocalPlayer())) {
			this.debugArea = client.getLocalPlayer().getTile().getArea(6);
			this.debugAreaCounter = 0;
		}
		else {
			debugAreaCounter++;
		}
		debugCounter++;
		
		if(this.killHandler) {
			this.location.killCurrentAction();
			return true;
		}
		else {
			return false;
		}
		//TODO: What if counter goes too far + something needs to reset location thread killer
	}
	
	private void resetMonitor() {
		debugCounter = 0;
		debugAreaCounter = 0;
		debugArea = null;
	}
	
	public boolean isPlayerInLocation() {
		return this.location.inArea();
	}
	
	public Tile getRandomLocationTile() {
		return this.location.randomTragetTile();
	}

	@Override
	public void killHandler() {
		this.killHandler = true;
	}

}
