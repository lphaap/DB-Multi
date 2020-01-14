package client;

public interface KillableThread extends Runnable{
	/**
	 * Kills this thread aka. stops the runnable loop
	 */
	public void killThread();
	
	/**
	 * Returns true if the run() loops is still true
	 */
	public boolean isAlive();

}
