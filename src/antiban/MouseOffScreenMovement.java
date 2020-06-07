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
			RandomProvider.sleep(5000, 5000);
			if(!pauseThread) {

				while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
				controller.getGraphicHandler().setInfo("Random: Mouse outside of the screen");
				client.getMouse().moveMouseOutsideScreen();
				controller.returnMouseAccess();
			}
		}
		
	}
}
