package edu.ohiou.lev_neiman.sceneapi.visualize;

import edu.ohiou.lev_neiman.sceneapi.basic.ANode;

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
public class SolarSystem
        extends ANode
{
    private Sphere sun = new Sphere();
    private Sphere earth = new Sphere();
    private Sphere moon = new Sphere();

    public SolarSystem()
    {
        sun.setRadius( 5 );
        earth.setRadius( 2 );
        moon.setRadius( .5f );

        earth.translation().setY( 3 );

        earth.addChild( moon );
        sun.addChild( earth );
        addChild( sun );
    }

}
