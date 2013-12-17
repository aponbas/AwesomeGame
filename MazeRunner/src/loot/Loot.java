package loot;

import mazerunner.VisibleObject;
import model.Model;

public abstract class Loot implements VisibleObject {
	protected double x,y,z;
	protected Model model;
	
	/**
	 * Loot constructor.
	 * 
	 * @param x						The x-coordinate of the location
	 * @param y						The y-coordinate of the location
	 * @param z						The z-coordinate of the location
	 * @param modelFileLocation		The location of the model file
	 */
	public Loot(double x,double y,double z,String modelFileLocation){
		this.x = x;
		this.y = y;
		this.z = z;
		if(modelFileLocation != null)
			model.load(modelFileLocation);
	}
	
	/**
	 * Gives the X location 
	 * @return the x coordinate
	 */
	public double getX(){return x;}
	/**
	 * Gives the Y location
	 * @return the y coordinate
	 */
	public double getY(){return y;}
	/**
	 * Gives the z location
	 * @return the z coordinate
	 */
	public double getZ(){return z;}
	
	public boolean near(double x,double y,double z){
		if(this.y == y){
			double seperation = Math.sqrt(Math.pow(this.x-x, 2) + Math.pow(this.z-z, 2));
			if(seperation < 0.5)
				return true;
		}
		return false;
	}
}
