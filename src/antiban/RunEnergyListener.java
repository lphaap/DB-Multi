package antiban;

import client.ClientThread;
import client.KillableThread;
import client.PauseableThread;
import client.ThreadController;

public class RunEnergyListener implements KillableThread, PauseableThread {
	private boolean killThread;
	private boolean pauseThread;
	
	private ClientThread client;
	private ThreadController controller;
	
	int limit = RandomProvider.randomInt(RandomProvider.randomInt(5, 11), RandomProvider.randomInt(22, 35));
	
	public RunEnergyListener(ThreadController controller, ClientThread client) {
		this.controller = controller;
		this.client = client;
	}
	
	@Override
	public void run() {
		while(!killThread) {
			RandomProvider.sleep(1000, 2000);
			if(!pauseThread) {
				if(client.getWalking().getRunEnergy() > limit && !client.getWalking().isRunEnabled()) {
					while(controller.requestMouseAccess()) {RandomProvider.sleep(10);};
					RandomProvider.sleep(200,350);
					client.getWalking().toggleRun();
					RandomProvider.sleep(150,250);
					client.getMouse().move();
					RandomProvider.sleep(150,250);
					
					controller.returnMouseAccess();
					limit = RandomProvider.randomInt(RandomProvider.randomInt(5, 11), RandomProvider.randomInt(22, 35));
				}
			}
		}

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

	@Override
	public void killThread() {
		this.killThread = true;
	}

	@Override
	public boolean isAlive() {
		return !this.killThread;
	}

}
