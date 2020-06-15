package antiban;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.tabs.Tab;

import client.ClientThread;
import client.KillableThread;
import client.PauseableThread;
import client.ThreadController;

public class StatsHovering implements KillableThread, PauseableThread {
	protected ClientThread client;
	protected ThreadController controller;
	protected boolean killThread;
	protected boolean pauseThread;
	
	public StatsHovering(ClientThread client, ThreadController controller) {
		this.client = client;
		this.controller = controller;
	}
	
	@Override
	public void run() {
		while(!killThread) {
			RandomProvider.sleep(120000, 180000);
			//RandomProvider.sleep(1000, 2000); 
			if(killThread) {
				break;
			}
			
			if(!pauseThread) {
				
				while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
				while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
				
				controller.debug("Keyboard control: StatsHovering");
				controller.debug("Mouse control: StatsHovering");
					
				controller.getGraphicHandler().setInfo("Random: Hovering XP");
				client.getTabs().open(Tab.STATS);
				
				if(RandomProvider.fiftyfifty()) {
					client.getSkills().hoverSkill(controller.getCurrentSkill());
				}
				else {
					client.getSkills().hoverSkill(Skill.values()[RandomProvider.randomInt(Skill.values().length)]);
				}
				
				RandomProvider.sleep(2000, 3000);
				
				if(RandomProvider.fiftyfifty()) {
					client.getSkills().hoverSkill(Skill.values()[RandomProvider.randomInt(Skill.values().length)]);
					RandomProvider.sleep(1500, 2500);
				}
				
				client.getTabs().open(Tab.INVENTORY);
				client.getMouse().move();
				
				RandomProvider.sleep(1200, 1400);
				
				controller.returnKeyboardAccess();
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
