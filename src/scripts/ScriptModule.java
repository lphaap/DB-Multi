package scripts;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.skills.Skill;

import client.KillableThread;
import client.ThreadController;

public abstract class ScriptModule implements KillableThread{
	protected String moduleName;
	protected ThreadController controller;
	
	/**
	 * Runnable.run() method
	 * Does the loop action of the said Script module
	 */
	
	/**
	 * Return true if the current script has finished
	 * @return
	 */
	public abstract boolean isReady();
	
	/**
	 * Does everything needed, banking etc. to start this module
	 * Should Request own access to mouse and keyboard
	 */
	public abstract boolean setupModule();
	

	/**
	 * Gets the wanted skill which to hover for random event
	 * @return
	 */
	public abstract Skill getSkillToHover();
	
	
	/**
	 * Get a name to identify this module
	 */
	public String getModuleName() {
		return this.moduleName;
	}
	
	/**
	 * Thread sleep with try catch
	 * @param time
	 */
	public void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
