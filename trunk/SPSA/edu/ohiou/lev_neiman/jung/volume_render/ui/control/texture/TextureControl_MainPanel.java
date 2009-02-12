package edu.ohiou.lev_neiman.jung.volume_render.ui.control.texture;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import edu.ohiou.lev_neiman.jung.volume_render.Main;
import edu.ohiou.lev_neiman.jung.volume_render.event.RenderEventQueue;
import edu.ohiou.lev_neiman.jung.volume_render.ui.Helper;
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
public class TextureControl_MainPanel
        extends JPanel implements ActionListener
{
    private ExpandablePanel general_control;
    private ExpandablePanel shader_control;

    private TextureShaderControl shader_panel = new TextureShaderControl();
    private JPanel general_panel = new JPanel( new FlowLayout( FlowLayout.LEFT, 0, 0 ) );

    private static String apply_slices_text = "Set";
    private static String show_hide_texture = "Show Texture";

    JLabel num_slices_label = new JLabel( "# of slices: " );
    JTextField num_slices = new JTextField( "128" );
    JButton apply_slices = new JButton( apply_slices_text );

    JCheckBox show_texture = new JCheckBox( show_hide_texture );

    private boolean show = true;

    public TextureControl_MainPanel()
    {

        general_panel.setPreferredSize( new Dimension( 300, 55 ) );
        general_control = new ExpandablePanel( "General", general_panel );

        shader_control = new ExpandablePanel( "Shaders", shader_panel );

        Helper.addPreferredHeight( this, shader_panel );

        num_slices_label.setPreferredSize( new Dimension( 70, 20 ) );
        num_slices.setPreferredSize( new Dimension( 40, 20 ) );
        apply_slices.setPreferredSize( new Dimension( 70, 20 ) );

        //show_texture.setPreferredSize( new Dimension( 300, 15 ) );

        JPanel input_panel = new JPanel();

        input_panel.setPreferredSize( new Dimension( 200, 70 ) );

        input_panel.add( num_slices_label );
        input_panel.add( num_slices );
        input_panel.add( apply_slices );

        Helper.addPreferredHeight( this, input_panel );

        super.setLayout( new FlowLayout( FlowLayout.LEFT, 0, 0 ) );
        super.setPreferredSize( new Dimension( 300, 220 ) );

        add( general_control );
        add( shader_control );
        general_panel.add( show_texture );
        general_panel.add( input_panel );
        //add( apply_slices );

        apply_slices.addActionListener( this );
        show_texture.addActionListener( this );

        show_texture.setSelected( true );

        validate();

    }

    public void actionPerformed( ActionEvent e )
    {
        String command = e.getActionCommand();
        System.out.println( command );
        if( command.equals( apply_slices_text ) )
        {
            String val = num_slices.getText();
            try
            {
                Integer i = Integer.parseInt( val );
                Main.setNumSlices( i );
            }
            catch( Exception exc )
            {
                num_slices.setText( Integer.toString( Main.getNumSlices() ) );
            }
        }
        if( command.equals( show_hide_texture ) )
        {
            show = !show;
            if( show )
            {
                Main.addRenderEvent( RenderEventQueue.Event.SHOW_VOLUME );
            }
            else
            {
                Main.addRenderEvent( RenderEventQueue.Event.HIDE_VOLUME );
            }

        }
    }
}
