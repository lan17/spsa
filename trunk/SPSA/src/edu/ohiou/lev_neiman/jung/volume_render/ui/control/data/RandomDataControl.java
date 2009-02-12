package edu.ohiou.lev_neiman.jung.volume_render.ui.control.data;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

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
public class RandomDataControl
        extends JPanel implements ActionListener
{
    private static String generate_random_text = "Genreate Random Volume";

    private static JButton generate_random_button = new JButton( generate_random_text );

    public RandomDataControl()
    {
        add( generate_random_button );
        generate_random_button.addActionListener( this );
    }

    public void actionPerformed( ActionEvent e )
    {
        NewDataControl.setNewRandomData( 50, 100, 50, .5f );
    }
}
