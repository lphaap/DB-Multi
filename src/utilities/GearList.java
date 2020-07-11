package utilities;

import java.io.Serializable;
import java.util.ArrayList;

public class GearList implements Serializable{
	private static final long serialVersionUID = 1010L;
	
	private String helmet;
	private String body;
	private String legs;
	private String boots;
	private String ammo;
	private String weapon;
	private String shield;
	private String gloves;
	private String amulet;
	private String ring;
	private String cape;
	
	public ArrayList<String> getList(){
		ArrayList<String> list = new ArrayList<String>();
		
		if(helmet != null && !helmet.equals("")) {
			list.add(helmet);
		}
		if(body != null && !body.equals("")) {
			list.add(body);
		}
		if(legs != null && !legs.equals("")) {
			list.add(legs);
		}
		if(boots != null && !boots.equals("")) {
			list.add(boots);
		}
		if(ammo != null && !ammo.equals("")) {
			list.add(ammo);
		}
		if(weapon != null && !weapon.equals("")) {
			list.add(weapon);
		}
		if(shield != null && !shield.equals("")) {
			list.add(shield);
		}
		if(gloves != null && !gloves.equals("")) {
			list.add(gloves);
		}
		if(amulet != null && !amulet.equals("")) {
			list.add(amulet);
		}
		if(cape != null && !cape.equals("")) {
			list.add(cape);
		}
		if(ring != null && !ring.equals("")) {
			list.add(ring);
		}
		
		return list;
	}
	
	
	public String getHelmet() {
		return helmet;
	}
	public void setHelmet(String helmet) {
		this.helmet = helmet;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getBoots() {
		return boots;
	}
	public void setBoots(String boots) {
		this.boots = boots;
	}
	public String getLegs() {
		return legs;
	}
	public void setLegs(String legs) {
		this.legs = legs;
	}
	public String getAmmo() {
		return ammo;
	}
	public void setAmmo(String ammo) {
		this.ammo = ammo;
	}
	public String getWeapon() {
		return weapon;
	}
	public void setWeapon(String weapon) {
		this.weapon = weapon;
	}
	public String getShield() {
		return shield;
	}
	public void setShield(String shield) {
		this.shield = shield;
	}
	public String getGloves() {
		return gloves;
	}
	public void setGloves(String gloves) {
		this.gloves = gloves;
	}
	public String getAmulet() {
		return amulet;
	}
	public void setAmulet(String amulet) {
		this.amulet = amulet;
	}
	public String getCape() {
		return cape;
	}
	public void setCape(String cape) {
		this.cape = cape;
	}
	public String getRing() {
		return ring;
	}
	public void setRing(String ring) {
		this.ring = ring;
	}
	
	
}
