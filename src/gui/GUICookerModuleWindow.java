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
import movement.LocationFactory;
import scripts.CombatModule;
import scripts.CookerModule;

public class GUICookerModuleWindow extends JFrame {
	private ThreadController controller;
	private GUIMainWindow mainWindow;
	private ClientThread client;
	
	private JPanel contentPane;

	private JSpinner limit;
	
	private JComboBox comboBoxCooking;
	private JComboBox comboBoxLocation;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUICookerModuleWindow frame = new GUICookerModuleWindow(null,null,null);
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
	public GUICookerModuleWindow(ThreadController controller, ClientThread client, GUIMainWindow mainWindow) {
		this.controller = controller;
		this.client = client;
		this.mainWindow = mainWindow;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 350, 256);
		contentPane = new JPanel();
		contentPane.setLayout(null);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblWindowTittle = new JLabel("CookingModule Setup");
		lblWindowTittle.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblWindowTittle.setBounds(92, 11, 168, 22);
		contentPane.add(lblWindowTittle);
		
		JLabel lblLimit = new JLabel("Action limit:");
		lblLimit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblLimit.setBounds(31, 137, 168, 22);
		contentPane.add(lblLimit);
		
		limit = new JSpinner();
		limit.setBounds(31, 160, 71, 22);
		limit.setValue(1);
		limit.addChangeListener(e -> {
			checkLimit(limit);
		});
		contentPane.add(limit);
		
		
		JLabel lblCooking = new JLabel("Cooking:");
		lblCooking.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCooking.setBounds(10, 59, 168, 22);
		contentPane.add(lblCooking);
		
		comboBoxCooking = new JComboBox();
		comboBoxCooking.setBounds(10, 82, 120, 22);
		comboBoxCooking.setModel(new DefaultComboBoxModel(CookerModule.Cook.values()));
		contentPane.add(comboBoxCooking);
		
		JLabel lblLocation = new JLabel("Location:");
		lblLocation.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblLocation.setBounds(168, 59, 168, 22);
		contentPane.add(lblLocation);
		
		comboBoxLocation = new JComboBox();
		comboBoxLocation.setBounds(168, 82, 156, 22);
		comboBoxLocation.setModel(new DefaultComboBoxModel(LocationFactory.GameLocation.values()));
		contentPane.add(comboBoxLocation);
		
		JButton btnStart = new JButton("Ready");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread t = new Thread(() -> {
					close();
				});
				t.start();
	
			}
		});
		btnStart.setBounds(194, 160, 90, 22);
		contentPane.add(btnStart);
	}
	
	public void parseModule() {
		controller.addModule(new CookerModule(controller, client, (LocationFactory.GameLocation)this.comboBoxLocation.getSelectedItem(), 
							(CookerModule.Cook)this.comboBoxCooking.getSelectedItem(), (int)this.limit.getValue()));
	}
	
	public void parseInfo() {
		mainWindow.addToList("CookerModule: " + (this.comboBoxCooking.getSelectedItem().toString()));
	}
	
	public void close() {
		parseModule();
		parseInfo();
		this.dispose();
	}
	
	public void checkLimit(JSpinner spinner) {
		if((int)spinner.getValue() < 1) {
			spinner.setValue(1);
		}
	}

}
