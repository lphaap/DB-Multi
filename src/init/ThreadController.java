package init;

import java.util.ArrayList;
import java.util.List;

import scripts.ScriptModule;

public class ThreadController {
	private boolean keyboardInUse;
	private boolean mouseInUse;
	private ClientThread client;
	private ArrayList<KillableThread> threads = new ArrayList<KillableThread>();
	private ArrayList<ScriptModule> modules = new ArrayList<ScriptModule>();
	private ScriptModule currentModule;
	
	public ThreadController(ClientThread client) {
		this.client = client;
	}
	
	
	public synchronized boolean requestKeyboardAccess() {
		if(keyboardInUse || client.getKeyboard().isTyping()) {
			return false;
		}
		else {
			keyboardInUse = true;
			return true; 
		}
		
	}
	
	public synchronized boolean requestMouseAccess() {
		if(mouseInUse) {
			return false;
		}
		else {
			mouseInUse = true;
			return true; 
		}
	}
	
	public synchronized void returnKeyboardAccess() {
		this.keyboardInUse = false;
	}
	
	public synchronized void returnMouseAccess() {
		this.mouseInUse = false;
	}	
	
	public ScriptModule getCurrentTask() {
		return this.currentModule;
	}
}