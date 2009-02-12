package edu.ohiou.lev_neiman.jung.volume_render.ui.control.iso_surface.buttonz;

import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JButton;

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
public class ColorButton
        extends JButton
{
    static ImageIcon icon;

    public Color s_color = new Color( 0, 0, 0 );

    static
    {
        icon = new ImageIcon( ColorButton.class.getResource( "color_icon.png" ) );
    }

    public ColorButton()
    {
        super.setToolTipText( "Change color" );
        setPreferredSize( new Dimension( 15, 15 ) );
        //setBorder( BorderFactory.createEmptyBorder() );
    }

    public void paintComponent( Graphics g )
    {
        g.setColor( s_color );
        g.fillRect( 0, 0, super.getWidth(), super.getHeight() );

        //icon.paintIcon( this, g, 0, 0 );

    }
}
