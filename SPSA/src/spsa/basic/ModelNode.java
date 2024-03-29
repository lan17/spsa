package spsa.basic;

import java.util.Set;
import java.util.Vector;

import javax.media.opengl.*;

/**
 * <p>
 * Wrapper class for ModelFile that can be put anywhere in the scene tree.
 * </p>
 * 
 * <p>
 * Description: wrapper class for ModelFile that can be put anywhere in the
 * scene tree.
 * </p>
 * 
 * <p>
 * Copyright: Lev A Neiman 2008
 * </p>
 * 
 * <p>
 * Company: Ohio University EECS
 * </p>
 * 
 * @author Lev A Neiman
 * @version 1.0
 */
public class ModelNode extends ANode
{
	/**
	 * handle on the ModelFile object which contains model data.
	 */
	public ModelFile data = null;

	/**
     *
     */
	public Vector< MaterialGroupNode > mtl_nodes = new Vector< MaterialGroupNode >();

	/**
     *
     */
	private void populateMtls()
	{
		Set< String > names = data.mtl_groups.keySet();
		for (String name : names)
		{
			MaterialGroupNode tmp = new MaterialGroupNode( data.mtl_groups.get( name ) );
			tmp.name = name;
			mtl_nodes.add( tmp );
			// Coordinate c = tmp.centrify();
			// tmp.translation.set( c.multiply( c, 2 ) );
			addChild( tmp );
		}
	}

	/**
	 * default constructor.
	 * 
	 * @param data
	 *            ModelFile
	 */
	public ModelNode(ModelFile data)
	{
		this.data = data;
		try
		{
			populateMtls();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Overridden method from ANode. calls super's version and then renders
	 * model.
	 * 
	 * @param gld
	 *            GLAutoDrawable
	 */
	public void render(GL gl)
	{
		super.render( gl );
	}
}
