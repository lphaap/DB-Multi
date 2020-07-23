package scripts;

import java.util.ArrayList;

import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.GroundItem;

import antiban.RandomProvider;
import client.ClientThread;
import client.ThreadController;
import utilities.GroundItemHandler;

public class ManualCombatModule extends CombatModule {
	private String monsterName;
	
	private int hopLimit;
	
	private int slayerXP;

	
	public ManualCombatModule(ThreadController controller, ClientThread client, Food food,
						   	Potion potion, int limit, int hoplimit, Boolean pickUp, Boolean trainSlayer,Training skill,
							String monsterName) {
		
		super(controller, client, Monster.BARBARIAN, food, potion, limit, 0, 0, 0, pickUp, skill);
		this.monsterName = monsterName;
		this.hopLimit = hoplimit;

		this.trainSlayer = trainSlayer;
		
		this.potionHandler = new PotionHandler(false);
		
	}
	
	@Override
	public void run() {
		
		threadloop: while(!killThread) {
			//controller.debug("CModule looping");
			sleep(delay);
			delay = RandomProvider.randomInt(1000) + 2000;
		
			if(script.getSkills().getExperience(Skill.SLAYER) > this.slayerXP && this.trainSlayer) {
				this.actionsCompleted++;
				this.slayerXP = script.getSkills().getExperience(Skill.SLAYER);
				controller.debug("Kill achieved");
			}
			
			if(!script.getLocalPlayer().isAnimating() && !script.getLocalPlayer().isInCombat()) {
				
				RandomProvider.sleep(800, 1200);
				if(!script.getInventory().contains(f -> f != null && f.getName().equals(food))) {
					this.killThread = true;
					break threadloop;
				}
				else if(this.actionsCompleted > this.limit) {
					this.killThread = true;
					break threadloop;
				}
				else if(this.skill == Training.RANGE && script.getEquipment().getItemInSlot(EquipmentSlot.ARROWS.getSlot()) == null) {
					this.killThread = true;
					break threadloop;
				}
				else if(!script.getLocalPlayer().isAnimating() && !script.getLocalPlayer().isInCombat()) {

					
					controller.getGraphicHandler().setInfo("Combat trainer: Attacking target - "+ monsterName);
					
					while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
					
					NPC monster = script.getNpcs().closest(f -> f.getName().equals(monsterName) && f != null && !f.isInCombat());
					org.dreambot.api.wrappers.interactive.Character existingEnemy = script.getLocalPlayer().getInteractingCharacter();
					
					int randomizer = RandomProvider.randomInt(2);
					
					if(existingEnemy != null && existingEnemy.getName().equals(monsterName)) {
						if(RandomProvider.fiftyfifty()) {
							script.getCamera().rotateToEntity(existingEnemy);
						}
						if(randomizer == 0) {
							existingEnemy.interact();
						}
						else {
							existingEnemy.interact("Attack");
						}
						if(!this.trainSlayer) {
							this.actionsCompleted++;
						}
					}
					else if(monster != null) {
						if(RandomProvider.fiftyfifty()) {
							script.getCamera().rotateToEntity(monster);
						}
						if(randomizer == 0) {
							monster.interact();
						}
						else {
							monster.interact("Attack");
						}
						if(!this.trainSlayer) {
							this.actionsCompleted++;
						}
					}
					script.getMouse().move();
					
					controller.returnMouseAccess();
				}
				
			}
				
		}
	}
	
	@Override
	public boolean setupModule() {
		if(usePotions) {
			new Thread(potionHandler).start();
		}
		new Thread(healingHandler).start();
		new Thread(antipoisonHandler).start();
		new Thread(grounditemHandler).start();
		
		this.moduleName = "CombatModule: " + monsterName;
		
		this.controller.getWorldHandler().setPlayerLimit(hopLimit);
		this.controller.getWorldHandler().setToActive();
		this.controller.getWorldHandler().setToUnBanking();
		
		this.slayerXP = script.getSkills().getExperience(Skill.SLAYER);
		return true;
	}
	
	@Override
	public void killThread() {
		//controller.debug("Killing Cmodule");
		this.killThread = true;
		if(healingHandler != null) {
			this.healingHandler.killThread();
		}
		if(potionHandler != null) {
			this.potionHandler.killThread();
		}
		if(this.antipoisonHandler != null) {
			this.antipoisonHandler.killThread();
		}
		if(this.grounditemHandler != null) {
			//controller.debug("Killing GIH");
			this.grounditemHandler.killThread();
		}
		//controller.debug("CMODULE KILLED");
	}
}
