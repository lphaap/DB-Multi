import java.util.ArrayList;
import java.util.Random;

import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;

public class Teleporter {
	private MainLooper script;
	private Tile target;
	private Tile teleport;
	private Tile bank;
	private Tile player;
	private TeleportItem tele;
	private ArrayList<TeleportItem> teleLocations;
	private Random random = new Random();
	
	public Teleporter(MainLooper script, Area target) {
		this.script = script;
		this.target = target.getNearestTile(script.getLocalPlayer());
		
		teleLocations = new ArrayList<TeleportItem>();
		//MainLooper script, Tile location, String locationname, String action, String itemName
		teleLocations.add(new TeleAmulet(script, new Tile(3407,3156), new Tile(3369, 3170), "Desert Eagle", "Necklace of passage(", 3)); 
		teleLocations.add(new TeleAmulet(script, new Tile(3315,3236), new Tile(3382, 3268), "Dueal Arena", "Ring of dueling(", 1));
		teleLocations.add(new TeleAmulet(script, new Tile(2438,3091), new Tile(2442, 3084), "Castle wars", "Ring of dueling(", 2));
		teleLocations.add(new TeleAmulet(script, new Tile(2896,3555), new Tile(2809, 3440), "Burthope", "Games necklace(", 1));
		teleLocations.add(new TeleAmulet(script, new Tile(2522,3571), new Tile(2536, 3573), "Barbarian Outpost", "Games necklace(", 2));
		teleLocations.add(new TeleAmulet(script, new Tile(3087,3496), new Tile(3093, 3491), "Edgeville", "Amulet of glory(", 1));
		teleLocations.add(new TeleAmulet(script, new Tile(2918,3176), new Tile(3093, 3243), "Karamja", "Amulet of glory(", 2));
		teleLocations.add(new TeleAmulet(script, new Tile(3105,3251), new Tile(3093, 3243), "Draynor Village", "Amulet of glory(", 3));
		teleLocations.add(new TeleTab(script, new Tile(3548,3528), new Tile(3512, 3479), "Morytania", "Fenkenstrain's castle teleport"));
		teleLocations.add(new TeleTab(script, new Tile(3221,3218), new Tile(3208, 3218), "Lumbridge", "Lumbridge teleport"));
		teleLocations.add(new TeleTab(script, new Tile(3213,3425), new Tile(3183, 3435), "Varrock", "Varrock teleport"));
		teleLocations.add(new TeleTab(script, new Tile(2966,3379), new Tile(2946, 3370), "Falador", "Falador teleport"));
		teleLocations.add(new TeleTab(script, new Tile(2757,3478), new Tile(2726, 3492), "Camelot", "Camelot teleport"));
		
		teleport = getClosestTeleport();
	}
	
	/**
	 * Teleports player to the initialized location and gets the required tele item from bank
	 * @.pre script.getClient().isMembers() == true
	 * @.post (Teleport player to location)
	 */
	public void teleport() {
		player = script.getLocalPlayer().getTile();
		bank = script.getBank().getClosestBankLocation().getCenter();	
		
		double playerToBank = player.distance(bank);
		double playerToTarget = player.distance(target);
				//Math.sqrt(Math.pow((target.getX()-player.getX()), 2) +  Math.pow((target.getY()-player.getY()), 2));
		
		//double walkToBank = player.distance(bank);
				//Math.sqrt(Math.pow((bank.getX()-player.getX()), 2) +  Math.pow((bank.getY()-player.getY()), 2));
		//double teleportToTarget = Math.sqrt(Math.pow((target.getX()-teleport.getX()), 2) +  Math.pow((target.getY()-teleport.getY()), 2));
		
		double teleportToBank = teleport.distance(tele.getBankTile());
		double bankToTarget = tele.getBankTile().distance(target);
		
		double teleporter = playerToBank + teleportToBank + bankToTarget;
		double walk = playerToBank + playerToTarget;
		
		script.log("playerTobank: " + playerToBank + "playerToTarget: " + playerToTarget +" teleportToBank: " + teleportToBank + " bankToTarget: " + bankToTarget);
		script.log("walk: " + walk + " teleporter: " + teleporter);
		if(walk > teleporter) {
			tele.getItem();
			script.sleep(random.nextInt(250) + 500);
			tele.useTeleport();
			//TODO: ADD check for not found teleporters 
		}
		
			
	}
	
	/**
	 * Counts the nearest teleport location from the teleLocations list
	 * @.pre true
	 * @.post (Closest teleLocation tile)
	 */
	public Tile getClosestTeleport() {
		double close = 999999999;
		Tile closest = new Tile(0, 0);
		for(TeleportItem item : teleLocations) {
			Tile tile = item.getTile();
			if((Math.sqrt(Math.pow((target.getX()-tile.getX()), 2) +  Math.pow((target.getY()-tile.getY()), 2))) < close) {
				close = (Math.sqrt(Math.pow((target.getX()-tile.getX()), 2) +  Math.pow((target.getY()-tile.getY()), 2)));
				closest = tile;
				this.tele = item;
			}
		}
		return closest;
	}
	
	/**
	 * Getter for teleLocations
	 * @.pre true
	 * @.post RESULT == Teleporter.teleLocations
	 */
	public ArrayList<TeleportItem> getTeleLocations() {
		return this.teleLocations;
	}
}
