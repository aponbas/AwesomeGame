package Editor;

import java.io.File;

import javax.media.opengl.GL;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

public class Level {
	
	protected int[][] level;
	private int x;
	private int y;
	protected float buttonSize;
	protected int lineWidth = 2;
	private Texture[] textureMaze;
	private int nTiles = 98; //this is the number of different tiles currently present in the maze
	
    static int primeNumbers[] = new int[100];
    private int aantalobjecten = 100;
	
	//creates a level with borders at the edges of the matrix
	public Level(int x, int y){
		this.x = x;
		this.y = y;
		level = new int [x][y];
		for (int i = 0; i < x; i++){
			for(int j = 0; j < y; j++){
				level[i][j] = 0;	
				if(i == 0 || i == x-1 || j == 0 || j == y-1){
					level[i][j] = 1;
				}
			}
		}
	}
		
	//draws the level on the specified location
	public void draw(GL gl, float startx, float starty, float width, float height){
		textureMaze = new Texture[nTiles];
		//!!
		
		//Loading all the textures in the maze
		try {
			//textureMaze[0] = TextureIO.newTexture(new File("img\\Floor.png"), false); // do not use (yet) because of transparency issues
			//only implement if there is time: putting textures over the floor texture
			
			textureMaze[1] = TextureIO.newTexture(new File("img\\Wall.png"), false);
			textureMaze[2] = TextureIO.newTexture(new File("img\\TorchN.png"), false);
			textureMaze[3] = TextureIO.newTexture(new File("img\\TorchE.png"), false);
			textureMaze[5] = TextureIO.newTexture(new File("img\\TorchS.png"), false);
			textureMaze[11] = TextureIO.newTexture(new File("img\\StairsL.png"), false);
			textureMaze[7] = TextureIO.newTexture(new File("img\\TorchW.png"), false);
			textureMaze[13] = TextureIO.newTexture(new File("img\\StairsH.png"), false);
			textureMaze[97] = TextureIO.newTexture(new File("img\\Player.png"), false);
			//textureMaze[9] = TextureIO.newTexture(new File("img\\StairsH.png"), false);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		buttonSize = height/y;
		
		for(int i = 0; i < x; i++){
			for(int j = 0; j < y; j++){
				
				//Drawing the floor color
				//Also be sure to maintain the background color of the floor for specific textures, making it not white
				if ((check(i,j,0) && textureMaze[0] == null) || check(i,j,2) || check(i,j,3) || check(i,j,5)
						|| check(i,j,7) || check(i,j,97)){
					
					gl.glColor4f(150/255f, 73/255f, 37/255f, 1);
				}
				
 				//Drawing the floor color before a texture is applied
 				gl.glBegin(GL.GL_QUADS);
				gl.glVertex2f(startx+((width-x*buttonSize)/2)+i*buttonSize, (j+1)*buttonSize);
				gl.glVertex2f(startx+((width-x*buttonSize)/2)+(i+1)*buttonSize, (j+1)*buttonSize);
				gl.glVertex2f(startx+((width-x*buttonSize)/2)+(i+1)*buttonSize, (j)*buttonSize);
				gl.glVertex2f(startx+((width-x*buttonSize)/2)+i*buttonSize, (j)*buttonSize);
 				gl.glEnd();

				
				//Drawing the wall color
				if(check(i,j,1) && textureMaze[1] == null){
					gl.glColor4f(87/255f, 84/255f, 83/255f, 1);
				}
				//^is sneller dan textures^
				
				//Drawing the voids, setting tile to black
				if(check(i,j,17)){
					gl.glColor3f(0/255f, 0/255f, 0/255f);
				}
				
				//drawing textures if present
				for (int k = 1; k < nTiles; k++){
					if ((check(i,j,k))){
						if (textureMaze[k] != null) {
							gl.glEnable(GL.GL_BLEND);
							gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
							textureMaze[k].enable();
							textureMaze[k].bind();
							//White background color for normal texture view
							gl.glColor3f(255/255f, 255/255f, 255/255f);
						}
						

						int[] a = new int[2];	a[0] = 0; a[1] = 0;
						int[] b = new int[2];	b[0] = 1; b[1] = 0;
						int[] c = new int[2];	c[0] = 1; c[1] = 1;
						int[] d = new int[2];	d[0] = 0; d[1] = 1;
						
						//If the stairs are not facing from left to the right we need to adjust the direction of the textures
						
						//Start Drawing the stairs from right to left
						if ((check(i,j,11) && check(i-1,j,13)) || (check(i,j,13) && check(i+1,j,11))){	
							a[0] = 1; a[1] = 0;
							b[0] = 0; b[1] = 0;
							c[0] = 0; c[1] = 1;
							d[0] = 1; d[1] = 1;
						}
						
						//Start Drawing the stairs from top to bottom
						if ((check(i,j,11) && check(i,j-1,13)) || (check(i,j,13) && check(i,j+1,11))){	
							a[0] = 0; a[1] = 0;
							b[0] = 0; b[1] = 1;
							c[0] = 1; c[1] = 1;
							d[0] = 1; d[1] = 0;
						}
						
						//Start Drawing the stairs from bottom to top
						if ((check(i,j,11) && check(i,j+1,13)) || (check(i,j,13) && check(i,j-1,11))){	
							a[0] = 1; a[1] = 1;
							b[0] = 1; b[1] = 0;
							c[0] = 0; c[1] = 0;
							d[0] = 0; d[1] = 1;
						}
						
						//normal texture drawing:
						gl.glBegin(GL.GL_QUADS);
						gl.glTexCoord2f(a[0],a[1]);
						gl.glVertex2f(startx+((width-x*buttonSize)/2)+i*buttonSize, (j+1)*buttonSize);
						gl.glTexCoord2f(b[0],b[1]);
						gl.glVertex2f(startx+((width-x*buttonSize)/2)+(i+1)*buttonSize, (j+1)*buttonSize);
						gl.glTexCoord2f(c[0],c[1]);
						gl.glVertex2f(startx+((width-x*buttonSize)/2)+(i+1)*buttonSize, (j)*buttonSize);
						gl.glTexCoord2f(d[0],d[1]);
						gl.glVertex2f(startx+((width-x*buttonSize)/2)+i*buttonSize, (j)*buttonSize);
						gl.glEnd();
						
						if (textureMaze[k] != null) {
							textureMaze[k].disable();
						}
						
					}
				}
				//Drawing the voids, drawing a red cross
				if(check(i,j,17)){
					gl.glColor3f(255/255f, 0/255f, 0/255f);
					gl.glLineWidth(3);
					gl.glBegin(GL.GL_LINES);
					gl.glVertex2f(startx+((width-x*buttonSize)/2)+i*buttonSize, (j+1)*buttonSize);		
					gl.glVertex2f(startx+((width-x*buttonSize)/2)+(i+1)*buttonSize, (j)*buttonSize);
					gl.glVertex2f(startx+((width-x*buttonSize)/2)+(i+1)*buttonSize, (j+1)*buttonSize);
					gl.glVertex2f(startx+((width-x*buttonSize)/2)+i*buttonSize, (j)*buttonSize);
					gl.glEnd();
				}
			}
		}
		
		
		//Drawing the GRID on top of everything!
		//set the LineWidth and the line color
		gl.glLineWidth(lineWidth);
		gl.glColor3f(255/255f, 255/255f, 255/255f);
		
		//vertical lines of the grid
		for(int i = 0; i <= x; i++){
			gl.glBegin(GL.GL_LINES);
			gl.glVertex2f(startx+((width-x*buttonSize)/2)+i*buttonSize, 0);
			gl.glVertex2f(startx+((width-x*buttonSize)/2)+i*buttonSize, height);
			gl.glEnd();
		}
		
		//horizontal lines of the grid
		for(int i = 0; i <= y; i++){
			gl.glBegin(GL.GL_LINES);
			gl.glVertex2f(startx+((width-x*buttonSize)/2), i*buttonSize);
			gl.glVertex2f(startx+width-((width-x*buttonSize)/2), i*buttonSize);
			gl.glEnd();
		}
		//End of drawing the grid
	}
	
	public  String toString(){
		String res = new String();
		for (int j = 0; j < y; j++){
			for(int i = 0; i < x; i++){
				res = res + level[i][j] + " ";
			}
			res = res +"\n";
		}
		return res;
	}

	public void setLevel(int[][] level) {
		this.level = level;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int[][] getLevel() {
		return level;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void primes(){
		// Initialize array of the first 100 prime numbers
        int index = 0;
        //int product = 1;
        //index<VALUE change value for larger thingy
        while(index<100){
            for (int i = 2; i < 100*10; i++){
                boolean primeNum = true;
                for(int j=2; j<i; j++){
                    if (i%j==0){
                        primeNum = false;
                    }
                }
                if (primeNum){
                    primeNumbers[index] = i;
                    //System.out.println(i);
                    index++;
                    if(index==100){
                        break;
                    }
                }
            }
        }
	}
	
	boolean check(int i, int j, int value){
		int index = 0;
		int number = level[i][j];
		int objects[] = new int[10];
        // Input is larger than the maximum value, so the value given will miss factors and thus objects
        if(value>=Integer.MAX_VALUE){
            System.out.println("Error: Value is larger than Integer.MAX_VALUE.");
            objects[0] = -1;
            System.exit(0);
        }
        else if(number == 1){
        	objects[0] = 1;
        }
        else if(number == 0){
        	objects[0] = 0;
        }
        // Find all prime factors of input
        // p<VALUE VALUE is primeNumber array size, also change in primes()
        else for (int p = 0; p < 100; p++) {
        	
            //System.out.println(primeNumbers[i]);
            if (number % primeNumbers[p] == 0) {
                objects[index] = primeNumbers[p];
                index++;
                //System.out.println(primeNumbers[i]);
                number /= primeNumbers[p];
                p = p - 1;
            }
        }
        // If there are no prime factors, the input is prime
        if(index==0){
            objects[0] = number;
        }
        // If the remaining number is more than 1, not all prime factors are calculated
        if(number>1){
            System.out.println("Error: Not all prime factors are given, primeNumbers array too small.");
            objects[0] = -1;
            System.exit(0);
        }		
		
		for(int l = 0;l<objects.length;l++){
			if(objects[l] == value){
				return true;
			}
		}
		return false;
	}
}
