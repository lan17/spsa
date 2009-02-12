package edu.ohiou.lev_neiman.acm.tju;

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
public class Hello_World
{
    public Hello_World()
    {
    }

    public static void main( String[] args )
    {

        while( true )
        {
            try
            {
                int n = System.in.read();
                if( n == 0 )
                {
                    System.exit( 0 );
                }
                System.out.print( ( char ) n );
            }
            catch( Exception io_e )
            {
                io_e.printStackTrace();
            }
        }
    }
}
