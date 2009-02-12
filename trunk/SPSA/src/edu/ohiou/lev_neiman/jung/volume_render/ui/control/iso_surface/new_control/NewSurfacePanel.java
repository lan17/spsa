package edu.ohiou.lev_neiman.jung.volume_render.ui.control.iso_surface.new_control;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import edu.ohiou.lev_neiman.jung.volume_render.ui.control.iso_surface.IsoSurfaceControl_MainPanel;

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
public class NewSurfacePanel
        extends JPanel implements ActionListener
{
    private static String partition_text = "Partition data";
    private static String create_isosurface_text = "Generate using Gaussian settings";


    private Dimension button_dimension = new Dimension( 250, 15 );

    JButton partition_space = new JButton( partition_text );
    JButton add_iso_surface = new JButton( create_isosurface_text );

    public void actionPerformed( ActionEvent e )
    {
        String command = e.getActionCommand();

        if( command.equals( create_isosurface_text ) )
        {
            p.addIsoSurface();
        }
        if( command.equals( partition_text ) )
        {
            p.partitionSpace();
        }
    }

    IsoSurfaceControl_MainPanel p = null;

    public NewSurfacePanel( IsoSurfaceControl_MainPanel daddy )
    {
        super( new FlowLayout( FlowLayout.LEFT, 0, 0 ) );
        super.setPreferredSize( new Dimension( 300, 20 ) );
        p = daddy;
        partition_space.setPreferredSize( button_dimension );
        add_iso_surface.setPreferredSize( button_dimension );
        //add( partition_space );
        add( add_iso_surface );
        add_iso_surface.addActionListener( this );
        partition_space.addActionListener( this );

    }
}
