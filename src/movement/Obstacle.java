package movement;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;

import client.ClientThread;

public class Obstacle {
	private String nameBefore;
	private String nameAfter;
	private String interactBefore;
	private String interactAfter;
	private ClientThread script;
	
	public Obstacle(ClientThread script, String nameBefore, String nameAfter, String interactBefore, String interactAfter) {
		this.nameBefore = nameBefore;
		this.nameAfter = nameAfter;
		this.interactBefore = interactBefore;
		this.interactAfter = interactAfter;
		this.script = script;
	}
	
	public Obstacle(ClientThread script, String nameBefore,  String interactBefore) {
		this.nameBefore = nameBefore;
		this.nameAfter = nameBefore;
		this.interactBefore = interactBefore;
		this.interactAfter = interactBefore;
		this.script = script;
	}
	
	public boolean interactBefore() {
		GameObject obstacle = script.getGameObjects().closest(f -> f != null && f.getName().equals(nameBefore) && f.hasAction(interactBefore));
		if(obstacle != null) {
			return obstacle.interact(interactBefore);
		}
		else {
			NPC npc = script.getNpcs().closest(f -> f != null && f.getName().equals(nameBefore)  && f.hasAction(interactBefore));
			if(npc != null) {
				return npc.interact(interactBefore);
			}
			else {
				return true;
			}
		}
	}
	
	public boolean interactAfter() {
		GameObject obstacle = script.getGameObjects().closest(f -> f != null && f.getName().equals(nameAfter) && f.hasAction(interactAfter));
		if(obstacle != null) {
			return obstacle.interact(interactAfter);
		}
		else {
			NPC npc = script.getNpcs().closest(f -> f != null && f.getName().equals(nameAfter) && f.hasAction(interactAfter));
			if(npc != null) {
				return npc.interact(interactAfter);
			}
			else {
				return true;
			}
		}
	}
	
	public boolean afterObjectExists() {
		GameObject obstacle = script.getGameObjects().closest(f -> f != null && f.getName().equals(nameAfter) && f.hasAction(interactAfter) && f.isOnScreen());
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
		GameObject obstacle = script.getGameObjects().closest(f -> f != null && f.getName().equals(nameBefore) && f.hasAction(interactBefore) && f.isOnScreen());
		if(obstacle != null) {
			return true;
		}
		else {
			NPC npc = script.getNpcs().closest(f -> f != null && f.getName().equals(nameBefore) && f.hasAction(interactBefore) && f.isOnScreen());
			if(npc != null) {
				return true;
			}
			else {
				return false;
			}
		}
	}
	
	
		
	
	
}
