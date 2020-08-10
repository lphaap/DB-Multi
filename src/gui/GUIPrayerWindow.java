package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.dreambot.api.methods.prayer.Prayer;

public class GUIPrayerWindow extends JFrame {

	private JPanel contentPane;
	
	private GUICombatModuleWindow combatWindow;
	private GUIManualCModule manualCWindow;
	
	private JCheckBox prayerCheckbox8;
	private JCheckBox prayerCheckbox28;
	private ArrayList<JCheckBox> prayCheckList;
	
	private Map<Integer,Integer> spotToWidget = new HashMap<Integer,Integer>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIPrayerWindow frame = new GUIPrayerWindow(null, null);
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
	public GUIPrayerWindow(GUICombatModuleWindow combatWindow, GUIManualCModule combatMWindow) {
		this.combatWindow = combatWindow;
		manualCWindow = combatMWindow;
		
		createMap();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 344, 448);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		this.setTitle("Prayer Selection");
		setContentPane(contentPane);
		
	//	this.prayList1 = new PrayerList("Hot Key Prayers \"1\"");
		
		prayCheckList = new ArrayList<JCheckBox>();

		JPanel prayerPanel = new JPanel();
		prayerPanel.setBackground(Color.WHITE);
		contentPane.add(prayerPanel);
		
		prayerPanel.setLayout(null);
		
		
		ActionListener prayerCheckboxListener = new ActionListener() {
		    public void actionPerformed(ActionEvent e) {}
		};
		
		JButton start = new JButton("OK");
		start.setFont(new Font("Tahoma", Font.BOLD, 12));
		start.setBounds(255,345,53,43);
		start.addActionListener(l -> { start();});
		prayerPanel.add(start);
		
		JCheckBox prayerCheckbox1 = new JCheckBox("");
		prayerCheckbox1.setBackground(Color.DARK_GRAY);
		prayerCheckbox1.setBounds(6, 29, 17, 13);
		prayerPanel.add(prayerCheckbox1);
		prayCheckList.add(prayerCheckbox1);
		prayerCheckbox1.addActionListener(prayerCheckboxListener);
		
		JCheckBox prayerCheckbox2 = new JCheckBox("");
		prayerCheckbox2.setBackground(Color.DARK_GRAY);
		prayerCheckbox2.setBounds(69, 29, 17, 13);
		prayerPanel.add(prayerCheckbox2);
		prayCheckList.add(prayerCheckbox2);
		prayerCheckbox2.addActionListener(prayerCheckboxListener);
		
		JCheckBox prayerCheckbox3 = new JCheckBox("");
		prayerCheckbox3.setBackground(Color.DARK_GRAY);
		prayerCheckbox3.setBounds(131, 29, 17, 13);
		prayerPanel.add(prayerCheckbox3);
		prayCheckList.add(prayerCheckbox3);
		prayerCheckbox3.addActionListener(prayerCheckboxListener);
		
		JCheckBox prayerCheckbox4 = new JCheckBox("");
		prayerCheckbox4.setBackground(Color.DARK_GRAY);
		prayerCheckbox4.setBounds(193, 29, 17, 13);
		prayerPanel.add(prayerCheckbox4);
		prayCheckList.add(prayerCheckbox4);
		prayerCheckbox4.addActionListener(prayerCheckboxListener);
		
		JCheckBox prayerCheckbox5 = new JCheckBox("");
		prayerCheckbox5.setBackground(Color.DARK_GRAY);
		prayerCheckbox5.setBounds(255, 29, 17, 13);
		prayerPanel.add(prayerCheckbox5);
		prayCheckList.add(prayerCheckbox5);
		prayerCheckbox5.addActionListener(prayerCheckboxListener);
		
		JCheckBox prayerCheckbox6 = new JCheckBox("");
		prayerCheckbox6.setBackground(Color.DARK_GRAY);
		prayerCheckbox6.setBounds(6, 90, 17, 13);
		prayerPanel.add(prayerCheckbox6);
		prayCheckList.add(prayerCheckbox6);
		prayerCheckbox6.addActionListener(prayerCheckboxListener);
		
		JCheckBox prayerCheckbox7 = new JCheckBox("");
		prayerCheckbox7.setBackground(Color.DARK_GRAY);
		prayerCheckbox7.setBounds(69, 90, 17, 13);
		prayerPanel.add(prayerCheckbox7);
		prayCheckList.add(prayerCheckbox7);
		prayerCheckbox7.addActionListener(prayerCheckboxListener);
		
		prayerCheckbox8 = new JCheckBox("");
		prayerCheckbox8.setBackground(Color.DARK_GRAY);
		prayerCheckbox8.setBounds(131, 90, 17, 13);
		prayerPanel.add(prayerCheckbox8);
		prayCheckList.add(prayerCheckbox8);
		prayerCheckbox8.addActionListener(prayerCheckboxListener);
		
		JCheckBox prayerCheckbox9 = new JCheckBox("");
		prayerCheckbox9.setBackground(Color.DARK_GRAY);
		prayerCheckbox9.setBounds(193, 90, 17, 13);
		prayerPanel.add(prayerCheckbox9);
		prayCheckList.add(prayerCheckbox9);
		prayerCheckbox9.addActionListener(prayerCheckboxListener);
		
		JCheckBox prayerCheckbox10 = new JCheckBox("");
		prayerCheckbox10.setBackground(Color.DARK_GRAY);
		prayerCheckbox10.setBounds(255, 90, 17, 13);
		prayerPanel.add(prayerCheckbox10);
		prayCheckList.add(prayerCheckbox10);
		prayerCheckbox10.addActionListener(prayerCheckboxListener);
		
		JCheckBox prayerCheckbox11 = new JCheckBox("");
		prayerCheckbox11.setBackground(Color.DARK_GRAY);
		prayerCheckbox11.setBounds(6, 150, 17, 13);
		prayerPanel.add(prayerCheckbox11);
		prayCheckList.add(prayerCheckbox11);
		prayerCheckbox11.addActionListener(prayerCheckboxListener);
		
		JCheckBox prayerCheckbox12 = new JCheckBox("");
		prayerCheckbox12.setBackground(Color.DARK_GRAY);
		prayerCheckbox12.setBounds(69, 150, 17, 13);
		prayerPanel.add(prayerCheckbox12);
		prayCheckList.add(prayerCheckbox12);
		prayerCheckbox12.addActionListener(prayerCheckboxListener);
		
		JCheckBox prayerCheckbox13 = new JCheckBox("");
		prayerCheckbox13.setBackground(Color.DARK_GRAY);
		prayerCheckbox13.setBounds(131, 150, 17, 13);
		prayerPanel.add(prayerCheckbox13);
		prayCheckList.add(prayerCheckbox13);
		prayerCheckbox13.addActionListener(prayerCheckboxListener);
		
		JCheckBox prayerCheckbox14 = new JCheckBox("");
		prayerCheckbox14.setBackground(Color.DARK_GRAY);
		prayerCheckbox14.setBounds(193, 150, 17, 13);
		prayerPanel.add(prayerCheckbox14);
		prayCheckList.add(prayerCheckbox14);
		prayerCheckbox14.addActionListener(prayerCheckboxListener);
		
		JCheckBox prayerCheckbox15 = new JCheckBox("");
		prayerCheckbox15.setBackground(Color.DARK_GRAY);
		prayerCheckbox15.setBounds(255, 150, 17, 13);
		prayerPanel.add(prayerCheckbox15);
		prayCheckList.add(prayerCheckbox15);
		prayerCheckbox15.addActionListener(prayerCheckboxListener);
		
		JCheckBox prayerCheckbox16 = new JCheckBox("");
		prayerCheckbox16.setBackground(Color.DARK_GRAY);
		prayerCheckbox16.setBounds(6, 212, 17, 13);
		prayerPanel.add(prayerCheckbox16);
		prayCheckList.add(prayerCheckbox16);
		prayerCheckbox16.addActionListener(prayerCheckboxListener);
		
		JCheckBox prayerCheckbox17 = new JCheckBox("");
		prayerCheckbox17.setBackground(Color.DARK_GRAY);
		prayerCheckbox17.setBounds(69, 212, 17, 13);
		prayerPanel.add(prayerCheckbox17);
		prayCheckList.add(prayerCheckbox17);
		prayerCheckbox17.addActionListener(prayerCheckboxListener);
		
		JCheckBox prayerCheckbox18 = new JCheckBox("");
		prayerCheckbox18.setBackground(Color.DARK_GRAY);
		prayerCheckbox18.setBounds(131, 212, 17, 13);
		prayerPanel.add(prayerCheckbox18);
		prayCheckList.add(prayerCheckbox18);
		prayerCheckbox18.addActionListener(prayerCheckboxListener);
		
		JCheckBox prayerCheckbox19 = new JCheckBox("");
		prayerCheckbox19.setBackground(Color.DARK_GRAY);
		prayerCheckbox19.setBounds(193, 212, 17, 13);
		prayerPanel.add(prayerCheckbox19);
		prayCheckList.add(prayerCheckbox19);
		prayerCheckbox19.addActionListener(prayerCheckboxListener);
		
		JCheckBox prayerCheckbox20 = new JCheckBox("");
		prayerCheckbox20.setBackground(Color.DARK_GRAY);
		prayerCheckbox20.setBounds(255, 212, 17, 13);
		prayerPanel.add(prayerCheckbox20);
		prayCheckList.add(prayerCheckbox20);
		prayerCheckbox20.addActionListener(prayerCheckboxListener);
		
		JCheckBox prayerCheckbox21 = new JCheckBox("");
		prayerCheckbox21.setBackground(Color.DARK_GRAY);
		prayerCheckbox21.setBounds(6, 275, 17, 13);
		prayerPanel.add(prayerCheckbox21);
		prayCheckList.add(prayerCheckbox21);
		prayerCheckbox21.addActionListener(prayerCheckboxListener);
		
		JCheckBox prayerCheckbox22 = new JCheckBox("");
		prayerCheckbox22.setBackground(Color.DARK_GRAY);
		prayerCheckbox22.setBounds(69, 275, 17, 13);
		prayerPanel.add(prayerCheckbox22);
		prayCheckList.add(prayerCheckbox22);
		prayerCheckbox22.addActionListener(prayerCheckboxListener);
		
		JCheckBox prayerCheckbox23 = new JCheckBox("");
		prayerCheckbox23.setBackground(Color.DARK_GRAY);
		prayerCheckbox23.setBounds(131, 275, 17, 13);
		prayerPanel.add(prayerCheckbox23);
		prayCheckList.add(prayerCheckbox23);
		prayerCheckbox23.addActionListener(prayerCheckboxListener);
		
		JCheckBox prayerCheckbox24 = new JCheckBox("");
		prayerCheckbox24.setBackground(Color.DARK_GRAY);
		prayerCheckbox24.setBounds(193, 275, 17, 13);
		prayerPanel.add(prayerCheckbox24);
		prayCheckList.add(prayerCheckbox24);
		prayerCheckbox24.addActionListener(prayerCheckboxListener);
		
		JCheckBox prayerCheckbox25 = new JCheckBox("");
		prayerCheckbox25.setBackground(Color.DARK_GRAY);
		prayerCheckbox25.setBounds(255, 275, 17, 13);
		prayerPanel.add(prayerCheckbox25);
		prayCheckList.add(prayerCheckbox25);
		prayerCheckbox25.addActionListener(prayerCheckboxListener);
		
		JCheckBox prayerCheckbox26 = new JCheckBox("");
		prayerCheckbox26.setBackground(Color.DARK_GRAY);
		prayerCheckbox26.setBounds(6, 335, 17, 13);
		prayerPanel.add(prayerCheckbox26);
		prayCheckList.add(prayerCheckbox26);
		prayerCheckbox26.addActionListener(prayerCheckboxListener);
		
		JCheckBox prayerCheckbox27 = new JCheckBox("");
		prayerCheckbox27.setBackground(Color.DARK_GRAY);
		prayerCheckbox27.setBounds(69, 335, 17, 13);
		prayerPanel.add(prayerCheckbox27);
		prayCheckList.add(prayerCheckbox27);
		prayerCheckbox27.addActionListener(prayerCheckboxListener);
		
		prayerCheckbox28 = new JCheckBox("");
		prayerCheckbox28.setBackground(Color.DARK_GRAY);
		prayerCheckbox28.setBounds(131, 335, 17, 13);
		prayerPanel.add(prayerCheckbox28);
		prayCheckList.add(prayerCheckbox28);
		prayerCheckbox28.addActionListener(prayerCheckboxListener);
		
		JCheckBox prayerCheckbox29 = new JCheckBox("");
		prayerCheckbox29.setBackground(Color.DARK_GRAY);
		prayerCheckbox29.setBounds(193, 335, 17, 13);
		prayerPanel.add(prayerCheckbox29);
		prayCheckList.add(prayerCheckbox29);
		prayerCheckbox29.addActionListener(prayerCheckboxListener);
		
		
		ImageIcon prayerImageBase = new ImageIcon(getClass().getResource("/DBMultiResources/Prayer2.jpg"));
		
		JLabel prayerPicLabel2 = new JLabel(prayerImageBase);
		prayerPicLabel2.setBounds(0, 0, 319, 409);
		prayerPanel.add(prayerPicLabel2);

	}
	
	public class PrayerList {
		private Prayer[] prayers;
		private ArrayList<Prayer> active;
		private String name;
		private ArrayList<Boolean> checkValues;
		
		public PrayerList(String guiName) {
			prayers = Prayer.values();
			active = new ArrayList<Prayer>();
			this.name = guiName;
			this.checkValues = new ArrayList<Boolean>();
		}
		
		/**
		 * Takes values list from GUI
		 * This tells the method which prayers in different indexes have been selected
		 * @param values
		 */
		public void setActive(ArrayList<JCheckBox> values) {
			active.clear();
			for(JCheckBox box : values) {
				if(box.isSelected()){
					active.add(prayers[values.indexOf(box)]);
				}
			}
		}
		
		public ArrayList<Prayer> getActive() {
			return active;
		}
		
		public String toString(){
			return this.name;
			
		}
		
		/**
		 * Sets the boolean values for cheking if gui checkboxes should be selected
		 * @param boxes
		 */
		public void setCheckValues(ArrayList<JCheckBox> boxes) {
			checkValues.clear();
			for(JCheckBox box : boxes) {
				checkValues.add(box.isSelected());
			}
		}
		
		public ArrayList<Boolean> getCheckValues() {
			return this.checkValues;
		}
	}
	
	public void start() {
		ArrayList<Integer> values = new ArrayList<Integer>();
		for(JCheckBox box : this.prayCheckList) {
			if(box.isSelected()) {
				values.add(spotToWidget.get(prayCheckList.indexOf(box)));
			}
		}
		if(this.combatWindow != null) {
			this.combatWindow.setPrayers(values);
		}
		if(this.manualCWindow != null) {
			this.manualCWindow.setPrayers(values);
		}
		this.dispose();
	}
	
	public void createMap() {
		spotToWidget.put(0, 0);
		spotToWidget.put(1, 1);
		spotToWidget.put(2, 2);
		spotToWidget.put(3, 18);
		spotToWidget.put(4, 19);
		spotToWidget.put(5, 3);
		spotToWidget.put(6, 4);
		spotToWidget.put(7, 5);
		spotToWidget.put(8, 6);
		spotToWidget.put(9, 7);
		spotToWidget.put(10, 8);
		spotToWidget.put(11, 20);
		spotToWidget.put(12, 21);
		spotToWidget.put(13, 9);
		spotToWidget.put(14, 10);
		spotToWidget.put(15, 11);
		spotToWidget.put(16, 12);
		spotToWidget.put(17, 13);
		spotToWidget.put(18, 14);
		spotToWidget.put(19, 22);
		spotToWidget.put(20, 23);
		spotToWidget.put(21, 15);
		spotToWidget.put(22, 16);
		spotToWidget.put(23, 17);
		spotToWidget.put(24, 28);
		spotToWidget.put(25, 25);
		spotToWidget.put(26, 26);
		spotToWidget.put(27, 24);
		spotToWidget.put(28, 27);
	}


}
