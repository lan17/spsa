package edu.ohiou.lev_neiman.jung.volume_render;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.FloatBuffer;
import java.util.LinkedList;

import javax.swing.*;

import edu.ohiou.lev_neiman.jung.volume_render.event.*;
import edu.ohiou.lev_neiman.jung.volume_render.ui.control.data.DataReader;
import edu.ohiou.lev_neiman.jung.volume_render.ui.control.data.NewDataControl;
import edu.ohiou.lev_neiman.sceneapi.visualize.functionz.GaussianDistribution;

/**
 * <p>Main class for the project.</p>
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
        implements Runnable, ActionListener
{

    private class myLoader
            extends java.lang.ClassLoader
    {

    }


    Main()
    {
        try
        {
            jbInit();
        }
        catch( Exception ex )
        {
            ex.printStackTrace();
        }

    }

    public JFileChooser file_chooser = new JFileChooser( System.getProperty( "user.home" ) );

    public JTextField alpha_scale_factor = new JFormattedTextField( ".5" );


    private static float texture_factor, texture_mean, texture_dev;
    private static int num_slices = 128;
    static
    {
        texture_factor = .5f;
        texture_mean = .2f;
        texture_dev = .05f;

    }

    private static Renderer canvas;
    public static Frame frame;

    private static RenderEventQueue action_queue = new RenderEventQueue();

    BottomBar bottom_bar = new BottomBar();

    public void run()
    {
        init();
        canvas = new Renderer();
        frame = new Frame( "Volume Renderer by Lev A. Neiman." );
        frame.setLayout( new BorderLayout() );

        canvas.setSize( new Dimension( 500, 500 ) );
        frame.add( canvas, BorderLayout.CENTER );

        frame.add( new ControlPanel(), BorderLayout.EAST );
        frame.add( bottom_bar, BorderLayout.SOUTH );

        frame.setSize( new Dimension( 900, 550 ) );
        //

        frame.addWindowListener( new WindowAdapter()
        {
            public void windowClosing( WindowEvent e )
            {
                System.exit( 0 );
            }
        } );
        frame.setVisible( true );
        render_thread = new Thread( new RenderThread() );
        render_thread.start();

    }

    private static LinkedList<RenderEventListener> listeners = new LinkedList<RenderEventListener> ();

    public static void addRenderEventListener( RenderEventListener listener )
    {
        listeners.add( listener );
    }

    private static void updateAllListeners( GenericEvent e )
    {
        for( RenderEventListener l : listeners )
        {
            l.renderEventHappened( e );
        }
    }

    public static synchronized void addRenderEvent( RenderEventQueue.Event event )
    {
        action_queue.addEvent( event );
    }

    public static synchronized Dimension getCanvasSize()
    {
        return canvas.getSize();
    }

    public static synchronized int getNumSlices()
    {
        return num_slices;
    }

    public static synchronized void setNumSlices( int n )
    {
        num_slices = n;
        action_queue.addEvent( RenderEventQueue.Event.UPDATE_TEXTURE_SLICES );
    }

    public static synchronized FloatBuffer getCurrentDataBuffer()
    {
        return canvas.volume.data_buffer;
    }

    public static synchronized GaussianDistribution getGaussianFunction()
    {
        return new GaussianDistribution( texture_factor, texture_mean, texture_dev, .01f );
    }

    public static synchronized float getTextureFactor()
    {
        return texture_factor;
    }

    public static synchronized float getTextureMean()
    {
        return texture_mean;
    }

    public static synchronized float getTextureDev()
    {
        return texture_dev;
    }

    public static synchronized void setTextureVariables( float factor, float mean, float dev )
    {
        texture_factor = factor;
        texture_mean = mean;
        texture_dev = dev;
    }

    public static synchronized void writePNG( File file, BufferedImage image )
    {
        canvas.writePNG( file, image );
    }

    public static synchronized void randomData( int N, int how_many, int smooth, float ratio )
    {
        Main.canvas.volume.setNewData( Main.canvas.volume.createRandomData( N, how_many, smooth, ratio ) );
    }

    public static boolean RENDER_DISPLAY = true;

    static Thread main_thread;
    static Thread render_thread;

    static LinkedList<File> screen_shot_queue = new LinkedList<File> ();

    private class RenderThread
            implements Runnable
    {
        private void doEvents()
        {
            if( action_queue.hasEvents() )
            {
                switch( action_queue.nextEvent() )
                {
                    case LOAD_VOLUME:
                        try
                        {
                            canvas.volume.setNewData( DataReader.readFileIntoBuffer( NewDataControl.getCurrentFile2() ) );
                        }
                        catch( Exception exc )
                        {
                            System.err.println( "Unable to read file" );
                            exc.printStackTrace();
                        }
                        break
                                ;
                    case SHOW_VOLUME:
                        canvas.volume.show = true;
                        break;
                    case HIDE_VOLUME:
                        canvas.volume.show = false;
                        break;
                    case USE_GAUSSIAN:
                        canvas.volume.useGaussian();
                        break;
                    case USE_LINEAR:
                        canvas.volume.useLinear();
                        break;
                    case UPDATE_TEXTURE_SLICES:
                        canvas.volume.num_slices = num_slices;
                        break;
                    case TAKE_SCREENSHOT:

                        BufferedImage image = takeScreenshot();
                        GenericEvent screenshot_event = new GenericEvent( image, RenderEventQueue.Event.TAKE_SCREENSHOT.name() );
                        updateAllListeners( screenshot_event );
                        canvas.setDraw( false );
                        break;
                    case SCREENSHOT_TAKEN:
                        canvas.setDraw( true );
                        break;
                    case MAP_TAKE_SCREENSHOT:
                        if( screen_shot_queue.size() == 0 )
                        {

                        }
                        else
                        {

                        }
                        break;

                }
            }
        }

        public synchronized BufferedImage takeScreenshot()
        {
            canvas.take_screenshot = 2;

            System.out.println( "TAKING SCREENSHOT" );
            while( canvas.take_screenshot > 0 )
            {
                System.out.println( canvas.take_screenshot );
                canvas.display();
            }
            return canvas.screenshot;
        }


        public void run()
        {
            try
            {
                while( true )
                {
                    doEvents();
                    if( RENDER_DISPLAY )
                    {

                        canvas.display();
                    }
                    //render_thread.yield();
                    render_thread.sleep( 25 );
                }
            }
            catch( Exception exc )
            {
                exc.printStackTrace();
            }
        }
    }


    public static final String group_folder = "lev_neiman";
    public static final String program_folder = "volume_rendering";
    public static String separator = System.getProperty( "file.separator" );
    public static final String working_folder = System.getProperty( "user.home" ) + separator + group_folder + separator + program_folder;
    //public static final String working_folder = "C:\\Documents and Settings\\Entheogen\\My Documents\\jogl-1.1.1-windows-i586\\lib";

    public static File getFileFromInputStream( InputStream i, String name )
    {
        File r = new File( working_folder + separator + name );
        if( r.exists() )
        {
            System.out.println( name + " already exists" );
            //return r;
        }
        r = null;
        byte[] buf = new byte[1024 ];
        InputStream zipinputstream = i;
        //for each entry to be extracted

        String output_folder = working_folder;
        if( !new File( output_folder ).exists() )
        {
            boolean lol = new File( output_folder ).mkdirs();
            System.out.println( "Could create working folder= " + Boolean.toString( lol ) );
        }

        String entryName = output_folder + separator + name;

        int n;
        FileOutputStream fileoutputstream;
        File newFile = new File( entryName );
        String directory = newFile.getParent();

        if( directory == null )
        {
            if( newFile.isDirectory() )
            {
                return null;
            }
        }

        try
        {
            fileoutputstream = new FileOutputStream( entryName );
        }
        catch( Exception e )
        {
            System.out.println( "EXCEPTION:   entryname " + entryName );
            e.printStackTrace();
            // zipentry = (JarEntry)zipinputstream.getNextEntry();
            // System.exit(0 );
            return null;
        }
        System.out.println( "entryname " + entryName );
        try
        {
            while( ( n = zipinputstream.read( buf, 0, 1024 ) ) > -1 )
            {
                fileoutputstream.write( buf, 0, n );
            }

            fileoutputstream.close();
            zipinputstream.close();
        }
        catch( Exception e )
        {
            e.printStackTrace();
            System.out.println( "OH NOES!" );
            return null;
        }

        return new File( entryName );

    }

    static
    {

        // extract needed shader files.

        getFileFromInputStream( Main.class.getResourceAsStream( "iso_colorful_frag.glsl.png" ), "iso_colorful_frag.glsl" );
        getFileFromInputStream( Main.class.getResourceAsStream( "scale_vshader.glsl.png" ), "scale_vshader.glsl" );
        getFileFromInputStream( Main.class.getResourceAsStream( "t3d_fragment.glsl.png" ), "t3d_fragment.glsl" );
        getFileFromInputStream( Main.class.getResourceAsStream( "t3d_g_fragment.glsl.png" ), "t3d_g_fragment.glsl" );
        getFileFromInputStream( Main.class.getResourceAsStream( "t3d_raycast.glsl.png" ), "t3d_raycast.glsl" );
        getFileFromInputStream( Main.class.getResourceAsStream( "iso_vert.glsl.png" ), "iso_vert.glsl" );
        getFileFromInputStream( Main.class.getResourceAsStream( "iso_frag.glsl.png" ), "iso_frag.glsl" );
        getFileFromInputStream( Main.class.getResourceAsStream( "iso_metal_vert.glsl.png" ), "iso_metal_vert.glsl" );
        getFileFromInputStream( Main.class.getResourceAsStream( "iso_plain_vert.glsl.png" ), "iso_plain_vert.glsl" );
        getFileFromInputStream( Main.class.getResourceAsStream( "phong_vert.glsl.png" ), "phong_vert.glsl" );
        getFileFromInputStream( Main.class.getResourceAsStream( "phong_frag.glsl.png" ), "phong_frag.glsl" );
    }

    static void init()
    {
        //System.setProperty( "java.library.path", working_folder + separator + "jogl.dll" );
        System.out.println( System.getProperty( "java.library.path" ) );

        File f = new File( "." );
        for( File f1 : f.listFiles() )
        {
            //System.out.println( f1.toString() );
        }

        try
        {
            //System.load( new File( working_folder ).getAbsolutePath() + separator + System.mapLibraryName( "jogl" ) );

            System.loadLibrary( "jogl" );

            System.out.println( "LOADED LIBRARIES" );

        }
        catch( Exception exc )
        {
            //exc.initCause( exc);
            exc.printStackTrace();
            System.out.println( "ZOMG" + exc.toString() );
            return;
        }

    }


    public static void main( String[] args )
    {
        //init();

        //System.loadLibrary( "jogl" );

        //System.loadLibrary( "jogl" );

        try
        {
            new Main();
        }
        catch( Exception exc )
        {
            exc.printStackTrace();
        }

    }

    public void actionPerformed( ActionEvent e )
    {
        String command = e.getActionCommand();

    }

    private void jbInit()
            throws Exception
    {
        main_thread = new Thread( this );
        main_thread.start();
    }


}
