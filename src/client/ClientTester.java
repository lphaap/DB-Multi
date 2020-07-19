package client;

import java.awt.Point;

import org.dreambot.api.methods.skills.Skill;

import antiban.RandomProvider;
import movement.LocationFactory.GameLocation;
import movement.LocationFactory;
import scripts.ScriptModule;
import utilities.GearHandler.Gear;
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
		setup();
		while(!killThread) {

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
	
	private void setup() {
		new Thread( () -> {
			while(!killThread) {
				RandomProvider.sleep(50,100);
				controller.getGraphicHandler().setInfo(
						"Tile: " + client.getLocalPlayer().getTile() +
						" Animating: " + client.getLocalPlayer().isAnimating());
				controller.getGraphicHandler().setScriptTimer(
						"InCombat: " + client.getLocalPlayer().isInCombat() +
						" Players: " + client.getPlayerCount());
				controller.getGraphicHandler().setPauseTimer(
						"NPC: " + client.getNpcs().closest(f -> f != null).getName() +
						" G-O: " + client.getGameObjects().closest(f -> f != null).getName()
	 					);
			}
			}).start();
		controller.getGraphicHandler().toggleTester();
		controller.getAntiBanHandler().pauseAllAntibanThreads();
	}

}
