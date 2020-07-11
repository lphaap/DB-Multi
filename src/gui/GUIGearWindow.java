package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import client.ThreadController;
import utilities.GearHandler;
import utilities.GearHandler.Gear;
import utilities.GearList;

public class GUIGearWindow extends JFrame {

	private JPanel contentPane;
	
	private JFormattedTextField mHelmBox;
	private JFormattedTextField mWepBox;
	private JFormattedTextField mBodyBox;
	private JFormattedTextField mLegBox;
	private JFormattedTextField mShieldBox;
	private JFormattedTextField mBootsBox;
	private JFormattedTextField mAmuletBox;
	private JFormattedTextField mRingBox;
	private JFormattedTextField mGlovesBox;
	private JFormattedTextField mCapeBox;
	private JFormattedTextField mAmmoBox;
	
	private JComboBox gearBox;
	
	private JLabel gearTittle;
	
	private JButton btnEdit;
	private boolean editing;
	
	private ThreadController controller;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIGearWindow frame = new GUIGearWindow(null);
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
	public GUIGearWindow(ThreadController controller) {
		this.controller = controller;
		
		this.setTitle("Gear Editor");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 350, 604);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel mainPanel = new JPanel();
		contentPane.add(mainPanel);
		
		gearTittle = new JLabel("Utility Gear");
		gearTittle.setFont(new Font("Tahoma", Font.BOLD, 16));
		gearTittle.setBounds(108, 11, 112, 22);
		mainPanel.add(gearTittle);
		
		gearBox = new JComboBox();
		gearBox.setBounds(10, 509, 130, 20);
		gearBox.setName("Ammo");
		gearBox.setModel(new DefaultComboBoxModel(new String[] {"Utility Gear", "Melee Gear", "Range Gear", "Magic Gear", "Other Gear"}));
		gearBox.addItemListener(l -> handleGearSelect());
		
		JLabel lblGear = new JLabel("Edit gear:");
		lblGear.setBounds(10, 492, 130, 14);
		
		mainPanel.add(lblGear);
		mainPanel.add(gearBox);
		
		btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleClick();
			}
		});
		btnEdit.setBounds(195, 508, 90, 22);
		mainPanel.add(btnEdit);

		
		mHelmBox = new JFormattedTextField();
		mHelmBox.setBounds(10, 85, 130, 20);
		mHelmBox.setName("Helmet");
		mHelmBox.addKeyListener(new KeyAdapter() {public void keyReleased(KeyEvent e) {checkChange(mHelmBox);}});
		
		mWepBox = new JFormattedTextField();
		mWepBox.setBounds(179, 85, 130, 20);
		mWepBox.setName("Weapon");
		mWepBox.addKeyListener(new KeyAdapter() {public void keyReleased(KeyEvent e) {checkChange(mWepBox);}});
		
		mBodyBox = new JFormattedTextField();
		mBodyBox.setBounds(10, 155, 130, 20);
		mBodyBox.setName("Body");
		mBodyBox.addKeyListener(new KeyAdapter() {public void keyReleased(KeyEvent e) {checkChange(mBodyBox);}});
		
		mShieldBox = new JFormattedTextField();
		mShieldBox.setBounds(179, 155, 130, 20);
		mShieldBox.setName("Shield");
		mShieldBox.addKeyListener(new KeyAdapter() {public void keyReleased(KeyEvent e) {checkChange(mShieldBox);}});
		
		mLegBox = new JFormattedTextField();
		mLegBox.setBounds(10, 225, 130, 20);
		mLegBox.setName("Legs");
		mLegBox.addKeyListener(new KeyAdapter() {public void keyReleased(KeyEvent e) {checkChange(mLegBox);}});
		
		mBootsBox = new JFormattedTextField();
		mBootsBox.setBounds(10, 295, 130, 20);
		mBootsBox.setName("Boots");
		mBootsBox.addKeyListener(new KeyAdapter() {public void keyReleased(KeyEvent e) {checkChange(mBootsBox);}});
		
		mAmuletBox = new JFormattedTextField ();
		mAmuletBox.setName("Amulet");
		mAmuletBox.setBounds(179, 225, 130, 20);
		mAmuletBox.addKeyListener(new KeyAdapter() {public void keyReleased(KeyEvent e) {checkChange(mAmuletBox);}});
		
		mGlovesBox = new JFormattedTextField();
		mGlovesBox.setName("Gloves");
		mGlovesBox.setBounds(10, 365, 130, 20);
		mGlovesBox.addKeyListener(new KeyAdapter() {public void keyReleased(KeyEvent e) {checkChange(mGlovesBox);}});
		
		mCapeBox = new JFormattedTextField();
		mCapeBox.setName("Cape");
		mCapeBox.setBounds(179, 365, 130, 20);
		mCapeBox.addKeyListener(new KeyAdapter() {public void keyReleased(KeyEvent e) {checkChange(mCapeBox);}});
		
		mAmmoBox = new JFormattedTextField();
		mAmmoBox.setBounds(90, 435, 130, 20);
		mAmmoBox.setName("Ammo");
		mAmmoBox.addKeyListener(new KeyAdapter() {public void keyReleased(KeyEvent e) {checkChange(mAmmoBox);}});
		
		mRingBox = new JFormattedTextField ();
		mRingBox.setName("Ring");
		mRingBox.setBounds(179, 295, 130, 20);
		mRingBox.addKeyListener(new KeyAdapter() {public void keyReleased(KeyEvent e) {checkChange(mRingBox);}});
		
		JLabel label_m5 = new JLabel("Helmet Slot");
		label_m5.setBounds(10, 70, 130, 14);
		
		JLabel label_m = new JLabel("Weapon Slot");
		label_m.setBounds(179, 70, 130, 14);
		
		JLabel label_m1 = new JLabel("Body Slot");
		label_m1.setBounds(10, 140, 130, 14);
		
		JLabel label_m2 = new JLabel("Shield Slot");
		label_m2.setBounds(179, 140, 130, 14);
		
		JLabel label_m3 = new JLabel("Legs Slot");
		label_m3.setBounds(10, 210, 130, 14);
		
		JLabel label_m4 = new JLabel("Boots Slot");
		label_m4.setBounds(10, 280, 130, 14);
		
		JLabel lblMAmulet = new JLabel("Amulet Slot");
		lblMAmulet.setBounds(179, 210, 130, 14);
		
		JLabel lblMGloves = new JLabel("Gloves Slot");
		lblMGloves.setBounds(10, 350, 130, 14);
		
		JLabel lblMCape = new JLabel("Cape Slot");
		lblMCape.setBounds(179, 350, 130, 14);
		
		JLabel lblAmmoSlot = new JLabel("Ammo Slot");
		lblAmmoSlot.setBounds(90, 420, 130, 14);
		
		JLabel lblMRing = new JLabel("Ring Slot");
		lblMRing.setBounds(179, 280, 130, 14);
		
		mainPanel.setLayout(null);
		
		mainPanel.add(mHelmBox);
		mainPanel.add(mWepBox);
		mainPanel.add(mBodyBox);
		mainPanel.add(mShieldBox);
		mainPanel.add(mLegBox);
		mainPanel.add(mBootsBox);
		mainPanel.add(mAmuletBox);
		mainPanel.add(mRingBox);
		mainPanel.add(mGlovesBox);
		mainPanel.add(mCapeBox);
		mainPanel.add(mAmmoBox);
		
		mainPanel.add(label_m5);
		mainPanel.add(label_m);
		mainPanel.add(label_m1);
		mainPanel.add(label_m2);
		mainPanel.add(label_m3);
		mainPanel.add(label_m4);
		mainPanel.add(lblMAmulet);
		mainPanel.add(lblMRing);
		mainPanel.add(lblMGloves);
		mainPanel.add(lblMCape);
		mainPanel.add(lblAmmoSlot);
		
		handleStart();

	}
	
	public void checkChange(JFormattedTextField field) {
		new Thread(() -> {
			String word = (field.getText());
			boolean error = false;
			if(word == null || word.length() <= 0) {
				field.setBackground(Color.WHITE);
				return;
			}
			if(!Character.isUpperCase((word.toCharArray())[0])) {
				error = true;
			}
			for(Character c : word.substring(1).toCharArray()) {
				if(Character.isUpperCase(c)) {
					field.setBackground(Color.RED);
					error = true;
				}
			}
			if(!error) {
				field.setBackground(Color.GREEN);
			}
			else {
				field.setBackground(Color.RED);
			}
			
		}).start();
	}

	public void handleStart() {
		editFalse();
		new Thread(() -> {parseGearSave(GearHandler.Gear.UTILITY);}).start();
	}
	
	public void handleClick() {
		if(this.editing) {
			this.btnEdit.setText("Edit");
			editFalse();
			resetBoxBackground();
			new Thread(() -> {saveGear();}).start();
			this.editing = false;
			//System.out.println("Saved");
		}
		else {
			this.btnEdit.setText("Save");
			editTrue();
			this.editing = true;
			//System.out.println("Editing");
		}
	}
	
	public void handleGearSelect() {
		this.gearTittle.setText((String)this.gearBox.getSelectedItem());
		this.btnEdit.setText("Edit");
		editFalse();
		resetBoxBackground();
		if(((String)this.gearBox.getSelectedItem()).equals("Utility Gear")) {
			new Thread(() -> {parseGearSave(GearHandler.Gear.UTILITY);}).start();
		}
		else if(((String)this.gearBox.getSelectedItem()).equals("Melee Gear")) {
			new Thread(() -> {parseGearSave(GearHandler.Gear.MELEE);}).start();
		}
		else if(((String)this.gearBox.getSelectedItem()).equals("Range Gear")) {
			new Thread(() -> {parseGearSave(GearHandler.Gear.RANGE);}).start();
		}
		else if(((String)this.gearBox.getSelectedItem()).equals("Magic Gear")) {
			new Thread(() -> {parseGearSave(GearHandler.Gear.MAGIC);}).start();
		}
		else if(((String)this.gearBox.getSelectedItem()).equals("Other Gear")) {
			new Thread(() -> {parseGearSave(GearHandler.Gear.OTHER);}).start();
		}
		this.editing = false;
	}
	
	public void parseGearSave(GearHandler.Gear gear) {
		GearList parse = new GearList();
		FileInputStream fi;
		ObjectInputStream oi;
		try {
			switch(gear) {
			case MAGIC:
				fi = new FileInputStream(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "/../DBMultiResources/magic.gear");
				oi = new ObjectInputStream(fi);
				parse = (GearList)oi.readObject();
				oi.close();
				fi.close();
				break;
				
			case UTILITY:
				fi = new FileInputStream(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "/../DBMultiResources/utility.gear");
				oi = new ObjectInputStream(fi);
				parse = (GearList)oi.readObject();
				oi.close();
				fi.close();
				break;
			
			case MELEE:
				fi = new FileInputStream(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "/../DBMultiResources/melee.gear");
				oi = new ObjectInputStream(fi);
				parse = (GearList)oi.readObject();
				oi.close();
				fi.close();
				break;
				
			case RANGE:
				fi = new FileInputStream(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "/../DBMultiResources/range.gear");
				oi = new ObjectInputStream(fi);
				parse = (GearList)oi.readObject();
				oi.close();
				fi.close();
				break;
				
			case OTHER:
				fi = new FileInputStream(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "/../DBMultiResources/other.gear");
				oi = new ObjectInputStream(fi);
				parse = (GearList)oi.readObject();
				oi.close();
				fi.close();
				break;
			}
		} catch (Exception e) {e.printStackTrace();}
		
		this.mAmmoBox.setText(parse.getAmmo());
		this.mHelmBox.setText(parse.getHelmet());
		this.mBodyBox.setText(parse.getBody());
		this.mLegBox.setText(parse.getLegs());
		this.mBootsBox.setText(parse.getBoots());
		this.mAmuletBox.setText(parse.getAmulet());
		this.mRingBox.setText(parse.getRing());
		this.mGlovesBox.setText(parse.getGloves());
		this.mCapeBox.setText(parse.getCape());
		this.mShieldBox.setText(parse.getShield());
		this.mWepBox.setText(parse.getWeapon());
		
	}
	
	public void saveGear() {
		GearList parse = new GearList();
		FileOutputStream fi;
		ObjectOutputStream oi;
		parse.setHelmet(mHelmBox.getText());
		parse.setBody(mBodyBox.getText());
		parse.setLegs(mLegBox.getText());
		parse.setBoots(mBootsBox.getText());
		parse.setWeapon(mWepBox.getText());
		parse.setShield(mShieldBox.getText());
		parse.setGloves(mGlovesBox.getText());
		parse.setAmulet(mAmuletBox.getText());
		parse.setRing(mRingBox.getText());
		parse.setCape(mCapeBox.getText());
		parse.setAmmo(mAmmoBox.getText());
		
		try {
			if(((String)this.gearBox.getSelectedItem()).equals("Utility Gear")) {
				fi = new FileOutputStream(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "/../DBMultiResources/utility.gear");
				oi = new ObjectOutputStream(fi);
				oi.writeObject(parse);
				oi.close();
				fi.close();
			}
			else if(((String)this.gearBox.getSelectedItem()).equals("Melee Gear")) {
				fi = new FileOutputStream(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "/../DBMultiResources/melee.gear");
				oi = new ObjectOutputStream(fi);
				oi.writeObject(parse);
				oi.close();
				fi.close();
			}
			else if(((String)this.gearBox.getSelectedItem()).equals("Range Gear")) {
				fi = new FileOutputStream(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "/../DBMultiResources/range.gear");
				oi = new ObjectOutputStream(fi);
				oi.writeObject(parse);
				oi.close();
				fi.close();
			}
			else if(((String)this.gearBox.getSelectedItem()).equals("Magic Gear")) {
				fi = new FileOutputStream(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "/../DBMultiResources/magic.gear");
				oi = new ObjectOutputStream(fi);
				oi.writeObject(parse);
				oi.close();
				fi.close();
			}
			else if(((String)this.gearBox.getSelectedItem()).equals("Other Gear")) {
				fi = new FileOutputStream(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "/../DBMultiResources/other.gear");
				oi = new ObjectOutputStream(fi);
				oi.writeObject(parse);
				oi.close();
				fi.close();
			}
		} catch (Exception e) {}
	}
	
	public void resetBoxBackground() {
		this.mAmmoBox.setBackground(Color.WHITE);
		this.mHelmBox.setBackground(Color.WHITE);
		this.mBodyBox.setBackground(Color.WHITE);
		this.mLegBox.setBackground(Color.WHITE);
		this.mBootsBox.setBackground(Color.WHITE);
		this.mAmuletBox.setBackground(Color.WHITE);
		this.mRingBox.setBackground(Color.WHITE);
		this.mGlovesBox.setBackground(Color.WHITE);
		this.mCapeBox.setBackground(Color.WHITE);
		this.mShieldBox.setBackground(Color.WHITE);
		this.mWepBox.setBackground(Color.WHITE);
	}
	
	public void editFalse() {
		this.mAmmoBox.setEditable(false);
		this.mHelmBox.setEditable(false);
		this.mBodyBox.setEditable(false);
		this.mLegBox.setEditable(false);
		this.mBootsBox.setEditable(false);
		this.mAmuletBox.setEditable(false);
		this.mRingBox.setEditable(false);
		this.mGlovesBox.setEditable(false);
		this.mCapeBox.setEditable(false);
		this.mShieldBox.setEditable(false);
		this.mWepBox.setEditable(false);
	}
	
	public void editTrue() {
		this.mAmmoBox.setEditable(true);
		this.mHelmBox.setEditable(true);
		this.mBodyBox.setEditable(true);
		this.mLegBox.setEditable(true);
		this.mBootsBox.setEditable(true);
		this.mAmuletBox.setEditable(true);
		this.mRingBox.setEditable(true);
		this.mGlovesBox.setEditable(true);
		this.mCapeBox.setEditable(true);
		this.mShieldBox.setEditable(true);
		this.mWepBox.setEditable(true);
	}

}
