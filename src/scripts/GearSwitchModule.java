package scripts;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.wrappers.items.Item;

import client.ClientThread;

public class GearSwitchModule extends ScriptModule {
	private ArrayList<String> magic = new ArrayList<String>();
	private ArrayList<String> melee = new ArrayList<String>();
	private ArrayList<String> range = new ArrayList<String>();
	private ArrayList<String> utility = new ArrayList<String>();
	private ArrayList<String> gear = new ArrayList<String>();
	private ArrayList<Boolean> found;
	private ClientThread script;
	private Random random = new Random();
	private boolean repeat;
	private Gear type;
	
	public GearSwitchModule(ClientThread script, Gear type) {
		this.type = type;
		this.script = script;
		this.found = new ArrayList<Boolean>();
		this.repeat = false;
		this.moduleName = "GearSwitchModule";
		setGears();
		setGearList(type);
	}
	


	@Override
	public int onLoop() {
		
		if(completeTest() || repeat) {
			script.nextModule();
			script.sleep(5000);
			return random.nextInt(5000)+5000;
		}
		else {
			setupModule();
			this.repeat = true;
			return random.nextInt(2000)+2000;
		}
	}

	@Override
	public int actionsCompleted() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean setupModule() {
		script.setInfoText("Gear Switcher: Switching gear");
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
		
		script.getBank().depositAllEquipment();
		script.sleep(random.nextInt(750)+ 1000);
		
		
		for(String piece : gear) {
			found.add(false);
			script.log("" + found.size());
			if(script.getBank().contains(f -> f != null && f.getName().equals(piece))) {
				if(piece.toLowerCase().contains("arrow") || piece.toLowerCase().contains("dart") || piece.toLowerCase().contains("knife")) {
					script.getBank().withdrawAll(f -> f != null && f.getName().equals(piece));
				}
				else {
					script.getBank().withdraw(f -> f != null && f.getName().equals(piece));
				}
				script.log("" + gear.indexOf(piece));
				found.set(gear.indexOf(piece), true);
				script.sleep(random.nextInt(300)+ 100);
			}
		}
		
		script.sleep(random.nextInt(750)+ 1000);
		script.getBank().close();
		
		if(!script.getTabs().isOpen(Tab.INVENTORY)) {
			script.getTabs().open(Tab.INVENTORY);
			script.sleep(70);
		}
		
		for(String piece : gear) {
			if(script.getInventory().contains(f -> f != null && f.getName().equals(piece))) {
				Item item = script.getInventory().get(f -> f != null && f.getName().equals(piece));
				item.interact();
				script.sleep(random.nextInt(300)+ 100);
			}
		}
		
		script.sleep(random.nextInt(750)+ 1000);
		while(!script.getBank().isOpen()) {
			script.getBank().open(script.getBank().getClosestBankLocation());
			script.sleep(random.nextInt(1000)+2000);
		}
		
		return true;
	}
	
	private void setGearList(Gear gear) {
		switch(gear) {
		
			case MAGIC:
				this.gear = this.magic;
				break;
				
			case UTILITY:
				this.gear = this.utility;
				break;
			
			case MELEE:
				this.gear = this.melee;
				break;
				
			case RANGE:
				this.gear = this.range;
				break;
		}
		
	}
	
	private void setGears() {
		ArrayList<String> meleeTraining = new ArrayList<String>();
		meleeTraining.add("Iron full helm");
		meleeTraining.add("Iron platebody");
		meleeTraining.add("Iron platelegs");
		meleeTraining.add("Iron kiteshield");
		meleeTraining.add("Amulet of glory");
		meleeTraining.add("Dragon sword");
		meleeTraining.add("Purple gloves");
		meleeTraining.add("Team-48 cape");
		meleeTraining.add("Leather boots");
		Collections.shuffle(meleeTraining);
		this.melee = meleeTraining;
		
		ArrayList<String> rangeTraining = new ArrayList<String>();
		rangeTraining.add("Leather cowl");
		rangeTraining.add("Blue d'hide chaps");
		rangeTraining.add("Blue d'hide vamb");
		rangeTraining.add("Leather body");
		rangeTraining.add("Amulet of glory");
		rangeTraining.add("Maple shortbow");
		rangeTraining.add("Iron arrow");
		rangeTraining.add("Team-48 cape");
		rangeTraining.add("Leather boots");
		Collections.shuffle(rangeTraining);
		this.range = rangeTraining;
		
		ArrayList<String> skillTraining = new ArrayList<String>();
		skillTraining.add("Wizard hat");
		skillTraining.add("Amulet of glory");
		skillTraining.add("Granite longsword");
		skillTraining.add("Purple gloves");
		skillTraining.add("Team-48 cape");
		skillTraining.add("Leather boots");
		skillTraining.add("Brown apron");
		Collections.shuffle(skillTraining);
		this.utility = skillTraining;
		
		ArrayList<String> mageTraining = new ArrayList<String>();
		mageTraining.add("Staff of fire");
		mageTraining.add("Blue d'hide vamb");
		mageTraining.add("Team-48 cape");
		mageTraining.add("Leather boots");
		mageTraining.add("Iron full helm");
		mageTraining.add("Iron platebody");
		mageTraining.add("Iron platelegs");
		mageTraining.add("Iron kiteshield");
		Collections.shuffle(mageTraining);
		this.magic = mageTraining;
		
	}

	public boolean completeTest() {
		for(String piece : gear) {
			script.log(""+gear.indexOf(piece));
			script.log(""+found.size());
			if(found.get(gear.indexOf(piece))) {
				if(!script.getEquipment().contains(f -> f != null && f.getName().equals(piece))) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public Skill getSkillToHover() {
		// TODO Auto-generated method stub
		return Skill.HITPOINTS;
	}

	@Override
	public void errorTest() {
		// TODO Auto-generated method stub
		
	}
	
	public enum Gear {MELEE, RANGE, MAGIC, UTILITY}

}
