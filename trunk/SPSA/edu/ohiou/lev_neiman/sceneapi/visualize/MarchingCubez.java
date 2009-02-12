package edu.ohiou.lev_neiman.sceneapi.visualize;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.util.*;

import javax.media.opengl.*;

import edu.ohiou.lev_neiman.jung.volume_render.Main;

import edu.ohiou.lev_neiman.sceneapi.basic.*;
import edu.ohiou.lev_neiman.sceneapi.visualize.functionz.TransformFunction;
import edu.ohiou.lev_neiman.jung.volume_render.*;

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
public class MarchingCubez
        extends ANode implements Comparable
{
    private static double eps = 1e-20;
    private class CoordinateComporator
            implements Comparator
    {
        private double minx, miny, minz, maxx, maxy, maxz;

        public CoordinateComporator()
        {
            minx = miny = minz = Double.MAX_VALUE;
            maxx = maxy = maxz = Double.MAX_VALUE * -1;
        }


        public int compare( Object o1, Object o2 )
        {
            if( o1 instanceof Coordinate && o2 instanceof Coordinate )
            {
                Coordinate a = ( Coordinate ) o1;
                Coordinate b = ( Coordinate ) o2;
                /*
                                 minx = Math.min( minx, Math.min( a.getX(), b.getX() ) );
                                 miny = Math.min( miny, Math.min( a.getY(), b.getY() ) );
                                 minz = Math.min( minz, Math.min( a.getZ(), b.getZ() ) );
                                 maxx = Math.max( maxx, Math.max( a.getX(), b.getX() ) );
                                 maxy = Math.max( maxy, Math.max( a.getY(), b.getY() ) );
                                 maxz = Math.max( maxz, Math.max( a.getZ(), b.getZ() ) );
                 */

                if( Math.abs( a.getX() - b.getX() ) > eps )
                {
                    return Double.compare( a.getX(), b.getX() );
                }
                if( Math.abs( a.getY() - b.getY() ) > eps )
                {
                    return Double.compare( a.getY(), b.getY() );
                }
                if( Math.abs( a.getZ() - b.getZ() ) > eps )
                {
                    return Double.compare( a.getZ(), b.getZ() );
                }

            }
            return 0;

        }

        public boolean equals( Object obj )
        {
            return false;
        }

        public void print()
        {
            System.out.println( ( new Coordinate( minx, miny, minz ) ).toString() + " :: " + ( new Coordinate( maxx, maxy, maxz ) ).toString() );
        }

    }


    private CoordinateComporator coord_comparator = new CoordinateComporator();

    class TriangleComporator
            implements Comparator
    {
        double[] matrix;

        Map<Triangle, Triangle> t_table = new HashMap<Triangle, Triangle> ();

        public TriangleComporator()
        {

        }

        public void reset()
        {
            t_table.clear();
        }

        public void putMatrix( double[] m )
        {
            /*
                         double inv[] = new double[ 16 ];
                         Matrix.GenerateInverseMatrix4f( inv, m );
             */

            matrix = m;
        }

        public int compare( Object a1, Object b1 )
        {
            Triangle a, b;
            if( t_table.containsKey( a1 ) )
            {
                a = t_table.get( a1 );
            }
            else
            {
                a = ( ( Triangle ) a1 ).clone();
                a.transform( matrix );
                t_table.put( ( Triangle ) a1, a );
            }
            if( t_table.containsKey( b1 ) )
            {
                b = t_table.get( b1 );
            }
            else
            {
                b = ( ( Triangle ) b1 ).clone();

                b.transform( matrix );
                t_table.put( ( Triangle ) b1, b );
            }
            return a.compareTo( b );

        }

    }


    private TriangleComporator t_comporator = new TriangleComporator();


    public double threshold = .01f;
    FloatBuffer data;
    int N;
    boolean[][][] volume;
    TransformFunction func;

    private final static Vector<Coordinate> edge_vertices = new Vector<Coordinate> ();
    private final static Vector<Coordinate> cube_vertices = new Vector<Coordinate> ();

    /**
     * map edge_index and offset coordinate to interpolated coordinate;
     */
    //Map<Pair<Integer, Coordinate>, Coordinate> interpolated_coords = new HashMap<Pair<Integer, Coordinate>, Coordinate> ();

    Map<Coordinate, Coordinate> coordinates;
    Map<Coordinate, Vector<Triangle>> coordinates_triangles;
    Vector<Triangle> mesh = new Vector<Triangle> ();

    Material m = new Material();

    private static GLSLSHaderProgram colorful_shader;
    private static GLSLSHaderProgram metal_shader;
    private static GLSLSHaderProgram plain_shader;
    private static GLSLSHaderProgram phong_shader;

    public enum ShaderType
    {
        FixedFunction, Plain, Colorful, Metal, Phong
    }


    private static ShaderType current_shader = ShaderType.FixedFunction;

    public static void changeShader( ShaderType new_shader )
    {
        current_shader = new_shader;
    }

    private GLContext context;

    public void setColor( Color c )
    {
        m.color[ 0 ] = ( float ) c.getRed() / 255f;
        m.color[ 1 ] = ( float ) c.getGreen() / 255f;
        m.color[ 2 ] = ( float ) c.getBlue() / 255f;

    }

    public Color getColor()
    {
        Color ret = new Color( m.color[ 0 ], m.color[ 1 ], m.color[ 2 ] );

        return ret;
    }

    public Float getTransperency()
    {
        return m.color[ 3 ];
    }

    public void setTransperency( Float f )
    {
        m.color[ 3 ] = f;
    }


    public int getN()
    {
        return N;
    }

    static final int edgeTable[] =
            {
            0x0, 0x109, 0x203, 0x30a, 0x406, 0x50f, 0x605, 0x70c,
            0x80c, 0x905, 0xa0f, 0xb06, 0xc0a, 0xd03, 0xe09, 0xf00,
            0x190, 0x99, 0x393, 0x29a, 0x596, 0x49f, 0x795, 0x69c,
            0x99c, 0x895, 0xb9f, 0xa96, 0xd9a, 0xc93, 0xf99, 0xe90,
            0x230, 0x339, 0x33, 0x13a, 0x636, 0x73f, 0x435, 0x53c,
            0xa3c, 0xb35, 0x83f, 0x936, 0xe3a, 0xf33, 0xc39, 0xd30,
            0x3a0, 0x2a9, 0x1a3, 0xaa, 0x7a6, 0x6af, 0x5a5, 0x4ac,
            0xbac, 0xaa5, 0x9af, 0x8a6, 0xfaa, 0xea3, 0xda9, 0xca0,
            0x460, 0x569, 0x663, 0x76a, 0x66, 0x16f, 0x265, 0x36c,
            0xc6c, 0xd65, 0xe6f, 0xf66, 0x86a, 0x963, 0xa69, 0xb60,
            0x5f0, 0x4f9, 0x7f3, 0x6fa, 0x1f6, 0xff, 0x3f5, 0x2fc,
            0xdfc, 0xcf5, 0xfff, 0xef6, 0x9fa, 0x8f3, 0xbf9, 0xaf0,
            0x650, 0x759, 0x453, 0x55a, 0x256, 0x35f, 0x55, 0x15c,
            0xe5c, 0xf55, 0xc5f, 0xd56, 0xa5a, 0xb53, 0x859, 0x950,
            0x7c0, 0x6c9, 0x5c3, 0x4ca, 0x3c6, 0x2cf, 0x1c5, 0xcc,
            0xfcc, 0xec5, 0xdcf, 0xcc6, 0xbca, 0xac3, 0x9c9, 0x8c0,
            0x8c0, 0x9c9, 0xac3, 0xbca, 0xcc6, 0xdcf, 0xec5, 0xfcc,
            0xcc, 0x1c5, 0x2cf, 0x3c6, 0x4ca, 0x5c3, 0x6c9, 0x7c0,
            0x950, 0x859, 0xb53, 0xa5a, 0xd56, 0xc5f, 0xf55, 0xe5c,
            0x15c, 0x55, 0x35f, 0x256, 0x55a, 0x453, 0x759, 0x650,
            0xaf0, 0xbf9, 0x8f3, 0x9fa, 0xef6, 0xfff, 0xcf5, 0xdfc,
            0x2fc, 0x3f5, 0xff, 0x1f6, 0x6fa, 0x7f3, 0x4f9, 0x5f0,
            0xb60, 0xa69, 0x963, 0x86a, 0xf66, 0xe6f, 0xd65, 0xc6c,
            0x36c, 0x265, 0x16f, 0x66, 0x76a, 0x663, 0x569, 0x460,
            0xca0, 0xda9, 0xea3, 0xfaa, 0x8a6, 0x9af, 0xaa5, 0xbac,
            0x4ac, 0x5a5, 0x6af, 0x7a6, 0xaa, 0x1a3, 0x2a9, 0x3a0,
            0xd30, 0xc39, 0xf33, 0xe3a, 0x936, 0x83f, 0xb35, 0xa3c,
            0x53c, 0x435, 0x73f, 0x636, 0x13a, 0x33, 0x339, 0x230,
            0xe90, 0xf99, 0xc93, 0xd9a, 0xa96, 0xb9f, 0x895, 0x99c,
            0x69c, 0x795, 0x49f, 0x596, 0x29a, 0x393, 0x99, 0x190,
            0xf00, 0xe09, 0xd03, 0xc0a, 0xb06, 0xa0f, 0x905, 0x80c,
            0x70c, 0x605, 0x50f, 0x406, 0x30a, 0x203, 0x109, 0x0};

    static final int triTable[][] =
            {
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {0, 8, 3, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {0, 1, 9, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {1, 8, 3, 9, 8, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {1, 2, 10, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {0, 8, 3, 1, 2, 10, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {9, 2, 10, 0, 2, 9, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {2, 8, 3, 2, 10, 8, 10, 9, 8, -1, -1, -1, -1, -1, -1, -1},
            {3, 11, 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {0, 11, 2, 8, 11, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {1, 9, 0, 2, 3, 11, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {1, 11, 2, 1, 9, 11, 9, 8, 11, -1, -1, -1, -1, -1, -1, -1},
            {3, 10, 1, 11, 10, 3, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {0, 10, 1, 0, 8, 10, 8, 11, 10, -1, -1, -1, -1, -1, -1, -1},
            {3, 9, 0, 3, 11, 9, 11, 10, 9, -1, -1, -1, -1, -1, -1, -1},
            {9, 8, 10, 10, 8, 11, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {4, 7, 8, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {4, 3, 0, 7, 3, 4, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {0, 1, 9, 8, 4, 7, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {4, 1, 9, 4, 7, 1, 7, 3, 1, -1, -1, -1, -1, -1, -1, -1},
            {1, 2, 10, 8, 4, 7, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {3, 4, 7, 3, 0, 4, 1, 2, 10, -1, -1, -1, -1, -1, -1, -1},
            {9, 2, 10, 9, 0, 2, 8, 4, 7, -1, -1, -1, -1, -1, -1, -1},
            {2, 10, 9, 2, 9, 7, 2, 7, 3, 7, 9, 4, -1, -1, -1, -1},
            {8, 4, 7, 3, 11, 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {11, 4, 7, 11, 2, 4, 2, 0, 4, -1, -1, -1, -1, -1, -1, -1},
            {9, 0, 1, 8, 4, 7, 2, 3, 11, -1, -1, -1, -1, -1, -1, -1},
            {4, 7, 11, 9, 4, 11, 9, 11, 2, 9, 2, 1, -1, -1, -1, -1},
            {3, 10, 1, 3, 11, 10, 7, 8, 4, -1, -1, -1, -1, -1, -1, -1},
            {1, 11, 10, 1, 4, 11, 1, 0, 4, 7, 11, 4, -1, -1, -1, -1},
            {4, 7, 8, 9, 0, 11, 9, 11, 10, 11, 0, 3, -1, -1, -1, -1},
            {4, 7, 11, 4, 11, 9, 9, 11, 10, -1, -1, -1, -1, -1, -1, -1},
            {9, 5, 4, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {9, 5, 4, 0, 8, 3, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {0, 5, 4, 1, 5, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {8, 5, 4, 8, 3, 5, 3, 1, 5, -1, -1, -1, -1, -1, -1, -1},
            {1, 2, 10, 9, 5, 4, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {3, 0, 8, 1, 2, 10, 4, 9, 5, -1, -1, -1, -1, -1, -1, -1},
            {5, 2, 10, 5, 4, 2, 4, 0, 2, -1, -1, -1, -1, -1, -1, -1},
            {2, 10, 5, 3, 2, 5, 3, 5, 4, 3, 4, 8, -1, -1, -1, -1},
            {9, 5, 4, 2, 3, 11, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {0, 11, 2, 0, 8, 11, 4, 9, 5, -1, -1, -1, -1, -1, -1, -1},
            {0, 5, 4, 0, 1, 5, 2, 3, 11, -1, -1, -1, -1, -1, -1, -1},
            {2, 1, 5, 2, 5, 8, 2, 8, 11, 4, 8, 5, -1, -1, -1, -1},
            {10, 3, 11, 10, 1, 3, 9, 5, 4, -1, -1, -1, -1, -1, -1, -1},
            {4, 9, 5, 0, 8, 1, 8, 10, 1, 8, 11, 10, -1, -1, -1, -1},
            {5, 4, 0, 5, 0, 11, 5, 11, 10, 11, 0, 3, -1, -1, -1, -1},
            {5, 4, 8, 5, 8, 10, 10, 8, 11, -1, -1, -1, -1, -1, -1, -1},
            {9, 7, 8, 5, 7, 9, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {9, 3, 0, 9, 5, 3, 5, 7, 3, -1, -1, -1, -1, -1, -1, -1},
            {0, 7, 8, 0, 1, 7, 1, 5, 7, -1, -1, -1, -1, -1, -1, -1},
            {1, 5, 3, 3, 5, 7, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {9, 7, 8, 9, 5, 7, 10, 1, 2, -1, -1, -1, -1, -1, -1, -1},
            {10, 1, 2, 9, 5, 0, 5, 3, 0, 5, 7, 3, -1, -1, -1, -1},
            {8, 0, 2, 8, 2, 5, 8, 5, 7, 10, 5, 2, -1, -1, -1, -1},
            {2, 10, 5, 2, 5, 3, 3, 5, 7, -1, -1, -1, -1, -1, -1, -1},
            {7, 9, 5, 7, 8, 9, 3, 11, 2, -1, -1, -1, -1, -1, -1, -1},
            {9, 5, 7, 9, 7, 2, 9, 2, 0, 2, 7, 11, -1, -1, -1, -1},
            {2, 3, 11, 0, 1, 8, 1, 7, 8, 1, 5, 7, -1, -1, -1, -1},
            {11, 2, 1, 11, 1, 7, 7, 1, 5, -1, -1, -1, -1, -1, -1, -1},
            {9, 5, 8, 8, 5, 7, 10, 1, 3, 10, 3, 11, -1, -1, -1, -1},
            {5, 7, 0, 5, 0, 9, 7, 11, 0, 1, 0, 10, 11, 10, 0, -1},
            {11, 10, 0, 11, 0, 3, 10, 5, 0, 8, 0, 7, 5, 7, 0, -1},
            {11, 10, 5, 7, 11, 5, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {10, 6, 5, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {0, 8, 3, 5, 10, 6, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {9, 0, 1, 5, 10, 6, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {1, 8, 3, 1, 9, 8, 5, 10, 6, -1, -1, -1, -1, -1, -1, -1},
            {1, 6, 5, 2, 6, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {1, 6, 5, 1, 2, 6, 3, 0, 8, -1, -1, -1, -1, -1, -1, -1},
            {9, 6, 5, 9, 0, 6, 0, 2, 6, -1, -1, -1, -1, -1, -1, -1},
            {5, 9, 8, 5, 8, 2, 5, 2, 6, 3, 2, 8, -1, -1, -1, -1},
            {2, 3, 11, 10, 6, 5, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {11, 0, 8, 11, 2, 0, 10, 6, 5, -1, -1, -1, -1, -1, -1, -1},
            {0, 1, 9, 2, 3, 11, 5, 10, 6, -1, -1, -1, -1, -1, -1, -1},
            {5, 10, 6, 1, 9, 2, 9, 11, 2, 9, 8, 11, -1, -1, -1, -1},
            {6, 3, 11, 6, 5, 3, 5, 1, 3, -1, -1, -1, -1, -1, -1, -1},
            {0, 8, 11, 0, 11, 5, 0, 5, 1, 5, 11, 6, -1, -1, -1, -1},
            {3, 11, 6, 0, 3, 6, 0, 6, 5, 0, 5, 9, -1, -1, -1, -1},
            {6, 5, 9, 6, 9, 11, 11, 9, 8, -1, -1, -1, -1, -1, -1, -1},
            {5, 10, 6, 4, 7, 8, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {4, 3, 0, 4, 7, 3, 6, 5, 10, -1, -1, -1, -1, -1, -1, -1},
            {1, 9, 0, 5, 10, 6, 8, 4, 7, -1, -1, -1, -1, -1, -1, -1},
            {10, 6, 5, 1, 9, 7, 1, 7, 3, 7, 9, 4, -1, -1, -1, -1},
            {6, 1, 2, 6, 5, 1, 4, 7, 8, -1, -1, -1, -1, -1, -1, -1},
            {1, 2, 5, 5, 2, 6, 3, 0, 4, 3, 4, 7, -1, -1, -1, -1},
            {8, 4, 7, 9, 0, 5, 0, 6, 5, 0, 2, 6, -1, -1, -1, -1},
            {7, 3, 9, 7, 9, 4, 3, 2, 9, 5, 9, 6, 2, 6, 9, -1},
            {3, 11, 2, 7, 8, 4, 10, 6, 5, -1, -1, -1, -1, -1, -1, -1},
            {5, 10, 6, 4, 7, 2, 4, 2, 0, 2, 7, 11, -1, -1, -1, -1},
            {0, 1, 9, 4, 7, 8, 2, 3, 11, 5, 10, 6, -1, -1, -1, -1},
            {9, 2, 1, 9, 11, 2, 9, 4, 11, 7, 11, 4, 5, 10, 6, -1},
            {8, 4, 7, 3, 11, 5, 3, 5, 1, 5, 11, 6, -1, -1, -1, -1},
            {5, 1, 11, 5, 11, 6, 1, 0, 11, 7, 11, 4, 0, 4, 11, -1},
            {0, 5, 9, 0, 6, 5, 0, 3, 6, 11, 6, 3, 8, 4, 7, -1},
            {6, 5, 9, 6, 9, 11, 4, 7, 9, 7, 11, 9, -1, -1, -1, -1},
            {10, 4, 9, 6, 4, 10, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {4, 10, 6, 4, 9, 10, 0, 8, 3, -1, -1, -1, -1, -1, -1, -1},
            {10, 0, 1, 10, 6, 0, 6, 4, 0, -1, -1, -1, -1, -1, -1, -1},
            {8, 3, 1, 8, 1, 6, 8, 6, 4, 6, 1, 10, -1, -1, -1, -1},
            {1, 4, 9, 1, 2, 4, 2, 6, 4, -1, -1, -1, -1, -1, -1, -1},
            {3, 0, 8, 1, 2, 9, 2, 4, 9, 2, 6, 4, -1, -1, -1, -1},
            {0, 2, 4, 4, 2, 6, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {8, 3, 2, 8, 2, 4, 4, 2, 6, -1, -1, -1, -1, -1, -1, -1},
            {10, 4, 9, 10, 6, 4, 11, 2, 3, -1, -1, -1, -1, -1, -1, -1},
            {0, 8, 2, 2, 8, 11, 4, 9, 10, 4, 10, 6, -1, -1, -1, -1},
            {3, 11, 2, 0, 1, 6, 0, 6, 4, 6, 1, 10, -1, -1, -1, -1},
            {6, 4, 1, 6, 1, 10, 4, 8, 1, 2, 1, 11, 8, 11, 1, -1},
            {9, 6, 4, 9, 3, 6, 9, 1, 3, 11, 6, 3, -1, -1, -1, -1},
            {8, 11, 1, 8, 1, 0, 11, 6, 1, 9, 1, 4, 6, 4, 1, -1},
            {3, 11, 6, 3, 6, 0, 0, 6, 4, -1, -1, -1, -1, -1, -1, -1},
            {6, 4, 8, 11, 6, 8, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {7, 10, 6, 7, 8, 10, 8, 9, 10, -1, -1, -1, -1, -1, -1, -1},
            {0, 7, 3, 0, 10, 7, 0, 9, 10, 6, 7, 10, -1, -1, -1, -1},
            {10, 6, 7, 1, 10, 7, 1, 7, 8, 1, 8, 0, -1, -1, -1, -1},
            {10, 6, 7, 10, 7, 1, 1, 7, 3, -1, -1, -1, -1, -1, -1, -1},
            {1, 2, 6, 1, 6, 8, 1, 8, 9, 8, 6, 7, -1, -1, -1, -1},
            {2, 6, 9, 2, 9, 1, 6, 7, 9, 0, 9, 3, 7, 3, 9, -1},
            {7, 8, 0, 7, 0, 6, 6, 0, 2, -1, -1, -1, -1, -1, -1, -1},
            {7, 3, 2, 6, 7, 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {2, 3, 11, 10, 6, 8, 10, 8, 9, 8, 6, 7, -1, -1, -1, -1},
            {2, 0, 7, 2, 7, 11, 0, 9, 7, 6, 7, 10, 9, 10, 7, -1},
            {1, 8, 0, 1, 7, 8, 1, 10, 7, 6, 7, 10, 2, 3, 11, -1},
            {11, 2, 1, 11, 1, 7, 10, 6, 1, 6, 7, 1, -1, -1, -1, -1},
            {8, 9, 6, 8, 6, 7, 9, 1, 6, 11, 6, 3, 1, 3, 6, -1},
            {0, 9, 1, 11, 6, 7, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {7, 8, 0, 7, 0, 6, 3, 11, 0, 11, 6, 0, -1, -1, -1, -1},
            {7, 11, 6, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {7, 6, 11, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {3, 0, 8, 11, 7, 6, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {0, 1, 9, 11, 7, 6, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {8, 1, 9, 8, 3, 1, 11, 7, 6, -1, -1, -1, -1, -1, -1, -1},
            {10, 1, 2, 6, 11, 7, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {1, 2, 10, 3, 0, 8, 6, 11, 7, -1, -1, -1, -1, -1, -1, -1},
            {2, 9, 0, 2, 10, 9, 6, 11, 7, -1, -1, -1, -1, -1, -1, -1},
            {6, 11, 7, 2, 10, 3, 10, 8, 3, 10, 9, 8, -1, -1, -1, -1},
            {7, 2, 3, 6, 2, 7, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {7, 0, 8, 7, 6, 0, 6, 2, 0, -1, -1, -1, -1, -1, -1, -1},
            {2, 7, 6, 2, 3, 7, 0, 1, 9, -1, -1, -1, -1, -1, -1, -1},
            {1, 6, 2, 1, 8, 6, 1, 9, 8, 8, 7, 6, -1, -1, -1, -1},
            {10, 7, 6, 10, 1, 7, 1, 3, 7, -1, -1, -1, -1, -1, -1, -1},
            {10, 7, 6, 1, 7, 10, 1, 8, 7, 1, 0, 8, -1, -1, -1, -1},
            {0, 3, 7, 0, 7, 10, 0, 10, 9, 6, 10, 7, -1, -1, -1, -1},
            {7, 6, 10, 7, 10, 8, 8, 10, 9, -1, -1, -1, -1, -1, -1, -1},
            {6, 8, 4, 11, 8, 6, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {3, 6, 11, 3, 0, 6, 0, 4, 6, -1, -1, -1, -1, -1, -1, -1},
            {8, 6, 11, 8, 4, 6, 9, 0, 1, -1, -1, -1, -1, -1, -1, -1},
            {9, 4, 6, 9, 6, 3, 9, 3, 1, 11, 3, 6, -1, -1, -1, -1},
            {6, 8, 4, 6, 11, 8, 2, 10, 1, -1, -1, -1, -1, -1, -1, -1},
            {1, 2, 10, 3, 0, 11, 0, 6, 11, 0, 4, 6, -1, -1, -1, -1},
            {4, 11, 8, 4, 6, 11, 0, 2, 9, 2, 10, 9, -1, -1, -1, -1},
            {10, 9, 3, 10, 3, 2, 9, 4, 3, 11, 3, 6, 4, 6, 3, -1},
            {8, 2, 3, 8, 4, 2, 4, 6, 2, -1, -1, -1, -1, -1, -1, -1},
            {0, 4, 2, 4, 6, 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {1, 9, 0, 2, 3, 4, 2, 4, 6, 4, 3, 8, -1, -1, -1, -1},
            {1, 9, 4, 1, 4, 2, 2, 4, 6, -1, -1, -1, -1, -1, -1, -1},
            {8, 1, 3, 8, 6, 1, 8, 4, 6, 6, 10, 1, -1, -1, -1, -1},
            {10, 1, 0, 10, 0, 6, 6, 0, 4, -1, -1, -1, -1, -1, -1, -1},
            {4, 6, 3, 4, 3, 8, 6, 10, 3, 0, 3, 9, 10, 9, 3, -1},
            {10, 9, 4, 6, 10, 4, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {4, 9, 5, 7, 6, 11, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {0, 8, 3, 4, 9, 5, 11, 7, 6, -1, -1, -1, -1, -1, -1, -1},
            {5, 0, 1, 5, 4, 0, 7, 6, 11, -1, -1, -1, -1, -1, -1, -1},
            {11, 7, 6, 8, 3, 4, 3, 5, 4, 3, 1, 5, -1, -1, -1, -1},
            {9, 5, 4, 10, 1, 2, 7, 6, 11, -1, -1, -1, -1, -1, -1, -1},
            {6, 11, 7, 1, 2, 10, 0, 8, 3, 4, 9, 5, -1, -1, -1, -1},
            {7, 6, 11, 5, 4, 10, 4, 2, 10, 4, 0, 2, -1, -1, -1, -1},
            {3, 4, 8, 3, 5, 4, 3, 2, 5, 10, 5, 2, 11, 7, 6, -1},
            {7, 2, 3, 7, 6, 2, 5, 4, 9, -1, -1, -1, -1, -1, -1, -1},
            {9, 5, 4, 0, 8, 6, 0, 6, 2, 6, 8, 7, -1, -1, -1, -1},
            {3, 6, 2, 3, 7, 6, 1, 5, 0, 5, 4, 0, -1, -1, -1, -1},
            {6, 2, 8, 6, 8, 7, 2, 1, 8, 4, 8, 5, 1, 5, 8, -1},
            {9, 5, 4, 10, 1, 6, 1, 7, 6, 1, 3, 7, -1, -1, -1, -1},
            {1, 6, 10, 1, 7, 6, 1, 0, 7, 8, 7, 0, 9, 5, 4, -1},
            {4, 0, 10, 4, 10, 5, 0, 3, 10, 6, 10, 7, 3, 7, 10, -1},
            {7, 6, 10, 7, 10, 8, 5, 4, 10, 4, 8, 10, -1, -1, -1, -1},
            {6, 9, 5, 6, 11, 9, 11, 8, 9, -1, -1, -1, -1, -1, -1, -1},
            {3, 6, 11, 0, 6, 3, 0, 5, 6, 0, 9, 5, -1, -1, -1, -1},
            {0, 11, 8, 0, 5, 11, 0, 1, 5, 5, 6, 11, -1, -1, -1, -1},
            {6, 11, 3, 6, 3, 5, 5, 3, 1, -1, -1, -1, -1, -1, -1, -1},
            {1, 2, 10, 9, 5, 11, 9, 11, 8, 11, 5, 6, -1, -1, -1, -1},
            {0, 11, 3, 0, 6, 11, 0, 9, 6, 5, 6, 9, 1, 2, 10, -1},
            {11, 8, 5, 11, 5, 6, 8, 0, 5, 10, 5, 2, 0, 2, 5, -1},
            {6, 11, 3, 6, 3, 5, 2, 10, 3, 10, 5, 3, -1, -1, -1, -1},
            {5, 8, 9, 5, 2, 8, 5, 6, 2, 3, 8, 2, -1, -1, -1, -1},
            {9, 5, 6, 9, 6, 0, 0, 6, 2, -1, -1, -1, -1, -1, -1, -1},
            {1, 5, 8, 1, 8, 0, 5, 6, 8, 3, 8, 2, 6, 2, 8, -1},
            {1, 5, 6, 2, 1, 6, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {1, 3, 6, 1, 6, 10, 3, 8, 6, 5, 6, 9, 8, 9, 6, -1},
            {10, 1, 0, 10, 0, 6, 9, 5, 0, 5, 6, 0, -1, -1, -1, -1},
            {0, 3, 8, 5, 6, 10, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {10, 5, 6, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {11, 5, 10, 7, 5, 11, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {11, 5, 10, 11, 7, 5, 8, 3, 0, -1, -1, -1, -1, -1, -1, -1},
            {5, 11, 7, 5, 10, 11, 1, 9, 0, -1, -1, -1, -1, -1, -1, -1},
            {10, 7, 5, 10, 11, 7, 9, 8, 1, 8, 3, 1, -1, -1, -1, -1},
            {11, 1, 2, 11, 7, 1, 7, 5, 1, -1, -1, -1, -1, -1, -1, -1},
            {0, 8, 3, 1, 2, 7, 1, 7, 5, 7, 2, 11, -1, -1, -1, -1},
            {9, 7, 5, 9, 2, 7, 9, 0, 2, 2, 11, 7, -1, -1, -1, -1},
            {7, 5, 2, 7, 2, 11, 5, 9, 2, 3, 2, 8, 9, 8, 2, -1},
            {2, 5, 10, 2, 3, 5, 3, 7, 5, -1, -1, -1, -1, -1, -1, -1},
            {8, 2, 0, 8, 5, 2, 8, 7, 5, 10, 2, 5, -1, -1, -1, -1},
            {9, 0, 1, 5, 10, 3, 5, 3, 7, 3, 10, 2, -1, -1, -1, -1},
            {9, 8, 2, 9, 2, 1, 8, 7, 2, 10, 2, 5, 7, 5, 2, -1},
            {1, 3, 5, 3, 7, 5, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {0, 8, 7, 0, 7, 1, 1, 7, 5, -1, -1, -1, -1, -1, -1, -1},
            {9, 0, 3, 9, 3, 5, 5, 3, 7, -1, -1, -1, -1, -1, -1, -1},
            {9, 8, 7, 5, 9, 7, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {5, 8, 4, 5, 10, 8, 10, 11, 8, -1, -1, -1, -1, -1, -1, -1},
            {5, 0, 4, 5, 11, 0, 5, 10, 11, 11, 3, 0, -1, -1, -1, -1},
            {0, 1, 9, 8, 4, 10, 8, 10, 11, 10, 4, 5, -1, -1, -1, -1},
            {10, 11, 4, 10, 4, 5, 11, 3, 4, 9, 4, 1, 3, 1, 4, -1},
            {2, 5, 1, 2, 8, 5, 2, 11, 8, 4, 5, 8, -1, -1, -1, -1},
            {0, 4, 11, 0, 11, 3, 4, 5, 11, 2, 11, 1, 5, 1, 11, -1},
            {0, 2, 5, 0, 5, 9, 2, 11, 5, 4, 5, 8, 11, 8, 5, -1},
            {9, 4, 5, 2, 11, 3, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {2, 5, 10, 3, 5, 2, 3, 4, 5, 3, 8, 4, -1, -1, -1, -1},
            {5, 10, 2, 5, 2, 4, 4, 2, 0, -1, -1, -1, -1, -1, -1, -1},
            {3, 10, 2, 3, 5, 10, 3, 8, 5, 4, 5, 8, 0, 1, 9, -1},
            {5, 10, 2, 5, 2, 4, 1, 9, 2, 9, 4, 2, -1, -1, -1, -1},
            {8, 4, 5, 8, 5, 3, 3, 5, 1, -1, -1, -1, -1, -1, -1, -1},
            {0, 4, 5, 1, 0, 5, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {8, 4, 5, 8, 5, 3, 9, 0, 5, 0, 3, 5, -1, -1, -1, -1},
            {9, 4, 5, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {4, 11, 7, 4, 9, 11, 9, 10, 11, -1, -1, -1, -1, -1, -1, -1},
            {0, 8, 3, 4, 9, 7, 9, 11, 7, 9, 10, 11, -1, -1, -1, -1},
            {1, 10, 11, 1, 11, 4, 1, 4, 0, 7, 4, 11, -1, -1, -1, -1},
            {3, 1, 4, 3, 4, 8, 1, 10, 4, 7, 4, 11, 10, 11, 4, -1},
            {4, 11, 7, 9, 11, 4, 9, 2, 11, 9, 1, 2, -1, -1, -1, -1},
            {9, 7, 4, 9, 11, 7, 9, 1, 11, 2, 11, 1, 0, 8, 3, -1},
            {11, 7, 4, 11, 4, 2, 2, 4, 0, -1, -1, -1, -1, -1, -1, -1},
            {11, 7, 4, 11, 4, 2, 8, 3, 4, 3, 2, 4, -1, -1, -1, -1},
            {2, 9, 10, 2, 7, 9, 2, 3, 7, 7, 4, 9, -1, -1, -1, -1},
            {9, 10, 7, 9, 7, 4, 10, 2, 7, 8, 7, 0, 2, 0, 7, -1},
            {3, 7, 10, 3, 10, 2, 7, 4, 10, 1, 10, 0, 4, 0, 10, -1},
            {1, 10, 2, 8, 7, 4, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {4, 9, 1, 4, 1, 7, 7, 1, 3, -1, -1, -1, -1, -1, -1, -1},
            {4, 9, 1, 4, 1, 7, 0, 8, 1, 8, 7, 1, -1, -1, -1, -1},
            {4, 0, 3, 7, 4, 3, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {4, 8, 7, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {9, 10, 8, 10, 11, 8, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {3, 0, 9, 3, 9, 11, 11, 9, 10, -1, -1, -1, -1, -1, -1, -1},
            {0, 1, 10, 0, 10, 8, 8, 10, 11, -1, -1, -1, -1, -1, -1, -1},
            {3, 1, 10, 11, 3, 10, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {1, 2, 11, 1, 11, 9, 9, 11, 8, -1, -1, -1, -1, -1, -1, -1},
            {3, 0, 9, 3, 9, 11, 1, 2, 9, 2, 11, 9, -1, -1, -1, -1},
            {0, 2, 11, 8, 0, 11, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {3, 2, 11, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {2, 3, 8, 2, 8, 10, 10, 8, 9, -1, -1, -1, -1, -1, -1, -1},
            {9, 10, 2, 0, 9, 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {2, 3, 8, 2, 8, 10, 0, 1, 8, 1, 10, 8, -1, -1, -1, -1},
            {1, 10, 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {1, 3, 8, 9, 1, 8, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {0, 9, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {0, 3, 8, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1}
    };

    static final double unit_cube_size = 1;

    static
    {
        edge_vertices.add( new Coordinate( .5, 1, 1 ) ); // 0
        edge_vertices.add( new Coordinate( 1, 1, .5 ) ); // 1
        edge_vertices.add( new Coordinate( .5, 1, 0 ) ); // 2
        edge_vertices.add( new Coordinate( 0, 1, .5 ) ); // 3
        edge_vertices.add( new Coordinate( .5, 0, 1 ) ); // 4
        edge_vertices.add( new Coordinate( 1, 0, .5 ) ); // 5
        edge_vertices.add( new Coordinate( .5, 0, 0 ) ); // 6
        edge_vertices.add( new Coordinate( 0, 0, .5 ) ); // 7
        edge_vertices.add( new Coordinate( 0, .5, 1 ) ); // 8
        edge_vertices.add( new Coordinate( 1, .5, 1 ) ); // 9
        edge_vertices.add( new Coordinate( 1, .5, 0 ) ); // 10
        edge_vertices.add( new Coordinate( 0, .5, 0 ) ); // 11

        cube_vertices.add( new Coordinate( 0, unit_cube_size, unit_cube_size ) ); // 0
        cube_vertices.add( new Coordinate( unit_cube_size, unit_cube_size, unit_cube_size ) ); // 1
        cube_vertices.add( new Coordinate( unit_cube_size, unit_cube_size, 0 ) ); // 2
        cube_vertices.add( new Coordinate( 0, unit_cube_size, 0 ) ); // 3
        cube_vertices.add( new Coordinate( 0, 0, unit_cube_size ) ); // 4
        cube_vertices.add( new Coordinate( unit_cube_size, 0, unit_cube_size ) ); // 5
        cube_vertices.add( new Coordinate( unit_cube_size, 0, 0 ) ); // 6
        cube_vertices.add( new Coordinate( 0, 0, 0 ) ); // 7
    }


    public MarchingCubez( FloatBuffer data, TransformFunction func, double threshold )
    {
        String t_lab = BottomBar.getLabelText();
        BottomBar.setLabelText( "Creating new Iso-Surface...." );
        BottomBar.setProgressVal( 0 );

        coordinates = new TreeMap<Coordinate, Coordinate> ( coord_comparator );
        coordinates_triangles = new TreeMap<Coordinate, Vector<Triangle>> ( coord_comparator );

        //coordinates = new HashMap< Coordinate, Coordinate > ();
        //coordinates_triangles = new HashMap< Coordinate, Vector< Triangle > >();

        //m.color[ 3 ] = .5f;
        long start = System.nanoTime();
        System.out.println( "Creating new IsoSurface" );
        try
        {
            this.threshold = threshold;
            this.data = data;
            this.func = func;
            N = ( int ) Math.pow( ( double ) data.capacity(), 1f / 3f );

            System.out.println( "DIMENSION = " + Integer.toString( N ) );
            //makeEdgeVertices();
            analyzeData();
            BottomBar.setProgressVal( 20 );
            buildMesh();


            System.err.println( "calculating normals" );
            calculateNormals();

            System.err.println( "done calculating normals" );

        }
        catch( Exception exc )
        {
            exc.printStackTrace();
            return;
        }
        System.out.println( "Succefuly created iso surface" );

        System.out.println( "This took - " + Long.toString( System.nanoTime() - start ) );
        coord_comparator.print();
        BottomBar.setLabelText( t_lab );
        BottomBar.setProgressVal( 0 );
    }

    private void clearData()
    {
        volume = null;
        this.mesh = null;
        this.coordinates = null;
        this.coordinates_triangles = null;

    }


    private int from3Dto1D( int x, int y, int z )
    {

        return x + y * N + z * N * N;
    }

    private int num_included;
    private void analyzeData()
    {
        volume = new boolean[N ][ N ][ N ];
        int c = 0;
        for( int y = 0; y < N; ++y )
        {
            for( int x = 0; x < N; ++x )
            {
                for( int z = 0; z < N; ++z )
                {
                    int i = x + y * N + z * N * N;
                    //int i = from3Dto1D( x / 2, y / 2, z /2 );
                    float f = data.get( i );
                    f = func.transform( f );
                    if( f > threshold )
                    {
                        volume[ y ][ x ][ z ] = true;
                        c++;
                    }
                    else
                    {
                        volume[ y ][ x ][ z ] = false;
                    }
                }
            }
        }
        num_included = c;
        System.out.println( "THERES " + Integer.toString( c ) );
    }

    public int getNumIncluded()
    {
        return num_included;
    }

    private Coordinate transformCoord( Coordinate b )
    {
        Coordinate c = new Coordinate();
        //  ( x - xDim / 2f ) / xDim
        c.setX( ( b.getX() - N / 2 ) / N );
        c.setY( ( b.getY() - N / 2 ) / N );
        c.setZ( ( b.getZ() - N / 2 ) / N );
        return c;
    }

    private void transformTriangle( Triangle t, Set<Coordinate> s )
    {
        if( !s.contains( t.a ) )
        {
            transformCoord( t.a );
            s.add( t.a );
        }
        if( !s.contains( t.b ) )
        {
            transformCoord( t.b );
            s.add( t.b );
        }
        if( !s.contains( t.c ) )
        {
            transformCoord( t.c );
            s.add( t.c );
        }
    }


    private Coordinate interpolate( final Coordinate p1, final Coordinate p2, float f1, float f2 )
    {
        Coordinate ret = new Coordinate( 0, 0, 0 );

        double mu = 0;
        /*
                 if( Math.abs( threshold - f1 ) < _epsilon )
                 {
            return Coordinate.add( ret, p1 );
                 }
                 else if( Math.abs( threshold - f2 ) < _epsilon )
                 {
            return Coordinate.add( ret, p2 );
                 }
                 else if( Math.abs( f1 - f2 ) < _epsilon )
                 {
            return Coordinate.add( ret, p1 );
                 }
                 else
         */
        {
            if( f2 > 1 || f1 > 1 || f2 < 0 || f1 < 0 )
            {
                System.out.println( "OMG -> " + Float.toString( f1 ) + " " + Float.toString( f2 ) );
            }
            mu = ( threshold - f1 ) / ( f2 - f1 );
            //mu = Func.roundOff( mu, 4 );
            //mu = f1 / ( f2 );

            //ret = Coordinate.interpolate( p1, p2, mu );
            //if( ret.getX() < 0 || ret.getY() < 0 || ret.getZ() < 0 ){}
            if( mu > ( double ) 1 || mu < ( double ) 0 || ret.getX() < 0 || ret.getY() < 0 || ret.getZ() < 0 )
            {
                System.err.println( "HOLY CRAP" + " " + Double.toString( ret.getLength() ) + " " + Double.toString( mu ) + " " +
                                    Float.toString( f1 ) + " " + Float.toString( f2 ) );
            }

            ret.setX( p1.getX() + mu * ( p2.getX() - p1.getX() ) );
            ret.setY( p1.getY() + mu * ( p2.getY() - p1.getY() ) );
            ret.setZ( p1.getZ() + mu * ( p2.getZ() - p1.getZ() ) );

            //ret.setX( ( p1.getX() + p2.getX() ) / 2 );
            //ret.setY( ( p1.getY() + p2.getY() ) / 2 );
            //ret.setZ( ( p1.getZ() + p2.getZ() ) / 2 );


            /*
                         ret.setX( p1.getX() * mu + p2.getX() * ( 1 - mu ) );
                         ret.setY( p1.getY() * mu + p2.getY() * ( 1 - mu ) );
                         ret.setZ( p1.getZ() * mu + p2.getZ() * ( 1 - mu ) );
             */
        }

        /*
           float alpha = -1f / ( min-max );
           float beta = 1f - alpha * max;
         */
        Coordinate.roundOff( ret, 5 );

        return ret;
    }


    private float getVal( Coordinate c, int x, int y, int z )
    {
        try
        {
            return func.transform( data.get( from3Dto1D( ( int ) ( c.getX() / unit_cube_size ) + x,
                    ( int ) ( c.getY() / unit_cube_size ) + y, ( int ) ( c.getZ() / unit_cube_size ) + z ) ) );

            //return data.get( from3Dto1D( ( int ) ( c.getX() / unit_cube_size ) + x, ( int ) ( c.getY() / unit_cube_size ) + y, ( int ) ( c.getZ() / unit_cube_size ) + z ) );
        }
        catch( Exception exc )
        {
            exc.printStackTrace();
            System.err.println( c.getX() );
            System.err.println( c.getY() );
            System.err.println( c.getZ() );
            System.err.println( x );
            System.err.println( y );
            System.err.println( z );
            //System.exit( 0 );
            return 0;
        }
    }

    private Coordinate interpolate( int edge_index, int x, int y, int z )
    {

        Coordinate t;
        switch( edge_index )
        {
            case 0:
                t = interpolate( cube_vertices.get( 0 ), cube_vertices.get( 1 ), getVal( cube_vertices.get( 0 ), x, y, z ),
                                 getVal( cube_vertices.get( 1 ), x, y, z ) );
                break;
            case 1:
                t = interpolate( cube_vertices.get( 1 ), cube_vertices.get( 2 ), getVal( cube_vertices.get( 1 ), x, y, z ),
                                 getVal( cube_vertices.get( 2 ), x, y, z ) );
                break;
            case 2:
                t = interpolate( cube_vertices.get( 2 ), cube_vertices.get( 3 ), getVal( cube_vertices.get( 2 ), x, y, z ),
                                 getVal( cube_vertices.get( 3 ), x, y, z ) );
                break;
            case 3:
                t = interpolate( cube_vertices.get( 0 ), cube_vertices.get( 3 ), getVal( cube_vertices.get( 0 ), x, y, z ),
                                 getVal( cube_vertices.get( 3 ), x, y, z ) );
                break;
            case 4:
                t = interpolate( cube_vertices.get( 4 ), cube_vertices.get( 5 ), getVal( cube_vertices.get( 4 ), x, y, z ),
                                 getVal( cube_vertices.get( 5 ), x, y, z ) );
                break;
            case 5:
                t = interpolate( cube_vertices.get( 5 ), cube_vertices.get( 6 ), getVal( cube_vertices.get( 5 ), x, y, z ),
                                 getVal( cube_vertices.get( 6 ), x, y, z ) );
                break;
            case 6:
                t = interpolate( cube_vertices.get( 6 ), cube_vertices.get( 7 ), getVal( cube_vertices.get( 6 ), x, y, z ),
                                 getVal( cube_vertices.get( 7 ), x, y, z ) );
                break;
            case 7:
                t = interpolate( cube_vertices.get( 7 ), cube_vertices.get( 4 ), getVal( cube_vertices.get( 7 ), x, y, z ),
                                 getVal( cube_vertices.get( 4 ), x, y, z ) );
                break;
            case 8:
                t = interpolate( cube_vertices.get( 4 ), cube_vertices.get( 0 ), getVal( cube_vertices.get( 4 ), x, y, z ),
                                 getVal( cube_vertices.get( 0 ), x, y, z ) );
                break;
            case 9:
                t = interpolate( cube_vertices.get( 5 ), cube_vertices.get( 1 ), getVal( cube_vertices.get( 5 ), x, y, z ),
                                 getVal( cube_vertices.get( 1 ), x, y, z ) );
                break;
            case 10:
                t = interpolate( cube_vertices.get( 6 ), cube_vertices.get( 2 ), getVal( cube_vertices.get( 6 ), x, y, z ),
                                 getVal( cube_vertices.get( 2 ), x, y, z ) );
                break;
            default:
                t = interpolate( cube_vertices.get( 7 ), cube_vertices.get( 3 ), getVal( cube_vertices.get( 7 ), x, y, z ),
                                 getVal( cube_vertices.get( 3 ), x, y, z ) );

        }
        return t;
    }

    private int num_triangles = 0;

    private void buildMesh()
    {
        for( int y = 0; y < N - 1; ++y )
        {
            if( y % N / 10 == 0 )
            {
                BottomBar.setProgressVal( BottomBar.getProgressVal() + 6 );
            }
            for( int x = 0; x < N - 1; ++x )
            {
                for( int z = 0; z < N - 1; ++z )
                {
                    int cubeIndex = 0;
                    if( volume[ y + 1 ][ x ][ z + 1 ] )
                    {
                        cubeIndex |= 1;
                    }
                    if( volume[ y + 1 ][ x + 1 ][ z + 1 ] )
                    {
                        cubeIndex |= 2;
                    }
                    if( volume[ y + 1 ][ x + 1 ][ z ] )
                    {
                        cubeIndex |= 4;
                    }
                    if( volume[ y + 1 ][ x ][ z ] )
                    {
                        cubeIndex |= 8;
                    }
                    if( volume[ y ][ x ][ z + 1 ] )
                    {
                        cubeIndex |= 16;
                    }
                    if( volume[ y ][ x + 1 ][ z + 1 ] )
                    {
                        cubeIndex |= 32;
                    }
                    if( volume[ y ][ x + 1 ][ z ] )
                    {
                        cubeIndex |= 64;
                    }
                    if( volume[ y ][ x ][ z ] )
                    {
                        cubeIndex |= 128;
                    }
                    if( edgeTable[ cubeIndex ] == 0 )
                    {
                        continue;
                    }

                    Coordinate base = new Coordinate( x, y, z );
                    for( int i = 0; triTable[ cubeIndex ][ i ] != -1; i += 3 )
                    {
                        Triangle new_triangle = new Triangle();

                        Coordinate a = interpolate( triTable[ cubeIndex ][ i ], x, y, z );
                        Coordinate b = interpolate( triTable[ cubeIndex ][ i + 1 ], x, y, z );
                        Coordinate c = interpolate( triTable[ cubeIndex ][ i + 2 ], x, y, z );
                        a.add( base );
                        b.add( base );
                        c.add( base );

                        if( coordinates.containsKey( a ) )
                        {
                            a = coordinates.get( a );
                        }
                        else
                        {
                            coordinates.put( a, a );
                        }
                        if( coordinates.containsKey( b ) )
                        {
                            b = coordinates.get( b );
                        }
                        else
                        {
                            coordinates.put( b, b );
                        }
                        if( coordinates.containsKey( c ) )
                        {
                            c = coordinates.get( c );
                        }
                        else
                        {
                            coordinates.put( c, c );
                        }

                        new_triangle.a = a;
                        new_triangle.b = b;
                        new_triangle.c = c;
                        mesh.add( new_triangle );

                        if( shade_model == _SMOOTH )
                        {
                            if( !coordinates_triangles.containsKey( a ) )
                            {
                                coordinates_triangles.put( a, new Vector<Triangle> () );
                            }
                            if( !coordinates_triangles.containsKey( b ) )
                            {
                                coordinates_triangles.put( b, new Vector<Triangle> () );
                            }
                            if( !coordinates_triangles.containsKey( c ) )
                            {
                                coordinates_triangles.put( c, new Vector<Triangle> () );
                            }
                            try
                            {
                                coordinates_triangles.get( a ).add( new_triangle );
                                coordinates_triangles.get( b ).add( new_triangle );
                                coordinates_triangles.get( c ).add( new_triangle );
                            }
                            catch( NullPointerException n_e )
                            {
                                n_e.printStackTrace();
                            }
                        }

                    }
                }
            }
        }

        num_triangles = mesh.size();

    }

    public int getNumTriangles()
    {
        return num_triangles;
    }

    private void calculateNormals()
    {

        // calculate normal for each separate triangle
        for( Triangle t : mesh )
        {
            Coordinate norm = ANode.getNormal( t.a, t.b, t.c );
            t.normal = norm;
            t.a.setNormal( norm );
            t.b.setNormal( norm );
            t.c.setNormal( norm );

        }

        if( shade_model == _SMOOTH )
        {
            HashMap<Coordinate, Coordinate> area_normal = new HashMap<Coordinate, Coordinate> ();
            int omg_count = 0;
            int total_num = 0;
            // average normals for each separate vertex due to triangles it is connected to
            for( Coordinate a : coordinates.keySet() )
            {
                Coordinate normal = new Coordinate( 0, 0, 0 );

                Vector<Triangle> triangles = coordinates_triangles.get( a );
                if( triangles == null )
                {
                    //System.err.println( "WHOOPS" + a.toString());
                    omg_count++;
                    continue;
                }
                int num = triangles.size();
                //System.out.println( num );

                for( Triangle t : triangles )
                {
                    total_num++;
                    Coordinate n = t.normal;
                    if( area_normal.containsKey( n ) )
                    {
                        n = area_normal.get( n );
                    }
                    else
                    {
                        n = Coordinate.multiply( n, 1 / t.getArea() );
                        area_normal.put( t.normal, n );
                    }
                    normal.add( n );
                    //normal.add( t.normal);
                }
                a.setNormal( normal );
                //normal.multiply( ( double ) 1 / normal.getLength() );
            }
            System.out.println( "OMG OMG OMG OMG " + Integer.toString( omg_count ) );
            System.out.println( "Connected triangles = " + Integer.toString( total_num ) + " out of " + Integer.toString( mesh.size() ) );
        }

        //transformCoordinates();
    }

    private void transformCoordinates()
    {
        TreeSet s = new TreeSet<Coordinate> ();

        for( Triangle t : mesh )
        {
            transformTriangle( t, s );
        }

    }

    public static int _SMOOTH = 1;
    public static int _FLAT = 2;
    public int shade_model = _SMOOTH;

    private int wireframe_list = -1;
    private static boolean use_display_list = true;
    private boolean need_to_make_display_list = true;
    private static boolean need_to_make_shader = true;

    public static float line_width = .5f;

    public static void setUseDisplayLists( boolean val )
    {
        use_display_list = val;
    }

    public static boolean getUseDisplayLists()
    {
        return use_display_list;
    }

    private boolean deleting_self = false;
    public boolean isShouldDelete()
    {
        return deleting_self;
    }

    public void deleteSelf()
    {
        /*
                 context.makeCurrent();
                 GL gl = context.getGL();
                 gl.glDeleteLists( super.display_list, 1 );
                 super.display_list = -1;
         */
        deleting_self = true;
    }

    private void renderMesh( GLAutoDrawable g, Vector<Triangle> mesh )
    {
        GL gl = g.getGL();
        gl.glBegin( gl.GL_TRIANGLES );
        for( Triangle t : mesh )
        {
            if( shade_model == _FLAT )
            {
                gl.glNormal3d( t.normal.getX(), t.normal.getY(), t.normal.getZ() );
            }
            if( shade_model == _SMOOTH )
            {
                gl.glNormal3d( t.a.getNormal().getX(), t.a.getNormal().getY(), t.a.getNormal().getZ() );
            }
            Coordinate a = transformCoord( t.a );
            gl.glVertex3d( a.getX(), a.getY(), a.getZ() );

            if( shade_model == _SMOOTH )
            {
                gl.glNormal3d( t.b.getNormal().getX(), t.b.getNormal().getY(), t.b.getNormal().getZ() );
            }

            Coordinate b = transformCoord( t.b );
            gl.glVertex3d( b.getX(), b.getY(), b.getZ() );

            if( shade_model == _SMOOTH )
            {
                gl.glNormal3d( t.c.getNormal().getX(), t.c.getNormal().getY(), t.c.getNormal().getZ() );
            }

            Coordinate c = transformCoord( t.c );
            gl.glVertex3d( c.getX(), c.getY(), c.getZ() );
        }
        gl.glEnd();

    }

    double transform_matrix[] = new double[16 ];

    public void render( GLAutoDrawable g )
    {
        GL gl = g.getGL();

        if( need_to_make_shader )
        {
            colorful_shader = new GLSLSHaderProgram( g );
            colorful_shader.addShader( g, ShaderProgram.ProgramType.VERTEX_PROGRAM, Main.working_folder + Main.separator + "iso_vert.glsl" );
            colorful_shader.addShader( g, ShaderProgram.ProgramType.FRAGMENT_PROGRAM,
                                       Main.working_folder + Main.separator + "iso_colorful_frag.glsl" );
            colorful_shader.link( g );

            metal_shader = new GLSLSHaderProgram( g );
            metal_shader.addShader( g, ShaderProgram.ProgramType.VERTEX_PROGRAM, Main.working_folder + Main.separator + "iso_metal_vert.glsl" );
            metal_shader.addShader( g, ShaderProgram.ProgramType.FRAGMENT_PROGRAM, Main.working_folder + Main.separator + "iso_frag.glsl" );
            metal_shader.link( g );

            plain_shader = new GLSLSHaderProgram( g );
            plain_shader.addShader( g, ShaderProgram.ProgramType.VERTEX_PROGRAM, Main.working_folder + Main.separator + "iso_plain_vert.glsl" );
            plain_shader.addShader( g, ShaderProgram.ProgramType.FRAGMENT_PROGRAM, Main.working_folder + Main.separator + "iso_frag.glsl" );
            plain_shader.link( g );

            phong_shader = new GLSLSHaderProgram( g );
            phong_shader.addShader( g, ShaderProgram.ProgramType.VERTEX_PROGRAM, Main.working_folder + Main.separator + "phong_vert.glsl" );
            phong_shader.addShader( g, ShaderProgram.ProgramType.FRAGMENT_PROGRAM, Main.working_folder + Main.separator + "phong_frag.glsl" );
            phong_shader.link( g );

            //wire_frame_shader.use();
            need_to_make_shader = false;
        }
        if( need_to_make_display_list )
        {
            context = g.getContext();
            System.out.println( "Number of triangles in this mesh = " + Integer.toString( mesh.size() ) );
            super.display_list = gl.glGenLists( 1 );
            gl.glNewList( super.display_list, gl.GL_COMPILE );
            {
                renderMesh( g, mesh );
            }
            gl.glEndList();

            wireframe_list = gl.glGenLists( 1 );
            gl.glNewList( wireframe_list, gl.GL_COMPILE );
            {
                for( Triangle t : mesh )
                {
                    gl.glBegin( gl.GL_LINE_LOOP );
                    gl.glVertex3d( t.a.getX(), t.a.getY(), t.a.getZ() );
                    gl.glVertex3d( t.b.getX(), t.b.getY(), t.b.getZ() );
                    gl.glVertex3d( t.c.getX(), t.c.getY(), t.c.getZ() );
                    gl.glEnd();
                }
            }
            gl.glEndList();

            need_to_make_display_list = false;
            clearData();
        }

        if( this.deleting_self )
        {
            gl.glDeleteLists( super.display_list, 1 );
            super.display_list = -1;
            show = false;
            //edu.ohiou.lev_neiman.jung.volume_render.ui.control.iso_surface.IsoSurfaceControl_MainPanel.surfaces.remove( this );
        }

        if( show )
        {

            gl.glShadeModel( shade_model == _SMOOTH ? GL.GL_SMOOTH : gl.GL_SMOOTH );
            if( m.color[ 3 ] < .95 )
            {
                gl.glDisable( gl.GL_DEPTH_TEST );
                //gl.glDepthFunc( gl.GL_ALWAYS );
                //gl.glEnable( GL.GL_CULL_FACE );
                //gl.glCullFace( GL.GL_BACK );
                //gl.glEnable( gl.GL_DEPTH_TEST );

            }
            else
            {
                //gl.glDisable( gl.GL_CULL_FACE );
                gl.glEnable( gl.GL_DEPTH_TEST );
                //gl.glDepthFunc( GL.GL_LEQUAL );

            }

            switch( current_shader )
            {
                case FixedFunction:
                    gl.glUseProgram( 0 );
                    break;
                case Colorful:
                {
                    colorful_shader.use( g );
                    break;
                }
                case Metal:
                {
                    metal_shader.use( g );
                    break;
                }
                case Plain:
                {
                    plain_shader.use( g );
                    break;
                }
                case Phong:
                {
                    phong_shader.use( g );
                    break;
                }
            }

            //gl.glUseProgram( 0 );
            gl.glEnable( gl.GL_LIGHTING );
            m.apply( g );

            if( use_display_list )
            {

                gl.glCallList( super.display_list );

                gl.glColor3f( 0, 0, 0 );
                gl.glLineWidth( line_width );
                //gl.glCallList( wireframe_list );
            }
            else
            {

                gl.glPushMatrix();
                {
                    /*
                                         gl.glLoadIdentity();

                                         gl.glScalef( .5f, .5f, .5f );
                                         gl.glRotatef( Renderer.aX, 1, 0, 0 );
                                         gl.glRotatef( Renderer.aY, 0, 1, 0 );
                     */


                    gl.glGetDoublev( GL.GL_MODELVIEW_MATRIX, transform_matrix, 0 );
                }
                gl.glPopMatrix();
                t_comporator.reset();
                t_comporator.putMatrix( transform_matrix );
                Collections.sort( mesh, t_comporator );

                renderMesh( g, mesh );

            }
        }

    }

    public static int _ASCENDING = 1;
    public static int _DESCENDING = 2;
    public static int sort_order = _ASCENDING;

    /**
     * this method is used to sort surfaces based upon their transperency level.  Least transperenty should be rendered last.
     * @param o Object
     * @return int
     */
    public int compareTo( Object o )
    {
        if( sort_order == _ASCENDING )
        {
            return Float.compare( this.m.color[ 3 ], ( ( MarchingCubez ) o ).m.color[ 3 ] );
        }
        else
        {
            return Float.compare( ( ( MarchingCubez ) o ).m.color[ 3 ], this.m.color[ 3 ] );
        }
    }
}
