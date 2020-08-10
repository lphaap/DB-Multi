package scripts;

import antiban.RandomProvider;
import client.ClientThread;
import client.ThreadController;

public class SlayerModule extends CombatModule {

	public SlayerModule(ThreadController controller, ClientThread client, Monster monster, Food food, Potion potion,
			int limit, int timeLimitMins, int foodLimit, int potionLimit, Boolean pickUp, boolean prayer, Training skill) {
		super(controller, client, monster, food, potion, limit, timeLimitMins, foodLimit, potionLimit, pickUp, prayer, skill);
		// TODO Auto-generated constructor stub
	}

	
	public enum SlayerMonster {
		HELLHOUND
	}
	
	@Override
	public boolean setupModule() {
		
		this.controller.getMovementHandler().newLocation(this.locationEnum);
		
		this.controller.getMovementHandler().teleportToLocation();
		
		this.controller.getGearHandler().handleGearSwap(this.getGearToSwap());
		
		this.controller.getWorldHandler().setToActive();
		this.controller.getWorldHandler().setPlayerLimit(this.playerLimit);
		
		controller.getGraphicHandler().setInfo("Combat trainer: Setting up module");
		if(!script.getInventory().contains(f -> f != null && f.getName().equals(food))) {
			
			controller.getMovementHandler().locateBank();
			
			while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
			while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
			
			controller.debug("Mouse control: CombatModule");
			controller.debug("Keyboard control: CombatModule");
			
			if(!script.getInventory().isEmpty()) {
				script.getBank().depositAllItems();
				sleep(RandomProvider.randomInt(750)+ 1000);
			}
			
			if(script.getBank().contains(food)) {
				if(this.foodLimit <= 0 && !this.usePotions) {
					script.getBank().withdrawAll(f -> f != null && f.getName().equals(food));
				}
				else {
					script.getBank().withdraw(f -> f != null && f.getName().equals(food), this.foodLimit);
				}
				RandomProvider.sleep(400, 600);
				
				if(usePotions) {
					for(String p : potions) {
						if(script.getBank().contains((p+"4)"))) {
							script.getBank().withdraw(f -> f != null && f.getName().equals((p+"4)")), this.potionCount);
							RandomProvider.sleep(400, 600);
						}
					}
				}
				
				sleep(RandomProvider.randomInt(750)+ 700);
				script.getBank().close();
				sleep(RandomProvider.randomInt(750)+ 400);
				script.getMouse().move();
				
				controller.returnKeyboardAccess();
				controller.returnMouseAccess();
				
				if(usePotions) {
					new Thread(potionHandler).start();
				}
				
				new Thread(healingHandler).start();
				new Thread(antiPotionHandler).start();
				new Thread(grounditemHandler).start();
				new Thread(specHandler).start();
				return true;
			}
			else {
				controller.returnKeyboardAccess();
				controller.returnMouseAccess();
				return false;
			}

		}
		else {
			new Thread(healingHandler).start();
			return true;
		}
	}
}
