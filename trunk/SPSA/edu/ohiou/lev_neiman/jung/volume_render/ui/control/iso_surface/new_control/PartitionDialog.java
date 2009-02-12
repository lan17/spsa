package edu.ohiou.lev_neiman.jung.volume_render.ui.control.iso_surface.new_control;

import javax.swing.JDialog;

import edu.ohiou.lev_neiman.jung.volume_render.Main;

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
public class PartitionDialog
        extends JDialog
{
    public static final int _OK = 1;
    public static final int _CANCEL = 2;

    public PartitionDialog()
    {
        super( Main.frame, "Generate new Iso-Surfaces", true );
    }
}
