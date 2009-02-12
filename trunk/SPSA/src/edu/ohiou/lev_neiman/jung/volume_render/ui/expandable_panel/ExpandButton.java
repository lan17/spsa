package edu.ohiou.lev_neiman.jung.volume_render.ui.expandable_panel;

import java.awt.*;
import java.awt.event.ActionEvent;

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
public class ExpandButton
        extends JButton
{
    ExpandablePanel parent;
    private boolean expanded = true;
    public ExpandButton( ExpandablePanel parent )
    {
        super.setToolTipText( "Show/Hide" );
        this.parent = parent;
        //super.setBorder( javax.swing.BorderFactory.createLineBorder( new Color( 0,0,0 ) ));
        super.setBorderPainted( false );
        super.setPreferredSize( new Dimension( 20, 20 ) );
    }

    public boolean isExpanded()
    {
        return expanded;
    }

    public void setExpanded( boolean expanded )
    {
        this.expanded = expanded;
        parent.expansionPerformed( expanded );
        repaint();
    }

    public void paintComponent( Graphics g )
    {

        Graphics2D g2d = ( Graphics2D ) g;
        g2d.setColor( getBackground() );
        g2d.fillRect( 0, 0, 30, 30 );
        g2d.setColor( new Color( 0, 0, 0 ) );
        g2d.fillRect( 0, 8, 20, 4 );
        if( !expanded )
        {
            g2d.fillRect( 8, 0, 4, 20 );
        }

        //super.getBorder().paintBorder( this, g, 0, 0, 20, 20 );
    }

    protected void fireActionPerformed( ActionEvent event )
    {
        super.fireActionPerformed( event );
        setExpanded( !expanded );

    }
}
