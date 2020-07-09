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
	private boolean inControll;
	
	public MovementHandler(ClientThread client, ThreadController controller) {
		this.client = client;
		this.controller = controller;
	}
	
	/**
	 * Handles own input accsess
	 * @throws IllegalArgumentException
	 */
	public void moveToLocation() {
		
		
		while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);};
		while(controller.requestMouseAccess()) {RandomProvider.sleep(10);};
		
		RandomProvider.sleep(600, 1000);
		
		controller.debug("Mouse control: MovementHandler");
		controller.debug("Keyboard control: MovementHandler");
		
		this.inControll = true;
		
		new Thread(() -> {location.travelToLocation();}).start();
		RandomProvider.sleep(1000, 1500);
		while(location.isMovementInProgress()) {
			RandomProvider.sleep(900, 1100);
			int indexInUse = location.getIndexOfMovement();
			controller.debug("index: " +indexInUse);
			if(monitorMovement()) {
				return;
			}
			controller.debug("" + new Tile(2991, 3341, 1).getArea(1).contains(client.getLocalPlayer()));
			
			if(indexInUse != location.getIndexOfMovement()) {
				indexInUse = location.getIndexOfMovement();
				this.resetMonitor();
			}
			
		}
		this.resetMonitor();
		
		controller.debug("MOVEMENT HANDLER RETURNED ACCESS");
		controller.debug("MOVEMENT HANDLER RETURNED ACCESS");
		
		this.inControll = false;
		controller.returnKeyboardAccess();
		controller.returnMouseAccess();
		
	}
	
	/**
	 * Handles own input accsess
	 * @throws IllegalArgumentException
	 */
	public void moveToBank() {
		
		
		while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);};
		while(controller.requestMouseAccess()) {RandomProvider.sleep(10);};
		
		RandomProvider.sleep(600, 1000);
		
		controller.debug("Mouse control: MovementHandler");
		controller.debug("Keyboard control: MovementHandler");
		
		this.inControll = true;
	
		new Thread(() -> {location.travelToBank();}).start();
		RandomProvider.sleep(1000, 1500);
		while(location.isMovementInProgress()) {
			
			RandomProvider.sleep(900, 1100);
			int indexInUse = location.getIndexOfMovement();
			controller.debug("index: "+ indexInUse);
			if(monitorMovement()) {
				return;
			}
			
			if(indexInUse != location.getIndexOfMovement()) {
				indexInUse = location.getIndexOfMovement();
				this.resetMonitor();
			}
			
		}
		this.resetMonitor();
		

		
		//controller.debug("MOVEMENT HANDLER RETURNED ACCESS");
	//	controller.debug("MOVEMENT HANDLER RETURNED ACCESS");
		
		this.inControll = false;
		controller.returnKeyboardAccess();
		controller.returnMouseAccess();
	}
	
	public void teleportToLocation() {
		while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);};
		while(controller.requestMouseAccess()) {RandomProvider.sleep(10);};
		
		RandomProvider.sleep(600, 1000);
		
		controller.debug("Mouse control: MovementHandler");
		controller.debug("Keyboard control: MovementHandler");
		
		this.inControll = true;
		
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
		
		RandomProvider.sleep(1000, 1500);
		
		//controller.debug("MOVEMENT HANDLER RETURNED ACCESS");
	//	controller.debug("MOVEMENT HANDLER RETURNED ACCESS");
		
		this.inControll = false;
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
			if(this.location != null) {
				this.location.killCurrentAction();
			}
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
	
	public Area getMainLocation() {
		return this.location.getMainArea();
	}

	@Override
	public void killHandler() {
		this.killHandler = true;
	}
	
	//Does this handler have mouse/key control
	public boolean isInControl() {
		if(location != null && location.isTeleportInProgress()) {
			return true;
		}
		else if(this.inControll) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void locateBank() {
		while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);};
		while(controller.requestMouseAccess()) {RandomProvider.sleep(10);};
		
		RandomProvider.sleep(600, 1000);
		
		this.inControll = true;
		
		controller.debug("Mouse control: MovementHandler");
		controller.debug("Keyboard control: MovementHandler");
		
			int runEnergyTest = RandomProvider.randomInt(10) + 1;
			client.sleep(RandomProvider.randomInt(1000)+2000);
			while(!client.getBank().isOpen() && !this.monitorMovement()) {
				client.getBank().open(client.getBank().getClosestBankLocation());
				client.sleep(RandomProvider.randomInt(1000)+2000);
				
				if(client.getWalking().getRunEnergy() >= runEnergyTest && !client.getWalking().isRunEnabled()) {
					client.getWalking().toggleRun();
					client.sleep(RandomProvider.randomInt(1000)+2000);
					runEnergyTest = RandomProvider.randomInt(10) + 1;
				}
				
			}
			
		this.controller.returnKeyboardAccess();
		this.controller.returnMouseAccess();
		this.inControll = false;
		this.resetMonitor();

		
	}

	
}
