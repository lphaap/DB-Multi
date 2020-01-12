package init;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.processing.Messager;
import javax.security.auth.login.LoginException;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.magic.Normal;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.walking.pathfinding.impl.web.WebFinder;
import org.dreambot.api.methods.walking.web.node.AbstractWebNode;
import org.dreambot.api.methods.walking.web.node.impl.BasicWebNode;
import org.dreambot.api.methods.world.World;
import org.dreambot.api.methods.world.Worlds;
import org.dreambot.api.randoms.RandomEvent;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.listener.AdvancedMessageListener;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.widgets.WidgetChild;
import org.dreambot.api.wrappers.widgets.message.Message;

import chat.Discord;
import movement.Locations;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import scripts.MinerModule;
import scripts.ScriptModule;
import scripts.SmelterModule;
import scripts.MinerModule.Ore;
import scripts.SmelterModule.Bars;

/**
 * TODO:
 * Experiment, Catherby fishing - Locations
 * Teleporter
 * P2P improvements Fishing, Smelting 
 * Thieving module
 * 
 * @author Soulless
 */



@ScriptManifest(author = "TheSoulles", name = "Multi Bot", version = 2.1, description = "Multi Bot", category = Category.MISC)
public class ClientThread extends AbstractScript implements AdvancedMessageListener{
	
	private int sleep;
	private ScriptModule currentAction;
	private ArrayList<ScriptModule> activeModules = new ArrayList<ScriptModule>();;
	private int mEnd;
	private int mStart;
	private int mPauseEnd;
	private int mPauseStart;
	private int mCurrent;
	private int mLast;
	private int randomCD;
	private int react;
	private ArrayList<String> msg1 = new ArrayList<String>();
	private ArrayList<String> msg2 = new ArrayList<String>();
	private Font fontInfo;
	private String timerText;
	private String pauseTimerText;
	private String pauseText;
	private String infoText;
	private boolean pause;
	private boolean fullLoopStop;
	private Discord messenger;
	private boolean autoReact;
	private boolean hopperTest;
	private boolean nextModule;
	private boolean scriptPause;
	
	
	
	
	public void onStart() {
		fullLoopStop = true;    
        
		
		msg1.add("Supp m8");
		msg1.add("Sup m8");
		msg1.add("Sup dude");
		msg1.add("Hello friend");
		msg1.add("Hi");
		msg1.add("Hello");
		msg1.add("Sup");
		msg1.add("Gday");
		msg1.add("Yello");
		msg2.add("I'll swap m8");
		msg2.add("Ill swap");
		msg2.add("Imma swap");
		msg2.add("I will swap");
		msg2.add("I'll hop");
		msg2.add("Ill Hop m8");
		msg2.add("Imma swap worlds");
		msg2.add("Imma hop worlds");
		msg2.add("Imma hop");
		msg2.add("Well ill hop");
		msg2.add("Well ill change");
		msg2.add("Changing m8");
		msg2.add("Hopping m8");
		msg2.add("Bruh.");
		msg2.add("m8 plox");
		msg2.add("Alright.. Ill hop");
		msg2.add("...");
		msg2.add("....");
		msg2.add("Wowee ill hop");
		
		sleep = 0;
		react = 0;
		
		ArrayList<String> meleeTraining = new ArrayList<String>();
		meleeTraining.add("Iron full helm");
		meleeTraining.add("Iron platebody");
		meleeTraining.add("Iron platelegs");
		meleeTraining.add("Iron kiteshield");
		meleeTraining.add("Amulet of glory");
		meleeTraining.add("Dragon sword");
		meleeTraining.add("Purple gloves");
		meleeTraining.add("Team-48 cape");
		meleeTraining.add("Leather boots");
		Collections.shuffle(meleeTraining);
		
		ArrayList<String> rangeTraining = new ArrayList<String>();
		rangeTraining.add("Leather cowl");
		rangeTraining.add("Blue d'hide chaps");
		rangeTraining.add("Blue d'hide vamb");
		rangeTraining.add("Leather body");
		rangeTraining.add("Amulet of glory");
		rangeTraining.add("Maple shortbow");
		rangeTraining.add("Iron arrow");
		rangeTraining.add("Team-48 cape");
		rangeTraining.add("Leather boots");
		Collections.shuffle(rangeTraining);
		
		ArrayList<String> skillTraining = new ArrayList<String>();
		skillTraining.add("Wizard hat");
		skillTraining.add("Amulet of glory");
		skillTraining.add("Granite longsword");
		skillTraining.add("Purple gloves");
		skillTraining.add("Team-48 cape");
		skillTraining.add("Leather boots");
		skillTraining.add("Brown apron");
		Collections.shuffle(skillTraining);
		
		ArrayList<String> mageTraining = new ArrayList<String>();
		mageTraining.add("Staff of fire");
		mageTraining.add("Blue d'hide vamb");
		mageTraining.add("Team-48 cape");
		mageTraining.add("Leather boots");
		mageTraining.add("Iron full helm");
		mageTraining.add("Iron platebody");
		mageTraining.add("Iron platelegs");
		mageTraining.add("Iron kiteshield");
		Collections.shuffle(mageTraining);
		
		//activeModules.add(new GearSwitchModule(this, rangeTraining));
		//activeModules.add(new GearSwitchModule(this, meleeTraining));
		//activeModules.add(new GearSwitchModule(this, skillTraining));
		//activeModules.add(new GearSwitchModule(this, mageTraining));
	
		//activeModules.add(new MinerModule(this, Locations.MINER_CRAFTING_GUILD_GOLD, MinerModule.Ore.GOLD_ORE,6));
		//activeModules.add(new SmithingModule(SmithingModule.SmithingType.PLATEBODY, SmithingModule.SmithingMaterial.STEEL, Locations.SMITHING_WEST_VARROCK, this));
		//activeModules.add(new JewelleryModule(this, Locations.SMELTER_AL_KHARID, 
		//	 JewelleryModule.JewelleryMaterial.RUBY, JewelleryModule.JewelleryType.NECKLACE));
		//activeModules.add(new SmelterModule(Locations.SMELTER_AL_KHARID, SmelterModule.Bars.STEEL, this));
		
		//activeModules.add(new FishingModule(this, FishingModule.Fish.TROUT_SALMON, 10, true));
		//activeModules.add(new CookerModule(this, Locations.COOKING_AL_KHARID, CookerModule.Cook.TROUT));
		//activeModules.add(new CookerModule(this, Locations.COOKING_AL_KHARID, CookerModule.Cook.SALMON));
		
		//activeModules.add(new CombatModule(this, CombatModule.Monster.GIANT_FROG, CombatModule.Food.TROUT, 2, true));
		
		activeModules.add(new MinerModule(this, Locations.MINER_WEST_VARROCK, MinerModule.Ore.TIN_ORE,7));
		activeModules.add(new MinerModule(this, Locations.MINER_EAST_VARROCK, MinerModule.Ore.COPPER_ORE,7));
		activeModules.add(new SmelterModule(Locations.SMELTER_AL_KHARID, SmelterModule.Bars.BRONZE, this));
		
		
		currentAction = activeModules.get(0);
		
		//currentAction = new LocationPrinter(this);
		
		Calendar today = new GregorianCalendar();
		mCurrent = today.get(Calendar.MINUTE);
		mPauseStart = 0;
		mPauseEnd =  Calculations.random(90, 125);
		mStart = 0;
		mEnd = Calculations.random(180, 280);
		randomCD = 0;
		
		this.fontInfo = new Font("Arial", Font.BOLD, 15);
		this.timerText = "";
		this.infoText = "";
		this.pauseText = "";
		
		
		
		JDABuilder builder = new JDABuilder(AccountType.BOT);
        String token = "NjY0NTQ1NTcwMTUyNTEzNTQ3.XhZZfA.t42nR6nQJex7A9VsK9aBCT4zNZ4";
        Discord discord = new Discord();
        discord.setScript(this);
        this.messenger = discord;
        
        builder.setToken(token);
        builder.addEventListeners(new Discord());
        try {
			builder.build();
		} catch (LoginException e) {e.printStackTrace();}
        
        this.nextModule = true;
        this.scriptPause = false;
		

		addDwarvenMine();
		addExperimentCave();
		addCanifisFix();
		

	}
	
	
	public int onLoop(){
		Calendar today = new GregorianCalendar();
		mCurrent = today.get(Calendar.MINUTE);
		if(mCurrent != mLast) {
			mLast = today.get(Calendar.MINUTE);
			mStart++;
			mPauseStart++;
			this.pauseTimerText = "Time Until Pause: " + (mPauseEnd - mPauseStart) + " Minutes";
			this.timerText = "Time Left In Script: " + (mEnd - mStart) + " Minutes";
			
		}
				
		if(mStart >= mEnd) {
			logOut();
			messenger.sendMessage("Script Ended Normally.");
			System.exit(0);
			stop();
			
		}
		
		if(randomCD <= 0) {
			randoms();
		}
		
		if(this.nextModule) {
			if(!this.currentAction.setupModule()) {
				nextModule();
				this.sleep = Calculations.random(2000,4000);
			}
			else {
				this.nextModule = false;
			}
		}
		
		if(mPauseStart >= mPauseEnd) {
			this.pause = true;
			getRandomManager().disableSolver(RandomEvent.LOGIN);
			int pauseMins = Calculations.random(12, 26);
			int pauseMilSecs = Calculations.random(50000, 72000);
			int pauseFinal = (pauseMins * pauseMilSecs) / 60000;
			int hour = today.get(Calendar.HOUR);
			int mins = today.get(Calendar.MINUTE);
			int minsFinal = mins + pauseFinal;
			if(minsFinal > 60) {
				minsFinal = minsFinal - 60; 
				hour++;
			}
			
			setPauseText("Pause Stop: " + hour + ":" + minsFinal);  
			this.infoText = "Pause Stop: " + hour + ":" + minsFinal;
			
			
			logOut();
			
			messenger.sendMessage("Script Paused - Stop: " + hour + ":" + minsFinal);
			
			
			sleep(pauseMilSecs * pauseMins);

			logIn();
			
			this.mPauseStart = 0;
			mPauseEnd =  Calculations.random(90, 125);
			this.pause = false;
			sleep(Calculations.random(10000,15000));
		} 
			
		if(!hopperTest && !this.nextModule && !scriptPause) {
			this.sleep = currentAction.onLoop();
		}
		
		randomCD = randomCD - sleep;
		
		return sleep;
	}
	
	public void randoms() {
		Calculations.setRandomSeed(Calculations.random(10000));
		int test = Calculations.random(0, 100);
		int random = -1;
		
		if(test > 96) {
			this.infoText = "Random: Hovering XP";
			getTabs().open(Tab.STATS);
			getSkills().hoverSkill(currentAction.getSkillToHover());
			sleep(Calculations.random(2000, 3000));
			getTabs().open(Tab.INVENTORY);
			getMouse().move();
			randomCD = Calculations.random(30000,60000);	
		}
		if(test < 4) {
			getMouse().moveMouseOutsideScreen();
			int delay = Calculations.random(10000, 14000);
			this.infoText = "Random: Delay " + delay+" ms";
			sleep(delay);
			randomCD = Calculations.random(80000,110000);
			
		}
		
		if(test > 50 && test <= 65) {
			random = Calculations.random(0,2);
			if(random == 0) {
				this.infoText = "Random: Keyboard Rotate Camera";
				getCamera().keyboardRotateToTile(getLocalPlayer().getTile().getArea(6).getRandomTile());
			}
			else{
				this.infoText = "Random: Mouse Rotate Camera";
				getCamera().mouseRotateToTile(getLocalPlayer().getTile().getArea(6).getRandomTile());
			}
			randomCD = Calculations.random(20000,40000);
			
		}
		
		if(test <= 90 && test >= 85) {
			
			this.infoText = "Random: Mouse outside of the screen";
			getMouse().moveMouseOutsideScreen();
			sleep(Calculations.random(4000, 5500));
			randomCD = Calculations.random(35000,80000);
		}
	}
	
	
	public void onAutoMessage(Message arg0) {	
	}
	
	public void onClanMessage(Message arg0) {	
	}

	public void onGameMessage(Message m) {	
	}

	public void onPlayerMessage(Message m) {
		if(this.react == 1 && !m.getUsername().equals(getLocalPlayer().getName())) {
			String msg = m.getUsername() + ": "+ m.getMessage();
			messenger.sendMessage(msg);
		}
		
		
		if(this.react == 1 && !m.getUsername().equals(getLocalPlayer().getName()) && this.autoReact) {
			this.infoText = "AutoReact: Msg found, hopping";
			messenger.sendMessage("AutoReact: Msg found, hopping");
			this.react = 0;
			String text1  = msg1.get(Calculations.random(0,msg1.size()));
			messenger.sendMessage("AutoReact: Msg sent - " + text1);
			String text2  = msg2.get(Calculations.random(0,msg2.size()));
			messenger.sendMessage("AutoReact: Msg sent - " + text2);
			getKeyboard().type(text1,true,true);
			sleep(Calculations.random(2000, 3000));
			getKeyboard().type(text2,true,true);
			hop();
		}
	}
	
	public void onPrivateInMessage(Message arg0) {
	}

	public void onPrivateInfoMessage(Message arg0) {
	}
	
	public void onPrivateOutMessage(Message arg0) {
	}
	
	public void onTradeMessage(Message arg0) {
	}
	
	public void setReact(int mode) {
		this.react = mode;
	}
	
	public void hop() {
		this.hopperTest = true;
		World w;
		if(this.getClient().isMembers()) {
			w = getWorlds().getRandomWorld(f -> f != null && f.isMembers() && !f.isDeadmanMode() && !f.isPVP() && f.getMinimumLevel() == 0);
		}
		else {
			w = getWorlds().getRandomWorld(f -> f != null && !f.isMembers() && !f.isDeadmanMode() && !f.isPVP() && f.getMinimumLevel() == 0);
		}
		while(getPlayers().localPlayer().isInCombat()) {
			sleep(Calculations.random(500, 1000));
		}
		sleep(Calculations.random(12000, 14000));
		getWorldHopper().openWorldHopper();
		getWorldHopper().hopWorld(w);
		sleep(Calculations.random(3000, 4000));
		getWorldHopper().closeWorldHopper();
		sleep(1000);
		getTabs().open(Tab.INVENTORY);
		getMouse().move();
		this.hopperTest = false;
	}
	
	public void nextModule() {
		int current = activeModules.indexOf(this.currentAction);
		if(current + 1 >= activeModules.size()) {
			if(this.messenger != null) {
				messenger.sendMessage("All Modules Completed.");
				messenger.sendMessage("Script Ended.");
			}
			logOut();
			System.exit(0);
			stop();
		}
		else {
			this.currentAction = activeModules.get(current + 1);
			if(this.messenger != null) {
				messenger.sendMessage("Module Swap - " + this.currentAction.getModuleName());
				messenger.sendMessage("Time Left In Script: " + (mEnd - mStart) + " Minutes");
			}
			this.nextModule = true;
		}
		
	}
	
	public void onPaint(Graphics g) {
		if(!pause) {
			g.setColor(Color.YELLOW);
			g.setFont(fontInfo);
			g.drawString(this.infoText,5,334);
			g.setColor(Color.RED);
			g.drawString(this.timerText, 5, 314);
			g.setColor(Color.MAGENTA);
			g.drawString(this.pauseTimerText, 5, 294);
		}
		else {
			g.setColor(Color.MAGENTA);
			g.setFont(fontInfo);
			g.drawString(this.pauseText, 5, 20);
		}
	}
	
	public void setInfoText(String text) {
		this.infoText = text;
	}
	public void setPauseText(String text) {
		this.pauseText = text;
	}

	
	public void addDwarvenMine() {
		AbstractWebNode webNode0 = new BasicWebNode(3020, 9852);
		AbstractWebNode webNode1 = new BasicWebNode(3020, 9846);
		AbstractWebNode webNode10 = new BasicWebNode(3017, 9819);
		AbstractWebNode webNode11 = new BasicWebNode(3018, 9813);
		AbstractWebNode webNode12 = new BasicWebNode(3008, 9813);
		AbstractWebNode webNode13 = new BasicWebNode(3002, 9813);
		AbstractWebNode webNode14 = new BasicWebNode(2999, 9819);
		AbstractWebNode webNode15 = new BasicWebNode(2998, 9827);
		AbstractWebNode webNode16 = new BasicWebNode(2991, 9827);
		AbstractWebNode webNode17 = new BasicWebNode(2998, 9833);
		AbstractWebNode webNode18 = new BasicWebNode(2999, 9837);
		AbstractWebNode webNode19 = new BasicWebNode(2998, 9843);
		AbstractWebNode webNode2 = new BasicWebNode(3029, 9847);
		AbstractWebNode webNode20 = new BasicWebNode(2995, 9849);
		AbstractWebNode webNode21 = new BasicWebNode(2999, 9808);
		AbstractWebNode webNode22 = new BasicWebNode(3001, 9802);
		AbstractWebNode webNode23 = new BasicWebNode(3004, 9797);
		AbstractWebNode webNode24 = new BasicWebNode(2994, 9809);
		AbstractWebNode webNode25 = new BasicWebNode(2988, 9809);
		AbstractWebNode webNode26 = new BasicWebNode(2984, 9805);
		AbstractWebNode webNode27 = new BasicWebNode(2984, 9810);
		AbstractWebNode webNode28 = new BasicWebNode(2984, 9817);
		AbstractWebNode webNode29 = new BasicWebNode(2979, 9811);
		AbstractWebNode webNode3 = new BasicWebNode(3035, 9847);
		AbstractWebNode webNode30 = new BasicWebNode(2970, 9810);
		AbstractWebNode webNode31 = new BasicWebNode(2965, 9809);
		AbstractWebNode webNode32 = new BasicWebNode(3027, 9835);
		AbstractWebNode webNode33 = new BasicWebNode(3033, 9833);
		AbstractWebNode webNode34 = new BasicWebNode(3032, 9828);
		AbstractWebNode webNode35 = new BasicWebNode(3031, 9820);
		AbstractWebNode webNode36 = new BasicWebNode(3040, 9833);
		AbstractWebNode webNode37 = new BasicWebNode(3049, 9840);
		AbstractWebNode webNode38 = new BasicWebNode(3045, 9828);
		AbstractWebNode webNode39 = new BasicWebNode(3051, 9826);
		AbstractWebNode webNode4 = new BasicWebNode(3020, 9839);
		AbstractWebNode webNode40 = new BasicWebNode(3053, 9820);
		AbstractWebNode webNode41 = new BasicWebNode(3052, 9815);
		AbstractWebNode webNode42 = new BasicWebNode(3051, 9812);
		AbstractWebNode webNode43 = new BasicWebNode(3040, 9824);
		AbstractWebNode webNode44 = new BasicWebNode(3037, 9818);
		AbstractWebNode webNode45 = new BasicWebNode(3037, 9811);
		AbstractWebNode webNode46 = new BasicWebNode(3039, 9804);
		AbstractWebNode webNode47 = new BasicWebNode(3042, 9799);
		AbstractWebNode webNode48 = new BasicWebNode(3043, 9792);
		AbstractWebNode webNode49 = new BasicWebNode(3044, 9788);
		AbstractWebNode webNode5 = new BasicWebNode(3020, 9830);
		AbstractWebNode webNode50 = new BasicWebNode(3047, 9788);
		AbstractWebNode webNode51 = new BasicWebNode(3039, 9787);
		AbstractWebNode webNode52 = new BasicWebNode(3038, 9801);
		AbstractWebNode webNode53 = new BasicWebNode(3038, 9798);
		AbstractWebNode webNode6 = new BasicWebNode(3023, 9820);
		AbstractWebNode webNode7 = new BasicWebNode(3027, 9812);
		AbstractWebNode webNode8 = new BasicWebNode(3025, 9805);
		AbstractWebNode webNode9 = new BasicWebNode(3023, 9800);
		webNode0.addConnections(webNode1);
		webNode1.addConnections(webNode0);
		webNode1.addConnections(webNode2);
		webNode1.addConnections(webNode4);
		webNode10.addConnections(webNode11);
		webNode10.addConnections(webNode6);
		webNode11.addConnections(webNode10);
		webNode11.addConnections(webNode12);
		webNode12.addConnections(webNode11);
		webNode12.addConnections(webNode13);
		webNode13.addConnections(webNode12);
		webNode13.addConnections(webNode14);
		webNode13.addConnections(webNode21);
		webNode14.addConnections(webNode13);
		webNode14.addConnections(webNode15);
		webNode15.addConnections(webNode14);
		webNode15.addConnections(webNode16);
		webNode15.addConnections(webNode17);
		webNode16.addConnections(webNode15);
		webNode17.addConnections(webNode15);
		webNode17.addConnections(webNode18);
		webNode18.addConnections(webNode17);
		webNode18.addConnections(webNode19);
		webNode19.addConnections(webNode18);
		webNode19.addConnections(webNode20);
		webNode2.addConnections(webNode1);
		webNode2.addConnections(webNode3);
		webNode20.addConnections(webNode19);
		webNode21.addConnections(webNode13);
		webNode21.addConnections(webNode22);
		webNode21.addConnections(webNode24);
		webNode22.addConnections(webNode21);
		webNode22.addConnections(webNode23);
		webNode23.addConnections(webNode22);
		webNode24.addConnections(webNode21);
		webNode24.addConnections(webNode25);
		webNode25.addConnections(webNode24);
		webNode25.addConnections(webNode26);
		webNode25.addConnections(webNode27);
		webNode26.addConnections(webNode25);
		webNode27.addConnections(webNode25);
		webNode27.addConnections(webNode28);
		webNode27.addConnections(webNode29);
		webNode28.addConnections(webNode27);
		webNode29.addConnections(webNode27);
		webNode29.addConnections(webNode30);
		webNode3.addConnections(webNode2);
		webNode30.addConnections(webNode29);
		webNode30.addConnections(webNode31);
		webNode31.addConnections(webNode30);
		webNode32.addConnections(webNode33);
		webNode32.addConnections(webNode4);
		webNode33.addConnections(webNode32);
		webNode33.addConnections(webNode34);
		webNode33.addConnections(webNode36);
		webNode34.addConnections(webNode33);
		webNode34.addConnections(webNode35);
		webNode35.addConnections(webNode34);
		webNode36.addConnections(webNode33);
		webNode36.addConnections(webNode37);
		webNode36.addConnections(webNode38);
		webNode37.addConnections(webNode36);
		webNode38.addConnections(webNode36);
		webNode38.addConnections(webNode39);
		webNode38.addConnections(webNode43);
		webNode39.addConnections(webNode38);
		webNode39.addConnections(webNode40);
		webNode4.addConnections(webNode1);
		webNode4.addConnections(webNode32);
		webNode4.addConnections(webNode5);
		webNode40.addConnections(webNode39);
		webNode40.addConnections(webNode41);
		webNode41.addConnections(webNode40);
		webNode41.addConnections(webNode42);
		webNode42.addConnections(webNode41);
		webNode43.addConnections(webNode38);
		webNode43.addConnections(webNode44);
		webNode44.addConnections(webNode43);
		webNode44.addConnections(webNode45);
		webNode45.addConnections(webNode44);
		webNode45.addConnections(webNode46);
		webNode46.addConnections(webNode45);
		webNode46.addConnections(webNode47);
		webNode46.addConnections(webNode52);
		webNode47.addConnections(webNode46);
		webNode47.addConnections(webNode48);
		webNode48.addConnections(webNode47);
		webNode48.addConnections(webNode49);
		webNode49.addConnections(webNode48);
		webNode49.addConnections(webNode50);
		webNode49.addConnections(webNode51);
		webNode5.addConnections(webNode4);
		webNode5.addConnections(webNode6);
		webNode50.addConnections(webNode49);
		webNode51.addConnections(webNode49);
		webNode52.addConnections(webNode46);
		webNode52.addConnections(webNode53);
		webNode53.addConnections(webNode52);
		webNode6.addConnections(webNode10);
		webNode6.addConnections(webNode5);
		webNode6.addConnections(webNode7);
		webNode7.addConnections(webNode6);
		webNode7.addConnections(webNode8);
		webNode8.addConnections(webNode7);
		webNode8.addConnections(webNode9);
		webNode9.addConnections(webNode8);
		WebFinder webFinder = getWalking().getWebPathFinder();
		AbstractWebNode[] webNodes = {webNode43, webNode44, webNode38, webNode35, webNode34, webNode36, webNode33, webNode45, webNode39, webNode40, webNode41, webNode7, webNode6, webNode42, webNode32, webNode46, webNode37, webNode5, webNode52, webNode8, webNode10, webNode11, webNode47, webNode4, webNode3, webNode53, webNode2, webNode9, webNode1, webNode48, webNode12, webNode0, webNode49, webNode50, webNode51, webNode13, webNode14, webNode15, webNode21, webNode17, webNode18, webNode23, webNode22, webNode19, webNode24, webNode16, webNode20, webNode25, webNode28, webNode27, webNode26, webNode29, webNode30, webNode31};
		for (AbstractWebNode webNode : webNodes) {
			webFinder.addWebNode(webNode);	
		}
	}
	
	public void addCanifisFix() {
		
		AbstractWebNode webNode0 = new BasicWebNode(3501, 3497);
		AbstractWebNode webNode1 = new BasicWebNode(3502, 3499);
		AbstractWebNode webNode2 = new BasicWebNode(3506, 3501);
		AbstractWebNode webNode3 = new BasicWebNode(3509, 3505);
		AbstractWebNode webNode4 = new BasicWebNode(3510, 3510);
		AbstractWebNode webNode5 = new BasicWebNode(3513, 3513);
		AbstractWebNode webNode6 = new BasicWebNode(3517, 3518);
		webNode0.addConnections(webNode1);
		webNode1.addConnections(webNode0);
		webNode1.addConnections(webNode2);
		webNode2.addConnections(webNode1);
		webNode2.addConnections(webNode3);
		webNode3.addConnections(webNode2);
		webNode3.addConnections(webNode4);
		webNode4.addConnections(webNode3);
		webNode4.addConnections(webNode5);
		webNode5.addConnections(webNode4);
		webNode5.addConnections(webNode6);
		webNode6.addConnections(webNode5);
		WebFinder webFinder = getWalking().getWebPathFinder();
		AbstractWebNode[] webNodes = {webNode6, webNode5, webNode4, webNode3, webNode2, webNode1, webNode0};
		for (AbstractWebNode webNode : webNodes) {
		webFinder.addWebNode(webNode);
		}


		


	}
	
	public void addExperimentCave() {
		AbstractWebNode webNode0 = new BasicWebNode(3574, 9928);
		AbstractWebNode webNode1 = new BasicWebNode(3570, 9933);
		AbstractWebNode webNode10 = new BasicWebNode(3537, 9928);
		AbstractWebNode webNode11 = new BasicWebNode(3532, 9926);
		AbstractWebNode webNode12 = new BasicWebNode(3529, 9933);
		AbstractWebNode webNode13 = new BasicWebNode(3521, 9931);
		AbstractWebNode webNode14 = new BasicWebNode(3513, 9934);
		AbstractWebNode webNode15 = new BasicWebNode(3507, 9932);
		AbstractWebNode webNode16 = new BasicWebNode(3502, 9934);
		AbstractWebNode webNode17 = new BasicWebNode(3497, 9938);
		AbstractWebNode webNode18 = new BasicWebNode(3490, 9937);
		AbstractWebNode webNode19 = new BasicWebNode(3485, 9940);
		AbstractWebNode webNode2 = new BasicWebNode(3565, 9937);
		AbstractWebNode webNode20 = new BasicWebNode(3480, 9943);
		AbstractWebNode webNode3 = new BasicWebNode(3565, 9941);
		AbstractWebNode webNode4 = new BasicWebNode(3561, 9944);
		AbstractWebNode webNode5 = new BasicWebNode(3556, 9946);
		AbstractWebNode webNode6 = new BasicWebNode(3552, 9941);
		AbstractWebNode webNode7 = new BasicWebNode(3551, 9936);
		AbstractWebNode webNode8 = new BasicWebNode(3549, 9932);
		AbstractWebNode webNode9 = new BasicWebNode(3543, 9931);
		webNode0.addConnections(webNode1);
		webNode1.addConnections(webNode0);
		webNode1.addConnections(webNode2);
		webNode10.addConnections(webNode11);
		webNode10.addConnections(webNode9);
		webNode11.addConnections(webNode10);
		webNode11.addConnections(webNode12);
		webNode12.addConnections(webNode11);
		webNode12.addConnections(webNode13);
		webNode13.addConnections(webNode12);
		webNode13.addConnections(webNode14);
		webNode14.addConnections(webNode13);
		webNode14.addConnections(webNode15);
		webNode15.addConnections(webNode14);
		webNode15.addConnections(webNode16);
		webNode16.addConnections(webNode15);
		webNode16.addConnections(webNode17);
		webNode17.addConnections(webNode16);
		webNode17.addConnections(webNode18);
		webNode18.addConnections(webNode17);
		webNode18.addConnections(webNode19);
		webNode19.addConnections(webNode18);
		webNode19.addConnections(webNode20);
		webNode2.addConnections(webNode1);
		webNode2.addConnections(webNode3);
		webNode20.addConnections(webNode19);
		webNode3.addConnections(webNode2);
		webNode3.addConnections(webNode4);
		webNode4.addConnections(webNode3);
		webNode4.addConnections(webNode5);
		webNode5.addConnections(webNode4);
		webNode5.addConnections(webNode6);
		webNode6.addConnections(webNode5);
		webNode6.addConnections(webNode7);
		webNode7.addConnections(webNode6);
		webNode7.addConnections(webNode8);
		webNode8.addConnections(webNode7);
		webNode8.addConnections(webNode9);
		webNode9.addConnections(webNode10);
		webNode9.addConnections(webNode8);
		WebFinder webFinder = getWalking().getWebPathFinder();
		AbstractWebNode[] webNodes = {webNode19, webNode20, webNode18, webNode17, webNode16, webNode15, webNode14, webNode13, webNode12, webNode11, webNode10, webNode9, webNode8, webNode7, webNode6, webNode5, webNode4, webNode2, webNode3, webNode1, webNode0};
		for (AbstractWebNode webNode : webNodes) {
			webFinder.addWebNode(webNode);
		}
	}
	
	public boolean swapAutoReact() {
		this.autoReact = !autoReact;
		return autoReact;
	}
	
	public void setMessenger(Discord chat) {
		this.messenger = chat;
	}
	
	public Discord getMessenger() {
		return this.messenger;
	}
	
	public String getCurrentAction() {
		return this.infoText;
	}
	
	public String getTimer() {
		return this.timerText;
	}
	
	public String getPauseTimer() {
		return this.pauseTimerText;
	}
	
	public int getPlayerCount() {
		return getPlayers().all().size();
	}
	
	public void logOut() {
		this.hopperTest = true;
		getRandomManager().disableSolver(RandomEvent.LOGIN);
		while(getPlayers().localPlayer().isInCombat()) {
			sleep(Calculations.random(500, 1000));
		}
		sleep(Calculations.random(12000, 14000));
		getTabs().open(Tab.LOGOUT);
		if(this.getWorldHopper().isWorldHopperOpen()) {
			this.getWorldHopper().closeWorldHopper();
		}
		sleep(Calculations.random(300,500));
		getWidgets().getWidget(182).getChild(12).interact();
		sleep(Calculations.random(300,500));
		messenger.sendMessage("Log Out Completed");
		
	}
	
	public void logIn() {
		this.hopperTest = false;
		getRandomManager().enableSolver(RandomEvent.LOGIN);
		messenger.sendMessage("Log In Completed");
	}
	
	public boolean togglePause() {
		this.scriptPause = !this.scriptPause;
		return this.scriptPause;
	}
	
	public ScriptModule getCurrent() {
		return this.currentAction;
	}
	
	public WidgetChild getWidget(int parent, int child) {
		return getWidgets().getWidget(parent).getChild(child);
	}
	
	

	
}
