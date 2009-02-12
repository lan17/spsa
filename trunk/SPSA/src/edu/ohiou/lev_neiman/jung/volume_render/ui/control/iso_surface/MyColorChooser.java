package edu.ohiou.lev_neiman.jung.volume_render.ui.control.iso_surface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.ohiou.lev_neiman.jung.volume_render.Main;
import edu.ohiou.lev_neiman.misc.Pair;

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
public class MyColorChooser
        extends JDialog implements ChangeListener
{
    static JPanel content = new JPanel();
    static JColorChooser color_chooser = new JColorChooser();

    JLabel transperency_label = new JLabel( "Transperency: " );
    JTextField transperency_field = new JTextField( "1" );
    float transperency = 1f;

    private static Dimension text_field_dimension = new Dimension( 40, 20 );

    Dimension d_size = new Dimension( 500, 400 );

    private static MyColorChooser dialog = null;

    private static boolean keep_showing = true;

    private MyColorChooser()
    {
        super( Main.frame, "Pick a color", true );
        super.setContentPane( content );
        super.setSize( d_size );
        super.setResizable( false );

        super.addWindowListener( new WindowAdapter()
        {
            public void windowClosing( WindowEvent e )
            {
                keep_showing = false;
            }
        }
        );

        content.setPreferredSize( d_size );
        content.setLayout( new BoxLayout( content, BoxLayout.Y_AXIS ) );

        JPanel transperency_panel = new JPanel();
        transperency_field.setPreferredSize( text_field_dimension );
        transperency_panel.add( transperency_label );
        transperency_panel.add( transperency_field );

        content.add( color_chooser );
        content.add( transperency_panel );

        color_chooser.getSelectionModel().addChangeListener( this );

    }

    public void stateChanged( ChangeEvent e )
    {
        Color c = color_chooser.getColor();
    }

    public static Pair<Color, Float> show( Color c, Float transperency )
    {
        if( dialog == null )
        {
            dialog = new MyColorChooser();
        }
        color_chooser.setColor( c );
        dialog.transperency_field.setText( Float.toString( transperency ) );
        dialog.setVisible( true );
        return new Pair<Color, Float> ( color_chooser.getColor(), Float.parseFloat( dialog.transperency_field.getText() ) );
    }


}
