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
import scripts.MageTrainerModule;
import scripts.SmithingModule;

public class GUIMagicModuleWindow extends JFrame {
	private ThreadController controller;
	private GUIMainWindow mainWindow;
	private ClientThread client;
	
	private JPanel contentPane;

	private JSpinner limit;
	
	private JComboBox comboBoxCurse;
	private JComboBox comboBoxItem;
	private JComboBox comboBoxLocation;
	
	private JCheckBox alchemyCheck;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIMagicModuleWindow frame = new GUIMagicModuleWindow(null,null,null);
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
	public GUIMagicModuleWindow(ThreadController controller, ClientThread client, GUIMainWindow mainWindow) {
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
		
		
		JLabel lblSplashing = new JLabel("Splashing:");
		lblSplashing.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSplashing.setBounds(10, 59, 168, 22);
		contentPane.add(lblSplashing);
		
		comboBoxCurse = new JComboBox();
		comboBoxCurse.setBounds(10, 82, 120, 22);
		comboBoxCurse.setModel(new DefaultComboBoxModel(MageTrainerModule.Curse.values()));
		contentPane.add(comboBoxCurse);
		
		JLabel lblMaterial = new JLabel("Alching:");
		lblMaterial.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblMaterial.setBounds(10, 110, 168, 22);
		contentPane.add(lblMaterial);
		
		comboBoxItem = new JComboBox();
		comboBoxItem.setBounds(10, 133, 120, 22);
		comboBoxItem.setModel(new DefaultComboBoxModel(MageTrainerModule.AlchemyItem.values()));
		contentPane.add(comboBoxItem);
		
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
		btnStart.setBounds(188, 173, 90, 22);
		contentPane.add(btnStart);
		
		JLabel lblPickup = new JLabel("Cast Alchemy:");
		lblPickup.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPickup.setBounds(166, 127, 94, 22);
		contentPane.add(lblPickup);
		
		alchemyCheck = new JCheckBox();
		alchemyCheck.setBounds(266, 127, 35, 22);
		alchemyCheck.setSelected(true);
		contentPane.add(alchemyCheck);
	}
	
	public void parseModule() {
		controller.addModule(new MageTrainerModule(controller, client, 
							(int)this.limit.getValue(),
							(LocationFactory.GameLocation)this.comboBoxLocation.getSelectedItem(),
							(MageTrainerModule.Curse)this.comboBoxCurse.getSelectedItem(), 
							(this.alchemyCheck.isSelected()),
							(MageTrainerModule.AlchemyItem)this.comboBoxItem.getSelectedItem()));
	}
	
	public void parseInfo() {
		if(this.alchemyCheck.isSelected()) {
			mainWindow.addToList("MageTrainerModule: " + (this.comboBoxCurse.getSelectedItem().toString()) + " | " + (this.comboBoxItem.getSelectedItem().toString()));
		}
		else {
			mainWindow.addToList("MageTrainerModule: " + (this.comboBoxCurse.getSelectedItem().toString()));
		}
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
