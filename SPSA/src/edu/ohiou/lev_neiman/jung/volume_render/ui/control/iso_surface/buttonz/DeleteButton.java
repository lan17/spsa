package edu.ohiou.lev_neiman.jung.volume_render.ui.control.iso_surface.buttonz;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.*;

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
public class DeleteButton
        extends JButton
{

    static ImageIcon icon;

    static
    {
        icon = new ImageIcon( DeleteButton.class.getResource( "trash_icon.png" ) );
    }


    public DeleteButton()
    {
        super.setToolTipText( "Delete" );
        super.setPreferredSize( new Dimension( 15, 15 ) );
        super.setBorder( BorderFactory.createEmptyBorder() );
    }

    public void paintComponent( Graphics g )
    {

        g.setColor( super.getBackground() );
        g.fillRect( 0, 0, super.getWidth(), super.getHeight() );

        icon.paintIcon( this, g, 0, 0 );

    }
}
