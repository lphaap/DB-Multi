package scripts;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.skills.Skill;

import client.KillableThread;

public abstract class ScriptModule implements KillableThread{
	protected String moduleName;
	
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
}
