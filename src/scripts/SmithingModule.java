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


public class SmithingModule extends ScriptModule {
	private ClientThread script;
	private ThreadController controller;

	private int barsRequired;
	private int actionsCompleted;
	private int delay;
	private int actionTester;
	private int limit;
	
	private boolean firsInteract;
	private boolean firstInteractSupport;
	private boolean widgetSet;
	private boolean error;
	private boolean killThread;
	
	private LocationFactory.GameLocation locationEnum;
	
	private SmithingMaterial materialEnum;
	private SmithingType type;
	
	private String interactWith;
	private String barText;
	
	private WidgetChild widget;
	
	public SmithingModule(ThreadController controller, ClientThread script, SmithingModule.SmithingType type, SmithingModule.SmithingMaterial material, LocationFactory.GameLocation location, int limit) {
		this.script = script;
		this.controller = controller;
		
		this.type = type;
		this.barText = getTextFromMaterial(material);
		this.getBarsRequired(this.type);
		
		this.widget = null;
		
		this.firsInteract = false;
		this.widgetSet = true;
		this.firstInteractSupport = true;
		this.error = false;
		
		this.actionTester = 0;
		this.limit = limit;
		
		this.locationEnum = location;
		this.materialEnum = material;
		
		this.moduleName = "SmithingModule: " + materialEnum + " - " + this.type;
	}
	
	@Override
	public void run()  {
		
		while(!this.killThread) {
			
			RandomProvider.sleep(1500, 2200);
		
			if(!script.getLocalPlayer().isAnimating()) {
				if(script.getInventory().count(f -> f != null && f.getName().equals(barText)) < barsRequired) {
					
					controller.getGraphicHandler().setInfo("Smither: Inventory Done - Banking");
					
					
					
					controller.getMovementHandler().moveToBank();
					
					while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
					while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
					
					script.getBank().depositAllExcept(f -> f != null && f.getName().toLowerCase().equals("hammer") || f.getName().toLowerCase().equals(barText));
					RandomProvider.sleep(1000, 1750);
					
					if(script.getBank().count(f -> f != null && f.getName().equals(barText)) > barsRequired) {
						RandomProvider.sleep(500, 750);
						
						script.getBank().withdrawAll(f -> f != null && f.getName().equals(barText));
					}
					else {
						this.killThread = true;
						
						script.sleep(2000);
						controller.debug("Ouf of Bars");
					}
					RandomProvider.sleep(700, 1450);
					
					script.getBank().close();
					
					RandomProvider.sleep(500, 1250);
	
					script.getMouse().move();
					
					this.actionsCompleted++;
					
					controller.returnKeyboardAccess();
					controller.returnMouseAccess();
	
				}
			
				else if(!controller.getMovementHandler().isPlayerInLocation()) {
					
					controller.getGraphicHandler().setInfo("Smither: Walking to Anvil - " + locationEnum);
					
					controller.getMovementHandler().moveToLocation();
	
						
				}
				
				else if(actionTester != script.getInventory().count(f -> f != null && f.getName().toLowerCase().contains("bar"))) {
					this.actionTester = script.getInventory().count(f -> f != null && f.getName().toLowerCase().contains("bar"));
				}
				
				else if(!script.getLocalPlayer().isAnimating()) {
							
					if(!script.getInventory().contains(f -> f != null && f.getName().toLowerCase().contains("hammer"))) {
						controller.debug("Smither - Module ERROR");
						controller.debug("Trying to Restart Module...");
						if(!setupModule() || error) {
							controller.debug("Jeweller - Module ERROR");
							controller.debug("Changing Module.");
							this.killThread = true;
						}
						controller.debug("Module Restart Complete");
						this.error = true;
					}
					
				
					controller.getGraphicHandler().setInfo("Smither: Smithing - " + materialEnum + " - " + this.type);
					GameObject anvil = script.getGameObjects().closest(f -> f != null && f.getName().toLowerCase().contains("anvil"));
					int tester = RandomProvider.randomInt(2);
					
					while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
					while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
					
					if(anvil != null) {
					
						if(RandomProvider.fiftyfifty()) {
							script.getCamera().rotateToEntity(anvil);
							RandomProvider.sleep(750, 1000);
						}
						
						if(tester == 0) {
							anvil.interact("Smith");
							script.getMouse().move();
						}
						else {
							anvil.interact();
							script.getMouse().move();
						}
						
						RandomProvider.sleep(1300, 1600);
						this.interactWithWidget();
						
						/*
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
		
							RandomProvider.sleep(1500, 2500);
							if(interactWith != null) {
								widget.interact(interactWith);
							}
							else {
								this.setWidget();
							}
							script.getMouse().move();
						}*/
					}
					
					controller.returnKeyboardAccess();
					controller.returnMouseAccess();
					
					this.firsInteract = true;
						
					
				}
			}
		
		}
		
	}
		


	@Override
	public boolean setupModule() {
		controller.getMovementHandler().newLocation(this.locationEnum);
		
		controller.getMovementHandler().teleportToLocation();
		
		controller.getGearHandler().handleGearSwap(Gear.UTILITY); 
		
		controller.getGraphicHandler().setInfo("Smither: Setting Up Module");
		
		if(!script.getInventory().contains(f -> f != null && f.getName().toLowerCase().contains("hammer") )) {
			
			controller.getMovementHandler().locateBank();
			
			while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
			while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
			
			controller.debug("Mouse control: SmithingModule");
			controller.debug("Keyboard control: SmithingModule");
			
			if(!script.getInventory().isEmpty()) {
				script.getBank().depositAllItems();	
			}
			
			RandomProvider.sleep(500, 1250);
			if(script.getBank().contains(f -> f != null && f.getName().toLowerCase().contains("hammer") )){
				script.getBank().withdraw(f -> f != null && f.getName().toLowerCase().contains("hammer"));
				RandomProvider.sleep(1000, 1750);
				
				controller.returnKeyboardAccess();
				controller.returnMouseAccess();
				return true;
			}
			else {
				
				controller.returnKeyboardAccess();
				controller.returnMouseAccess();
				return false;
			}
		}
		else {
			return true;
		}
	}
	
	public String getTextFromMaterial(SmithingModule.SmithingMaterial material) {
		if(material == SmithingMaterial.BRONZE) {
			return "Bronze bar";
		}
		else if(material == SmithingMaterial.IRON) {
			return "Iron bar";
		}
		else if(material == SmithingMaterial.STEEL) {
			return "Steel bar";
		}
		else if(material == SmithingMaterial.MITHRIL) {
			return "Mithril bar";
		}
		else if(material == SmithingMaterial.ADAMANTITE) {
			return "Adamantite bar";
		}
		else if(material == SmithingMaterial.RUNE) {
			return "Runite bar";
		}
		else {
			return null;
		}
	}
	
	public enum SmithingMaterial{
		BRONZE, IRON, STEEL, ADAMANTITE, MITHRIL, RUNE
	}
	public enum SmithingType{
		PLATEBODY, FULL_HELM, PLATELEGS, SCIMITAR, NAILS, ARROW_HEAD, KITE_SHIELD, KNIVES
	}
	

		
		public void getBarsRequired(SmithingModule.SmithingType type) {
			if(type == SmithingType.PLATEBODY) {
				this.barsRequired = 5;
			}
			else if(type == SmithingType.PLATELEGS) {
				this.barsRequired = 3;
			}
			else if(type == SmithingType.NAILS) {
				this.barsRequired = 1;
			}
			else if(type == SmithingType.SCIMITAR) {
				this.barsRequired = 2;
			}
			else if(type == SmithingType.FULL_HELM) {
				this.barsRequired = 2;
			}
			else if(type == SmithingType.ARROW_HEAD) {
				this.barsRequired = 1;
			}
			else if(type == SmithingType.KITE_SHIELD) {
				this.barsRequired = 3;
			}
			else if(type == SmithingType.KNIVES) {
				this.barsRequired = 1;
			}
		}
		
		public void interactWithWidget() {
			int failsafe = 0;
			this.widget = null;
			
			while(!killThread && failsafe < 5) {
				RandomProvider.sleep(1000, 1250);
				failsafe++;
				
				try {
					if(type == SmithingType.PLATEBODY) {
						this.widget = script.getWidgets().getWidget(312).getChild(22);
						this.interactWith = "Smith";
						
					}
					else if(type == SmithingType.PLATELEGS) {
						this.widget = script.getWidgets().getWidget(312).getChild(20);
						this.interactWith = "Smith";
					
		
					}
					else if(type == SmithingType.NAILS) {
						this.widget = script.getWidgets().getWidget(312).getChild(23);
						this.interactWith = "Smith set";
					
					
					}
					else if(type == SmithingType.SCIMITAR) {
						this.widget = script.getWidgets().getWidget(312).getChild(11);
						this.interactWith = "Smith";
					
						
					}
					else if(type == SmithingType.FULL_HELM) {
						this.widget = script.getWidgets().getWidget(312).getChild(25);
						this.interactWith = "Smith";
					
						
					}
					else if(type == SmithingType.ARROW_HEAD) {
						this.widget = script.getWidgets().getWidget(312).getChild(30);
						this.interactWith = "Smith set";
						
						
					}
					else if(type == SmithingType.KITE_SHIELD) {
						this.widget = script.getWidgets().getWidget(312).getChild(27);
						this.interactWith = "Smith";
					
						
					}
					else if(type == SmithingType.KNIVES) {
						this.widget = script.getWidgets().getWidget(312).getChild(31);
						this.interactWith = "Smith set";
					
					}
				}
				catch(Exception e) {}
				if(this.widget != null) {
					widget.interact();
					RandomProvider.sleep(500, 750);
					if(RandomProvider.fiftyfifty()) {
						script.getMouse().move();
						RandomProvider.sleep(500, 750);
					}
					else {
						script.getMouse().moveMouseOutsideScreen();
						RandomProvider.sleep(500, 750);
					}
					return;
				}
			
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
			if((this.limit <= this.actionsCompleted)) {
				return true;
			}
			else {
				return false;
			}
		}
		
		
	

}
