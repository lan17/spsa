package spsa;

import java.awt.Color;
import java.util.LinkedList;

import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import javax.swing.JOptionPane;

/**
 * <p>
 * Title: GenericRenderer
 * </p>
 * 
 * <p>
 * Description: This class implements both GLCanvas and GLEventListener. It's
 * purpose is to make it easier to set up a quick JOGL program. Might not be
 * necessarily good for bigger and more complicated apps.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2009, Lev A. Neiman
 * </p>
 * 
 * <p>
 * Company: Ohio University school of EECS.
 * </p>
 * 
 * @author Lev A. Neiman
 * @version 1.0
 */
public class GenericRenderer extends GLCanvas implements GLEventListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Clear color for the scene. Think of it as background color.
	 */
	protected Color clear_color = new Color( 0, 0, 0 );

	/**
	 * field of view angle.
	 */
	protected double fovy = 50.0;

	/**
	 * nearest objects to the camera that are still visible.
	 */
	protected double near = .01;

	/**
	 * furthest distance from camera to objects that are still visible.
	 */
	protected double far = 1000;

	/**
	 * list of functions that are required for this app.
	 */
	protected LinkedList< String > required_gl_functions = new LinkedList< String >();

	/**
     * 
     */
	public GenericRenderer()
	{
		super.addGLEventListener( this );
	}

	protected boolean checkFunctions(GLAutoDrawable g)
	{
		GL gl = g.getGL();
		boolean ret_value = true;
		String bad_funcs = "Sorry but your system does not appear to support the following GL functions: \n";
		for (String func : required_gl_functions)
		{
			boolean this_func = gl.isFunctionAvailable( func );
			ret_value &= this_func;
			if (!this_func)
			{
				bad_funcs += ",\n" + func;
			}
		}
		if (!ret_value)
		{
			// JOptionPane.showMessageDialog(
			// edu.ohiou.vital_lab.jung.volume_render.Main.frame, bad_funcs );
			System.exit( -1 );

		}
		return ret_value;

	}

	public void init(GLAutoDrawable gLAutoDrawable)
	{
		checkFunctions( gLAutoDrawable );
		GL gl = gLAutoDrawable.getGL();
		//gLAutoDrawable.setGL( new DebugGL( gl ) );
		gl.glShadeModel( GL.GL_SMOOTH );
		//gl.glClearColor( (float) clear_color.getRed() / 255f, (float) clear_color.getGreen() / 255f, (float) clear_color.getBlue() / 255f, 1f ); // set
																																					// background
																																					// color
		gl.glEnable(gl.GL_CULL_FACE);																																// to
																																					// black
																																					// .
		gl.glClearDepth( 1.0f );

		gl.glEnable( GL.GL_DEPTH_TEST );
		gl.glDepthFunc( GL.GL_LEQUAL );
		//gl.glHint( GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST );

		//gl.glEnable( GL.GL_BLEND );
		//gl.glBlendFunc( GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA );

	}

	public void display(GLAutoDrawable gLAutoDrawable)
	{
		GL gl = gLAutoDrawable.getGL();
		gl.glClear( GL.GL_COLOR_BUFFER_BIT );
		gl.glClear( GL.GL_DEPTH_BUFFER_BIT );
		//gl.glMatrixMode( GL.GL_MODELVIEW );
		gl.glLoadIdentity();

	}

	public void reshape(GLAutoDrawable gLAutoDrawable, int _int, int _int2, int width, int height)
	{
		final GL gl = gLAutoDrawable.getGL();
		if (width <= 2)
		{
			width = 3;
		}
		if (height <= 2)
		{
			height = 3;
		}
		final float h = (float) width / (float) height;
		gl.glMatrixMode( GL.GL_PROJECTION );
		gl.glLoadIdentity();
		GLU glu = new GLU();
		glu.gluPerspective( fovy, h, near, far );
		gl.glMatrixMode( GL.GL_MODELVIEW );
		gl.glLoadIdentity();

	}

	public void displayChanged(GLAutoDrawable gLAutoDrawable, boolean _boolean, boolean _boolean2)
	{
	}

	public void setClearColor(Color clear_color)
	{
		this.clear_color = clear_color;
	}

	public void setClearColor(GLAutoDrawable g, Color clear_color)
	{
		this.clear_color = clear_color;
		(g.getGL()).glClearColor( (float) clear_color.getRed() / 255f, (float) clear_color.getGreen() / 255f, (float) clear_color.getBlue() / 255f, 1f );
	}

}
