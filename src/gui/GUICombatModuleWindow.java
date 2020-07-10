package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.border.EmptyBorder;

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
	
	private JComboBox comboBoxMonster;
	private JComboBox comboBoxFood;
	private JComboBox comboBoxTrain;
	
	private JCheckBox pickupCheck;
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
		lblLimit.setBounds(10, 59, 168, 22);
		contentPane.add(lblLimit);
		
		limit = new JSpinner();
		limit.setBounds(10, 82, 71, 22);
		limit.addChangeListener(e -> {
			checkLimit(limit);
		});
		limit.setValue(1);
		contentPane.add(limit);

		JLabel lblFoodLimit = new JLabel("Food limit:");
		lblFoodLimit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblFoodLimit.setBounds(229, 59, 168, 22);
		contentPane.add(lblFoodLimit);
		
		foodLimit = new JSpinner();
		foodLimit.setBounds(229, 82, 71, 22);
		foodLimit.addChangeListener(e -> {
			checkFoodTimerLimit(foodLimit);
		});
		contentPane.add(foodLimit);
		
		JLabel lblTimeLimit = new JLabel("Time limit:");
		lblTimeLimit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTimeLimit.setBounds(120, 59, 168, 22);
		contentPane.add(lblTimeLimit);
		
		timeLimit = new JSpinner();
		timeLimit.setBounds(120, 82, 71, 22);
		timeLimit.addChangeListener(e -> {
			checkFoodTimerLimit(timeLimit);
		});
		contentPane.add(timeLimit);
		
		
		JLabel lblMonster = new JLabel("Monster:");
		lblMonster.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblMonster.setBounds(40, 157, 168, 22);
		contentPane.add(lblMonster);
		
		comboBoxMonster = new JComboBox();
		comboBoxMonster.setBounds(10, 178, 120, 22);
		comboBoxMonster.setModel(new DefaultComboBoxModel(CombatModule.Monster.values()));
		contentPane.add(comboBoxMonster);
		
		
		JLabel lblFood = new JLabel("Food:");
		lblFood.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblFood.setBounds(207, 157, 168, 22);
		contentPane.add(lblFood);
		
		comboBoxFood = new JComboBox();
		comboBoxFood.setBounds(170, 178, 120, 22);
		comboBoxFood.setModel(new DefaultComboBoxModel(CombatModule.Food.values()));
		contentPane.add(comboBoxFood);
		
		JLabel lblTraining = new JLabel("Training:");
		lblTraining.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTraining.setBounds(40, 233, 168, 22);
		contentPane.add(lblTraining);
		
		comboBoxTrain = new JComboBox();
		comboBoxTrain.setBounds(10, 258, 120, 22);
		comboBoxTrain.setModel(new DefaultComboBoxModel(CombatModule.Training.values()));
		contentPane.add(comboBoxTrain);
		
		JLabel lblPickup = new JLabel("Collect Items:");
		lblPickup.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPickup.setBounds(170, 256, 94, 22);
		contentPane.add(lblPickup);
		
		pickupCheck = new JCheckBox();
		pickupCheck.setBounds(270, 258, 35, 22);
		pickupCheck.setSelected(true);
		contentPane.add(pickupCheck);
		
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
		controller.addModule(new CombatModule(controller, client, ((CombatModule.Monster)(this.comboBoxMonster.getSelectedItem())), 
							((CombatModule.Food)(this.comboBoxFood.getSelectedItem())), 
							((int)this.limit.getValue()), ((int)this.timeLimit.getValue()), ((int)this.foodLimit.getValue()), 
							this.pickupCheck.isSelected(), ((CombatModule.Training)this.comboBoxTrain.getSelectedItem())));
	}
	
	public void parseInfo() {
		mainWindow.addToList("CombatModule: " + (this.comboBoxMonster.getSelectedItem().toString()));
	}
	
	public void close() {
		parseModule();
		parseInfo();
		this.dispose();
	}
	
	public void checkFoodTimerLimit(JSpinner spinner) {
		if((int)spinner.getValue() < 0) {
			spinner.setValue(0);
		}
	}
	public void checkLimit(JSpinner spinner) {
		if((int)spinner.getValue() < 1) {
			spinner.setValue(1);
		}
	}

}
