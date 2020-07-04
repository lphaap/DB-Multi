package client;

import scripts.ScriptModule;

public class ScriptExceptionHandler {
	private ThreadController controller;
	private Thread.UncaughtExceptionHandler handler;
	
	private ScriptModule currentModule;
	
	private int exceptionCounter; 
	
	public ScriptExceptionHandler(ThreadController controller) {
		this.controller = controller;
		createHandler();
	}
	
	public void handleNewThread(ScriptModule module, Thread moduleThread) {
		this.exceptionCounter = 0;
		this.currentModule = module;
		(moduleThread).setUncaughtExceptionHandler(handler);
		
	}
	
	private void createHandler() {
		this.handler = new Thread.UncaughtExceptionHandler() {
		    public void uncaughtException(Thread th, Throwable ex) {
		        controller.debug("ERROR IN SCRIPT");
		        controller.debug("" + ex + " At: " + ex.getStackTrace()[0]);
		        ex.printStackTrace();
		        controller.debug("ERROR IN SCRIPT");
		        
		        controller.returnKeyboardAccess();
				controller.returnMouseAccess();
		        exceptionCounter++;
		        
		        if(exceptionCounter >= 10) {
		        	controller.debug("EXCEPTION OWERFLOW");
		        	controller.debug("FIX SCRIPT");

		        	currentModule.killThread();
		        }
		        else {
		        	if(currentModule.isAlive()) {
		        		controller.debug("Restarting Module Loop");

			        	Thread re = new Thread(currentModule);
			        	re.setUncaughtExceptionHandler(handler);
			        	re.start();
			        	
		        	}
		        }
		      
		    }
		};
	}
	
}
