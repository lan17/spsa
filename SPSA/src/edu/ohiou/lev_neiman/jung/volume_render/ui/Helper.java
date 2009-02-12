package edu.ohiou.lev_neiman.jung.volume_render.ui;

import java.awt.Component;
import java.awt.Dimension;

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
public class Helper
{
    public static void validateAllParents( Component e )
    {
        for( Component i = e; i != null; i = i.getParent() )
        {
            i.validate();
        }
    }

    public static Dimension addPreferredHeight( Component resizee, Component content )
    {
        Dimension ret = new Dimension( resizee.getPreferredSize().width, resizee.getPreferredSize().height + content.getPreferredSize().height );
        resizee.setPreferredSize( ret );
        resizee.validate();
        return ret;
    }

    public static Dimension subtractPreferredHeight( Component resizee, Component content )
    {
        Dimension ret = new Dimension( resizee.getPreferredSize().width, resizee.getPreferredSize().height - content.getPreferredSize().height );
        resizee.setPreferredSize( ret );
        resizee.validate();

        return ret;
    }
}
