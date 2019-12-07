import java.util.ArrayList;
import java.util.Random;

import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.wrappers.items.Item;

public class CombatModule extends ScriptModule {
	private Random random = new Random();
	private MainLooper script;
	private String monsterName;
	private Skill skillToTrain;
	private String food;
	private Location location;
	private int delay;
	private int eatAt;
	private Monster monsterEnum;
	private Food foodEnum;
	private int actionsCompleted;
	private int limit;
	private ArrayList<String> collect;
	private boolean pickUp;
	private int heal;
	private int realHp;
	private Locations locationEnum;
	private boolean error;
	private Training skill;
	
	public CombatModule(MainLooper script, CombatModule.Monster monster, CombatModule.Food food, int limit, Boolean pickUp, Training skill) {
		eatAt = random.nextInt(6) + 10;
		this.limit = limit;
		this.script = script;
		this.collect = new ArrayList<String>();
		this.skillToTrain = Skill.STRENGTH;
		setMonsterVariables(monster);
		setFood(food);
		this.pickUp = pickUp;
		this.monsterEnum = monster;
		this.foodEnum = food;
		this.error = false;
		this.skill = skill;
		this.moduleName = "CombatModule: " + monster;
	}
	
	@Override
	public int onLoop() {
		random.setSeed(random.nextLong());
		delay = random.nextInt(1000) + 2000;
		
		if(limit <= this.actionsCompleted) {
			script.nextModule();
			script.sleep(2000);
			return delay;
		}
		
		if(script.getSkills().getBoostedLevels(Skill.HITPOINTS) <= eatAt) {
			script.setInfoText("Combat trainer: Eating - " + foodEnum);
			if(script.getInventory().contains(f -> f != null && f.getName().equals(food))) {
				Item eat = script.getInventory().get(f -> f != null && f.getName().equals(food));
				eat.interact();
				script.getMouse().move();
				int realHp = script.getSkills().getRealLevel(Skill.HITPOINTS);
				eatAt = realHp - random.nextInt((int)(realHp * 0.2323232323)) - (heal + 1);
						//random.nextInt(6) + 10;
			}
		}
		
		if(!script.getLocalPlayer().isAnimating() && !script.getLocalPlayer().isInCombat()) {
			
			
			if(!script.getInventory().isFull() && pickUp && location.inArea()) {
				for(String item : collect) {
					GroundItem collectItem = script.getGroundItems().closest(f -> f != null && f.getName().equals(item));
					if(collectItem != null) {
						script.setInfoText("Combat trainer: Picking up Item");
						collectItem.interact("Take");
						script.getMouse().move();
						return delay -1000;
					}
				}
			}
			
			if(!script.getInventory().contains(f -> f != null && f.getName().equals(food))) {
				script.setReact(0);
				this.actionsCompleted++;
				script.setInfoText("Combat trainer: No Food Left - Banking");
				location.travelToBank();
				script.sleep(random.nextInt(750)+ 700);
				if(!script.getInventory().isEmpty()) {
					script.getBank().depositAllItems();
					script.sleep(random.nextInt(750)+ 1000);
				}
				
				if(script.getBank().contains(food)) {
					script.getBank().withdrawAll(f -> f != null && f.getName().equals(food));
					script.sleep(random.nextInt(750)+ 700);
					script.getBank().close();
					script.sleep(random.nextInt(750)+ 400);
					script.getMouse().move();
				}
				else {
					script.nextModule();
					script.sleep(2000);
					return delay;
				}
				
			}
			
			else if(!location.inArea()) {
				script.setReact(0);
				script.setInfoText("Combat trainer: Moving to Location - " + locationEnum);
				location.travel();
			}
			
			else if(!script.getLocalPlayer().isAnimating() && !script.getLocalPlayer().isInCombat()) {
				
				if(script.getEquipment().getItemInSlot(EquipmentSlot.WEAPON.getSlot()) == null) {
					script.getMessenger().sendMessage("CombatTrainer - Module ERROR");
					script.getMessenger().sendMessage("No Weapon Found - Changing Module");
					script.nextModule();
					script.sleep(2000);
					return delay;
					
				}
				if(script.getEquipment().getItemInSlot(EquipmentSlot.WEAPON.getSlot()).getName().toLowerCase().contains("bow")) {
					if(script.getEquipment().getItemInSlot(EquipmentSlot.WEAPON.getSlot()) == null) {
						script.getMessenger().sendMessage("CombatTrainer - Module ERROR");
						script.getMessenger().sendMessage("No Arrows Found - Changing Module");
						script.nextModule();
						script.sleep(2000);
						return delay;
					}
					
				}
				
				script.setReact(1);
				script.setInfoText("Combat trainer: Attacking target - "+ monsterEnum);
				NPC monster = script.getNpcs().closest(f -> f.getName().equals(monsterName) && f != null && !f.isInCombat() && f.getLevel()!=51);
				int randomizer = random.nextInt(2);
				if(randomizer == 0) {
					monster.interact();
				}
				else {
					monster.interact("Attack");
				}
				script.getMouse().move();
			}
			
		}
			
		return delay;
	}
	
	public enum Training {
		RANGE, ATTACK, STRENGTH
	}
	

	@Override
	public int actionsCompleted() {
		return this.actionsCompleted;
	}

	@Override
	public boolean setupModule() {
		this.location.teleportToLocation();
		
		script.setInfoText("Combat trainer: Setting up module");
		if(!script.getInventory().contains(f -> f != null && f.getName().equals(food))) {
			
			if(!script.getWalking().isRunEnabled() && script.getWalking().getRunEnergy() > 0) {
				script.getWalking().toggleRun();
			}
	
			while(!script.getBank().isOpen()) {
				script.getBank().open(script.getBank().getClosestBankLocation());
				script.sleep(random.nextInt(1000)+2000);
			}
			
			if(!script.getInventory().isEmpty()) {
				script.getBank().depositAllItems();
				script.sleep(random.nextInt(750)+ 1000);
			}
			
			if(script.getBank().contains(food)) {
				script.getBank().withdrawAll(f -> f != null && f.getName().equals(food));
				script.sleep(random.nextInt(750)+ 700);
				script.getBank().close();
				script.sleep(random.nextInt(750)+ 400);
				script.getMouse().move();
				
				
				return true;
			}
			else {
				return false;
			}

		}
		else {
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
	
	public enum Monster {
		GIANT_FROG, BARBARIAN, EXPERIMENT
	}
	
	public enum Food {
		TROUT, SALMON, TUNA
	}
	
	public void setMonsterVariables(Monster monster) {
		if(monster == Monster.GIANT_FROG) {
			this.monsterName = "Giant frog";
			this.location = new Location(script, Locations.COMBAT_GIANT_FROG);
			this.collect.add("Big bones");
			this.locationEnum = Locations.COMBAT_GIANT_FROG;
			
			
		}
		else if(monster == Monster.BARBARIAN) {
			this.monsterName = "Barbarian";
			this.location = new Location(script, Locations.COMBAT_BARBARIAN);
			this.locationEnum = Locations.COMBAT_BARBARIAN;
		}
		else if(monster == Monster.EXPERIMENT) {
			this.monsterName = "Experiment";
			this.location = new Location(script, Locations.COMBAT_EXPERIMENTS);
			this.locationEnum = Locations.COMBAT_EXPERIMENTS;
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
		

	}

	@Override
	public void errorTest() {
		// TODO Auto-generated method stub
		
	}

}
