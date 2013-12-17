package menu;

import javax.media.opengl.GL;

public class PlayMenu extends MenuObject{
	private Button buttons[];
	private float[] buttonColor = {1,0,0};
	
	public static final byte NEW = 0;
	public static final byte CONTINUE = 1;
	public static final byte BACK = 2;
	
	
	/**
	 * Constructor creates menu objects
	 */
	public PlayMenu(int minX,int maxX,int minY,int maxY){
		super(minX,maxX,minY,maxY);
		
		buttons = new Button[3];
		
		buttons[0] = new Button("New",minX,maxX,minY+(maxY-minY)*2/3,maxY,buttonColor);
		buttons[1] = new Button("Continue",minX,maxX,minY+(maxY-minY)/3,minY+(maxY-minY)*2/3,buttonColor);
		buttons[2] = new Button("Back",minX,maxX,minY,minY+(maxY-minY)/3,buttonColor);
	}
	
	/**
	 * Returns the value of the button hovered over
	 */
	public int getButton(int x,int y){
		if(buttons[0].minX < x && x < buttons[0].maxX && buttons[0].minY < y && y < buttons[0].maxY)
			return NEW;
		if(buttons[1].minX < x && x < buttons[1].maxX && buttons[1].minY < y && y < buttons[1].maxY)
			return CONTINUE;
		if(buttons[2].minX < x && x < buttons[2].maxX && buttons[2].minY < y && y < buttons[2].maxY)
			return BACK;
		return -1;
	}
	
	/**
	 * Draw the menu
	 */
	public void display(GL gl){
		for(int i=0; i<buttons.length; i++)
			buttons[i].display(gl);
	}
	
	/**
	 * This methode is used to check if and what is selected
	 */
	public void update(int x,int y){
		
		// set all the buttons to false
		for (int i=0; i<buttons.length; i++) {
			buttons[i].setSelected(false);}
		
		// set selected button to true
		switch(getButton(x,y)){
		case NEW: 		buttons[NEW].setSelected(true);		break;
		case CONTINUE: 		buttons[CONTINUE].setSelected(true);		break;
		case BACK:		buttons[BACK].setSelected(true);		break;}
	}
}
