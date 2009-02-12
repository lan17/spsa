package edu.ohiou.lev_neiman.life;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

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
public class Main
        implements Runnable
{
    static Board canvaz;
    static Thread render_thread;

    static Dimension window_size = new Dimension( 800, 600 );

    public void run()
    {
        try
        {
            while( true )
            {
                canvaz.display();

                render_thread.sleep( 200 );
            }

        }
        catch( Exception e )
        {
            e.printStackTrace();
        }

    }

    public static void main( String[] args )
    {
        JFrame window_frame = new JFrame( "Game Of Life" );
        window_frame.setSize( window_size );
        window_frame.addWindowListener( new WindowAdapter()
        {
            public void windowClosing( WindowEvent e )
            {
                System.exit( 0 );
            }
        } );
        canvaz = new Board( 20 );
        canvaz.setPreferredSize( window_size );
        window_frame.add( canvaz );
        window_frame.setVisible( true );

        render_thread = new Thread( new Main() );
        render_thread.start();

    }
}
