package mazerunner;

import javax.media.opengl.GL;

import loot.Weapon;
import model.Model;
import model.TexturedModel;

/**
 * Class for living creatures Player, Enemy
 */
public abstract class Creature extends GameObject {
	protected double hitpoints;
	protected double maxHP = 200; // can't be static final because not all the enemy's will have the same hp
	
	protected Weapon weapon;
	protected TexturedModel texturedModel;			// the model of the Creature
	
	/**
	 * The Creature constructor.
	 * 
	 * @param x					The x coordinate of the location
	 * @param y					The y coordinate of the location
	 * @param z					The z coordinate of the location
	 * @param hitpoints			The amount of hitpoints (HP) that the creature has
	 * @param weapon			The weapon the creature has (null if no weapon)
	 * @param modelFileLocation	The location of the model (.obj) file
	 */
	public Creature(GL gl,double x, double y, double z,double hitpoints,Weapon weapon,String modelFileLocation){
		super(x,y,z);
		this.hitpoints = hitpoints;
		this.weapon = weapon;
		if(this.weapon != null) // if Creature has no weapon weapon will be null
			this.weapon.setEquipped(true);
		if(modelFileLocation != null) // if Creature has no model the string will be null
			texturedModel = new TexturedModel(gl,new Model(modelFileLocation,0.75f));
	}
	
	/**
	 * Gives current the HP
	 * @return the current hp
	 */
	public double getHP(){return hitpoints;}
	
	/**
	 * Adds the HP
	 * @param add the amount to add
	 * @return true if addHP was successful false otherwise
	 */
	public boolean addHP(double add){
		if(add <= 0 || hitpoints == maxHP)
			return false;
		if(hitpoints + add <= maxHP)
			hitpoints += add;
		else
			hitpoints = maxHP;
		return true;
	}
	
	/**
	 * Remove HP
	 * @param remove amount to remove
	 */
	public void removeHP(double remove){
		if(remove <= 0)
			return;
		hitpoints -= remove;
	}
}
