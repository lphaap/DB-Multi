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
	private ArrayList<PauseableThread> paused = new ArrayList<PauseableThread>();
	private ArrayList<PauseableThread> active = new ArrayList<PauseableThread>();
	
	private Pauser pauser = new Pauser();
	private Resumer resumer = new Resumer();
	private ListHandler listHandler = new ListHandler();
	
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
		
	private class ListHandler implements KillableThread {
		private boolean killThread;

		
		@Override
		public void run() {
			while(!killThread) {
				RandomProvider.sleep(1000, 1500);
				for(PauseableThread t : active) {
					if(t.isPaused()) {
						paused.add(t);
						active.remove(t);
					}
				}
				for(PauseableThread t : paused) {
					if(!t.isPaused()) {
						active.add(t);
						paused.remove(t);
					}
				}
			}
			
		}

		@Override
		public void killThread() {
			this.killThread  = true;
		}
		
		@Override
		public boolean isAlive() {
			return !this.killThread;
		}

		
	}
	
	private class Resumer implements KillableThread, PauseableThread {
		private boolean killThread;
		private boolean pauseThread;
		
		@Override
		public void run() {
			while(!killThread) {
				RandomProvider.sleep(30000, 90000);
				controller.debug("Paused AB: " + paused.size());
				controller.debug("Active AB: " + active.size());
				if(!this.pauseThread) {
					if(paused.size() >= 5) {
						Collections.shuffle(paused);
						if(paused.get(0) != null) {
							paused.get(0).resumeThread();
						}
						Collections.shuffle(paused);
						if(paused.get(0) != null) {
							paused.get(0).resumeThread();
						}
					}
					else {
						Collections.shuffle(paused);
						if(paused.get(0) != null) {
							paused.get(0).resumeThread();
						}
					}
				}
			}
			
		}

		@Override
		public void killThread() {
			this.killThread  = true;
		}
		
		@Override
		public boolean isAlive() {
			return !this.killThread;
		}

		@Override
		public void pauseThread() {
			this.pauseThread = true;
		}

		@Override
		public void resumeThread() {
			this.pauseThread = false;
		}

		@Override
		public boolean isPaused() {
			return this.pauseThread;
		}
		
	}
	
	private class Pauser implements KillableThread, PauseableThread {
		private boolean killThread;
		private boolean pauseThread;
		
		@Override
		public void run() {
			while(!killThread) {
				RandomProvider.sleep(30000, 90000);
				controller.debug("Paused AB: " + paused.size());
				controller.debug("Active AB: " + active.size());
				if(!this.pauseThread) {
					if(active.size() >= 5) {
						Collections.shuffle(active);
						if(active.get(0) != null) {
							active.get(0).pauseThread();
						}
						Collections.shuffle(active);
						if(active.get(0) != null) {
							active.get(0).pauseThread();
						}
					}
					else {
						Collections.shuffle(active);
						if(active.get(0) != null) {
							active.get(0).pauseThread();
						}
					}
				}
			}
			
		}

		@Override
		public void killThread() {
			this.killThread  = true;
		}
		
		@Override
		public boolean isAlive() {
			return !this.killThread;
		}

		@Override
		public void pauseThread() {
			this.pauseThread = true;
		}

		@Override
		public void resumeThread() {
			this.pauseThread = false;
		}

		@Override
		public boolean isPaused() {
			return this.pauseThread;
		}
		
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
			this.resumer.pauseThread();
			this.pauser.pauseThread();
			
			this.hoverer.pauseThread();
			this.mouseMove.pauseThread();
			this.mouseOffMove.pauseThread();
			this.cameraMove.pauseThread();
			this.examiner.pauseThread();
			this.runEnergyListener.pauseThread();
	}
	
	public void resumeAllAntibanThreads() {
			this.resumer.resumeThread();
			this.pauser.resumeThread();
		
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
				RandomProvider.sleep(23000, 32000);
				if(this.killHandler) {
					controller.debug("Breaking AB starter");
					return;
				}
				new Thread(thread).start();
				controller.debug("Antiban " + threads.indexOf(thread) + " Started");
				if(!thread.equals(this.runEnergyListener)) {
					this.active.add((PauseableThread)thread);
				}
			}
			new Thread( (pauser)).start();
			new Thread( (resumer)).start();
			new Thread( (listHandler)).start();
			
		}).start();
	}
	
	@Override
	public void killHandler() {
		controller.debug("Killing AB");
		this.killHandler = true;
		
		this.cameraMove.killThread();
		this.examiner.killThread();
		this.mouseMove.killThread();
		this.runEnergyListener.killThread();
		this.mouseOffMove.killThread();
		this.hoverer.killThread();
		
		this.pauser.killThread();
		this.resumer.killThread();
		this.listHandler.killThread();
		controller.debug("AB KILLED");
	}

}
