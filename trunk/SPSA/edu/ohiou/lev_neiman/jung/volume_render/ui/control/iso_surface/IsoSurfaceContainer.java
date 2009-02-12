package edu.ohiou.lev_neiman.jung.volume_render.ui.control.iso_surface;

import java.awt.*;

import javax.swing.JPanel;

import edu.ohiou.lev_neiman.jung.volume_render.ui.expandable_panel.ExpandablePanel;
import edu.ohiou.lev_neiman.sceneapi.visualize.MarchingCubez;
import edu.ohiou.lev_neiman.sceneapi.visualize.functionz.TransformFunction;

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
public class IsoSurfaceContainer
        extends JPanel
{

    private int num_surfaces = 0;


    ExpandablePanel panel;

    String name;

    JPanel contents = new JPanel();

    public IsoSurfaceContainer( String name )
    {

        this.name = name;
        try
        {

            contents.setLayout( new FlowLayout( FlowLayout.LEADING, 0, 0 ) );
            contents.setPreferredSize( new Dimension( 300, 0 ) );

            panel = new ExpandablePanel( name, contents );
            setPreferredSize( panel.getPreferredSize() );

            add( panel );
        }
        catch( Exception exc )
        {
            exc.printStackTrace();
        }

    }

    public void deleteEntry( IsoSurfaceControl_Individual el )
    {
        num_surfaces--;
        edu.ohiou.lev_neiman.jung.volume_render.ui.Helper.subtractPreferredHeight( contents, el );
        contents.remove( el );

        panel.toggle();
        if( num_surfaces == 0 )
        {
            IsoSurfaceControl_MainPanel.removeContainer( this );
        }

    }

    public void addIsoSurface( MarchingCubez iso, TransformFunction func )
    {
        num_surfaces++;
        IsoSurfaceControl_Individual new_control = new IsoSurfaceControl_Individual( iso, name, func, this );

        contents.add( new_control );
        for( Component e = contents; e != null; e = e.getParent() )
        {
            edu.ohiou.lev_neiman.jung.volume_render.ui.Helper.addPreferredHeight( e, new_control );
        }

        // panel.toggle();
    }

}
