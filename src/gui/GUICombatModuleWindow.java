package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.border.EmptyBorder;

import org.dreambot.api.methods.skills.Skill;

import client.ClientThread;
import client.ThreadController;
import scripts.CombatModule;
import scripts.CombatModule.Training;

public class GUICombatModuleWindow extends JFrame {
	private ThreadController controller;
	private GUIMainWindow mainWindow;
	private ClientThread client;
	
	private JPanel contentPane;

	private JSpinner limit;
	private JSpinner timeLimit;
	private JSpinner foodLimit;
	private JSpinner potionLimit;
	
	private JComboBox comboBoxMonster;
	private JComboBox comboBoxFood;
	private JComboBox comboBoxTrain;
	private JComboBox comboBoxPotion;
	
	private JCheckBox pickupCheck;
	private JCheckBox prayerCheck;
	
	private ArrayList<Integer> prayers = new ArrayList<Integer>();
	
	private int maxFood =28;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUICombatModuleWindow frame = new GUICombatModuleWindow(null,null,null);
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
	public GUICombatModuleWindow(ThreadController controller, ClientThread client, GUIMainWindow mainWindow) {
		this.controller = controller;
		this.client = client;
		this.mainWindow = mainWindow;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 350, 422);
		contentPane = new JPanel();
		contentPane.setLayout(null);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblWindowTittle = new JLabel("CombatModule Setup");
		lblWindowTittle.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblWindowTittle.setBounds(92, 11, 168, 22);
		contentPane.add(lblWindowTittle);
		
		JLabel lblLimit = new JLabel("Action limit:");
		lblLimit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblLimit.setBounds(48, 59, 83, 22);
		contentPane.add(lblLimit);
		
		limit = new JSpinner();
		limit.setBounds(48, 82, 71, 22);
		limit.addChangeListener(e -> {
			checkLimit(limit);
		});
		limit.setValue(1);
		contentPane.add(limit);
		
		foodLimit = new JSpinner();
		foodLimit.setBounds(130, 172, 31, 22);
		foodLimit.addChangeListener(e -> {
			checkFoodLimit();
		});
		contentPane.add(foodLimit);
		
		JLabel lblTimeLimit = new JLabel("Time limit:");
		lblTimeLimit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTimeLimit.setBounds(205, 59, 99, 22);
		contentPane.add(lblTimeLimit);
		
		timeLimit = new JSpinner();
		timeLimit.setBounds(205, 82, 71, 22);
		timeLimit.addChangeListener(e -> {
			checkTimerLimit(timeLimit);
		});
		contentPane.add(timeLimit);
		
		
		JLabel lblMonster = new JLabel("Monster:");
		lblMonster.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblMonster.setBounds(205, 148, 83, 22);
		contentPane.add(lblMonster);
		
		comboBoxMonster = new JComboBox();
		comboBoxMonster.setBounds(180, 172, 120, 22);
		comboBoxMonster.setModel(new DefaultComboBoxModel(CombatModule.Monster.values()));
		contentPane.add(comboBoxMonster);
		
		
		JLabel lblFood = new JLabel("Food:");
		lblFood.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblFood.setBounds(58, 148, 50, 22);
		contentPane.add(lblFood);
		
		comboBoxFood = new JComboBox();
		comboBoxFood.setBounds(10, 172, 120, 22);
		comboBoxFood.setModel(new DefaultComboBoxModel(CombatModule.Food.values()));
		contentPane.add(comboBoxFood);
		
		JLabel lblPotion = new JLabel("Potions:");
		lblPotion.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPotion.setBounds(48, 237, 168, 22);
		contentPane.add(lblPotion);
		
		comboBoxPotion = new JComboBox();
		comboBoxPotion.setBounds(10, 259, 120, 22);
		comboBoxPotion.setModel(new DefaultComboBoxModel(CombatModule.Potion.values()));
		comboBoxPotion.addItemListener(l -> this.checkInventoryLimit());
		contentPane.add(comboBoxPotion);
		
		potionLimit = new JSpinner();
		potionLimit.setBounds(130, 259, 31, 22);
		potionLimit.addChangeListener(e -> {
			checkInventoryLimit();
		});
		contentPane.add(potionLimit);
		
		JLabel lblTraining = new JLabel("Training:");
		lblTraining.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTraining.setBounds(205, 237, 168, 22);
		contentPane.add(lblTraining);
		
		comboBoxTrain = new JComboBox();
		comboBoxTrain.setBounds(180, 259, 120, 22);
		comboBoxTrain.setModel(new DefaultComboBoxModel(CombatModule.Training.values()));
		contentPane.add(comboBoxTrain);
		
		JLabel lblPickup = new JLabel("Collect Items:");
		lblPickup.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPickup.setBounds(106, 321, 94, 22);
		contentPane.add(lblPickup);
		
		pickupCheck = new JCheckBox();
		pickupCheck.setBounds(193, 321, 35, 22);
		pickupCheck.setSelected(true);
		contentPane.add(pickupCheck);
		
		JLabel lblPrayer = new JLabel("Use Prayer:");
		lblPrayer.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPrayer.setBounds(106, 296, 94, 22);
		contentPane.add(lblPrayer);
		
		prayerCheck = new JCheckBox();
		prayerCheck.setBounds(193, 296, 35, 22);
		prayerCheck.addActionListener(l -> {
			System.out.println("T");
			if(prayerCheck.isSelected()) {
				new GUIPrayerWindow(this, null).show();
			}
		});
		contentPane.add(prayerCheck);
		
		JButton btnStart = new JButton("Ready");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread t = new Thread(() -> {
					close();
				});
				t.start();
	
			}
		});
		btnStart.setBounds(118, 350, 90, 22);
		contentPane.add(btnStart);
	}
	
	public void parseModule() {
		CombatModule cm = new CombatModule(controller, client, ((CombatModule.Monster)(this.comboBoxMonster.getSelectedItem())), 
				((CombatModule.Food)(this.comboBoxFood.getSelectedItem())), ((CombatModule.Potion)(this.comboBoxPotion.getSelectedItem())),
				((int)this.limit.getValue()), ((int)this.timeLimit.getValue()), ((int)this.foodLimit.getValue()), ((int)this.potionLimit.getValue()),
				this.pickupCheck.isSelected(), this.prayerCheck.isSelected(), ((CombatModule.Training)this.comboBoxTrain.getSelectedItem()));
		cm.setPrayerValues(this.prayers);
		controller.addModule(cm);
	}
	
	public void parseInfo() {
		mainWindow.addToList("CombatModule: " + (this.comboBoxMonster.getSelectedItem().toString()));
	}
	
	public void close() {
		parseModule();
		parseInfo();
		this.dispose();
	}
	public void checkFoodLimit() {
		int foodcount =(int)this.foodLimit.getValue();
		if(foodcount < 0) {
			this.foodLimit.setValue(0);
		}
		else if(foodcount > this.maxFood) {
			this.foodLimit.setValue(maxFood);
		}
	}
	
	public void checkInventoryLimit() {
		
		new Thread(() -> {
			int foodcount =(int)this.foodLimit.getValue();
			int potioncount =(int)this.potionLimit.getValue();
			int times = 1;
			
			if(potioncount < 0) {
				this.potionLimit.setValue(0);
			}
			switch((CombatModule.Potion)this.comboBoxPotion.getSelectedItem()) {
				case NONE:
					this.potionLimit.setValue(0);
					break;
				case STR:
					times =1;
					break;
				case STR_ATT:
					times =2;
					break;
				case STR_ATT_DEF:
					times =3;
					break;
				case S_STR:
					times =1;
					break;
				case S_STR_ATT:
					times =2;
					break;
				case S_STR_ATT_DEF:
					times =3;
					break;
				case S_COMBAT:
					times =1;
					break;
				case RANGE:
					times =1;
					break;
			}
			
			switch(times) {
				case 1:
					if(potioncount > 28) {
						this.potionLimit.setValue(28);
					}
					this.maxFood = (28-(potioncount));
					break;
				case 2:
					if(potioncount > 14) {
						this.potionLimit.setValue(14);
					}
					this.maxFood = (28-(potioncount*times));
					break;
				case 3:
					if(potioncount > 9) {
						this.potionLimit.setValue(9);
					}
					this.maxFood = (28-(potioncount*times));
					break;
			}
			
			if(foodcount > this.maxFood) {
				if(this.maxFood < 0) {
					this.maxFood =0;
				}
				this.foodLimit.setValue(this.maxFood);
			}
		}).start();
	}
	
	public void checkTimerLimit(JSpinner spinner) {
		if((int)spinner.getValue() < 0) {
			spinner.setValue(0);
		}
	}
	public void checkLimit(JSpinner spinner) {
		if((int)spinner.getValue() < 1) {
			spinner.setValue(1);
		}
	}
	
	public void setPrayers(ArrayList<Integer> list){
		this.prayers = list;
	}

}
