package init;
import java.util.List;
import java.util.Random;

import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.magic.Normal;
import org.dreambot.api.methods.magic.cost.Rune;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.Item;

import scripts.ScriptModule;

public class LocationPrinter extends ScriptModule{
	private MainLooper script;
	private Random random = new Random();
	
	
	@Override
	public int onLoop() {
		script.log("Tile: X: "+script.getLocalPlayer().getX() + ", Y: "+script.getLocalPlayer().getY());
		script.log("Distance to bank: " + script.getBank().getClosestBankLocation().getCenter());
		
		Item i = script.getInventory().get(f -> f != null && f.getName().equals("Dragonstone bolt tips"));
		if(script.getInventory().getItemInSlot(15) == null || (i != null && !script.getInventory().getItemInSlot(15).getName().equals("Dragonstone bolt tips"))) {
			script.getMouse().move(i.getDestination());
			script.log(""+script.getInventory().getItemInSlot(15));
			script.sleep(random.nextInt(500) + 500);
			script.getMouse().drag(script.getInventory().slotBounds(15));
		}
		//Location l = new Location(script, Locations.COMBAT_EXPERIMENTS);
		//l.travel();
		//l.travelToBank();
		
		//examinePlayer();
		//script.sleep(5000);
		//examineNPC();
		//script.sleep(5000);
		//examineGameObject();
		//script.sleep(5000);
		
		
		/*Tile ta = new Tile(0,0);
		Teleporter t = new Teleporter(script,ta.getArea(1));
		for(TeleportItem i : t.getTeleLocations()) {
			i.getItem();
			script.sleep(5000);
			i.useTeleport();
			script.sleep(5000);
		}
		/*
		for(Rune rune : Normal.CURSE.getCost()) {
			script.log(rune.getName());
			script.log(""+script.getBank().contains(f -> f != null && f.getName().equals(rune.getName())));
			script.getBank().withdrawAll(f -> f != null && f.getName().equals(rune.getName()));
			script.sleep(2000);
		}
		script.getBank().depositAllItems();
		
		MageTrainer mg = new MageTrainer(script, 1000, Normal.CURSE);
		mg.setupModule();

		//script.log(""+(script.getEquipment().getItemInSlot(EquipmentSlot.WEAPON.getSlot()) == null));
		//script.log("" + script.getGameObjects().closest(f -> f != null && f.getName().equals("Rocks")).getModelColors()[0]);
		//Location l = new Location(script, Locations.MINER_DWARVEN_MINE_COAL);
		//l.travel();
		//Random random = new Random();
	
		//script.log(script.getEquipment().getSlotForItem(f -> f != null && f.getName().contains("scimitar")).name()+ " - "script.getEquipment().getSlotForItem(f -> f != null && f.getName().contains("scimitar")).);
		
		//script.getTabs().open(Tab.LOGOUT);
		//script.sleep(random.nextInt(300)+300);
		//script.getWidgets().getWidget(182).getChild(12).interact();
		/*
		GameObject ore = script.getGameObjects().closest(gameObject -> gameObject != null && gameObject.getName().equals("Rocks") 
				&& gameObject.getModelColors() != null && gameObject.getModelColors()[0] == 8885
				);
		int test = 0;
		while(test < 15) {
			test++;
			script.sleep(3000);
			script.log("Player: " + ore.getSurroundingArea(1).contains(script.getLocalPlayer()));
		}*/
		/*if(script.getEquipment().getItemInSlot(EquipmentSlot.WEAPON.getSlot()).getName().toLowerCase().contains("bow")) {
			if(script.getEquipment().getItemInSlot(EquipmentSlot.ARROWS.getSlot()) == null) {
				script.log("no arrows");
			}
			
		}*/
		
		//script.log(""+script.getBank().count(f -> f != null && f.getName().equals("Steel bar")));
		
		return 2000;
	}
	
	public boolean examinePlayer() {
		if(script.getPlayerCount() > 0) {
			Player examine = script.getPlayers().closest(f -> f != null && !f.equals(script.getLocalPlayer()));
			script.sleep(random.nextInt(300)+300);
			examine.interactForceRight(null);
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean examineNPC() {
		NPC examine = script.getNpcs().closest(f -> f != null);
		if(examine != null) {
			script.getCamera().mouseRotateToEntity(examine);
			script.sleep(random.nextInt(300)+300);
			examine.interactForceRight("Examine");
			script.sleep(random.nextInt(300)+300);
			script.getMouse().move();
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean examineGameObject() {
		List<GameObject> goList = script.getGameObjects().all(f -> f != null && (f.distance(script.getLocalPlayer()) <= 4) && f.hasAction("Examine"));
		goList.get(random.nextInt(goList.size())).interactForceRight("Examine");
		script.sleep(random.nextInt(300)+300);
		script.getMouse().move();
		return true;
	}
	
	
	
	public LocationPrinter(MainLooper script) {
		this.script = script;
	}

	@Override
	public int actionsCompleted() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean setupModule() {
		// TODO Auto-generated method stub
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
