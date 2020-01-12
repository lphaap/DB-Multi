package antiban;

import java.awt.Point;

import init.ClientThread;
import init.ThreadController;

public class MouseOffScreenMovement extends MouseMovement {
	
	public MouseOffScreenMovement(ClientThread client, ThreadController controller) {
		super(client, controller);
	}

	@Override
	public void run() {
		while(!killThread) {
			RandomProvider.sleep(5000, 5000);
			while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
			client.getMouse().moveMouseOutsideScreen();
			controller.returnMouseAccess();
		}
		
	}
}
