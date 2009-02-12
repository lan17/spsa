package edu.ohiou.lev_neiman.sceneapi.basic;

import javax.media.opengl.GLAutoDrawable;
import java.io.*;

public interface ShaderProgram
{
    public enum ProgramType
    {
        VERTEX_PROGRAM, FRAGMENT_PROGRAM
    }


    public void addShader( GLAutoDrawable g, ProgramType type, String[] source )
            throws IllegalArgumentException;

    public void addShader( GLAutoDrawable g, ProgramType type, InputStream str ) throws IOException;

    public void deleteShader( GLAutoDrawable g, ProgramType type );

    public boolean link( GLAutoDrawable g );

    public boolean setUniformVariable( GLAutoDrawable g, String name, float var );

    public boolean setUniformVariable( GLAutoDrawable g, String name, int var );

    public boolean setUniformVariableMatrix4( GLAutoDrawable g, String name, float[] var );

    public void use( GLAutoDrawable g );
}
