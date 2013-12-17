package mazerunner;

/**
 * Class holding information for an ingame, certain level, 2D location
 */
public class Location extends GameObject {
	
	public Location(double x, double z){
		super(x, 0, z);
	}
	
	public String toString() {
		return "[" + String.format("%.2f", locationX) + ", " + String.format("%.2f", locationZ) + "]";
	}
}
