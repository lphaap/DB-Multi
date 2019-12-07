import java.util.Random;

import org.dreambot.api.methods.map.Tile;

public abstract class TeleportItem {
	protected String locationName;
	protected String itemName;
	protected String action;
	protected Tile xy;
	protected MainLooper script;
	protected Random random;
	protected Tile bankTile;
	
	public TeleportItem(MainLooper script, Tile location, Tile bank, String locationname, String action, String itemName) {
		this.xy = location;
		this.locationName = locationName;
		this.action = action;
		this.itemName = itemName;
		this.script = script;
		this.random = new Random();
		this.bankTile = bank;
	}
	
	public boolean getItem() {
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
		
		if(script.getBank().contains(f -> f != null && f.getName().contains(itemName))) {
			script.getBank().withdraw(f -> f != null &&  f.getName().contains(itemName));
			script.sleep(random.nextInt(750)+ 1000);
			script.getBank().close();
			
			return true;
		}
		else {
			return false;
		}
	}
	
	public abstract boolean useTeleport();
	
	public Tile getTile() {
		return this.xy;
	}
	public String getLocationName() {
		return this.locationName;
	}
	public Tile getBankTile() {
		return this.bankTile;
	}

	
	
	}
