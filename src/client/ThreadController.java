package client;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.security.auth.login.LoginException;

import org.dreambot.api.methods.skills.Skill;

import antiban.RandomProvider;
import chat.Discord;
import chat.MsgHandler;
import movement.MovementHandler;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import scripts.ScriptModule;

public class ThreadController implements KillableThread{
	private ClientThread client; //-> Will be passed to other classes since it's used so reqularly
	
	private boolean killThread;
	
	private boolean keyboardInUse;
	private boolean mouseInUse;
	

	private ScriptModule currentModule;
	private ArrayList<ScriptModule> modules = new ArrayList<ScriptModule>();
	private ArrayList<KillableThread> antibanThreads = new ArrayList<KillableThread>();
	
	private GraphicHandler graphic;
	private MsgHandler msgHandler;
	private MovementHandler movement;
	private Discord discord;
	
	private int keyboardInUseFor;
	private int mouseInUseFor;
	
	private int scriptTimer;
	private int pauseTimer;
	private int onPause;
	
	public ThreadController(ClientThread client) {
		this.client = client;
		this.graphic = new GraphicHandler();
		this.msgHandler = new MsgHandler(client, this);
		this.movement = new MovementHandler(client, this);
		this.pauseTimer = RandomProvider.randomInt(90*60, 125*60); 
		this.scriptTimer = RandomProvider.randomInt(180*60, 280*60);
		createDiscordThread();
		
		modules.add(null);
		modules.add(null);
		
		currentModule = modules.get(0);
		currentModule.setupModule();
		Thread thread = new Thread(currentModule);
		thread.start();
		
	}
	
	@Override
	public void run() {
		Thread.currentThread().setPriority(Thread.NORM_PRIORITY + 1);
		while(!this.killThread) {
			sleep(1000); //Tics every 1 second
			
			//--Checks when to change Module--//
			if(currentModule.isReady()) {
				currentModule.killThread();
				nextModule();
			}
			else if(!currentModule.isAlive()) {
				currentModule.killThread();
				nextModule();
			}
			//--Checks when to change Module--//
			
			//--PauseTimer--//
				this.pauseTimer--;
				if(pauseTimer <= 0) {
					pauseBot();
					pauseTimer = RandomProvider.randomInt(90*60, 125*60);
				}
			//--PauseTimer--//
			
			
			//--ScriptTimer--//
				this.scriptTimer--;
				if(scriptTimer <= 0) {
					killBot();
				}
			//--ScriptTimer--//
			
			
			//--If Thread Dosen't release Mouse or Keyboard--//
				keyboardInUseFor++;
				mouseInUseFor++;
				if(keyboardInUseFor >= 100) {
					System.out.println("Keyboard In Use For Too Long!");
					this.keyboardInUseFor = 0;
					//TODO:this.keyboardInUse = false;
				}
				if(mouseInUseFor >= 100) {
					System.out.println("Mouse In Use For Too Long!");
					this.mouseInUseFor = 0;
					//TODO:this.mouseInUse = false;
				}
			//--If Thread Dosen't release Mouse or Keyboard--//
		}
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
		this.keyboardInUseFor = 0;
		this.keyboardInUse = false;
	}
	
	public synchronized void returnMouseAccess() {
		this.mouseInUseFor = 0;
		this.mouseInUse = false;
	}
	
	public synchronized void restartModule() {
		//TODO:
	}
	
	public String getCurrentActionPrint() {
		return this.currentModule.getModuleName();
	}
	
	public Skill getCurrentSkill() {
		return this.currentModule.getSkillToHover();
	}
	
	private ScriptModule getCurrentTask() {
		return this.currentModule;
	}
	
	public GraphicHandler getGraphicHandler() {
		return this.graphic;
	}
	
	public MsgHandler getMsgHandler() {
		return this.msgHandler;
	}
	
	public Discord getDiscord() {
		return this.discord;
	}
	
	public void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {e.printStackTrace();}
	}
	
	public void createDiscordThread() {
		JDABuilder builder = new JDABuilder(AccountType.BOT);
        String token = "NjY0NTQ1NTcwMTUyNTEzNTQ3.XhZZfA.t42nR6nQJex7A9VsK9aBCT4zNZ4";
      
        Discord discord = new Discord(client, this); 
        
        builder.setToken(token);
        builder.addEventListeners(discord);
        try {
			builder.build();
		} catch (LoginException e) {e.printStackTrace();}
        this.discord = discord;
	}
	
	public void pauseBot(int seconds) {
		discord.sendMessage("Pausing Bot...");
		
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		while(this.requestKeyboardAccess()) {RandomProvider.sleep(10);}
		while(this.requestMouseAccess()) {RandomProvider.sleep(10);}
		
		client.logOut();
		
		int sleep = seconds;
		
		Calendar today = new GregorianCalendar();
		int hour = today.get(Calendar.HOUR);
		int min = today.get(Calendar.MINUTE);
		int minFinal = min + (sleep/60);
		if(minFinal > 60) {
			minFinal = minFinal - 60; 
			hour++;
		}
		
		this.graphic.setInfo("Pause Stop: " + hour + ":" + minFinal);
		this.graphic.setPause("Pause Stop: " + hour + ":" + minFinal);
		this.graphic.togglePause();
		discord.sendMessage("Script Paused - Stop: " + hour + ":" + minFinal);
		
		sleep(sleep*1000);
		
		client.logIn();
		
		this.returnKeyboardAccess();
		this.returnMouseAccess();
		
		this.graphic.togglePause();
		
		Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
	}
	
	public void pauseBot() {
		
		discord.sendMessage("Pausing Bot...");
		
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		while(this.requestKeyboardAccess()) {RandomProvider.sleep(10);}
		while(this.requestMouseAccess()) {RandomProvider.sleep(10);}
		
		client.logOut();
		
		int sleep = RandomProvider.randomInt(12*60, 26*60);
		
		Calendar today = new GregorianCalendar();
		int hour = today.get(Calendar.HOUR);
		int min = today.get(Calendar.MINUTE);
		int minFinal = min + (sleep/60);
		if(minFinal > 60) {
			minFinal = minFinal - 60; 
			hour++;
		}
		
		this.graphic.setInfo("Pause Stop: " + hour + ":" + minFinal);
		this.graphic.setPause("Pause Stop: " + hour + ":" + minFinal);
		this.graphic.togglePause();
		discord.sendMessage("Script Paused - Stop: " + hour + ":" + minFinal);
		
		sleep(sleep*1000);
		
		client.logIn();
		
		this.returnKeyboardAccess();
		this.returnMouseAccess();
		
		this.graphic.togglePause();
		
		Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
	}
	
	public void killBot() {
		discord.sendMessage("Killing Bot..");
		for(KillableThread t : this.antibanThreads) {
			t.killThread();
		}
		this.currentModule.killThread();
		this.killThread();
		client.stop();
	}
	
	public void killClient() {
		discord.sendMessage("Killing Client..");
		for(KillableThread t : this.antibanThreads) {
			t.killThread();
		}
		this.currentModule.killThread();
		this.killThread();
		client.stop();
		System.exit(0);
	}
	
	//Avoid infinite loop if called from ClientThread().stop();
	public void killController() {
		for(KillableThread t : this.antibanThreads) {
			t.killThread();
		}
		this.currentModule.killThread();
		this.killThread();
	}
	
	public void hopWorlds() {
		
		discord.sendMessage("Hopping Worlds..");
		graphic.setInfo("Hopping Worlds..");
		
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		while(this.requestKeyboardAccess()) {RandomProvider.sleep(10);}
		while(this.requestMouseAccess()) {RandomProvider.sleep(10);}
		
		client.hopWorlds();
		
		this.returnKeyboardAccess();
		this.returnMouseAccess();
		
		Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
	}
	
	public void nextModule() {
		this.currentModule.killThread();
		this.modules.remove(currentModule);
		if(this.modules.size() <= 0) {
			this.killBot();
		}
		else {
			this.currentModule = modules.get(0);
			this.currentModule.setupModule();
			Thread t = new Thread(this.currentModule);
			t.start();
		}
	}

	@Override
	public void killThread() {
		this.killThread = true;
		
	}
	
	@Override
	public boolean isAlive() {
		return !(killThread);
	}

	public MovementHandler getMovementHandler() {
		return this.movement;
	}

	
}
