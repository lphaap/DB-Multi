package scripts;
import java.util.Random;

import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import antiban.RandomProvider;
import client.ClientThread;
import client.ThreadController;
import movement.Location;
import movement.LocationFactory;
import utilities.GearHandler.Gear;



public class JewelleryModule extends ScriptModule {
	private ClientThread script;
	private ThreadController controller;
	
	private String mouldName;
	private final String barName = "Gold bar";
	private String gemName;
	
	private int limit;
	private int gemBankNum;
	private int barBankNum;
	private int actionTester;
	private int typeKeyNumber;
	private int actionsCompleted;
	
	private JewelleryMaterial materialEnum;
	private JewelleryType typeEnum;
	private LocationFactory.GameLocation locationEnum;
	
	private boolean firsInteract;
	private boolean killThread;
	private boolean widgetSet;
	private boolean error;
	
	private WidgetChild widget;

	public JewelleryModule(ThreadController controller, ClientThread script, LocationFactory.GameLocation location,
							JewelleryModule.JewelleryMaterial material, JewelleryModule.JewelleryType type, int limit) {
		
		this.script = script;
		this.controller = controller;
		
		this.locationEnum = location;
		this.materialEnum = material;
		this.typeEnum = type;
		
		this.firsInteract = false;
		this.widgetSet = true;
		this.error = false;
		
		this.moduleName = "JewelleryModule: " + this.materialEnum + " - " + this.typeEnum;
		
		this.widget = null;
		
		this.limit = limit;
		
		setupMouldName(type);
		setupGemNameAndReq(material);
	}
	
	
	@Override
	public void run() {
		
		while(!killThread) {
			
			RandomProvider.sleep(2000, 3000);
			
			if(!script.getLocalPlayer().isAnimating()) {
				
				if( !(script.getInventory().contains(f -> f != null && f.getName().contains(barName))) ||
					!(script.getInventory().contains(f -> f != null && f.getName().contains(gemName))) ) {
					
					this.actionTester = 0;
					
					controller.getGraphicHandler().setInfo("Jeweller: Inventory Full Banking");
					
					controller.getMovementHandler().moveToBank();
					
					while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
					while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
					
					if(!script.getInventory().isEmpty()) {
						script.getBank().depositAllExcept(f -> f != null && f.getName().equals(mouldName));	
					}
					
					if(script.getBank().count(f -> f != null && f.getName().equals(barName)) >= this.barBankNum) {
						if(script.getBank().count(f -> f != null && f.getName().equals(this.gemName)) >= this.gemBankNum) {
							int randomizer = RandomProvider.randomInt(2);
							if(randomizer == 1) {
								script.getBank().withdraw(f -> f != null && f.getName().equals(barName), this.barBankNum);
								RandomProvider.sleep(500, 1250);
								script.getBank().withdraw(f -> f != null && f.getName().equals(gemName), this.gemBankNum);
							}
							else {
								script.getBank().withdraw(f -> f != null && f.getName().equals(gemName), this.gemBankNum);
								RandomProvider.sleep(500, 12500);
								script.getBank().withdraw(f -> f != null && f.getName().equals(barName), this.barBankNum);
							}
	
						}
						/**
						 * If module doesn't materials to continue:
						 */
						else {
							controller.returnKeyboardAccess();
							controller.returnMouseAccess();
							this.killThread = true;
						}
					}
					/**
					* If module doesn't materials to continue:
					*/
					else {
						controller.returnKeyboardAccess();
						controller.returnMouseAccess();
						this.killThread = true;
					}
					
		
					script.getBank().close();
					RandomProvider.sleep(500, 1250);
					script.getMouse().move();
					this.actionsCompleted++;
					
					controller.returnKeyboardAccess();
					controller.returnMouseAccess();
				}
				
				else if(!this.controller.getMovementHandler().isPlayerInLocation()) {
					controller.getGraphicHandler().setInfo("Jeweller: Walking to Furnace - " + locationEnum);
					
					controller.getMovementHandler().moveToLocation();
				}
				
				else if(actionTester != script.getInventory().count(f -> f != null && f.getName().equals(barName))) {
					this.actionTester = script.getInventory().count(f -> f != null && f.getName().equals(barName));
				}
				
				else if(!script.getLocalPlayer().isAnimating()) {
					
					if(!script.getInventory().contains(f -> f != null && f.getName().equals(mouldName))) {
						controller.debug("Jeweller - Module ERROR");
						controller.debug("Trying to Restart Module...");
						if(!setupModule() || error) {
							controller.debug("Jeweller - Module ERROR");
							controller.debug("Changing Module.");
							this.killThread = true;
						}
						controller.debug("Restart Completed");
						this.error = true;
					}
					
					while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
					while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
					
					controller.getGraphicHandler().setInfo("Jewellery: Smelting Jeweles - " + this.materialEnum + " - " + this.typeEnum);
					GameObject furnace = script.getGameObjects().closest(f -> f != null && f.getName().toLowerCase().contains("furnace"));
					int tester = RandomProvider.randomInt(2);
					if(tester == 0) {
						furnace.interact("Smelt");
						script.getMouse().move();
					}
					else {
						furnace.interact();
						script.getMouse().move();
					}
					if(this.firsInteract) {
						int failSafe = 0;
						if(this.widgetSet) {
							while(!this.setWidget() || failSafe > 5){
								failSafe++;	
							}
							if(failSafe < 5) {
								widgetSet = false;
							}
							
						}
	
						RandomProvider.sleep(1400, 2600);
						widget.interact();
						script.getMouse().move();
					}
					
					controller.returnKeyboardAccess();
					controller.returnMouseAccess();
					
					this.firsInteract = true;
					
				}
			}
		}


	}
	
	public enum JewelleryMaterial{
		GOLD, SAPPHIRE, EMERALD, RUBY, DIAMOND, DRAGONSTONE
	}
	
	public enum JewelleryType{
		RING, NECKLACE, BRACELET, AMULET
	}
	
	public void setupMouldName(JewelleryType type) {
		if(type == JewelleryType.RING) {
			this.mouldName = "Ring mould";
		}
		else if(type == JewelleryType.NECKLACE) {
			this.mouldName = "Necklace mould";
		}
		else if(type == JewelleryType.BRACELET) {
			this.mouldName = "Bracelet mould";
		}
		else if(type == JewelleryType.AMULET) {
			this.mouldName = "Amulet mould";
		}
	}
	
	public void setupGemNameAndReq(JewelleryMaterial material) {
		if(material == JewelleryMaterial.GOLD) {
			this.gemName = "";
			this.gemBankNum = -1;
			this.barBankNum = 27;
		}
		else if(material == JewelleryMaterial.SAPPHIRE) {
			this.gemName = "Sapphire";
			this.gemBankNum = 13;
			this.barBankNum = 13;
		}
		else if(material == JewelleryMaterial.EMERALD) {
			this.gemName = "Emerald";
			this.gemBankNum = 13;
			this.barBankNum = 13;
		}
		else if(material == JewelleryMaterial.RUBY) {
			this.gemName = "Ruby";
			this.gemBankNum = 13;
			this.barBankNum = 13;
		}
		else if(material == JewelleryMaterial.DIAMOND) {
			this.gemName = "Diamond";
			this.gemBankNum = 13;
			this.barBankNum = 13;
		}
		else if(material == JewelleryMaterial.DRAGONSTONE) {
			this.gemName = "Dragonstone";
			this.gemBankNum = 13;
			this.barBankNum = 13;
		}
	}

	@Override
	public boolean setupModule() {
		controller.getMovementHandler().newLocation(this.locationEnum);
		
		controller.getMovementHandler().teleportToLocation();
		
		controller.getGearHandler().handleGearSwap(Gear.UTILITY);
		
		controller.getGraphicHandler().setInfo("Jeweller: Setting Up Module");
			
		while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
		while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
		
			if(!script.getWalking().isRunEnabled() && script.getWalking().getRunEnergy() > 0) {
				script.getWalking().toggleRun();
			}

			while(!script.getBank().isOpen()) {
				script.getBank().open(script.getBank().getClosestBankLocation());
				RandomProvider.sleep(2000, 3000);
			}
			
			if(!script.getInventory().isEmpty()) {
				script.getBank().depositAllItems();
				RandomProvider.sleep(1000, 1750);
			}

			
			if(script.getBank().contains(f -> f != null && f.getName().equals(mouldName))) {
				script.getBank().withdraw(f -> f != null && f.getName().equals(mouldName));
				RandomProvider.sleep(500, 1250);
			}
			else {
				controller.returnKeyboardAccess();
				controller.returnMouseAccess();
				return false;
			}
			
			if(script.getBank().count(f -> f != null && f.getName().equals(barName)) >= barBankNum) {
				script.getBank().withdraw(f -> f != null && f.getName().equals(barName), this.barBankNum);
				RandomProvider.sleep(500, 1250);
				if(script.getBank().count(f -> f != null && f.getName().equals(gemName)) >= gemBankNum) {
					script.getBank().withdraw(f -> f != null && f.getName().equals(gemName), gemBankNum);
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

	@Override
	public Skill getSkillToHover() {
		return Skill.CRAFTING;
	}
	
	public boolean setWidget() {
		RandomProvider.sleep(3000, 3750);
		if(this.widget != null) {
			return true;
		}
		try {
			if(typeEnum == JewelleryType.RING) {
				if(materialEnum == JewelleryMaterial.GOLD) {
					this.widget = script.getWidgets().getWidget(446).getChild(7);
				}
				else if(materialEnum == JewelleryMaterial.SAPPHIRE) {
					this.widget = script.getWidgets().getWidget(446).getChild(8);
				}
				else if(materialEnum == JewelleryMaterial.EMERALD) {
					this.widget = script.getWidgets().getWidget(446).getChild(9);
				}
				else if(materialEnum == JewelleryMaterial.RUBY) {
					this.widget = script.getWidgets().getWidget(446).getChild(10);
				}
				else if(materialEnum == JewelleryMaterial.DIAMOND) {
					this.widget = script.getWidgets().getWidget(446).getChild(11);
				}
				else if(materialEnum == JewelleryMaterial.DRAGONSTONE) {
				}
				
			}
			else if(typeEnum == JewelleryType.NECKLACE) {
				if(materialEnum == JewelleryMaterial.GOLD) {
					this.widget = script.getWidgets().getWidget(446).getChild(21);
				}
				else if(materialEnum == JewelleryMaterial.SAPPHIRE) {
					this.widget = script.getWidgets().getWidget(446).getChild(22);
				}
				else if(materialEnum == JewelleryMaterial.EMERALD) {
					this.widget = script.getWidgets().getWidget(446).getChild(23);
				}
				else if(materialEnum == JewelleryMaterial.RUBY) {
					this.widget = script.getWidgets().getWidget(446).getChild(24);
				}
				else if(materialEnum == JewelleryMaterial.DIAMOND) {
					this.widget = script.getWidgets().getWidget(446).getChild(25);
				}
				else if(materialEnum == JewelleryMaterial.DRAGONSTONE) {
				}
			}
			else if(typeEnum == JewelleryType.AMULET) {
				if(materialEnum == JewelleryMaterial.GOLD) {
					this.widget = script.getWidgets().getWidget(446).getChild(34);
				}
				else if(materialEnum == JewelleryMaterial.SAPPHIRE) {
					this.widget = script.getWidgets().getWidget(446).getChild(35);
				}
				else if(materialEnum == JewelleryMaterial.EMERALD) {
					this.widget = script.getWidgets().getWidget(446).getChild(36);
				}
				else if(materialEnum == JewelleryMaterial.RUBY) {
					this.widget = script.getWidgets().getWidget(446).getChild(37);
				}
				else if(materialEnum == JewelleryMaterial.DIAMOND) {
					this.widget = script.getWidgets().getWidget(446).getChild(38);
				}
				else if(materialEnum == JewelleryMaterial.DRAGONSTONE) {
				}
			}
			else if(typeEnum == JewelleryType.BRACELET) {
				if(materialEnum == JewelleryMaterial.GOLD) {
				}
				else if(materialEnum == JewelleryMaterial.SAPPHIRE) {
				}
				else if(materialEnum == JewelleryMaterial.EMERALD) {
				}
				else if(materialEnum == JewelleryMaterial.RUBY) {
				}
				else if(materialEnum == JewelleryMaterial.DIAMOND) {
				}
				else if(materialEnum == JewelleryMaterial.DRAGONSTONE) {
				}
			}

		}
		catch(Exception e) {
			return false;
		}
		return false;
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
		if(limit <= actionsCompleted) {
			return true;
		}
		else {
			return false;
		}
	}

}
