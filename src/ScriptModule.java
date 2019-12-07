import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.skills.Skill;

public abstract class ScriptModule {
	protected String moduleName;
	
	/**
	 * Does the loop action of the said Script module
	 * @return sleep timer for main loop
	 */
	public abstract int onLoop();
	
	/**
	 * Return the amount of full actions that the module has completed
	 * @return
	 */
	public abstract int actionsCompleted();
	
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
	 * This method is meant to check initial errors in a script loop
	 */
	public abstract void errorTest();
	
	/**
	 * Get a name to identify this module
	 */
	public String getModuleName() {
		return this.moduleName;
	}
}
