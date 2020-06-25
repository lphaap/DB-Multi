package scripts;
import java.util.ArrayList;
import java.util.Random;

import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.wrappers.items.Item;

import antiban.RandomProvider;
import client.ClientThread;
import client.KillableThread;
import client.ThreadController;
import movement.Location;
import movement.LocationFactory;
import utilities.GearHandler.Gear;


public class CombatModule extends ScriptModule {
	private ThreadController controller;
	private ClientThread script;

	private ArrayList<String> collect = new ArrayList<String>();
	
	private KillableThread healingHandler = new HealingHandler();
	
	private String monsterName;
	private String food;
	
	private int heal;
	private int realHp;
	private int delay;
	private int eatAt;
	private int limit;
	private int timeLimit;
	private int actionsCompleted;
	private int playerLimit;
	
	private Training skill;
	private Monster monsterEnum;
	private Food foodEnum;
	private Skill skillToTrain;
	private LocationFactory.GameLocation locationEnum;
	
	private boolean pickUp;
	private boolean error;
	private boolean killThread;
	
	private boolean test;
	
	//Leave timeLimitMins == 0, for unlimited time, 60mins timer ~ 70mins real time
	public CombatModule(ThreadController controller, ClientThread client, CombatModule.Monster monster, CombatModule.Food food, int limit, int timeLimitMins, Boolean pickUp, Training skill) {
		this.script = client;
		eatAt = RandomProvider.randomInt(6) + 10;
		this.limit = limit;
		this.timeLimit = (timeLimitMins*60*1000);
		controller.debug(""+timeLimit);
		this.controller = controller;
		setMonsterVariables(monster);
		setFood(food);
		this.pickUp = pickUp;
		this.monsterEnum = monster;
		this.foodEnum = food;
		this.error = false;
		this.skill = skill;
		this.moduleName = "CombatModule: " + monster;
		getSkillToHover();
		
		
	}
	
	@Override
	public void run() {
		
		threadloop: while(!killThread) {
			
			this.timeLimit = (this.timeLimit - delay);
			//controller.debug("" + timeLimit);
			if(timeLimit < 0) {
				this.killThread = true;
				break threadloop;
			}
			
			sleep(delay);
			delay = RandomProvider.randomInt(1000) + 2000;
		
			
			if(!script.getLocalPlayer().isAnimating() && !script.getLocalPlayer().isInCombat()) {
				if(!script.getInventory().isFull() && pickUp && controller.getMovementHandler().isPlayerInLocation()) {
					for(String item : collect) {
						GroundItem collectItem = script.getGroundItems().closest(f -> f != null && f.getName().equals(item));
						if(collectItem != null) {
							while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
							
							controller.debug("Mouse control: CombatModule");
							
							controller.getGraphicHandler().setInfo("Combat trainer: Picking up Item");
							collectItem.interact("Take");
							script.getMouse().move();
							this.delay -= 1000;
							controller.returnMouseAccess();
							continue threadloop;
						}
					}
				}
				
				if(!script.getInventory().contains(f -> f != null && f.getName().equals(food))) {
					
					if(!this.controller.getWorldHandler().isBanking()) {
						this.controller.getWorldHandler().setToBanking();
					}
					
					controller.getGraphicHandler().setInfo("Combat trainer: No Food Left - Banking");
					
					controller.getMovementHandler().moveToBank();
					
					this.actionsCompleted++;
					
					sleep(RandomProvider.randomInt(750)+ 700);
					
					while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
					while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
					
					controller.debug("Mouse control: CombatModule");
					controller.debug("Keyboard control: CombatModule");
					
					if(!script.getInventory().isEmpty()) {
						script.getBank().depositAllItems();
						sleep(RandomProvider.randomInt((750)+ 1000));
					}
					
					if(script.getBank().contains(food)) {
						script.getBank().withdrawAll(f -> f != null && f.getName().equals(food));
						sleep(RandomProvider.randomInt(750)+ 700);
						script.getBank().close();
						sleep(RandomProvider.randomInt(750)+ 400);
						script.getMouse().move();
						
					}
					else {
						this.error = true;
						sleep(2000);
						this.killThread();
					}
					
					controller.returnKeyboardAccess();
					controller.returnMouseAccess();
					
				}
				
				else if(!controller.getMovementHandler().isPlayerInLocation()) {
					if(!this.controller.getWorldHandler().isBanking()) {
						this.controller.getWorldHandler().setToBanking();
					}
					
					controller.getGraphicHandler().setInfo("Combat trainer: Moving to Location - " + locationEnum);
					controller.getMovementHandler().moveToLocation();
				}
				
				else if(!script.getLocalPlayer().isAnimating() && !script.getLocalPlayer().isInCombat()) {
					if(this.controller.getWorldHandler().isBanking()) {
						this.controller.getWorldHandler().setToUnBanking();
					}
					
					if(script.getEquipment().getItemInSlot(EquipmentSlot.WEAPON.getSlot()) == null) {
						controller.debug("CombatTrainer - Module ERROR");
						controller.debug("No Weapon Found - Changing Module");
						this.error = true;
						sleep(2000);
						this.killThread();
						
					}
					if(script.getEquipment().getItemInSlot(EquipmentSlot.WEAPON.getSlot()).getName().toLowerCase().contains("bow")) {
						if(script.getEquipment().getItemInSlot(EquipmentSlot.WEAPON.getSlot()) == null) {
							controller.debug("CombatTrainer - Module ERROR");
							controller.debug("No Arrows Found - Changing Module");
							this.error = true;
							sleep(2000);
							this.killThread();
						}
						
					}
					
					controller.getGraphicHandler().setInfo("Combat trainer: Attacking target - "+ monsterEnum);
					
					while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
					
					NPC monster = script.getNpcs().closest(f -> f.getName().equals(monsterName) && f != null && !f.isInCombat() && f.getLevel()!=51);
					int randomizer = RandomProvider.randomInt(2);
					if(randomizer == 0) {
						monster.interact();
					}
					else {
						monster.interact("Attack");
					}
					script.getMouse().move();
					
					controller.returnMouseAccess();
				}
				
			}
				
			  
			}
	}
	
	public enum Training {
		RANGE, ATTACK, STRENGTH
	}

	@Override
	public boolean setupModule() {
		
		this.controller.getMovementHandler().newLocation(this.locationEnum);
		
		this.controller.getMovementHandler().teleportToLocation();
		
		this.controller.getGearHandler().handleGearSwap(this.getGearToSwap());
		
		this.controller.getWorldHandler().setToActive();
		this.controller.getWorldHandler().setPlayerLimit(this.playerLimit);
		
		controller.getGraphicHandler().setInfo("Combat trainer: Setting up module");
		if(!script.getInventory().contains(f -> f != null && f.getName().equals(food))) {
			
			while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
			while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
			
			controller.debug("Mouse control: Combat module setup");
			controller.debug("Keyboard control: Combat module setup");
			
			if(!script.getWalking().isRunEnabled() && script.getWalking().getRunEnergy() > 0) {
				script.getWalking().toggleRun();
			}
	
			Area debugArea = null;
			int failsafe = 0;
			while(!script.getBank().isOpen()) {
				script.getBank().open(script.getBank().getClosestBankLocation());
				sleep(RandomProvider.randomInt(1000)+2000);
				if(debugArea == null || !debugArea.contains(script.getLocalPlayer())) {
					debugArea = script.getLocalPlayer().getTile().getArea(6);
					failsafe = 0;
				}
				else {
					failsafe++;
				}
				//TODO: Test if failsafe lvl is alright
				if(failsafe > 15) {
					controller.returnKeyboardAccess();
					controller.returnMouseAccess();
					return false;
				}
			}
			
			if(!script.getInventory().isEmpty()) {
				script.getBank().depositAllItems();
				sleep(RandomProvider.randomInt(750)+ 1000);
			}
			
			if(script.getBank().contains(food)) {
				script.getBank().withdrawAll(f -> f != null && f.getName().equals(food));
				sleep(RandomProvider.randomInt(750)+ 700);
				script.getBank().close();
				sleep(RandomProvider.randomInt(750)+ 400);
				script.getMouse().move();
				
				controller.returnKeyboardAccess();
				controller.returnMouseAccess();
				
				new Thread(healingHandler).start();
				return true;
			}
			else {
				controller.returnKeyboardAccess();
				controller.returnMouseAccess();
				return false;
			}

		}
		else {
			new Thread(healingHandler).start();
			return true;
		}
	}

	@Override
	public Skill getSkillToHover() {
		if(this.skill == Training.RANGE) {
			return Skill.RANGED;
		}
		else if(this.skill == Training.ATTACK){
			return Skill.ATTACK;
		}
		else if(this.skill == Training.STRENGTH){
			return Skill.STRENGTH;
		}
		else {
			return Skill.HITPOINTS;
		}
	}
	
	public Gear getGearToSwap() {
		if(this.skill == Training.RANGE) {
			return Gear.RANGE;
		}
		else {
			return Gear.MELEE;
		}
	}
	
	public enum Monster {
		GIANT_FROG, BARBARIAN, EXPERIMENT
	}
	
	public enum Food {
		TROUT, SALMON, TUNA
	}
	
	public void setMonsterVariables(Monster monster) {
		if(monster == Monster.GIANT_FROG) {
			this.monsterName = "Giant frog";
			this.collect.add("Big bones");
			this.locationEnum = LocationFactory.GameLocation.COMBAT_GIANT_FROG;
			this.playerLimit = 3;
		}
		else if(monster == Monster.BARBARIAN) {
			this.monsterName = "Barbarian";
			this.locationEnum = LocationFactory.GameLocation.COMBAT_BARBARIAN;
			this.playerLimit = 3000;
			
		}
		else if(monster == Monster.EXPERIMENT) {
			this.monsterName = "Experiment";
			this.locationEnum = LocationFactory.GameLocation.COMBAT_EXPERIMENTS;
			this.playerLimit = 1;
		}
	}
	
	public void setFood(Food food) {
		if(food == Food.TROUT) {
			this.food = "Trout";
			this.heal = 7;
		}
		else if(food == Food.SALMON){
			this.food = "Salmon";
			this.heal = 9;
		}
		else if(food == Food.TUNA){
			this.food = "Tuna";
			this.heal = 10;
		}
		
		int realHp = script.getSkills().getRealLevel(Skill.HITPOINTS);
		eatAt = realHp - RandomProvider.randomInt((int)(realHp * 0.2323232323)) - (heal + 1);
		

	}


	@Override
	public void killThread() {
		this.killThread = true;
		this.healingHandler.killThread();
	}

	@Override
	public boolean isAlive() {
		return !this.killThread;
	}

	@Override
	public boolean isReady() {
		if(this.error) {
			return true;
		}
		else if(this.limit <= this.actionsCompleted) {
			return true;
		}
		else { return  false; }
	
	}
	
	private class HealingHandler implements KillableThread{
		private boolean killThread;
		
		@Override
		public void run() {
			
			while(!killThread) {
				RandomProvider.sleep(1000, 1500);
				if(script.getSkills().getBoostedLevels(Skill.HITPOINTS) <= eatAt) {
					controller.getGraphicHandler().setInfo("Combat trainer: Eating - " + foodEnum);
					if(script.getInventory().contains(f -> f != null && f.getName().equals(food))) {
						Thread.currentThread().setPriority(Thread.MAX_PRIORITY-1);
						while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
						
						controller.debug("Mouse control: Combat module healing");
						
						Item eat = script.getInventory().get(f -> f != null && f.getName().equals(food));
						eat.interact();
						script.getMouse().move();
						int realHp = script.getSkills().getRealLevel(Skill.HITPOINTS);
						eatAt = realHp - RandomProvider.randomInt((int)(realHp * 0.2323232323)) - (heal + 1);
						controller.returnMouseAccess();
						Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
								//random.nextInt(6) + 10;
					}
				}
			}			
			
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



}
