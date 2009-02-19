package edu.ohiou.vital_lab.sceneapi.examples.hyper_cube;

import edu.ohiou.vital_lab.sceneapi.*;
import edu.ohiou.vital_lab.sceneapi.basic.*;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main extends GenericRenderer implements Runnable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Thread main_thread;
	
	HyperCube h_cube;
	LightNode light0;

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		new Main();
	}

	public Main()
	{
		main_thread = new Thread( this );
		main_thread.run();
	}
	
	

	@Override public void run()
	{
		Frame frame = new Frame( "HyperCube" );
		frame.setSize( new Dimension( 640, 480 ) );
		frame.add( this );
		frame.addWindowListener( new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit( 0 );
			}
		} );
		frame.setVisible( true );
		//super.setClearColor(  new Color( 255, 255, 255 ) );
		
		try
		{
			while (true)
			{
				this.display();
				Thread.yield();
			}
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
	}
	
	
	private int light_disp_list;
	public void init( GLAutoDrawable g )
	{
		super.init(  g  );
		
		h_cube = new HyperCube( g );
		System.out.println( "Nodes in Hyper Cube = " + Integer.toString(  ANode.countNodes( h_cube ) ));
		light0 = new LightNode( GL.GL_LIGHT0 );
		
		light0.position[0]=0;
		
		Material m = new Material();
		
		GL gl = g.getGL();
		light_disp_list = gl.glGenLists( 1 );
		gl.glNewList( light_disp_list, gl.GL_COMPILE );
		light0.render(  gl  );
		m.apply( gl );
		gl.glEndList();
		
		//h_cube.addChild(  light0  );
		//h_cube.makeRCubez(  5 );
	}
	
	
	private float r = 0;
	private int h_list = -1;
	public void display(GLAutoDrawable gLAutoDrawable)
	{
		super.display( gLAutoDrawable );
		GL gl = gLAutoDrawable.getGL();
		gl.glCallList(  light_disp_list );
		gl.glTranslatef( 0.0f, 0, -3.0f );
		gl.glRotatef( r, 1, 1, 1 );
		
		/*
		if( h_list == -1 ) 
		{
			h_list = gl.glGenLists(  1  );
			gl.glNewList(  h_list, gl.GL_COMPILE );
			ANode.renderTree(  gl, h_cube );
			gl.glEndList();
		}
		gl.glCallList(  h_list  );
		*/
		
		ANode.renderTree(  gl, h_cube );
		
		r += .5;

	}
	
	

}
