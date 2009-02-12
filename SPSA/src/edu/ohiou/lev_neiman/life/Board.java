package edu.ohiou.lev_neiman.life;

import java.util.*;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import edu.ohiou.lev_neiman.sceneapi.GenericRenderer;
import edu.ohiou.lev_neiman.sceneapi.basic.*;

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
public class Board
        extends GenericRenderer
{
    static Random generator = new Random( System.currentTimeMillis() );

    int N;
    short[][][] board;

    static final short _ALIVE = 1;
    static final short _DEAD = 2;

    private double scale_factor = 1;
    Coordinate alive_color = new Coordinate( 1, 0, 0 );
    Coordinate dead_color = new Coordinate( 0, 0, 0 );

    Coordinate tmp_pos = new Coordinate();

    float y_rot = 0f;

    LightNode light = new LightNode( GL.GL_LIGHT0 );

    Vector<Cube> cubes = new Vector<Cube> ();
    Cube[][][] cube_board;

    public Board( int n )
    {
        N = n;
        board = new short[N ][ N ][ N ];
        randomizeBoard( board );
        buildCubeBoard();
    }

    private void buildCubeBoard()
    {
        cube_board = new Cube[N ][ N ][ N ];
        for( int x = 0; x < N; ++x )
        {
            for( int y = 0; y < N; ++y )
            {
                for( int z = 0; z < N; ++z )
                {
                    cube_board[ y ][ x ][ z ] = new Cube( new Coordinate( x, y, z ) );
                    cubes.add( cube_board[ y ][ x ][ z ] );
                }
            }
        }
        Collections.sort( cubes );
    }

    private void updateCubeBoard()
    {
        for( int x = 0; x < N; ++x )
        {
            for( int y = 0; y < N; ++y )
            {
                for( int z = 0; z < N; ++z )
                {
                    if( board[ y ][ x ][ z ] == _ALIVE )
                    {
                        cube_board[ y ][ x ][ z ].show = true;
                    }
                    else
                    {
                        cube_board[ y ][ x ][ z ].show = false;
                    }
                }
            }
        }

    }

    public void init( GLAutoDrawable g )
    {
        super.init( g );
        GL gl = g.getGL();
        gl.glEnable( GL.GL_CULL_FACE );
        gl.glCullFace( GL.GL_BACK );

        gl.glPointSize( 5f );
    }

    private boolean transform_once = true;
    public void display( GLAutoDrawable g )
    {
        super.display( g );
        GL gl = g.getGL();
        light.render( g );
        gl.glTranslated( -10, -10, -50 );
        //gl.glRotatef( y_rot, 0, 1, 0 );
        gl.glColor4d( 1, 0, 0, .1 );

        updateCubeBoard();
        gl.glGetDoublev( GL.GL_MODELVIEW_MATRIX, transform_matrix, 0 );
        transformed.clear();
        if( transform_once )
        {
            transform_once = false;
            Collections.sort( cubes );
        }
        for( Cube c : cubes )
        {
            c.drawCube( g );
        }

        updateBoard();
        y_rot += 5;
    }

    private void applyColor( GL gl, Coordinate color, double t )
    {
        gl.glColor4d( color.getX(), color.getY(), color.getZ(), t );
    }

    public static double transform_matrix[] = new double[16 ];
    private static Map<Coordinate, Coordinate> transformed = new HashMap<Coordinate, Coordinate> ();
    private class Cube
            implements Comparable
    {

        private int cube_list;
        private boolean need_to_make_cube_list = true;
        public boolean show = true;
        private Coordinate p;


        public Cube( Coordinate p )
        {
            this.p = p;
        }

        public Coordinate getPosition()
        {
            return p;
        }

        public void setPosition( Coordinate p )
        {
            this.p = p;
        }

        public void drawCube( GLAutoDrawable g )
        {
            GL gl = g.getGL();
            if( need_to_make_cube_list )
            {
                cube_list = gl.glGenLists( 1 );
                gl.glNewList( cube_list, gl.GL_COMPILE );
                {
                    ANode.drawCube( g );
                    /*
                                         gl.glBegin( GL.GL_POINTS );
                                         gl.glVertex3d( 0,0,0 );
                                         gl.glEnd();
                     */
                }
                gl.glEndList();

                need_to_make_cube_list = false;
            }
            if( show )
            {
                gl.glPushMatrix();
                {
                    gl.glTranslated( p.getX(), p.getY(), p.getZ() );
                    gl.glCallList( cube_list );
                }
                gl.glPopMatrix();
            }
        }


        public int compareTo( Object o )
        {
            if( o instanceof Cube )
            {
                Coordinate a, b;
                if( transformed.containsKey( p ) )
                {
                    a = transformed.get( p );
                }
                else
                {
                    a = Coordinate.multiply4x4( p, transform_matrix );
                    transformed.put( p, a );
                }
                if( transformed.containsKey( ( ( Cube ) o ).p ) )
                {
                    b = transformed.get( ( ( Cube ) o ).p );
                }
                else
                {
                    b = Coordinate.multiply4x4( ( ( Cube ) o ).p, transform_matrix );
                    transformed.put( ( ( Cube ) o ).p, b );
                }

                return Double.compare( a.getZ(), b.getZ() );

            }
            return 0;
        }
    }


    private short getShort( int x, int y, int z )
    {
        if( x < 0 )
        {
            x = N + x;
        }
        if( y < 0 )
        {
            y = N + y;
        }
        if( z < 0 )
        {
            z = N + z;
        }
        if( x >= N )
        {
            x = x - N;
        }
        if( y >= N )
        {
            y = y - N;
        }
        if( z >= N )
        {
            z = z - N;
        }
        return board[ y ][ x ][ z ];
    }

    private short determineNextState( int x, int y, int z )
    {
        short ret = _ALIVE;
        short alive_count = 0;
        short dead_count = 0;
        for( int y_t = y - 1; y_t <= y + 1; ++y_t )
        {
            for( int x_t = x - 1; x_t <= x + 1; ++x_t )
            {
                for( int z_t = z - 1; z_t <= z + 1; ++z_t )
                {
                    if( y_t != y || x_t != x || z_t != z )
                    {
                        if( getShort( x_t, y_t, z_t ) == _ALIVE )
                        {
                            alive_count++;
                        }
                        else
                        {
                            dead_count++;
                        }
                    }
                }
            }
        }
        //System.out.println( Integer.toString( alive_count ) + " " + Integer.toString( dead_count ));
        if( board[ y ][ x ][ z ] == _ALIVE && ( alive_count < 4 || alive_count > 13 ) )
        {
            return _DEAD;
        }
        else if( board[ y ][ x ][ z ] == _ALIVE )
        {
            return _ALIVE;
        }
        else if( board[ y ][ x ][ z ] == _DEAD && ( alive_count >= 9 && alive_count <= 14 ) )
        {
            return _ALIVE;
        }
        else if( board[ y ][ x ][ z ] == _DEAD )
        {
            return _DEAD;
        }

        return 0;
    }

    private void updateBoard()
    {
        short[][][] new_board = new short[N ][ N ][ N ];
        for( int y = 0; y < N; ++y )
        {
            for( int x = 0; x < N; ++x )
            {
                for( int z = 0; z < N; ++z )
                {
                    new_board[ y ][ x ][ z ] = determineNextState( x, y, z );
                }
            }
        }
        board = null;
        board = new_board;
    }

    private static void randomizeBoard( short[][][] board )
    {
        for( int y = 0; y < board.length; ++y )
        {
            for( int x = 0; x < board[ y ].length; ++x )
            {
                for( int z = 0; z < board[ y ][ x ].length; ++z )
                {
                    if( generator.nextBoolean() )
                    {
                        board[ y ][ x ][ z ] = _ALIVE;
                    }
                    else
                    {
                        board[ y ][ x ][ z ] = _DEAD;
                    }
                }
            }
        }
    }
}
