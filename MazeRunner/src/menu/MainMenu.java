package menu;

import gamestate.GameState;
import gamestate.UserInput;

import javax.media.opengl.GL;

public class MainMenu extends MenuObject implements MenuInterface{
	
	private UserInput input;
	
	private Button buttons[];
	private PlayMenu playMenu;
	private OptionsMenu optionsMenu;
	private QuitMenu quitMenu;
	
	public MenuState menuState;
	private float[] buttonColor = {1,0,0};  
	
	public static final byte PLAY = 0;
	public static final byte OPTIONS = 1;
	public static final byte QUIT = 2;
	
	int x,y;
	
	/**
	 * Constructor creates menu objects
	 */
	public MainMenu(UserInput input, int minX,int maxX,int minY,int maxY){
		super(minX,maxX,minY,maxY);
		
		// set the menu state to MAIN
		menuState = MenuState.MAIN;
		
		// create sub menus
		playMenu = new PlayMenu(minX,maxX,minY,maxY);
		optionsMenu = new OptionsMenu(minX,maxX,minY,maxY);
		quitMenu = new QuitMenu(minX,maxX,minY,maxY);
		
		// create menu buttons
		buttons = new Button[3];
		buttons[0] = new Button("Play",minX,maxX,minY+(maxY-minY)*2/3,maxY,buttonColor);
		buttons[1] = new Button("Options",minX,maxX,minY+(maxY-minY)/3,minY+(maxY-minY)*2/3,buttonColor);
		buttons[2] = new Button("Quit",minX,maxX,minY,minY+(maxY-minY)/3,buttonColor);
		
		// set input object
		this.input = input;
	}
	
	/**
	 * Returns the value of the button hovered over
	 */
	public int getButton(int x,int y){
		if(buttons[0].minX < x && x < buttons[0].maxX && buttons[0].minY < y && y < buttons[0].maxY)
			return PLAY;
		if(buttons[1].minX < x && x < buttons[1].maxX && buttons[1].minY < y && y < buttons[1].maxY)
			return OPTIONS;
		if(buttons[2].minX < x && x < buttons[2].maxX && buttons[2].minY < y && y < buttons[2].maxY)
			return QUIT;
		return -1;
	}
	
	
	/*
	 * **********************************************
	 * *			   drawing methods				*
	 * **********************************************
	 */
	
	/**
	 * Select the current display method
	 */
	public void display(GL gl){
		
		switch(menuState) {
		case MAIN:
			displayMain(gl);
			break;
		case OPTIONS:
			optionsMenu.display(gl);
			break;
		case PLAY:
			playMenu.display(gl);
			break;
		case QUIT:
			quitMenu.display(gl);
			break;
		}
	}
	
	/**
	 * Draw the menu
	 */
	private void displayMain(GL gl) {
		for (Button b : buttons) {
			b.display(gl);}
	}

	
	/*
	 * **********************************************
	 * *			   update methods				*
	 * **********************************************
	 */
	
	/**
	 * Get the update variables and select the current update method
	 */
	public void update(){
		x = input.mouseLocation.x;
		y = input.mouseLocation.y;
		
		switch(menuState) {
		case MAIN:		updateMain(x,y); break;
		case OPTIONS:	optionsMenu.update(x,y); break;
		case PLAY:		playMenu.update(x,y); break;
		case QUIT:		quitMenu.update(x,y); break;}
		
		if(input.wasMousePressed()) {
			input.resetMousePressed();
			buttonPressed(x,y);}
	}
	
	/**
	 * Update the menu
	 */
	private void updateMain(int x, int y) {
		// set all the buttons to false
		for (int i=0; i<buttons.length; i++) {
			buttons[i].setSelected(false);}
		
		// set selected button to true
		switch(getButton(x,y)){
		case PLAY: 		buttons[PLAY].setSelected(true); break;
		case OPTIONS: 	buttons[OPTIONS].setSelected(true);	break;
		case QUIT:		buttons[QUIT].setSelected(true); break;}
	}

	
	/*
	 * **********************************************
	 * *			   buttonPressed				*
	 * **********************************************
	 */
	
	/**
	* This method performs the correct actionz when a button is pressed
	**/
	public void buttonPressed(int x, int y){
		
		switch(menuState) {
		case MAIN:
			switch(getButton(x,y)) {
			case PLAY: 		menuState = MenuState.PLAY; break;
			case OPTIONS: 	menuState = MenuState.OPTIONS; break;
			case QUIT: 		menuState = MenuState.QUIT; break;}
			break;
			
		case OPTIONS:
			switch(optionsMenu.getButton(x, y)) {
			case OptionsMenu.BACK:	menuState = MenuState.MAIN; break;}
			break;
			
		case PLAY:
			switch(playMenu.getButton(x, y)) {
			case PlayMenu.NEW:		
				input.setGameState(GameState.INGAME); 
				input.setNewGame(true);
				menuState = MenuState.MAIN;
				break;
			case PlayMenu.CONTINUE: input.setGameState(GameState.INGAME);
			case PlayMenu.BACK: 	menuState = MenuState.MAIN; break;}
			break;
			
		case QUIT:
			switch(quitMenu.getButton(x, y)) {
			case QuitMenu.YES: 		System.exit(0);	break;			
			case QuitMenu.NO: 		menuState = MenuState.MAIN; break;}
			break;
		}
	}
}
