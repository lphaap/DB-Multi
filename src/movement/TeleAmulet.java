package movement;
import org.dreambot.api.methods.map.Tile;

import init.ClientThread;

public class TeleAmulet extends TeleportItem{
	private int actionNumber;
	
	public TeleAmulet(ClientThread script, Tile location, Tile bank, String locationname, String itemName, int actionNumber) {
		super(script, location, bank, locationname, "Rub", itemName);
		this.actionNumber = actionNumber;
	}

	@Override
	public boolean useTeleport() {
		if(script.getInventory().contains(f -> f != null && f.getName().contains(this.itemName))) {
			script.getInventory().get(f -> f != null && f.getName().contains(this.itemName)).interact(action);
			script.sleep(random.nextInt(750)+ 1000);
			script.getKeyboard().type(actionNumber);
			return true;
		}
		else {
			return false;
		}
	}
	
	
	
}
