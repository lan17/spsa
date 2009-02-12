package edu.ohiou.lev_neiman.mandelbrot;

import javax.media.opengl.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.*;


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
public class Main extends edu.ohiou.lev_neiman.sceneapi.GenericRenderer implements MouseListener, MouseMotionListener, KeyListener
{
    private Mesh mesh = new Mesh();

    public Main()
    {
        super.setClearColor( new Color( 255,255, 255) );
        try
        {
            init();
            super.addMouseListener( this );
            super.addMouseMotionListener( this );
            super.addKeyListener( this );
        }
        catch( Exception exc )
        {
            exc.printStackTrace();
        }
    }
    int t=0;
    float omega_x = 0;
    float omega_y = 0;
    public void display( GLAutoDrawable gl )
    {
        GL g = gl.getGL();
        t=(t-1+255)%255;
        //super.setClearColor( gl, new Color( t, 255-t, 0 ) );
        super.display( gl );
        //g.glTranslate3f( width / 2.0, height / 2.0, 0 );
        if( rotate_flag )
        {
            g.glRotatef( omega_x, 1, 0, 0 );
            g.glRotatef( omega_y, 0, 1, 0 );
        }
        mesh.render( gl );
        //omega += .5;
    }

    public void reshape( GLAutoDrawable gLAutoDrawable, int _int, int _int2, int width, int height )
    {
        final GL gl = gLAutoDrawable.getGL();
        if( width <= 2 )
        {
            width = 3;
        }
        if( height <= 2 )
        {
            height = 3;
        }
        final float h = ( float ) width / ( float ) height;
        gl.glMatrixMode( GL.GL_PROJECTION );
        gl.glLoadIdentity();
        gl.glOrtho( -1*h, 10*h, -1, 10, -100, 1000 );
        gl.glMatrixMode( GL.GL_MODELVIEW );
        gl.glLoadIdentity();

    }



    private void init()
    {
        JFrame frame = new JFrame( "Mandelbrot" );
        frame.setSize( new Dimension( 800, 600 ) );
        frame.setLayout( new BorderLayout() );
        frame.add( this, BorderLayout.CENTER );

        frame.addWindowListener( new WindowAdapter()
        {
            public void windowClosing( WindowEvent e )
            {
                System.exit( 0 );
            }
        } );


        frame.setVisible( true );

    }

    public static void main( String[] args )
    {
        Main main = new Main();
        while( true )
        {
            main.display();
        }
    }

    public void mouseClicked( MouseEvent e )
    {
        mesh.dist_i /= 2f;
        mesh.dist_real /= 2f;
    }

    public void mousePressed( MouseEvent e )
    {
    }

    public void mouseReleased( MouseEvent e )
    {
    }

    public void mouseEntered( MouseEvent e )
    {
        m_x = e.getX();
        m_y = e.getY();
    }

    public void mouseExited( MouseEvent e )
    {
    }

    public void mouseDragged( MouseEvent e )
    {

    }

    int m_x;
    int m_y;
    public void mouseMoved( MouseEvent e )
    {
        if( move_flag )
        {
            mesh.center_a += ( ( float ) ( m_x - e.getX() ) ) / ( float ) 500;
            mesh.center_b += ( ( float ) ( m_y - e.getY() ) ) / ( float ) 500;
        }
        if( rotate_flag )
        {omega_x += m_x - e.getX();
            omega_y += m_y - e.getY();
        }
        m_x = e.getX();
        m_y = e.getY();

    }

    public void keyTyped( KeyEvent e )
    {
    }

    boolean rotate_flag = false;
    boolean move_flag = false;
    public void keyPressed( KeyEvent e )
    {
        System.out.println( "KEY PRESSED = " + e.getKeyChar() );
        rotate_flag = e.getKeyChar() == 'q' | rotate_flag;
        move_flag = e.getKeyChar() == 'a' | move_flag;
    }

    public void keyReleased( KeyEvent e )
    {
        System.out.println( "Key Released = " + e.getKeyChar() );
        rotate_flag = e.getKeyChar() != 'q' & rotate_flag;
        move_flag = e.getKeyChar() != 'a' & move_flag;
    }
}
