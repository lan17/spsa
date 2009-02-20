package spsa.micah.scene;


import javax.media.opengl.GL;
import javax.media.opengl.GLContext;

import spsa.micah.geometry.Vector3d;


public class Eye {
	public Vector3d pos;
	public Vector3d rot;
	GL gl;
	
	Eye()
	{
		this(new Vector3d(), new Vector3d());
	}
	
	Eye(Vector3d pos, Vector3d rot)
	{
		this.pos = pos;
		this.rot = rot;
	}
	
	public void look()
	{
		gl = GLContext.getCurrent().getGL();
		
		gl.glRotated(-(rot.x * (180/Math.PI)), 1, 0, 0);
		gl.glRotated(-(rot.y * (180/Math.PI)), 0, 1, 0);
		gl.glRotated(-(rot.z * (180/Math.PI)), 0, 0, 1);
		
		gl.glTranslated(-pos.x, -pos.y, -pos.z);
	
	}
	
	
	public void moveForward(int distance)
	{
		pos.z -= distance;
	}
	
	public void moveBackward(int distance)
	{
		pos.z += distance;
	}
	
	public void moveUp(int distance)
	{
		pos.y += distance;
	}
	
	public void moveDown(int distance)
	{
		pos.y -= distance;
	}
	
	public void moveLeft(int distance)
	{
		pos.x -= distance;
	}
	
	public void moveRight(int distance)
	{
		pos.x += distance;
	}
}
