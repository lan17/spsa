package edu.ohiou.lev_neiman.misc;

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
public class Func
{
    public Func()
    {
    }

    public static double roundOff( double f, int dec_places )
    {

        double round_off_factor = Math.pow( 10, dec_places );
        double p = 5 / ( 10 * round_off_factor );
        double ret = ( ( ( double ) ( ( int ) ( ( f + p ) * round_off_factor ) ) ) / round_off_factor );
        return ret;

        /*
         String s = Double.toString( f );
         int i = s.lastIndexOf( '.' );
         if( i + dec_places + 1 >= s.length() ) return Double.parseDouble( s );
         else return Double.parseDouble( s.substring( 0, i + dec_places+1));
         */

    }

}
