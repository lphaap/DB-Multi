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
import scripts.CookerModule;
import scripts.FishingModule;

public class GUIFishingModuleWindow extends JFrame {
	private ThreadController controller;
	private GUIMainWindow mainWindow;
	private ClientThread client;
	
	private JPanel contentPane;

	private JSpinner limit;
	
	private JComboBox comboBoxFishing;
	
	private JCheckBox bankCheck;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIFishingModuleWindow frame = new GUIFishingModuleWindow(null,null,null);
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
	public GUIFishingModuleWindow(ThreadController controller, ClientThread client, GUIMainWindow mainWindow) {
		this.controller = controller;
		this.client = client;
		this.mainWindow = mainWindow;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 350, 225);
		contentPane = new JPanel();
		contentPane.setLayout(null);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblWindowTittle = new JLabel("FishingModule Setup");
		lblWindowTittle.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblWindowTittle.setBounds(92, 11, 168, 22);
		contentPane.add(lblWindowTittle);
		
		JLabel lblLimit = new JLabel("Action limit:");
		lblLimit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblLimit.setBounds(166, 59, 168, 22);
		contentPane.add(lblLimit);
		
		limit = new JSpinner();
		limit.setBounds(166, 82, 71, 22);
		limit.addChangeListener(e -> {
			checkLimit(limit);
		});
		limit.setValue(1);
		contentPane.add(limit);
		
		
		JLabel lblFishing = new JLabel("Fishing:");
		lblFishing.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblFishing.setBounds(10, 59, 168, 22);
		contentPane.add(lblFishing);
		
		comboBoxFishing = new JComboBox();
		comboBoxFishing.setBounds(10, 82, 120, 22);
		comboBoxFishing.setModel(new DefaultComboBoxModel(FishingModule.Fish.values()));
		contentPane.add(comboBoxFishing);
		
		JButton btnStart = new JButton("Ready");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread t = new Thread(() -> {
					close();
				});
				t.start();
	
			}
		});
		
		btnStart.setBounds(166, 137, 90, 22);
		contentPane.add(btnStart);
		
		JLabel lblPickup = new JLabel("Bank Fish:");
		lblPickup.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPickup.setBounds(10, 137, 94, 22);
		contentPane.add(lblPickup);
		
		bankCheck = new JCheckBox();
		bankCheck.setBounds(92, 137, 35, 22);
		bankCheck.setSelected(true);
		contentPane.add(bankCheck);
	}
	
	public void parseModule() {
		controller.addModule(new FishingModule(controller, client,
							(FishingModule.Fish)this.comboBoxFishing.getSelectedItem(),
							(int)this.limit.getValue(),
							(this.bankCheck.isSelected())));
	}
	
	public void parseInfo() {
		mainWindow.addToList("FishingModule: " + (this.comboBoxFishing.getSelectedItem().toString()));
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
