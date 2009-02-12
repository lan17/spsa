package edu.ohiou.lev_neiman.jung.volume_render;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.*;

import edu.ohiou.lev_neiman.jung.volume_render.ui.control.data.DataControl_MainPanel;

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
public class ControlPanel
        extends JPanel
{
    private JTabbedPane menu = new JTabbedPane();

    private MethodPanel method_control = new MethodPanel();
    private ScreenshotPanel screen_shot_control = new ScreenshotPanel();
    private DataControl_MainPanel data_control = new DataControl_MainPanel();

    public static JFileChooser file_chooser = new JFileChooser( System.getProperty( "user.home" ) );

    public ControlPanel()
    {
        Dimension s = new Dimension( 300, 600 );
        super.setSize( s );
        super.setPreferredSize( s );
        //menu.add( "Shaders", shader_control );
        menu.add( "Method", method_control );
        menu.add( "Screenshot", screen_shot_control );
        menu.add( "Data", data_control );

        super.setLayout( new BorderLayout() );

        super.add( menu, BorderLayout.CENTER );
    }
}
