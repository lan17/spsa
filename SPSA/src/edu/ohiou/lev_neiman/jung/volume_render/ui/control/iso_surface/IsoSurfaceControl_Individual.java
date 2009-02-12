package edu.ohiou.lev_neiman.jung.volume_render.ui.control.iso_surface;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;

import javax.swing.*;

import edu.ohiou.lev_neiman.jung.volume_render.ui.control.iso_surface.buttonz.*;
import edu.ohiou.lev_neiman.misc.Pair;
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
public class IsoSurfaceControl_Individual
        extends JPanel implements ActionListener
{

    edu.ohiou.lev_neiman.sceneapi.visualize.functionz.TransformFunction func;

    MarchingCubez surface;

    JCheckBox show_button = new JCheckBox( "" );
    ColorButton color_button = new ColorButton();
    InfoButton info_button = new InfoButton();
    DeleteButton delete_button = new DeleteButton();
    JLabel name_label;

    JColorChooser color_chooser = new JColorChooser( default_color );
    //MyColorChooser c_chooser = new MyColorChooser();

    static Color default_color = new Color( 0, 255, 0 );

    public IsoSurfaceContainer parent;

    public IsoSurfaceControl_Individual( MarchingCubez surface, String name,
                                         edu.ohiou.lev_neiman.sceneapi.visualize.functionz.TransformFunction func,
                                         IsoSurfaceContainer parent )
    {
        show_button.setToolTipText( "Show/Hide" );
        this.parent = parent;
        this.func = func;
        this.surface = surface;
        super.setPreferredSize( new Dimension( 300, 23 ) );
        //super.setSize( new Dimension( 300, 20 ) );
        //super.setBorder( BorderFactory.createLineBorder( new Color( 0, 0, 0 ) ) );

        super.setLayout( new FlowLayout() );
        ( ( FlowLayout )super.getLayout() ).setVgap( 0 );
        ( ( FlowLayout )super.getLayout() ).setHgap( 0 );

        this.name_label = new JLabel( " " + name );

        super.setLayout( new FlowLayout( FlowLayout.LEFT, 0, 0 ) );

        add( show_button );
        add( color_button );
        add( info_button );
        add( delete_button );
        add( name_label );

        name_label.setToolTipText( getInfoAsString() );

        show_button.setSelected( true );
        show_button.addActionListener( this );
        delete_button.addActionListener( this );
        info_button.addActionListener( this );
        color_button.addActionListener( this );

        color_button.s_color = surface.getColor();
    }

    private void deleteSelf()
    {
        //IsoSurfaceControl_MainPanel.surfaces.remove( surface );
        surface.deleteSelf();
        parent.deleteEntry( this );
    }

    public void actionPerformed( ActionEvent e )
    {
        Object source = e.getSource();
        if( source instanceof JCheckBox )
        {
            if( show_button.isSelected() )
            {
                surface.show = true;
            }
            else
            {
                surface.show = false;
            }
        }
        if( source instanceof DeleteButton )
        {
            int n = JOptionPane.showConfirmDialog(
                    edu.ohiou.lev_neiman.jung.volume_render.Main.frame,
                    "Are you sure you want to delete this surface?",
                    "Confirm Delete Surface",
                    JOptionPane.YES_NO_OPTION );
            System.out.println( n );
            if( n == 0 )
            {
                deleteSelf();
            }

        }
        if( source instanceof InfoButton )
        {
            showInfo();

        }
        if( source instanceof ColorButton )
        {

            Pair<Color, Float> pick_color = MyColorChooser.show( surface.getColor(), surface.getTransperency() );
            if( pick_color == null )
            {
                return;
            }

            System.out.println( pick_color.toString() );
            surface.setColor( pick_color.getFirst() );
            surface.setTransperency( pick_color.getSecond() );
            Collections.sort( IsoSurfaceControl_MainPanel.surfaces );

            color_button.s_color = pick_color.getFirst();
            color_button.repaint();
        }
    }

    public String getInfoAsString()
    {
        return " There are " + Integer.toString( surface.getNumTriangles() ) + " triangles.\nThere are " +
                Integer.toString( surface.getNumIncluded() ) + " / " +
                Integer.toString( surface.getN() * surface.getN() * surface.getN() ) +
                " = " +
                Double.toString( 100 * ( double ) surface.getNumIncluded() /
                                 ( double ) ( surface.getN() * surface.getN() * surface.getN() ) ) + "% included in this volume.";

    }

    private void showInfo()
    {
        JOptionPane.showMessageDialog( edu.ohiou.lev_neiman.jung.volume_render.Main.frame, getInfoAsString()
                );

    }
}
