package edu.ohiou.lev_neiman.jung.volume_render.ui.control.data;

import java.awt.Dimension;

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
public class DataControl_MainPanel
        extends JPanel
{
    RandomDataControl random_data = new RandomDataControl();
    NewDataControl new_data = new NewDataControl();

    public DataControl_MainPanel()
    {
        super.setPreferredSize( new Dimension( 250, 500 ) );
        //add( random_data );
        add( new_data );

    }
}
