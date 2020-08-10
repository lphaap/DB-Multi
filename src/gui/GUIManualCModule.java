package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.border.EmptyBorder;

import client.ClientThread;
import client.ThreadController;
import scripts.CombatModule;
import scripts.ManualCombatModule;

	public class GUIManualCModule extends JFrame {
		private ThreadController controller;
		private GUIMainWindow mainWindow;
		private ClientThread client;
		
		private JPanel contentPane;

		private JSpinner limit;
		private JSpinner hoplimit;
		
		private JFormattedTextField comboBoxMonster;
		private JComboBox comboBoxFood;
		private JComboBox comboBoxTrain;
		private JComboBox comboBoxPotion;
		
		private JCheckBox pickupCheck;
		private JCheckBox slayerCheck;
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
		public GUIManualCModule(ThreadController controller, ClientThread client, GUIMainWindow mainWindow) {
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
			
			
			JLabel lblHop = new JLabel("Hop limit:");
			lblHop.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblHop.setBounds(205, 59, 83, 22);
			contentPane.add(lblHop);
			
			JLabel lblMonster2 = new JLabel("Monster:");
			lblMonster2.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblMonster2.setBounds(205, 148, 83, 22);
			contentPane.add(lblMonster2);
			
			comboBoxMonster = new JFormattedTextField();
			comboBoxMonster.setBounds(180, 172, 120, 22);
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
			contentPane.add(comboBoxPotion);
			
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
			lblPickup.setBounds(180, 317, 94, 22);
			contentPane.add(lblPickup);
			
			pickupCheck = new JCheckBox();
			pickupCheck.setBounds(262, 317, 35, 22);
			pickupCheck.setSelected(true);
			contentPane.add(pickupCheck);
			
			JLabel lblSlayer = new JLabel("Train Slayer:");
			lblSlayer.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblSlayer.setBounds(10, 317, 94, 22);
			contentPane.add(lblSlayer);
			
			slayerCheck = new JCheckBox();
			slayerCheck.setBounds(92, 317, 35, 22);
			slayerCheck.setSelected(true);
			contentPane.add(slayerCheck);
			
			JLabel lblPrayer = new JLabel("Use Prayer:");
			lblPrayer.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblPrayer.setBounds(103, 292, 94, 22);
			contentPane.add(lblPrayer);
			
			prayerCheck = new JCheckBox();
			prayerCheck.setBounds(180, 292, 35, 22);
			prayerCheck.addActionListener(l -> {
				System.out.println("T");
				if(prayerCheck.isSelected()) {
					new GUIPrayerWindow(null, this).show();
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
			
			hoplimit = new JSpinner();
			hoplimit.setBounds(205, 82, 71, 22);
			hoplimit.addChangeListener(e -> {
				//checkTimerLimit(hoplimit);
			});
			contentPane.add(hoplimit);
		}
		
		
		
		public void parseModule() {
			ManualCombatModule cm = (new ManualCombatModule(controller, client, ((CombatModule.Food)(this.comboBoxFood.getSelectedItem())), 
								((CombatModule.Potion)(this.comboBoxPotion.getSelectedItem())),
								((int)this.limit.getValue()), ((int)this.hoplimit.getValue()), 
								this.pickupCheck.isSelected(), this.slayerCheck.isSelected(), this.prayerCheck.isSelected(),
								((CombatModule.Training)this.comboBoxTrain.getSelectedItem()),
								this.comboBoxMonster.getText()));
			

			cm.setPrayerValues(this.prayers);
			controller.addModule(cm);
		}
		
		public void parseInfo() {
			mainWindow.addToList("CombatModule: " + (this.comboBoxMonster.getText()));
		}
		
		public void close() {
			parseModule();
			parseInfo();
			this.dispose();
		}

		public void setPrayers(ArrayList<Integer> list){
			this.prayers = list;
		}

		public void checkLimit(JSpinner spinner) {
			if((int)spinner.getValue() < 1) {
				spinner.setValue(1);
			}
		}

	}

