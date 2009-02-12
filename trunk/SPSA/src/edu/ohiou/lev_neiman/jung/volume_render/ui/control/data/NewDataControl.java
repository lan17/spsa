package edu.ohiou.lev_neiman.jung.volume_render.ui.control.data;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.ohiou.lev_neiman.jung.volume_render.ControlPanel;
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
public class NewDataControl
        extends JPanel implements ListSelectionListener, ActionListener
{
    static String open_file_text = "Load Files";
    static String remove_file_text = "Remove Files";
    static String next_file_text = "Next";
    static String prev_file_text = "Previous";

    JComboBox combo_box = new JComboBox();

    JTable data_table = new JTable();
    JButton open_filez = new JButton( open_file_text );
    JButton remove_filez = new JButton( remove_file_text );
    JButton prev_file = new JButton( prev_file_text );
    JButton next_file = new JButton( next_file_text );

    private static File current_file = null;
    private static String current_name = null;

    private static int random_num = 0;

    // DataTableModel table_model;
    public NewDataControl()
    {
        try
        {
            jbInit();
        }
        catch( Exception ex )
        {
            ex.printStackTrace();
        }

    }

    private void jbInit()
            throws Exception
    {
        super.setPreferredSize( new Dimension( 280, 500 ) );
        super.setSize( new Dimension( 280, 500 ) );

        /*
                 table_model = new DataTableModel();
                data_table.setModel( table_model );
                //data_table.setCellSelectionEnabled( true );
               // data_table.setColumnSelectionAllowed( true );
                //data_table.setRowSelectionAllowed( true );
                data_table.setDefaultRenderer( Object.class, table_model);
                data_table.getSelectionModel().addListSelectionListener( this );
                data_table.setDragEnabled(true );

                 JScrollPane scrolla = new JScrollPane(data_table);
                // data_table.setPreferredSize( new Dimension( 200, 300 ));
                // data_table.setSize( new Dimension( 200, 300 ));

                 //scrolla.add( data_table );

                 scrolla.setAutoscrolls( true );
                 //scrolla.setPreferredSize(  new Dimension(   280, 400 ));


                 add( scrolla, BorderLayout.CENTER);
         */


        super.setLayout( new BorderLayout() );

        combo_box.addActionListener( this );
        combo_box.setPreferredSize( new Dimension( 270, 20 ) );
        //add( combo_box, BorderLayout.CENTER );



        JPanel buttonz = new JPanel();
        buttonz.add( open_filez );
        //buttonz.add( remove_filez );
        buttonz.add( combo_box );
        buttonz.add( prev_file );
        buttonz.add( next_file );

        buttonz.setPreferredSize( new Dimension( 300, 100 ) );
        add( buttonz, BorderLayout.NORTH );

        open_filez.addActionListener( this );
        remove_filez.addActionListener( this );
        prev_file.addActionListener( this );
        next_file.addActionListener( this );
    }

    public void valueChanged( ListSelectionEvent e )
    {
        e.getFirstIndex();
        int[] rows = data_table.getSelectedRows();
        data_table.getSelectedColumnCount();
        System.out.println( data_table.getSelectedRow() );
    }

    File[] cur_files = null;

    public void actionPerformed( ActionEvent e )
    {
        String command = e.getActionCommand();
        Object source = e.getSource();
        if( source instanceof JButton )
        {
            if( command.equals( open_file_text ) )
            {
                ControlPanel.file_chooser.setMultiSelectionEnabled( true );
                ControlPanel.file_chooser.setFileSelectionMode( JFileChooser.FILES_ONLY );
                if( ControlPanel.file_chooser.showOpenDialog( null ) == JFileChooser.APPROVE_OPTION )
                {
                    File[] new_files = ControlPanel.file_chooser.getSelectedFiles();
                    //table_model.addFilez( new_files );
                    Arrays.sort( new_files );
                    cur_files = null;
                    cur_files = new_files;
                    combo_box.removeAllItems();
                    for( File f : new_files )
                    {
                        combo_box.addItem( f.getName() );
                    }
                    combo_box.repaint();
                    combo_box.setSelectedIndex( 0 );

                }
            }
            if( command.equals( this.remove_file_text ) )
            {
                int[] rows = data_table.getSelectedRows();
                //table_model.removeFilez( rows );
            }
            if( command.equals( this.prev_file_text ) )
            {
                int i = combo_box.getSelectedIndex();
                if( i == -1 || i == 0 )
                {
                    return;
                }
                i--;
                combo_box.setSelectedIndex( i );

                File f = cur_files[ combo_box.getSelectedIndex() ];
                setNewData( f );

            }
            if( command.equals( this.next_file_text ) )
            {
                if( cur_files == null )
                {
                    return;
                }
                int i = combo_box.getSelectedIndex();
                if( i == cur_files.length - 1 )
                {
                    i = cur_files.length - 1;
                }
                else
                {
                    i++;
                }
                combo_box.setSelectedIndex( i );
                File f = cur_files[ combo_box.getSelectedIndex() ];
                setNewData( f );
            }
        }
        if( source instanceof JComboBox )
        {
            System.out.println( "Doing stuff" );
            if( combo_box.getSelectedIndex() == -1 )
            {
                return;
            }
            try
            {
                File f = cur_files[ combo_box.getSelectedIndex() ];
                setNewData( f );
            }
            catch( Exception exc )
            {
                exc.printStackTrace();
            }
        }
    }

    public static String getCurrentName()
    {
        if( current_file != null )
        {
            return current_file.getName();
        }
        else
        {
            return current_name;
        }
    }

    public static File getCurrentFile2()
    {
        return current_file;
    }

    public static void setNewRandomData( int N, int how_many, int smooth, float ratio )
    {
        random_num++;
        current_file = null;
        current_name = "Rand #" + Integer.toString( random_num );
        Main.randomData( N, how_many, smooth, ratio );
        //Main.canvas.volume.setNewData( Main.canvas.volume.createRandomData( N, how_many, ratio ) );
    }

    private void setNewData( File f )
    {
        try
        {
            // Main.canvas.volume.setNewData( DataReader.readFileIntoBuffer( f ) );
            current_file = f;
            Main.addRenderEvent( RenderEventQueue.Event.LOAD_VOLUME );
            edu.ohiou.lev_neiman.jung.volume_render.BottomBar.setLabelText( f.getName() +" "+ Long.toString( f.length() ) + " bytes" );
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }
}
