import java.util.ArrayList;
import java.util.Random;

import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.wrappers.items.Item;

public class GearSwitchModule extends ScriptModule {
	private ArrayList<String> gear;
	private ArrayList<Boolean> found;
	private MainLooper script;
	private Random random = new Random();
	private boolean repeat;
	
	public GearSwitchModule(MainLooper script, ArrayList<String> gear) {
		this.gear = gear;
		this.script = script;
		this.found = new ArrayList<Boolean>();
		this.repeat = false;
		this.moduleName = "GearSwitchModule";
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
		
		//int x = 0;
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

}
