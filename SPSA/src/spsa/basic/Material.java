package spsa.basic;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import com.sun.opengl.util.texture.Texture;

/**
 * <p>
 * Container class for material properties.
 * </p>
 * 
 * <p>
 * Description: container class for material properties.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2007, Lev A Neiman
 * </p>
 * 
 * <p>
 * Company: Ohio University EECS
 * </p>
 * 
 * @author Lev A Neiman
 * @version 1.0
 */
public class Material
{
	/**
	 * JOGL Texture object. if it is equal to null then there is no texture.
	 */
	public Texture texture = null;

	/**
	 * unique name for this material.
	 */
	public String name;

	/**
	 * RGBA color
	 */
	public float color[] = { 0f, .5f, 0f, 1f };
	/**
	 * ambience
	 */
	public float ambient[] = { 1f, 1f, 1f, 1f };
	/**
	 * diffuse
	 */
	public float diffuse[] = { 1f, 1f, 1f, 1f };
	/**
	 * specular
	 */
	public float specular[] = { 1f, 1f, 1f, 1f };
	/**
	 * shininess
	 */
	public float shininess[] = { 100f };

	/**
	 * 
	 * @param name
	 *            String
	 */
	public Material(String name)
	{
		this.name = new String( name );
	}

	/**
	 * default constructor
	 */
	public Material()
	{
		this.name = "no_name";
	}

	/**
	 * this method applies this material to the GL instance.
	 * 
	 * @param drawable
	 *            GLAutoDrawable
	 */
	public void apply(GL gl)
	{
		// GL gl = drawable.getGL();

		gl.glMaterialfv( gl.GL_FRONT_AND_BACK, gl.GL_AMBIENT, ambient, 0 );
		gl.glMaterialfv( gl.GL_FRONT_AND_BACK, gl.GL_DIFFUSE, diffuse, 0 );
		gl.glMaterialfv( gl.GL_FRONT_AND_BACK, gl.GL_SPECULAR, specular, 0 );
		gl.glMaterialfv( gl.GL_FRONT_AND_BACK, gl.GL_SHININESS, shininess, 0 );
		if (texture != null)
		{
			texture.bind();
		}
		gl.glColor4f( color[0], color[1], color[2], color[3] );
	}
}
