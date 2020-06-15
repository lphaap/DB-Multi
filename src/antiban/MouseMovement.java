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
			RandomProvider.sleep(40000, 70000);
			//RandomProvider.sleep(1000,2000);
			if(killThread) {
				break;
			}
			
			if(!pauseThread) {
				
				while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
				
				controller.debug("Mouese control: MouseMovement");
				
				controller.getGraphicHandler().setInfo("Random: Moving Mouse On Screen");
				
				int repeat = RandomProvider.randomInt(1,4);
				controller.debug(""+repeat);
				
				for(int i = 0; i < repeat; i++) {
					client.getMouse().move(new Point(RandomProvider.randomInt(RandomProvider.randomInt(40, 100),RandomProvider.randomInt(700, 750)),
													 RandomProvider.randomInt(RandomProvider.randomInt(40, 100),RandomProvider.randomInt(440, 490))));
					RandomProvider.sleep(200,400);
				}

				RandomProvider.sleep(1200, 1400);
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
