package scripts;
import java.util.Random;

import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.widget.Widget;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import antiban.RandomProvider;
import client.ClientThread;
import client.ThreadController;
import movement.Location;
import movement.LocationFactory;
import utilities.GearHandler.Gear;

public class SmelterModule extends ScriptModule {
	private ClientThread script;
	private ThreadController controller;
	
	private int actionsCompleted;
	private int limit;
	private int actionTester;
	
	
	private OreToBar bar;	
	private Bars barEnum;
	
	private LocationFactory.GameLocation locationEnum;
	
	private boolean killThread;
	
	public SmelterModule(ClientThread script, ThreadController controller, LocationFactory.GameLocation location, int limit, SmelterModule.Bars b) {
		this.controller = controller;
		this.script = script;
		this.locationEnum = location;
		
		this.limit = limit;
		this.actionTester = 0;
		this.moduleName = "SmelterModule: " + b;
		
		this.barEnum = b;
		this.bar = new OreToBar(b);
	}
	
	@Override
	public void run() {

		while(!this.killThread) {
			RandomProvider.sleep(1500, 2200);
			
			if(!script.getLocalPlayer().isAnimating()) {
				if(!(script.getInventory().count(f -> f != null && f.getName().contains(bar.getOreName1())) >= bar.getOreCost1()) ||
						!(script.getInventory().count(f -> f != null && f.getName().contains(bar.getOreName2())) >= bar.getOreCost2())) {
	
					this.actionTester = 0;
					controller.getGraphicHandler().setInfo("Smelter: Inventory Full Banking");
					
					controller.getMovementHandler().moveToBank();
					
					while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
					while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
					
					this.actionsCompleted++;
					
					if(!script.getInventory().isEmpty()) {
						
						script.getBank().depositAllExcept(f -> f != null && f.getName().equals("Ammo mould"));	
					}
					RandomProvider.sleep(500, 1250);
					
					if(script.getBank().count(f -> f != null && f.getName().contains(bar.getOreName1())) >= bar.getOre1InBack()) {
						script.getBank().withdraw(f -> f != null && f.getName().contains(this.bar.getOreName1()), this.bar.getOre1InBack());
						RandomProvider.sleep(500, 1250);
						
						if(this.barEnum != Bars.CANNON_BALL) {
							if(script.getBank().count(f -> f != null && f.getName().contains(bar.getOreName2())) >= bar.getOre2InBack()) {
								script.getBank().withdraw(f -> f != null && f.getName().contains(this.bar.getOreName2()), this.bar.getOre2InBack());
							}
							/**
							 * If module doesn't materials to continue:
							 */
							else {
								this.killThread = true;
								controller.returnKeyboardAccess();
								controller.returnMouseAccess();
							}
						}
					}
					/**
					 * If module doesn't materials to continue:
					 */
					else {
						this.killThread = true;
						controller.returnKeyboardAccess();
						controller.returnMouseAccess();
					}
					
					script.getBank().close();
					RandomProvider.sleep(500, 1250);
					script.getMouse().move();
					
					controller.returnKeyboardAccess();
					controller.returnMouseAccess();
				}
				
				else if(!controller.getMovementHandler().isPlayerInLocation()) {
				
					controller.getGraphicHandler().setInfo("Smelter: Walking to Furnace - " + locationEnum);
					
					controller.getMovementHandler().moveToLocation();
						
				}
				else if(actionTester != script.getInventory().count(f -> f != null && f.getName().toLowerCase().contains("bar"))) {
					this.actionTester = script.getInventory().count(f -> f != null && f.getName().toLowerCase().contains("bar"));
				}
				else if(!script.getLocalPlayer().isAnimating()) {
					controller.getGraphicHandler().setInfo("Smelter: Smelting - " + barEnum);
					GameObject furnace = script.getGameObjects().closest(f -> f != null && f.getName().toLowerCase().contains("furnace"));
					
					int tester = RandomProvider.randomInt(2);
					int tester2 = RandomProvider.randomInt(2);
	
					while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
					while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
					
					if(furnace != null) {
						
						if(tester2 == 0) {
							script.getCamera().rotateToEntity(furnace);
							RandomProvider.sleep(750, 1000);
						}
						
						if(tester == 0) {
							furnace.interact("Smelt");
							script.getMouse().move();
						}
						else {
							furnace.interact();
							script.getMouse().move();
						}
						RandomProvider.sleep(1150, 1750);
						script.getKeyboard().type(bar.getKeyboardNumber(),false);
						
						RandomProvider.sleep(750, 1750);
					
					}
					
					controller.returnKeyboardAccess();
					controller.returnMouseAccess();
				}
			}
		}
		
	}

	public boolean setupModule() {
		controller.getMovementHandler().newLocation(this.locationEnum);
		
		controller.getMovementHandler().teleportToLocation();
		
		controller.getGearHandler().handleGearSwap(Gear.UTILITY);
		
		controller.getGraphicHandler().setInfo("Smelter: Setting Up Module");
		
		if(!(script.getInventory().count(f -> f != null && f.getName().contains(bar.getOreName1())) >= bar.getOreCost1()) ||
				!(script.getInventory().count(f -> f != null && f.getName().contains(bar.getOreName2())) >= bar.getOreCost2())	) {
			
			controller.getMovementHandler().locateBank();
			
			while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
			while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}

			controller.debug("Mouse control: SmelterModule");
			controller.debug("Keyboard control: SmelterModule");
			
			if(!script.getInventory().isEmpty()) {
				script.getBank().depositAllExcept(f -> f != null && f.getName().equals("Ammo mould"));
				RandomProvider.sleep(1000, 1750);
				
			}
			
			if(script.getBank().count(f -> f != null && f.getName().contains(bar.getOreName1())) >= bar.getOre1InBack()) {
				script.getBank().withdraw(f -> f != null && f.getName().contains(this.bar.getOreName1()), this.bar.getOre1InBack());
				RandomProvider.sleep(500, 1250);
	
					if(script.getBank().count(f -> f != null && f.getName().contains(bar.getOreName2())) >= bar.getOre2InBack()) {
							script.getBank().withdraw(f -> f != null && f.getName().contains(this.bar.getOreName2()), this.bar.getOre2InBack());
					}
					else {
						controller.returnKeyboardAccess();
						controller.returnMouseAccess();
						return false;
					}
				
			}
			else {
				controller.returnKeyboardAccess();
				controller.returnMouseAccess();
				return false;
			}

			
			script.getBank().close();
			RandomProvider.sleep(500, 1250);
			script.getMouse().move();
			
			controller.returnKeyboardAccess();
			controller.returnMouseAccess();
			return true;
			
		}
		else {
			return true;
		}
	}
	
	public enum Bars {
		BRONZE, IRON, STEEL, ADAMANTITE, MITHRIL, RUNE, GOLD, CANNON_BALL
	}
	
	private class OreToBar{
		private int oreCost1;
		private int oreCost2;
		private int ore1InBack;
		private int ore2InBack;
		private String oreName1;
		private String oreName2;
		private int keyboardNumber;
		
		private OreToBar(SmelterModule.Bars b) {
			if(b == SmelterModule.Bars.BRONZE) {
				this.setOreCost1(1);
				this.setOreName1("Tin ore");
				this.setOreCost2(1);
				this.setOreName2("Copper ore");
				this.setOre1InBack(14);
				this.setOre2InBack(14);
				this.keyboardNumber = 1;
			}
			else if(b == SmelterModule.Bars.IRON) {
				this.setOreCost1(1);
				this.setOreName1("Iron ore");
				this.setOreCost2(0);
				this.setOreName2("");
				this.setOre1InBack(28);
				this.setOre2InBack(0);
				this.keyboardNumber = 2;
			}
			else if(b == SmelterModule.Bars.STEEL) {
				this.setOreCost1(1);
				this.setOreName1("Iron ore");
				this.setOreCost2(2);
				this.setOreName2("Coal");
				this.setOre1InBack(9);
				this.setOre2InBack(18);
				this.keyboardNumber = 4;
			}
			else if(b == SmelterModule.Bars.GOLD) {
				this.setOreCost1(1);
				this.setOreName1("Gold ore");
				this.setOreCost2(0);
				this.setOreName2("");
				this.setOre1InBack(28);
				this.setOre2InBack(0);
				this.keyboardNumber = 5;
			}
			else if(b == SmelterModule.Bars.MITHRIL) {
				this.setOreCost1(1);
				this.setOreName1("Mithril ore");
				this.setOreCost2(4);
				this.setOreName2("Coal");
				this.setOre1InBack(5);
				this.setOre2InBack(20);
				this.keyboardNumber = 6;
			}
			else if(b == SmelterModule.Bars.ADAMANTITE) {
				this.setOreCost1(1);
				this.setOreName1("Adamantite ore");
				this.setOreCost2(6);
				this.setOreName2("Coal");
				this.setOre1InBack(4);
				this.setOre2InBack(24);
				this.keyboardNumber = 7;
			}
			else if(b == SmelterModule.Bars.RUNE) {
				this.setOreCost1(1);
				this.setOreName1("Rune ore");
				this.setOreCost2(8);
				this.setOreName2("Coal");
				this.setOre1InBack(3);
				this.setOre2InBack(24);
				this.keyboardNumber = 8;
			}
			else if(b == SmelterModule.Bars.CANNON_BALL) {
				this.setOreCost1(1);
				this.setOreName1("Steel bar");
				this.setOreCost2(0);
				this.setOreName2("Ammo mould");
				this.setOre1InBack(27);
				this.setOre2InBack(1);
				this.keyboardNumber = 1;
			}
		}
		
		public int getOreCost1() {
			return oreCost1;
		}
		public void setOreCost1(int oreCost1) {
			this.oreCost1 = oreCost1;
		}
		public int getOre2InBack() {
			return ore2InBack;
		}
		public void setOre2InBack(int ore2InBack) {
			this.ore2InBack = ore2InBack;
		}
		public int getOre1InBack() {
			return ore1InBack;
		}
		public void setOre1InBack(int ore1InBack) {
			this.ore1InBack = ore1InBack;
		}
		public int getOreCost2() {
			return oreCost2;
		}
		public void setOreCost2(int oreCost2) {
			this.oreCost2 = oreCost2;
		}
		public String getOreName1() {
			return oreName1;
		}
		public void setOreName1(String oreName1) {
			this.oreName1 = oreName1;
		}
		public String getOreName2() {
			return oreName2;
		}
		public void setOreName2(String oreName2) {
			this.oreName2 = oreName2;
		}
		public int getKeyboardNumber() {
			return this.keyboardNumber;
		}

	}

	@Override
	public Skill getSkillToHover() {
		return Skill.SMITHING;
	}


	@Override
	public void killThread() {
		this.killThread = true;
	}

	@Override
	public boolean isAlive() {
		return !this.killThread;
	}

	@Override
	public boolean isReady() {
		if(this.limit <= this.actionsCompleted) {
			return true;
		}
		else {
			return false;
		}
	}
}
