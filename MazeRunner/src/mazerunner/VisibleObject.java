package mazerunner;

/**
 * VisibleObject is the interface for all classes that have to be displayed on screen.
 * <p> 
 * All the objects that have to be displayed on the screen, should implement this interface.
 * This forces the developer to implement all methods that are defined in this interface in 
 * the class, else the code will not compile.  
 * 
 * @author Bruno Scheele
 *
 */
public interface VisibleObject {
	/**
	 * display(GL) contains all the GL callback functions to display the object on the screen.
	 * <p>
	 * The OpenGL functions can be accessed through the GL context, for example:
	 * <ul>
	 * <li> <i>gl.glBegin( GL.GL_QUADS );</i>
	 * <li> <i>gl.glVertex3d( 0.0, 0.0, 0.0 );</i>
	 * <li> <i>gl.glEnd();</i>
	 * </ul>
	 * Note that both the functions as the GL static variables are called through gl. Because 
	 * the static variables should be called in a static way, we type GL in caps then, like GL.GL_QUADS.
	 * 
	 */
	void display();
}
