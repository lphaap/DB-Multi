package utilities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import client.ThreadController;

public class GraphicHandler {
	private String info; 
	private String pause;
	private String pauseTimer;
	private String scriptTimer;
	private Font generalFont;
	private boolean isOnPause;
	private boolean useTester;
	private boolean onManualPause;
	private ThreadController controller;
	
	public GraphicHandler(ThreadController controller) {
		this.generalFont = new Font("Arial", Font.BOLD, 15);
		this.info = "";
		this.pause = "";
		this.pauseTimer = "";
		this.scriptTimer = "";
		this.controller = controller;
	}
	
	public void handleGraphics(Graphics g) {
		if(useTester) {
			g.setColor(Color.PINK);
			g.setFont(generalFont);
			g.drawString(this.info,5,334);
			g.drawString(this.scriptTimer, 5, 314);
			g.drawString(this.pauseTimer, 5, 294);
		}
		else if(onManualPause) {
			this.scriptTimer = "MANUAL PAUSE";
			this.pauseTimer  = "MANUAL PAUSE";
			this.info = "MANUAL PAUSE";
			g.setColor(Color.RED);
			g.setFont(generalFont);
			g.drawString(this.info,5,334);
			g.drawString(this.scriptTimer, 5, 314);
			g.drawString(this.pauseTimer, 5, 294);
		}
		else if(!isOnPause) {
			this.scriptTimer = "Time left in Script: " + controller.timeLeftInScript() + " Minutes";
			this.pauseTimer = "Time Until Pause: " + controller.timeLeftUntillPause() + " Minutes";
			
			g.setColor(Color.YELLOW);
			g.setFont(generalFont);
			g.drawString(this.info,5,334);
			g.setColor(Color.RED);
			g.drawString(this.scriptTimer, 5, 314);
			g.setColor(Color.MAGENTA);
			g.drawString(this.pauseTimer, 5, 294);
		}
		else {
			g.setColor(Color.MAGENTA);
			g.setFont(generalFont);
			g.drawString(this.pause, 5, 20);
		}
	}

	
	
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getPause() {
		return pause;
	}
	public void setPause(String pause) {
		this.pause = pause;
	}
	public String getPauseTimer() {
		return pauseTimer;
	}
	public void setPauseTimer(String pauseTimer) {
		this.pauseTimer = pauseTimer;
	}
	public String getScriptTimer() {
		return scriptTimer;
	}
	public void setScriptTimer(String scriptTimer) {
		this.scriptTimer = scriptTimer;
	}
	public void togglePause() {
		this.isOnPause = !this.isOnPause;
	}
	
	public void toggleManualPause() {
		this.onManualPause = !this.onManualPause;
	}
	
	public void toggleTester() {
		this.useTester = !useTester;
	}
	
	
	
	
}
