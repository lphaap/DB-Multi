package client;

import java.awt.Point;

import org.dreambot.api.methods.skills.Skill;

import antiban.RandomProvider;
import scripts.ScriptModule;
import utilities.WorldHandler;

public class ClientTester extends ScriptModule {
	boolean killThread;
	private ThreadController controller;
	private ClientThread client;
	
	public ClientTester(ThreadController controller, ClientThread client) {
		this.controller = controller;
		this.client = client;
	}
	
	@Override
	public void killThread() {
		this.killThread = true;

	}

	@Override
	public boolean isAlive() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void run() {
		WorldHandler w = controller.getWorldHandler();
		
		w.hopWorlds();
		
		RandomProvider.sleep(1000, 2000);
		
		w.hopWorlds();
		
		w.setToActive();
		w.setToBanking();
		w.setPlayerLimit(1);
		RandomProvider.sleep(9500,10000);
		
		w.setToUnBanking();
		
		while(!killThread) {
			
			
			
			
			/*
			RandomProvider.sleep(1000, 2000);
			
			while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);};
			controller.debug("TESTER KEY CONTROL");
			while(controller.requestMouseAccess()) {RandomProvider.sleep(10);};
			controller.debug("TESTER MOUSE CONTROL");
			
			int repeat = RandomProvider.randomInt(1,4);
			for(int i = 0; i < repeat; i++) {
				client.getMouse().move(new Point(RandomProvider.randomInt(RandomProvider.randomInt(40, 100),RandomProvider.randomInt(700, 750)),
												 RandomProvider.randomInt(RandomProvider.randomInt(40, 100),RandomProvider.randomInt(440, 490))));
				RandomProvider.sleep(200,400);
			}
			
			controller.returnKeyboardAccess();
			controller.returnMouseAccess();
			controller.debug("TESTER CONTROL RETURN");
			*/
		}

	}

	@Override
	public boolean isReady() {
		return false;
	}

	@Override
	public boolean setupModule() {
		return true;
	}

	@Override
	public Skill getSkillToHover() {
		return Skill.HITPOINTS;
	}

}
