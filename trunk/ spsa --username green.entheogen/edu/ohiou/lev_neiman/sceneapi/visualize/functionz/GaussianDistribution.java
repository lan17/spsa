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
public class GaussianDistribution
        extends TransformFunction
{
    public float factor, mean, deviation, threshold;
    public GaussianDistribution( float factor, float mean, float deviation, float threshold )
    {
        this.threshold = threshold;
        this.factor = factor;
        this.mean = mean;
        this.deviation = deviation;
    }


    /**
     *
     * @param x float
     * @return float
     * @todo Implement this
     *   edu.ohiou.lev_neiman.sceneapi.visualize.TransformFunction method
     */
    public float transform( float x )
    {
        Float f = factor * ( float ) Math.exp( - ( Math.pow( x - mean, 2 ) ) / ( 2 * deviation * deviation ) );
        //f = (float)Func.roundOff((double)f, 5 );

        return f;
    }
}
