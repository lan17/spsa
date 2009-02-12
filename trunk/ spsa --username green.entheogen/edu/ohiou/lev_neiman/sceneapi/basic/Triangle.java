package edu.ohiou.lev_neiman.sceneapi.basic;


/**
 * <p>Describes a triangle and associated values.</p>
 *
 * <p>Description: Describes a triangle and associated values.</p>
 *
 * <p>Copyright: Lev A Neiman 2008</p>
 *
 * <p>Company: Ohio University EECS </p>
 *
 * @author Lev A Neiman
 * @version 1.0
 */
public class Triangle
        implements Comparable, Cloneable
{
    /**
     * vertex a
     */
    public Coordinate a = null;

    /**
     * vertex b
     */
    public Coordinate b = null;

    /**
     * vertex c
     */
    public Coordinate c = null;

    /**
     * normal for (a-c)x(b-c)
     */
    public Coordinate normal = null;

    /**
     * smooth shaded normal for a
     */
    public Coordinate a_normal = null;

    /**
     * smooth shaded normal for b
     */
    public Coordinate b_normal = null;

    /**
     * smooth shaded nomral for c
     */
    public Coordinate c_normal = null;

    /**
     * Material name that applies to this Triangle
     */
    public String material_name = "Default";

    /**
     * Material reference.
     */
    public Material material;

    /**
     * texture coordinate for a
     */
    Coordinate ta;

    /**
     * texture coordinate for b
     */
    Coordinate tb;

    /**
     * texture coordinate for c
     */
    Coordinate tc;

    /**
     * Default constructor.
     */
    public Triangle()
    {
        a = new Coordinate();
        b = new Coordinate();
        c = new Coordinate();
    }

    public Triangle clone()
    {
        Triangle ret = new Triangle();
        ret.a = a.clone();
        ret.b = b.clone();
        ret.c = c.clone();
        if( ta != null )
        {
            ret.ta = ta.clone();
        }
        if( tb != null )
        {
            ret.tb = tb.clone();
        }
        if( tc != null )
        {
            ret.tc = tc.clone();
        }
        try
        {
            ret.a_normal = a_normal.clone();
            ret.b_normal = b_normal.clone();
            ret.c_normal = c_normal.clone();
        }
        catch( Exception exc )
        {
            //exc.printStackTrace();
        }
        return ret;
    }

    public double getArea()
    {
        Coordinate AB = Coordinate.subtract( a, b );
        Coordinate AC = Coordinate.subtract( a, c );
        return .5 * Math.sqrt( Math.pow( AB.getLength(), 2 ) * Math.pow( AC.getLength(), 2 ) - Math.pow( AB.dot( AC ), 2 ) );
    }

    public void transform( double matrix[] )
    {
        a.multiply4x4( matrix );
        b.multiply4x4( matrix );
        c.multiply4x4( matrix );

    }

    public Coordinate[] toArray_abc()
    {
        Coordinate[] ret =
                {a, b, c};
        return ret;
    }

    public int hashCode()
    {
        return a.hashCode() + b.hashCode() + c.hashCode();
    }

    public int compareTo( Object o )
    {
        if( ! ( o instanceof Triangle ) )
        {
            return -1;
        }
        else
        {
            Triangle nother = ( Triangle ) o;
            Double az = Math.pow( a.getZ(), 2 ) + Math.pow( b.getZ(), 2 ) + Math.pow( c.getZ(), 2 );
            Double notherz = Math.pow( nother.a.getZ(), 2 ) + Math.pow( nother.b.getZ(), 2 ) + Math.pow( nother.c.getZ(), 2 );
            if( Math.abs( az - notherz ) <= Coordinate._epsilon )
            {
                return 0;
            }
            else if( az < notherz )
            {
                return 1;
            }
            else
            {
                return -1;
            }

        }
    }
}
