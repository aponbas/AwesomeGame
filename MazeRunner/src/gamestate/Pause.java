package gamestate;

import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;

/**
 * Class for the pause functionality of the game (PAUSE)
 */
public abstract class Pause {
	
	private static String pauseString = "PAUSE";
	
	/**
	  * Displays pause on the screen when the game is paused
	  */
	protected static void display(GL gl, int sw, int sh) {
        GLUT glut = new GLUT();
        
        //draw fading box
        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        	gl.glColor4f(0f, 0f, 0f, .5f);
        	gl.glVertex2d(0, 0);
        	gl.glVertex2d(sw, 0);
        	gl.glColor4f(0f, 0f, 0f, 1f);
        	gl.glVertex2d(sw, sh);
        	gl.glVertex2d(0, sh);
        gl.glEnd();
        
        // draw pause box
        gl.glColor4f(0, 0, 0, 1);
        gl.glBegin(GL.GL_QUADS);
        	gl.glVertex2d(0, 18);
        	gl.glVertex2d(sw, 18);
        	gl.glVertex2d(sw, 31);
        	gl.glVertex2d(0, 31);
        gl.glEnd();
        gl.glPopMatrix();
        
       	// draw pause string
        int length = glut.glutBitmapLength(GLUT.BITMAP_9_BY_15, pauseString);
        gl.glColor4f(0f, 1f, 0f, 1f);
        gl.glRasterPos2i(sw/2 - length/2, 20);
        glut.glutBitmapString(GLUT.BITMAP_9_BY_15, pauseString);
	}
}
