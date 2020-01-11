package scripts;
import java.util.Random;

import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.widget.Widget;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import init.ClientThread;
import movement.Location;
import movement.Locations;

public class SmelterModule extends ScriptModule {
	private ClientThread script;
	private int actionsCompleted;
	private Location location;
	private OreToBar bar;
	private Random random;
	private int delay;
	private int actionTester;
	private Locations locationEnum;
	private Bars barEnum;
	
	public SmelterModule(Locations location, SmelterModule.Bars b, ClientThread script) {
		this.location = new Location(script, location);
		this.bar = new OreToBar(b);
		this.random = new Random();
		this.script = script;
		this.locationEnum = location;
		this.barEnum = b;
		actionTester = 0;
		this.moduleName = "SmelterModule: " + b;
	}
	
	@Override
	public int onLoop() {
		random.setSeed(random.nextLong());
		delay = random.nextInt(1000) + 2000;
		if(!script.getLocalPlayer().isAnimating()) {
			if(!(script.getInventory().count(f -> f != null && f.getName().contains(bar.getOreName1())) >= bar.getOreCost1()) ||
					!(script.getInventory().count(f -> f != null && f.getName().contains(bar.getOreName2())) >= bar.getOreCost2())) {
				script.setReact(0);
				this.actionTester = 0;
				script.setInfoText("Smelter: Inventory Full Banking");
				
				location.travelToBank();
				
				if(!script.getInventory().isEmpty()) {
					script.getBank().depositAllExcept(f -> f != null && f.getName().equals("Ammo mould"));	
				}
				script.sleep(random.nextInt(750)+ 500);
				
				if(script.getBank().count(f -> f != null && f.getName().contains(bar.getOreName1())) >= bar.getOre1InBack()) {
					script.getBank().withdraw(f -> f != null && f.getName().contains(this.bar.getOreName1()), this.bar.getOre1InBack());
					script.sleep(random.nextInt(750)+ 500);
					
					if(this.barEnum != Bars.CANNON_BALL) {
						if(script.getBank().count(f -> f != null && f.getName().contains(bar.getOreName2())) >= bar.getOre2InBack()) {
							script.getBank().withdraw(f -> f != null && f.getName().contains(this.bar.getOreName2()), this.bar.getOre2InBack());
						}
						/**
						 * If module doesn't materials to continue:
						 */
						else {
							script.nextModule();
							script.sleep(2000);
							return delay;
	
						}
					}
				}
				/**
				 * If module doesn't materials to continue:
				 */
				else {
					script.nextModule();
					script.sleep(2000);
					return delay;
				}
				
				script.getBank().close();
				script.sleep(random.nextInt(750)+ 500);
				script.getMouse().move();
				this.actionsCompleted++;
			}
			
			else if(!location.inArea()) {
				script.setReact(0);
				script.setInfoText("Smelter: Walking to Furnace - " + locationEnum);
				
				location.travel();
					
			}
			else if(actionTester != script.getInventory().count(f -> f != null && f.getName().toLowerCase().contains("bar"))) {
				this.actionTester = script.getInventory().count(f -> f != null && f.getName().toLowerCase().contains("bar"));
			}
			else if(!script.getLocalPlayer().isAnimating()) {
				script.setReact(1);
				script.setInfoText("Smelter: Smelting - " + barEnum);
				GameObject furnace = script.getGameObjects().closest(f -> f != null && f.getName().toLowerCase().contains("furnace"));
				int tester = random.nextInt(2);
				if(tester == 0) {
					furnace.interact("Smelt");
					script.getMouse().move();
				}
				else {
					furnace.interact();
					script.getMouse().move();
				}
				script.sleep(random.nextInt(750)+ 1000);
				script.getKeyboard().type(bar.getKeyboardNumber(),false);
				
				
			}
		}
		return delay;
	}

	@Override
	public int actionsCompleted() {
		
		return this.actionsCompleted;
	}


	public boolean setupModule() {
		this.location.teleportToLocation();
		
		script.setReact(0);
		script.setInfoText("Smelter: Setting Up Module");
		if(!(script.getInventory().count(f -> f != null && f.getName().contains(bar.getOreName1())) >= bar.getOreCost1()) ||
				!(script.getInventory().count(f -> f != null && f.getName().contains(bar.getOreName2())) >= bar.getOreCost2())	) {
			
			if(!script.getWalking().isRunEnabled() && script.getWalking().getRunEnergy() > 0) {
				script.getWalking().toggleRun();
			}

			while(!script.getBank().isOpen()) {
				script.getBank().open(script.getBank().getClosestBankLocation());
				script.sleep(random.nextInt(1000)+2000);
			}
			
			if(!script.getInventory().isEmpty()) {
				script.getBank().depositAllExcept(f -> f != null && f.getName().equals("Ammo mould"));
				script.sleep(random.nextInt(750)+ 1000);
			}
			
			if(script.getBank().count(f -> f != null && f.getName().contains(bar.getOreName1())) >= bar.getOre1InBack()) {
				script.getBank().withdraw(f -> f != null && f.getName().contains(this.bar.getOreName1()), this.bar.getOre1InBack());
				script.sleep(random.nextInt(750)+ 500);
	
					if(script.getBank().count(f -> f != null && f.getName().contains(bar.getOreName2())) >= bar.getOre2InBack()) {
							script.getBank().withdraw(f -> f != null && f.getName().contains(this.bar.getOreName2()), this.bar.getOre2InBack());
					}
					else {
						return false;
					}
				
			}
			else {
				return false;
			}

			
			script.getBank().close();
			script.sleep(random.nextInt(750)+ 500);
			script.getMouse().move();
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
	public void errorTest() {
		// TODO Auto-generated method stub
		
	}
}
