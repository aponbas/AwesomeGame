package menu;

import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;

public class OptionsMenu extends MenuObject{
	
	private Button buttons[];
	private float[] buttonColor = {1,0,0};
	
	public static final byte BACK = 0;
	
	/**
	 * Constructor creates menu objects
	 */
	public OptionsMenu(int minX,int maxX,int minY,int maxY){
		super(minX,maxX,minY,maxY);
		
		buttons = new Button[1];
		buttons[0] = new Button("Back",minX,maxX,minY,maxY/2,buttonColor);
	}
	
	/**
	 * Returns the value of the button hovered over
	 */
	public int getButton(int x,int y){
		if(buttons[0].minX < x && x < buttons[0].maxX && buttons[0].minY < y && y < buttons[0].maxY)
			return BACK;
		return -1;
	}
	
	/**
	 * Draw the menu
	 */
	public void display(GL gl) {
		gl.glPushMatrix();
		GLUT glut = new GLUT();
		float width = glut.glutStrokeLengthf(GLUT.STROKE_ROMAN, "Options"); // the width of the text-string in gl coordinations
		gl.glColor3f(1f,0f,0f);
		gl.glTranslatef(0, maxY-(maxY-minY)/4, 0); // Translation to upperside of screen
		gl.glScalef((maxX-minX)/width,(maxY-minY)/4/100f, 1f); // Text scale
		glut.glutStrokeString(GLUT.STROKE_ROMAN, "Options"); // Draw's the text
		gl.glPopMatrix();
		
		for (Button b : buttons) {
			b.display(gl);}
	}
	
	/**
	* This method is used to check if and what is selected
	**/
	public void update(int x, int y){
		// set all the buttons to false
		for (int i=0; i<buttons.length; i++) {
			buttons[i].setSelected(false);}
		
		// set selected button to true
		switch(getButton(x,y)){
		case BACK: 		buttons[BACK].setSelected(true);		break;}
	}
}
