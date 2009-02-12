package edu.ohiou.lev_neiman.jung.volume_render.ui.control.texture;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.ohiou.lev_neiman.jung.volume_render.Main;
import edu.ohiou.lev_neiman.jung.volume_render.event.RenderEventQueue;

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
public class TextureShaderControl
        extends JPanel implements ActionListener, ChangeListener
{
    public TextureShaderControl()
    {
        this_s = this;
        try
        {
            jbInit();
        }
        catch( Exception ex )
        {
            ex.printStackTrace();
        }
    }

    private static TextureShaderControl this_s;

    public static final String check_box_text = "Use Gaussian";
    public static final String button_text = "Apply";

    private static Dimension slider_dimension = new Dimension( 140, 20 );
    private static Dimension text_field_dimension = new Dimension( 40, 20 );

    private static JSlider mean_slider = new JSlider();
    private static JSlider deviation_slider = new JSlider();
    private static JSlider factor_slider = new JSlider();

    private static JCheckBox use_gaussian = new JCheckBox( check_box_text );

    private static JLabel factor_label = new JLabel( "Factor: " );
    private static JTextField factor_text = new JTextField( ".5" );

    private static JLabel mean_label = new JLabel( "Mean: " );
    private static JTextField mean_text = new JTextField( ".2" );

    private static JLabel deviation_label = new JLabel( "Dev: " );
    private static JTextField deviation_text = new JTextField( ".05" );

    private static JButton apply_button = new JButton( button_text );

    private JPanel emptyPanle()
    {
        JPanel ret = new JPanel();
        ret.setPreferredSize( new Dimension( 300, 0 ) );
        return ret;
    }

    private void jbInit()
            throws Exception
    {
        super.setPreferredSize( new Dimension( 300, 110 ) );
        LayoutManager layout = new FlowLayout( FlowLayout.LEFT, 0, 0 );

        super.setLayout( layout );

        // set up listeners for text fields
        factor_text.addActionListener( this );
        mean_text.addActionListener( this );
        deviation_text.addActionListener( this );

        // set up sizes for the text fields....
        mean_text.setPreferredSize( text_field_dimension );
        deviation_text.setPreferredSize( text_field_dimension );
        factor_text.setPreferredSize( text_field_dimension );

        mean_label.setPreferredSize( text_field_dimension );
        deviation_label.setPreferredSize( text_field_dimension );
        factor_text.setPreferredSize( text_field_dimension );

        //set up sliders
        mean_slider.setPreferredSize( slider_dimension );
        mean_slider.setMinorTickSpacing( 1 );
        mean_slider.setMajorTickSpacing( 10 );
        mean_slider.setMinimum( 0 );
        mean_slider.setMaximum( 1000 );
        mean_slider.setValue( 200 );
        //mean_slider.setPaintLabels( true );
        // mean_slider.setPaintTicks( true );
        deviation_slider.setPreferredSize( slider_dimension );
        deviation_slider.setMinorTickSpacing( 1 );
        deviation_slider.setMajorTickSpacing( 50 );
        deviation_slider.setMinimum( 0 );
        deviation_slider.setMaximum( 250 );
        deviation_slider.setValue( 50 );
        //deviation_slider.setPaintLabels( true );
        //deviation_slider.setPaintTicks( true );
        factor_slider.setPreferredSize( slider_dimension );
        factor_slider.setMinorTickSpacing( 1 );
        factor_slider.setMajorTickSpacing( 50 );
        factor_slider.setMinimum( 0 );
        factor_slider.setMaximum( 1000 );
        factor_slider.setValue( 500 );

        mean_slider.addChangeListener( this );
        deviation_slider.addChangeListener( this );
        factor_slider.addChangeListener( this );
        // add some action listneres

        use_gaussian.addActionListener( this );
        apply_button.addActionListener( this );

        JPanel fields = new JPanel( new FlowLayout( FlowLayout.LEFT, 0, 0 ) );
        JPanel fields_label = new JPanel( new FlowLayout( FlowLayout.LEFT, 4, 0 ) );
        JPanel fields_text = new JPanel( new FlowLayout( FlowLayout.LEFT, 2, 0 ) );
        fields_label.setPreferredSize( new Dimension( 40, 60 ) );
        fields_text.setPreferredSize( new Dimension( 60, 60 ) );

        fields.add( fields_label );
        fields.add( fields_text );

        JPanel slidaz = new JPanel( new FlowLayout( FlowLayout.LEFT, 0, 0 ) );

        fields.setPreferredSize( new Dimension( 100, 60 ) );
        slidaz.setPreferredSize( new Dimension( 140, 60 ) );

        Dimension field_dimension = new Dimension( 100, 20 );

        // add stuff to the actual panel
        add( use_gaussian );
        add( emptyPanle() );
        // adding stuff for factor
        fields_label.add( factor_label );
        fields_text.add( factor_text );
        //factor_field_panel.setPreferredSize( new Dimension( 300, 20 ) );

        slidaz.add( factor_slider );
        //add( emptyPanle() );
        // adding fields for mean....

        fields_label.add( mean_label );
        fields_text.add( mean_text );
        slidaz.add( mean_slider );
        //add( emptyPanle() );
        // adding fields for deviation....

        fields_label.add( deviation_label );
        fields_text.add( deviation_text );
        slidaz.add( deviation_slider );
        //add( emptyPanle() );

        add( fields );
        add( slidaz );
        add( this.emptyPanle() );

        JPanel apply_button_panel = new JPanel( new FlowLayout( FlowLayout.CENTER, 0, 0 ) );
        apply_button_panel.setPreferredSize( new Dimension( 250, 30 ) );
        apply_button_panel.add( apply_button );
        add( apply_button_panel );

        use_gaussian.setSelected( true );
        //setEnabledAll( true );
        enable_all = true;

        apply_button.setPreferredSize( new Dimension( 70, 20 ) );

    }

    private boolean enable_all;
    public void setEnabledAll( boolean f )
    {
        enable_all = f;
        mean_text.setEnabled( f );
        deviation_text.setEnabled( f );
        mean_slider.setEnabled( f );
        deviation_slider.setEnabled( f );
        factor_slider.setEnabled( true );

        if( f )
        {
            Main.addRenderEvent( RenderEventQueue.Event.USE_GAUSSIAN );
            updateCanvas();
        }
        else
        {
            Main.addRenderEvent( RenderEventQueue.Event.USE_LINEAR );
        }
    }

    public void actionPerformed( ActionEvent e )
    {
        String command = e.getActionCommand();
        System.out.println( command );
        if( command.equals( this.check_box_text ) )
        {
            setEnabledAll( !enable_all );
        }
        if( command.equals( this.button_text ) )
        {
            try
            {
                updateCanvas();
            }
            catch( Exception exc )
            {
                exc.printStackTrace();
                return;

            }
        }

    }

    public static void setValues( float f, float m, float d )
    {
        mean_text.setText( Float.toString( m ) );
        deviation_text.setText( Float.toString( d ) );
        factor_text.setText( Float.toString( f ) );
        deviation_slider.setValue( ( int ) ( d * 1000f ) );
        mean_slider.setValue( ( int ) ( m * 1000f ) );
        factor_slider.setValue( ( int ) ( f * 1000f ) );
        this_s.updateCanvas();
        this_s.repaint();

    }

    private boolean first_run = true;
    public void updateCanvas()
    {
        float mean, dev, factor;
        try
        {
            mean = Float.valueOf( mean_text.getText() );
            dev = Float.valueOf( deviation_text.getText() );
            factor = Float.valueOf( factor_text.getText() );
        }
        catch( Exception exc )
        {

            int v = mean_slider.getValue();
            float a = v / 1000f;
            mean_text.setText( Float.toString( a ) );
            v = deviation_slider.getValue();
            a = v / 1000f;
            deviation_text.setText( Float.toString( a ) );
            v = factor_slider.getValue();
            a = v / 1000f;
            factor_text.setText( Float.toString( a ) );

            repaint();

            System.err.println( "OMG OMG " );

            return;
        }
        if( !first_run )
        {

            Main.setTextureVariables( factor, mean, dev );
        }
        else
        {
            first_run = false;
        }
    }

    public void stateChanged( ChangeEvent e )
    {
        Object source = e.getSource();
        if( source == mean_slider )
        {
            int v = mean_slider.getValue();
            float a = v / 1000f;
            mean_text.setText( Float.toString( a ) );
        }
        if( source == deviation_slider )
        {
            int v = deviation_slider.getValue();
            float a = v / 1000f;
            deviation_text.setText( Float.toString( a ) );
        }
        if( source == factor_slider )
        {
            int v = factor_slider.getValue();
            float a = v / 1000f;
            factor_text.setText( Float.toString( a ) );
        }

        updateCanvas();
    }


}
