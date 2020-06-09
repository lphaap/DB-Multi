package antiban;

import java.util.ArrayList;

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
	private ArrayList<KillableThread> threads = new ArrayList<KillableThread>();
	
	public AntibanHandler(ClientThread client, ThreadController controller) {
		this.client = client;
		this.controller = controller;
		
		this.examiner = new RandomExaminer(client, controller);
		this.hoverer = new StatsHovering(client, controller);
		this.mouseOffMove = new MouseOffScreenMovement(client, controller);
		this.mouseMove = new MouseMovement(client, controller);
		this.cameraMove = new CameraRotate(client, controller);
		
		threads.add(examiner);
		threads.add(hoverer);
		threads.add(mouseOffMove);
		threads.add(mouseMove);
		threads.add(cameraMove);
		
		this.pauseAllAntibanThreads();
		
		for(KillableThread thread : threads) {
			new Thread(thread).start();
		}
	}
	
	public enum AntiBanThread {
		STATS_HOVERING, MOUSE_MOVEMENT, MOUSE_MOVEMENT_OFF_SCREEN, ENTITY_EXAMINER, CAMERA_MOVEMENT
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
	}
	
	public void resumeAntibanThread(AntiBanThread thread) {
		if(thread == AntiBanThread.STATS_HOVERING) {
			this.hoverer.resumeThread();
		}
		else if(thread == AntiBanThread.MOUSE_MOVEMENT) {
			this.mouseMove.resumeThread();
		}
		else if(thread == AntiBanThread.MOUSE_MOVEMENT_OFF_SCREEN) {
			this.mouseOffMove.pauseThread();
		}
		else if(thread == AntiBanThread.CAMERA_MOVEMENT) {
			this.cameraMove.resumeThread();
		}
		else if(thread == AntiBanThread.ENTITY_EXAMINER) {
			this.examiner.resumeThread();
		}
	}
	
	public void pauseAllAntibanThreads() {
			this.hoverer.pauseThread();
			this.mouseMove.pauseThread();
			this.mouseOffMove.pauseThread();
			this.cameraMove.pauseThread();
			this.examiner.pauseThread();
	}
	
	public void resumeAllAntibanThreads() {
			this.hoverer.resumeThread();
			this.mouseMove.resumeThread();
			this.mouseOffMove.pauseThread();
			this.cameraMove.resumeThread();
			this.examiner.resumeThread();
	}
	
	@Override
	public void killHandler() {
		for(KillableThread thread : threads) {
			thread.killThread();
		}
	}

}
