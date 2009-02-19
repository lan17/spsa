package edu.ohiou.vital_lab.sceneapi.examples.hyper_cube;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import edu.ohiou.vital_lab.sceneapi.basic.ANode;
import edu.ohiou.vital_lab.sceneapi.basic.DisplayListManager;

/**
 * <p>
 * Generates a Tree which represents the HyperCube.
 * </p>
 * 
 * <p>
 * Description: Generates a Tree which represents the HyperCube.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * 
 * <p>
 * Company: Ohio University EECS
 * </p>
 * 
 * @author Lev A Neiman
 * @version 1.0
 */
/**
 * @author Entheogen
 *
 */
public class HyperCube extends ANode
{
	
	public class Cube extends ANode
	{		
		public Cube( int disp_list )
		{
			super( disp_list );
		}
		
		public void setOffsetMask( int mask )
		{
			this.offset_mask = mask;			
		}
		
		
		public void applyMaskOffset( )
		{
			if( (offset_mask & x_offset ) != 0 ) x_off = offset;
			if( (offset_mask & y_offset ) != 0 ) y_off = offset;
			if( (offset_mask & z_offset ) != 0 ) z_off = offset;
			
			if( (offset_mask & x_dir ) != 0 ) x_off *= -1;
			if( (offset_mask & y_dir ) != 0 ) y_off *= -1;
			if( (offset_mask & z_dir ) != 0 ) z_off *= -1;
			
			super.translation.set( x_off, y_off, z_off );
		}
		
		public void animate()
		{
			this.applyMaskOffset();
			for( ANode child : children )
			{
				((Cube)child).animate();
			}
		}
		
		double x_off, y_off, z_off;
		
		public int offset_mask;
		public int direction;
		
		
	}
	
	static int x_offset = 1<<3;
	static int y_offset = 1<<2;
	static int z_offset = 1;
	
	static int z_dir = 1 <<4;
	static int y_dir = 1<<5;
	static int x_dir = 1<<6;
	
	/**
	 * size difference between parent and children. its .5 by default, meaning
	 * each child is half the size of parent.
	 */
	private float sf;

	/**
	 * offset between nodes.
	 */
	private float offset;

	/**
	 * current display list used to render all nodes.
	 */
	private int cur_disp;

	/**
	 * defines the number of recursive levels used to generate the fractal.
	 */
	private int detail = 5;

	/**
	 * init functions that initializes variables to their default values.
	 */
	private void init(GLAutoDrawable g)
	{
		sf = .5f;
		offset = (float) .75;
		if( DisplayListManager.getList( "cube" ) == null )
		{
			GL gl = g.getGL();
			int tmp = gl.glGenLists( 1 );
			gl.glNewList( tmp, gl.GL_COMPILE );
			ANode.drawCube( gl );
			gl.glEndList();
			DisplayListManager.addList( g, "cube", tmp );
		}
		cur_disp = DisplayListManager.getList( "cube" );
		display_list = DisplayListManager.getList( "cube" );

	}

	/**
	 * Default constructor.
	 * 
	 * @param g
	 *            GLAutoDrawable
	 */
	public HyperCube(GLAutoDrawable g)
	{
		super();
		init( g );
		makeRCubez( this, detail );

	}
	
	public void setOffset( float offset )
	{
		this.offset = offset;
	}
	
	public float getOffset( )
	{
		return offset;
	}

	private void cleanUp()
	{
		for (Object child : children)
		{
			child = null;
		}
		children.clear();
	}

	/**
	 * regenerate tree with new level of recursive detail.
	 * 
	 * @param detail
	 *            int
	 */
	public void resetDetail(int detail)
	{
		this.detail = detail;
		cleanUp();
		makeRCubez( this, detail );
		System.out.println( ANode.countNodes( this ) );
	}

	/**
	 * what is the current recursive detail level.
	 * 
	 * @return int
	 */
	public int getDetail()
	{
		return detail;
	}

	/**
	 * display list that each node will use to render itself.
	 * 
	 * @param list
	 *            int
	 */
	public void setDisplayList(int list)
	{
		super.display_list = list;
		cur_disp = list;
	}

	/**
	 * create the HyperCube tree.
	 * 
	 * @param n
	 *            int
	 */
	public void makeRCubez(int n)
	{
		makeRCubez( this, n );
	}

	/**
	 * create the HyperCube tree.
	 * 
	 * @param tree
	 *            ANode
	 * @param n
	 *            int
	 */
	public void makeRCubez(ANode tree, int n)
	{
		makeRCubez( tree, n, 0 );
	}

	/**
	 * create the HyperCube tree.
	 * 
	 * @param tree
	 *            ANode
	 * @param n
	 *            int
	 * @param rec
	 *            int
	 */
	/**
	 * @param tree
	 * @param n
	 * @param rec
	 */
	public void makeRCubez(ANode tree, int n, int rec)
	{
		if (rec >= n) { return; } // if current recursion level is deeper than
									// n, return.
		int nrec = rec + 1;
		Cube small_cube = new Cube( cur_disp );
		small_cube.setName( Integer.toString( nrec ) + "x1" );
		tree.addChild( small_cube );
		//small_cube.translation().set( -offset, 0, 0 );
		small_cube.offset_mask = x_offset | x_dir;
		small_cube.applyMaskOffset();
		small_cube.scale().set( sf, sf, sf );
		makeRCubez( small_cube, n, nrec );

		small_cube = new Cube( cur_disp );
		small_cube.setName( Integer.toString( nrec ) + "x2" );
		tree.addChild( small_cube );
		//small_cube.translation().set( offset, 0, 0 );
		small_cube.offset_mask = x_offset;
		small_cube.applyMaskOffset();
		small_cube.scale().set( sf, sf, sf );
		makeRCubez( small_cube, n, nrec );

		small_cube = new Cube( cur_disp );
		small_cube.setName( Integer.toString( nrec ) + "x3" );
		tree.addChild( small_cube );
		//small_cube.translation().set( 0, offset, 0 );
		small_cube.offset_mask = y_offset;
		small_cube.applyMaskOffset();
		small_cube.scale().set( sf, sf, sf );
		makeRCubez( small_cube, n, nrec );

		small_cube = new Cube( cur_disp );
		small_cube.setName( Integer.toString( nrec ) + "x4" );
		tree.addChild( small_cube );
		//small_cube.translation().set( 0, -offset, 0 );
		small_cube.offset_mask = y_offset | y_dir;
		small_cube.applyMaskOffset();
		small_cube.scale().set( sf, sf, sf );
		makeRCubez( small_cube, n, nrec );

		small_cube = new Cube( cur_disp );
		small_cube.setName( Integer.toString( nrec ) + "x5" );
		tree.addChild( small_cube );
		//small_cube.translation().set( 0, 0, offset );
		small_cube.offset_mask = z_offset;
		small_cube.applyMaskOffset();
		small_cube.scale().set( sf, sf, sf );
		makeRCubez( small_cube, n, nrec );

		small_cube = new Cube( cur_disp );
		small_cube.setName( Integer.toString( nrec ) + "x6" );
		tree.addChild( small_cube );
		//small_cube.translation().set( 0, 0, -offset );
		small_cube.offset_mask = z_offset | z_dir;
		small_cube.applyMaskOffset();
		small_cube.scale().set( sf, sf, sf );
		makeRCubez( small_cube, n, nrec );
	}
	
	
	public void animte( )
	{
		for( ANode child : children )
		{
			((Cube)child).animate();
		}
	}

}
