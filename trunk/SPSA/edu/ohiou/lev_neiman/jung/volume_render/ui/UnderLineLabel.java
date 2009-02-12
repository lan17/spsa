package edu.ohiou.lev_neiman.jung.volume_render.ui;

import java.awt.*;

import javax.swing.JLabel;

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
public class UnderLineLabel
        extends JLabel
{
    private String label;
    public UnderLineLabel( String text )
    {
        super.setPreferredSize( new Dimension( 250, 20 ) );
        this.label = text;
    }

    public void paintComponent( Graphics g )
    {
        Graphics2D g2d = ( Graphics2D ) g;
        g2d.setColor( new Color( 0, 0, 0 ) );
        g2d.drawString( label, 5, 12 );
        g2d.drawLine( 5, 14, 10000000, 14 );
    }

}
