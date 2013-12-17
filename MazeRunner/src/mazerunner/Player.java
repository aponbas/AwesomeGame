package mazerunner;

import java.util.Iterator;

import javax.media.opengl.GL;

import loot.*;

/**
 * Player represents the actual player in MazeRunner.
 * <p>
 * This class extends GameObject to take advantage of the already implemented location 
 * functionality. Furthermore, it also contains the orientation of the Player, ie. 
 * where it is looking at and the player's speed. 
 * <p>
 * For the player to move, a reference to a Control object can be set, which can then
 * be polled directly for the most recent input. 
 * <p>
 * All these variables can be adjusted freely by MazeRunner. They could be accessed
 * by other classes if you pass a reference to them, but this should be done with 
 * caution.
 * 
 * @author Bruno Scheele
 *
 */
public class Player extends Creature {	
	private double horAngle, verAngle;
	private double speed;
	private double rotationSpeed;
	private Control control;
	
	private LootController lootController;
	
	/**
	 * The Player constructor.
	 * @param x			The x-coordinate of the location
	 * @param y			The y-coordinate of the location
	 * @param z			The z-coordinate of the location
	 * @param h			The horizontal angle of the orientation in degrees
	 * @param v			The vertical angle of the orientation in degrees
	 * @param hitpoints	The hit points (HP) of the player
	 * @param weapon	The weapon equipped by the player (null if not equipped)
	 */
	public Player(GL gl,LootController lootController,double x,double y,double z,double h,double v,double hitpoints,Weapon weapon){
		super(gl,x,y,z,hitpoints,weapon,null);
		horAngle = h;
		verAngle = v;
		speed = 0.01;
		rotationSpeed = 1;
		this.lootController = lootController;
	}
	
	
	/*
	 * **********************************************
	 * *			Setters and Getters				*
	 * **********************************************
	 */
	
	/**
	 * Sets the Control object that will control the player's motion
	 * <p>
	 * The control must be set if the object should be moved.
	 * @param input
	 */
	public void setControl(Control control)
	{
		this.control = control;
	}
	
	/**
	 * Gets the Control object currently controlling the player
	 * @return
	 */
	public Control getControl()
	{
		return control;
	}

	/**
	 * Returns the horizontal angle of the orientation.
	 * @return the horAngle
	 */
	public double getHorAngle() {
		return horAngle;
	}

	/**
	 * Sets the horizontal angle of the orientation.
	 * @param horAngle the horAngle to set
	 */
	public void setHorAngle(double horAngle) {
		this.horAngle = horAngle;
	}

	/**
	 * Returns the vertical angle of the orientation.
	 * @return the verAngle
	 */
	public double getVerAngle() {
		return verAngle;
	}

	/**
	 * Sets the vertical angle of the orientation.
	 * @param verAngle the verAngle to set
	 */
	public void setVerAngle(double verAngle) {
		this.verAngle = verAngle;
	}
	
	/**
	 * Returns the speed.
	 * @return the speed
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * Sets the speed.
	 * @param speed the speed to set
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	/**
	 * Updates the physical location and orientation of the player
	 * @param deltaTime The time in milliseconds since the last update.
	 */
	public void update(int deltaTime) {
		if (control != null) {
			// update control
			control.update();
			
			// rotate the player, according to control
			horAngle -= rotationSpeed*control.getdX();
			verAngle -= rotationSpeed*control.getdY();
			
			// move the player, according to control
			if (control.moveDirection != null) {
				locationX -= speed*deltaTime*
						Math.sin((horAngle + control.moveDirection)*(Math.PI/180));
				locationZ -= speed*deltaTime*
						Math.cos((horAngle + control.moveDirection)*(Math.PI/180));
			}
		}
		
		Loot currentLoot;
		Iterator<Loot> lootIterator = lootController.getList().listIterator();
		while(lootIterator.hasNext()){
			currentLoot = lootIterator.next();
			
			if(currentLoot.near(locationX,locationY,locationZ)){
				if(currentLoot instanceof Food){
					this.addHP(50);
					lootIterator.remove();}}}
		
		
	}
	
	public double doDamage(){
		if(weapon.getEquipped())
			return weapon.doDamage();
		return 0;
	}
}
