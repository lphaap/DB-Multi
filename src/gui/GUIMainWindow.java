package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;

import antiban.RandomProvider;
import client.ClientThread;
import client.ThreadController;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import java.awt.List;
import java.awt.TextField;

public class GUIMainWindow extends JFrame {
	ThreadController controller;
	ClientThread client;
	
	private boolean pauseBot;
	
	JFrame thisFrame = this;

	private JPanel contentPane;
	
	private JComboBox comboBoxScriptEditor;
	
	private JSpinner pauseSpinnerMin;
	private JSpinner pauseSpinnerMax;
	private JSpinner scriptSpinnerMax;
	private JSpinner scriptSpinnerMin;
	
	private List listScript;
	
	private JFormattedTextField typeField;

	private JLabel	lblPausetimer;
	private JLabel lblScripttimer;
	private JLabel lblTimers;
	
	private JButton startBtn;
	private JButton pauseBtn;
	private JButton hopBtn;
	private JButton btnGearEditor;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIMainWindow frame = new GUIMainWindow(null,null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUIMainWindow(ThreadController controller, ClientThread client) {
		this.controller = controller;
		this.client = client;
		
		this.setTitle("DB-Multi");
		
		this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                int i=JOptionPane.showConfirmDialog(null, "Kill Bot?");
                if(i==0) {
                	new Thread(() -> {
                		controller.killBot();
                		thisFrame.dispose();
                	}).start();
                }
            }
        });
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 430, 705);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		comboBoxScriptEditor = new JComboBox();
		comboBoxScriptEditor.setBounds(10, 66, 161, 22);
		comboBoxScriptEditor.setModel(new DefaultComboBoxModel(new String[] 
									{"ManualCombatModule", "CombatModule", "MagicModule", "MiningModule", "SmithingModule",
									 "SmeltingModule", "JewelleryModule", "FishingModule", "CookingModule"}));
		contentPane.add(comboBoxScriptEditor);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addNewScript();
			}
		});
		btnAdd.setBounds(181, 66, 90, 22);
		contentPane.add(btnAdd);
		
		JLabel lblScriptManager = new JLabel("Script Manager");
		lblScriptManager.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblScriptManager.setBounds(10, 33, 144, 22);
		contentPane.add(lblScriptManager);
		
		
		/*this.pauseLabel = new JLabel("Script Manager");
		this.pauseLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		this.pauseLabel.setBounds(10, 33, 144, 22);
		contentPane.add(this.pauseLabel );
		
		this.scriptLabel = new JLabel("Script Manager");
		this.scriptLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		this.scriptLabel.setBounds(10, 33, 144, 22);
		contentPane.add(this.scriptLabel);*/
		
		btnGearEditor = new JButton("Gear Editor");
		btnGearEditor.addActionListener(e -> openGearEditor());
		btnGearEditor.setBounds(294, 66, 110, 22);
		contentPane.add(btnGearEditor);
		
		listScript = new List();
		listScript.setBounds(10, 108, 394, 449);
		listScript.setFont(new Font("Tahoma", Font.BOLD, 14));
		contentPane.add(listScript);
		
		this.pauseSpinnerMin = new JSpinner();
		pauseSpinnerMin.setBounds(10, 600, 71, 22);
		pauseSpinnerMin.setValue(90);
		pauseSpinnerMin.addChangeListener(e -> {
			handlePauseSpinners();
		});
		contentPane.add(pauseSpinnerMin);

		pauseSpinnerMax = new JSpinner();
		pauseSpinnerMax.setBounds(91, 600, 71, 22);
		pauseSpinnerMax.setValue(125);
		pauseSpinnerMax.addChangeListener(e -> {
				handlePauseSpinners();
		});
		contentPane.add(pauseSpinnerMax);
		
		scriptSpinnerMin = new JSpinner();
		scriptSpinnerMin.setBounds(10, 633, 71, 22);
		scriptSpinnerMin.setValue(180);
		scriptSpinnerMin.addChangeListener(e -> {
			handleScriptSpinners();
		});
		contentPane.add(scriptSpinnerMin);
		
		scriptSpinnerMax = new JSpinner();
		scriptSpinnerMax.setBounds(91, 633, 71, 22);
		scriptSpinnerMax.setValue(280);
		scriptSpinnerMax.addChangeListener(e -> {
			handleScriptSpinners();
		});
		contentPane.add(scriptSpinnerMax);
		
		lblPausetimer = new JLabel("Pause");
		lblPausetimer.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPausetimer.setBounds(172, 598, 144, 22);
		contentPane.add(lblPausetimer);
		
		lblScripttimer = new JLabel("Script");
		lblScripttimer.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblScripttimer.setBounds(172, 631, 144, 22);
		contentPane.add(lblScripttimer);
		
		lblTimers = new JLabel("Timers");
		lblTimers.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblTimers.setBounds(10, 573, 144, 22);
		contentPane.add(lblTimers);
		
		startBtn = new JButton("Start");
		startBtn.setFont(new Font("Tahoma", Font.BOLD, 12));
		startBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startBot();
			}
		});
		startBtn.setBounds(268, 614, 90, 22);
		contentPane.add(startBtn);
		
		pauseBtn = new JButton("Pause");
		pauseBtn.setFont(new Font("Tahoma", Font.BOLD, 12));
		pauseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(() -> {
					toggleBotPause();
				}).start();
			}
		});
		pauseBtn.setBounds(252, 590, 115, 22);
		pauseBtn.setVisible(false);
		contentPane.add(pauseBtn);
		
		hopBtn = new JButton("Hop Worlds");
		hopBtn.setFont(new Font("Tahoma", Font.BOLD, 12));
		hopBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(() -> {
					controller.getWorldHandler().hopWorlds();
				}).start();
			}
		});
		hopBtn.setBounds(252, 625, 115, 22);
		hopBtn.setVisible(false);
		contentPane.add(hopBtn);
		
		
		typeField = new JFormattedTextField();
		typeField.setBounds(10, 606, 219, 25);
		typeField.addKeyListener(new KeyAdapter() {public void keyReleased(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				String send = typeField.getText();
				new Thread(() -> {
					controller.getInGameMsgHandler().sendMsgInGame(send);
				}).start();
				typeField.setText("");
				
			}
		}});
		typeField.setVisible(false);
		contentPane.add(typeField);
	}
	
	
	public void handlePauseSpinners() {
		if(((int)this.pauseSpinnerMin.getValue()) >= ((int)this.pauseSpinnerMax.getValue())) {
			this.pauseSpinnerMax.setValue((int)this.pauseSpinnerMin.getValue()+1);
		}
	}
	
	public void handleScriptSpinners() {
		if(((int)this.scriptSpinnerMin.getValue()) >= ((int)this.scriptSpinnerMax.getValue())) {
			this.scriptSpinnerMax.setValue((int)this.scriptSpinnerMin.getValue()+1);
		}
	}
	
	public void startBot() {
		controller.getGearHandler().readGearLists();
		
		new Thread(() -> {
			controller.setPauseTimer(((int)pauseSpinnerMin.getValue()), ((int)(pauseSpinnerMax.getValue())));
			controller.setScriptTimer(((int)scriptSpinnerMin.getValue()), ((int)(scriptSpinnerMax.getValue())));
			new Thread(controller).start();
		}).start();;
		new Thread(() -> {
			startBtn.setVisible(false);
			this.lblPausetimer.setVisible(false);
			this.lblScripttimer.setVisible(false);
			this.pauseSpinnerMax.setVisible(false);
			this.pauseSpinnerMin.setVisible(false);
			this.scriptSpinnerMax.setVisible(false);
			this.scriptSpinnerMin.setVisible(false);
			this.btnGearEditor.setEnabled(false);
			
			this.lblTimers.setText("Type:");
			lblTimers.setBounds(10, 580, 144, 22);
			pauseBtn.setVisible(true);
			hopBtn.setVisible(true);
			typeField.setVisible(true);
		}).start();
		
	}
	
	public void addNewScript() {
		if(((String)this.comboBoxScriptEditor.getSelectedItem()).equals("CombatModule")) {
			GUICombatModuleWindow window = new GUICombatModuleWindow(controller, client, this);
			window.show();
		}
		else if(((String)this.comboBoxScriptEditor.getSelectedItem()).equals("MagicModule")) {
			GUIMagicModuleWindow window = new GUIMagicModuleWindow(controller, client, this);
			window.show();
		}
		else if(((String)this.comboBoxScriptEditor.getSelectedItem()).equals("MiningModule")) {
			GUIMinerModuleWindow window = new GUIMinerModuleWindow(controller, client, this);
			window.show();
		}
		else if(((String)this.comboBoxScriptEditor.getSelectedItem()).equals("SmithingModule")) {
			GUISmithingModuleWindow window = new GUISmithingModuleWindow(controller, client, this);
			window.show();
		}
		else if(((String)this.comboBoxScriptEditor.getSelectedItem()).equals("SmeltingModule")) {
			GUISmeltingModuleWindow window = new GUISmeltingModuleWindow(controller, client, this);
			window.show();
		}
		else if(((String)this.comboBoxScriptEditor.getSelectedItem()).equals("JewelleryModule")) {
			GUIJewelleryModuleWindow window = new GUIJewelleryModuleWindow(controller, client, this);
			window.show();
		}
		else if(((String)this.comboBoxScriptEditor.getSelectedItem()).equals("FishingModule")) {
			GUIFishingModuleWindow window = new GUIFishingModuleWindow(controller, client, this);
			window.show();
		}
		else if(((String)this.comboBoxScriptEditor.getSelectedItem()).equals("CookingModule")) {
			GUICookerModuleWindow window = new GUICookerModuleWindow(controller, client, this);
			window.show();
		}
		else if(((String)this.comboBoxScriptEditor.getSelectedItem()).equals("ManualCombatModule")) {
			GUIManualCModule window = new GUIManualCModule(controller, client, this);
			window.show();
		}
	}
	
	public void openGearEditor() {
		new GUIGearWindow(controller).show();
	}
	
	public void addToList(String item) {
		this.listScript.add("" + (this.listScript.getItemCount()+1) + ". " + item);
	}
	
	public void toggleBotPause() {
		if(!controller.isPaused()) {
			if(!this.pauseBot) {
				this.pauseBot = true;
				new Thread(() -> {controller.manualPause();}).start();
				this.pauseBtn.setText("Resume");
			}
			else {
				
				new Thread(() -> {controller.manualResume();}).start();
				this.pauseBot = false;
				this.pauseBtn.setText("Pause");
			}
		}
	}
	
	
	public void onClose() {
		
	}
	

}
