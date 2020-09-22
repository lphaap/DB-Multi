package utilities;

import java.util.ArrayList;

import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.wrappers.items.Item;

import antiban.RandomProvider;
import client.ClientThread;
import client.KillableThread;
import client.ThreadController;

public class GroundItemHandler implements KillableThread{
	private ThreadController controller;
	private ClientThread client;
	
	private boolean killThread;
	private boolean dropForMinor;
	
	private ArrayList<String> unique = new ArrayList<String>(); // > ~100k
	private ArrayList<String> major = new ArrayList<String>(); // > Sara brew
	private ArrayList<String> minor = new ArrayList<String>(); // < Sara brew 
	private ArrayList<String> drop = new ArrayList<String>(); // items to drop
	
	public GroundItemHandler(ThreadController controller, ClientThread client, 
							 ArrayList<String> dropItems, boolean dropForMinor) {
		this.controller = controller;
		this.client = client;
		this.dropForMinor = dropForMinor;
		drop = dropItems;
		drop.add(0, "Vial");
		
		createUnique();
		createMajor();
		createMinor();
	}
	
	@Override
	public void run() {
		while(!killThread) {
			RandomProvider.sleep(400, 600);
		//	controller.debug("Looping GIH");
			//controller.debug("Major Start");
			
			unique: for(String name : unique) { //Unique items
				GroundItem item = client.getGroundItems().closest(f -> f != null && f.getName().contains(name));
				if(item != null) {
					
					if(!client.getLocalPlayer().getTile().getArea(8).contains(item)) {
						continue unique;
					}
					while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
					controller.debug("Mouse control: GroundItemHandler");
					controller.getGraphicHandler().setInfo("GroundItemHandler: Trying to pick item");
					
					if(client.getInventory().isFull()) {
						boolean dropped = false;
						
						dropLoop: for(String i : drop) {
							if(client.getInventory().contains(f -> f != null && f.getName().contains(i))) {
								Item inv = client.getInventory().get(f -> f != null && f.getName().contains(i));
								if(inv.hasAction("Eat")) {
									inv.interact("Eat");
								}
								else {
									inv.interact("Drop");
								}
								RandomProvider.sleep(150, 200);
								dropped = true;
								break dropLoop;
							}
						}
						
						minorLoop: if(!dropped) {
							for(String i : minor) {
								if(client.getInventory().contains(f -> f != null && f.getName().contains(i))) {
									client.getInventory().get(f -> f != null && f.getName().contains(i)).interact("Drop");
									RandomProvider.sleep(150, 200);
									dropped = true;
									break minorLoop;
								}
							}
						}
						
						majorLoop: if(!dropped) {
							for(String i : major) {
								if(client.getInventory().contains(f -> f != null && f.getName().contains(i))) {
									client.getInventory().get(f -> f != null && f.getName().contains(i)).interact("Drop");
									RandomProvider.sleep(150, 200);
									dropped = true;
									break majorLoop;
								}
							}
						}
						
						if(client.getInventory().isFull()) {
							controller.returnMouseAccess();
							continue unique;
						}
					}
					
					if(item.exists()) {
						item.interact("Take");
						client.getMouse().move();
					}
					controller.returnMouseAccess();
				}
			}
			//controller.debug("Major Stop");
			RandomProvider.sleep(400, 600);
			
			major: for(String name : major) { //Major items
				GroundItem item = client.getGroundItems().closest(f -> f != null && f.getName().contains(name));
				if(item != null) {
					
					if(!client.getLocalPlayer().getTile().getArea(10).contains(item)) {
						continue major;
					}
					while(controller.requestMouseAccess()) {RandomProvider.sleep(8);}
					controller.debug("Mouse control: GroundItemHandler");
					controller.getGraphicHandler().setInfo("GroundItemHandler: Trying to pick item");
					
					if(client.getInventory().isFull()) {
						boolean dropped = false;
						
						dropLoop: for(String i : drop) {
							if(client.getInventory().contains(f -> f != null && f.getName().contains(i))) {
								Item inv = client.getInventory().get(f -> f != null && f.getName().contains(i));
								if(inv.hasAction("Eat")) {
									inv.interact("Eat");
								}
								else {
									inv.interact("Drop");
								}
								RandomProvider.sleep(150, 200);
								dropped = true;
								break dropLoop;
							}
						}
						
						minorLoop: if(!dropped) {
							for(String i : minor) {
								if(client.getInventory().contains(f -> f != null && f.getName().contains(i))) {
									client.getInventory().get(f -> f != null && f.getName().contains(i)).interact("Drop");
									RandomProvider.sleep(150, 200);
									dropped = true;
									break minorLoop;
								}
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
					}
					controller.returnMouseAccess();
				}
			}
			//controller.debug("Major Stop");
			RandomProvider.sleep(400, 600);
			
			//controller.debug("Minor Start");
			if(this.dropForMinor || !client.getInventory().isFull()) {
				minor: for(String name : minor) {//Minor items
					GroundItem item = client.getGroundItems().closest(f -> f != null && f.getName().contains(name));
					if(item != null) {
						
						if(!client.getLocalPlayer().getTile().getArea(8).contains(item)) {
							continue minor;
						}
						while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
						controller.debug("Mouse control: GroundItemHandler");
						controller.getGraphicHandler().setInfo("GroundItemHandler: Trying to pick item");
						
						if(client.getInventory().isFull()) {
							dropLoop: for(String i : drop) {
								if(client.getInventory().contains(f -> f != null && f.getName().contains(i))) {
									Item inv = client.getInventory().get(f -> f != null && f.getName().contains(i));
									if(inv.hasAction("Eat")) {
										inv.interact("Eat");
									}
									else {
										inv.interact("Drop");
									}
									RandomProvider.sleep(150, 200);
									break dropLoop;
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
						}
						controller.returnMouseAccess();
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
		this.unique.clear();
		//controller.debug("GIH Killed");
	}

	@Override
	public boolean isAlive() {
		return !killThread;
	}
	
	//https://www.reddit.com/r/2007scape/comments/hrmb1h/complete_loot_tab_from_7599_slayer/
	//Monsters added: Hellhound, Greater demon, Banshee, Twisted Banshee, Wyrm, Kurask
	
	
	private void createUnique() {
		unique.add("Uncut onyx");
		unique.add("Uncut zenyte");
		unique.add("Shield right half");
		unique.add("Dragonfruit tree seed");
		unique.add("Smouldering stone");
		unique.add("Clue scroll");
		unique.add("champion scroll");
		unique.add("Brimstone key");
		unique.add("defender");
		unique.add("Mossy key");
		unique.add("Dragon platelegs");
		unique.add("Dragon harpoon");
		unique.add("Dragon knife");
		unique.add("Dragon plateskirt");
		unique.add("Obsidian cape");
		unique.add("Toktz-xil-ak");
		unique.add("Ecumenical key");
	}
	
	private void createMajor() {
		major.add("Toktz-xil-ul");
		major.add("Toktz-xil-ek");
		major.add("Long bone");
		major.add("Curved bone");
		major.add("Snapdragon seed");
		major.add("Palm tree seed");
		major.add("Magic seed");
		major.add("Maple seed");
		minor.add("Yew seed");
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
		major.add("Dragon thrownaxe");
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
		major.add("Rune axe");
		major.add("Runite bar");
		major.add("Runite limbs");
		major.add("battlestaff");
		major.add("Battlestaff");
		minor.add("Runite bar");
		major.add("Black d'hide body");
		major.add("Ensouled demon head");
		major.add("Ensouled abyssal head");
		major.add("Ensouled aviansie head");
		major.add("Ensouled bloodveld head");
		major.add("Ensouled dragon head");
		major.add("Ensouled tzhaar head");
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
		major.add("Dark totem base");
		major.add("Dark totem top");
		minor.add("Dragonstone");
		minor.add("Uncut dragonstone");
		major.add("Ancient shard");
		major.add("Coconut");
		major.add("Papaya fruit");
		major.add("Leaf-bladed");
		
		//Kurask farming
	//	major.add("Coins");
		//major.add("Big bones");
		
	}
	
	private void createMinor() {
		major.add("White berries");
		minor.add("Dragon bones");
		minor.add("Black d'hide");
		minor.add("Red d'hide");
		minor.add("Blue d'hide");
		minor.add("Magic shortbow");
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
		minor.add("Wyrm bones");
		minor.add("Staff of");
		minor.add("Adamantite bar");
		minor.add("Uncut diamond");
		minor.add("Diamond");
		minor.add("Uncut ruby");
		minor.add("Ruby");
		minor.add("Adamant med helm");
		minor.add("Nature talisman");
		minor.add("Adamantite ore");
		minor.add("Avantoe seed");
		minor.add("Cadantine seed");
		minor.add("Grimy avantoe");
		minor.add("Grimy cadantine");
		minor.add("Grimy kwuarm");
		minor.add("Grimy toadflax");
		minor.add("Grimy lantadyme");
		minor.add("Ensouled giant head");
		minor.add("Ensouled kalphite head");
		minor.add("Ensouled ogre head");
		minor.add("Ensouled scorpion head");
		minor.add("Ensouled troll head");
		minor.add("Ensouled unicorn head");
		minor.add("Ensouled elf head");
		minor.add("Ensouled dagannoth head");
		minor.add("Ensouled elf head");
		minor.add("Ensouled bear head");
		minor.add("Ensouled dog head");
		minor.add("Ensouled horror head");
		minor.add("Blood rune");
		minor.add("Death rune");
		minor.add("Nature rune");
		minor.add("Law rune");
		minor.add("Soul rune");
		minor.add("Black robe");
		minor.add("Mud rune");
		minor.add("Mist rune");
		minor.add("Chaos rune");
		minor.add("Cosmic rune");
		minor.add("Wrath rune");
		minor.add("Smoke rune");
		minor.add("Dragonstone bolt tips");
		minor.add("Dragonstone bolts");
		minor.add("Raw sharks");
		minor.add("Raw manta ray");
		minor.add("Raw anglerfish");
		minor.add("Magic logs");
		minor.add("Teak plank");
		minor.add("Dragon arrowtips");
		minor.add("Rune arrowtips");
		minor.add("Dragon thrownaxe");
		minor.add("Adamant kiteshield");
		minor.add("Rune javelin");
		minor.add("Limpwurt root");
		major.add("Adamant plateskirt");
		major.add("Adamant battleaxe");
		major.add("Adamant platebody");
		major.add("Adamant platelegs");
		major.add("Adamant chainbody");
		major.add("Adamant full helm");
		major.add("Adamant scimitar");
		minor.add("Adamant med helm");
		minor.add("Adamant 2h sword");
		minor.add("Adamant sq shield");
		minor.add("Mithril kiteshield");
		minor.add("Mithril platelegs");
		major.add("Mithril full helm");
		minor.add("Mithril platebody");
		minor.add("Mithril plateskirt");
		minor.add("Adamant warhammer");
	}
	

}
