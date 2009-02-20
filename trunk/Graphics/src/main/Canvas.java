package main;
import geometry.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Vector;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
//import javax.media.opengl.GLJPanel;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

import drawables.*;

import javax.media.opengl.GLContext;

import scene.Scene;
import animations.*;
import collidables.*;

public class Canvas extends GLCanvas implements GLEventListener, KeyListener, MouseListener, MouseMotionListener
{
	GLContext glcontext;
	GLU glu;
	GL gl;
	
	
	Animator animator;//calls display repeatedly in new thread
	final double PI = Math.PI;
	final double MOUSE_SENSITIVITY = .03;
	
	Scene scene = new Scene();
	
	//loaded textures are stored here
	HashMap <String, Texture> textures = new HashMap <String, Texture>();
	
	
	Canvas()
	{
		addGLEventListener(this);
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);

		//creates new thread and calls display repeatedly
		animator = new Animator(this);
		animator.start();
	}
	
	public void init (GLAutoDrawable drawable)
	{
		//set these now that the context is available
		glu = new GLU();
		gl = drawable.getGL();
		
		//set up display lists
		Deer.init();
		SpinnyCubez.init();
		
		//init Projection
		scene.init();
		

		//System.out.println("wtf");
		//Deer d = new Deer(new Vector3d(-400,0,0), 100, Deer.BUCK);
		//d.movement.velocity = new Vector3d(20,0,0);
		//scene.addItem(d);
		//scene.addAnimation(new Move(d, new Vector3d(0,0,-30), new Vector3d(0,0,2), 500));
		//scene.addAnimation(new Rotate(d, new Vector3d(-3,-3,-3), new Vector3d(.1,.2,.3), 500));
		
		//d = new Deer(new Vector3d(400,0,0), 100, Deer.BUCK);
		//d.movement.velocity = new Vector3d(-20,0,0);
		//scene.addItem(d);
		//scene.addAnimation(new Rotate(d, new Vector3d(-3,-3,-3), new Vector3d(.1,.2,.3), 500));
		
		SpinnyCubez sc = new SpinnyCubez(scene);
		scene.addItem(sc);
		
		/*
		d = new Deer(new Vector3d(), 100, Deer.MALE_YEARLING);
		scene.addItem((Collidable)d);
		//scene.addAnimation(new Move(d, new Vector3d(5,10,-20), new Vector3d(0,0,2), 1500));
		scene.addAnimation(new Rotate(d, new Vector3d(-3,-3,-3), new Vector3d(.1,.2,.3), 500));
		   
		d = new Deer(new Vector3d(), 100, Deer.MALE_YEARLING);
		scene.addItem(d);
		//scene.addAnimation(new Move(d, new Vector3d(-20,20,-10), new Vector3d(0,0,2), 350));
		scene.addAnimation(new Rotate(d, new Vector3d(-3,-3,-3), new Vector3d(.1,.2,.3), 500));
		*/
	}

	
	//Called by the drawable to initiate OpenGL rendering by the client.
	public void display(GLAutoDrawable drawable)
	{   
		//clear canvas from last draw
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
		scene.draw();

	}


	Texture loadTexture(String name, String fileName)
	{
		System.out.println("loading texture " + name);
		Texture tex = null;
		try {
			tex = TextureIO.newTexture( new File(fileName), true);
			tex.setTexParameteri( GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT );
			tex.setTexParameteri( GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT );
			tex.setTexParameteri( GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR );
			tex.setTexParameteri( GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR );
		}
		catch(Exception e) {
			System.out.println("Error loading texture " + fileName); 
			return null;
		}
		textures.put(name, tex);
		return tex;
	}
	
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged)
	{}
	
	public void reshape(GLAutoDrawable gLDrawable, int x, int y, int width, int height) 
	{}	
	
	//Event handlers

	public void keyPressed(KeyEvent ke)
	{

		switch (ke.getKeyCode())
		{
			case KeyEvent.VK_UP:
				break;
			case KeyEvent.VK_DOWN:
				break;
			case KeyEvent.VK_LEFT:
				break;
			case KeyEvent.VK_RIGHT:
				break;
		}
		switch (ke.getKeyChar())
		{
			case 'w':
				scene.eye.moveUp(10);
				break;
			case 's':
				scene.eye.moveDown(10);
				break;
			case 'd':
				scene.eye.moveRight(10);
				break;
			case 'a':
				scene.eye.moveLeft(10);
				break;
			case 'e':
				scene.eye.pos.z -= 10;
				break;
			case 'q':
				scene.eye.pos.z += 10;
				break;
			case 'o': //goto origin
				scene.eye.pos.clear();
				scene.eye.rot.clear();
				break;
			case 'b':
		}

	}

	int prevMouseX, prevMouseY;
	public void mouseDragged(MouseEvent me)
	{
		scene.eye.rot.x -= (me.getY() - prevMouseY) * MOUSE_SENSITIVITY;
		scene.eye.rot.y -= (me.getX() - prevMouseX) * MOUSE_SENSITIVITY;
		
		prevMouseX = me.getX();
		prevMouseY = me.getY();

	}
	
		
	public void mouseMoved(MouseEvent me)
	{

	}
	
	public void keyTyped(KeyEvent ke)
	{
		
	}
	public void keyReleased(KeyEvent ke)
	{
		
	}
	public void mousePressed(MouseEvent me)
	{
		prevMouseX = me.getX();
		prevMouseY = me.getY();
	}
	public void mouseReleased(MouseEvent me)
	{
		
	}
	public void mouseClicked(MouseEvent me)
	{
		
	}
	public void mouseExited(MouseEvent me)
	{
		
	}
	public void mouseEntered(MouseEvent me)
	{
		
	}
}