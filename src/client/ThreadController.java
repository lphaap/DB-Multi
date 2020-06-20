package client;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.security.auth.login.LoginException;

import org.dreambot.api.methods.skills.Skill;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import antiban.AntibanHandler;
import antiban.AntibanHandler.AntiBanThread;
import antiban.RandomProvider;
import utilities.InGameMsgHandler;
import movement.LocationFactory;
import movement.MovementHandler;

import scripts.*;
import scripts.CombatModule.Food;
import scripts.CombatModule.Monster;
import scripts.CombatModule.Training;
import scripts.CookerModule.Cook;
import utilities.GearHandler;

public class ThreadController implements KillableThread{
	private ClientThread client; //-> Will be passed to other classes since it's used so reqularly
	
	private boolean killThread;
	
	private boolean keyboardInUse;
	private boolean mouseInUse;
	private boolean debug;
	
	private ScriptModule currentModule;
	private ArrayList<ScriptModule> modules = new ArrayList<ScriptModule>();
	
	private GraphicHandler graphicHandler;
	private InGameMsgHandler inGameMsgHandler;
	private MovementHandler movementHandler;
	private GearHandler gearHandler;
	private AntibanHandler antibanHandler;
	
	private int keyboardInUseFor;
	private int mouseInUseFor;
	
	private int scriptTimer;
	private int pauseTimer;
	private int onPause;
	
	public ThreadController(ClientThread client) {
		this.client = client; //Save bot-client for spreading to Scripts
		
		this.debug = true; //Set false for no Script info in console
		
		debug("Controller Start");
		
		//---Setup Handlers---//
		this.graphicHandler = new GraphicHandler();
		this.inGameMsgHandler = new InGameMsgHandler(client, this);
		this.movementHandler = new MovementHandler(client, this);
		this.gearHandler = new GearHandler(client, this);
		this.antibanHandler = new AntibanHandler(client, this);
		debug("Handlers loaded");
		//---Setup Handlers---//
		
		//---Setup Pause Parametres---//
		this.pauseTimer = RandomProvider.randomInt(90*60, 125*60); 
		this.scriptTimer = RandomProvider.randomInt(180*60, 280*60);
		//---Setup Pause Parametres---//
		
		//---Modules---//
		modules.add(null); //DO NOT REMOVE - Needed for the start with nextModule();
		//modules.add(new ClientTester(this, client));
		
		modules.add(new SmithingModule(this, client, SmithingModule.SmithingType.SCIMITAR, SmithingModule.SmithingMaterial.IRON, LocationFactory.GameLocation.SMITHING_WEST_VARROCK, 4));
		
		//modules.add(new CookerModule(this, client, LocationFactory.GameLocation.COOKING_AL_KHARID, Cook.SHRIMP, 8));
		//modules.add(new CombatModule(this, client, Monster.GIANT_FROG, Food.TROUT, 1, true, Training.STRENGTH));
		//modules.add(new FishingModule(this, client, FishingModule.Fish.HERRING, 2, false));
		
		//modules.add(new JewelleryModule(this, client, LocationFactory.GameLocation.SMELTER_AL_KHARID,
			//		JewelleryModule.JewelleryMaterial.GOLD, JewelleryModule.JewelleryType.NECKLACE, 10));
		
		//modules.add( new SmelterModule(client, this, LocationFactory.GameLocation.SMELTER_AL_KHARID, 10, SmelterModule.Bars.BRONZE));
		//modules.add(new MinerModule(client, this, LocationFactory.GameLocation.MINER_WEST_VARROCK, MinerModule.Ore.TIN_ORE, true, 1));
		
		//modules.add(new MinerModule(client, this, LocationFactory.GameLocation.MINER_EAST_VARROCK, MinerModule.Ore.COPPER_ORE, true, 2));
		//modules.add(new MinerModule(client, this, LocationFactory.GameLocation.MINER_WEST_VARROCK, MinerModule.Ore.COPPER_ORE, 2));
		//modules.add(new CombatModule(this, client, Monster.GIANT_FROG, Food.TROUT, 2, true, Training.STRENGTH));
		//---Modules---//
		
		//---Antiban StartUp---//
		this.antibanHandler.startAllAntibanThreads();
		//---Antiban StartUp---//
		
		debug("Controller Loaded");

	}
	
	@Override
	public void run() {
		
		Thread.currentThread().setPriority(Thread.NORM_PRIORITY + 1);
		
		//---Manual Start of 1st Module---//
		nextModule();
		//---Manual Start of 1st Module---//
		
		while(!this.killThread) {
			sleep(1000); //Tics every 1 second
			
			
			//--Checks when to change Module--//
			if(currentModule.isReady() || !currentModule.isAlive()) {
				
				currentModule.killThread();
				nextModule();
			}
			//--Checks when to change Module--//
			
			//--PauseTimer--//
				this.pauseTimer--;
				if(pauseTimer <= 0) {
					//pauseBot();
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
				if(!this.movementHandler.isInControl()) { //Check if movement handler has control (takes too long for debug)
					if(this.keyboardInUse) {
						keyboardInUseFor++;
					}
					if(this.mouseInUse) {
						mouseInUseFor++;
					}
				}
				if(keyboardInUseFor >= 100) {
					debug("ERROR");
					debug("Keyboard In Use For Too Long!");
					debug("ERROR");
					this.keyboardInUseFor = 0;
					this.killBot();
					//TODO:this.keyboardInUse = false;
				}
				if(mouseInUseFor >= 100) {
					debug("ERROR");
					System.out.println("Mouse In Use For Too Long!");
					debug("ERROR");
					this.mouseInUseFor = 0;
					this.killBot();
					//TODO:this.mouseInUse = false;
				}
			//--If Thread Dosen't release Mouse or Keyboard--//

		}
	}
	
	//Returns false to break asking loop if access granted otherwise true
	//Ask control -> while(controller.requestKeyboardAccess()) {RandomProvider.sleep(10);}
	public synchronized boolean requestKeyboardAccess() {
		if(keyboardInUse || client.getKeyboard().isTyping()) {
			return true;
		}
		else {
			keyboardInUse = true;
			return false; 
		}
		
	}
	
	//Returns false to break asking loop if access granted otherwise true
	//Ask control -> while(controller.requestMouseAccess()) {RandomProvider.sleep(10);}
	public synchronized boolean requestMouseAccess() {
		if(mouseInUse) { 
			return true;
		}
		else {
			mouseInUse = true;
			return false; 
		}
	}

	
	public synchronized void returnKeyboardAccess() {
		this.keyboardInUseFor = 0;
		//RandomProvider.sleep(950, 1050);
		sleep(300);
		this.keyboardInUse = false;
		
		debug("Keyboard control returned");
	}
	
	public synchronized void returnMouseAccess() {
		
		
		new Thread( () -> {
			Point p = client.getMouse().getPosition();
			//RandomProvider.sleep(950, 1050);
			while(true && !this.killThread) {
				sleep(300);
				if(p.equals(client.getMouse().getPosition())) {
					break;
				}
				debug("MOUSE MOVED SINCE RELEASE");
				p = client.getMouse().getPosition();
			}
			this.mouseInUse = false;
			this.mouseInUseFor = 0;
			debug("Mouse control returned");
		}).start();
		
		
		
	}
	
	//Samples if mouse is moving
	//return true if moving else false
	public synchronized boolean mouseInUse() {
		Point p = client.getMouse().getPosition();
		
		for(int i = 0; i < 20; i++) {
			sleep(10);
			if(!p.equals(client.getMouse().getPosition())) {
				return true;
			}
		}
		
		return false;
	}
	
	public synchronized void restartModule() {
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		while(this.requestKeyboardAccess()) {RandomProvider.sleep(10);}
		while(this.requestMouseAccess()) {RandomProvider.sleep(10);}
		
		this.currentModule.setupModule();
		
		this.returnMouseAccess();
		this.returnKeyboardAccess();
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
		return this.graphicHandler;
	}
	/*
	public TelegramHandler getTelegramHandler() {
		return this.telegramHandler;
	}*/
	
	public InGameMsgHandler getInGameMsgHandler() {
		return this.inGameMsgHandler;
	}
	
	public void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {e.printStackTrace();}
	}
	
	/*public static void main( String[] args ) {
		JDABuilder builder = new JDABuilder(AccountType.BOT);
        String token = "NjY0NTQ1NTcwMTUyNTEzNTQ3.XlmjOQ.t5pUs5Xh9zKJIeiwo6mbzQYG3J8";
      
        //Discord discord = new Discord(null, null); 
        
        builder.setToken(token);
        builder.addEventListeners(new Discord(null, null));
        try {
			builder.build();
		} catch (LoginException e) {e.printStackTrace();}
        //this.discord = discord;
	}
	
	private void connectTelegramThread() {
		debug("Phase 1");
		ApiContextInitializer.init();
		debug("Phase 2");
        TelegramBotsApi botsApi = new TelegramBotsApi();
        debug("Phase 3");
        try {
			botsApi.registerBot(new TelegramHandler(this, client));
		} catch (TelegramApiRequestException e1) {debug("Telegram failed to load");} 
        
        debug("Telegram loaded");
	}
	
	public void connectTelegramToController(TelegramHandler telegram) {
		this.telegramHandler = telegram;
	}*/
	
	public void pauseBot(int seconds) {
		debug("Pausing Bot...");
		debug("Pausing Bot...");
		debug("Pausing Bot...");
		
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
		
		this.graphicHandler.setInfo("Pause Stop: " + hour + ":" + minFinal);
		this.graphicHandler.setPause("Pause Stop: " + hour + ":" + minFinal);
		this.graphicHandler.togglePause();
		debug("Script Paused - Stop: " + hour + ":" + minFinal);
		
		sleep(sleep*1000);
		
		client.logIn();
		
		this.returnKeyboardAccess();
		this.returnMouseAccess();
		
		this.graphicHandler.togglePause();
		
		Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
	}
	
	public void pauseBot() {
		
		debug("Pausing Bot...");
		debug("Pausing Bot...");
		debug("Pausing Bot...");
		
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
		
		this.graphicHandler.setInfo("Pause Stop: " + hour + ":" + minFinal);
		this.graphicHandler.setPause("Pause Stop: " + hour + ":" + minFinal);
		this.graphicHandler.togglePause();
		debug("Script Paused - Stop: " + hour + ":" + minFinal);
		
		sleep(sleep*1000);
		
		client.logIn();
		
		this.returnKeyboardAccess();
		this.returnMouseAccess();
		
		this.graphicHandler.togglePause();
		
		Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
	}
	
	public void killBot() {
		debug("KILLING BOT");
		debug("KILLING BOT");
		debug("KILLING BOT");
		
		this.currentModule.killThread();
		this.antibanHandler.killHandler();
		this.gearHandler.killHandler();
		this.movementHandler.killHandler();
		this.killThread();
		client.stop();
	}
	
	public void killClient() {
		debug("KILLING CLIENT");
		debug("KILLING CLIENT");
		debug("KILLING CLIENT");
		
		this.currentModule.killThread();
		this.gearHandler.killHandler();
		this.antibanHandler.killHandler();
		this.movementHandler.killHandler();
		this.killThread();
		client.stop();
		System.exit(0);
	}
	
	//Avoid infinite loop if called from ClientThread().stop();
	public void killController() {
		this.antibanHandler.killHandler();
		this.currentModule.killThread();
		this.killThread();
	}
	
	public void hopWorlds() {
		
		debug("Hopping Worlds..");
		graphicHandler.setInfo("Hopping Worlds..");
		
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		while(this.requestKeyboardAccess()) {RandomProvider.sleep(10);}
		while(this.requestMouseAccess()) {RandomProvider.sleep(10);}
		
		client.hopWorlds();
		
		this.returnKeyboardAccess();
		this.returnMouseAccess();
		
		Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
	}
	
	public void nextModule() {
		
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);		
		
		if(this.currentModule != null) {
			this.currentModule.killThread();
		}
		
		this.modules.remove(currentModule);
		if(this.modules.size() <= 0) {
			debug("All Modules Completed");
			this.killBot();
		}
		else {
			this.currentModule = modules.get(0);
			debug("Loading new module: " + this.currentModule.getModuleName());
			if(!this.currentModule.setupModule()) {
				debug("Module Loading Failed");
				nextModule(); 
				return;
			}
			Thread t = new Thread(this.currentModule);
			t.start();
		}
		
		Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
	}
	
	//Save string in console for debuging
	public void debug(String info) {
		if(debug) {
			client.log(info);
		}
	}
	
	//Returns time in minutes
	public int timeLeftInScript() {
		return (int)(this.scriptTimer/60.0);
	}
	
	//Returns time in minutes
	public int timeLeftUntillPause() {
		return (int)(this.pauseTimer/60.0);
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
		return this.movementHandler;
	}
	
	public GearHandler getGearHandler() {
		return this.gearHandler;
	}
	
}
