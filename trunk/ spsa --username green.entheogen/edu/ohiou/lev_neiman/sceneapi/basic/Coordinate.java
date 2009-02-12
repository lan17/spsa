package edu.ohiou.lev_neiman.sceneapi.basic;

import java.io.Serializable;

import edu.ohiou.lev_neiman.misc.Func;

/**
 * <p>Container for 3D coordinate.</p>
 *
 * <p>Description: container for 3D coordinate.</p>
 *
 * <p>Copyright: Lev A Neiman 2008</p>
 *
 * <p>Company: Ohio University EECS </p>
 *
 * @author Lev A Neiman
 * @version 1.0
 */
public class Coordinate
        implements Serializable, Comparable, Cloneable
{
    /**
     * x y and z values for this coordinate.
     */
    private double x, y, z;

    private Coordinate normal = null;
    private Coordinate color = null;

    /**
     * default constructor
     */
    public Coordinate()
    {
        set( 0, 0, 0 );
    }

    public Coordinate( Coordinate a )
    {
        set( a.getX(), a.getY(), a.getZ() );
        if( a.normal != null )
        {
            normal = new Coordinate( a.normal );
        }
    }

    public Coordinate clone()
    {
        return new Coordinate( this );
    }


    /**
     * Constructor with xyz passed to it.
     * @param x double
     * @param y double
     * @param z double
     */
    public Coordinate( double x, double y, double z )
    {
        set( x, y, z );
    }

    public int hashCode()
    {
        //return 0;

        int dig = 1;
        return Double.valueOf( x ).hashCode() + Double.valueOf( y ).hashCode() + Double.valueOf( z ).hashCode();
    }


    public static void roundOff( Coordinate a, int dec_places )
    {
        a.x = Func.roundOff( a.x, dec_places );
        a.y = Func.roundOff( a.y, dec_places );
        a.z = Func.roundOff( a.z, dec_places );
    }


    /**
     * set x value for this coordinate.
     * @param x double
     */
    public synchronized void setX( double x )
    {
        this.x = x;
    }

    /**
     * set y value for this coordinate.
     * @param y double
     */
    public synchronized void setY( double y )
    {
        this.y = y;
    }

    /**
     * set z value for this coordinate.
     * @param z double
     */
    public synchronized void setZ( double z )
    {
        this.z = z;
    }

    /**
     * add this coordinate and coordinate b.
     * @param b Coordinate
     */
    public synchronized void add( Coordinate b )
    {
        this.x += b.x;
        this.y += b.y;
        this.z += b.z;
    }

    public synchronized void subtract( Coordinate b )
    {
        this.x -= b.x;
        this.y -= b.y;
        this.z -= b.z;
    }

    /**
     * add a and b and return result as new Coordinate
     * @param a Coordinate
     * @param b Coordinate
     * @return Coordinate
     */
    public static Coordinate add( Coordinate a, Coordinate b )
    {
        Coordinate ret = new Coordinate( a.x + b.x, a.y + b.y, a.z + b.z );
        return ret;
    }

    public static Coordinate subtract( Coordinate a, Coordinate b )
    {
        return new Coordinate( a.x - b.x, a.y - b.y, a.z - b.z );
    }

    public static Coordinate multiply( Coordinate a, double b )
    {
        Coordinate ret = new Coordinate( a.x * b, a.y * b, a.z * b );
        return ret;
    }

    public static Coordinate interpolate( Coordinate a, Coordinate b, double f )
    {
        return Coordinate.add( Coordinate.multiply( a, 1 - f ), Coordinate.multiply( b, f ) );
    }


    /**
     *
     * @param a double
     */
    public synchronized void multiply( double a )
    {
        x *= a;
        y *= a;
        y *= a;

    }

    public synchronized void multiply4x4( final double[] matrix )
            throws IllegalArgumentException
    {
        try
        {
            set( multiply4x4( this, matrix ) );
        }
        catch( IllegalArgumentException exc )
        {
            throw exc;
        }

        if( normal != null )
        {
            normal.multiply4x4( matrix );
        }
    }

    public static Coordinate multiply4x4( Coordinate a, final double[] matrix )
            throws IllegalArgumentException
    {
        Coordinate ret = new Coordinate();
        if( matrix.length != 16 )
        {
            throw new IllegalArgumentException( "Passed matrix is not 4x4" );
        }
        ret.x = ( matrix[ 0 ] * a.x + matrix[ 4 ] * a.y + matrix[ 8 ] * a.z + matrix[ 12 ] * 1 );
        ret.y = matrix[ 1 ] * a.x + matrix[ 5 ] * a.y + matrix[ 9 ] * a.z + matrix[ 13 ] * 1;
        ret.z = matrix[ 2 ] * a.x + matrix[ 6 ] * a.y + matrix[ 10 ] * a.z + matrix[ 14 ] * 1;

        return ret;
    }

    /**
     * return the value of x.
     * @return double
     */
    public synchronized double getX()
    {
        return x;
    }

    /**
     * return the value of y.
     * @return double
     */
    public synchronized double getY()
    {
        return y;
    }

    /**
     * return the value of z.
     * @return double
     */
    public synchronized double getZ()
    {
        return z;
    }

    /**
     * multiply this coordinate with b.
     * @param b Coordinate
     */
    public synchronized void multiply( Coordinate b )
    {
        this.x *= b.x;
        this.y *= b.y;
        this.z *= b.z;
    }

    /**
     * compute dot product between this and b.
     * @param b Coordinate
     * @return double
     */
    public synchronized double dot( Coordinate b )
    {
        double ret = 0;
        ret += this.x * b.x;
        ret += this.y * b.y;
        ret += this.z * b.z;
        return ret;
    }

    public synchronized double getLength()
    {
        return Math.sqrt( x * x + y * y + z * z );
    }

    /**
     * set this coordinate equal to b.
     * @param b Coordinate
     */
    public synchronized void set( Coordinate b )
    {
        this.x = b.x;
        this.y = b.y;
        this.z = b.z;
    }

    /**
     * returns normal associated with this coordinate.
     * @return Coordinate
     */
    public synchronized Coordinate getNormal()
    {
        return normal;
    }

    /**
     * return color associated with this coordinate
     * @return Coordinate
     */
    public synchronized Coordinate getColor()
    {
        return color;
    }

    public synchronized void setNormal( Coordinate normal )
    {
        this.normal = normal;
    }

    public synchronized void setColor( Coordinate color )
    {
        this.color = color;
    }

    /**
     * set x y and z for this coordinate.
     * @param x double
     * @param y double
     * @param z double
     */
    public synchronized void set( double x, double y, double z )
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void print()
    {
        System.out.println( toString() );
    }

    public String toString()
    {
        String ret = "basic.Coordinate { " + Double.toString( x ) + ", " +
                     Double.toString( y ) + ", " + Double.toString( z ) + " }";
        return ret;
    }

    public static double _epsilon = 1e-20;

    public int compareTo( Object o )
    {
        if( o instanceof Coordinate )
        {
            Coordinate c = ( Coordinate ) o;
            //if( Math.abs( this.x - c.x ) <= _epsilon && Math.abs( this.y - c.y ) <= _epsilon && Math.abs( this.z - c.z ) <= _epsilon )
            //if(  x == c.x && y == c.y && z == c.z )
            //c.print();
            if( Math.abs( x - c.x ) > _epsilon )
            {
                return Double.compare( x, c.x );
            }
            if( Math.abs( y - c.y ) > _epsilon )
            {
                return Double.compare( y, c.y );
            }
            if( Math.abs( z - c.z ) > _epsilon )
            {
                return Double.compare( z, c.z );
            }
        }
        return 0;
    }

    public boolean equals( Object o )
    {
        if( ! ( o instanceof Coordinate ) )
        {
            return false;
        }
        else if( this.compareTo( o ) == 0 )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


}
