package movement;

import org.dreambot.api.methods.map.Area;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;

import antiban.RandomProvider;
import client.ClientThread;

public class Obstacle {
	private String nameBefore;
	private String nameAfter;
	private String interactBefore;
	private String interactAfter;
	
	private Area beforeArea;
	private Area afterArea;
	
	private GameObject lastGO;
	private NPC lastNPC;
	
	private ClientThread script;
	
	public Obstacle(ClientThread script, String nameBefore, String interactBefore, 
				    String nameAfter, String interactAfter, Area areaBefore, Area areaAfter) {
		
		this.nameBefore = nameBefore;
		this.nameAfter = nameAfter;
		
		this.interactBefore = interactBefore;
		this.interactAfter = interactAfter;
		
		this.beforeArea = areaBefore;
		this.afterArea = areaAfter;
		
		this.script = script;
	}
	
	public Obstacle(ClientThread script, String nameBefore,  String interactBefore, Area areaBefore, Area areaAfter) {
		this.nameBefore = nameBefore;
		this.nameAfter = nameBefore;
		
		this.interactBefore = interactBefore;
		this.interactAfter = interactBefore;
		
		this.beforeArea = areaBefore;
		this.afterArea = areaAfter;
		
		this.script = script;
	}
	
	public boolean handleBeforeInteraction() {
		int failSafe = 0;
		
		script.sleepUntil(() -> this.afterArea.contains(script.getLocalPlayer()),
						 (RandomProvider.randomInt(4000,5000)));
		RandomProvider.sleep(100, 220);
		while(!interactBefore()) {
			script.getWalking().walk(this.beforeArea.getRandomTile());
			script.sleep(RandomProvider.randomInt(1000)+2000);
			failSafe++;
			if(failSafe > 5) {
				return false;
			}
			if(doorException()) {
				break;
			}
		}
		
		failSafe = 0;
		
		while(!afterObjectExists()) {
			RandomProvider.sleep(500,600);
			if(failSafe > 18) {
				return false;
			}
			failSafe++;
			if(doorException()) {
				break;
			}
		}
		
		return true;
	}
	
	public boolean handleAfterInteraction() {
		int failSafe = 0;
		
		script.sleepUntil(() -> this.afterArea.contains(script.getLocalPlayer()),
				 (RandomProvider.randomInt(4000,5000)));
		RandomProvider.sleep(100, 220);
		while(!interactAfter()) {
			script.getWalking().walk(this.afterArea.getRandomTile());
			script.sleep(RandomProvider.randomInt(1000)+2000);
			failSafe++;
			if(failSafe > 5) {
				return false;
			}
			if(doorException()) {
				break;
			}
		}
		
		failSafe = 0;
		
		while(!afterObjectExists()) {
			RandomProvider.sleep(500,600);
			if(failSafe > 18) {
				return false;
			}
			failSafe++;
			if(doorException()) {
				break;
			}
		}
		
		return true;
	}
	
	private boolean doorException() {
		if(nameBefore.equals(nameAfter) &&
			script.getGameObjects().closest(f -> f != null && f.getName().equals(nameBefore) && f.hasAction("Close") && f.getTile().getArea(4).contains(script.getLocalPlayer())).exists()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private boolean interactBefore() {
		GameObject obstacle = script.getGameObjects().closest(f -> f != null && f.getName().equals(nameBefore) && f.hasAction(interactBefore) 
															  && f.getTile().getArea(4).contains(script.getLocalPlayer()));
		if(obstacle != null) {
			this.lastGO = obstacle;
			if(RandomProvider.fiftyfifty()) {
				script.getCamera().rotateToEntity(obstacle);
				RandomProvider.sleep(100,150);
			}
			return obstacle.interact(interactBefore);
		}
		else {
			NPC npc = script.getNpcs().closest(f -> f != null && f.getName().equals(nameBefore)  && f.hasAction(interactBefore)
												&& f.getTile().getArea(4).contains(script.getLocalPlayer()));
			if(npc != null) {
				this.lastNPC = npc;
				if(RandomProvider.fiftyfifty()) {
					script.getCamera().rotateToEntity(npc);
					RandomProvider.sleep(100,150);
				}
				return npc.interact(interactBefore);
			}
			else {
				return true;
			}
		}
	}
	
	public boolean interactAfter() {
		GameObject obstacle = script.getGameObjects().closest(f -> f != null && f.getName().equals(nameAfter) && f.hasAction(interactAfter)
															  && f.getTile().getArea(4).contains(script.getLocalPlayer()));
		if(obstacle != null) {
			this.lastGO = obstacle;
			if(RandomProvider.fiftyfifty()) {
				script.getCamera().rotateToEntity(obstacle);
				RandomProvider.sleep(100,150);
			}
			return obstacle.interact(interactAfter);
		}
		else {
			NPC npc = script.getNpcs().closest(f -> f != null && f.getName().equals(nameAfter) && f.hasAction(interactAfter)
												&& f.getTile().getArea(4).contains(script.getLocalPlayer()));
			if(npc != null) {
				this.lastNPC = npc;
				if(RandomProvider.fiftyfifty()) {
					script.getCamera().rotateToEntity(npc);
					RandomProvider.sleep(100,150);
				}
				return npc.interact(interactAfter);
			}
			else {
				return true;
			}
		}
	}
	
	public boolean afterObjectExists() {
		GameObject obstacle = script.getGameObjects().closest(f -> f != null && (lastGO != null && (f.equals(lastGO))));
		if(obstacle != null) {
			return true;
		}
		else {
			NPC npc = script.getNpcs().closest(f -> f != null && f.getName().equals(nameAfter) && f.hasAction(interactAfter) && f.isOnScreen());
			if(npc != null) {
				return true;
			}
			else {
				return false;
			}
		}
	}
	
	public boolean beforeObjectExists() {
		GameObject obstacle = script.getGameObjects().closest(f -> f != null && (lastGO != null && (f.equals(lastGO))));
		if(obstacle != null) {
			return true;
		}
		else {
			NPC npc = script.getNpcs().closest(f -> f != null && (lastGO != null && (f.equals(lastNPC))));
			if(npc != null) {
				return true;
			}
			else {
				return false;
			}
		}
	}
	
	public Area getReturnLocation() {
		return this.afterArea;
	}
	
	public Area getGoingLocation() {
		return this.beforeArea;
	}
}
