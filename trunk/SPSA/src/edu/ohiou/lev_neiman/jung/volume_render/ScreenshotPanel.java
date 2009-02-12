package edu.ohiou.lev_neiman.jung.volume_render;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.*;

import edu.ohiou.lev_neiman.jung.volume_render.event.*;

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
public class ScreenshotPanel
        extends JPanel implements ActionListener, RenderEventListener
{
    JLabel width_label = new JLabel( "Width: " );
    JLabel height_label = new JLabel( "Height: " );

    JTextField width_text = new JTextField( "1024" );
    JTextField height_text = new JTextField( "840" );

    JLabel num_slices = new JLabel( "Number of Slices: " );
    JTextField num_slices_text = new JTextField( "1000" );

    static String one_screenshot_text = "Take a screenshot of current data";
    static String many_screenshot_text = "Take screenshots of all files";

    JButton take_one_screenshot = new JButton( one_screenshot_text );
    JButton many_screenshot = new JButton( many_screenshot_text );

    private JFileChooser screenshot_file_chooser = new JFileChooser( System.getProperty( "user.home" ) );

    private Dimension text_field_dimension = new Dimension( 50, 20 );

    public ScreenshotPanel()
    {
        Main.addRenderEventListener( this );
        // super.setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
        super.setPreferredSize( new Dimension( 250, 600 ) );

        width_text.setPreferredSize( text_field_dimension );
        height_text.setPreferredSize( text_field_dimension );
        num_slices_text.setPreferredSize( text_field_dimension );

        JPanel slices_control = new JPanel();
        slices_control.add( num_slices );
        slices_control.add( num_slices_text );
        slices_control.setPreferredSize( new Dimension( 220, 30 ) );

        JPanel width_control = new JPanel();
        width_control.add( width_label );
        width_control.add( width_text );

        JPanel height_control = new JPanel();
        height_control.add( height_label );
        height_control.add( height_text );

        add( slices_control );
        //add( width_control );
        //add( height_control );
        add( take_one_screenshot );
        add( many_screenshot );

        many_screenshot.addActionListener( this );
        take_one_screenshot.addActionListener( this );
    }

    private int t_num_slices;

    public void actionPerformed( ActionEvent e )
    {
        String command = e.getActionCommand();
        if( command.equals( this.one_screenshot_text ) )
        {
            try
            {
                t_num_slices = Main.getNumSlices();
                Main.setNumSlices( Integer.parseInt( num_slices_text.getText() ) );
                Main.addRenderEvent( RenderEventQueue.Event.TAKE_SCREENSHOT );
                take_one_screenshot.setEnabled( false );
            }
            catch( Exception exc )
            {
                exc.printStackTrace();
                return;
            }
        }

        if( command.equals( this.many_screenshot ) )
        {

        }

    }

    public void renderEventHappened( GenericEvent event )
    {
        if( ! ( event.getCarryOn() instanceof BufferedImage ) )
        {
            return;
        }

        if( screenshot_file_chooser.showSaveDialog( null ) == screenshot_file_chooser.APPROVE_OPTION )
        {
            Main.writePNG( new File( screenshot_file_chooser.getSelectedFile().getAbsolutePath() ), ( BufferedImage ) event.getCarryOn() );
        }
        Main.addRenderEvent( RenderEventQueue.Event.SCREENSHOT_TAKEN );
        Main.setNumSlices( t_num_slices );
        take_one_screenshot.setEnabled( true );
    }


}
