package mazerunner;

import java.util.ArrayList;

public class EnemyControl extends Control{

	private Enemy enemy;					// the associated enemy
	
	// target information
	public ArrayList<Location> targets = new ArrayList<Location>();
	private double targetX;					// target x coordinate
	private double targetZ;					// target y coordinate
	
	private double targetAngle;				// angle of the current target
	private int angleToRotate;				// angle to rotate
	
	
	/**
	 * update the control variables for the related enemy
	 */
	@Override
	public void update() {
		
		targetX = targets.get(0).locationX;
		targetZ = targets.get(0).locationZ;
		
		// find the target angle
		targetAngle = (180/Math.PI)*
			Math.atan2(enemy.locationX - targetX, enemy.locationZ - targetZ);
		
		angleToRotate = (int) Math.floor(targetAngle - enemy.getHorAngle());
		
		// normalise the angle to rotate
		angleToRotate = (int) GameObject.normaliseAngle(angleToRotate);
		
		// set the moving variables
		deriveMovement();
		deriveRotation();
	}
		
	/**
	 * deriveMovement() sets forward to true if there is a target
	 */
	private void deriveMovement() {
		if (!targets.isEmpty() && Math.abs(angleToRotate) < 30) {
			moveDirection = 0;
			
			// set the speed
			if (Math.abs(angleToRotate) > 10) {
				enemy.setSpeed((10d/angleToRotate));}
			else enemy.setSpeed(1);}
		
		else moveDirection = null;
	}
	
	/**
	 * deriveRotation() calculates the amount of rotation required for the current enemy
	 */
	private void deriveRotation() {
		if (!targets.isEmpty() && Math.abs(angleToRotate) > 1)
			dX =  (int) Math.signum(angleToRotate);
		else dX = 0;	
	}
	
	/**
	 * sets the current target
	 */
	public void setTarget(double targetX, double targetZ) {
		targets.clear();
		targets.add(new Location(targetX, targetZ));
	}
	
	/**
	 * updates the main target
	 */
	public void updateMainTarget(double targetX, double targetZ) {
		if (targets.isEmpty()) 
			targets.add(new Location(targetX, targetZ));
		else
			targets.set(targets.size()-1, new Location(targetX, targetZ));
	}
	
	/**
	 * reset the target to null
	 */
	public void resetTarget() {
		targets.clear();
	}

	/**
	 * atTarget() checks if the enemy has reached the target or the target
	 */
	public boolean atTarget(double margin) {
		return ((Math.abs(enemy.locationX - targetX) < margin) && 
				(Math.abs(enemy.locationZ - targetZ) < margin));
	}
	
	/**
	 * sets the enemy
	 */
	public void setEnemy(Enemy enemy) {
		this.enemy = enemy;
	}	
}
