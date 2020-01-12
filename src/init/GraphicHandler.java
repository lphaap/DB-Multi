package init;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class GraphicHandler {
	private String info; 
	private String pause;
	private String pauseTimer;
	private String scriptTimer;
	private Font generalFont;
	private boolean isOnPause;
	
	public GraphicHandler() {
		this.generalFont = new Font("Arial", Font.BOLD, 15);
		this.info = "";
		this.pause = "";
		this.pauseTimer = "";
		this.scriptTimer = "";
	}
	
	public void handleGraphics(Graphics g) {
		if(!isOnPause) {
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
	
	
	
	
	
}
