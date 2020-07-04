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
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.awt.List;
import java.awt.TextField;

public class GUIMainWindow extends JFrame {
	ThreadController controller;
	ClientThread client;

	private JPanel contentPane;
	
	JComboBox comboBoxScriptEditor;
	
	private JSpinner pauseSpinnerMin;
	private JSpinner pauseSpinnerMax;
	private JSpinner scriptSpinnerMax;
	private JSpinner scriptSpinnerMin;
	
	private List listScript;

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
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 430, 705);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		comboBoxScriptEditor = new JComboBox();
		comboBoxScriptEditor.setBounds(10, 66, 161, 22);
		comboBoxScriptEditor.setModel(new DefaultComboBoxModel(new String[] 
									{"CombatModule", "MagicModule", "MiningModule", "SmithingModule",
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
		
		JButton btnGearEditor = new JButton("Gear Editor");
		btnGearEditor.addActionListener(e -> openGearEditor());
		btnGearEditor.setBounds(294, 66, 110, 22);
		contentPane.add(btnGearEditor);
		
		listScript = new List();
		listScript.setBounds(10, 108, 394, 449);
		
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
		
		JLabel lblPausetimer = new JLabel("Pause");
		lblPausetimer.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPausetimer.setBounds(172, 598, 144, 22);
		contentPane.add(lblPausetimer);
		
		JLabel lblScripttimer = new JLabel("Script");
		lblScripttimer.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblScripttimer.setBounds(172, 631, 144, 22);
		contentPane.add(lblScripttimer);
		
		JLabel lblTimers = new JLabel("Timers");
		lblTimers.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblTimers.setBounds(10, 573, 144, 22);
		contentPane.add(lblTimers);
		
		JButton btnStart = new JButton("Start");
		btnStart.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startBot();
			}
		});
		btnStart.setBounds(268, 614, 90, 22);
		contentPane.add(btnStart);
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
		System.out.println("TODO: Start");
	}
	
	public void addNewScript() {
		if(((String)this.comboBoxScriptEditor.getSelectedItem()).equals("CombatModule")) {
			GUICombatModuleWindow window = new GUICombatModuleWindow(controller, client, this);
			window.show();
		}
		else if(((String)this.comboBoxScriptEditor.getSelectedItem()).equals("MagicModule")) {
			System.out.println("TODO: MagicModule");
		}
		else if(((String)this.comboBoxScriptEditor.getSelectedItem()).equals("MiningModule")) {
			System.out.println("TODO: MiningModule");
		}
		else if(((String)this.comboBoxScriptEditor.getSelectedItem()).equals("SmithingModule")) {
			System.out.println("TODO: SmithingModule");
		}
		else if(((String)this.comboBoxScriptEditor.getSelectedItem()).equals("SmeltingModule")) {
			System.out.println("TODO: SmeltingModule");
		}
		else if(((String)this.comboBoxScriptEditor.getSelectedItem()).equals("JewelleryModule")) {
			System.out.println("TODO: JewelleryModule");
		}
		else if(((String)this.comboBoxScriptEditor.getSelectedItem()).equals("FishingModule")) {
			System.out.println("TODO: FishingModule");
		}
		else if(((String)this.comboBoxScriptEditor.getSelectedItem()).equals("CookingModule")) {
			System.out.println("TODO: CookingModule");
		}
	}
	
	public void openGearEditor() {
		System.out.println("TODO: GearEditor");
	}
	
	public List getModuleList() {
		return this.listScript;
	}
	

}
