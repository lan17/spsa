package edu.ohiou.lev_neiman.sceneapi.visualize;

import javax.media.opengl.GLAutoDrawable;

import edu.ohiou.lev_neiman.jung.volume_render.Main;
import edu.ohiou.lev_neiman.sceneapi.basic.GLSLSHaderProgram;
import edu.ohiou.lev_neiman.sceneapi.basic.ShaderProgram;

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
public class MappingFunction
{
    public static final float e = 2.71828f;
    public static final float pi = 3.14159f;
    public static final String[] vertex_shader_skeleton =
            {"uniform float scale_factor; uniform float gauss_a; uniform float gauss_b; uniform float gauss_c; varying vec4 colorz; void main( void ) { gl_FrontColor = gl_Color;",
            "colorz = gl_FrontColor; gl_Position = ftransform();}"};

    public MappingFunction( GLSLSHaderProgram shader )
    {
        super();
    }

    /**
     * construct gaussian function of form f(x) = ae^-( (x-b)^2 / (2c^2 ) )
     * @param a float
     * @param b float
     * @param c float
     * @param shader ShaderProgram
     */
    public static GLSLSHaderProgram gaussianDistribution( GLAutoDrawable g )
    {

        GLSLSHaderProgram ret = new GLSLSHaderProgram( g );
        //ret.addShader( GL.GL_VERTEX_SHADER, new String[] { constructProgram( inject ) });
        ret.addShader( g, ShaderProgram.ProgramType.VERTEX_PROGRAM, Main.working_folder + Main.separator + "scale_vshader.glsl" );
        ret.addShader( g, ShaderProgram.ProgramType.FRAGMENT_PROGRAM, Main.working_folder + Main.separator + "t3d_g_fragment.glsl" );
        ret.link( g );
        return ret;
    }

    public static String constructProgram( String[] body )
    {
        String ret = "";
        ret += vertex_shader_skeleton[ 0 ];
        for( String line : body )
        {
            ret += line + "\n";
        }
        ret += vertex_shader_skeleton[ 1 ];

        return ret;
    }

    public static void main( String[] args )
    {

    }
}
