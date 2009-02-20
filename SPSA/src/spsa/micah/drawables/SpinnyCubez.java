package spsa.micah.drawables;

import javax.media.opengl.GL;
import javax.media.opengl.GLContext;

import spsa.micah.geometry.*;
import spsa.micah.scene.*;

import java.lang.Math;

public class SpinnyCubez extends Drawable {
	int numCubes = 1000,
		cycleTime = 15;
	static int cubeList;
	
	Scene scene;
	
	public SpinnyCubez(Scene s){
		scene = s;
	}
	
	static public void init(){
		GL gl = GLContext.getCurrent().getGL();
		
		cubeList = gl.glGenLists(1);
		gl.glNewList(cubeList,GL.GL_COMPILE);
			_drawCube(gl);
		gl.glEndList();
	}
	
	@Override
	protected void drawElement() {
		gl.glColor3f(1,0,0);
		for (int i=0; i < numCubes; i++){
			
			double seconds = (scene.currentTime),
				rot = ((seconds % cycleTime));
			
			if (Math.floor(seconds / cycleTime % 2) == 0)
				rot = cycleTime - rot;
			rot = rot - (cycleTime/2);
			
			gl.glRotated(rot, 0, 1, 0);
			gl.glTranslated(1,rot/(cycleTime/2),0);
			drawCube();
		}
	}
	
	void drawCube(){
		gl.glCallList(cubeList);
	}
	
	static void _drawCube(GL gl){
		gl.glBegin(GL.GL_QUADS);
			gl.glNormal3f( 1, 0, 0 );
			gl.glVertex3f( 0.5f, 0.5f,-0.5f);
			gl.glVertex3f( 0.5f, 0.5f, 0.5f);
			gl.glVertex3f( 0.5f,-0.5f, 0.5f);
			gl.glVertex3f( 0.5f,-0.5f,-0.5f);
		gl.glEnd();
		
		
		gl.glBegin(GL.GL_QUADS);
		gl.glNormal3f( -1, 0, 0 );
			gl.glVertex3f(-0.5f, 0.5f, 0.5f);
			gl.glVertex3f(-0.5f, 0.5f,-0.5f);
			gl.glVertex3f(-0.5f,-0.5f,-0.5f);
			gl.glVertex3f(-0.5f,-0.5f, 0.5f);
		gl.glEnd();
		
		
		gl.glBegin(GL.GL_QUADS);
			gl.glNormal3f( 0, 1, 0 );
			gl.glVertex3f( 0.5f, 0.5f,-0.5f);
			gl.glVertex3f(-0.5f, 0.5f,-0.5f);
			gl.glVertex3f(-0.5f, 0.5f, 0.5f);
			gl.glVertex3f( 0.5f, 0.5f, 0.5f);
		gl.glEnd();
		
		
		gl.glBegin(GL.GL_QUADS);
			gl.glNormal3f( 0, -1, 0 );
			gl.glVertex3f( 0.5f,-0.5f, 0.5f);
			gl.glVertex3f(-0.5f,-0.5f, 0.5f);
			gl.glVertex3f(-0.5f,-0.5f,-0.5f);
			gl.glVertex3f( 0.5f,-0.5f,-0.5f);
		gl.glEnd();
		
		gl.glBegin(GL.GL_QUADS);
			gl.glNormal3f( 0, 0, 1);
			gl.glVertex3f( 0.5f, 0.5f, 0.5f);
			gl.glVertex3f(-0.5f, 0.5f, 0.5f);
			gl.glVertex3f(-0.5f,-0.5f, 0.5f);
			gl.glVertex3f( 0.5f,-0.5f, 0.5f);
		gl.glEnd();
		
		gl.glBegin(GL.GL_QUADS);
			gl.glNormal3f( 0, 0, -1);
			gl.glVertex3f( 0.5f,-0.5f,-0.5f);
			gl.glVertex3f(-0.5f,-0.5f,-0.5f);
			gl.glVertex3f(-0.5f, 0.5f,-0.5f);
			gl.glVertex3f( 0.5f, 0.5f,-0.5f);	
		gl.glEnd();
	}
	
	
	

	@Override
	public Sphere getBoundingSphere() {

		return null;
	}

	@Override
	public Polygons getPolygons() {

		return null;
	}

}
