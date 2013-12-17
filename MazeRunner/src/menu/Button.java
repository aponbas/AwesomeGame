package menu;
import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;

public class Button extends MenuObject{
	private String text;
	private float colorRed,colorGreen,colorBlue;
	
	/**
	 * Button constructor
	 * @param text The text on the button
	 * @param gl The GL to draw to
	 * @param minX The left x
	 * @param maxX the right x
	 * @param minY the bottom y
	 * @param maxY the top y
	 */
	public Button(String text,int minX,int maxX,int minY,int maxY,float[] buttonColor){
		super(minX,maxX,minY,maxY);
		this.text = text;
		this.colorRed = buttonColor[0];
		this.colorGreen = buttonColor[1];
		this.colorBlue = buttonColor[2];
	}
	
	/**
	 * Draw the button
	 */
	public void display(GL gl){
		if(selected)
			gl.glColor3f(1-colorRed, 1-colorGreen, 1-colorBlue); // Color of the button background when selected
		else 
			gl.glColor3f(colorRed, colorGreen, colorBlue); // Color of the button background when not selected
		
		// Draw's the button background
		gl.glBegin(GL.GL_QUADS);
			gl.glVertex2f(minX, minY);
			gl.glVertex2f(maxX, minY);
			gl.glVertex2f(maxX, maxY);
			gl.glVertex2f(minX,maxY);
		gl.glEnd();
		
		// Draw's the text on the button
		gl.glPushMatrix();
		GLUT glut = new GLUT();
		float width = glut.glutStrokeLengthf(GLUT.STROKE_ROMAN, text); // the width of the text-string in gl coordinations
		float borderGap = 30; // Gap between the button border and the text
		
		if(selected)
			gl.glColor3f(colorRed,colorGreen,colorBlue); // Color of the text
		else
			gl.glColor3f(1-colorRed,1-colorGreen, 1-colorBlue);
		
		gl.glTranslatef(minX+borderGap, minY+borderGap, 0); // Translation to the button
		gl.glScalef((maxX-minX-borderGap*2)/width, (maxY-minY-borderGap*2)/100f, 1f); // Text scale to the button size
		glut.glutStrokeString(GLUT.STROKE_ROMAN, text); // Draw's the text
		
		gl.glPopMatrix();
	}
}
