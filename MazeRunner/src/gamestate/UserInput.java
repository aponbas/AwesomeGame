package gamestate;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.media.opengl.GLCanvas;

import mazerunner.Control;

/**
 * The UserInput class is an extension of the Control class. It also implements three 
 * interfaces, each providing handler methods for the different kinds of user input.
 *
 * Note: because of how java is designed, it is not possible for the game window to
 * react to user input if it does not have focus. The user must first click the window 
 * (or alt-tab or something) before further events, such as keyboard presses, will 
 * function.
 * 
 * @author Mattijs Driel
 *
 */
public class UserInput extends Control 
		implements MouseListener, MouseMotionListener, KeyListener {
	
	private GameState gameState;
	
	public Point mousePressedLocation;						// the mouse pressed location in canvas coordinates
	public Point mouseLocation;								// the mouse location in canvas coordinates
	private Point screenCenter;								// the screen center in screen coordinates
	private Dimension screenSize;							// the screen size
	private Dimension canvasSize;							// the canvas size
	
	private Robot robot;									// used for centering the mouse
	
	protected boolean wasMousePressed;						// boolean indicating a mouse press
	private boolean newGame = true;							// boolean indicating a new game start
	
	/**
	 * UserInput constructor.
	 * <p>
	 * To make the new UserInput instance able to receive input, listeners 
	 * need to be added to a GLCanvas.
	 * 
	 * @param canvas The GLCanvas to which to add the listeners.
	 */
	public UserInput(GLCanvas canvas, Dimension screenSize)
	{
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		canvas.addKeyListener(this);
		
		this.mouseLocation = new Point(0, 0);
		this.screenSize = screenSize;
		this.canvasSize = canvas.getSize();
		
		// calculate the screen center
		this.screenCenter = canvas.getLocationOnScreen(); 
		screenCenter.translate(screenSize.width/2, screenSize.height/2);
		
		// initalise a robot for INGAME mouse centering
		try {robot = new Robot();} 
		catch (AWTException e) {e.printStackTrace();}
	}
	
	/*
	 * **********************************************
	 * *				Updating					*
	 * **********************************************
	 */
	
	/**
	 * update()
	 */
	@Override
	public void update()
	{	
		// calculate rotation since previous update
		dX = mouseLocation.x - screenSize.width/2;
		dY = mouseLocation.y - screenSize.height/2;
		
		// move the mouse to the center of the screen
	    centerMouse();
	    
		/* 
		 * calculate walking direction angle (relative to viewing direction), null 
		 * means there is no movement. 
		 */
		moveDirection = null;
		Point temp = new Point();
		
		if (forward) 	temp.translate(0,1);
		if (back)		temp.translate(0,-1);
		if (left) 		temp.translate(1,0);
		if (right)		temp.translate(-1,0);
		
		if (temp.distance(0,0) != 0)
			moveDirection = (int) Math.floor((180d/Math.PI)*Math.atan2(temp.x, temp.y));

	}

	
	/*
	 * **********************************************
	 * *		Input event handlers				*
	 * **********************************************
	 */

	/**
	 * Detects where and if the mouse was pressed
	 * depending on 
	 */
	@Override
	public void mousePressed(MouseEvent event)
	{
		if (gameState == GameState.INGAME) {
			mousePressedLocation = new Point(event.getX(), event.getY());
		}
		else if (gameState == GameState.MENU) {
			wasMousePressed = true;}
	}
	
	/**
	 * Detects the location of the mouse
	 */
	@Override
	public void mouseMoved(MouseEvent event)
	{
		if(gameState == GameState.INGAME){
			mouseLocation = new Point(event.getX(), event.getY());}
		
		else if (gameState == GameState.MENU)
			mouseLocation = new Point(event.getX(), canvasSize.height - event.getY());
	}
	
	/**
	 * Detects keys pressed
	 */
	@Override
	public void keyPressed(KeyEvent event)
	{	
		if(gameState == GameState.INGAME) {
			switch(event.getKeyCode()) {
			case KeyEvent.VK_W: forward = true; break;
			case KeyEvent.VK_A: left = true;	break;
			case KeyEvent.VK_S: back = true;	break;
			case KeyEvent.VK_D: right = true;	break;
			}
		}
	}

	/**
	 * Detects keys released
	 */
	@Override
	public void keyReleased(KeyEvent event)
	{
		switch(event.getKeyCode()) {
		case KeyEvent.VK_W: forward = false; 	break;	
		case KeyEvent.VK_A: left = false;	 	break;
		case KeyEvent.VK_S: back = false;	 	break;	
		case KeyEvent.VK_D: right = false;	 	break;
		
		// pause/unpause
		case KeyEvent.VK_P: 
			if (gameState == GameState.INGAME) gameState = GameState.PAUSE;
			else if (gameState == GameState.PAUSE) gameState = GameState.INGAME;
			break;
		
		// to menu
		case KeyEvent.VK_ESCAPE: 	gameState = GameState.MENU;		break;}
	}
	

	/*
	 * **********************************************
	 * *			 Getters and setters			*
	 * **********************************************
	 */

	/**
	 * @return Returns true if mouse was clicked
	 */
	public boolean wasMousePressed() {
		return wasMousePressed;
	}
	
	/**
	 * resets the mouse to not clicked
	 */
	public void resetMousePressed() {
		wasMousePressed = false;
	}
	
	/**
	 * sets the new game variable
	 */
	public void setNewGame(boolean newGame) {
		this.newGame = newGame;
	}
	
	/**
	 * checks if a new game has to be started
	 */
	public boolean isNewGame() {
		return newGame;
	}
	
	/**
	 * @return the current gameState
	 */
	public GameState getGameState() {
		return gameState;
	}
	
	/**
	 * set the current gameState
	 */
	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}
	
	/**
	 * set the screen Dimensions
	 */
	public void setScreenSize(Dimension screenSize) {
		this.screenSize = screenSize;
	}
	
	/**
	 * set the canvas Dimensions
	 * @param canvasSize
	 */
	public void setCanvasSize(Dimension canvasSize) {
		this.canvasSize = canvasSize;
	}

	/**
	 * set the screen center (in screen coordinates)
	 */
	public void setScreenCenter(Point screenCenter) {
		this.screenCenter = screenCenter;
	}
	
	/**
	 * move the mouse to the center of the screen
	 */
	public void centerMouse() {
	    robot.mouseMove(screenCenter.x, screenCenter.y);
	}
	
	/**
	 * reset mouse location to the center of the screen (in canvas coordinates)
	 */
	public void resetMouseLocation() {
		mouseLocation = new Point (screenSize.width/2, screenSize.height/2);
	}
	
	/*
	 * **********************************************
	 * *		Unused event handlers				*
	 * **********************************************
	 */
	
	@Override
	public void mouseDragged(MouseEvent event)
	{	
		
	}
	
	@Override
	public void keyTyped(KeyEvent event)
	{
		
	}

	@Override
	public void mouseClicked(MouseEvent event)
	{
	}

	@Override
	public void mouseEntered(MouseEvent event)
	{
	}

	@Override
	public void mouseExited(MouseEvent event)
	{
	}

	@Override
	public void mouseReleased(MouseEvent event)
	{
	}
}
