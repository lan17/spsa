package edu.ohiou.lev_neiman.jung.volume_render;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collections;

import javax.imageio.ImageIO;
import javax.media.opengl.*;

import edu.ohiou.lev_neiman.sceneapi.GenericRenderer;
import edu.ohiou.lev_neiman.sceneapi.basic.LightNode;
import edu.ohiou.lev_neiman.sceneapi.visualize.MarchingCubez;
import edu.ohiou.lev_neiman.sceneapi.visualize.Volume;

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
public class Renderer
        extends GenericRenderer implements MouseMotionListener, MouseListener, KeyListener
{
    public static GLContext context;

    public Volume volume;
    // private LightNode light = new LightNode( GL.GL_LIGHT0);

    private float angle = 0f;

    public static float aX = 0;
    public static float aY = 0;

    public static float dX = 0;
    public static float dY = 0;

    private int mX, mY;

    private float zoom = -1;
    private int cur_m_button;

    private boolean draw_stuff = true;
    public int take_screenshot = 0;

    public BufferedImage screenshot = null;


    public int fps_counter = 0;


    LightNode lights[] =
            {new LightNode( GL.GL_LIGHT0 ), new LightNode( GL.GL_LIGHT1 )};

    public Renderer()
    {
        super.addGLEventListener( this );

        super.addMouseListener( this );
        super.addMouseMotionListener( this );
        super.addKeyListener( this );
    }


    public synchronized void setDraw( boolean draw )
    {
        this.draw_stuff = draw;
    }

    /**
     * display
     *
     * @param gLAutoDrawable GLAutoDrawable
     * @todo Implement this javax.media.opengl.GLEventListener method
     */
    public void display( GLAutoDrawable g )
    {
        if( edu.ohiou.lev_neiman.jung.volume_render.ui.control.iso_surface.IsoSurfaceControl_MainPanel.need_to_delete_surfaces )
        {
            for( MarchingCubez iso : edu.ohiou.lev_neiman.jung.volume_render.ui.control.iso_surface.IsoSurfaceControl_MainPanel.surfaces )
            {
                if( iso.isShouldDelete() )
                {
                    edu.ohiou.lev_neiman.jung.volume_render.ui.control.iso_surface.IsoSurfaceControl_MainPanel.surfaces.remove( iso );
                }
            }
            edu.ohiou.lev_neiman.jung.volume_render.ui.control.iso_surface.IsoSurfaceControl_MainPanel.need_to_delete_surfaces = false;
        }
        if( draw_stuff || take_screenshot > 0 )
        {
            GL gl = g.getGL();
            super.display( g );
            for( LightNode light : lights )
            {
                light.render( g );
            }
            gl.glTranslated( dX, dY, zoom );

            gl.glPushMatrix();
            {

                gl.glRotatef( aX, 1, 0, 0 );
                gl.glRotatef( aY, 0, 1, 0 );
                gl.glEnable( GL.GL_BLEND );
                gl.glBlendFunc( GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA );
                //gl.glBlendEquation( GL.GL_MAX );


                gl.glScalef( .5f, .5f, .5f );
                MarchingCubez.line_width = 1f / ( float ) Math.exp( ( double ) Math.abs( zoom ) - 1 );
                MarchingCubez.sort_order = MarchingCubez._DESCENDING;
                Collections.sort( edu.ohiou.lev_neiman.jung.volume_render.ui.control.iso_surface.IsoSurfaceControl_MainPanel.surfaces );
                for( MarchingCubez iso : edu.ohiou.lev_neiman.jung.volume_render.ui.control.iso_surface.IsoSurfaceControl_MainPanel.surfaces )
                {
                    iso.render( g );
                }
                /*
                                 MarchingCubez.sort_order = MarchingCubez._DESCENDING;
                 Collections.sort( edu.ohiou.lev_neiman.jung.volume_render.ui.control.iso_surface.IsoSurfaceControl_MainPanel.surfaces );
                 for( MarchingCubez iso : edu.ohiou.lev_neiman.jung.volume_render.ui.control.iso_surface.IsoSurfaceControl_MainPanel.surfaces )
                                 {
                    iso.render( g );
                                 }
                 */



            }
            gl.glPopMatrix();

            gl.glDisable( GL.GL_DEPTH_TEST );
            gl.glDisable( GL.GL_CULL_FACE );
            gl.glEnable( gl.GL_BLEND );
            gl.glBlendFunc( GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA );
            //gl.glBlendEquation( GL.GL_FUNC_ADD );

            volume.render( g );

            if( take_screenshot > 0 )
            {
                take_screenshot--;
                if( take_screenshot == 0 )
                {
                    screenshot = com.sun.opengl.util.Screenshot.readToBufferedImage( this.getWidth(), this.getHeight() );
                }
            }
        }
    }

    /**
     * displayChanged
     *
     * @param gLAutoDrawable GLAutoDrawable
     * @param _boolean boolean
     * @param _boolean2 boolean
     * @todo Implement this javax.media.opengl.GLEventListener method
     */
    public void displayChanged( GLAutoDrawable gLAutoDrawable, boolean _boolean,
                                boolean _boolean2 )
    {
    }

    /**
     * init
     *
     * @param gLAutoDrawable GLAutoDrawable
     * @todo Implement this javax.media.opengl.GLEventListener method
     */
    public void init( GLAutoDrawable gLDrawable )
    {
        super.required_gl_functions.add( "glCreateProgram" );
        super.required_gl_functions.add( "glTexImage3D" );
        super.init( gLDrawable );
        GL gl = gLDrawable.getGL();
        System.out.println( "INITIALIZING!" );

        //gl.glEnable( gl.GL_POINT_SMOOTH);

        //gl.glEnable( gl.GL_NORMALIZE );

        factor = .5f;
        mean = .2f;
        dev = .05f;
        volume = new Volume();
        volume.render( gLDrawable );
        volume.useGaussian();

        float global_ambient[] =
                {0.5f, 0.5f, 0.5f, 1.0f};
        gl.glLightModelfv( gl.GL_LIGHT_MODEL_AMBIENT, global_ambient, 0 );

        // set up lights
        lights[ 0 ].position[ 0 ] = -2;
        lights[ 0 ].position[ 1 ] = -2;
        lights[ 0 ].position[ 2 ] = -2;
        lights[ 1 ].position[ 0 ] = 2;
        lights[ 1 ].position[ 1 ] = 2;
        lights[ 1 ].position[ 2 ] = 2;

        // System.out.println( shader. setUniformVariable( "scale_factor", 1f) );



        //setup fps counter
        //FPSMeasure m = new FPSMeasure( this );

    }


    public void mouseDragged( MouseEvent mouseEvent )
    {
        //System.out.println( mouseEvent.getButton() );
        if( cur_m_button == 1 && !translate_flag )
        {
            aY -= mX - mouseEvent.getX();
            aX -= mY - mouseEvent.getY();

        }
        if( cur_m_button == 3 )
        {
            zoom += ( mX - mouseEvent.getX() ) * .01;
        }
        if( cur_m_button == 1 && translate_flag )
        {
            dY += ( mY - mouseEvent.getY() ) * .001;
            dX += ( mX - mouseEvent.getX() ) * .001;
        }
        mX = mouseEvent.getX();
        mY = mouseEvent.getY();
    }


    public void mouseMoved( MouseEvent mouseEvent )
    {
        //System.out.println( "MouseMoving" );
        mX = mouseEvent.getX();
        mY = mouseEvent.getY();
    }

    public void mouseClicked( MouseEvent mouseEvent )
    {
    }

    public void mousePressed( MouseEvent mouseEvent )
    {
        cur_m_button = mouseEvent.getButton();
    }

    public void mouseReleased( MouseEvent mouseEvent )
    {
    }

    public void mouseEntered( MouseEvent mouseEvent )
    {
    }

    public void mouseExited( MouseEvent mouseEvent )
    {
    }

    /**
     * changeAlphaScaleFactor( float f )
     */
    public void changeAlphaScaleFactor( float f )
    {
        factor = f;

    }


    public static float factor, mean, dev;
    public void changeGaussFactor( float mean, float dev )
    {
        this.mean = mean;
        this.dev = dev;

    }


    public static boolean writePNG( File file, BufferedImage image )
    {
        try
        {
            ImageIO.write( image, "png", file );
        }
        catch( Exception e )
        {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    public void keyTyped( KeyEvent e )
    {
    }

    private boolean translate_flag = false;
    public void keyPressed( KeyEvent e )
    {
        if( e.getKeyChar() == 'q' )
        {
            //System.out.println( "SHIFT" );
            translate_flag = true;
        }
    }

    public void keyReleased( KeyEvent e )
    {
        if( e.getKeyChar() == 'q' )
        {
            //System.out.println( "SHIFT" );
            translate_flag = false;
        }

    }

}


class FPSMeasure
        extends Thread
{
    Renderer render;
    public FPSMeasure( Renderer r )
    {
        render = r;
        start();
    }

    public void run()
    {
        while( true )
        {
            try
            {
                super.sleep( 1000 );
            }
            catch( Exception exc )
            {
                exc.printStackTrace();
            }
            System.out.println( render.fps_counter );
            render.fps_counter = 0;
        }
    }
}
