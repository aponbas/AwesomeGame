package Editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

public class Editor extends JFrame implements GLEventListener, MouseListener, MouseMotionListener, ActionListener {

	/**
	 * This is the LevelEditor for AwesomeGame. With this editor mazes consisting of 1 to 12 levels can be
	 * designed.
	 */
	private static final long serialVersionUID = -1698109322093496405L;
	//Graphic related variables
	private GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	private int screenWidth = gd.getDisplayMode().getWidth();
	private int screenHeight =  gd.getDisplayMode().getHeight();
	private GLCanvas canvas;
	
	//Maze related variables
	private int mazeX = 20;						//number of X-squares, 10 entered for testing
	private int nlevels = 6;									//number of levels in the maze
	private int buttonRow = 10;
	private float mazeL = ((screenWidth-screenHeight)/3*2);					//Left bound of mazeDrawingWindow
	private float mazeR = screenWidth-((screenWidth-screenHeight)/3);		//Right bound of mazeDrawingWindow
	
	//creates the initial levels with walls on the borders
	private Level level = new Level(mazeX,mazeX);
	private Level[] levels = new Level[nlevels];

	//Button related
	private Button btn[];
	private Button btnr[];	
	
	//New Maze Menu variables
	private JTextField size = new JTextField();
	private JTextField nlev = new JTextField();
	private JFrame frame = new JFrame("Create New Maze");
	private JButton newmaze = new JButton("Create New Maze");
	private JPanel paneln = new JPanel(); //the north panel
	private JLabel sizel = new JLabel("Level size(3-63): ");
	private JLabel nlevl = new JLabel("Number of levels (1-12): ");
	private JFileChooser chooser = new JFileChooser();
    File file = new File("mazes\\test.maze");
    FileNameExtensionFilter filter = new FileNameExtensionFilter(".maze files", "maze");
    int returnVal;
	
	private Texture[] textureLeft;
	private Texture[] textureRight;
	private Texture[] textureMaze;
		
	public Editor() {
		super("Level Editor Beta v1.0");
		setSize(screenWidth, screenHeight);
		GLCapabilities caps = new GLCapabilities();
		caps.setDoubleBuffered(true);
		caps.setHardwareAccelerated(true);
		canvas = new GLCanvas(caps);
		add(canvas);
		canvas.addGLEventListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		Animator anim = new Animator(canvas);
		anim.start();
		setUndecorated(true);
		setResizable(false);
		setVisible(true);
	}

	public static void main(String[] args){
		new Editor();
		System.out.println("Level editor started\nGenerating maze...\n");
	}

	public void drawLevel(GL gl){
		level.draw(gl, mazeL, 0, mazeR-mazeL, screenHeight);
	   	if (mazeX > 19){
	   		level.lineWidth = 1;
	   	}
	}
	
	//Button definition
	public void Buttons(GL gl){
		
		for (int i = 0; i < buttonRow*3; i++){
			btn[i].draw(gl);
		}
		
		//left row of buttons
		for (int i = 0; i < buttonRow; i++){
			btnr[i].draw(gl);
		}	
	}
	
	public int getButton(int x,int y){
		int i = -1;
		for(int it = 0;it<buttonRow*3;it++){
			if(btn[it].getX() < x && x < btn[it].getSizex()+btn[it].getX() && btn[it].getY() < y &&
					y < btn[it].getSizey()+btn[it].getY()){
				i = it;
				break;
			}
		}
		return i;
	}
	
	public int getButtonR(int x,int y){
		int i = -1;
		for(int it = 0; it<buttonRow;it++){
			if(btnr[it].getX() < x && x < btnr[it].getSizex()+btnr[it].getX() && btnr[it].getY() < y &&
					y < btnr[it].getSizey()+btnr[it].getY()){
				i = it;
				break;
			}
		}
		return i;
	}
	
	@Override
	public void display(GLAutoDrawable drawable) {

		GL gl = drawable.getGL();

		// Set the clear color and clear the screen.
		gl.glClearColor(255/255f, 238/255f, 131/255f, 1);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		
		// Draw the buttons.
		Buttons(gl);
		
		// Draw the level
		drawLevel(gl);

		// Flush the OpenGL buffer, outputting the result to the screen.
		gl.glFlush();
	}

	@Override
	public void init(GLAutoDrawable drawable) {

		//Retrieve the OpenGL handle, this allows us to use OpenGL calls.
		GL gl = drawable.getGL();

		// Set the matrix mode to GL_PROJECTION, allowing us to manipulate the
		// projection matrix
		gl.glMatrixMode(GL.GL_PROJECTION);

		// Always reset the matrix before performing transformations, otherwise
		// those transformations will stack with previous transformations!
		gl.glLoadIdentity();

		/*
		* glOrtho performs an "orthogonal projection" transformation on the
		* active matrix. In this case, a simple 2D projection is performed,
		* matching the viewing frustum to the screen size.
		*/
		
		gl.glOrtho(0, screenWidth, 0, screenHeight, -1, 1);

		// Set the matrix mode to GL_MODELVIEW, allowing us to manipulate the
		// model-view matrix.
		gl.glMatrixMode(GL.GL_MODELVIEW);

		// We leave the model view matrix as the identity matrix. As a result,
		// we view the world 'looking forward' from the origin.
		gl.glLoadIdentity();

		// We have a simple 2D application, so we do not need to check for depth
		// when rendering.
		gl.glDisable(GL.GL_DEPTH_TEST);	
		
		//print the level for reference, should be turned off eventually
		System.out.println(level.toString());
		level.primes();
//		boolean x = level.check(level.primeFactors(level.level[0][0]),2);
//		System.out.println(x);
		
		//Creating the left buttonmenu
		//Vierkante knoppen?
		float buttonsizex = mazeL/4;
    	float buttonsizey = screenHeight/(buttonRow+1);
    	float spacingy = buttonsizey/(buttonRow);
    	float spacingx = buttonsizex/4;
    	
		btn = new Button[buttonRow*3];
		btnr = new Button[buttonRow];
		textureLeft = new Texture[buttonRow*3];
		textureRight = new Texture[buttonRow];
		
		
		//Loading all the textures
		try {
			textureLeft[1] = TextureIO.newTexture(new File("img\\StairsL.png"), false);
			textureLeft[2] = TextureIO.newTexture(new File("img\\StairsH.png"), false);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		//Colors for the buttons on the left
		float colors[][] = new float[4][buttonRow*3];
		
		//color of the other buttons (temporary)
		for (int i = 0; i < 3; i++){
			for (int j = 2; j < 30; j++){
				colors[i][j] = /*(float) Math.random();*/0.5f;
				colors[3][j] = 1.0f;
			}
		}
		
		//color of the walldraw button0
		colors[0][0] = 87/255f; colors[1][0] = 84/255f; colors[2][0] = 83/255f; colors[3][0] = 1.0f;
		
		//background color of the stairsL button1
		colors[0][1] = 255/255f; colors[1][1] = 255/255f; colors[2][1] = 255/255f; colors[3][1] = 1.0f;
		
		//background color of the stairsH button2
		colors[0][2] = 255/255f; colors[1][2] = 255/255f; colors[2][2] = 255/255f; colors[3][2] = 1.0f;
		
		//color of the void button27
		colors[0][27] = 0/255f; colors[1][27] = 0/255f; colors[2][27] = 0/255f; colors[3][27] = 1.0f;
		
		//Texts for the buttons on the left
		String text[] = new String[30];
		for (int i = 0; i < 30; i++){
			text[i] = "TEMPORARY";
			//empty text for these buttons
			if (i == 1 || i == 2){
				text[i] = "";
			}
		}
		text[0] = "Wall"; text[3] = "Door"; text[4] = "Chest"; text[5] = "Food";
		text[6] = "TorchN"; text[7] = "TorchE"; text[8] = "TorchS"; text[9] = "TorchW"; 
		text[25] = "LtoString"; text[26] = "Player"; text[27] = "Void"; text[28] = "Clear"; text[29] = "ClearAll";
		
		//Create the buttons on the left
	   	int index = 0;
	   	for(int row = 0;row<buttonRow;row++){
	   		for(int col = 0;col<3;col++){
	   			btn[index] = new Button(gl, spacingx+col*spacingx+col*buttonsizex, screenHeight - (row+1)*(spacingy+buttonsizey), 
	   					buttonsizex, buttonsizey, colors[0][index], colors[1][index], colors[2][index], colors[3][index], text[index]);
	   			index++;
	   		 }
	   	 }
	   	
	   	//If present, set textures on the buttons on the left
	   	btn[1].setTexture(textureLeft[1]);
	   	btn[2].setTexture(textureLeft[2]);
	   	
	   	//Texts for the buttons on the right
	   	String textr[] = new String[10];
	   	for (int i = 3; i < 9; i++){
	   		textr[i] = "Level " + (i-2);
	   	}
	   	textr[0] = "New Maze"; textr[1] = "Save Maze"; textr[2] = "Load Maze"; textr[9] = "Exit";
	   	
	   	//Create the buttons on the right
	   	int indexr = 0;
	   	for (int i = 0; i < buttonRow; i++){
	   		btnr[indexr] = new Button(gl, mazeR+spacingx, screenHeight - (spacingy+spacingy*(i)+(i+1)*buttonsizey), 
	   				(screenWidth-mazeR)-2*spacingx, buttonsizey, 0.5f, 0.5f, 0.5f, 1.0f, textr[indexr]);
	   		indexr++;
	   	}
	   	
	   	for (int k = 0; k < nlevels; k++){
	   		levels[k] = new Level(mazeX,mazeX);
	   	}
	   	level = levels[0];
	   	//set level button default to level 1
	   	btnr[3].setSelected(true);
	   	//set default to walldraw
	   	btn[0].setSelected(true);
	}
	
	@Override
	public void mouseReleased(MouseEvent me) {
		
		//System.out.println(me.getX() + " " + me.getY());		//for development to see where the mouse is released
		/*
		 * The 30 buttons on the left side are defined here below
		 * First we determine which button is selected
		 */
		int i;
		i = getButton(me.getX(),screenHeight-me.getY());
		//set selected button to true
		if(i>=0){
			btn[i].setSelected(true);
			//set selected for other buttons to false
			for(int j = 0; j < buttonRow*3; j++){
				if (j != i){
					btn[j].setSelected(false);
				}
			}
		}
		
		/*
		 * Now we will define the functionality of the individual buttons if they use the 
		 * mouseReleased function
		 */	
		
        //print level to console
        if (i == 25){
            System.out.println(level.toString());
            btn[25].setSelected(false);
        }
        
		
		//Clear level button
		if (i == 28){
			for(int j = 3; j < buttonRow-1; j++){
				if (btnr[j].selected){
					levels[j-3] = new Level(mazeX,mazeX);
					break;
				}
			}
			//select the walldraw as default and de-select the current button
			btn[28].setSelected(false);
			btn[0].setSelected(true);
		}
		
		//Clear all button
		if (i == 29){
			for(int j = 3; j < buttonRow-1; j++){
				levels[j-3] = new Level(mazeX,mazeX);
			}
			//select the walldraw as default and de-select the current button
			btn[29].setSelected(false);
			btn[0].setSelected(true);
		}
	
		
		/*
		 * The 10 buttons on the right side are defined here below
		 * First we determine which button is selected
		 */
		int k;
		k = getButtonR(me.getX(),screenHeight-me.getY());
		//DEV: System.out.println(k);
		//DEV: System.out.println("Dit is knop "+ k + " aan de rechter kant");

		//set 1 selected button to true for the level buttons
		if(k >= 3 && k < nlevels+3){
			btnr[k].setSelected(true);
			//set selected for other buttons to false
			for(int j = 3; j < buttonRow-1; j++){
				if (j != k){
					btnr[j].setSelected(false);
				}
			}
		}
		
		//set 1 selected button to true for the new, load and save buttons
		if(k >= 0 && k < 3){
	   		btnr[k].setSelected(true);
	   		for(int j = 0; j<3; j++){
	   			if (j!=k){
	   				btnr[j].setSelected(false);
	   			}
	   		}
	   	}
		
		if (k == 0){
			toFront();
			frame.setAlwaysOnTop(true);
		    frame.setSize(250, 120);
		    frame.setVisible(true);
		    frame.setResizable(false);
		    frame.toFront();
		    
		    newmaze.addActionListener(this);
		    
		    size.setPreferredSize(new Dimension(30,20));
		    nlev.setPreferredSize(new Dimension(30,20));
		    
		    paneln.setBackground(Color.WHITE);
		    paneln.add(sizel);
		    paneln.add(size);
		    paneln.add(nlevl);
		    paneln.add(nlev);
		    paneln.add(newmaze);
		    //add the panel to the frame
		    frame.add(paneln);
		    
			btnr[0].setSelected(false);
			frame.toFront();
		}
		
		//Saving a file
        if (k == 1){
        	toFront();
            System.out.println("Starting Save");
            chooser.setFileFilter(filter);
            chooser.setCurrentDirectory(file);
            returnVal = chooser.showSaveDialog(Editor.this);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                file = chooser.getSelectedFile();
                String path = file.getAbsolutePath();
                if(!path.endsWith(".maze")){
                	file = new File(path + ".maze");
                }
                System.out.println(chooser.getSelectedFile().getName() + " saved.");
            }
                        
            try{
                FileOutputStream fmaze = new FileOutputStream(file);
                ObjectOutputStream omaze = new ObjectOutputStream(fmaze);
                omaze.writeObject(nlevels);
                omaze.writeObject(mazeX);
                for (int n = 0; n < nlevels; n++){
                    int[][] writeLevel = levels[n].getLevel();
                    omaze.writeObject(writeLevel);
                }
                omaze.flush();
                omaze.close();
         
               }catch(Exception ex){
                   ex.printStackTrace();
               }
            
            System.out.println("Save Completed!");
            btnr[1].setSelected(false);
        }
        
        //Loading a file
        if (k == 2){
        	toFront();
            System.out.println("Start Loading...");
            chooser.setFileFilter(filter);
            chooser.setCurrentDirectory(file);
            returnVal = chooser.showOpenDialog(Editor.this);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                file = chooser.getSelectedFile();                
                System.out.println(chooser.getSelectedFile().getName() + " loaded.");
            }
                
            try{     
                FileInputStream fmaze = new FileInputStream(file);
                ObjectInputStream omaze = new ObjectInputStream(fmaze);
                
                nlevels = (int) omaze.readObject();
                mazeX = (int) omaze.readObject();
                
                
                int[][] firstLevel = (int[][]) omaze.readObject();
                int x = firstLevel.length;
                
                mazeX = x;
            
                level = new Level(mazeX,mazeX);
                levels = new Level[nlevels];
                
                   for (int b = 0; b < nlevels; b++){
                       levels[b] = new Level(mazeX,mazeX);
                   }
                   
                   level = levels[0];
                   btnr[3].setSelected(true);
                   for (int c = 4; c < 9; c++){
                       btnr[c].setSelected(false);
                   }
                
                level.setX(x);
                level.setY(x);
                
                int n = 0;
                
                int[][] nextLevel;
                levels[n].setLevel(firstLevel);
                for ( n=1; n<nlevels; n++){
                    nextLevel = (int[][]) omaze.readObject();
                    if (nextLevel != null) {
                        levels[n].setLevel(nextLevel);
                    }
                    else break;
                }        
                omaze.close();    
            }
                
            catch(Exception ex){
                ex.printStackTrace();
            }
            
            System.out.println("De grootte van de levels is " + mazeX + " en de maze bevat " + nlevels + " levels.");
            System.out.println("Loading Completed!");
            btnr[2].setSelected(false);
            
        }

		//The Exit button on the bottom-right
		if(k == 9){
			System.out.println(level.toString());
			System.exit(0);
		}
		
		//Check which level-button is selected
		for (k = 3; k < nlevels+3; k++){
			if (btnr[k].selected){
				level = levels[k-3];
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent me) {
		double squareX = Math.floor( ( (me.getX() - (mazeL)) / level.buttonSize));
		double squareY = Math.floor(((me.getY())/level.buttonSize));
		int X = (int) squareX;
		int Y = mazeX - (int) squareY;
		
		//The wall draw button
		if (btn[0].selected == true && squareX >= 0 && squareX < mazeX && squareY < mazeX && squareY >= 0){
			//System.out.println("Hier kan getekent worden");
			level.level[X][Y-1] = 1;
		}
		
		//Stair Low Part draw button
		if (btn[1].selected == true && squareX >= 0 && squareX < mazeX && squareY < mazeX && squareY >= 0){
			level.level[X][Y-1] = 11;
		}
		
		//Stair High Part draw button
		if (btn[2].selected == true && squareX >= 0 && squareX < mazeX && squareY < mazeX && squareY >= 0){
			level.level[X][Y-1] = 13;
		}
		
		//TorchN draw button
		if (btn[6].selected == true && squareX >= 0 && squareX < mazeX && squareY < mazeX && squareY >= 0 && level.level[X][Y-1] != 97 && level.check(X,Y-1,2) == false){
			if(level.level[X][Y-1] == 0){
			    level.level[X][Y-1] = 2;
			}
			else{
			    level.level[X][Y-1] *= 2;
			}
		}
		
		//TorchE draw button
		if (btn[7].selected == true && squareX >= 0 && squareX < mazeX && squareY < mazeX && squareY >= 0 && level.level[X][Y-1] != 97 && level.check(X,Y-1,3) == false){
			if(level.level[X][Y-1] == 0){
			    level.level[X][Y-1] = 3;
			}
			else{
			    level.level[X][Y-1] *= 3;
			}
		}
		
		//TorchS draw button
		if (btn[8].selected == true && squareX >= 0 && squareX < mazeX && squareY < mazeX && squareY >= 0 && level.level[X][Y-1] != 97 && level.check(X,Y-1,5) == false){
			if(level.level[X][Y-1] == 0){
			    level.level[X][Y-1] = 5;
			}
			else{
			    level.level[X][Y-1] *= 5;
			}
		}
		
		//TorchW draw button
		if (btn[9].selected == true && squareX >= 0 && squareX < mazeX && squareY < mazeX && squareY >= 0 && 
				level.level[X][Y-1] != 97 && level.check(X,Y-1,7) == false){
			if(level.level[X][Y-1] == 0){
			    level.level[X][Y-1] = 7;
			}
			else{
			    level.level[X][Y-1] *= 7;
			}
		}
		
        //Player Spawn button
        if (SwingUtilities.isLeftMouseButton(me) && btn[26].selected == true && squareX >= 0 && squareX < mazeX && 
        		squareY < mazeX && squareY >= 0 && level.level[X][Y-1] != 97){
            for(int a = 0; a < mazeX; a++){
                for(int b = 0; b < mazeX; b++){
                	for (int c = 0; c < nlevels; c++){
	                    if (levels[c].level[a][b] == 97){
	                        levels[c].level[a][b] = 0;
	                    }
                	}
                }
            }
            level.level[X][Y-1] = 97;
        }
		
		//The Void draw button
		if (btn[27].selected == true && level != levels[0] && squareX >= 0 && squareX < mazeX && squareY < mazeX && 
				squareY >= 0 && level.level[X][Y-1] != 97){
			level.level[X][Y-1] = 17;
		}
		
		//The right mouse button always draws an empty floor tile
		if(SwingUtilities.isRightMouseButton(me)){
			try{
				level.level[X][Y-1] = 0;
			}
			catch (ArrayIndexOutOfBoundsException ex){
				//System.err.println("you are drawing out of bounds");
			}
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent me){
		//The wall and floor draw buttons can be dragged for easier drawing
		if(btn[0].selected || SwingUtilities.isRightMouseButton(me) || btn[27].selected){
			mousePressed(me);
		}
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Button Pressed");
        
        mazeX = 0;
        nlevels = 0;
        
        try{nlevels = Integer.parseInt(nlev.getText());}
        catch (NumberFormatException ex){System.out.println("One or more invalid numbers were entered");}
        
        try{mazeX = Integer.parseInt(size.getText());}
        catch (NumberFormatException ex){System.out.println("One or more invalid numbers were entered");}
        
        if (mazeX < 3 || mazeX > 63){
            mazeX = 63;
        }
        
        if (nlevels < 1 || nlevels > 6){
            nlevels = 6;
        }
        
        level = new Level(mazeX,mazeX);
        levels = new Level[nlevels];
        
           for (int k = 0; k < nlevels; k++){
               levels[k] = new Level(mazeX,mazeX);
           }
           
           level = levels[0];
           btnr[3].setSelected(true);
           for (int i = 4; i < 9; i++){
               btnr[i].setSelected(false);
           }
        
           frame.dispose();


	}

	@Override
	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {/* NOT USED */}
	@Override
	public void mouseMoved(MouseEvent arg0) {/*NOT USED*/}
	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {/*NOT USED*/}
	@Override
	public void mouseClicked(MouseEvent me) {/*NOT USED*/}
	@Override
	public void mouseEntered(MouseEvent arg0) {/*NOT USED*/}
	@Override
	public void mouseExited(MouseEvent arg0) {/*NOT USED*/}

	public Button[] getBtn() {
		return btn;
	}
}
