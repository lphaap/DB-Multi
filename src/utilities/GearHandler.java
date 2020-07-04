package utilities;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.wrappers.items.Item;

import antiban.RandomProvider;
import client.ClientThread;
import client.KillableHandler;
import client.KillableThread;
import client.ThreadController;

public class GearHandler implements KillableHandler {
	private ArrayList<String> magic = new ArrayList<String>();
	private ArrayList<String> melee = new ArrayList<String>();
	private ArrayList<String> range = new ArrayList<String>();
	private ArrayList<String> utility = new ArrayList<String>();
	private ArrayList<String> gear = new ArrayList<String>();
	private ThreadController controller;
	private ArrayList<Boolean> found;
	private ClientThread script;
	private boolean repeat;
	private boolean killHandler;
	
	private GearSwapper swapper;
	private int debugCounter = 0;
	private int debugAreaCounter = 0;
	private Area debugArea;
	
	private Gear currentGear;
	
	public GearHandler(ClientThread script, ThreadController controller) {
		this.script = script;
		this.controller = controller;
		this.found = new ArrayList<Boolean>();
		this.repeat = false;
		
		initGearLists();
		
		
	}
	
	/**
	 * Handles its own accsess control
	 */
	public void handleGearSwap(Gear gear) {
		if(this.currentGear == gear ) {
			return;
		}
		else {
			
			this.setGearList(gear);
			this.currentGear = gear;
			
			if(manualGearCheck()) {
				return;
			}
			
		}
		
		while(controller.requestKeyboardAccess());
		while(controller.requestMouseAccess());
		
		controller.debug("Mouse control: GearSwap");
		controller.debug("Keyboard control: GearSwap");

		
		new Thread( () -> {
			swapper = new GearSwapper();
			swapper.swapGear();
		}).start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {e.printStackTrace();}
		while(swapper.isSwapInProgress()) {
			if(monitorSwap()) {
				return;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {e.printStackTrace();}
		}
		resetMonitor();
		
		controller.returnKeyboardAccess();
		controller.returnMouseAccess();
	}
	
	
	private boolean monitorSwap() {
		if(debugArea == null || debugArea.contains(script.getLocalPlayer())) {
			this.debugArea = script.getLocalPlayer().getTile().getArea(6);
			this.debugAreaCounter = 0;
		}
		else {
			debugAreaCounter++;
		}
		debugCounter++;
		if(killHandler) {
			if(this.swapper.isAlive()) {
				swapper.killThread();
			}
			return true;
		}
		else {
			return false;
		}
		//TODO: What if counter goes too far + something needs to reset location thread killer
	}
	
	private void resetMonitor() {
		debugCounter = 0;
		debugAreaCounter = 0;
		debugArea = null;
	}
	
	private boolean manualGearCheck() {
		for(String gear : this.gear) {
			if(!script.getEquipment().contains(f -> f != null && f.getName().equals(gear))) {
				return false;
			}
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
	
	private void initGearLists() {
		ArrayList<String> meleeTraining = new ArrayList<String>();
		meleeTraining.add("Purple partyhat");
		meleeTraining.add("Adamant platebody");
		meleeTraining.add("Adamant plateskirt");
		meleeTraining.add("Adamant kiteshield");
		meleeTraining.add("Amulet of strength");
		meleeTraining.add("Rune scimitar");
		//meleeTraining.add("Purple gloves");
		meleeTraining.add("Cabbage cape");
		meleeTraining.add("Leather boots");
		Collections.shuffle(meleeTraining);
		this.melee = meleeTraining;
		
		ArrayList<String> rangeTraining = new ArrayList<String>();
		rangeTraining.add("Purple partyhat");
		rangeTraining.add("Green d'hide chaps");
		rangeTraining.add("Green d'hide vambraces");
		rangeTraining.add("Green d'hide body");
		rangeTraining.add("Amulet of power");
		rangeTraining.add("Maple shortbow");
		rangeTraining.add("Iron arrow");
		rangeTraining.add("Cabbage cape");
		rangeTraining.add("Leather boots");
		Collections.shuffle(rangeTraining);
		this.range = rangeTraining;
		
		ArrayList<String> skillTraining = new ArrayList<String>();
		//skillTraining.add("Adamant kiteshield");
		skillTraining.add("Amulet of strength");
		skillTraining.add("Rune scimitar");
		skillTraining.add("Purple partyhat");
		//skillTraining.add("Purple gloves");
		skillTraining.add("Cabbage cape");
		skillTraining.add("Leather boots");
		//skillTraining.add("Brown apron");
		Collections.shuffle(skillTraining);
		this.utility = skillTraining;
		
		ArrayList<String> mageTraining = new ArrayList<String>();
		//mageTraining.add("Staff of fire");
		mageTraining.add("Green d'hide vambraces");
		mageTraining.add("Cabbage cape");
		mageTraining.add("Leather boots");
		mageTraining.add("Iron full helm");
		mageTraining.add("Iron platebody");
		mageTraining.add("Iron plateskirt");
		mageTraining.add("Iron kiteshield");
		Collections.shuffle(mageTraining);
		this.magic = mageTraining;
		
	}

	public boolean completeTest() {
		for(String piece : gear) {
			//script.log(""+gear.indexOf(piece));
			//script.log(""+found.size());
			if(found.get(gear.indexOf(piece))) {
				if(!script.getEquipment().contains(f -> f != null && f.getName().equals(piece))) {
					return false;
				}
			}
		}
		return true;
	}
	
	public enum Gear {MELEE, RANGE, MAGIC, UTILITY}

	
	private class GearSwapper implements KillableThread{
		private boolean killThread;
		private boolean swapInProggress;
		
		private boolean swapGear() {
			this.swapInProggress = true;
			controller.getGraphicHandler().setInfo("Gear Switcher: Switching gear");
			
			if(!script.getWalking().isRunEnabled() && script.getWalking().getRunEnergy() > 0 && !killThread) {
				script.getWalking().toggleRun();
			}

			while(!script.getBank().isOpen()) {
				script.getBank().open(script.getBank().getClosestBankLocation());
				RandomProvider.sleep(1200, 2000);
				if(this.killThread) {
					return false;
				}
			}
			
			if(!script.getInventory().isEmpty() && !killThread) {
				script.getBank().depositAllItems();
				RandomProvider.sleep(1000, 1800);
			}
			
			script.getBank().depositAllEquipment();
			RandomProvider.sleep(1000, 1800);
			
			
			for(String piece : gear) {
				if(this.killThread) {
					return false;
				}
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
					RandomProvider.sleep(100, 400);
				}
			}
			
			script.sleep(RandomProvider.randomInt(750)+ 1000);
			script.getBank().close();
			
			if(!script.getTabs().isOpen(Tab.INVENTORY) && !killThread) {
				script.getTabs().open(Tab.INVENTORY);
				try {
					Thread.sleep(70);
				} catch (InterruptedException e) {e.printStackTrace();}
			}
			
			for(String piece : gear) {
				if(this.killThread) {
					return false;
				}
				if(script.getInventory().contains(f -> f != null && f.getName().equals(piece))) {
					Item item = script.getInventory().get(f -> f != null && f.getName().equals(piece));
					item.interact();
					RandomProvider.sleep(300, 400);
				}
			}
			
			RandomProvider.sleep(750, 1750);
			while(!script.getBank().isOpen()) {
				if(this.killThread) {
					return false;
				}
				script.getBank().open(script.getBank().getClosestBankLocation());
				RandomProvider.sleep(1000,3000);
			}
			
			this.swapInProggress = false;
			return true;
		}

		@Override
		public void run() {
			swapGear();
			
		}
		
		public boolean isSwapInProgress() {
			return this.swapInProggress;
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


	@Override
	public void killHandler() {
		this.killHandler = true;
		
	}

	
	
}
