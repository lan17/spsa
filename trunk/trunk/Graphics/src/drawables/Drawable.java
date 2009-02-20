package drawables;

import geometry.*;

import java.util.Vector;

import javax.media.opengl.GL;
import javax.media.opengl.GLContext;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import scene.*;


public abstract class Drawable {

	public Transformation transformation;
	
	protected GL gl;
	protected GLU glu = new GLU();
	protected GLUquadric quadric = glu.gluNewQuadric();
	
	public static Scene scene;
	
	public Drawable()
	{
		this(new Vector3d(), new Vector3d(), new Vector3d(1,1,1));
	}
	
	public Drawable(Vector3d pos, Vector3d rot, Vector3d scale)
	{
		this.transformation = new Transformation(pos, rot, scale);
	}
	
	
	public void draw()
	{
		gl = GLContext.getCurrent().getGL();
		
		begin();
			drawElement();
		end();
	}
	
	private void begin()
	{
		gl.glPushMatrix();
			gl.glTranslated(transformation.pos.x,transformation.pos.y,transformation.pos.z);
			gl.glRotated((transformation.rot.x * (180/Math.PI)), 1, 0, 0);
			gl.glRotated((transformation.rot.y * (180/Math.PI)), 0, 1, 0);
			gl.glRotated((transformation.rot.z * (180/Math.PI)), 0, 0, 1);
			
			gl.glScaled(transformation.scale.x, transformation.scale.y, transformation.scale.z);
	}
	
	private void end()
	{
		gl.glPopMatrix();
	}
	

	
	abstract protected void drawElement();
	
	public abstract Sphere getBoundingSphere();
	public abstract Polygons getPolygons();
	
}