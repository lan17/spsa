package edu.ohiou.lev_neiman.jung.volume_render.ui.expandable_panel;

import java.awt.*;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import edu.ohiou.lev_neiman.jung.volume_render.ui.Helper;
import edu.ohiou.lev_neiman.jung.volume_render.ui.UnderLineLabel;


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
public class ExpandablePanel
        extends JPanel
{
    protected UnderLineLabel label;
    protected ExpandButton show_hide_button;
    protected JPanel content;

    protected Dimension expanded_dimension;
    protected static int p_width = 300;
    protected static final Dimension contracted_dimension = new Dimension( p_width, 25 );

    protected JPanel empty_left;

    private ExpandablePanel expandable_parent = null;
    private Vector<ExpandablePanel> children = new Vector<ExpandablePanel> ();


    public void setParent( Component n_p )
    {

        expandable_parent = ( ExpandablePanel ) n_p;
    }

    public ExpandablePanel parent()
    {
        return expandable_parent;
    }

    public void addChild( ExpandablePanel c )
    {
        children.add( c );
    }


    public ExpandablePanel( String label, JPanel content )
    {
        //content.setBorder( BorderFactory.createLineBorder( new Color( 0, 0, 0 ) ) );
        //super.setBorder( BorderFactory.createLineBorder( new Color( 0, 0, 0 ) ) );

        empty_left = new JPanel()
        {
            public void paintComponent( Graphics g )
            {
                // do not do anything.
            }
        };
        empty_left.setPreferredSize( new Dimension( 20, 20 ) );
        empty_left.setSize( new Dimension( 20, 20 ) );

        expanded_dimension = new Dimension( p_width, 27 + ( int ) content.getPreferredSize().height );
        super.setPreferredSize( expanded_dimension );
        super.setLayout( new BorderLayout() );
        ( ( BorderLayout )super.getLayout() ).setHgap( 0 );
        ( ( BorderLayout )super.getLayout() ).setVgap( 0 );
        this.content = content;
        this.label = new UnderLineLabel( label );
        show_hide_button = new ExpandButton( this );

        JPanel title = new JPanel( new FlowLayout( FlowLayout.LEADING, 1, 1 ) );
        title.setBorder( BorderFactory.createEmptyBorder() );

        title.add( show_hide_button );
        title.add( this.label );
        title.setPreferredSize( new Dimension( 275, 25 ) );
        add( title, BorderLayout.NORTH );
        add( empty_left, BorderLayout.WEST );
        add( content, BorderLayout.CENTER );

        //expansionPerformed( true );
    }


    private boolean expansion = true;
    public void expansionPerformed( boolean expansion )
    {
        this.expansion = expansion;
        if( expansion )
        {

            expanded_dimension = new Dimension( p_width, 27 + ( int ) content.getPreferredSize().getHeight() );
            setPreferredSize( expanded_dimension );

            for( Component e = this.getParent(); e != null; e = e.getParent() )
            {
                Helper.addPreferredHeight( e, content );
                if( e.getParent() == null )
                {
                    e.repaint();
                }
            }
            content.setVisible( true );

        }
        else
        {

            setPreferredSize( this.contracted_dimension );
            //Helper.subtractPreferredHeight( this, content );

            for( Component e = this.getParent(); e != null; e = e.getParent() )
            {
                Helper.subtractPreferredHeight( e, content );
                if( e.getParent() == null )
                {
                    e.repaint();
                }
            }
            content.setVisible( false );

        }
        content.validate();
        content.repaint();
        /*
                 for( Component e = content; e != null; e = e.getParent() )
                 {
            e.validate();
            e.repaint();
                 }
         */

    }

    public void toggle()
    {
        expansionPerformed( !expansion );
        expansionPerformed( !expansion );

    }
}
