package edu.ohiou.lev_neiman.jung.volume_render.ui.control.iso_surface.shaders;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import edu.ohiou.lev_neiman.sceneapi.visualize.MarchingCubez;

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
public class SurfaceShader_MainPanel
        extends JPanel implements ActionListener
{


    String[] options =
                       {"Fixed Function", "Phong", "Plain"};
    JComboBox shader_menu = new JComboBox( options );

    public SurfaceShader_MainPanel()
    {
        super( new FlowLayout( FlowLayout.LEFT, 0, 0 ), true );
        super.setPreferredSize( new Dimension( 250, 25 ) );

        add( shader_menu );

        shader_menu.addActionListener( this );

    }

    public void actionPerformed( ActionEvent e )
    {
        String command = ( String ) shader_menu.getItemAt( shader_menu.getSelectedIndex() );

        System.out.println( shader_menu.getItemAt( shader_menu.getSelectedIndex() ) );
        if( command.equals( "Fixed Function" ) )
        {
            MarchingCubez.changeShader( MarchingCubez.ShaderType.FixedFunction );
        }
        if( command.equals( "Plain" ) )
        {
            MarchingCubez.changeShader( MarchingCubez.ShaderType.Plain );
        }

        if( command.equals( "Colorful" ) )
        {
            MarchingCubez.changeShader( MarchingCubez.ShaderType.Colorful );
        }
        if( command.equals( "Metal" ) )
        {
            MarchingCubez.changeShader( MarchingCubez.ShaderType.Metal );
        }
        if( command.equals( "Phong" ) )
        {
            MarchingCubez.changeShader( MarchingCubez.ShaderType.Phong );
        }

    }


}
