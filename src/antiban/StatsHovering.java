package antiban;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.tabs.Tab;

import init.ClientThread;
import init.KillableThread;
import init.ThreadController;

public class StatsHovering implements KillableThread, Runnable {
	protected ClientThread client;
	protected ThreadController controller;
	protected boolean killThread;
	
	public StatsHovering(ClientThread client, ThreadController controller) {
		this.client = client;
		this.controller = controller;
	}
	
	@Override
	public void run() {
		while(!killThread) {
			RandomProvider.sleep(5000, 5000);
			
			while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
			while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
				

			client.getTabs().open(Tab.STATS);
			client.getSkills().hoverSkill(controller.getCurrentTask().getSkillToHover());
			RandomProvider.sleep(2000, 3000);
			client.getTabs().open(Tab.INVENTORY);
			client.getMouse().move();
			
			controller.returnKeyboardAccess();
			controller.returnMouseAccess();
				
				
			
		}
		
	}

	@Override
	public void killThread() {
		this.killThread = true;
	}
}
