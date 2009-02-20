package spsa.micah.drawables;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import com.sun.opengl.util.texture.*;
import com.sun.opengl.util.*;
import java.io.*;
import java.util.*;

public class TexturedCube
{
	Texture pz, nz, px, nx, py, ny;
	GL gl;
	
	TexturedCube(GL gl)
	{
		this.gl = gl;
	}
	
	TexturedCube(GL gl, Texture px, Texture nx, Texture py, Texture ny, Texture pz, Texture nz)
	{
		this.px = px;
		this.nx = nx;
		this.py = py;
		this.ny = ny;
		this.pz = pz;
		this.nz = nz;
		this.gl = gl;
		
	}
	
	void draw()
	{
	    if (px != null)
	    {
	    	px.enable();
	    	px.bind();
	    	
	    	gl.glBegin(GL.GL_QUADS);
	    		gl.glNormal3f( 1, 0, 0 );
	    		gl.glTexCoord2f(0, 1); gl.glVertex3f( 0.5f, 0.5f,-0.5f);
	    		gl.glTexCoord2f(1, 1); gl.glVertex3f( 0.5f, 0.5f, 0.5f);
	    		gl.glTexCoord2f(1, 0); gl.glVertex3f( 0.5f,-0.5f, 0.5f);
	    		gl.glTexCoord2f(0, 0); gl.glVertex3f( 0.5f,-0.5f,-0.5f);
		    gl.glEnd();
	    }
	    else
	    {
	    	gl.glBegin(GL.GL_QUADS);
		    	gl.glVertex3f( 0.5f, 0.5f,-0.5f);
		    	gl.glVertex3f( 0.5f, 0.5f, 0.5f);
		    	gl.glVertex3f( 0.5f,-0.5f, 0.5f);
		    	gl.glVertex3f( 0.5f,-0.5f,-0.5f);
	    	gl.glEnd();
	    }
	    
	    
	    
	    if (nx != null)
	    {
	    	nx.enable();
	    	nx.bind();
		    
	    	gl.glBegin(GL.GL_QUADS);
	    		gl.glNormal3f( -1, 0, 0 );
			    gl.glTexCoord2f(1, 1); gl.glVertex3f(-0.5f, 0.5f, 0.5f);
			    gl.glTexCoord2f(0, 1); gl.glVertex3f(-0.5f, 0.5f,-0.5f);
			    gl.glTexCoord2f(0, 0); gl.glVertex3f(-0.5f,-0.5f,-0.5f);
			    gl.glTexCoord2f(1, 0); gl.glVertex3f(-0.5f,-0.5f, 0.5f);
		    gl.glEnd();
		}
	    else
	    {
	    	gl.glBegin(GL.GL_QUADS);
	    	gl.glNormal3f( -1, 0, 0 );
		    	gl.glVertex3f(-0.5f, 0.5f, 0.5f);
		    	gl.glVertex3f(-0.5f, 0.5f,-0.5f);
		    	gl.glVertex3f(-0.5f,-0.5f,-0.5f);
		    	gl.glVertex3f(-0.5f,-0.5f, 0.5f);
	    	gl.glEnd();
	    }
		

		
	    if (py != null)
	    {
	    	py.enable();
	    	py.bind();
	    	gl.glBegin(GL.GL_QUADS);
	    	gl.glNormal3f( 0, 1, 0 );
		    	gl.glTexCoord2f(1, 0); gl.glVertex3f( 0.5f, 0.5f,-0.5f);
		    	gl.glTexCoord2f(0, 0); gl.glVertex3f(-0.5f, 0.5f,-0.5f);
		    	gl.glTexCoord2f(0, 1); gl.glVertex3f(-0.5f, 0.5f, 0.5f);
		    	gl.glTexCoord2f(1, 1); gl.glVertex3f( 0.5f, 0.5f, 0.5f);
	    	gl.glEnd();
	    }
	    else
	    {
	    	gl.glBegin(GL.GL_QUADS);
	    		gl.glNormal3f( 0, 1, 0 );
		    	gl.glVertex3f( 0.5f, 0.5f,-0.5f);
		    	gl.glVertex3f(-0.5f, 0.5f,-0.5f);
		    	gl.glVertex3f(-0.5f, 0.5f, 0.5f);
		    	gl.glVertex3f( 0.5f, 0.5f, 0.5f);
	    	gl.glEnd();
	    }
		
	    
	    if (ny != null)
	    {
	    	ny.enable();
	    	ny.bind();
	    	
	    	gl.glBegin(GL.GL_QUADS);
	    		gl.glNormal3f( 0, -1, 0 );
		    	gl.glTexCoord2f(1, 1); gl.glVertex3f( 0.5f,-0.5f, 0.5f);
		    	gl.glTexCoord2f(0, 1); gl.glVertex3f(-0.5f,-0.5f, 0.5f);
		    	gl.glTexCoord2f(0, 0); gl.glVertex3f(-0.5f,-0.5f,-0.5f);
		    	gl.glTexCoord2f(1, 0); gl.glVertex3f( 0.5f,-0.5f,-0.5f);
	    	gl.glEnd();
	    }
	    else
	    {
	    	gl.glBegin(GL.GL_QUADS);
	    		gl.glNormal3f( 0, -1, 0 );
		    	gl.glVertex3f( 0.5f,-0.5f, 0.5f);
		    	gl.glVertex3f(-0.5f,-0.5f, 0.5f);
		    	gl.glVertex3f(-0.5f,-0.5f,-0.5f);
		    	gl.glVertex3f( 0.5f,-0.5f,-0.5f);
	    	gl.glEnd();
	    }

	    
	    if (pz != null)
	    {
	    	pz.enable();
	    	pz.bind();
	    
	    	gl.glBegin(GL.GL_QUADS);
	    		gl.glNormal3f( 0, 0, 1);
			    gl.glTexCoord2f(1, 1); gl.glVertex3f( 0.5f, 0.5f, 0.5f);
			    gl.glTexCoord2f(0, 1); gl.glVertex3f(-0.5f, 0.5f, 0.5f);
			    gl.glTexCoord2f(0, 0); gl.glVertex3f(-0.5f,-0.5f, 0.5f);
			    gl.glTexCoord2f(1, 0); gl.glVertex3f( 0.5f,-0.5f, 0.5f);
		    gl.glEnd();
	    }
	    else
	    {
	    	gl.glBegin(GL.GL_QUADS);
	    		gl.glNormal3f( 0, 0, 1);
		    	gl.glVertex3f( 0.5f, 0.5f, 0.5f);
		    	gl.glVertex3f(-0.5f, 0.5f, 0.5f);
		    	gl.glVertex3f(-0.5f,-0.5f, 0.5f);
		    	gl.glVertex3f( 0.5f,-0.5f, 0.5f);
	    	gl.glEnd();
	    }
	    
	    
	    if (nz != null)
	    {
	    	nz.enable();
	    	nz.bind();
	    	
	    	gl.glBegin(GL.GL_QUADS);
	    		gl.glNormal3f( 0, 0, -1);
		    	gl.glTexCoord2f(0, 0); gl.glVertex3f( 0.5f,-0.5f,-0.5f);
		    	gl.glTexCoord2f(1, 0); gl.glVertex3f(-0.5f,-0.5f,-0.5f);
		    	gl.glTexCoord2f(1, 1); gl.glVertex3f(-0.5f, 0.5f,-0.5f);
		    	gl.glTexCoord2f(0, 1); gl.glVertex3f( 0.5f, 0.5f,-0.5f);
	    	gl.glEnd();
	    }
	    else
	    {
	    	gl.glBegin(GL.GL_QUADS);
	    		gl.glNormal3f( 0, 0, -1);
		    	gl.glVertex3f( 0.5f,-0.5f,-0.5f);
		    	gl.glVertex3f(-0.5f,-0.5f,-0.5f);
		    	gl.glVertex3f(-0.5f, 0.5f,-0.5f);
		    	gl.glVertex3f( 0.5f, 0.5f,-0.5f);	
	    	gl.glEnd();
	    }
	    
	 gl.glEnd();// End Drawing The Cube
	}
}