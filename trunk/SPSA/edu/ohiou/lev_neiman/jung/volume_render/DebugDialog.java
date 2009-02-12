package edu.ohiou.lev_neiman.jung.volume_render;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.swing.JScrollPane;


// class StringStream was borrowed from: http://www.javalobby.org/java/forums/t75938.html
/**
 *
 * <p>StringStream</p>
 *
 * <p>Description: borrowed from: http://www.javalobby.org/java/forums/t75938.html</p>
 *
 * <p>Copyright: Copyright (c) 2008, Lev A. Neiman</p>
 *
 * <p>Company: Dr. Peter Jung</p>
 *
 * @author Rohit Khariwal
 * @version 1.0
 */
class StringStream
        extends PrintStream
{

    ByteArrayOutputStream out;
    TextArea text;

    PrintStream standard_out, standard_err;

    public StringStream( ByteArrayOutputStream out, TextArea text, PrintStream standard_out, PrintStream standard_err )
    {
        super( System.out );
        this.out = out;
        this.text = text;
        this.standard_err = standard_err;
        this.standard_out = standard_out;
    }

    public void write( byte buf[], int off, int len )
    {
        out.write( buf, off, len );
        standard_out.write( buf, off, len );
        String message = new String( buf, off, len );
        text.append( message );
    }

    public void flush()
    {
        super.flush();
    }
}


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
public class DebugDialog
        extends Frame
{
    private JScrollPane scroll = new JScrollPane();
    private TextArea text = new TextArea( "" );

    public static void createNewDialog()
    {
        new DebugDialog();
    }

    private void redirectSystemStreams()
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream stringStream = new StringStream( out, text, System.out, System.err );
        System.setOut( stringStream );
        System.setErr( stringStream );
    }


    private DebugDialog()
    {
        redirectSystemStreams();
        super.setSize( new Dimension( 800, 240 ) );
        super.setTitle( "Debug Dialog.  All System print streams are directed to here." );
        super.setLayout( new BorderLayout() );
        // scroll.getViewport().add( text );

        //scroll.setPreferredSize( new Dimension( 620, 460 ) );
        text.setPreferredSize( new Dimension( 620, 460 ) );

        //scroll.updateUI();
        super.add( text, BorderLayout.CENTER );

        super.setVisible( true );

        final Frame lol = this;
        this.addWindowListener( new WindowAdapter()
        {
            public void windowClosing( WindowEvent e )
            {
                lol.setVisible( false );
                System.exit( 0 );
            }
        } );
    }


}
