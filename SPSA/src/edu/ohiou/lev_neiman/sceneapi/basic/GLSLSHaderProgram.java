package edu.ohiou.lev_neiman.sceneapi.basic;

import java.io.*;
import java.util.*;
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
public class GLSLSHaderProgram
        implements ShaderProgram
{
    private int program[] =
            { -1};

    private int vertex_shader = -1;
    private int fragment_shader = -1;


    public GLSLSHaderProgram( GLAutoDrawable g )
    {
        GL gl = g.getGL();
        System.out.println( "Is glCreateProgram available? = " + Boolean.toString( gl.isFunctionAvailable( "glCreateProgram" ) ) );
        //gl.glGenProgramsNV( 1, program, 0 );
        program[ 0 ] = gl.glCreateProgram();
    }

    public void addShader( GLAutoDrawable g, ProgramType type, File file )
    {
        String[] shader = null;
        try
        {
            shader = readFile( file );
        }
        catch( FileNotFoundException ex )
        {
            System.err.println( "Couldn't read shader from file - '" + file.toString() + "' because it doesnt exist" );
            return;
        }
        catch( IOException ex )
        {
            System.err.println( "Some sort of IO error happened while reading shader from file - " + file.toString() );
            ex.printStackTrace();
            return;
        }

        addShader( g, type, shader );
    }

    public void addShader( GLAutoDrawable g, ProgramType type, String file_name )
    {
        addShader( g, type, new File( file_name ) );
    }

    public void deleteShader( GLAutoDrawable g, ProgramType type )
    {

        int shada = type == ProgramType.VERTEX_PROGRAM ? vertex_shader : fragment_shader;

        GL gl = g.getGL();
        System.out.println( "Is program valid program  = " + Boolean.toString( gl.glIsProgram( program[ 0 ] ) ) );
        System.out.println( "Is shader valid shader = " + Boolean.toString( gl.glIsShader( shada ) ) );

        gl.glDetachShader( program[ 0 ], vertex_shader );
        gl.glDeleteShader( shada );

    }

    private void printStringArr( String[] arr )
    {
        for( String s : arr )
        {
            System.out.println( s );
        }
    }

    private void contatenateStringArr( String[] source )
    {
        for( int i = 1; i < source.length; ++i )
        {
            source[0].concat( "\n"+source[i] );
        }
    }

    public String stream2string( InputStream str )
    {
        String ret;
        try
        {
            char[] st = new char[str.available()];
            int c = str.read();
            int i = 0;
            while( c != -1  )
            {
                st[i] = (char)c;
                c = str.read();
                i++;
            }
            ret = new String( st );
            return ret;
        }
        catch( Exception exc )
        {
            exc.printStackTrace();
        }
        return null;
    }

    public void addShader( GLAutoDrawable g, ProgramType type, InputStream in_str )
    {

        String[] source = new String[1];
        source[0] = stream2string( in_str );

        System.out.println( "ZOMG = " + source[0] );
        addShader( g, type, source );
    }
    public void addShader( GLAutoDrawable g, ProgramType type, String[] source )
            throws IllegalArgumentException
    {
        GL gl = g.getGL();
        //printStringArr( source );
        if( source == null )
        {
            throw new IllegalArgumentException( "Wrong arguments passed to addShader( int, String[] )" );
        }
        contatenateStringArr( source );
        int shada = -1;
        int type_int = type == ProgramType.VERTEX_PROGRAM ? GL.GL_VERTEX_SHADER : GL.GL_FRAGMENT_SHADER;
        switch( type_int )
        {
            case GL.GL_VERTEX_SHADER:
            {
                shada = vertex_shader = gl.glCreateShader( type_int );
            }
            case GL.GL_FRAGMENT_SHADER:
            {
                shada = fragment_shader = gl.glCreateShader( type_int );
            }
        }
        gl.glShaderSource( shada, 1, source, null );
        gl.glCompileShader( shada );
        gl.glAttachShader( program[ 0 ], shada );
    }

    public boolean link( GLAutoDrawable g )
    {
        boolean ret = false;
        ( g.getGL() ).glLinkProgram( program[ 0 ] );
        return ret;
    }

    public void use( GLAutoDrawable g )
    {
        try
        {
            ( g.getGL() ).glUseProgram( program[ 0 ] );
        }
        catch( GLException g_e )
        {
            System.err.println( "unable to use shader " + toString() );
            g_e.printStackTrace();
            ( g.getGL() ).glUseProgram( 0 );
        }
    }

    public boolean setUniformVariableMatrix4( GLAutoDrawable g, String name, float[] var )
    {
        GL gl = g.getGL();
        int var_location = gl.glGetUniformLocationARB( program[ 0 ], name );
        if( var_location == -1 )
        {
            return false;
        }
        gl.glUniformMatrix4fv( var_location, 1, false, var, 0 );
        return true;

    }

    public boolean setUniformVariable( GLAutoDrawable g, String name, float var )
    {
        GL gl = g.getGL();
        int var_location = gl.glGetUniformLocationARB( program[ 0 ], name );
        if( var_location == -1 )
        {
            return false;
        }
        gl.glUniform1f( var_location, var );
        return true;
    }

    public boolean setUniformVariable( GLAutoDrawable g, String name, int var )
    {
        GL gl = g.getGL();
        int var_location = gl.glGetUniformLocationARB( program[ 0 ], name );
        if( var_location == -1 )
        {
            return false;
        }
        gl.glUniform1i( var_location, var );
        return true;
    }


    public static String[] readFile( File file )
            throws FileNotFoundException, IOException
    {
        String ret[] =
                {""};
        FileInputStream fstream = new FileInputStream( file );

        // Convert our input stream to a
        // DataInputStream
        DataInputStream in = new DataInputStream( fstream );

        // Continue to read lines while
        // there are still some left to read
        while( in.available() != 0 )
        {
            // Print file line to screen
            ret[ 0 ] += in.readLine().trim() + "\n";
        }
        in.close();

        System.out.println( "Read File" );

        return ret;
    }

    public static String[] trippy =
            {"varying vec4 colorz; void main( void ) { float f; vec3 norm = normalize( gl_NormalMatrix * gl_Normal ); vec3 lightv = normalize( gl_LightSource[0].position - gl_ModelViewMatrix * gl_Vertex );   const vec4 color1 = { 0,1,0, 1 };   const vec4 color2 = { 1,0,0,1 };   const vec4 color3 = { 0, 0, 1, 1 };   float dt = dot(lightv, norm );   if( dt < .3 ) gl_FrontColor = color1;      else if( dt < .7 ) gl_FrontColor=color2;         else gl_FrontColor = color3;   gl_FrontColor *= vec4( norm, 1 ) ;   colorz = gl_FrontColor;   gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;}"};
    public static String[] simple_fragment_shader =
            {"varying vec4 colorz;void main(void){gl_FragColor = colorz;}"};

}
