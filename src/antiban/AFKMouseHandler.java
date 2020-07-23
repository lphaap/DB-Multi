package antiban;

import java.awt.Point;

import client.ClientThread;
import client.KillableHandler;
import client.PauseableThread;
import client.ThreadController;

//Not optimal for use too bot like
//WIP

public class AFKMouseHandler implements KillableHandler, PauseableThread{
	private ThreadController controller;
	private ClientThread client;
	
	private boolean killHandler;
	private boolean locationCheck;
	private boolean pauseHandler;
	
	private Point lastPosition;
	
	private int sleep;
	private int positionTimer;
	
	private final int unlimited = 100000000;
	
	public AFKMouseHandler(ThreadController controller, ClientThread client) {
		this.controller = controller;
		this.client = client;
	}
	
	public void start() {
		this.locationCheck = true;
		this.lastPosition = client.getMouse().getPosition();
		
		this.pauseThread();
		
		positionTimer = RandomProvider.randomInt(7500,10500);
		controller.debug("Mouse afk handler started");
		while(!killHandler) {
			sleep = RandomProvider.randomInt(10, 20);
			controller.sleep(sleep);
			
			if(!this.pauseHandler) {
				
				if(!client.getMouse().isMouseInScreen()) {
					this.positionTimer = unlimited;
					this.locationCheck = true;
				}
				
				if(lastPosition != null && ((lastPosition.getX() != client.getMouse().getPosition().getX()) || (lastPosition.getY() != client.getMouse().getPosition().getY()))) {
					this.locationCheck = true;
					this.lastPosition = client.getMouse().getPosition();
					controller.debug("Mouse moved: " + positionTimer);
				}
				
				if(locationCheck) {
					this.locationCheck = false;
					positionTimer = RandomProvider.randomInt(7500,10500);
				}
				
				positionTimer = (positionTimer - sleep);
				//controller.debug("" + positionTimer);
				
				if(positionTimer < 0) {
					controller.debug("Mouse afked");
					while(!controller.requestMouseAccess()) {RandomProvider.sleep(10);}
					if(RandomProvider.fiftyfifty()) {
						client.getMouse().move();
						RandomProvider.sleep(100,150);
						if(RandomProvider.fiftyfifty()) {
							client.getMouse().move();
							RandomProvider.sleep(100,150);
						}
					}
					client.getMouse().moveMouseOutsideScreen();
					RandomProvider.sleep(100,150);
					controller.returnMouseAccess();
				}
				
			}
		}
	}
	
	
	@Override
	public void killHandler() {
		this.killHandler = true;
		
	}

	@Override
	public void pauseThread() {
		this.pauseHandler = true;
	}

	@Override
	public void resumeThread() {
		this.pauseHandler = false;
	}

	@Override
	public boolean isPaused() {
		return this.pauseHandler;
	}

}
