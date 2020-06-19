package antiban;

import java.awt.Point;

import client.ClientThread;
import client.ThreadController;

public class MouseOffScreenMovement extends MouseMovement {
	
	public MouseOffScreenMovement(ClientThread client, ThreadController controller) {
		super(client, controller);
	}

	@Override
	public void run() {
		while(!killThread) {
			RandomProvider.sleep(100000, 180000);
			//RandomProvider.sleep(1000, 2000);
			if(killThread) {
				break;
			}
			
			if(!pauseThread) {

				while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
				
				if(killThread) {
					controller.returnMouseAccess();
					break;
				}
				
				controller.debug("Mouse control: MouseOffScreen");
				
				controller.getGraphicHandler().setInfo("Random: Mouse outside of the screen");
				client.getMouse().moveMouseOutsideScreen();
				RandomProvider.sleep(10000, 13000);
				controller.returnMouseAccess();
			}
		}
		
	}
}
