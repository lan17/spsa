package spsa.micah.drawables;


import java.util.Vector;

import spsa.micah.geometry.*;

//only supporting GL_POLYGON for now
public class Polygons extends Drawable{
	
	public Vector<Polygon> polygons;
	
	public Polygons(Vector<Polygon> polygons){
		this.polygons = polygons;
	}
	
	protected void drawElement(){
		
	}
	
	public Sphere getBoundingSphere(){
		return null;
	}
	
	public Polygons getPolygons(){
		return this;
	}
	
}
