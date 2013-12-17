package menu;

import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;

public class QuitMenu extends MenuObject{
	private Button buttons[];
	private float[] buttonColor = {1,0,0};
	
	public static final byte YES = 0;
	public static final byte NO = 1;
	
	/**
	 * Constructor creates menu objects
	 */
	public QuitMenu(int minX,int maxX,int minY,int maxY){
		super(minX,maxX,minY,maxY);
		
		buttons = new Button[2];
		buttons[0] = new Button("Yes",minX,maxX,minY+(maxY-minY)/4,minY+(maxY-minY)/2,buttonColor);
		buttons[1] = new Button("No",minX,maxX,minY,minY+(maxY-minY)/4,buttonColor);
		
	}
	
	/**
	 * Returns the value of the button hovered over
	 */
	public int getButton(int x,int y){
		if(buttons[0].minX < x && x < buttons[0].maxX && buttons[0].minY < y && y < buttons[0].maxY)
			return YES;
		if(buttons[1].minX < x && x < buttons[1].maxX && buttons[1].minY < y && y < buttons[1].maxY)
			return NO;
		return -1;
	}
	
	/**
	 * Draw the menu
	 */
	public void display(GL gl){
		gl.glPushMatrix();
		GLUT glut = new GLUT();
		float width = glut.glutStrokeLengthf(GLUT.STROKE_ROMAN, "Are your sure?"); // the width of the text-string in gl coordinations
		gl.glColor3f(1f,0f,0f);
		gl.glTranslatef(0, maxY-(maxY-minY)/4, 0); // Translation to upperside of screen
		gl.glScalef((maxX-minX)/width,(maxY-minY)/4/100f, 1f); // Text scale
		glut.glutStrokeString(GLUT.STROKE_ROMAN, "Are your sure?"); // Draw's the text
		gl.glPopMatrix();
		
		for(int i=0; i<buttons.length; i++)
			buttons[i].display(gl);
	}
	
	/**
	* This methode is used to check if and what is selected
	**/
	public void update(int x, int y){
		// set all the buttons to false
		for (int i=0; i<buttons.length; i++) {
			buttons[i].setSelected(false);}
		
		// set selected button to true
		switch(getButton(x,y)){
		case YES: 		buttons[YES].setSelected(true);		break;
		case NO: 		buttons[NO].setSelected(true);		break;}
	}

}