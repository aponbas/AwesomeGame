package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.vecmath.Vector3f;


public class TexturedModel {
	// private int texture;
	private int shaderProgramHandle;
	private int vboVertexHandle[] = new int[1];
	private int vboNormalHandle[] = new int [1];
	private int vertexShaderHandle;
	private int fragmentShaderHandle;
	private int diffuseModifierUniform;
	
	private  Model m;
	private GL gl;
	
	public TexturedModel(GL gl,Model model){
		this.gl = gl;
		this.m = model;
		this.setUpVBOs();
		this.setupShaders();
	}
	
	protected void finalize() throws Throwable{
		try{
			this.cleanUp();
		}finally{
			super.finalize();
		}
	}
	
	public void setUpVBOs(){
		gl.glGenBuffers(1,vboVertexHandle,0);
		gl.glGenBuffers(1,vboNormalHandle,0);
		
		if(!m.isLoaded()) // draw nothing if there is no model loaded, so stop init VBO
			return;
		
		int sizeOfBuffers = m.faces.size() *36; // 3*3*4
		FloatBuffer vertices = reserveData(sizeOfBuffers); // allocate memory for buffer
		FloatBuffer normals = reserveData(sizeOfBuffers); // allocate memory for buffer
		for(Face face : m.faces){
			vertices.put(asFloats(m.vertices.get((int) face.vertex.x-1)));
			vertices.put(asFloats(m.vertices.get((int) face.vertex.y-1)));
			vertices.put(asFloats(m.vertices.get((int) face.vertex.z-1)));
			
			normals.put(asFloats(m.normals.get((int) face.normals.x-1)));
			normals.put(asFloats(m.normals.get((int) face.normals.y-1)));
			normals.put(asFloats(m.normals.get((int) face.normals.z-1)));
		}
		// make the buffers readable for GL
		vertices.flip();
		normals.flip();
		
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboVertexHandle[0]);
		gl.glBufferData(GL.GL_ARRAY_BUFFER,sizeOfBuffers,vertices, GL.GL_STATIC_DRAW);
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER,vboNormalHandle[0]);
		gl.glBufferData(GL.GL_ARRAY_BUFFER,sizeOfBuffers,normals, GL.GL_STATIC_DRAW);
		
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0); // unbind the buffer
	}
	
	public void setupShaders(){
		shaderProgramHandle = gl.glCreateProgram();
		vertexShaderHandle = gl.glCreateShader(GL.GL_VERTEX_SHADER);
		fragmentShaderHandle = gl.glCreateShader(GL.GL_FRAGMENT_SHADER);
		
		String vertexShaderSource = new String();
		String fragmentShaderSource = new String();
		
		try{
			BufferedReader reader = new BufferedReader(new FileReader("src/model/shader.vert"));
			String line;
			
			while((line = reader.readLine()) != null){
				vertexShaderSource += line;
				vertexShaderSource += "\n";
			}
			reader.close();
			
		}catch(IOException e){
			System.err.println("Vertex shader wassn't loaded properly.");
			return;
		}
		
		try{
			BufferedReader reader = new BufferedReader(new FileReader("src/model/shader.frag"));
			String line;
			
			while((line = reader.readLine()) != null){
				fragmentShaderSource += line;
				fragmentShaderSource += "\n";
			}
			reader.close();
			
		}catch(IOException e){
			System.err.println("Fragment shader wassn't loaded properly.");
			return;
		}
		IntBuffer temp = IntBuffer.allocate(1);
		gl.glShaderSource(vertexShaderHandle, 1, new String[]{vertexShaderSource},null);
		gl.glCompileShader(vertexShaderHandle);
		
		gl.glGetShaderiv(vertexShaderHandle, GL.GL_COMPILE_STATUS, temp);
		if(temp.get(0) == GL.GL_FALSE) {
			System.err.println("Vertex shader wasn't able to be compiled corectly.");
			return;}
		
		gl.glShaderSource(fragmentShaderHandle,1,new String[]{fragmentShaderSource},null);
		gl.glCompileShader(fragmentShaderHandle);
		
		gl.glGetShaderiv(fragmentShaderHandle, GL.GL_COMPILE_STATUS, temp);
		if(temp.get(0) == GL.GL_FALSE) {
			System.err.println("Fragment shader wasn't able to be compiled corectly.");
			return;}
		
		gl.glAttachShader(shaderProgramHandle, vertexShaderHandle);
		gl.glAttachShader(shaderProgramHandle, fragmentShaderHandle);
		
		gl.glLinkProgram(shaderProgramHandle);
		gl.glValidateProgram(shaderProgramHandle);
		
		diffuseModifierUniform = gl.glGetUniformLocation(shaderProgramHandle, "diffuseIntensityModifier");
		
		setUpLighting();
	}
	
	public void cleanUp(){
		gl.glDeleteProgram(shaderProgramHandle);
		
		gl.glDeleteBuffers(1,vboVertexHandle,0);
		gl.glDeleteBuffers(1,vboNormalHandle,0);
		
		gl.glDeleteShader(vertexShaderHandle);
		gl.glDeleteShader(fragmentShaderHandle);
	}
	
	private void setUpLighting(){
		gl.glShadeModel(GL.GL_SMOOTH);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0);
		gl.glLightModelfv(GL.GL_LIGHT_MODEL_AMBIENT, asFloatBuffer(new float[]{0.5f,0.5f,0.05f,1f}));
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, asFloatBuffer(new float[]{0,0,0,1f}));
		gl.glEnable(GL.GL_CULL_FACE);
		gl.glCullFace(GL.GL_BACK);
		gl.glEnable(GL.GL_COLOR_MATERIAL);
		gl.glColorMaterial(GL.GL_FRONT, GL.GL_DIFFUSE);
	}
	
	public void render(double angle,double x,double y,double z){
		if(!m.isLoaded()) // there is nothing to render
			return;
		gl.glPushMatrix();
		
			gl.glTranslated(x, y, z);
			gl.glRotated(angle, 0, 1, 0);
			gl.glUseProgram(shaderProgramHandle);
			gl.glUniform1f(diffuseModifierUniform, 1.0f);
			gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, 10.0f);
			
			gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboVertexHandle[0]);
			gl.glVertexPointer(3, GL.GL_FLOAT, 0, 0L);
			
			gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboNormalHandle[0]);
			gl.glNormalPointer(GL.GL_FLOAT, 0, 0L);
			
			gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
			gl.glEnableClientState(GL.GL_NORMAL_ARRAY);
		
			gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, 10f);
			gl.glDrawArrays(GL.GL_TRIANGLES, 0, m.faces.size()*3);
			
			
			gl.glDisableClientState(GL.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL.GL_NORMAL_ARRAY);
			gl.glBindBuffer(GL.GL_ARRAY_BUFFER,0); // unbind the buffer
			gl.glUseProgram(0); // unbind the program
			
		gl.glPopMatrix();
		
	}
	
	
	private float[] asFloats(Vector3f v){
		return new float[]{v.x,v.y,v.z};
	}
	
	private FloatBuffer asFloatBuffer(float... values){
		FloatBuffer res = reserveData(values.length*4); // 4 bytes per float
		res.put(values);
		res.flip();
		return res;
	}
	
	
	private FloatBuffer reserveData(int size){
		ByteBuffer byteBuff = ByteBuffer.allocateDirect(size); // allocate memory
		byteBuff.order(ByteOrder.nativeOrder()); // use the device hardware's native byte order
		FloatBuffer data = byteBuff.asFloatBuffer(); // create a floating point buffer from the ByteBuffer
		return data;
	}
	
	
}
