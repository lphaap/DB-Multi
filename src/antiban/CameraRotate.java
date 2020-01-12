package antiban;

import java.awt.Point;

import init.ClientThread;
import init.KillableThread;
import init.ThreadController;

public class CameraRotate implements Runnable, KillableThread {
	protected ClientThread client;
	protected ThreadController controller;
	protected boolean killThread;
	
	public CameraRotate(ClientThread client, ThreadController controller) {
		this.client = client;
		this.controller = controller;
	}
	
	@Override
	public void run() {
		while(!killThread) {
			RandomProvider.sleep(5000, 5000);
			int random = RandomProvider.randomInt(2);
			if(random == 0) {
				while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
				controller.getGraphicHandler().setInfo("Random: Rotating Camera");
				client.getCamera().keyboardRotateToTile(client.getLocalPlayer().getTile().getArea(6).getRandomTile());
				controller.returnKeyboardAccess();
			}
			else{
				while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
				controller.getGraphicHandler().setInfo("Random: Rotating Camera");
				client.getCamera().mouseRotateToTile(client.getLocalPlayer().getTile().getArea(6).getRandomTile());
				controller.returnMouseAccess();
			}
		}
		
	}

	@Override
	public void killThread() {
		this.killThread = true;
	}

}
