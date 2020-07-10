package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.border.EmptyBorder;

import client.ClientThread;
import client.ThreadController;
import movement.LocationFactory;
import scripts.JewelleryModule;
import scripts.SmithingModule;

public class GUIJewelleryModuleWindow extends JFrame {
	private ThreadController controller;
	private GUIMainWindow mainWindow;
	private ClientThread client;
	
	private JPanel contentPane;

	private JSpinner limit;
	
	private JComboBox comboBoxType;
	private JComboBox comboBoxMaterial;
	private JComboBox comboBoxLocation;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIJewelleryModuleWindow frame = new GUIJewelleryModuleWindow(null,null,null);
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
	public GUIJewelleryModuleWindow(ThreadController controller, ClientThread client, GUIMainWindow mainWindow) {
		this.controller = controller;
		this.client = client;
		this.mainWindow = mainWindow;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 350, 256);
		contentPane = new JPanel();
		contentPane.setLayout(null);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblWindowTittle = new JLabel("SmithingModule Setup");
		lblWindowTittle.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblWindowTittle.setBounds(92, 11, 168, 22);
		contentPane.add(lblWindowTittle);
		
		JLabel lblLimit = new JLabel("Action limit:");
		lblLimit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblLimit.setBounds(10, 160, 168, 22);
		contentPane.add(lblLimit);
		
		limit = new JSpinner();
		limit.setBounds(10, 184, 71, 22);
		limit.addChangeListener(e -> {
			checkLimit(limit);
		});
		limit.setValue(1);
		contentPane.add(limit);
		
		
		JLabel lblSmithing = new JLabel("Making:");
		lblSmithing.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSmithing.setBounds(10, 59, 168, 22);
		contentPane.add(lblSmithing);
		
		comboBoxType = new JComboBox();
		comboBoxType.setBounds(10, 82, 120, 22);
		comboBoxType.setModel(new DefaultComboBoxModel(JewelleryModule.JewelleryType.values()));
		contentPane.add(comboBoxType);
		
		JLabel lblMaterial = new JLabel("Material:");
		lblMaterial.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblMaterial.setBounds(10, 110, 168, 22);
		contentPane.add(lblMaterial);
		
		comboBoxMaterial = new JComboBox();
		comboBoxMaterial.setBounds(10, 133, 120, 22);
		comboBoxMaterial.setModel(new DefaultComboBoxModel(JewelleryModule.JewelleryMaterial.values()));
		contentPane.add(comboBoxMaterial);
		
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
		controller.addModule(new JewelleryModule(controller, client, 
							(LocationFactory.GameLocation)this.comboBoxLocation.getSelectedItem(), 
							(JewelleryModule.JewelleryMaterial)this.comboBoxMaterial.getSelectedItem(), 
							(JewelleryModule.JewelleryType)this.comboBoxType.getSelectedItem(), 
							(int)this.limit.getValue()));
	}
	
	public void parseInfo() {
		mainWindow.addToList("JewelleryModule: " + (this.comboBoxMaterial.getSelectedItem().toString()) + " " + (this.comboBoxType.getSelectedItem().toString()));
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
