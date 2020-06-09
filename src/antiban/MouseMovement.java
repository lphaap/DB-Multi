package antiban;

import java.awt.Point;

import client.ClientThread;
import client.KillableThread;
import client.PauseableThread;
import client.ThreadController;

public class MouseMovement implements KillableThread, PauseableThread {
	protected ClientThread client;
	protected ThreadController controller;
	protected boolean killThread;
	protected boolean pauseThread;
	
	public MouseMovement(ClientThread client, ThreadController controller) {
		this.client = client;
		this.controller = controller;
	}
	
	@Override
	public void run() {
		while(!killThread) {
			RandomProvider.sleep(5000, 5000);
			if(!pauseThread) {
				
				while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
				
				controller.debug("Mouese control: MouseMovement");
				
				controller.getGraphicHandler().setInfo("Random: Moving Mouse On Screen");
				client.getMouse().move(new Point(RandomProvider.randomInt(100)+100,RandomProvider.randomInt(100)+100));
				controller.returnMouseAccess();
			}
		}
		
	}

	@Override
	public void killThread() {
		this.killThread = true;
	}

	@Override
	public boolean isAlive() {
		return !(killThread);
		
	}
	
	@Override
	public void pauseThread() {
		this.pauseThread = true;
	}

	@Override
	public void resumeThread() {
		this.pauseThread = false;
	}

	@Override
	public boolean isPaused() {
		return this.pauseThread;
	}

}
