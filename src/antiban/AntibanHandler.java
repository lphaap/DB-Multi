package antiban;

import java.util.ArrayList;
import java.util.Collections;

import client.ClientThread;
import client.KillableHandler;
import client.KillableThread;
import client.PauseableThread;
import client.ThreadController;
import scripts.ScriptModule;

public class AntibanHandler implements KillableHandler{
	private ThreadController controller;
	private ClientThread client;
	
	private RandomExaminer examiner;
	private StatsHovering hoverer;
	private MouseOffScreenMovement mouseOffMove;
	private MouseMovement mouseMove;
	private CameraRotate cameraMove;
	private RunEnergyListener runEnergyListener;
	private ArrayList<KillableThread> threads = new ArrayList<KillableThread>();
	
	private boolean killHandler;
	
	public AntibanHandler(ClientThread client, ThreadController controller) {
		this.client = client;
		this.controller = controller;
		
		this.cameraMove = new CameraRotate(client, controller);
		this.mouseOffMove = new MouseOffScreenMovement(client, controller);
		this.examiner = new RandomExaminer(client, controller);
		this.hoverer = new StatsHovering(client, controller);
		this.mouseMove = new MouseMovement(client, controller);
		this.runEnergyListener = new RunEnergyListener(controller,client);
		
		threads.add(runEnergyListener);
		threads.add(cameraMove);
		threads.add(mouseOffMove);
		threads.add(examiner);
		threads.add(hoverer);
		threads.add(mouseMove);
		
		Collections.shuffle(threads);
	}
	
	public enum AntiBanThread {
		STATS_HOVERING, MOUSE_MOVEMENT, MOUSE_MOVEMENT_OFF_SCREEN, ENTITY_EXAMINER, CAMERA_MOVEMENT, ENERGY_LISTENER
	}
	
	public void pauseAntibanThread(AntiBanThread thread) {
		if(thread == AntiBanThread.STATS_HOVERING) {
			this.hoverer.pauseThread();
		}
		else if(thread == AntiBanThread.MOUSE_MOVEMENT) {
			this.mouseMove.pauseThread();
		}
		else if(thread == AntiBanThread.MOUSE_MOVEMENT_OFF_SCREEN) {
			this.mouseOffMove.pauseThread();
		}
		else if(thread == AntiBanThread.CAMERA_MOVEMENT) {
			this.cameraMove.pauseThread();
		}
		else if(thread == AntiBanThread.ENTITY_EXAMINER) {
			this.examiner.pauseThread();
		}
		else if(thread == AntiBanThread.ENERGY_LISTENER) {
			this.runEnergyListener.pauseThread();
		}
	}
	
	public void resumeAntibanThread(AntiBanThread thread) {
		if(thread == AntiBanThread.STATS_HOVERING) {
			this.hoverer.resumeThread();
		}
		else if(thread == AntiBanThread.MOUSE_MOVEMENT) {
			this.mouseMove.resumeThread();
		}
		else if(thread == AntiBanThread.MOUSE_MOVEMENT_OFF_SCREEN) {
			this.mouseOffMove.resumeThread();
		}
		else if(thread == AntiBanThread.CAMERA_MOVEMENT) {
			this.cameraMove.resumeThread();
		}
		else if(thread == AntiBanThread.ENTITY_EXAMINER) {
			this.examiner.resumeThread();
		}
		else if(thread == AntiBanThread.ENERGY_LISTENER) {
			this.runEnergyListener.resumeThread();
		}
	}
	
	public void pauseAllAntibanThreads() {
			this.hoverer.pauseThread();
			this.mouseMove.pauseThread();
			this.mouseOffMove.pauseThread();
			this.cameraMove.pauseThread();
			this.examiner.pauseThread();
			this.runEnergyListener.pauseThread();
	}
	
	public void resumeAllAntibanThreads() {
			this.hoverer.resumeThread();
			this.mouseMove.resumeThread();
			this.mouseOffMove.resumeThread();
			this.cameraMove.resumeThread();
			this.examiner.resumeThread();
			this.runEnergyListener.resumeThread();
	}
	
	public void startAllAntibanThreads() {
		
		new Thread(() -> { 
			for(KillableThread thread : threads) {
				RandomProvider.sleep(23000, 42000);
				if(this.killHandler) {
					break;
				}
				new Thread(thread).start();
				controller.debug("Antiban " + threads.indexOf(thread) + " Started");
			}
		}).start();
	}
	
	@Override
	public void killHandler() {
		this.killHandler = true;
		
		for(KillableThread thread : threads) {
			
			thread.killThread();
		}
	}

}
