package antiban;

import java.awt.Point;

import client.ClientThread;
import client.KillableThread;
import client.PauseableThread;
import client.ThreadController;

public class RandomExaminer implements KillableThread, PauseableThread {
	protected ClientThread client;
	protected ThreadController controller;
	private boolean killThread;
	private boolean pauseThread;
	
	public RandomExaminer(ClientThread client, ThreadController controller) {
		this.client = client;
		this.controller = controller;
	}
	
	@Override
	public void run() {
		while(!killThread) {
			RandomProvider.sleep(5000, 5000);
			if(!pauseThread) {
				
				//TODO:
				while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
				
				controller.debug("Mouse control: EntityExaminer");
				
				controller.getGraphicHandler().setInfo("Random: Examining random target");
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
