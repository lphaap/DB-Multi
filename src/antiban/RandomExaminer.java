package antiban;

import java.awt.Point;
import java.util.List;

import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;

import client.ClientThread;
import client.KillableThread;
import client.PauseableThread;
import client.ThreadController;

public class RandomExaminer implements KillableThread, PauseableThread {
	protected ClientThread client;
	protected ThreadController controller;
	
	private boolean killThread;
	private boolean pauseThread;
	
	private NPC lastNPC;
	private GameObject lastObject;
	
	public RandomExaminer(ClientThread client, ThreadController controller) {
		this.client = client;
		this.controller = controller;
	}
	
	@Override
	public void run() {
		while(!killThread) {
			RandomProvider.sleep(50000, 70000);
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
				
				controller.debug("Mouse control: EntityExaminer");
				
				controller.getGraphicHandler().setInfo("Random: Examining random target");
				
				if(RandomProvider.fiftyfifty()) {
					
					if(!npcExamine()) {
						
						objectExamine();
					}
				}
				else {
					
					if(!objectExamine()) {
						
						npcExamine();
					}
				}
				RandomProvider.sleep(800, 1000);
				controller.returnMouseAccess();
			}
		}
		
	}
	
	
	private boolean npcExamine() {
		
		List<NPC> found = client.getNpcs().all(f -> f != null && f.isOnScreen() && 
											  (lastNPC == null || !f.equals(lastNPC)));
		this.lastNPC = found.get(RandomProvider.randomInt(found.size()));
		
		if(this.lastNPC == null) {
			return false;
		}
		else {
			if(RandomProvider.fiftyfifty()) {
				controller.debug("NPC: 1");
				client.getCamera().rotateToEntity(lastNPC);
				RandomProvider.sleep(100, 200);
				client.getMouse().click(lastObject, true);
				RandomProvider.sleep(600, 1000);
				
				//client.getMouse().move();
			}
			else {
				controller.debug("NPC: 2");
				client.getCamera().rotateToEntity(lastNPC);
				RandomProvider.sleep(100, 200);
				this.lastNPC.interactForceRight("Examine");
				
			}
			RandomProvider.sleep(1200, 1400);
			client.getMouse().move();
			return true;
		}
	}
	
	private boolean objectExamine() {

		List<GameObject> found = client.getGameObjects().all(f -> f != null && f.isOnScreen() && 
															(lastObject == null || !f.equals(lastObject)));
		this.lastObject = found.get(RandomProvider.randomInt(found.size()));
		
		if(this.lastObject == null) {
			return false;
		}
		else {
			if(RandomProvider.fiftyfifty()) {
				controller.debug("Object: 1");
				client.getCamera().rotateToEntity(lastObject);
				RandomProvider.sleep(100, 200);
				client.getMouse().click(lastObject, true);
				RandomProvider.sleep(600, 1000);
				//client.getMouse().move();

			}
			else {
				controller.debug("Object: 2");
				client.getCamera().rotateToEntity(lastObject);
				RandomProvider.sleep(100, 200);
				this.lastObject.interactForceRight("Examine");
			}
			RandomProvider.sleep(1200, 1400);
			client.getMouse().move();
			return true;
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
