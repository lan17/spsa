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
public abstract class TransformFunction<T>
{
    /**
     *
     * @param x float
     * @return float
     */
    public abstract float transform( float x );
}
