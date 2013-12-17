package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3f;

import mazerunner.Maze;

// Need to use GL VBO for better performance
public class Model {
	protected List<Vector3f> vertices = new ArrayList<Vector3f>();
	protected List<Vector3f> normals = new ArrayList<Vector3f>();
	protected List<Face> faces = new ArrayList<Face>();
	private float scale = 0;
	
	private boolean loaded = false;
	
	public Model(){}
	
	public Model(String file,float scale){
		this.scale = scale;
		this.load(file);
	}
	
	public void addVertice(Vector3f add){
		vertices.add(add);
	}
	public void addNormal(Vector3f add){
		normals.add(add);
	}
	public void addFace(Face add){
		faces.add(add);
	}
	
	
	public boolean isLoaded(){return loaded;}
	
	public void load(String file){
		try{
			Model temp = OBJLoader.loadModel(new File(file));
			vertices = temp.vertices;
			// Determine the height of the model
			float min=0,max=0;
			for(Vector3f vec3 : vertices){
				if(min > vec3.y)
					min = vec3.y;
				else if(max < vec3.y)
					max = vec3.y;
			}
			// rescale the model
			scale *= Maze.SQUARE_SIZE/(max-min);
			for(Vector3f vec3 : vertices)
				vec3.scale(scale);
			
			normals = temp.normals;
			faces = temp.faces;
			loaded = true;
		}catch(FileNotFoundException e){
			loaded = false;
			System.err.println("Model: File not found!");
		}catch(IOException e){
			loaded = false;
			System.out.println("Model: Read error!");
		}
	}
	
}
