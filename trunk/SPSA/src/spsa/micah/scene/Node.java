package spsa.micah.scene;
import java.util.Vector;

import spsa.micah.collidables.*;
import spsa.micah.drawables.*;
import spsa.micah.geometry.*;


public class Node extends Collidable {
	Vector<Collidable> children = new Vector<Collidable>();
	
	public void drawElement(){
		for (int i=0; i < children.size(); i++)
			children.get(i).draw();
		
	}
	
	public void addChild(Collidable c)
	{
		children.add(c);
	}
	
	public boolean removeChild(Collidable c)
	{
		return children.remove(c);
	}
	
	public Sphere getBoundingSphere(){
		Vector<Sphere> spheres = new Vector<Sphere>();
		for (int i=0; i<children.size(); i++)
			spheres.add(children.get(i).getBoundingSphere().transform(transformation));
		
		return new Sphere(spheres);
			
	}
	
	public Polygons getPolygons(){
		
		return null;
	}
	
	
}
