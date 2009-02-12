package edu.ohiou.lev_neiman.sceneapi.visualize.functionz;


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
public class Interval
        extends TransformFunction
{
    float a, b;
    public Interval( float a, float b )
    {
        this.a = a;
        this.b = b;
    }

    /**
     *
     * @param x float
     * @return float
     * @todo Implement this
     *   edu.ohiou.lev_neiman.sceneapi.visualize.functionz.TransformFunction
     *   method
     */
    public float transform( float x )
    {
        if( a <= x && x <= b )
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }
}
