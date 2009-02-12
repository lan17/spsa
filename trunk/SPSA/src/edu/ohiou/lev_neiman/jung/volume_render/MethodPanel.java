package edu.ohiou.lev_neiman.jung.volume_render;

import java.awt.*;

import javax.swing.*;

import edu.ohiou.lev_neiman.jung.volume_render.ui.control.texture.TextureControl_MainPanel;
import edu.ohiou.lev_neiman.jung.volume_render.ui.expandable_panel.ExpandablePanel;

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
public class MethodPanel
        extends JPanel

{


    public static ExpandablePanel texture_control;
    public static ExpandablePanel iso_surface_control;
    public static MethodPanel instance = null;

    Container containaz;
    JScrollPane scroll_pane;

    public MethodPanel()
    {
        instance = this;
        super.setPreferredSize( new Dimension( 280, 500 ) );
        super.setLayout( new BorderLayout() );

        texture_control = new ExpandablePanel( "3D Texture Control", new TextureControl_MainPanel() );
        iso_surface_control = new ExpandablePanel( "Iso Surface Control",
                new edu.ohiou.lev_neiman.jung.volume_render.ui.control.iso_surface.IsoSurfaceControl_MainPanel() );
        //texture_control.setPreferredSize( new Dimension( 300, 150 ) );

        containaz = new JPanel();
        containaz.setLayout( new FlowLayout( FlowLayout.LEFT, 0, 0 ) );
        containaz.setPreferredSize( new Dimension( 280, 450 ) );
        // containaz.setSize( new Dimension( 280, 500 ) );

        containaz.add( texture_control );
        containaz.add( iso_surface_control );

        scroll_pane = new JScrollPane( containaz, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
        scroll_pane.setPreferredSize( new Dimension( 280, 500 ) );
        scroll_pane.setBorder( BorderFactory.createEmptyBorder() );

        add( scroll_pane, BorderLayout.CENTER );

    }


}
