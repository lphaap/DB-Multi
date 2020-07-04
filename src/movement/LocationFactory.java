package movement;

import java.util.ArrayList;
import java.util.Random;

import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;

import client.ClientThread;


public class LocationFactory {
	
	public static Location newLocation(ClientThread script, GameLocation l) throws IllegalArgumentException {
			Location re = new Location();
			ArrayList<Obstacle> reObs = new ArrayList<Obstacle>();
			re.setClient(script);
			
			//-- REMINDER: Player Must be within 4 tiles of obstacle after movement to be able to interact--//
			
			if(l == GameLocation.MINER_EAST_VARROCK) {
				re.setTargetArea(new Tile(3285,3365).getArea(4));
				re.setTeleporter(new Teleporter(script, new Tile(3285,3365).getArea(4)));
				re.setObstacles(reObs);
			}
			else if(l == GameLocation.MINER_LUMBRIDGE) {
				re.setTargetArea(new Tile(3226,3147).getArea(6));
				re.setTeleporter(new Teleporter(script, new Tile(3226,3147).getArea(6)));
				re.setObstacles(reObs);
			}
			else if(l == GameLocation.MINER_WEST_VARROCK) {
				re.setTargetArea(new Tile(3182,3370).getArea(10));
				re.setTeleporter(new Teleporter(script, new Tile(3182,3370).getArea(10)));
				re.setObstacles(reObs);
			}
			else if(l == GameLocation.SMELTER_AL_KHARID) {
				re.setTargetArea(new Tile(3276,3186).getArea(2));
				re.setTeleporter(new Teleporter(script, new Tile(3276,3186).getArea(2)));
				re.setObstacles(reObs);
			}
			else if(l == GameLocation.SMITHING_WEST_VARROCK) {
				re.setTargetArea(new Tile(3187,3425).getArea(2));
				re.setTeleporter(new Teleporter(script, new Tile(3187,3425).getArea(2)));
				re.setObstacles(reObs);
			}
			else if(l == GameLocation.FISHING_AL_KHARID) {
				re.setTargetArea(new Tile(3267,3149).getArea(9));
				re.setTeleporter(new Teleporter(script, new Tile(3267,3149).getArea(9)));
				re.setObstacles(reObs);
			}
			else if(l == GameLocation.COOKING_AL_KHARID) {
				re.setTargetArea(new Tile(3273,3180).getArea(2));
				re.setTeleporter(new Teleporter(script, new Tile(3273,3180).getArea(2)));
				re.setObstacles(reObs);
			}
			else if(l == GameLocation.FISHING_BARBARIAN_VILLAGE) {
				re.setTargetArea(new Tile(3105,3430).getArea(7));
				re.setTeleporter(new Teleporter(script, new Tile(3105,3430).getArea(7)));
				re.setObstacles(reObs);
			}
			else if(l == GameLocation.COMBAT_GIANT_FROG) {
				re.setTargetArea(new Tile(3198,3176).getArea(10));
				re.setTeleporter(new Teleporter(script, new Tile(3198,3176).getArea(10)));
				re.setObstacles(reObs);
			}
			else if(l == GameLocation.COMBAT_BARBARIAN) {
				//re.setPhase2(new Tile(3079, 3433).getArea(2));
				//re.setObstacle2(new Obstacle(script, "Longhall door", "Open")); 
				reObs.add(new Obstacle(script, "Longhall door", "Open", 
						  new Tile(3079, 3433).getArea(2), new Tile(3078, 3438).getArea(2)));
				re.setTargetArea(new Tile(3078,3440).getArea(5));
				re.setTeleporter(new Teleporter(script, new Tile(3079, 3433).getArea(2)));
				re.setObstacles(reObs);
			}
			else if(l == GameLocation.MINER_CRAFTING_GUILD_GOLD) {
				//re.setPhase2(new Tile(2933,3290).getArea(1)); 3078 3438 2
			//	re.setObstacle2(new Obstacle(script, "Guild Door", "Open")); 
				//re.setPhase1(new Tile(2940,3279).getArea(3));
				re.setTeleporter(new Teleporter(script, new Tile(2933,3290).getArea(1)));
				re.setObstacles(reObs);
			}
			else if(l == GameLocation.SPLASHING_BEAR) {
				re.setTargetArea(new Tile(3225,3498).getArea(4));
				re.setTeleporter(new Teleporter(script, new Tile(3225,3498).getArea(4)));
				re.setObstacles(reObs);
			}
			else if(l == GameLocation.SMELTER_EDGEVILLE) {
				re.setTargetArea(new Tile(3108,3499).getArea(1));
				re.setTeleporter(new Teleporter(script, new Tile(3108,3499).getArea(1)));
				re.setObstacles(reObs);
			}
			else if(l == GameLocation.FISHING_CATHERBY) {
				re.setTargetArea(new Area(2832,3435, 2862,3424));
				re.setTeleporter(new Teleporter(script, new Area(2832,3435, 2862,3424)));
				re.setObstacles(reObs);
			}
			/*else if(l == GameLocation.COMBAT_EXPERIMENTS) {
				re.setPhase2(new Tile(3588,3533).getArea(2));
				re.setObstacle2(new Obstacle(script, "Memorial", "Ladder", "Push",  "Climb-up")); 
				re.setPhase1(new Tile(3483,9938).getArea(15));
				re.setTeleporter(new Teleporter(script, new Tile(3588,3533).getArea(2)));
				re.setObstacles(reObs);
			}*/
			else if(l == GameLocation.COOKING_CATHERBY) {
				re.setTargetArea(new Tile(2817,3443).getArea(1));
				re.setTeleporter(new Teleporter(script, new Tile(2817,3443).getArea(1)));
				re.setObstacles(reObs);
			}
			else if(l == GameLocation.LOCATION_TESTER) {
				reObs.add(new Obstacle(script, "Castle door", "Open", 
						  new Tile(2981, 3341).getArea(1), new Tile(2982, 3341).getArea(1)));
				reObs.add(new Obstacle(script, "Door", "Open", 
						  new Tile(2985, 3341).getArea(1), new Tile(2986, 3341).getArea(1)));
				reObs.add(new Obstacle(script, "Door", "Open", 
						  new Tile(2990, 3341).getArea(1), new Tile(2991, 3341).getArea(1)));
				reObs.add(new Obstacle(script, "Ladder", "Climb-up", "Ladder", "Climb-down", 
						  new Tile(2993, 3341).getArea(1), new Tile(2991, 3341).getArea(1)));
				reObs.add(new Obstacle(script, "Door", "Open", 
						  new Tile(2991, 3341, 1).getArea(1), new Tile(2992, 3341,1).getArea(1)));
				reObs.add(new Obstacle(script, "Staircase", "Climb-down", "Staircase", "Climb-up", 
						  new Tile(2968, 3348,1).getArea(1), new Tile(2971, 3347,1).getArea(1)));
	
				re.setTargetArea(new Tile(2973,3342).getArea(1));
				re.setTeleporter(new Teleporter(script, new Tile(2817,3443).getArea(1)));
				re.setObstacles(reObs); 
			}
			else {
				script.log("");
			
		}
			if(re == null) {
				throw new IllegalArgumentException("Factory hasn't implemented this GameLocation yet");
			}
			return re;
	}
	
	
	public enum GameLocation {
		/*
		 * Enum constants for different locations
		 * Used in class Location
		 */
			LOCATION_TESTER, MINER_EAST_VARROCK, MINER_LUMBRIDGE, MINER_WEST_VARROCK, MINER_DWARVEN_MINE_COAL, 
			SMELTER_AL_KHARID, SMITHING_WEST_VARROCK, FISHING_AL_KHARID, FISHING_BARBARIAN_VILLAGE, 
			COOKING_AL_KHARID, COMBAT_GIANT_FROG, COMBAT_BARBARIAN, MINER_CRAFTING_GUILD_GOLD, SPLASHING_BEAR,
			SMELTER_EDGEVILLE, FISHING_CATHERBY, COMBAT_EXPERIMENTS, COOKING_CATHERBY
	}
}
