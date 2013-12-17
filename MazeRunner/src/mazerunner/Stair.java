package mazerunner;

import javax.media.opengl.GL;

import model.Model;
import model.TexturedModel;

public class Stair implements VisibleObject{
	private double lowerX,lowerY,lowerZ;
	private double upperX,upperZ;
	private TexturedModel texturedModel;
	private double angle;
	
	
	public Stair(GL gl,double level,double lowerX,double lowerZ,double upperX,double upperZ,String modelLocation){
		this.lowerX = lowerX;
		this.lowerY = level;
		this.lowerZ = lowerZ;
		
		this.upperX = upperX;
		this.upperZ = upperZ;
		this.texturedModel = new TexturedModel(gl,new Model(modelLocation,1f));
		
		this.angle = 180; // North 0
		if(lowerZ > upperZ)
			angle -= 180;
		if(lowerX > upperX)
			angle -= 90;
		else if(lowerX < upperX)
			angle += 90;
	}
	
	
	
	public double getLowerX(){return lowerX;}
	public double getLowerY(){return lowerY;}
	public double getLowerZ(){return lowerZ;}
	
	
	public double getUpperX(){return upperX;}
	public double getUpperY(){return lowerY+1;}
	public double getUpperZ(){return upperZ;}
	
	
	
	
	public void display(){
		
		texturedModel.render(angle, lowerX, lowerY, lowerZ);
	}
}
