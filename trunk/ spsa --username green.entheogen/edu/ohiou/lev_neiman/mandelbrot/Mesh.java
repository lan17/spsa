package edu.ohiou.lev_neiman.mandelbrot;

import edu.ohiou.lev_neiman.sceneapi.basic.*;
import javax.media.opengl.*;

/**
 * <p>Title: Scientific Volume Rendering</p>
 *
 * <p>Description: Lev Neiman's Summer Job</p>
 *
 * <p>Copyright: Copyright (c) 2008, Lev A. Neiman</p>
 *
 * <p>Company: Dr. Peter Jung</p>
 *
 * @author Lev A. Neiman
 * @version 1.0
 */
public class Mesh
        extends ANode
{

    private ShaderProgram mandelbrot_shader = null;
    public float center_a, center_b, dist_real,dist_i;
    public Mesh()
    {
        width=height=9;
        z_const = -6;
        x_max = y_max = 50;
        a = 0;
        b = 0;
        center_a=center_b=0;
        dist_real = 2;
        dist_i = 1;
    }

    float a, b;

    float width,height,z_const;
    int x_max, y_max;

    private void set_up_shader( GLAutoDrawable gl )
    {
        mandelbrot_shader = new GLSLSHaderProgram( gl );
        try
        {
            mandelbrot_shader.addShader( gl, ShaderProgram.ProgramType.VERTEX_PROGRAM, Mesh.class.getResourceAsStream( "vertex_shader.png" ) );
            mandelbrot_shader.addShader( gl, ShaderProgram.ProgramType.FRAGMENT_PROGRAM, Mesh.class.getResourceAsStream( "fragment_shader.png" ));
            //Mesh.class.getResourceAsStream( "vertex_shader.txt" );

            mandelbrot_shader.link( gl );
        //mandelbrot_shader.use( gl );
        }
        catch( Exception exc )
        {
            exc.printStackTrace();
        }

    }



    public void render_mesh( GLAutoDrawable g )
    {
        GL gl = g.getGL();
        mandelbrot_shader.use( g );
        mandelbrot_shader.setUniformVariable( g, "left", 0f );
        mandelbrot_shader.setUniformVariable( g, "right", width );
        mandelbrot_shader.setUniformVariable( g, "down", 0f);
        mandelbrot_shader.setUniformVariable( g, "up", height );
        mandelbrot_shader.setUniformVariable( g, "center_a", center_a );
        mandelbrot_shader.setUniformVariable( g, "center_b", center_b );
        mandelbrot_shader.setUniformVariable( g, "dist_real", dist_real );
        mandelbrot_shader.setUniformVariable( g, "dist_i", dist_i );
        for( int i = 0; i < y_max; ++i )
        {
            gl.glBegin( gl.GL_LINE_STRIP );
            for( int j = 0; j < x_max; ++j )
            {
                double x = (double)j / (double)x_max * width ;
                double y = (double)i / (double)y_max * height;
                gl.glNormal3f( 0, 0, 1 );
                gl.glVertex3d( x, y, z_const );
                gl.glVertex3d( x, y + height / (double)y_max, z_const );
            }
            gl.glEnd();
        }
    }
    private boolean shader_set_up = false;
    public void render( GLAutoDrawable gl )
    {
        if( !shader_set_up )
            {
                set_up_shader( gl );
                shader_set_up = true;
            }
        super.render( gl );
        render_mesh( gl );
    }

    public static void main( String[] args )
    {
        Mesh mesh = new Mesh();
    }
}
