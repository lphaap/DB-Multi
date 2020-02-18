package movement;

import org.dreambot.api.methods.map.Area;

import client.ClientThread;
import client.KillableThread;
import client.ThreadController;

public class MovementHandler {
	private ThreadController controller;
	private Location location;
	private ClientThread client;
	
	private int debugCounter = 0;
	private int debugAreaCounter = 0;
	private Area debugArea;

	
	public MovementHandler(ThreadController controller, ClientThread client) {
		this.controller = controller;
	}
	
	/**
	 * Handles own input accsess
	 * @throws IllegalArgumentException
	 */
	public void moveToLocation() {
		while(controller.requestKeyboardAccess());
		while(controller.requestMouseAccess());
		
		new Thread( () -> location.travelPhase3()).start();
		while(!location.isPhase3Done()) {
			monitorMovement();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {e.printStackTrace();}
		}
		resetMonitor();
		
		new Thread( () -> location.travelPhase2()).start();
		while(!location.isPhase2Done()) {
			monitorMovement();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {e.printStackTrace();}
		}
		resetMonitor();
		
		new Thread( () -> location.travelPhase1()).start();
		while(!location.isPhase1Done()) {
			monitorMovement();
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
		
	}

	
	public void newLocation(LocationFactory.GameLocation location) {
		try {
			this.location = LocationFactory.newLocation(client, location);
		}
		catch(IllegalArgumentException e) {
			controller.nextModule();
		}
	}
	
	private void monitorMovement() {
		if(debugArea == null || debugArea.contains(client.getLocalPlayer())) {
			this.debugArea = client.getLocalPlayer().getTile().getArea(6);
			this.debugAreaCounter = 0;
		}
		else {
			debugAreaCounter++;
		}
		debugCounter++;
	}
	
	private void resetMonitor() {
		debugCounter = 0;
		debugAreaCounter = 0;
		debugArea = null;
	}

}
