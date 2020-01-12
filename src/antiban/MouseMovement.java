package antiban;

import java.awt.Point;

import client.ClientThread;
import client.KillableThread;
import client.ThreadController;

public class MouseMovement implements Runnable, KillableThread {
	protected ClientThread client;
	protected ThreadController controller;
	protected boolean killThread;
	
	public MouseMovement(ClientThread client, ThreadController controller) {
		this.client = client;
		this.controller = controller;
	}
	
	@Override
	public void run() {
		while(!killThread) {
			RandomProvider.sleep(5000, 5000);
			while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
			controller.getGraphicHandler().setInfo("Random: Moving Mouse On Screen");
			client.getMouse().move(new Point(RandomProvider.randomInt(100)+100,RandomProvider.randomInt(100)+100));
			controller.returnMouseAccess();
		}
		
	}

	@Override
	public void killThread() {
		this.killThread = true;
	}
	
	

}
