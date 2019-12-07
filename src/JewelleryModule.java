import java.util.Random;

import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.widgets.WidgetChild;



public class JewelleryModule extends ScriptModule {
	private MainLooper script;
	private int actionsCompleted;
	private Location location;
	private Random random;
	private int delay;
	private int actionTester;
	private Locations locationEnum;
	private String mouldName;
	private final String barName = "Gold bar";
	private int typeKeyNumber;
	private String gemName;
	private int gemBankNum;
	private int barBankNum;
	private JewelleryMaterial materialEnum;
	private JewelleryType typeEnum;
	private boolean firsInteract;
	private boolean widgetSet;
	private WidgetChild widget;
	private boolean error;

	public JewelleryModule(MainLooper script, Locations location,
							JewelleryModule.JewelleryMaterial material, JewelleryModule.JewelleryType type) {
		this.locationEnum = location;
		this.materialEnum = material;
		this.typeEnum = type;
		this.script = script;
		this.location = new Location(script, location);
		this.widget = null;
		this.firsInteract = false;
		this.widgetSet = true;
		this.random = new Random();
		setupMouldName(type);
		setupGemNameAndReq(material);
		this.error = false;
		this.moduleName = "JewelleryModule: " + this.materialEnum + " - " + this.typeEnum;
	}
	
	@Override
	public int onLoop() {
		random.setSeed(random.nextLong());
		delay = random.nextInt(1000) + 2000;
		
		if(!script.getLocalPlayer().isAnimating()) {
			
			if( !(script.getInventory().contains(f -> f != null && f.getName().contains(barName))) ||
				!(script.getInventory().contains(f -> f != null && f.getName().contains(gemName))) ) {
				
				script.setReact(0);
				this.actionTester = 0;
				script.setInfoText("Jeweller: Inventory Full Banking");
				
				location.travelToBank();
				
				if(!script.getInventory().isEmpty()) {
					script.getBank().depositAllExcept(f -> f != null && f.getName().equals(mouldName));	
				}
				
				if(script.getBank().count(f -> f != null && f.getName().equals(barName)) >= this.barBankNum) {
					if(script.getBank().count(f -> f != null && f.getName().equals(this.gemName)) >= this.gemBankNum) {
						int randomizer = random.nextInt(2);
						if(randomizer == 1) {
							script.getBank().withdraw(f -> f != null && f.getName().equals(barName), this.barBankNum);
							script.sleep(random.nextInt(750)+ 500);
							script.getBank().withdraw(f -> f != null && f.getName().equals(gemName), this.gemBankNum);
						}
						else {
							script.getBank().withdraw(f -> f != null && f.getName().equals(gemName), this.gemBankNum);
							script.sleep(random.nextInt(750)+ 500);
							script.getBank().withdraw(f -> f != null && f.getName().equals(barName), this.barBankNum);
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
				script.setInfoText("Jeweller: Walking to Furnace - " + locationEnum);
				
				location.travel();
			}
			
			else if(actionTester != script.getInventory().count(f -> f != null && f.getName().equals(barName))) {
				this.actionTester = script.getInventory().count(f -> f != null && f.getName().equals(barName));
			}
			
			else if(!script.getLocalPlayer().isAnimating()) {
				
				if(!script.getInventory().contains(f -> f != null && f.getName().equals(mouldName))) {
					script.getMessenger().sendMessage("Jeweller - Module ERROR");
					script.getMessenger().sendMessage("Trying to Restart Module...");
					if(!setupModule() || error) {
						script.getMessenger().sendMessage("Jeweller - Module ERROR");
						script.getMessenger().sendMessage("Changing Module.");
						script.nextModule();
					}
					script.getMessenger().sendMessage("Restart Completed");
					this.error = true;
				}
				
				script.setReact(1);
				script.setInfoText("Jewellery: Smelting Jeweles - " + this.materialEnum + " - " + this.typeEnum);
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


					script.sleep(random.nextInt(1200)+ 1400);
					widget.interact();
					script.getMouse().move();
				}
				

				this.firsInteract = true;
				
			}
		}
		return delay;

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
	public int actionsCompleted() {
		return this.actionsCompleted;
	}

	@Override
	public boolean setupModule() {
		this.location.teleportToLocation();
		
		script.setReact(0);
		script.setInfoText("Jewellery: Setting Up Module");
			
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

			
			if(script.getBank().contains(f -> f != null && f.getName().equals(mouldName))) {
				script.getBank().withdraw(f -> f != null && f.getName().equals(mouldName));
				script.sleep(random.nextInt(750)+ 500);
			}
			else {
				return false;
			}
			
			if(script.getBank().count(f -> f != null && f.getName().equals(barName)) >= barBankNum) {
				script.getBank().withdraw(f -> f != null && f.getName().equals(barName), this.barBankNum);
				script.sleep(random.nextInt(750)+ 500);
				if(script.getBank().count(f -> f != null && f.getName().equals(gemName)) >= gemBankNum) {
					script.getBank().withdraw(f -> f != null && f.getName().equals(gemName), gemBankNum);
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

	@Override
	public Skill getSkillToHover() {
		return Skill.CRAFTING;
	}
	
	public boolean setWidget() {
		script.sleep(random.nextInt(750)+ 3000);
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
	public void errorTest() {
		// TODO Auto-generated method stub
		
	}

}
