package mazerunner;
/**
 * Camera represents the camera player in MazeRunner.
 * <p>
 * This class extends GameObject to take advantage of the already implemented location 
 * functionality. Furthermore, it also contains the orientation of the Camera, ie. 
 * where it is looking at. 
 * <p>
 * All these variables can be adjusted freely by MazeRunner. They could be accessed
 * by other classes if you pass a reference to them, but this should be done with 
 * caution.
 * 
 * @author Bruno Scheele
 *
 */
public class Camera extends GameObject {
	private double horAngle, verAngle;
	private double vrpX, vrpY, vrpZ;
	private double vuvX, vuvY, vuvZ;
	
	public Camera( double x, double y, double z, double h, double v ) {
		// Set the initial position and viewing direction of the player.
		super( x, y, z );
		horAngle = h;
		verAngle = v;
		
		// Calculate a likely view reference point.
		calculateVRP();
		
		// Set the view up vector to be parallel to the y-axis of the world.
		vuvX = 0.0;
		vuvY = 1.0;
		vuvZ = 0.0;
	}

	/**
	 * Calculates the View Reference Point (VRP) of the camera.
	 * 
	 * The VRP of the camera is set to be just 1 in front of the current orientation 
	 * of the camera. This makes it easy to create a first-person view setting, since it 
	 * always looks in front of the player.
	 */
	public void calculateVRP() {
		vrpX = locationX + -Math.sin( Math.PI * horAngle / 180 ) * Math.cos( Math.PI * verAngle / 180 );
		vrpY = locationY + Math.sin( Math.PI * verAngle / 180 );
		vrpZ = locationZ + -Math.cos( Math.PI * horAngle / 180 ) * Math.cos( Math.PI * verAngle / 180 );
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
	 * Returns the x-coordinate of the view reference point.
	 * @return the vrpX
	 */
	public double getVrpX() {
		return vrpX;
	}

	/**
	 * Sets the x-coordinate of the view reference point.
	 * @param vrpX the vrpX to set
	 */
	public void setVrpX(double vrpX) {
		this.vrpX = vrpX;
	}

	/**
	 * Returns the y-coordinate of the view reference point.
	 * @return the vrpY
	 */
	public double getVrpY() {
		return vrpY;
	}

	/**
	 * Sets the y-coordinate of the view reference point.
	 * @param vrpY the vrpY to set
	 */
	public void setVrpY(double vrpY) {
		this.vrpY = vrpY;
	}

	/**
	 * Returns the z-coordinate of the view reference point.
	 * @return the vrpZ
	 */
	public double getVrpZ() {
		return vrpZ;
	}

	/**
	 * Sets the z-coordinate of the view reference point.
	 * @param vrpZ the vrpZ to set
	 */
	public void setVrpZ(double vrpZ) {
		this.vrpZ = vrpZ;
	}

	/**
	 * Returns the x-coordinate of the view up vector.
	 * @return the vuvX
	 */
	public double getVuvX() {
		return vuvX;
	}

	/**
	 * Sets the x-coordinate of the view up vector.
	 * @param vuvX the vuvX to set
	 */
	public void setVuvX(double vuvX) {
		this.vuvX = vuvX;
	}

	/**
	 * Returns the y-coordinate of the view up vector.
	 * @return the vuvY
	 */
	public double getVuvY() {
		return vuvY;
	}

	/**
	 * Sets the y-coordinate of the view up vector.
	 * @param vuvY the vuvY to set
	 */
	public void setVuvY(double vuvY) {
		this.vuvY = vuvY;
	}

	/**
	 * Returns the z-coordinate of the view up vector.
	 * @return the vuvZ
	 */
	public double getVuvZ() {
		return vuvZ;
	}

	/**
	 * Sets the z-coordinate of the view up vector.
	 * @param vuvZ the vuvZ to set
	 */
	public void setVuvZ(double vuvZ) {
		this.vuvZ = vuvZ;
	}
}
