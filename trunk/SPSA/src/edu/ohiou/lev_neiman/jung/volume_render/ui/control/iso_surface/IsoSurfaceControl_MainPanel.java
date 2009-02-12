package edu.ohiou.lev_neiman.jung.volume_render.ui.control.iso_surface;


import java.awt.*;
import java.util.*;

import javax.swing.JPanel;

import edu.ohiou.lev_neiman.jung.volume_render.Main;
import edu.ohiou.lev_neiman.jung.volume_render.ui.Helper;
import edu.ohiou.lev_neiman.jung.volume_render.ui.control.data.NewDataControl;
import edu.ohiou.lev_neiman.jung.volume_render.ui.control.iso_surface.new_control.NewSurfacePanel;
import edu.ohiou.lev_neiman.jung.volume_render.ui.control.iso_surface.shaders.SurfaceShader_MainPanel;
import edu.ohiou.lev_neiman.jung.volume_render.ui.expandable_panel.ExpandablePanel;
import edu.ohiou.lev_neiman.sceneapi.visualize.MarchingCubez;
import edu.ohiou.lev_neiman.sceneapi.visualize.functionz.GaussianDistribution;
import edu.ohiou.lev_neiman.sceneapi.visualize.functionz.Interval;

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
public class IsoSurfaceControl_MainPanel
        extends JPanel implements Comparator
{
    public static boolean need_to_delete_surfaces = false;

    /**
     * data structure to keep track of all iso-surfaces.
     */
    public static LinkedList<MarchingCubez> surfaces;

    ExpandablePanel shader_control_panel;
    ExpandablePanel new_surface_panel;
    ExpandablePanel data_panel;

    static SurfaceShader_MainPanel shader_control = new SurfaceShader_MainPanel();
    static JPanel iso_surfaces = new JPanel();
    static NewSurfacePanel new_panel = null;

    Dimension size_iso = new Dimension( 300, 20 );

    static TreeMap<String, IsoSurfaceContainer> groups = new TreeMap<String, IsoSurfaceContainer> ();


    public IsoSurfaceControl_MainPanel()
    {
        surfaces = new LinkedList<MarchingCubez> ();
        new_panel = new NewSurfacePanel( this );

        super.setLayout( new BorderLayout() );
        super.setPreferredSize( new Dimension( 300, 250 ) );
        iso_surfaces.setLayout( new FlowLayout( FlowLayout.LEADING, 0, 0 ) );
        //iso_surfaces.setBorder( BorderFactory.createLineBorder( new Color( 0, 0, 0 ) ));
        iso_surfaces.setPreferredSize( size_iso );

        shader_control_panel = new ExpandablePanel( "Surface Shaders", shader_control );
        new_surface_panel = new ExpandablePanel( "Surface Generator", new_panel );
        data_panel = new ExpandablePanel( "Surface Manager", iso_surfaces );

        JPanel controlz_panel = new JPanel( new FlowLayout( FlowLayout.LEFT, 0, 0 ) );
        controlz_panel.setPreferredSize( new Dimension( 300, 100 ) );
        controlz_panel.add( shader_control_panel );
        controlz_panel.add( new_surface_panel );

        super.add( controlz_panel, BorderLayout.NORTH );
        super.add( data_panel, BorderLayout.CENTER );
        ( ( BorderLayout )super.getLayout() ).setVgap( 0 );
        ( ( BorderLayout )super.getLayout() ).setHgap( 0 );
        //add_iso_surface.addActionListener( this );

    }

    public static void removeContainer( IsoSurfaceContainer c )
    {
        iso_surfaces.remove( ( Component ) c );
        iso_surfaces.validate();

        for( Map.Entry<String, IsoSurfaceContainer> e : groups.entrySet() )
        {
            if( e.getValue() == c )
            {
                groups.remove( e.getKey() );
                return;
            }
        }
    }


    private static void printDimension( Dimension t )
    {
        System.out.println( "{ " + Integer.toString( t.width ) + ", " + Integer.toString( t.height ) + "}" );
    }

    public void removeIsoSurfaceContainer( IsoSurfaceContainer iso )
    {
        iso_surfaces.remove( iso );
        validate();
        repaint();
    }

    public void partitionSpace()
    {
        String f_name = NewDataControl.getCurrentName() + "(Partition)";
        IsoSurfaceContainer container;
        if( groups.containsKey( f_name ) )
        {
            container = groups.get( f_name );
        }
        else
        {
            container = new IsoSurfaceContainer( f_name );
            groups.put( f_name, container );
            iso_surfaces.add( container );
        }

        float step = .2f;
        for( float a = 0f, b = step; b <= 1f; a += step, b += step )
        {
            Interval func = new Interval( a, b );
            //GaussianDistribution func = new GaussianDistribution( 1, a, .05f );
            MarchingCubez iso = new MarchingCubez( Main.getCurrentDataBuffer(), func, .5f );
            container.addIsoSurface( iso, func );
            surfaces.add( iso );
        }
        Collections.sort( surfaces );
        Helper.validateAllParents( iso_surfaces );

    }


    public void addIsoSurface()
    {
        try
        {
            IsoSurfaceContainer container;
            if( groups.containsKey( NewDataControl.getCurrentName() ) )
            {
                container = groups.get( NewDataControl.getCurrentName() );
            }
            else
            {
                container = new IsoSurfaceContainer( NewDataControl.getCurrentName() );
                groups.put( NewDataControl.getCurrentName(), container );
                iso_surfaces.add( container );
                //Helper.addPreferredHeight( iso_surfaces, container );

            }
            GaussianDistribution func = Main.getGaussianFunction();
            MarchingCubez iso = new MarchingCubez( Main.getCurrentDataBuffer(), func, .01f );

            container.addIsoSurface( iso, func );

            surfaces.add( iso );
            Collections.sort( surfaces );
        }
        catch( Exception exc )
        {
            exc.printStackTrace();
        }
        System.out.println( "updating UI" );

        Helper.validateAllParents( iso_surfaces );

    }

    public int compare( Object o1, Object o2 )
    {
        if( o1 instanceof MarchingCubez && o2 instanceof MarchingCubez )
        {
            MarchingCubez a = ( MarchingCubez ) o1;
            MarchingCubez b = ( MarchingCubez ) o2;
            return Float.compare( a.getTransperency(), b.getTransperency() );
        }
        return 0;
    }

}
