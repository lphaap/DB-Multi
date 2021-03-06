package client;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.EventQueue;
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

import antiban.RandomProvider;
import gui.GUIMainWindow;
import scripts.MinerModule;
import scripts.ScriptModule;
import scripts.SmelterModule;
import scripts.MinerModule.Ore;
import scripts.SmelterModule.Bars;
import utilities.InGameMsgHandler;


@ScriptManifest(author = "Molang", name = "Multi Bot", version = 1.6, description = "Multi Bot", category = Category.MISC)
public class ClientThread extends AbstractScript implements AdvancedMessageListener{
	
	private ThreadController controller;
	private ClientThread client = this;
	
	//--Called in the begining of the script--//
	@Override
	public void onStart() {
		log("Start");
		addDwarvenMine();
		addExperimentCave();
		addCanifisFix();
		
		this.controller = new ThreadController(this);
		//Thread thread = new Thread(controller);
		//hread.start();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIMainWindow gui = new GUIMainWindow(controller, client);
					gui.setVisible(true);
				} catch (Exception e) {
					log("FAILED TO INIT GUI");
					log(e.getMessage());
				}
			}
		});

	}
	//--Called in the begining of the script--//
	
	
	//--Called when exiting the script--//
	@Override
	public void onExit() {
		controller.killBot();
	}
	//--Called when exiting the script--//
	
	
	//--Updates this object with latest ingame info--//
	@Override
	public int onLoop() { 
		//log("Loop - Active");
		return(10); }
	//--Updates this object with latest ingame info--//
	

	//--Usseful methods that fit this class the most--//
	
	void logOut() {
		getRandomManager().disableSolver(RandomEvent.LOGIN);
		if(client.getPlayers().localPlayer().isInCombat()) {
			while(client.getPlayers().localPlayer().isInCombat()) {
				RandomProvider.sleep(500, 1000);
			}
			RandomProvider.sleep(12000, 14000);
		}
		getTabs().open(Tab.LOGOUT);
		if(this.getWorldHopper().isWorldHopperOpen()) {
			this.getWorldHopper().closeWorldHopper();
		}
		sleep(RandomProvider.randomInt(300,500));
		getWidgets().getWidget(182).getChild(12).interact();
		sleep(RandomProvider.randomInt(600,1000));
		getMouse().moveMouseOutsideScreen();
		controller.debug("Log Out Completed");
	}
	
	void logIn() {
		getRandomManager().enableSolver(RandomEvent.LOGIN);
		while(!this.getClient().isLoggedIn()) {RandomProvider.sleep(10);}
		controller.debug("Log In Completed");
	}
	
	public WidgetChild getWidget(int parent, int child) {
		return getWidgets().getWidget(parent).getChild(child);
	}
	
	public int getPlayerCount() {
		return getPlayers().all().size();
	}
	//--Usseful methods that fit this class the most--//
	
	//--Handles drawing graphics to client--//
	public void onPaint(Graphics g) {	
		if(controller.getGraphicHandler() != null) {
			controller.getGraphicHandler().handleGraphics(g);
		}
	}
	//--Handles drawing graphics to client--//
	
	//--Ingame Msg Listeners--//
	@Override
	public void onAutoMessage(Message m) {	
	}
	@Override
	public void onClanMessage(Message m) {
		if(controller.getInGameMsgHandler() != null) {
			controller.getInGameMsgHandler().processMessage(InGameMsgHandler.MsgOrigin.CLAN, m);
		}
	}
	@Override
	public void onGameMessage(Message m) {	
		if(controller.getInGameMsgHandler() != null) {
			controller.getInGameMsgHandler().processMessage(InGameMsgHandler.MsgOrigin.GAME, m);
		}
	}
	@Override
	public void onPlayerMessage(Message m) {
		if(controller.getInGameMsgHandler() != null) {
			controller.getInGameMsgHandler().processMessage(InGameMsgHandler.MsgOrigin.PLAYER, m);
		}
	}
	@Override
	public void onPrivateInMessage(Message m) {
		if(controller.getInGameMsgHandler() != null) {
			controller.getInGameMsgHandler().processMessage(InGameMsgHandler.MsgOrigin.FRIEND, m);
		}
	}
	@Override
	public void onPrivateInfoMessage(Message m) {
		
	}
	@Override
	public void onPrivateOutMessage(Message m) {
	}
	@Override
	public void onTradeMessage(Message m) {
	}
	//--Ingame Msg Listeners--//
	
	//--Adding manual location nodes to client--//
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
		//--Adding manual location nodes to client--//
		
	}
	
	

	
}
