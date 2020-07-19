package utilities;

import java.util.ArrayList;

import org.dreambot.api.wrappers.items.GroundItem;

import antiban.RandomProvider;
import client.ClientThread;
import client.KillableThread;
import client.ThreadController;

public class GroundItemHandler implements KillableThread{
	private ThreadController controller;
	private ClientThread client;
	
	private boolean killThread;
	private boolean dropForMinor;
	
	private ArrayList<String> major = new ArrayList<String>(); // > Sara brew
	private ArrayList<String> minor = new ArrayList<String>(); // < Sara brew 
	private ArrayList<String> drop = new ArrayList<String>(); // items to drop
	
	public GroundItemHandler(ThreadController controller, ClientThread client, 
							 ArrayList<String> dropItems, boolean dropForMinor) {
		this.controller = controller;
		this.client = client;
		this.dropForMinor = dropForMinor;
		drop = dropItems;
		drop.add("Vial");
		
		createMajor();
		createMinor();
	}
	
	@Override
	public void run() {
		while(!killThread) {
			RandomProvider.sleep(400, 600);
			
			//controller.debug("Major Start");
			major: for(String name : major) {
				GroundItem item = client.getGroundItems().closest(f -> f != null && f.getName().contains(name));
				if(item != null) {
					
					if(!client.getLocalPlayer().getTile().getArea(10).contains(item)) {
						continue major;
					}
					while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
					controller.debug("Mouse control: GroundItemHandler");
					controller.getGraphicHandler().setInfo("GroundItemHandler: Trying to pick item");
					
					if(client.getInventory().isFull()) {
						for(String i : drop) {
							if(client.getInventory().contains(f -> f != null && f.getName().contains(i))) {
								client.getInventory().get(f -> f != null && f.getName().contains(i)).interact("Drop");
								RandomProvider.sleep(150, 200);
							}
						}
						if(client.getInventory().isFull()) {
							controller.returnMouseAccess();
							continue major;
						}
					}
					
					if(item.exists()) {
						item.interact("Take");
						client.getMouse().move();
						controller.returnMouseAccess();
					}
				}
			}
			//controller.debug("Major Stop");
			RandomProvider.sleep(400, 600);
			
			//controller.debug("Minor Start");
			if(this.dropForMinor || !client.getInventory().isFull()) {
				minor: for(String name : minor) {
					GroundItem item = client.getGroundItems().closest(f -> f != null && f.getName().contains(name));
					if(item != null) {
						
						if(!client.getLocalPlayer().getTile().getArea(10).contains(item)) {
							continue minor;
						}
						while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
						controller.debug("Mouse control: GroundItemHandler");
						controller.getGraphicHandler().setInfo("GroundItemHandler: Trying to pick item");
						
						if(client.getInventory().isFull()) {
							for(String i : drop) {
								if(client.getInventory().contains(f -> f != null && f.getName().contains(i))) {
									client.getInventory().get(f -> f != null && f.getName().contains(i)).interact("Drop");
									RandomProvider.sleep(150, 200);
								}
							}
							if(client.getInventory().isFull()) {
								controller.returnMouseAccess();
								continue minor;
							}
						}
						
						if(item.exists()) {
							item.interact("Take");
							client.getMouse().move();
							controller.returnMouseAccess();
						}
					}
				}
			}
			//controller.debug("Minor Stop");
		}
	}

	@Override
	public void killThread() {
		this.killThread = true;
		this.major.clear();
		this.minor.clear();
	}

	@Override
	public boolean isAlive() {
		return !killThread;
	}
	
	//https://www.reddit.com/r/2007scape/comments/hrmb1h/complete_loot_tab_from_7599_slayer/
	//Monsters added: 
	
	
	private void createMajor() {
		major.add("Uncut onyx");
		major.add("Uncut zenyte");
		major.add("Shield right half");
		major.add("Dragonfruit tree seed");
		major.add("Snapdragon seed");
		major.add("Palm tree seed");
		major.add("Magic seed");
		major.add("Maple seed");
		minor.add("Yew seed");
		major.add("Dragon platelegs");
		major.add("Dragon plateskirt");
		major.add("Dragon halberd");
		major.add("Dragon battleaxe");
		major.add("Shield left half");
		major.add("Torstol seed");
		major.add("Granite maul");
		major.add("Granite shield");
		major.add("Dragon spear");
		major.add("Dragon longsword");
		major.add("Dragon dagger");
		major.add("Dragon med helm");
		major.add("Mystic robe");
		major.add("Mystic hat");
		major.add("Ranarr seed");
		major.add("Rune platebody");
		major.add("Runite ore");
		major.add("Rune plateskirt");
		major.add("Rune platelegs");
		major.add("Rune chainbody");
		major.add("Rune sq shield");
		major.add("Rune full helm");
		major.add("Rune med helm");
		major.add("Rune kiteshield");
		major.add("Rune scimitar");
		major.add("Rune axe");
		major.add("Rune sword");
		major.add("Rune 2h sword");
		major.add("Rune longsword");
		major.add("Rune warhammer");
		major.add("Rune battleaxe");
		major.add("Rune dagger");
		major.add("Rune spear");
		major.add("Rune mace");
		major.add("Runite bar");
		major.add("Runite limbs");
		major.add("battlestaff");
		major.add("Black d'hide body");
		major.add("Crystal key");
		major.add("Loop half of key");
		major.add("Tooth half of key");
		major.add("Papaya tree seed");
		major.add("Grimy torstol");
		major.add("Grimy ranarr weed");
		major.add("Grimy snapdragon");
		major.add("Uncut dragonstone");
		major.add("Onyx bolt tips");
		major.add("Onyx bolts");
		major.add("Dragonstone ring");
		major.add("Dragon knife");
		major.add("Rune dart");
		major.add("Leaf-bladed sword");
		major.add("Leaf-bladed battleaxe");
	}
	
	private void createMinor() {
		minor.add("Dragon bones");
		minor.add("Black d'hide");
		minor.add("Red d'hide");
		minor.add("Blue d'hide");
		minor.add("White lily seed");
		minor.add("Willow seed");
		minor.add("Green d'hide");
		minor.add("Mahogany seed");
		minor.add("Snape grass seed");
		minor.add("Toadflax seed");
		minor.add("Black dragonhide");
		minor.add("Red dragonhide");
		minor.add("Green dragonhide");
		minor.add("Blue dragonhide");
		minor.add("Lantadyme seed");
		minor.add("Staff of");
		minor.add("Adamantite bar");
		minor.add("Uncut diamond");
		minor.add("Diamond");
		minor.add("Uncut ruby");
		minor.add("Ruby");
		minor.add("Adamantite ore");
		minor.add("Avantoe seed");
		minor.add("Cadantine seed");
		minor.add("Grimy avantoe");
		minor.add("Grimy cadantine");
		minor.add("Grimy kwuarm");
		minor.add("Grimy toadflax");
		minor.add("Grimy lantadyme");
		minor.add("Blood rune");
		minor.add("Death rune");
		minor.add("Nature rune");
		minor.add("Law rune");
		minor.add("Soul rune");
		minor.add("Mud rune");
		minor.add("Mist rune");
		minor.add("Wrath rune");
		minor.add("Smoke rune");
		minor.add("Rune arrow");
		minor.add("Adamant arrow");
		minor.add("Dragonstone bolt tips");
		minor.add("Dragonstone bolts");
		minor.add("Raw sharks");
		minor.add("Raw manta ray");
		minor.add("Raw anglerfish");
		minor.add("Magic logs");
		minor.add("Teak plank");
		minor.add("Dragon arrowtips");
		minor.add("Dragon thrownaxe");
		minor.add("Rune knife");
		minor.add("Adamant knife");
		minor.add("Adamant dart");
		minor.add("Rune javelin");
	}
	

}
