package edu.ohiou.lev_neiman.sceneapi.visualize;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;
import java.util.TreeSet;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import com.sun.opengl.util.BufferUtil;
import edu.ohiou.lev_neiman.jung.volume_render.Main;
import edu.ohiou.lev_neiman.jung.volume_render.Renderer;
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
public class Volume
        extends ANode
{

    public int cur_mode = 0;

    float[][][][] data;
    int xDim = 10;
    int yDim = 10;
    int zDim = 10;
    static Random generator = new Random( System.currentTimeMillis() );

    private ANode cube = new ANode();

    //private static int display_list = -1;

    private static float getElementFromBuffer( FloatBuffer canvaz, int x, int y, int z, int N, float def )
    {
        if( x < 0 || y < 0 || z < 0 || x >= N || y >= N || z >= N )
        {
            return def;
        }
        return canvaz.get( from3Dto1D( x, y, z, N ) );
    }

    private double getElement( int x, int y, int z, int e, double def )
    {
        if( x < 0 || y < 0 || z < 0 || x >= xDim || y >= yDim || z >= zDim )
        {
            return def;
        }
        return data[ y ][ x ][ z ][ e ];
    }

    private void randomizeData()
    {

        double c = 125000;
        double count = 0;
        for( int y = 0; y < yDim; ++y )
        {
            for( int x = 0; x < xDim; ++x )
            {
                for( int z = 0; z < zDim; ++z )
                {
                    count++;
                    data[ y ][ x ][ z ][ 0 ] = generator.nextFloat();
                    data[ y ][ x ][ z ][ 1 ] = generator.nextFloat();
                    data[ y ][ x ][ z ][ 2 ] = generator.nextFloat();
                    data[ y ][ x ][ z ][ 3 ] = generator.nextFloat();
                    //data[y][x][z][3] = Math.sqrt( (xDim-x)*(xDim-x) + (yDim-y)*(yDim-y) + (zDim-z)*(zDim-z) ) / 100f ;
                    //data[y][x][z] = generator.nextFloat();
                    //data[y][x][z][3] = count/c;
                    //data[y][x][z][3] = 1;
                    //System.out.println( data[y][x][z] );
                }
            }
        }
    }


    private void smooth2()
    {
        float data2[][][][] = new float[yDim ][ xDim ][ zDim ][ 4 ];
        for( int y = 0; y < yDim; ++y )
        {
            for( int x = 0; x < xDim; ++x )
            {
                for( int z = 0; z < zDim; ++z )
                {
                    /*
                                         double r1, r2, r3, r4, r5, r6;
                                         double g1, g2, g3, g4, g5, g6;
                                         double b1, b2, b3, b4, b5, b6;
                                         double t1, t2, t3, t4, t5, t6;

                                         r1 = r2 = r3 = r4 = r5 = r6 = data[y][x][z][0];
                                         g1 = g2 = g3 = g4 = g5 = g6 = data[y][x][z][1];
                                         b1 = b2 = b3 = b4 = b5 = b6 = data[y][x][z][2];
                                         t1 = t2 = t3 = t4 = t5 = t6 = data[y][x][z][3];

                                         r1 = getElement( x, y - 1, z, 0, data[y][x][z][0] );
                                         r2 = getElement( x, y + 1, z, 0, data[y][x][z][0] );
                                         r3 = getElement( x - 1, y, z, 0, data[y][x][z][0] );
                                         r4 = getElement( x + 1, y, z, 0, data[y][x][z][0] );
                                         r5 = getElement( x, y, z - 1, 0, data[y][x][z][0] );
                                         r6 = getElement( x, y, z + 1, 0, data[y][x][z][0] );

                                         g1 = getElement( x, y - 1, z, 1, data[y][x][z][1] );
                                         g2 = getElement( x, y + 1, z, 1, data[y][x][z][1] );
                                         g3 = getElement( x - 1, y, z, 1, data[y][x][z][1] );
                                         g4 = getElement( x + 1, y, z, 1, data[y][x][z][1] );
                                         g5 = getElement( x, y, z - 1, 1, data[y][x][z][1] );
                                         g6 = getElement( x, y, z + 1, 1, data[y][x][z][1] );

                                         b1 = getElement( x, y - 1, z, 2, data[y][x][z][2] );
                                         b2 = getElement( x, y + 1, z, 2, data[y][x][z][2] );
                                         b3 = getElement( x - 1, y, z, 2, data[y][x][z][2] );
                                         b4 = getElement( x + 1, y, z, 2, data[y][x][z][2] );
                                         b5 = getElement( x, y, z - 1, 2, data[y][x][z][2] );
                                         b6 = getElement( x, y, z + 1, 2, data[y][x][z][2] );

                                         t1 = getElement( x, y - 1, z, 3, data[y][x][z][3] );
                                         t2 = getElement( x, y + 1, z, 3, data[y][x][z][3] );
                                         t3 = getElement( x - 1, y, z, 3, data[y][x][z][3] );
                                         t4 = getElement( x + 1, y, z, 3, data[y][x][z][3] );
                                         t5 = getElement( x, y, z - 1, 3, data[y][x][z][3] );
                                         t6 = getElement( x, y, z + 1, 3, data[y][x][z][3] );
                     */

                    int c = 0;
                    float r_sum, g_sum, b_sum, t_sum;
                    r_sum = g_sum = b_sum = t_sum = 0;
                    for( int i = x - 1; i <= x + 1; ++i )
                    {
                        for( int j = y - 1; j <= y + 1; ++j )
                        {
                            for( int k = z - 1; k <= z + 1; ++k )
                            {
                                r_sum += getElement( i, j, k, 0, data[ y ][ x ][ z ][ 0 ] );
                                g_sum += getElement( i, j, k, 1, data[ y ][ x ][ z ][ 1 ] );
                                b_sum += getElement( i, j, k, 2, data[ y ][ x ][ z ][ 2 ] );
                                t_sum += getElement( i, j, k, 3, data[ y ][ x ][ z ][ 3 ] );
                                c++;
                            }
                        }
                    }

                    //data2[y][x][z][0] = r_sum / c;
                    //data2[y][x][z][1] = g_sum / c;
                    //data2[y][x][z][2] = b_sum / c;


                    data2[ y ][ x ][ z ][ 0 ] = data[ y ][ x ][ z ][ 0 ];
                    data2[ y ][ x ][ z ][ 1 ] = data[ y ][ x ][ z ][ 1 ];
                    data2[ y ][ x ][ z ][ 2 ] = data[ y ][ x ][ z ][ 2 ];

                    /*
                                        data2[y][x][z][0] += r_sum / c;
                                        data2[y][x][z][1] += g_sum / c;
                                        data2[y][x][z][2] += b_sum / c;
                     */

                    data2[ y ][ x ][ z ][ 3 ] = t_sum / c;

                    /*
                                         data[y][x][z][0] = ( r1 + r2 + r3 + r4 + r5 + r6 ) / 6f;
                                         data[y][x][z][1] = ( g1 + g2 + g3 + g4 + g5 + g6 ) / 6f;
                                         data[y][x][z][2] = ( b1 + b2 + b3 + b4 + b5 + b6 ) / 6f;


                                         data[y][x][z][3] = ( t1 + t2 + t3 + t4 + t5 + t6 ) / 6f;
                     */
                }
            }
        }
        data = null;
        data = data2;
    }


    public void smooth3()
    {
        float data2[][][][] = new float[yDim ][ xDim ][ zDim ][ 4 ];
        for( int y = 0; y < yDim; ++y )
        {
            for( int x = 0; x < xDim; ++x )
            {
                for( int z = 0; z < zDim; ++z )
                {
                    float r1, r2, r3;
                    float g1, g2, g3;
                    float b1, b2, b3;
                    float t1, t2, t3;
                    r1 = r2 = r3 = data[ y ][ x ][ z ][ 0 ];
                    g1 = g2 = g3 = data[ y ][ x ][ z ][ 1 ];
                    b1 = b2 = b3 = data[ y ][ x ][ z ][ 2 ];
                    t1 = t2 = t3 = data[ y ][ x ][ z ][ 3 ];
                    if( y < yDim - 1 )
                    {
                        r1 = data[ y + 1 ][ x ][ z ][ 0 ];
                        g1 = data[ y + 1 ][ x ][ z ][ 1 ];
                        b1 = data[ y + 1 ][ x ][ z ][ 2 ];
                        t1 = data[ y + 1 ][ x ][ z ][ 3 ];
                    }
                    if( x < xDim - 1 )
                    {
                        r2 = data[ y ][ x + 1 ][ z ][ 0 ];
                        g2 = data[ y ][ x + 1 ][ z ][ 1 ];
                        b2 = data[ y ][ x + 1 ][ z ][ 2 ];
                        t2 = data[ y ][ x + 1 ][ z ][ 3 ];
                    }
                    if( z < zDim - 1 )
                    {
                        r3 = data[ y ][ x ][ z + 1 ][ 0 ];
                        g3 = data[ y ][ x ][ z + 1 ][ 1 ];
                        b3 = data[ y ][ x ][ z + 1 ][ 2 ];
                        t3 = data[ y ][ x ][ z + 1 ][ 3 ];

                    }
                    data2[ y ][ x ][ z ][ 0 ] = ( data[ y ][ x ][ z ][ 0 ] + r1 + r2 + r3 ) / 4f;
                    data2[ y ][ x ][ z ][ 1 ] = ( data[ y ][ x ][ z ][ 1 ] + g1 + g2 + g3 ) / 4f;
                    data2[ y ][ x ][ z ][ 2 ] = ( data[ y ][ x ][ z ][ 2 ] + b1 + b2 + b3 ) / 4f;
                    data2[ y ][ x ][ z ][ 3 ] = ( data[ y ][ x ][ z ][ 3 ] + t1 + t2 + t3 ) / 4f;
                }
            }
        }
        data = null;
        data = data2;
    }


    private void smooth()
    {
        float data2[][][][] = new float[yDim ][ xDim ][ zDim ][ 4 ];
        for( int y = 0; y < yDim; ++y )
        {
            for( int x = 0; x < xDim; ++x )
            {
                for( int z = 0; z < zDim; ++z )
                {
                    float r1, r2, r3, r4, r5, r6;
                    float g1, g2, g3, g4, g5, g6;
                    float b1, b2, b3, b4, b5, b6;
                    float t1, t2, t3, t4, t5, t6;

                    r1 = r2 = r3 = r4 = r5 = r6 = data[ y ][ x ][ z ][ 0 ];
                    g1 = g2 = g3 = g4 = g5 = g6 = data[ y ][ x ][ z ][ 1 ];
                    b1 = b2 = b3 = b4 = b5 = b6 = data[ y ][ x ][ z ][ 2 ];
                    t1 = t2 = t3 = t4 = t5 = t6 = data[ y ][ x ][ z ][ 3 ];
                    if( y > 0 )
                    {
                        r1 = data[ y - 1 ][ x ][ z ][ 0 ];
                        g1 = data[ y - 1 ][ x ][ z ][ 1 ];
                        b1 = data[ y - 1 ][ x ][ z ][ 2 ];
                        t1 = data[ y - 1 ][ x ][ z ][ 3 ];
                    }
                    if( y < yDim - 1 )
                    {
                        r2 = data[ y + 1 ][ x ][ z ][ 0 ];
                        g2 = data[ y + 1 ][ x ][ z ][ 1 ];
                        b2 = data[ y + 1 ][ x ][ z ][ 2 ];
                        t2 = data[ y + 1 ][ x ][ z ][ 3 ];
                    }
                    if( x > 0 )
                    {
                        r3 = data[ y ][ x - 1 ][ z ][ 0 ];
                        g3 = data[ y ][ x - 1 ][ z ][ 1 ];
                        b3 = data[ y ][ x - 1 ][ z ][ 2 ];
                        t3 = data[ y ][ x - 1 ][ z ][ 3 ];
                    }
                    if( x < xDim - 1 )
                    {
                        r4 = data[ y ][ x + 1 ][ z ][ 0 ];
                        g4 = data[ y ][ x + 1 ][ z ][ 1 ];
                        b4 = data[ y ][ x + 1 ][ z ][ 2 ];
                        t4 = data[ y ][ x + 1 ][ z ][ 3 ];
                    }
                    if( z > 0 )
                    {
                        r5 = data[ y ][ x ][ z - 1 ][ 0 ];
                        g5 = data[ y ][ x ][ z - 1 ][ 1 ];
                        b5 = data[ y ][ x ][ z - 1 ][ 2 ];
                        t5 = data[ y ][ x ][ z - 1 ][ 3 ];
                    }
                    if( z < zDim - 1 )
                    {
                        r6 = data[ y ][ x ][ z + 1 ][ 0 ];
                        g6 = data[ y ][ x ][ z + 1 ][ 1 ];
                        b6 = data[ y ][ x ][ z + 1 ][ 2 ];
                        t6 = data[ y ][ x ][ z + 1 ][ 3 ];
                    }

                    /*
                                         data2[y][x][z][0] = ( r1 + r2 + r3 + r4 + r5 + r6 ) / 6f;
                                         data2[y][x][z][1] = ( g1 + g2 + g3 + g4 + g5 + g6 ) / 6f;
                                         data2[y][x][z][2] = ( b1 + b2 + b3 + b4 + b5 + b6 ) / 6f;
                     */
                    data2[ y ][ x ][ z ][ 0 ] = data[ y ][ x ][ z ][ 0 ];
                    data2[ y ][ x ][ z ][ 1 ] = data[ y ][ x ][ z ][ 1 ];
                    data2[ y ][ x ][ z ][ 2 ] = data[ y ][ x ][ z ][ 2 ];

                    data2[ y ][ x ][ z ][ 3 ] = ( t1 + t2 + t3 + t4 + t5 + t6 ) / 6f;
                }
            }
        }
        data = null;
        data = data2;
    }

    private static float distance( int x, int y, int z, int x1, int y1, int z1 )
    {

        return( float ) Math.sqrt( ( x - x1 ) * ( x - x1 ) + ( y - y1 ) * ( y - y1 ) + ( z - z1 ) * ( z - z1 ) );

    }

    private void createSphere( int x, int y, int z, int radius, float red, float green, float blue, float alpha )
    {
        for( int x1 = ( x - radius >= 0 ) ? ( x - radius ) : ( radius ); x1 <= x1 + radius && x1 < xDim; ++x1 )
        {
            for( int y1 = ( y - radius >= 0 ) ? ( y - radius ) : ( radius ); y1 <= y1 + radius && y1 < yDim; ++y1 )
            {
                for( int z1 = ( z - radius >= 0 ) ? ( z - radius ) : ( radius ); z1 <= z + radius && z1 < zDim; ++z1 )
                {
                    float r = distance( x1, y1, z1, x, y, z );

                    if( r <= radius )
                    {
                        //System.out.println( r );
                        float factor = r / radius;
                        //System.out.println( factor );
                        if( generator.nextFloat() < .5 )
                        {
                            data[ y1 ][ x1 ][ z1 ][ 0 ] = red * factor;
                            data[ y1 ][ x1 ][ z1 ][ 1 ] = green * factor;
                            data[ y1 ][ x1 ][ z1 ][ 2 ] = blue * factor;
                            data[ y1 ][ x1 ][ z1 ][ 3 ] *= alpha * factor;
                        }
                        else
                        {
                            data[ y1 ][ x1 ][ z1 ][ 0 ] = red * factor;
                            data[ y1 ][ x1 ][ z1 ][ 1 ] = green * factor;
                            data[ y1 ][ x1 ][ z1 ][ 2 ] = blue * factor;
                            data[ y1 ][ x1 ][ z1 ][ 3 ] = alpha * factor;
                        }
                    }
                }
            }
        }
    }

    private void initData( float red, float green, float blue, float alpha )
    {
        for( int y = 0; y < yDim; ++y )
        {
            for( int x = 0; x < xDim; ++x )
            {
                for( int z = 0; z < zDim; ++z )
                {
                    data[ y ][ x ][ z ][ 0 ] = red;
                    data[ y ][ x ][ z ][ 1 ] = green;
                    data[ y ][ x ][ z ][ 2 ] = blue;
                    data[ y ][ x ][ z ][ 3 ] = alpha;
                }
            }
        }
    }

    private static Coordinate getRColor()
    {
        Coordinate ret = new Coordinate();
        ret.setX( generator.nextDouble() );
        ret.setY( generator.nextDouble() );
        ret.setZ( generator.nextDouble() );
        return ret;
    }

    private void createRandomSpheres( int how_many, int maxRadius )
    {
        for( int i = 0; i < how_many; ++i )
        {
            Coordinate color = getRColor();
            createSphere( generator.nextInt( xDim ), generator.nextInt( yDim ), generator.nextInt( zDim ),
                          generator.nextInt( maxRadius - 1 ) + 1, ( float ) color.getX(), ( float ) color.getY(), ( float ) color.getZ(),
                          generator.nextFloat() );
        }
    }

    public void setNewData( FloatBuffer new_data )
    {
        System.out.println( "Buffer size = " + Integer.toString( new_data.capacity() ) );
        int dim = ( int ) Math.pow( ( double ) new_data.capacity(), 1f / 3f );
        System.out.println( "New dimension = " + Integer.toString( dim ) );
        xDim = yDim = zDim = dim;
        if( data_buffer != null )
        {
            data_buffer.clear();
            data_buffer = null;
        }
        data_buffer = new_data;
        this.texture3d_method_need_to_set_up = true;

        //float dm [] = DataReader.computeMeanDev( new_data );
        //GaussEditor.setValues( .05f, dm[0], dm[1]*.1f );
        //FirstTest.canvas.changeGaussFactor( dm[0], dm[1] );

    }

    public void setNewData( float[][][] new_data )
    {
        float min = Float.MAX_VALUE;
        float max = Float.MAX_VALUE * -1;
        xDim = yDim = zDim = new_data.length;
        System.out.println( "New Dimension = " + Integer.toString( xDim ) );
        this.data = null;
        this.data = new float[new_data.length ][ new_data.length ][ new_data.length ][ 4 ];
        for( int y = 0; y < yDim; ++y )
        {
            for( int x = 0; x < xDim; ++x )
            {
                for( int z = 0; z < zDim; ++z )
                {
                    data[ y ][ x ][ z ][ 0 ] = .1f;
                    data[ y ][ x ][ z ][ 1 ] = .1f;
                    data[ y ][ x ][ z ][ 2 ] = .1f;
                    data[ y ][ x ][ z ][ 3 ] = new_data[ y ][ x ][ z ];
                    if( max < data[ y ][ x ][ z ][ 3 ] )
                    {
                        max = data[ y ][ x ][ z ][ 3 ];
                    }
                    if( min > data[ y ][ x ][ z ][ 3 ] )
                    {
                        min = data[ y ][ x ][ z ][ 3 ];
                    }
                }
            }
        }
        System.out.println( "Min = " + Float.toString( min ) + "\nMax = " + Float.toString( max ) );
        normalizeData( max );
    }

    public float interpolate( float y1, float y2, float t1, float t2, float ti )
    {
        float alpha = ( y2 - y1 ) / ( t2 - t1 );
        float beta = y1 - alpha * t1;
        return alpha * ti + beta;

    }


    public void normalizeData( float max )
    {

        for( int y = 0; y < yDim; ++y )
        {
            for( int x = 0; x < xDim; ++x )
            {
                for( int z = 0; z < zDim; ++z )
                {
                    data[ y ][ x ][ z ][ 0 ] = .5f;
                    data[ y ][ x ][ z ][ 1 ] = .5f;
                    data[ y ][ x ][ z ][ 2 ] = .5f;
                    data[ y ][ x ][ z ][ 3 ] /= max;
                }
            }
        }

    }

    public void createRandomGas()
    {
        initData( .5f, .5f, .5f, 1f );
        //randomizeData();
        System.out.print( "Creating random spheres...... " );
        createRandomSpheres( 10, 20 );
        System.out.print( "done!\nSmoothing data......" );
        for( int i = 0; i < 10; ++i )
        {
            //randomizeData();
            //smooth2();
            System.out.println( i );
        }
        System.out.print( "done!\n" );
        //smooth3();
    }

    private static int from3Dto1D( int x, int y, int z, int N )
    {

        return x + y * N + z * N * N;
    }


    private static void createSphere( FloatBuffer canvaz, int N, int x, int y, int z, int radius, float alpha )
    {
        for( int x1 = ( x - radius >= 0 ) ? ( x - radius ) : ( radius ); x1 <= x1 + radius && x1 < N; ++x1 )
        {
            for( int y1 = ( y - radius >= 0 ) ? ( y - radius ) : ( radius ); y1 <= y1 + radius && y1 < N; ++y1 )
            {
                for( int z1 = ( z - radius >= 0 ) ? ( z - radius ) : ( radius ); z1 <= z + radius && z1 < N; ++z1 )
                {
                    float r = distance( x1, y1, z1, x, y, z );

                    if( r <= radius )
                    {

                        float factor = r / radius;
                        int pos = from3Dto1D( x1, y1, z1, N );
                        if( generator.nextFloat() < 0f )
                        {
                            canvaz.put( pos, canvaz.get( pos ) * alpha * factor );
                        }
                        else
                        {
                            canvaz.put( pos, alpha * factor );
                        }

                    }
                }
            }
        }
    }


    private static void createRandomSpheres( FloatBuffer canvaz, int N, int how_many, int maxRadius )
    {
        for( int i = 0; i < how_many; ++i )
        {
            Coordinate color = getRColor();
            createSphere( canvaz, N, generator.nextInt( N ), generator.nextInt( N ), generator.nextInt( N ),
                          generator.nextInt( maxRadius + 1 ) + 1, generator.nextFloat() );

        }
    }

    private static void touchData( FloatBuffer canvaz, float constant )
    {
        for( int i = 0; i < canvaz.capacity(); ++i )
        {
            canvaz.put( i, constant );
        }
    }

    private static FloatBuffer smooth( FloatBuffer canvaz, int N )
    {
        FloatBuffer new_canvaz = BufferUtil.newFloatBuffer( canvaz.capacity() );
        for( int x = 0; x < N; ++x )
        {
            for( int y = 0; y < N; ++y )
            {
                for( int z = 0; z < N; ++z )
                {
                    int c = 0;
                    float sum = 0;
                    for( int i = x - 1; i <= x + 1; ++i )
                    {
                        for( int j = y - 1; j <= y + 1; ++j )
                        {
                            for( int k = z - 1; k <= z + 1; ++k )
                            {
                                sum += getElementFromBuffer( canvaz, x, y, z, N, canvaz.get( from3Dto1D( x, y, z, N ) ) );
                                c++;
                            }
                        }
                    }
                    sum /= ( float ) c;
                    new_canvaz.put( from3Dto1D( x, y, z, N ), sum );

                }
            }
        }
        return new_canvaz;
    }

    public static FloatBuffer createRandomData( int N, int how_many, int smooth, float n_maxRadius_ratio )
    {
        FloatBuffer new_data = BufferUtil.newFloatBuffer( N * N * N );
        touchData( new_data, .5f );
        createRandomSpheres( new_data, N, how_many, ( int ) ( N * n_maxRadius_ratio ) );
        int smooth_count = smooth;
        for( int i = 0; i < smooth; ++i )
        {
            new_data = smooth( new_data, N );
            System.out.println( Float.toString( ( float ) i / ( float ) smooth_count ) );
        }

        return new_data;
    }


    public Volume()
    {

    }

    public void renderUsingPoints( GLAutoDrawable g )
    {
        GL gl = g.getGL();

        gl.glBegin( GL.GL_POINTS );
        for( int y = 0; y < data.length; ++y )
        {
            for( int x = 0; x < data[ y ].length; ++x )
            {
                for( int z = 0; z < data[ y ][ x ].length; ++z )
                {

                    gl.glColor4d( data[ y ][ x ][ z ][ 0 ], data[ y ][ x ][ z ][ 1 ], data[ y ][ x ][ z ][ 2 ], data[ y ][ x ][ z ][ 3 ] );
                    gl.glVertex3f( ( x - xDim / 2f ) / xDim, ( y - yDim / 2f ) / yDim, ( z - zDim / 2f ) / zDim );
                    //cube.render( g );

                }
            }
        }
        gl.glEnd();

    }

    public void renderUsingTriangles( GLAutoDrawable g )
    {
        GL gl = g.getGL();

        for( int y = 0; y < data.length; ++y )
        {
            for( int x = 0; x < data[ y ].length; ++x )
            {
                for( int z = 0; z < data[ y ][ x ].length; ++z )
                {
                    //if( data[ y ][ x ][ z ][ 3 ] < 0.1f ) continue;
                    subdivide_count++;
                    gl.glBegin( GL.GL_TRIANGLE_FAN );
                    if( x < xDim - 1 && y < yDim - 1 )
                    {
                        {
                            gl.glColor4f( data[ y ][ x ][ z ][ 0 ], data[ y ][ x ][ z ][ 1 ], data[ y ][ x ][ z ][ 2 ],
                                          data[ y ][ x ][ z ][ 3 ] );
                            gl.glVertex3f( ( x - xDim / 2f ) / xDim, ( y - yDim / 2f ) / yDim, ( z - zDim / 2f ) / zDim );
                        }
                        {
                            gl.glColor4f( data[ y + 1 ][ x ][ z ][ 0 ], data[ y + 1 ][ x ][ z ][ 1 ], data[ y + 1 ][ x ][ z ][ 2 ],
                                          data[ y + 1 ][ x ][ z ][ 3 ] );
                            gl.glVertex3f( ( x - xDim / 2f ) / xDim, ( ( y + 1 ) - yDim / 2f ) / yDim, ( z - zDim / 2f ) / zDim );
                        }
                        {
                            gl.glColor4f( data[ y + 1 ][ x + 1 ][ z ][ 0 ], data[ y + 1 ][ x + 1 ][ z ][ 1 ], data[ y + 1 ][ x + 1 ][ z ][ 2 ],
                                          data[ y + 1 ][ x + 1 ][ z ][ 3 ] );
                            gl.glVertex3f( ( ( x + 1 ) - xDim / 2f ) / xDim, ( ( y + 1 ) - yDim / 2f ) / yDim, ( z - zDim / 2f ) / zDim );
                        }
                        {
                            gl.glColor4f( data[ y ][ x + 1 ][ z ][ 0 ], data[ y ][ x + 1 ][ z ][ 1 ], data[ y ][ x + 1 ][ z ][ 2 ],
                                          data[ y ][ x + 1 ][ z ][ 3 ] );
                            gl.glVertex3f( ( ( x + 1 ) - xDim / 2f ) / xDim, ( y - yDim / 2f ) / yDim, ( z - zDim / 2f ) / zDim );
                        }
                    }
                    if( x < xDim - 1 && z < zDim - 1 )
                    {
                        {
                            gl.glColor4f( data[ y ][ x + 1 ][ z + 1 ][ 0 ], data[ y ][ x + 1 ][ z + 1 ][ 1 ], data[ y ][ x + 1 ][ z + 1 ][ 2 ],
                                          data[ y ][ x + 1 ][ z + 1 ][ 3 ] );
                            gl.glVertex3f( ( ( x + 1 ) - xDim / 2f ) / xDim, ( y - yDim / 2f ) / yDim, ( ( z + 1 ) - zDim / 2f ) / zDim );

                        }

                        {
                            gl.glColor4f( data[ y ][ x ][ z + 1 ][ 0 ], data[ y ][ x ][ z + 1 ][ 1 ], data[ y ][ x ][ z + 1 ][ 2 ],
                                          data[ y ][ x ][ z + 1 ][ 3 ] );
                            gl.glVertex3f( ( x - xDim / 2f ) / xDim, ( y - yDim / 2f ) / yDim, ( ( z + 1 ) - zDim / 2f ) / zDim );

                        }
                    }

                    if( z < zDim - 1 && y < yDim - 1 )
                    {
                        {
                            gl.glColor4f( data[ y + 1 ][ x ][ z + 1 ][ 0 ], data[ y + 1 ][ x ][ z + 1 ][ 1 ], data[ y + 1 ][ x ][ z + 1 ][ 2 ],
                                          data[ y + 1 ][ x ][ z + 1 ][ 3 ] );
                            gl.glVertex3f( ( x - xDim / 2f ) / xDim, ( ( y + 1 ) - yDim / 2f ) / yDim, ( ( z + 1 ) - zDim / 2f ) / zDim );
                        }
                        {
                            gl.glColor4f( data[ y + 1 ][ x ][ z ][ 0 ], data[ y + 1 ][ x ][ z ][ 1 ], data[ y + 1 ][ x ][ z ][ 2 ],
                                          data[ y + 1 ][ x ][ z ][ 3 ] );
                            gl.glVertex3f( ( x - xDim / 2f ) / xDim, ( ( y + 1 ) - yDim / 2f ) / yDim, ( z - zDim / 2f ) / zDim );
                        }
                    }

                    gl.glEnd();

                }
            }
        }

    }


    private int num_vertices = 0;
    private FloatBuffer vertices = null;
    private FloatBuffer colors = null;
    private IntBuffer indecies = null;
    private int[] VBOvertices = new int[1 ];
    private int[] VBOcolors = new int[1 ];
    private int[] VBOindecies = new int[1 ];

    private void fillBuffers( GLAutoDrawable g, boolean bindVBOs )
    {
        num_vertices = xDim * yDim * zDim;
        vertices = BufferUtil.newFloatBuffer( num_vertices * 3 );
        colors = BufferUtil.newFloatBuffer( num_vertices * 4 );
        //indecies = BufferUtil.newIntBuffer( num_vertices );
        int counter = 0;
        for( int z = 0; z < zDim; ++z )
        {
            for( int y = 0; y < yDim; ++y )
            {
                for( int x = 0; x < xDim; ++x )
                {
                    //gl.glColor4d( data[ y ][ x ][ z ][ 0 ], data[ y ][ x ][ z ][ 1 ], data[ y ][ x ][ z ][ 2 ],
                    //                    data[ y ][ x ][ z ][ 3 ] );
                    //       gl.glVertex3f( ( x - xDim / 2f ) / xDim, ( y - yDim / 2f ) / yDim, ( z - zDim / 2f ) / zDim );

                    vertices.put( ( x - xDim / 2f ) / xDim );
                    vertices.put( ( y - yDim / 2f ) / yDim );
                    vertices.put( ( z - zDim / 2f ) / zDim );

                    colors.put( data[ y ][ x ][ z ][ 0 ] );
                    colors.put( data[ y ][ x ][ z ][ 1 ] );
                    colors.put( data[ y ][ x ][ z ][ 2 ] );
                    colors.put( data[ y ][ x ][ z ][ 3 ] );

                    /*
                                         colors.put( (byte)( data[ y ][ x ][ z ][ 0 ] * 255 ) );
                                         colors.put( (byte)( data[ y ][ x ][ z ][ 1 ] * 255 ) );
                                         colors.put( (byte)( data[ y ][ x ][ z ][ 2 ] * 255 ) );
                                         colors.put( (byte)( data[ y ][ x ][ z ][ 3 ] * 255) );
                     */

                }
            }
        }
        vertices.rewind();
        colors.rewind();

        if( bindVBOs )
        {
            GL gl = g.getGL();
            gl.glGenBuffers( 1, VBOvertices, 0 );
            gl.glBindBuffer( GL.GL_ARRAY_BUFFER, VBOvertices[ 0 ] );
            gl.glBufferData( GL.GL_ARRAY_BUFFER, num_vertices * 3 * BufferUtil.SIZEOF_FLOAT, vertices, GL.GL_STATIC_DRAW );

            gl.glGenBuffers( 1, VBOcolors, 0 );
            gl.glBindBuffer( GL.GL_ARRAY_BUFFER, VBOcolors[ 0 ] );
            gl.glBufferData( GL.GL_ARRAY_BUFFER, num_vertices * 4 * BufferUtil.SIZEOF_FLOAT, colors, GL.GL_STATIC_DRAW );

            vertices = null;
            colors = null;
            data = null;
        }
    }

    private int mapFrom3Dto2D( int x, int y, int z )
    {
        return x + y * xDim + z * xDim * yDim;
    }

    private void buildIndecies( GLAutoDrawable g, boolean bindVBOs )
    {
        indecies = BufferUtil.newIntBuffer( num_vertices * 3 * 4 );
        for( int z = 0; z < zDim; ++z )
        {
            for( int y = 0; y < yDim; ++y )
            {
                for( int x = 0; x < xDim; ++x )
                {
                    // insert 6 indecies
                    if( x < xDim - 1 && y < yDim - 1 )
                    {
                        indecies.put( mapFrom3Dto2D( x, y, z ) );
                        indecies.put( mapFrom3Dto2D( x, y + 1, z ) );
                        indecies.put( mapFrom3Dto2D( x + 1, y + 1, z ) );
                        indecies.put( mapFrom3Dto2D( x + 1, y, z ) );
                    }
                    if( y < yDim - 1 && z < zDim - 1 )
                    {
                        indecies.put( mapFrom3Dto2D( x, y, z ) );
                        indecies.put( mapFrom3Dto2D( x, y + 1, z ) );
                        indecies.put( mapFrom3Dto2D( x, y + 1, z + 1 ) );
                        indecies.put( mapFrom3Dto2D( x, y, z + 1 ) );
                    }
                    if( x < xDim - 1 && z < zDim - 1 )
                    {
                        indecies.put( mapFrom3Dto2D( x, y, z ) );
                        indecies.put( mapFrom3Dto2D( x, y, z + 1 ) );
                        indecies.put( mapFrom3Dto2D( x + 1, y, z + 1 ) );
                        indecies.put( mapFrom3Dto2D( x + 1, y, z ) );
                    }
                }
            }
        }
        indecies.rewind();

        if( bindVBOs )
        {
            GL gl = g.getGL();
            gl.glGenBuffers( 1, VBOindecies, 0 );
            gl.glBindBuffer( GL.GL_ELEMENT_ARRAY_BUFFER, VBOindecies[ 0 ] );
            gl.glBufferData( GL.GL_ELEMENT_ARRAY_BUFFER, num_vertices * 3 * 4 * BufferUtil.SIZEOF_INT, indecies, GL.GL_STATIC_DRAW );
        }
    }

    private void buildIndecies2( GLAutoDrawable g, boolean buildVBOs )
    {
        GL gl = g.getGL();
        indecies = BufferUtil.newIntBuffer( num_vertices * 8 );

        for( int y = 0; y < yDim; ++y )
        {
            for( int z = 0; z < zDim; ++z )
            {
                for( int x = 0; x < xDim; ++x )
                {
                    //IntBuffer ind = BufferUtil.newIntBuffer( 8 );
                    int c = 1;
                    indecies.put( mapFrom3Dto2D( x, y, z ) );

                    indecies.put( mapFrom3Dto2D( x, y, z + 1 ) );
                    indecies.put( mapFrom3Dto2D( x + 1, y, z + 1 ) );

                    indecies.put( mapFrom3Dto2D( x + 1, y, z ) );
                    indecies.put( mapFrom3Dto2D( x + 1, y + 1, z ) );

                    indecies.put( mapFrom3Dto2D( x, y + 1, z ) );
                    indecies.put( mapFrom3Dto2D( x, y + 1, z + 1 ) );

                    indecies.put( mapFrom3Dto2D( x, y, z + 1 ) );

                    //gl.glDrawElements( GL.GL_TRIANGLE_STRIP, c, GL.GL_UNSIGNED_INT, ind );

                }
            }
        }

        indecies.rewind();
        if( buildVBOs )
        {
            gl.glGenBuffers( 1, VBOindecies, 0 );
            gl.glBindBuffer( GL.GL_ELEMENT_ARRAY_BUFFER, VBOindecies[ 0 ] );
            gl.glBufferData( GL.GL_ELEMENT_ARRAY_BUFFER, num_vertices * 8 * BufferUtil.SIZEOF_INT, indecies, GL.GL_STATIC_DRAW );
        }
    }

    private class myTreeSet
            extends java.util.TreeSet
    {

    }


    // take data[][][] and make indecies.
    private void buildBufferz_Subdivision( GLAutoDrawable g, boolean buildVBO )
    {
        TreeSet<Integer> [][] shpace = new TreeSet[yDim ][ xDim ];
        for( int i = 0; i < yDim; ++i )
        {
            for( int j = 0; j < xDim; ++j )
            {
                shpace[ i ][ j ] = null;
                //shpace[ i ][ j ] = new TreeSet<Integer> ();
            }
        }
        System.out.println( "Begining subdivision process......" );
        subdivide( 0, 0, 0, xDim - 1, shpace );
        System.out.println( "Done subdividing!  Subdivide count = " + Integer.toString( subdivide_count ) );
    }

    private class Cube
    {
        public Cube( int x1, int y1, int z1, int length )
        {
            this.x1 = x1;
            this.y1 = y1;
            this.z1 = z1;
            this.length = length;
        }

        public int x1, y1, z1, length;
    }


    private static float _epsilon = 1e-9f;
    private int subdivide_count = 0;
    private void subdivide( int x1, int y1, int z1, int length, TreeSet<Integer>[][] shpace )
    {
        // base case
        if( length <= 1 )
        {
            return;
        }

        subdivide_count++;
        // create new Treesets for 2 points if they are null.
        if( shpace[ y1 ][ x1 ] == null )
        {
            shpace[ y1 ][ x1 ] = new TreeSet<Integer> ();
        }
        if( shpace[ y1 + length ][ x1 + length ] == null )
        {
            shpace[ y1 + length ][ x1 + length ] = new TreeSet<Integer> ();
        }
        shpace[ y1 ][ x1 ].add( z1 );
        shpace[ y1 + length ][ x1 + length ].add( z1 + length );

        // check this cube for uniformity.
        float theta = data[ y1 ][ x1 ][ z1 ][ 3 ];
        for( int x = x1; x <= x1 + length; ++x )
        {
            for( int y = y1; y <= y1 + length; ++y )
            {
                for( int z = z1; z <= z1 + length; ++z )
                {
                    if( Math.abs( data[ y ][ x ][ z ][ 3 ] - theta ) > _epsilon || true )
                    {
                        int half_length = length / 2;
                        subdivide( x1, y1, z1, half_length, shpace );
                        subdivide( x1 + half_length, y1, z1, half_length, shpace );
                        subdivide( x1, y1 + half_length, z1, half_length, shpace );
                        subdivide( x1 + half_length, y1 + half_length, z1, half_length, shpace );

                        subdivide( x1, y1, z1 + half_length, half_length, shpace );
                        subdivide( x1 + half_length, y1, z1 + half_length, half_length, shpace );
                        subdivide( x1, y1 + half_length, z1 + half_length, half_length, shpace );

                        subdivide( x1 + half_length, y1 + half_length, z1 + half_length, half_length, shpace );

                        return;
                    }
                }
            }
        }
    }

    private void render_subdivide_cube( float x1, float y1, float z1, float length1, GL gl )
    {
        /*
                 float diff = x1;
                 x1 = (float)Math.floor( x1 );
                 y1 = (float)Math.floor( y1 );
                 z1 = (float)Math.floor( z1 );
                 length1 = (float) diff - x1 + length1;
         */

        int x = ( int ) x1;
        int y = ( int ) y1;
        int z = ( int ) z1;
        int length = ( int ) ( length1 + 1 );
        if( x + length >= xDim || y + length >= yDim || z + length >= zDim )
        {
            return;
        }
        subdivide_count++;
        gl.glBegin( GL.GL_TRIANGLE_FAN );

        {
            gl.glColor4f( data[ y ][ x ][ z ][ 0 ], data[ y ][ x ][ z ][ 1 ], data[ y ][ x ][ z ][ 2 ],
                          data[ y ][ x ][ z ][ 3 ] );
            gl.glVertex3f( ( x1 - xDim / 2f ) / xDim, ( y1 - yDim / 2f ) / yDim, ( z1 - zDim / 2f ) / zDim );
        }
        {
            gl.glColor4f( data[ y + length ][ x ][ z ][ 0 ], data[ y + length ][ x ][ z ][ 1 ], data[ y + length ][ x ][ z ][ 2 ],
                          data[ y + length ][ x ][ z ][ 3 ] );
            gl.glVertex3f( ( x1 - xDim / 2f ) / xDim, ( ( y1 + length1 ) - yDim / 2f ) / yDim, ( z1 - zDim / 2f ) / zDim );
        }
        {
            gl.glColor4f( data[ y + length ][ x + length ][ z ][ 0 ], data[ y + length ][ x + length ][ z ][ 1 ],
                          data[ y + length ][ x + length ][ z ][ 2 ],
                          data[ y + length ][ x + length ][ z ][ 3 ] );
            gl.glVertex3f( ( ( x1 + length1 ) - xDim / 2f ) / xDim, ( ( y1 + length1 ) - yDim / 2f ) / yDim, ( z1 - zDim / 2f ) / zDim );
        }
        {
            gl.glColor4f( data[ y ][ x + length ][ z ][ 0 ], data[ y ][ x + length ][ z ][ 1 ], data[ y ][ x + length ][ z ][ 2 ],
                          data[ y ][ x + length ][ z ][ 3 ] );
            gl.glVertex3f( ( ( x1 + length1 ) - xDim / 2f ) / xDim, ( y1 - yDim / 2f ) / yDim, ( z1 - zDim / 2f ) / zDim );
        }
        {

            gl.glColor4f( data[ y ][ x + length ][ z + length ][ 0 ], data[ y ][ x + length ][ z + length ][ 1 ],
                          data[ y ][ x + length ][ z + length ][ 2 ],
                          data[ y ][ x + length ][ z + 1 ][ 3 ] );
            gl.glVertex3f( ( ( x1 + length1 ) - xDim / 2f ) / xDim, ( y1 - yDim / 2f ) / yDim, ( ( z1 + length1 ) - zDim / 2f ) / zDim );

        }

        {
            gl.glColor4f( data[ y ][ x ][ z + length ][ 0 ], data[ y ][ x ][ z + length ][ 1 ], data[ y ][ x ][ z + length ][ 2 ],
                          data[ y ][ x ][ z + length ][ 3 ] );
            gl.glVertex3f( ( x1 - xDim / 2f ) / xDim, ( y1 - yDim / 2f ) / yDim, ( ( z1 + length1 ) - zDim / 2f ) / zDim );

        }

        {
            gl.glColor4f( data[ y + length ][ x ][ z + length ][ 0 ], data[ y + length ][ x ][ z + length ][ 1 ],
                          data[ y + length ][ x ][ z + length ][ 2 ],
                          data[ y + length ][ x ][ z + length ][ 3 ] );
            gl.glVertex3f( ( x1 - xDim / 2f ) / xDim, ( ( y1 + length1 ) - yDim / 2f ) / yDim, ( ( z1 + length1 ) - zDim / 2f ) / zDim );
        }
        {
            gl.glColor4f( data[ y + length ][ x ][ z ][ 0 ], data[ y + length ][ x ][ z ][ 1 ], data[ y + length ][ x ][ z ][ 2 ],
                          data[ y + length ][ x ][ z ][ 3 ] );
            gl.glVertex3f( ( x1 - xDim / 2f ) / xDim, ( ( y1 + length1 ) - yDim / 2f ) / yDim, ( z1 - zDim / 2f ) / zDim );
        }

        gl.glEnd();

    }

    private static int floatToInt( float a )
    {
        int ret = 0;

        float floor_a = Math.round( a );

        return ret;
    }

    private void render_subdivide( float x1, float y1, float z1, float length, GL gl )
    {
        //System.out.println( "Subdividing.." );
        //if( subdivide_count > 10 ) return;

        //System.out.println( subdivide_count );
        //System.out.println( Integer.toString( x1 ) + " "+ Integer.toString( y1 ) + " " +Integer.toString( z1 ) +" "+ Integer.toString( x2 )+" " + Integer.toString( y2 ) +" "+ Integer.toString( z2 ) );

        // base case
        if( length <= 1f + _epsilon )
        {
            render_subdivide_cube( x1, y1, z1, length, gl );
            return;
        }

        // check this cube for uniformity.
        float theta = data[ Math.round( y1 ) ][ Math.round( x1 ) ][ Math.round( z1 ) ][ 3 ];
        for( int x = Math.round( x1 ); x <= x1 + length * 1f && x < xDim; ++x )
        {
            for( int y = Math.round( y1 ); y <= y1 + length * 1f && y < yDim; ++y )
            {
                for( int z = Math.round( z1 ); z <= z1 + length * 1f && z < zDim; ++z )
                {
                    if( Math.abs( data[ y ][ x ][ z ][ 3 ] - theta ) > _epsilon )
                    {
                        float half_length = length / 2f;
                        //System.out.println( "=========== -> " + Integer.toString(  half_length ) );
                        render_subdivide( x1, y1, z1, half_length, gl );
                        render_subdivide( x1 + half_length, y1, z1, half_length, gl );
                        render_subdivide( x1, y1 + half_length, z1, half_length, gl );
                        render_subdivide( x1 + half_length, y1 + half_length, z1, half_length, gl );

                        render_subdivide( x1, y1, z1 + half_length, half_length, gl );
                        render_subdivide( x1 + half_length, y1, z1 + half_length, half_length, gl );
                        render_subdivide( x1, y1 + half_length, z1 + half_length, half_length, gl );
                        render_subdivide( x1 + half_length, y1 + half_length, z1 + half_length, half_length, gl );

                        return;
                    }
                }
            }
        }
        // System.out.println( "OMG SKIPPED" );
        render_subdivide_cube( x1, y1, z1, length, gl );
    }


    private boolean need_to_build_buffers = true;
    public void renderUsingVertexArrays( GLAutoDrawable g )
    {
        GL gl = g.getGL();
        if( need_to_build_buffers )
        {
            //buildBufferz_Subdivision( g, true );
            fillBuffers( g, true );
            buildIndecies( g, true );

            need_to_build_buffers = false;
            gl.glEnableClientState( GL.GL_VERTEX_ARRAY );
            gl.glEnableClientState( GL.GL_COLOR_ARRAY );

            //gl.glVertexPointer( 3, GL.GL_FLOAT, 0, vertices );
            // gl.glColorPointer( 4, GL.GL_BYTE, 0, colors );



            gl.glBindBuffer( GL.GL_ARRAY_BUFFER, VBOvertices[ 0 ] );
            gl.glVertexPointer( 3, GL.GL_FLOAT, 0, 0 );
            gl.glBindBuffer( GL.GL_ARRAY_BUFFER, VBOcolors[ 0 ] );
            gl.glColorPointer( 4, GL.GL_FLOAT, 0, 0 );
            gl.glBindBuffer( GL.GL_ELEMENT_ARRAY_BUFFER, VBOindecies[ 0 ] );

        }

        gl.glDrawElements( GL.GL_QUADS, indecies.capacity(), GL.GL_UNSIGNED_INT, 0 );
        //gl.glDrawArrays( GL.GL_POINTS, 0, num_vertices );



    }


    public void renderUsingArrays1( GLAutoDrawable g )
    {
        GL gl = g.getGL();
        if( need_to_build_buffers )
        {
            fillBuffers( g, false );
            //buildIndecies( g, false );

            need_to_build_buffers = false;
            gl.glEnableClientState( GL.GL_VERTEX_ARRAY );
            gl.glEnableClientState( GL.GL_COLOR_ARRAY );

            gl.glVertexPointer( 3, GL.GL_FLOAT, 0, vertices );
            gl.glColorPointer( 4, GL.GL_FLOAT, 0, colors );
        }

        for( int y = 0; y < data.length; ++y )
        {
            for( int x = 0; x < data[ y ].length; ++x )
            {
                for( int z = 0; z < data[ y ][ x ].length; ++z )
                {
                    //if( data[ y ][ x ][ z ][ 3 ] < 0.1f ) continue;
                    gl.glBegin( GL.GL_TRIANGLE_FAN );
                    if( x < xDim - 1 && y < yDim - 1 )
                    {
                        {
                            gl.glArrayElement( mapFrom3Dto2D( x, y, z ) );
                        }
                        {
                            gl.glArrayElement( mapFrom3Dto2D( x, y + 1, z ) );
                        }
                        {
                            gl.glArrayElement( mapFrom3Dto2D( x + 1, y + 1, z ) );
                        }
                        {
                            gl.glArrayElement( mapFrom3Dto2D( x + 1, y, z ) );
                        }
                    }
                    if( x < xDim - 1 && z < zDim - 1 )
                    {
                        {
                            gl.glArrayElement( mapFrom3Dto2D( x + 1, y, z + 1 ) );

                        }

                        {
                            gl.glArrayElement( mapFrom3Dto2D( x, y, z + 1 ) );

                        }
                    }

                    if( z < zDim - 1 && y < yDim - 1 )
                    {
                        {
                            gl.glArrayElement( mapFrom3Dto2D( x, y + 1, z + 1 ) );
                        }
                        {
                            gl.glArrayElement( mapFrom3Dto2D( x, y + 1, z ) );
                        }
                    }

                    gl.glEnd();

                }
            }
        }

    }

    public void renderUsingQuads( GLAutoDrawable g )
    {
        GL gl = g.getGL();
        gl.glBegin( GL.GL_QUADS );
        for( int y = 0; y < data.length; ++y )
        {
            for( int x = 0; x < data[ y ].length; ++x )
            {
                for( int z = 0; z < data[ y ][ x ].length; ++z )
                {
                    //if( data[ y ][ x ][ z ][ 3 ] < 0.1f ) continue;
                    if( x < xDim - 1 && y < yDim - 1 )
                    {
                        {
                            gl.glColor4d( data[ y ][ x ][ z ][ 0 ], data[ y ][ x ][ z ][ 1 ], data[ y ][ x ][ z ][ 2 ],
                                          data[ y ][ x ][ z ][ 3 ] );
                            gl.glVertex3f( ( x - xDim / 2f ) / xDim, ( y - yDim / 2f ) / yDim, ( z - zDim / 2f ) / zDim );
                        }
                        {
                            gl.glColor4d( data[ y + 1 ][ x ][ z ][ 0 ], data[ y + 1 ][ x ][ z ][ 1 ], data[ y + 1 ][ x ][ z ][ 2 ],
                                          data[ y + 1 ][ x ][ z ][ 3 ] );
                            gl.glVertex3f( ( x - xDim / 2f ) / xDim, ( ( y + 1 ) - yDim / 2f ) / yDim, ( z - zDim / 2f ) / zDim );
                        }
                        {
                            gl.glColor4d( data[ y + 1 ][ x + 1 ][ z ][ 0 ], data[ y + 1 ][ x + 1 ][ z ][ 1 ], data[ y + 1 ][ x + 1 ][ z ][ 2 ],
                                          data[ y + 1 ][ x + 1 ][ z ][ 3 ] );
                            gl.glVertex3f( ( ( x + 1 ) - xDim / 2f ) / xDim, ( ( y + 1 ) - yDim / 2f ) / yDim, ( z - zDim / 2f ) / zDim );
                        }
                        {
                            gl.glColor4d( data[ y ][ x + 1 ][ z ][ 0 ], data[ y ][ x + 1 ][ z ][ 1 ], data[ y ][ x + 1 ][ z ][ 2 ],
                                          data[ y ][ x + 1 ][ z ][ 3 ] );
                            gl.glVertex3f( ( ( x + 1 ) - xDim / 2f ) / xDim, ( y - yDim / 2f ) / yDim, ( z - zDim / 2f ) / zDim );
                        }
                    }
                    if( x < xDim - 1 && z < zDim - 1 )
                    {
                        {
                            gl.glColor4d( data[ y ][ x ][ z ][ 0 ], data[ y ][ x ][ z ][ 1 ], data[ y ][ x ][ z ][ 2 ],
                                          data[ y ][ x ][ z ][ 3 ] );
                            gl.glVertex3f( ( x - xDim / 2f ) / xDim, ( y - yDim / 2f ) / yDim, ( z - zDim / 2f ) / zDim );
                        }
                        {
                            gl.glColor4d( data[ y ][ x ][ z + 1 ][ 0 ], data[ y ][ x ][ z + 1 ][ 1 ], data[ y ][ x ][ z + 1 ][ 2 ],
                                          data[ y ][ x ][ z + 1 ][ 3 ] );
                            gl.glVertex3f( ( x - xDim / 2f ) / xDim, ( y - yDim / 2f ) / yDim, ( ( z + 1 ) - zDim / 2f ) / zDim );

                        }
                        {
                            gl.glColor4d( data[ y ][ x + 1 ][ z + 1 ][ 0 ], data[ y ][ x + 1 ][ z + 1 ][ 1 ], data[ y ][ x + 1 ][ z + 1 ][ 2 ],
                                          data[ y ][ x + 1 ][ z + 1 ][ 3 ] );
                            gl.glVertex3f( ( ( x + 1 ) - xDim / 2f ) / xDim, ( y - yDim / 2f ) / yDim, ( ( z + 1 ) - zDim / 2f ) / zDim );

                        }
                        {
                            gl.glColor4d( data[ y ][ x + 1 ][ z ][ 0 ], data[ y ][ x + 1 ][ z ][ 1 ], data[ y ][ x + 1 ][ z ][ 2 ],
                                          data[ y ][ x + 1 ][ z ][ 3 ] );
                            gl.glVertex3f( ( ( x + 1 ) - xDim / 2f ) / xDim, ( y - yDim / 2f ) / yDim, ( z - zDim / 2f ) / zDim );
                        }
                    }
                    if( z < zDim - 1 && y < yDim - 1 )
                    {
                        {
                            gl.glColor4d( data[ y ][ x ][ z ][ 0 ], data[ y ][ x ][ z ][ 1 ], data[ y ][ x ][ z ][ 2 ],
                                          data[ y ][ x ][ z ][ 3 ] );
                            gl.glVertex3f( ( x - xDim / 2f ) / xDim, ( y - yDim / 2f ) / yDim, ( z - zDim / 2f ) / zDim );
                        }
                        {
                            gl.glColor4d( data[ y + 1 ][ x ][ z ][ 0 ], data[ y + 1 ][ x ][ z ][ 1 ], data[ y + 1 ][ x ][ z ][ 2 ],
                                          data[ y + 1 ][ x ][ z ][ 3 ] );
                            gl.glVertex3f( ( x - xDim / 2f ) / xDim, ( ( y + 1 ) - yDim / 2f ) / yDim, ( z - zDim / 2f ) / zDim );
                        }
                        {
                            gl.glColor4d( data[ y + 1 ][ x ][ z + 1 ][ 0 ], data[ y + 1 ][ x ][ z + 1 ][ 1 ], data[ y + 1 ][ x ][ z + 1 ][ 2 ],
                                          data[ y + 1 ][ x ][ z + 1 ][ 3 ] );
                            gl.glVertex3f( ( x - xDim / 2f ) / xDim, ( ( y + 1 ) - yDim / 2f ) / yDim, ( ( z + 1 ) - zDim / 2f ) / zDim );
                        }
                        {
                            gl.glColor4d( data[ y ][ x ][ z + 1 ][ 0 ], data[ y ][ x ][ z + 1 ][ 1 ], data[ y ][ x ][ z + 1 ][ 2 ],
                                          data[ y ][ x ][ z + 1 ][ 3 ] );
                            gl.glVertex3f( ( x - xDim / 2f ) / xDim, ( y - yDim / 2f ) / yDim, ( ( z + 1 ) - zDim / 2f ) / zDim );

                        }
                    }

                }
            }
        }
        gl.glEnd();

    }


    private GLSLSHaderProgram linear_shader;
    private GLSLSHaderProgram gauss_shader;
    private GLSLSHaderProgram current_shader;


    public void useLinear()
    {
        current_shader = linear_shader;
    }

    public void useGaussian()
    {
        current_shader = gauss_shader;
    }

    private void setUpShaders( GLAutoDrawable gLDrawable )
    {
        try
        {
            gauss_shader = MappingFunction.gaussianDistribution( gLDrawable );

            linear_shader = new GLSLSHaderProgram( gLDrawable );
            linear_shader.addShader( gLDrawable, ShaderProgram.ProgramType.VERTEX_PROGRAM,
                                     Main.working_folder + Main.separator + "scale_vshader.glsl" );
            linear_shader.addShader( gLDrawable, ShaderProgram.ProgramType.FRAGMENT_PROGRAM,
                                     Main.working_folder + Main.separator + "t3d_fragment.glsl" );
            linear_shader.link( gLDrawable );

            current_shader = gauss_shader;
        }
        catch( Exception exc )
        {
            System.err.println( "FAILED TO SET UP SHADERS!!!!" );
            exc.printStackTrace();
        }
    }


    private void setUp3DTexture( GLAutoDrawable g )
    {
        GL gl = g.getGL();
        gl.glDeleteTextures( 1, texture3d_handle, 0 );
        gl.glGenTextures( 1, texture3d_handle, 0 );
        gl.glBindTexture( gl.GL_TEXTURE_3D, texture3d_handle[ 0 ] );
        gl.glTexParameteri( gl.GL_TEXTURE_3D, gl.GL_TEXTURE_MIN_FILTER, gl.GL_LINEAR );
        gl.glTexParameteri( gl.GL_TEXTURE_3D, gl.GL_TEXTURE_MAG_FILTER, gl.GL_LINEAR );
        gl.glTexParameteri( gl.GL_TEXTURE_3D, gl.GL_TEXTURE_WRAP_S, gl.GL_CLAMP_TO_EDGE );
        gl.glTexParameteri( gl.GL_TEXTURE_3D, gl.GL_TEXTURE_WRAP_T, gl.GL_CLAMP_TO_EDGE );
        gl.glTexParameteri( gl.GL_TEXTURE_3D, gl.GL_TEXTURE_WRAP_R, gl.GL_CLAMP_TO_EDGE );
        gl.glTexEnvi( gl.GL_TEXTURE_ENV, gl.GL_TEXTURE_ENV_MODE, gl.GL_REPLACE );

        gl.glTexImage3D( gl.GL_TEXTURE_3D, 0, 1, xDim, yDim, zDim, 0, gl.GL_RED, gl.GL_FLOAT, data_buffer );

        current_shader.use( g );
        if( !current_shader.setUniformVariable( g, "tex3D", 0 ) )
        {
            System.out.println( "Couldn't bind sampler" );
        }

        System.out.println( "Succefully bound 3d texture " );

    }

    public FloatBuffer data_buffer = BufferUtil.newFloatBuffer( 1000 );
    private int texture3d_handle[] =
            {0};


    private boolean texture3d_method_need_to_set_up = true;
    private boolean texture3d_shaders_need_to_set_up = true;

    public int num_slices = 128;

    float matrix[] = new float[16 ];
    public void renderUsing3DTexture( GLAutoDrawable g )
    {
        GL gl = g.getGL();

        if( texture3d_shaders_need_to_set_up )
        {
            setUpShaders( g );
            texture3d_shaders_need_to_set_up = false;
        }

        if( texture3d_method_need_to_set_up )
        {
            setUp3DTexture( g );
            texture3d_method_need_to_set_up = false;

        }

        //Matrix m = new Matrix();

        gl.glPushMatrix();
        {
            gl.glLoadIdentity();
            //gl.glTranslatef( -.5f, -.5f, -.5f );

            gl.glRotatef( -1f * Renderer.aY, 0, 1, 0 );
            gl.glRotatef( -1f * Renderer.aX, 1, 0, 0 );

            gl.glGetFloatv( GL.GL_MODELVIEW_MATRIX, matrix, 0 );

        }
        gl.glPopMatrix();

        current_shader.use( g );
        current_shader.setUniformVariable( g, "scale_factor", Main.getTextureFactor() );
        if( current_shader == gauss_shader )
        {
            float a = 1f / ( Renderer.dev * ( float ) Math.sqrt( 2f * MappingFunction.pi ) );
            current_shader.setUniformVariable( g, "gauss_a", a );
            current_shader.setUniformVariable( g, "gauss_b", Main.getTextureMean() );
            current_shader.setUniformVariable( g, "gauss_c", Main.getTextureDev() );
        }
        current_shader.setUniformVariable( g, "tex3D", 0 );
        current_shader.setUniformVariableMatrix4( g, "inverse_rotation", matrix );
        gl.glBindTexture( gl.GL_TEXTURE_3D, texture3d_handle[ 0 ] );

        float t_pos = 2f;
        float v_pos = .5f;
        float tov = t_pos / v_pos;

        //gl.glUseProgram( 0 );
        gl.glEnable( gl.GL_TEXTURE_3D );
        gl.glBegin( GL.GL_QUADS );
        {
            for( float z = -1f * v_pos; z <= 1f * v_pos; z += ( v_pos * 2 ) / ( float ) num_slices )
            {
                float zPos = z * tov;
                gl.glTexCoord3f( -1f * t_pos, -1f * t_pos, zPos );
                gl.glVertex3f( -1f * v_pos, -1f * v_pos, z );

                gl.glTexCoord3f( t_pos, -1f * t_pos, zPos );
                gl.glVertex3f( v_pos, -1f * v_pos, z );

                gl.glTexCoord3f( t_pos, t_pos, zPos );
                gl.glVertex3f( v_pos, v_pos, z );

                gl.glTexCoord3f( -1f * t_pos, t_pos, zPos );
                gl.glVertex3f( -1f * v_pos, v_pos, z );

            }
        }
        gl.glEnd();
        gl.glDisable( gl.GL_TEXTURE_3D );
    }


    public static final boolean use_display_list = false;
    public static final boolean use_texture_method = true;
    public void render( GLAutoDrawable g )
    {
        if( show )
        {
            GL gl = g.getGL();
            renderUsing3DTexture( g );
        }
    }
}
