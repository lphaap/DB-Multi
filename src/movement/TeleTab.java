package movement;
import org.dreambot.api.methods.map.Tile;

import init.ClientThread;

public class TeleTab extends TeleportItem{

	public TeleTab(ClientThread script, Tile location, Tile bank, String locationname, String itemName) {
		super(script, location, bank, locationname, "Break", itemName);
		
	}

	@Override
	public boolean useTeleport() {
		if(script.getInventory().contains(f -> f != null && f.getName().contains(this.itemName))) {
			script.getInventory().get(f -> f != null && f.getName().contains(this.itemName)).interact(action);
			script.sleep(random.nextInt(750)+ 1000);
			return true;
		}
		else {
			return false;
		}
		
	}

	
	
}
