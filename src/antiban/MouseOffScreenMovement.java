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
			
			if(killThread) {
				break;
			}
			
			if(!pauseThread) {

				while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
				
				controller.debug("Mouse control: MouseOffScreen");
				
				controller.getGraphicHandler().setInfo("Random: Mouse outside of the screen");
				client.getMouse().moveMouseOutsideScreen();
				RandomProvider.sleep(10000, 13000);
				controller.returnMouseAccess();
			}
		}
		
	}
}
