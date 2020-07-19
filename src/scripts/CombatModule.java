package scripts;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.wrappers.items.Item;

import antiban.RandomProvider;
import client.ClientThread;
import client.KillableThread;
import client.ThreadController;
import movement.Location;
import movement.LocationFactory;
import utilities.GroundItemHandler;
import utilities.GearHandler.Gear;


public class CombatModule extends ScriptModule {
	protected ThreadController controller;
	protected ClientThread script;

	protected ArrayList<String> collect = new ArrayList<String>();
	protected ArrayList<String> potions = new ArrayList<String>();
	
	protected KillableThread healingHandler = new HealingHandler();
	protected KillableThread antipoisonHandler = new AntiPoisonHandler();
	protected KillableThread potionHandler = new PotionHandler(true);
	protected KillableThread grounditemHandler;
	
	protected String monsterName;
	protected String food;
	
	protected int heal;
	protected int realHp;
	protected int delay;
	protected int eatAt;
	protected int limit;
	protected int timeLimit;
	protected int actionsCompleted;
	protected int playerLimit;
	protected int foodLimit;
	protected int potionCount;
	protected int drinkAt;
	
	protected double potionProcent;
	protected double potionBoost;

	protected final int unlimited = 3000;
	
	protected Training skill;
	protected Monster monsterEnum;
	protected Food foodEnum;
	protected Skill skillToTrain;
	protected Skill potionSkill;
	protected Potion potionEnum;
	protected LocationFactory.GameLocation locationEnum;
	
	protected boolean pickUp;
	protected boolean error;
	protected boolean killThread;
	protected boolean timeLimited;
	protected boolean usePotions;
	
	protected boolean test;
	
	//Leave timeLimitMins == 0, for unlimited time, 60mins timer ~ 70mins real time
	public CombatModule(ThreadController controller, ClientThread client, CombatModule.Monster monster, CombatModule.Food food, CombatModule.Potion potion, 
				 	 	int limit, int timeLimitMins, int foodLimit, int potionLimit, Boolean pickUp, Training skill) {
		this.script = client;
		eatAt = RandomProvider.randomInt(6) + 10;
		this.limit = limit;
		this.timeLimit = (timeLimitMins*60*1000);
		
		this.foodLimit = foodLimit;
		
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
		
		if(potion != Potion.NONE) {
			this.usePotions = true;
			this.setPotions(potion, potionLimit);
		}
		
		if(timeLimitMins > 0) {
			this.timeLimited = true;
		}
		
		ArrayList<String> drop = new ArrayList<String>();
		drop.add(this.food);
		this.grounditemHandler = new GroundItemHandler(controller, script, drop, this.pickUp);
		
		getSkillToHover();
		
		
	}
	
	@Override
	public void run() {
		
		threadloop: while(!killThread) {
			
			this.timeLimit = (this.timeLimit - delay);
			//controller.debug("" + timeLimit);
			if(timeLimit < 0 && timeLimited) {
				if(!this.controller.getWorldHandler().isBanking()) {
					this.controller.getWorldHandler().setToBanking();
				}
				
				controller.getGraphicHandler().setInfo("Combat trainer: Time limit reached - Banking");
				
				controller.getMovementHandler().moveToBank();
				
				RandomProvider.sleep(1500, 2000);
				this.killThread = true;
				break threadloop;
			}
			
			sleep(delay);
			delay = RandomProvider.randomInt(1000) + 2000;
		
			
			if(!script.getLocalPlayer().isAnimating() && !script.getLocalPlayer().isInCombat()) {
				/*if(!script.getInventory().isFull() && pickUp && controller.getMovementHandler().isPlayerInLocation()) {
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
				}*/
				
				if(!script.getInventory().contains(f -> f != null && f.getName().equals(food))) {
					
					if(!this.controller.getWorldHandler().isBanking()) {
						this.controller.getWorldHandler().setToBanking();
					}
					
					controller.getGraphicHandler().setInfo("Combat trainer: No Food Left - Banking");
					
					controller.getMovementHandler().moveToBank();
					
					sleep(RandomProvider.randomInt(750)+ 700);
					
					while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
					while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
					
					this.actionsCompleted++;
					
					controller.debug("Mouse control: CombatModule");
					controller.debug("Keyboard control: CombatModule");
					
					if(!script.getInventory().isEmpty()) {
						script.getBank().depositAllItems();
						sleep(RandomProvider.randomInt((750)+ 1000));
					}
					
					if(script.getBank().contains(food)) {
						if(this.foodLimit <= 0) {
							script.getBank().withdrawAll(f -> f != null && f.getName().equals(food));
						}
						else {
							script.getBank().withdraw(f -> f != null && f.getName().equals(food), this.foodLimit);
						}
						
						RandomProvider.sleep(400, 600);
						
						if(usePotions) {
							for(String p : potions) {
								if(script.getBank().contains((p+"4)"))) {
									script.getBank().withdraw(f -> f != null && f.getName().equals((p+"4)")), this.potionCount);
									RandomProvider.sleep(400, 600);
								}
							}
						}
						
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
						
						controller.getGraphicHandler().setInfo("Combat trainer: No Arrows left - Banking");
						
						controller.getMovementHandler().moveToBank();
						
						RandomProvider.sleep(1500, 2000);
						this.killThread = true;
						
						break threadloop;
						
					}
					if(script.getEquipment().getItemInSlot(EquipmentSlot.WEAPON.getSlot()).getName().toLowerCase().contains("bow")) {
						if(script.getEquipment().getItemInSlot(EquipmentSlot.ARROWS.getSlot()) == null) {
							controller.debug("CombatTrainer - Module ERROR");
							controller.debug("No Arrows Found - Changing Module");
							this.error = true;
							sleep(2000);
							
							controller.getGraphicHandler().setInfo("Combat trainer: No Arrows left - Banking");
							
							controller.getMovementHandler().moveToBank();
							
							RandomProvider.sleep(1500, 2000);
							this.killThread = true;
							
							break threadloop;
						}
						
					}
					
					controller.getGraphicHandler().setInfo("Combat trainer: Attacking target - "+ monsterEnum);
					
					while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
					
					NPC monster = script.getNpcs().closest(f -> f.getName().equals(monsterName) && f != null && !f.isInCombat() && f.getLevel()!=51);
					int randomizer = RandomProvider.randomInt(2);
					
					if(monster != null) {
						if(RandomProvider.fiftyfifty()) {
							script.getCamera().rotateToEntity(monster);
						}
						
						if(randomizer == 0) {
							monster.interact();
						}
						else {
							monster.interact("Attack");
						}
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
			
			controller.getMovementHandler().locateBank();
			
			while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
			while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
			
			controller.debug("Mouse control: CombatModule");
			controller.debug("Keyboard control: CombatModule");
			
			if(!script.getInventory().isEmpty()) {
				script.getBank().depositAllItems();
				sleep(RandomProvider.randomInt(750)+ 1000);
			}
			
			if(script.getBank().contains(food)) {
				if(this.foodLimit <= 0 && !this.usePotions) {
					script.getBank().withdrawAll(f -> f != null && f.getName().equals(food));
				}
				else {
					script.getBank().withdraw(f -> f != null && f.getName().equals(food), this.foodLimit);
				}
				RandomProvider.sleep(400, 600);
				
				if(usePotions) {
					for(String p : potions) {
						if(script.getBank().contains((p+"4)"))) {
							script.getBank().withdraw(f -> f != null && f.getName().equals((p+"4)")), this.potionCount);
							RandomProvider.sleep(400, 600);
						}
					}
				}
				
				sleep(RandomProvider.randomInt(750)+ 700);
				script.getBank().close();
				sleep(RandomProvider.randomInt(750)+ 400);
				script.getMouse().move();
				
				controller.returnKeyboardAccess();
				controller.returnMouseAccess();
				
				if(usePotions) {
					new Thread(potionHandler).start();
				}
				
				new Thread(healingHandler).start();
				new Thread(antipoisonHandler).start();
				new Thread(grounditemHandler).start();
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
		TROUT, SALMON, TUNA, LOBSTER, SWORD_FISH, MONK_FISH, SHARK, KARAMBWAN, MANTA_RAY, DARK_CRAB
	}
	
	public enum Potion	{
		NONE, STR, STR_ATT, STR_ATT_DEF, S_STR, S_STR_ATT, S_STR_ATT_DEF, RANGE, S_COMBAT
	}
	
	public void setMonsterVariables(Monster monster) {
		if(monster == Monster.GIANT_FROG) {
			this.monsterName = "Giant frog";
			this.collect.add("Big bones");
			this.locationEnum = LocationFactory.GameLocation.COMBAT_GIANT_FROG;
			this.playerLimit = 2;
		}
		else if(monster == Monster.BARBARIAN) {
			this.monsterName = "Barbarian";
			this.locationEnum = LocationFactory.GameLocation.COMBAT_BARBARIAN;
			this.playerLimit = this.unlimited;
			
		}
		else if(monster == Monster.EXPERIMENT) {
			this.monsterName = "Experiment";
			this.locationEnum = LocationFactory.GameLocation.COMBAT_EXPERIMENTS;
			this.playerLimit = 1;
		}
	}
	
	public void setFood(Food food) {
		
		switch(food) {
			case TROUT:
				this.food = "Trout";
				this.heal = 7;
				break;
			case SALMON:
				this.food = "Salmon";
				this.heal = 9;
				break;
			case TUNA:
				this.food = "Tuna";
				this.heal = 10;
				break;
			case LOBSTER:
				this.food = "Lobster";
				this.heal = 12;
				break;
			case SWORD_FISH:
				this.food = "Swordfish";
				this.heal = 14;
				break;
			case MONK_FISH:
				this.food = "Monkfish";
				this.heal = 16;
				break;
			case SHARK:
				this.food = "Shark";
				this.heal = 20;
				break;
			case KARAMBWAN:
				this.food = "Karambwan";
				this.heal = 18;
				break;
			case MANTA_RAY:
				this.food = "Manta ray";
				this.heal = 22;
				break;
			case DARK_CRAB:
				this.food = "Drak crab";
				this.heal = 22;
				break;
		}
			
		int realHp = script.getSkills().getRealLevel(Skill.HITPOINTS);
		eatAt = realHp - RandomProvider.randomInt((int)(realHp * 0.2323232323)) - (heal + 1);
	
	}
	
	public void setPotions(Potion potion, int count) {
		this.potionCount = count;
		this.potionEnum = potion;
		this.potionSkill = Skill.STRENGTH;
		
		switch(potion) {
			case NONE:
				return;
			case STR:
				this.potions.add("Strength potion(");
				this.potionBoost = 3;
				this.potionProcent = 0.10;
				countInventory((count));
				break;
			case STR_ATT:
				this.potions.add("Strength potion(");
				this.potions.add("Attack potion(");
				this.potionBoost = 3;
				this.potionProcent = 0.10;
				countInventory((count*2));
				break;
			case STR_ATT_DEF:
				this.potions.add("Strength potion(");
				this.potions.add("Attack potion(");
				this.potions.add("Defence potion(");
				this.potionBoost = 3;
				this.potionProcent = 0.10;
				countInventory((count*3));
				break;
			case S_STR:
				this.potions.add("Super strength(");
				this.potionBoost = 5;
				this.potionProcent = 0.15;
				countInventory((count));
				break;
			case S_STR_ATT:
				this.potions.add("Super strength(");
				this.potions.add("Super attack(");
				this.potionBoost = 5;
				this.potionProcent = 0.15;
				countInventory((count*2));
				break;
			case S_STR_ATT_DEF:
				this.potions.add("Super strength(");
				this.potions.add("Super attack(");
				this.potions.add("Super defence(");
				this.potionBoost = 5;
				this.potionProcent = 0.15;
				countInventory((count*3));
				break;
			case S_COMBAT:
				this.potions.add("Super combat potion(");
				countInventory((count));
				this.potionBoost = 5;
				this.potionProcent = 0.15;
				break;
			case RANGE:
				this.potions.add("Ranging potion(");
				countInventory((count));
				this.potionBoost = 4;
				this.potionProcent = 0.10;
				this.potionSkill = Skill.RANGED;
				break;
		}
		if(this.potions.size() > 1) {
			Collections.shuffle(potions);
		}
	}
	
	protected void countInventory(int potions) {
		if((this.foodLimit + potions) > 28) {
			this.foodLimit = (28-potions);
		}
	}


	@Override
	public void killThread() {
		this.killThread = true;
		this.healingHandler.killThread();
		this.potionHandler.killThread();
		this.antipoisonHandler.killThread();
		this.grounditemHandler.killThread();
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
	
	protected class HealingHandler implements KillableThread{
		protected boolean killThread;
		
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

						if(!script.getTabs().isOpen(Tab.INVENTORY)) {
							script.getTabs().open(Tab.INVENTORY);
							RandomProvider.sleep(100, 150);
						}	
						
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
	
	protected class PotionHandler implements KillableThread{
		protected boolean killThread;
		protected boolean useLocation;
		
		public PotionHandler(boolean useLocation) {
			this.useLocation = useLocation;
		}
		
		@Override
		public void run() {
			int boosted = ((int)((script.getSkills().getRealLevel(potionSkill)*potionProcent)+potionBoost));
			drinkAt = RandomProvider.randomInt((script.getSkills().getRealLevel(potionSkill)+(int)(boosted*0.25)),
											   (script.getSkills().getRealLevel(potionSkill)+(int)(boosted*0.50)));
			while(!killThread) {
				RandomProvider.sleep(1000, 1500);
				if(script.getSkills().getBoostedLevels(potionSkill) <= drinkAt && (!useLocation || controller.getMovementHandler().isPlayerInLocation()) && usePotions) {
					if(script.getInventory().contains(f -> f != null && f.getName().contains(potions.get(0)))) {
						controller.getGraphicHandler().setInfo("Combat trainer: Drinking Potion");
						while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
						
						controller.debug("Mouse control: Combat module using potions");

						if(!script.getTabs().isOpen(Tab.INVENTORY)) {
							script.getTabs().open(Tab.INVENTORY);
							RandomProvider.sleep(100, 150);
						}
						
						
						for(String p : potions) {
							if(script.getInventory().contains(f -> f != null && f.getName().contains(p))) {
								script.getInventory().get(f -> f != null && f.getName().contains(p)).interact("Drink");
								RandomProvider.sleep(3000, 3500);
							}
						}
						
						script.getMouse().move();
						
						drinkAt = RandomProvider.randomInt((script.getSkills().getRealLevel(potionSkill)+(int)(boosted*0.25)),
															(script.getSkills().getRealLevel(potionSkill)+(int)(boosted*0.50)));
						
						controller.returnMouseAccess();
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
	
	protected class	AntiPoisonHandler implements KillableThread{
		protected boolean killThread;
		
		@Override
		public void run() {
			
			while(!killThread) {
				RandomProvider.sleep(1000, 1500);
				if(script.getCombat().isPoisoned()) {
					controller.getGraphicHandler().setInfo("Combat trainer: Drinking Antipoison");
					if(script.getInventory().contains(f -> f != null && f.getName().contains("Superantipoison("))) {
						Thread.currentThread().setPriority(Thread.MAX_PRIORITY-1);
						while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
						
						controller.debug("Mouse control: Combat module Antipoison");

						if(!script.getTabs().isOpen(Tab.INVENTORY)) {
							script.getTabs().open(Tab.INVENTORY);
							RandomProvider.sleep(100, 150);
						}	
						
						Item eat = script.getInventory().get(f -> f != null && f.getName().contains("Superantipoison("));
						eat.interact();
						script.getMouse().move();
						
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
