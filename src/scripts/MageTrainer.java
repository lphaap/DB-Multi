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

import init.MainLooper;
import movement.Location;
import movement.Locations;

public class MageTrainer extends ScriptModule{
	private MainLooper script;
	private Random random = new Random();
	private int delay;
	private int limit;
	private int actionsCompleted;
	private Location location;
	private Locations locationEnum;
	private Spell alchemy;
	private Spell curse;
	private WidgetChild curseArea;
	private WidgetChild alchemyArea;
	private Point alchemyPoint;
	private Point cursePoint;
	private Point itemPoint;
	private String itemName;
	private NPC target;
	private String targetName;
	private boolean error;
	private Rectangle itemArea;
	
	
	public MageTrainer(MainLooper script, int limit, Spell curse, MageTrainer.alchemyItem item) {
		this.script = script;
		this.limit = limit;
		this.alchemy = Normal.HIGH_LEVEL_ALCHEMY;
		this.location = new Location(this.script, Locations.SPLASHING_BEAR);
		this.targetName = "Grizzly bear";
		this.actionsCompleted = 0;
		setCurseVariables(curse);
		setItemName(item);
		this.alchemyArea = script.getWidget(218, 38);
		this.moduleName = "MageTrainerModule";
	}
	
	@Override
	public int onLoop() {
		random.setSeed(random.nextLong());
		delay = random.nextInt(100) + 100;
		Calculations.setRandomSeed(random.nextLong());
		
		if(limit <= this.actionsCompleted) {
			script.nextModule();
			script.sleep(2000);
			return delay;
		}
		
		
		
		if(!location.inArea()) {
			script.setReact(0);
			location.travel();
		}
		else {
			script.setReact(1);
			
			//alchemyPoint = new Point(Calculations.random(alchemyArea.getX(), alchemyArea.getX() + alchemyArea.getWidth()),
			//Calculations.random(alchemyArea.getY(), alchemyArea.getY() + alchemyArea.getHeight()));
			//script.getMouse().click(alchemyPoint);
			if(!script.getTabs().isOpen(Tab.MAGIC)) {
				script.getTabs().open(Tab.MAGIC);
				script.sleep(30);
			}
			
			
			if(!script.getMagic().canCast(curse) || !script.getInventory().contains(f -> f != null && f.getName().equals("Nature rune")) || !script.getInventory().contains(f -> f != null && f.getName().equals(itemName))) {
				
				
				//|| script.getMagic().canCast(alchemy)
				script.getMessenger().sendMessage("MageTrainer - Module ERROR");
				script.getMessenger().sendMessage("Trying to Restart Module...");
				if(!setupModule() || error) {
					script.getMessenger().sendMessage("MageTrainer - Module ERROR");
					script.getMessenger().sendMessage("Changing Module.");
					script.nextModule();
				}
				script.getMessenger().sendMessage("Module Restart Complete");
				this.error = true;
			}
			
			if(script.getEquipment().getItemInSlot(EquipmentSlot.WEAPON.getSlot()) == null || !script.getEquipment().getItemInSlot(EquipmentSlot.WEAPON.getSlot()).getName().equals("Staff of fire")) {
				if(script.getEquipment().getItemInSlot(EquipmentSlot.WEAPON.getSlot()) == null) {
					script.getMessenger().sendMessage("MageTrainer - Module ERROR");
					script.getMessenger().sendMessage("No Fire staff Found - Changing Module");
					script.nextModule();
					script.sleep(2000);
					return delay;
				}
			}
			
			alchemyPoint = new Point(Calculations.random(alchemyArea.getX(), alchemyArea.getX() + alchemyArea.getWidth()),
					Calculations.random(alchemyArea.getY(), alchemyArea.getY() + alchemyArea.getHeight()));
			script.getMouse().click(alchemyPoint);
			
			script.sleep(random.nextInt(100) + 100);
			
			if(script.getInventory().contains(itemName)) {
				script.setInfoText( "Mage trainer: Casting Alchemy");
				itemArea = script.getInventory().slotBounds(script.getInventory().get(itemName).getSlot());
				itemPoint = new Point(Calculations.random((int)itemArea.getX(), (int)itemArea.getX() + (int)itemArea.getWidth()),
						Calculations.random((int)itemArea.getY(), (int)itemArea.getY() + (int)itemArea.getHeight()));
				script.getMouse().click(itemPoint);
			}
			
			script.sleep(random.nextInt(250) + 450);
			
			script.setInfoText("Mage trainer: Casting Spell - " + this.curse);
			
			cursePoint = new Point(Calculations.random(curseArea.getX(), curseArea.getX() + curseArea.getWidth()),
								Calculations.random(curseArea.getY(), curseArea.getY() + curseArea.getHeight()));
			script.getMouse().click(cursePoint);
			script.sleep(random.nextInt(200) + 200);
			target = script.getNpcs().closest(f -> f != null && f.getName().equals(targetName) && (f.getInteractingCharacter() == null || f.getInteractingCharacter().equals(script.getLocalPlayer()) ));
			
			if(target != null) {
				int randomizer = random.nextInt(5);
				if(randomizer == 0){
					randomizer = random.nextInt(2);
					if(randomizer == 0) {
						script.getCamera().keyboardRotateToTile(target.getTile().getArea(3).getRandomTile());
					}
					else {
						script.getCamera().mouseRotateToTile(target.getTile().getArea(3).getRandomTile());
					}
				}
				target.interact();
				script.getMouse().move();
				this.actionsCompleted++;
			}
			else {
				target = script.getNpcs().closest(f -> f != null && f.getName().equals(targetName));
				if(target == null) {
					script.hop();
				}
				else if(target.isInteractedWith() && !target.isInteracting(script.getLocalPlayer())) {
					script.hop();
				}
			}
			int failSafe = 0;
			while(!script.getLocalPlayer().isAnimating() && failSafe < 500) {
				script.sleep(10);
				failSafe++;
			}
			
		}
		
		
		
		
		return delay;
	}
	
	public void setCurseVariables(Spell curse) {
		
		
		if(curse == Normal.CONFUSE) {
			this.curseArea = script.getWidgets().getWidget(218).getChild(6);
			this.curse = curse;
		}
		else if(curse == Normal.WEAKEN) {
			this.curseArea = script.getWidgets().getWidget(218).getChild(11);
			this.curse = curse;
		}
		else if(curse == Normal.CURSE) {
			this.curseArea = script.getWidget(218, 15);
			this.curse = curse;
		}
		else if(curse == Normal.SNARE) {
			this.curseArea = script.getWidgets().getWidget(218).getChild(34);
			this.curse = curse;
		}
		else if(curse == Normal.VULNERABILITY) {
			this.curseArea = script.getWidgets().getWidget(218).getChild(54);
			this.curse = curse;
		}
		else if(curse == Normal.ENFEEBLE) {
			this.curseArea = script.getWidgets().getWidget(218).getChild(58);	
			this.curse = curse;
		}
		else if(curse == Normal.ENTANGLE) {
			this.curseArea = script.getWidgets().getWidget(218).getChild(61);
			this.curse = curse;
		}
		else if(curse == Normal.STUN) {
			this.curseArea = script.getWidgets().getWidget(218).getChild(62);
			this.curse = curse;
		}

	}
	
	public void setItemName(alchemyItem item) {
		if(item == alchemyItem.DRAGOSTONE_BOLT_TIPS) {
			this.itemName = "Dragonstone bolt tips";
		
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
		script.setInfoText("Mage trainer: Setting up module");
		
		if(!script.getMagic().canCast(curse) || !script.getInventory().contains(f -> f != null && f.getName().equals("Nature rune"))) {
			
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
			
			for(Rune rune : curse.getCost()) {
				if(script.getBank().contains(f -> f != null && f.getName().equals(rune.getName()))) {
					script.getBank().withdrawAll(f -> f != null && f.getName().equals(rune.getName()));
					script.sleep(random.nextInt(500)+ 500);
				}
				else {
					return false;
				}
			}
			
			if(script.getBank().contains("Nature rune")) {
				script.getBank().withdrawAll(f -> f != null && f.getName().equals("Nature rune"));
				script.sleep(random.nextInt(500)+ 500);
			}
			else {
				return false;
			}
			
			if(script.getBank().contains(itemName)) {
				script.getBank().withdrawAll(f -> f != null && f.getName().equals(itemName));
				script.sleep(random.nextInt(500)+ 500);
			}
			else {
				return false;
			}
			
			script.getBank().close();
			
			
			Item i = script.getInventory().get(f -> f != null && f.getName().equals(itemName));
			if(script.getInventory().getItemInSlot(15) == null || (i != null && !script.getInventory().getItemInSlot(15).getName().equals(itemName))) {
				script.getMouse().move(i.getDestination());
				script.sleep(random.nextInt(500) + 500);
				script.getMouse().drag(script.getInventory().slotBounds(15));
				script.getMouse().move();
			}
			
			return true;

		}
		else {
			if(script.getBank().isOpen()) {
				script.getBank().close();
				script.sleep(random.nextInt(1000) + 1000);
			}
			
			Item i = script.getInventory().get(f -> f != null && f.getName().equals(itemName));
			if(script.getInventory().getItemInSlot(15) == null || (i != null && !script.getInventory().getItemInSlot(15).getName().equals(itemName))) {
				script.getMouse().move(i.getDestination());
				script.sleep(random.nextInt(300) + 300);
				script.getMouse().drag(script.getInventory().slotBounds(15));
				script.getMouse().move();
			}
			return true;
		}
	}

	@Override
	public Skill getSkillToHover() {
		return Skill.MAGIC;
	}

	@Override
	public void errorTest() {
		// TODO Auto-generated method stub
		
	}
	
	public enum alchemyItem {
		DRAGOSTONE_BOLT_TIPS
	
	}
	
}
