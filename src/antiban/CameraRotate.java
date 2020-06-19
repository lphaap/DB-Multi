package antiban;

import java.awt.Point;

import client.ClientThread;
import client.KillableThread;
import client.PauseableThread;
import client.ThreadController;

public class CameraRotate implements KillableThread, PauseableThread {
	protected ClientThread client;
	protected ThreadController controller;
	protected boolean killThread;
	protected boolean pauseThread;
	
	public CameraRotate(ClientThread client, ThreadController controller) {
		this.client = client;
		this.controller = controller;
	}
	
	@Override
	public void run() {
		while(!killThread) {
			//controller.debug("Camera sleeping");
			RandomProvider.sleep(30000, 45000);
			//RandomProvider.sleep(1000,2000);
			if(killThread) {
				break;
			}

			if(!pauseThread){
				int random = RandomProvider.randomInt(2);
				if(random == 0) {
					while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
					
					if(killThread) {
						controller.returnKeyboardAccess();
						break;
					}
					
					controller.debug("Keyboard control: CameraRotate");
					
					controller.getGraphicHandler().setInfo("Random: Rotating Camera");
					client.getCamera().keyboardRotateToTile(client.getLocalPlayer().getTile().getArea(6).getRandomTile());
					RandomProvider.sleep(1200, 1400);
					controller.returnKeyboardAccess();
				}
				else{
					while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
					
					if(killThread) {
						controller.returnMouseAccess();
						break;
					}
					
					controller.debug("Mouse control: CameraRotate");
					
					controller.getGraphicHandler().setInfo("Random: Rotating Camera");
					client.getCamera().mouseRotateToTile(client.getLocalPlayer().getTile().getArea(6).getRandomTile());
					RandomProvider.sleep(1200, 1400);
					controller.returnMouseAccess();
					
				}
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
