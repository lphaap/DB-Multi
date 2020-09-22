package scripts;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.prayer.Prayer;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.widget.Widget;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.api.wrappers.widgets.WidgetChild;

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
	
	protected final int unlimited = 3000;
	protected PrayerHandler prayerHandlerRef = new PrayerHandler();
	
	protected KillableThread healingHandler = new HealingHandler();
	protected KillableThread antiPotionHandler = new AntiPotionHandler();
	protected KillableThread potionHandler = new PotionHandler(true);
	protected KillableThread specHandler = new SpecialAttackHandler();
	protected KillableThread prayerHandler = prayerHandlerRef;
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
	protected boolean usePrayer;
	protected boolean trainSlayer;
	protected boolean test;
	
	
	//Leave timeLimitMins == 0, for unlimited time, 60mins timer ~ 70mins real time
	public CombatModule(ThreadController controller, ClientThread client, CombatModule.Monster monster, CombatModule.Food food, CombatModule.Potion potion, 
				 	 	int limit, int timeLimitMins, int foodLimit, int potionLimit, Boolean pickUp, boolean usePrayer, Training skill) {
		this.script = client;
		eatAt = RandomProvider.randomInt(6) + 10;
		this.limit = limit;
		this.timeLimit = (timeLimitMins*60*1000);
		
		this.foodLimit = foodLimit;
		this.usePrayer = usePrayer;
		this.trainSlayer = trainSlayer;
		
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
					org.dreambot.api.wrappers.interactive.Character existingEnemy = script.getLocalPlayer().getCharacterInteractingWithMe();
					
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
					}
					script.getMouse().move();
					
					controller.returnMouseAccess();
				}
				
			}
				
			  
			}
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
				if(usePrayer) {
					new Thread(prayerHandler).start();
				}
				new Thread(healingHandler).start();
				new Thread(antiPotionHandler).start();
				new Thread(grounditemHandler).start();
				new Thread(specHandler).start();
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

	
	public enum Monster {
		GIANT_FROG, BARBARIAN, EXPERIMENT
	}
	
	public enum Food {
		TROUT, SALMON, TUNA, LOBSTER, SWORD_FISH, BASS, CHEESE_POTATO, MONK_FISH, SHARK, KARAMBWAN, MANTA_RAY, DARK_CRAB
	}
	
	public enum Potion	{
		NONE, STR, STR_ATT, STR_ATT_DEF, S_STR, S_STR_ATT, S_STR_ATT_DEF, RANGE, S_COMBAT
	}
	
	public enum Training {
		STRENGTH, ATTACK, DEFENCE, RANGE
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
		else if(this.skill == Training.DEFENCE){
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
			case BASS:
				this.food = "Bass";
				this.heal = 13;
				break;
			case SWORD_FISH:
				this.food = "Swordfish";
				this.heal = 14;
				break;
			case CHEESE_POTATO:
				this.food = "Potato with cheese";
				this.heal = 16;
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
		this.antiPotionHandler.killThread();
		this.grounditemHandler.killThread();
		this.specHandler.killThread();
		this.prayerHandler.killThread();
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
	
	protected class	AntiPotionHandler implements KillableThread{
		protected boolean killThread;
		
		protected int aFireTimer = 0;
		
		@Override
		public void run() {
			new Thread(() -> {handleAntiPoison();}).start();
			new Thread(() -> {handleAntiFire();}).start();
		}

		@Override
		public void killThread() {
			this.killThread = true;
		}

		@Override
		public boolean isAlive() {
			return !this.killThread;
		}
		
		public void handleAntiPoison() {
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
						
						RandomProvider.sleep(2000, 2500);
						
						script.getMouse().move();
						
						controller.returnMouseAccess();
						Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
								//random.nextInt(6) + 10;
					}
				}
			}			
		}
		
		public void handleAntiFire() {
			while(!killThread) {
				int sleep = RandomProvider.randomInt(1000, 1500);
				controller.sleep(sleep);
				this.aFireTimer = this.aFireTimer - sleep;
				//controller.debug("Timer: " + aFireTimer);
				if(this.aFireTimer < 0) {
					if(script.getLocalPlayer().isHealthBarVisible()) {
						if(script.getInventory().contains(f -> f != null && f.getName().contains("Antifire potion"))) {
							controller.getGraphicHandler().setInfo("Combat trainer: Drinking Antifire");
							if(script.getInventory().contains(f -> f != null && f.getName().contains("Antifire potion"))) {
								Thread.currentThread().setPriority(Thread.MAX_PRIORITY-1);
								while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
								
								controller.debug("Mouse control: Combat module Antifire");
		
								if(!script.getTabs().isOpen(Tab.INVENTORY)) {
									script.getTabs().open(Tab.INVENTORY);
									RandomProvider.sleep(100, 150);
								}	
								
								Item eat = script.getInventory().get(f -> f != null && f.getName().contains("Antifire potion"));
								eat.interact();
								
								RandomProvider.sleep(2000, 2500);
								script.getMouse().move();
								
								controller.returnMouseAccess();
								Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
								this.aFireTimer = RandomProvider.randomInt((300*1000),(330*1000));
							}
						}
						else if(script.getInventory().contains(f -> f != null && f.getName().contains("Extended antifire"))) {
							controller.getGraphicHandler().setInfo("Combat trainer: Drinking Antifire");
							if(script.getInventory().contains(f -> f != null && f.getName().contains("Extended antifire"))) {
								Thread.currentThread().setPriority(Thread.MAX_PRIORITY-1);
								while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
								
								controller.debug("Mouse control: Combat module Antifire");
		
								if(!script.getTabs().isOpen(Tab.INVENTORY)) {
									script.getTabs().open(Tab.INVENTORY);
									RandomProvider.sleep(100, 150);
								}	
								
								Item eat = script.getInventory().get(f -> f != null && f.getName().contains("Extended antifire"));
								eat.interact();
								
								RandomProvider.sleep(2000, 2500);
								script.getMouse().move();
								
								controller.returnMouseAccess();
								Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
								this.aFireTimer = RandomProvider.randomInt((660*1000),(690*1000));
							}
						}
						else if(script.getInventory().contains(f -> f != null && f.getName().contains("Super antifire potion"))) {
							controller.getGraphicHandler().setInfo("Combat trainer: Drinking Antifire");
							if(script.getInventory().contains(f -> f != null && f.getName().contains("Super antifire potion"))) {
								Thread.currentThread().setPriority(Thread.MAX_PRIORITY-1);
								while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
								
								controller.debug("Mouse control: Combat module Antifire");
		
								if(!script.getTabs().isOpen(Tab.INVENTORY)) {
									script.getTabs().open(Tab.INVENTORY);
									RandomProvider.sleep(100, 150);
								}	
								
								Item eat = script.getInventory().get(f -> f != null && f.getName().contains("Super antifire potion"));
								eat.interact();
								
								RandomProvider.sleep(2000, 2500);
								script.getMouse().move();
								
								controller.returnMouseAccess();
								Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
								this.aFireTimer = RandomProvider.randomInt((120*1000),(150*1000));
							}
						else if(script.getInventory().contains(f -> f != null && f.getName().contains("Extended super antifire"))) {
							controller.getGraphicHandler().setInfo("Combat trainer: Drinking Antifire");
							if(script.getInventory().contains(f -> f != null && f.getName().contains("Extended super antifire"))) {
								Thread.currentThread().setPriority(Thread.MAX_PRIORITY-1);
								while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
								
								controller.debug("Mouse control: Combat module Antifire");
		
								if(!script.getTabs().isOpen(Tab.INVENTORY)) {
									script.getTabs().open(Tab.INVENTORY);
									RandomProvider.sleep(100, 150);
								}	
								
								Item eat = script.getInventory().get(f -> f != null && f.getName().contains("Extended super antifire"));
								eat.interact();
								
								RandomProvider.sleep(2000, 2500);
								script.getMouse().move();
								
								controller.returnMouseAccess();
								Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
								this.aFireTimer = RandomProvider.randomInt((300*1000),(330*1000));
							}
						}
					}
				}
			}
		}	
	}
		
	}
	
	public void setPrayerValues(ArrayList<Integer> values) {
		this.prayerHandlerRef.setPrayerValues(values);
	}
	
	protected class PrayerHandler implements KillableThread{
		protected boolean killThread;
		protected Map<Integer,Integer> toggleToTexture = new HashMap<Integer,Integer>(); //Widgets: 77/4/x
		protected ArrayList<Integer> prayerToggles = new ArrayList<Integer>();
		
		protected int lastPoint;
		protected int gameTick;
		protected int threadTime;
		protected int prayTime;
		
		protected boolean reset;
		
		public PrayerHandler() {
			this.createMap();
		}
		
		
		@Override
		public void run() {
			
			setupQuickPrayers();
			
			while(!killThread && usePrayer) {
				if(script.getLocalPlayer().isInCombat() && script.getSkills().getBoostedLevels(Skill.PRAYER) > 0) {
					controller.getAntiBanHandler().pauseAllAntibanThreads();
					
					controller.getGraphicHandler().setInfo("Combat trainer: Prayer flick");
					while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
					controller.debug("Mouse control: Combat module praying");
					
					//for(int i = 0; i < 10; i++) {
					WidgetChild qp = script.getWidget(160, 14);
					
					qp.interact("Activate");
					RandomProvider.sleep(1000, 1100);
					this.lastPoint = script.getSkills().getBoostedLevels(Skill.PRAYER);
					while(this.lastPoint == script.getSkills().getBoostedLevels(Skill.PRAYER)) {controller.sleep(10);}
					this.lastPoint = script.getSkills().getBoostedLevels(Skill.PRAYER);
					new Thread(() -> {
						while(!killThread) {
							for(int i = 0; i < 61; i++) {	
								if(this.lastPoint != script.getSkills().getBoostedLevels(Skill.PRAYER) && !this.reset) {
									i = 0;
									this.reset = true;
								}
								controller.sleep(10);
							}
							
							this.reset = false;
							
							if(this.threadTime == 0) {
								this.threadTime = 1;
							}
							else {
								this.threadTime = 0;
							}
						}
					}).start();

					flickLoop: while(!killThread && script.getLocalPlayer().isInCombat()) {	
					
					
						
						int sleep1 = RandomProvider.randomInt(35, 55); 
						int sleep2 = RandomProvider.randomInt(25, 55); 
						int sleep3 = RandomProvider.randomInt(35, 55); 
						
						while(prayTime == threadTime) {RandomProvider.sleep(10);}
						
						this.prayTime = threadTime;
						
						controller.sleep(sleep1);
						if(!qp.hasAction("Deactivate")) {
							qp.interact("Activate");
							//script.getMouse().move(new Point(RandomProvider.randomInt(525, 560),
								//					RandomProvider.randomInt(85, 100)));
							controller.debug("Fixing");
							continue flickLoop;
						}
						
						qp.interact("Deactivate");
						controller.sleep(sleep2);
						qp.interact("Activate");
						
						controller.sleep(sleep3);
						
						
					}
					
					if(qp.hasAction("Deactivate")) {
						qp.interact("Deactivate");
						script.getMouse().move(new Point(RandomProvider.randomInt(525, 560),
												RandomProvider.randomInt(85, 100)));
					}
					
					controller.returnMouseAccess();
					
				}
				else {
					controller.getAntiBanHandler().resumeAllAntibanThreads();
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
		
		public void setPrayerValues(ArrayList<Integer> values) {
			this.prayerToggles = values;
		}
		
		private void createMap() {
			toggleToTexture.put(0, 30);
			toggleToTexture.put(1, 32);
			toggleToTexture.put(2, 34);
			toggleToTexture.put(18, 36);
			toggleToTexture.put(19, 38);
			
			toggleToTexture.put(3, 40);
			toggleToTexture.put(4, 42);
			toggleToTexture.put(5, 44);
			toggleToTexture.put(6, 46);
			toggleToTexture.put(7, 48);
			
			toggleToTexture.put(8, 50);
			toggleToTexture.put(20, 52);
			toggleToTexture.put(21, 54);
			toggleToTexture.put(9, 56);
			toggleToTexture.put(10, 58);
			
			toggleToTexture.put(11, 60);
			toggleToTexture.put(12, 62);
			toggleToTexture.put(13, 64);
			toggleToTexture.put(14, 66);
			toggleToTexture.put(22, 68);
			
			toggleToTexture.put(23, 70);
			toggleToTexture.put(15, 72);
			toggleToTexture.put(16, 74);
			toggleToTexture.put(17, 76);
			toggleToTexture.put(28, 78);
			
			toggleToTexture.put(25, 80);
			toggleToTexture.put(26, 82);
			toggleToTexture.put(24, 84);
			toggleToTexture.put(27, 86);
			
		}
		
		private void setupQuickPrayers() {
			while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
			
			controller.debug("Mouse access: Prayer Handler");
			controller.debug("Mouse access: Prayer Handler");
			controller.debug("Mouse access: Prayer Handler");
			controller.debug("List size: " + this.prayerToggles.size());
			script.getWidget(160, 14).interact("Setup");
			RandomProvider.sleep(1000, 1500);
			for(Integer toggle : prayerToggles) {
				
				if(script.getWidget(77, 4).getChild(this.toggleToTexture.get(toggle)) != null 
				   && script.getWidget(77, 4).getChild(this.toggleToTexture.get(toggle)).getTextureId() == 180) {
					script.getWidget(77, 4).getChild(toggle).interact("Toggle");
					RandomProvider.sleep(400, 550);
				}
			}
			for(Integer key : this.toggleToTexture.keySet()) {
				if(script.getWidget(77, 4).getChild(this.toggleToTexture.get(key)) != null 
						  && script.getWidget(77, 4).getChild(this.toggleToTexture.get(key)).getTextureId() == 181) {
					if(!this.prayerToggles.contains(key)) {
						script.getWidget(77, 4).getChild(key).interact("Toggle");
						RandomProvider.sleep(400, 550);
					}
				}
			}
			
			script.getWidget(77, 5).interact("Done");
			RandomProvider.sleep(700, 800);
			
			script.getTabs().open(Tab.INVENTORY);
			RandomProvider.sleep(500, 700);
			
			controller.returnMouseAccess();
		}
		
	}

	
	protected class SpecialAttackHandler implements KillableThread{
		protected boolean killThread;
		protected boolean specSet;
		protected Map<String, Integer> map = new HashMap<>();
		protected int specAt;
		
		
		public SpecialAttackHandler() {
			map.put("Dragon dagger", 25);
			map.put("Dragon dagger(p)", 25);
			map.put("Dragon dagger(p+)", 25);
			map.put("Dragon dagger(p++)", 25);
			map.put("Dragon 2h sword", 60);
			map.put("Dragon battleaxe", 100);
			map.put("Dragon claws", 50);
			map.put("Dragon halberd", 30);
			map.put("Dragon hasta", 25);
			map.put("Dragon hasta(p)", 25);
			map.put("Dragon hasta(p+)", 25);
			map.put("Dragon hasta(p++)", 25);
			map.put("Dragon hasta(kp)", 25);
			map.put("Dragon longsword", 25);
			map.put("Dragon mace", 25);
			map.put("Dragon scimitar", 55);
			map.put("Dragon sword", 40);
			map.put("Dragon warhammer", 50);
			map.put("Armadyl godsword", 50);
			map.put("Bandos godsword", 50);
			map.put("Saradomin godsword", 50);
			map.put("Saradomin sword", 100);
			map.put("Saradomin's blessed sword", 100);
			map.put("Zamorak godsword", 50);
			map.put("Granite hammer", 60);
			map.put("Barrelchest anchor", 50);
			map.put("Crystal halberd", 30);
			map.put("Dorgeshuun crossbow", 75);
			map.put("Dragon crossbow", 60);
			map.put("Armadyl crossbow", 40);
			map.put("Dark bow", 55);
			map.put("Magic comp bow", 35);
			map.put("Magic longbow", 35);
			map.put("Magic shortbow (i)", 55);
			map.put("Magic shortbow", 55);
			map.put("Eldritch nightmare staff", 75);
			map.put("Staff of balance", 100);
			map.put("Staff of the dead", 100);
			map.put("Toxic staff of the dead", 100);
			map.put("Staff of light", 100);
			map.put("Volatile nightmare staff", 55);
			map.put("Arclight", 50);
			map.put("Darklight", 50);
			map.put("Abyssal bludgeon", 50);
			map.put("Abyssal dagger", 50);
			map.put("Abyssal dagger(p)", 50);
			map.put("Abyssal dagger(p+)", 50);
			map.put("Abyssal dagger(p++)", 50);
			map.put("Abyssal tentacle", 50);
			map.put("Abyssal whip", 50);
			map.put("Statius's warhammer", 35);
			map.put("Vesta's blighted longsword", 25);
			map.put("Vesta's longsword", 25);
			map.put("Vesta's spear", 50);
		}
		
		@Override
		public void run() {
			while(!killThread) {
				Item i = script.getEquipment().getItemInSlot(EquipmentSlot.WEAPON.getSlot());
				if(i != null && map.containsKey(i.getName()) && script.getLocalPlayer().isInCombat()) {
					if(!specSet && i != null) {
						int req = map.get(i.getName());
						if(req >= 100) {
							this.specAt = 100;
							this.specSet = true;
						}
						else {
							this.specAt = RandomProvider.randomInt(req,100);
							if(specAt >= 100) {
								this.specAt = 100;
							}
							this.specSet = true;
						}
						//controller.debug("At: " + specAt);
					}
					if(script.getCombat().getSpecialPercentage() >= specAt && !script.getCombat().isSpecialActive()) {
							controller.debug("1");
							this.specSet = false;
							while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
							
							controller.debug("Mouse control: Combat module SpecialHandler");
							
							script.getCombat().toggleSpecialAttack(true);
							RandomProvider.sleep(10, 20);
							script.getMouse().move();
							
							if(!script.getTabs().isOpen(Tab.INVENTORY)) {
								RandomProvider.sleep(2500, 3700);
								script.getTabs().open(Tab.INVENTORY);
								RandomProvider.sleep(100, 150);
							}	
							controller.returnMouseAccess();
							
					}
					//controller.debug("%" + script.getCombat().getSpecialPercentage());
					RandomProvider.sleep(900, 1100);
					
				}
				else {
					RandomProvider.sleep(900, 1100);
				}
			}
		}

		@Override
		public void killThread() {
			this.killThread = true;
			this.map.clear();
		}

		@Override
		public boolean isAlive() {
			return !this.killThread;
		}
		
	}


}
