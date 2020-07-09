package scripts;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.magic.Normal;
import org.dreambot.api.methods.magic.Spell;
import org.dreambot.api.methods.magic.cost.Rune;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import antiban.RandomProvider;
import client.ClientThread;
import client.ThreadController;
import movement.Location;
import movement.LocationFactory;
import movement.LocationFactory.GameLocation;
import utilities.GearHandler.Gear;

public class MageTrainerModule extends ScriptModule{
	private ClientThread script;
	private ThreadController controller;
	
	private int limit;
	private int actionsCompleted;
	
	private NPC target;
	private LocationFactory.GameLocation locationEnum;
	
	private Spell alchemy;
	private Spell curse;
	private Curse curseEnum;
	
	private WidgetChild curseArea;
	private WidgetChild alchemyArea;
	
	private Point alchemyPoint;
	private Point cursePoint;
	private Point itemPoint;
	
	private String itemName;
	private String targetName;
	
	private boolean error;
	private boolean killThread;
	private boolean randomized;
	private boolean trainAlchemy;
	
	private Rectangle itemArea;
	
	
	public MageTrainerModule(ThreadController controller, ClientThread script, int limit, MageTrainerModule.Curse curse, 
							 boolean trainAlchemy, MageTrainerModule.alchemyItem item) {
		
		this.script = script;
		this.controller = controller;
		
		this.trainAlchemy = trainAlchemy;
		
		this.limit = limit;
		this.actionsCompleted = 0;
		
		this.alchemy = Normal.HIGH_LEVEL_ALCHEMY;
		
		this.alchemyArea = script.getWidget(218, 38);
		
		this.moduleName = "MageTrainerModule";
		this.targetName = "Grizzly bear";
		
		this.locationEnum = GameLocation.SPLASHING_BEAR;

		setCurseVariables(curse);
		setItemName(item);
		
	}
	
	@Override
	public void run() {
		
		while(!killThread) {
		
			RandomProvider.sleep(100, 200);
	
			if(!controller.getMovementHandler().isPlayerInLocation()) {
				controller.getMovementHandler().moveToLocation();
			}
			else {
				if(trainAlchemy) {
					itemPoint = new Point(RandomProvider.randomInt((int)itemArea.getX(), (int)itemArea.getX() + (int)itemArea.getWidth()),
							  			  RandomProvider.randomInt((int)itemArea.getY(), (int)itemArea.getY() + (int)itemArea.getHeight()) );
					alchemyPoint = new Point(RandomProvider.randomInt(alchemyArea.getX(), alchemyArea.getX() + alchemyArea.getWidth()),
							 			     RandomProvider.randomInt(alchemyArea.getY(), alchemyArea.getY() + alchemyArea.getHeight()));
				}
				cursePoint = new Point(RandomProvider.randomInt(curseArea.getX(), curseArea.getX() + curseArea.getWidth()),
						   			   RandomProvider.randomInt(curseArea.getY(), curseArea.getY() + curseArea.getHeight()));
				
				if(this.trainAlchemy) {

					if((!script.getInventory().contains(f -> f != null && f.getName().equals("Nature rune")))) { //Can cast alchemy
						controller.debug("MageTrainer - No Nature Runes In Inventory");
						this.controller.getMovementHandler().moveToBank();
						RandomProvider.sleep(800,1200);
						this.killThread();
						continue;
					}
					if(!script.getInventory().contains(f -> f != null && f.getName().equals(itemName))) { //Has Alchemy Item
						controller.debug("MageTrainer - No Alchemy Item In Inventory");
						this.controller.getMovementHandler().moveToBank();
						RandomProvider.sleep(800,1200);
						this.killThread();
						continue;
					}
					if(script.getEquipment().getItemInSlot(EquipmentSlot.WEAPON.getSlot()) == null || 
					  !script.getEquipment().getItemInSlot(EquipmentSlot.WEAPON.getSlot()).getName().equals("Staff of fire")) { //Has Staff of fire 
						controller.debug("MageTrainer - No Staff of Fire In Inventory");
						this.controller.getMovementHandler().moveToBank();
						RandomProvider.sleep(800,1200);
						this.killThread();
						continue;
					}
				}
				if(!script.getMagic().canCast(curse)) { //Can Cast Curse
					controller.debug("MageTrainer - No Curse Runes In Inventory");
					this.controller.getMovementHandler().moveToBank();
					RandomProvider.sleep(800,1200);
					this.killThread();
					continue;
				}
				
				while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
				while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
				
				if(!script.getTabs().isOpen(Tab.MAGIC)) {
					script.getTabs().open(Tab.MAGIC);
					script.sleep(30);
				}	
				
				if(trainAlchemy) {
					
					randomize(alchemyPoint);
					
					alchemyPoint = new Point(RandomProvider.randomInt(alchemyArea.getX(), alchemyArea.getX() + alchemyArea.getWidth()),
			 				 RandomProvider.randomInt(alchemyArea.getY(), alchemyArea.getY() + alchemyArea.getHeight()));
	
					script.getMouse().click(alchemyPoint); //Alchemy cast
					
					RandomProvider.sleep(100, 200);
				}
				
				if(script.getInventory().contains(itemName)) {
					randomize(itemPoint);
					
					controller.getGraphicHandler().setInfo( "Mage trainer: Casting Alchemy");
					itemArea = script.getInventory().slotBounds(script.getInventory().get(itemName).getSlot());
				
					script.getMouse().click(itemPoint); //Item click
				}
				
				RandomProvider.sleep(450, 800);
				
				controller.getGraphicHandler().setInfo("Mage trainer: Casting Spell - " + this.curse);
				
				randomize(cursePoint);
				
				script.getMouse().click(cursePoint); //Curse cast
				
				RandomProvider.sleep(200, 400);
				
				target = script.getNpcs().closest(f -> f != null && f.getName().equals(targetName) && (f.getInteractingCharacter() == null || f.getInteractingCharacter().equals(script.getLocalPlayer()) ));
				
				if(target != null) {
					target.interact();
					if(RandomProvider.fiftyfifty()) {
						script.getMouse().move();
					}
					else {
						script.getMouse().move();
						RandomProvider.sleep(40, 80);
						script.getMouse().move();
					}
					
					if(limit <= (this.actionsCompleted + 1)) {
						controller.getMovementHandler().moveToBank();
						RandomProvider.sleep(800,1200);
						this.killThread = true;
					}
					this.actionsCompleted++;
					
					controller.returnKeyboardAccess();
					controller.returnMouseAccess();
				}
				else {
					target = script.getNpcs().closest(f -> f != null && f.getName().equals(targetName)); //Target click
					if(target == null) {
						
						controller.returnKeyboardAccess();
						controller.returnMouseAccess();
						
						controller.getWorldHandler().hopWorlds();
					}
					else if(target.isInteractedWith() && !target.isInteracting(script.getLocalPlayer())) {
						
						controller.returnKeyboardAccess();
						controller.returnMouseAccess();
						
						controller.getWorldHandler().hopWorlds();
					}
				}
				int failSafe = 0;
				while(!script.getLocalPlayer().isAnimating() && failSafe < 500) {
					script.sleep(10);
					failSafe++;
				}
				
			}
		
		}
		
	}
	
	public void randomize(Point point) {
		target = script.getNpcs().closest(f -> f != null && f.getName().equals(targetName) && 
										 (f.getInteractingCharacter() == null || f.getInteractingCharacter().equals(script.getLocalPlayer()) ));
		int randomizer = RandomProvider.randomInt(15);
		if(randomizer == 0){
			randomizer = RandomProvider.randomInt(3);
			if(randomizer == 0) {
				script.getCamera().keyboardRotateToTile(target.getTile().getArea(3).getRandomTile());
			}
			if(randomizer == 1) {
				script.getMouse().move(
							 new Point(
								  RandomProvider.randomInt((int)(point.getX()-20),(int)(point.getX()+20)),
								  RandomProvider.randomInt((int)(point.getY()-20),(int)(point.getY()+20))));
			}
			else {
				script.getCamera().mouseRotateToTile(target.getTile().getArea(3).getRandomTile());
			}
		}
		RandomProvider.sleep(200, 400);
	}
	
	
	public void setCurseVariables(Curse curse) {
		
		if(curse == Curse.CONFUSE) {
			this.curseArea = script.getWidgets().getWidget(218).getChild(7);
			this.curse = Normal.CONFUSE;
		}
		else if(curse == Curse.WEAKEN) {
			this.curseArea = script.getWidgets().getWidget(218).getChild(12);
			this.curse = Normal.WEAKEN;
		}
		else if(curse == Curse.CURSE) {
			this.curseArea = script.getWidget(218, 16);
			this.curse = Normal.CURSE;
		}
		else if(curse == Curse.SNARE) {
			this.curseArea = script.getWidgets().getWidget(218).getChild(35);
			this.curse = Normal.SNARE;
		}
		else if(curse == Curse.VULNERABILITY) {
			this.curseArea = script.getWidgets().getWidget(218).getChild(55);
			this.curse = Normal.VULNERABILITY;
		}
		else if(curse == Curse.ENFEEBLE) {
			this.curseArea = script.getWidgets().getWidget(218).getChild(59);	
			this.curse = Normal.ENFEEBLE;
		}
		else if(curse == Curse.ENTANGLE) {
			this.curseArea = script.getWidgets().getWidget(218).getChild(62);
			this.curse = Normal.ENTANGLE;
		}
		else if(curse == Curse.STUN) {
			this.curseArea = script.getWidgets().getWidget(218).getChild(63);
			this.curse = Normal.STUN;
		}

	}
	
	public enum Curse {
		CONFUSE, WEAKEN, CURSE, SNARE, VULNERABILITY, ENFEEBLE, ENTANGLE, STUN
	}
	
	public void setItemName(alchemyItem item) {
		if(item == alchemyItem.DRAGOSTONE_BOLT_TIPS) {
			this.itemName = "Dragonstone bolt tips";
		}
	}
	

	@Override
	public boolean setupModule() {
		controller.getMovementHandler().newLocation(this.locationEnum);
		
		controller.getMovementHandler().teleportToLocation();
		
		controller.getGearHandler().handleGearSwap(Gear.MAGIC); 
		
		controller.getGraphicHandler().setInfo("Mage trainer: Setting Up Module");
		
		if(!script.getMagic().canCast(curse) || ((!script.getInventory().contains(f -> f != null && f.getName().equals("Nature rune")) || 
						 						  !script.getInventory().contains(f -> f != null && f.getName().equals(this.itemName))) && trainAlchemy)) {
			
			controller.getMovementHandler().locateBank();
			
			while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
			while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
			
			controller.debug("Mouse control: MageTrainerModule");
			controller.debug("Keyboard control: MageTrainerModule");
			
			if(!script.getInventory().isEmpty()) {
				script.getBank().depositAllItems();
				RandomProvider.sleep(1000, 1750);
			}
			
			for(Rune rune : curse.getCost()) {
				if(script.getBank().contains(f -> f != null && f.getName().equals(rune.getName()))) {
					script.getBank().withdrawAll(f -> f != null && f.getName().equals(rune.getName()));
					RandomProvider.sleep(600, 950);
				}
				else {
					controller.returnKeyboardAccess();
					controller.returnMouseAccess();
					return false;
				}
			}
			
			if(this.trainAlchemy) {
				if(script.getBank().contains("Nature rune")) {
					script.getBank().withdrawAll(f -> f != null && f.getName().equals("Nature rune"));
					RandomProvider.sleep(600, 950);
				}
				else {
					controller.returnKeyboardAccess();
					controller.returnMouseAccess();
					return false;
				}
			
			
				if(script.getBank().contains(itemName)) {
					script.getBank().withdrawAll(f -> f != null && f.getName().equals(itemName));
					RandomProvider.sleep(600, 950);
				}
				else {
					controller.returnKeyboardAccess();
					controller.returnMouseAccess();
					return false;
				}
				
				script.getBank().close();
				
				
				Item i = script.getInventory().get(f -> f != null && f.getName().equals(itemName));
				if(script.getInventory().getItemInSlot(15) == null || (i != null && !script.getInventory().getItemInSlot(15).getName().equals(itemName))) {
					script.getMouse().move(i.getDestination());
					RandomProvider.sleep(500, 1000);
					script.getMouse().drag(script.getInventory().slotBounds(15));
					script.getMouse().move();
				}
			
			}
			
			controller.returnKeyboardAccess();
			controller.returnMouseAccess();
			return true;

		}
		else {
			while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
			while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
			
			if(script.getBank().isOpen()) {
				script.getBank().close();
				RandomProvider.sleep(1000, 2000);
			}
			
			if(trainAlchemy) {
				Item i = script.getInventory().get(f -> f != null && f.getName().equals(itemName));
				if(script.getInventory().getItemInSlot(15) == null || (i != null && !script.getInventory().getItemInSlot(15).getName().equals(itemName))) {
					script.getMouse().move(i.getDestination());
					RandomProvider.sleep(300, 600);
					script.getMouse().drag(script.getInventory().slotBounds(15));
					script.getMouse().move();
				}
			}
			
			controller.returnKeyboardAccess();
			controller.returnMouseAccess();
			return true;
		}
	}

	@Override
	public Skill getSkillToHover() {
		return Skill.MAGIC;
	}

	
	public enum alchemyItem {
		DRAGOSTONE_BOLT_TIPS
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
