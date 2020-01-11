package scripts;
import java.util.Random;

import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.widget.Widget;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import init.MainLooper;
import movement.Location;
import movement.Locations;

public class SmithingModule extends ScriptModule {
	private MainLooper script;
	private Random random;
	private int delay;
	private Location location;
	private String barText;
	private int actionsCompleted;
	private WidgetChild widget;
	private int barsRequired;
	private SmithingType type;
	private boolean firsInteract;
	private boolean firstInteractSupport;
	private boolean widgetSet;
	private int actionTester;
	private Locations locationEnum;
	private SmithingMaterial materialEnum;
	private String interactWith;
	private boolean error;
	
	
	public SmithingModule(SmithingModule.SmithingType type, SmithingModule.SmithingMaterial material, Locations location, MainLooper script) {
		this.type = type;
		this.barText = getTextFromMaterial(material);
		this.script = script;
		this.location = new Location(script, location);
		this.random = new Random();
		this.getBarsRequired(this.type);
		this.widget = null;
		this.firsInteract = false;
		this.widgetSet = true;
		actionTester = 0;
		firstInteractSupport = true;
		locationEnum = location;
		this.materialEnum = material;
		this.error = false;
		this.moduleName = "SmithingModule: " + materialEnum + " - " + this.type;
	}
	
	@Override
	public int onLoop() {
		random.setSeed(random.nextLong());
		delay = random.nextInt(1000) + 2000;
		
		if(!script.getLocalPlayer().isAnimating()) {
			if(script.getInventory().count(f -> f != null && f.getName().equals(barText)) < barsRequired) {
				script.setReact(0);
				script.setInfoText("Smither: Inventory Done - Banking");
				this.actionsCompleted++;
				
				location.travelToBank();
				
				script.getBank().depositAllExcept(f -> f != null && f.getName().toLowerCase().equals("hammer") || f.getName().toLowerCase().equals(barText));
				script.sleep(random.nextInt(750)+ 1000);
				
				script.log(""+(script.getBank().count(f -> f != null && f.getName().equals(barText)) > barsRequired));
				script.log(""+(script.getBank().count(f -> f != null && f.getName().equals(barText))));
				
				if(script.getBank().count(f -> f != null && f.getName().equals(barText)) > barsRequired) {
					script.sleep(random.nextInt(750)+ 1000);
					script.getBank().withdrawAll(f -> f != null && f.getName().equals(barText));
				}
				else {
					script.nextModule();
					script.sleep(2000);
					script.log("Ouf of Bars");
					return delay;
				}
				script.sleep(random.nextInt(750)+ 700);
				script.getBank().close();
				script.sleep(random.nextInt(750)+ 500);
				script.getMouse().move();

			}
		
			else if(!location.inArea()) {
				script.setReact(0);
				script.setInfoText("Smither: Walking to Anvil - " + locationEnum);
				
				location.travel();
					
			}
			
			else if(actionTester != script.getInventory().count(f -> f != null && f.getName().toLowerCase().contains("bar"))) {
				this.actionTester = script.getInventory().count(f -> f != null && f.getName().toLowerCase().contains("bar"));
			}
			
			else if(!script.getLocalPlayer().isAnimating()) {
				
				
				if(!script.getInventory().contains(f -> f != null && f.getName().toLowerCase().contains("hammer"))) {
					script.getMessenger().sendMessage("Smither - Module ERROR");
					script.getMessenger().sendMessage("Trying to Restart Module...");
					if(!setupModule() || error) {
						script.getMessenger().sendMessage("Jeweller - Module ERROR");
						script.getMessenger().sendMessage("Changing Module.");
						script.nextModule();
					}
					script.getMessenger().sendMessage("Module Restart Complete");
					this.error = true;
				}
				
				script.setReact(1);
				script.setInfoText("Smither: Smithing - " + materialEnum + " - " + this.type);
				GameObject anvil = script.getGameObjects().closest(f -> f != null && f.getName().toLowerCase().contains("anvil"));
				int tester = random.nextInt(2);
				
				if(tester == 0) {
					anvil.interact("Smith");
					script.getMouse().move();
				}
				else {
					anvil.interact();
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


					script.sleep(random.nextInt(1000)+ 1500);
					if(interactWith != null) {
						widget.interact(interactWith);
					}
					else {
						this.setWidget();
					}
					script.getMouse().move();
				}
				

				this.firsInteract = true;
					
				
			}
		}
		return delay;
	}
		
	

	@Override
	public int actionsCompleted() {
		return this.actionsCompleted;
	}

	@Override
	public boolean setupModule() {
		this.location.teleportToLocation();
		
		script.setReact(0);
		script.setInfoText("Smither: Setting Up Module");
		if(!script.getInventory().contains(f -> f != null && f.getName().toLowerCase().contains("hammer") )) {
			if(!script.getWalking().isRunEnabled() && script.getWalking().getRunEnergy() > 0) {
				script.getWalking().toggleRun();
			}

			while(!script.getBank().isOpen()) {
				script.getBank().open(script.getBank().getClosestBankLocation());
				script.sleep(random.nextInt(1000)+2000);
			}
			if(!script.getInventory().isEmpty()) {
				script.getBank().depositAllItems();	
				
			}
			script.sleep(random.nextInt(750)+ 500);
			if(script.getBank().contains(f -> f != null && f.getName().toLowerCase().contains("hammer") )){
				script.getBank().withdraw(f -> f != null && f.getName().toLowerCase().contains("hammer"));
				script.sleep(random.nextInt(750)+ 1000);
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
			script.stop();
			script.log("Setup ERROR");
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
		
		public boolean setWidget() {
			script.sleep(random.nextInt(750)+ 3000);
			if(this.widget != null) {
				return true;
			}
			try {
				if(type == SmithingType.PLATEBODY) {
					this.widget = script.getWidgets().getWidget(312).getChild(22);
					this.interactWith = "Smith";
					return true;
					
				}
				else if(type == SmithingType.PLATELEGS) {
					this.widget = script.getWidgets().getWidget(312).getChild(20);
					this.interactWith = "Smith";
					return true;
	
				}
				else if(type == SmithingType.NAILS) {
					this.widget = script.getWidgets().getWidget(312).getChild(23);
					this.interactWith = "Smith set";
					return true;
				
				}
				else if(type == SmithingType.SCIMITAR) {
					this.widget = script.getWidgets().getWidget(312).getChild(11);
					this.interactWith = "Smith";
					return true;
					
				}
				else if(type == SmithingType.FULL_HELM) {
					this.widget = script.getWidgets().getWidget(312).getChild(25);
					this.interactWith = "Smith";
					return true;
					
				}
				else if(type == SmithingType.ARROW_HEAD) {
					this.widget = script.getWidgets().getWidget(312).getChild(30);
					this.interactWith = "Smith set";
					return true;
					
				}
				else if(type == SmithingType.KITE_SHIELD) {
					this.widget = script.getWidgets().getWidget(312).getChild(27);
					this.interactWith = "Smith";
					return true;
					
				}
				else if(type == SmithingType.KNIVES) {
					this.widget = script.getWidgets().getWidget(312).getChild(31);
					this.interactWith = "Smith set";
					return true;
					
				}
			}
			catch(Exception e) {
				return false;
			}
			return false;
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
