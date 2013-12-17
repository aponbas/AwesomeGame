package mazerunner;

import java.awt.Point;

import javax.media.opengl.GL;

import loot.Weapon;

/**
 * Enemy represents enemies in the game
 * 
 * @author Marius
 */
public class Enemy extends Creature implements VisibleObject{
	
	private double horAngle;
	public double maxSpeed;
	private double speed;
	private int rotationSpeed;
	
	private EnemyControl control;		// the control controlling this enemy
	private boolean playerVisible;		// is true if the player is currently visible
	private boolean hitWall;			// is true if the enemy hit a wall
	private int TimePassed;				// integer used for keeping track of the "aggro" time
										// after a player disappears
	
	private Point memory; 				// holds the point previously visited in grid coordinates
	
	/**
	 * The Enemy constructor.
	 * 
	 * @param x					The x-grid location
	 * @param y					The y-grid location
	 * @param z					The z-grid location
	 * @param h					The horizontal angle of the orientation in degrees
	 * @param hp				The amount of HP that the enemy has
	 * @param weapon			The weapon of the Enemy
	 * @param modelFileLocation The location of the model file
	 */
	public Enemy(GL gl,int x, int y, int z, double horAngle, double hitpoints,
			Weapon weapon, String modelFileLocation) {
		// Set the initial position and viewing direction of the enemy.
		super(gl,x*Maze.SQUARE_SIZE + Maze.SQUARE_SIZE/2, 
				y*Maze.SQUARE_SIZE, 
				z*Maze.SQUARE_SIZE + Maze.SQUARE_SIZE/2, 
				hitpoints, weapon, modelFileLocation);
		
		this.horAngle = horAngle;
		maxSpeed = 0.005;
		speed = maxSpeed;
		rotationSpeed = 2;
		
		control = new EnemyControl();
		control.setEnemy(this);
		
		memory = new Point(x, z);
	}
 
	
	/*
	 * **********************************************
	 * *			Drawing functions 				*
	 * **********************************************
	 */
	
	/**
	 * enemy display function 
	 * draws the model of the enemy
	 */
	@Override
	public void display() { 
		if(texturedModel == null)
			System.out.println("null");
		else
		texturedModel.render(horAngle-180,locationX,locationY,locationZ);		
		
	}

	
	/*
	 * **********************************************
	 * *					update					*
	 * **********************************************
	 */
	
	/**
	 * Updates the physical location and orientation of the enemy
	 * @param deltaTime The time in milliseconds since the last update.
	 */
	public void update(int deltaTime) {
		
		// rotate the enemy, according to control
		horAngle += rotationSpeed*control.getdX();
		if (horAngle > 180) horAngle -= 360;
		else if (horAngle < -180) horAngle += 360;
		
		// move the enemy, according to control
		if (control.moveDirection != null) {
			locationX -= speed*deltaTime*
					Math.sin(Math.toRadians((control.moveDirection + horAngle)));
			locationZ -= speed*deltaTime*
					Math.cos(Math.toRadians((control.moveDirection + horAngle)));}
	}

	/**
	 * check if the enemy has reached its current target
	 */
	public boolean atTarget(double margin) {
		return control.atTarget(margin);
	}
	
	
	/*
	 * **********************************************
	 * *			getters and setters				*
	 * **********************************************
	 */

	protected double getHorAngle() {
		return horAngle;
	}

	protected void setHorAngle(double horAngle) {
		this.horAngle = horAngle;
	}

	protected double getSpeed() {
		return speed;
	}

	/**
	 * set the speed to a factor time maxSpeed
	 * @param speed
	 */
	protected void setSpeed(double speedFactor) {
		this.speed = speedFactor*maxSpeed;
	}

	protected Control getControl() {
		return control;
	}

	protected void setControl(EnemyControl control) {
		this.control = control;
	}
	
	public boolean isPlayerVisible() {
		return playerVisible;
	}
	
	public void setPlayerVisible(boolean playerVisible) {
		this.playerVisible = playerVisible;
	}

	/**
	 * Checks if the player is in the current enemy viewing cone
	 */
	public boolean derivePlayerInCone(Player player) {
		// find the angle to rotate
		double playerAngle = Math.toDegrees(
			Math.atan2(locationX - player.locationX, locationZ - player.locationZ));
		int angleToRotate = (int) Math.floor(playerAngle - horAngle);
		angleToRotate = (int) GameObject.normaliseAngle(angleToRotate);
		
		if (Math.abs(angleToRotate) < 45) return true;
		else return false;
	}

	/**
	 * get the memory
	 */
	public Point getMemory() {
		return memory;
	}

	/**
	 * set the memory
	 */
	public void setMemory(Point memory) {
		this.memory = memory;
	}

	/**
	 * has the enemy hit a wall
	 */
	public boolean hasHitWall() {
		return hitWall;
	}
	
	/**
	 * set hitWall
	 */
	public void setHitWall(boolean hitWall) {
		this.hitWall = hitWall;
	}

	/**
	 * get the time Passed since the player disappeared
	 */
	public int getTimePassed() {
		return TimePassed;
	}

	/**
	 * set timePassed
	 */
	public void setTimePassed(int timePassed) {
		TimePassed = timePassed;
	}


}
