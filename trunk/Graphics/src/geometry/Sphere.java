package geometry;

import java.lang.Math;
import general.*;
import java.util.Vector;
import scene.*;
import drawables.*;

public class Sphere extends Drawable {
	public double radius;
	
	public Sphere(Vector3d pt, double radius){
		super(pt, new Vector3d(), new Vector3d());
		this.radius = radius;
	}
	
	
	public Sphere(Vector<Sphere> spheres)
	{
		_merge(spheres);
	}
	
	
	public boolean intersects(Sphere s){
		return transformation.pos.dist(s.transformation.pos) < radius+s.radius;
	}
	
	//-1 -> back, 0 -> intersects, 1 -> front
	public int intersects(Plane p){

		return 0;
	}
	
	//t.scale must scale all dimensions equally for the result to accurate
	public Sphere transform(Transformation t){
		transformation.pos = transformation.pos.addN(t.pos);
		radius = radius * t.scale.x;
		return this;
	}
	
	//t.scale must scale all dimensions equally for the result to accurate
	public Sphere transformN(Transformation t){
		return new Sphere(transformation.pos.addN(t.pos), radius * t.scale.x);
	}
	
	public void merge(Sphere s)
	{
		Vector<Sphere> spheres = new Vector<Sphere>();
		spheres.add(this);
		spheres.add(s);
		_merge(spheres);
	}
	
	public void merge(Vector<Sphere> spheres)
	{
		spheres.add(this);
		_merge(spheres);

	}
	
	private void _merge(Vector<Sphere> spheres){
		Sphere s = spheres.get(0);		
		double 
			minX = s.transformation.pos.x-radius,
			maxX = s.transformation.pos.x+radius,
			minY = s.transformation.pos.y-radius,
			maxY = s.transformation.pos.y+radius,
			minZ = s.transformation.pos.z-radius,
			maxZ = s.transformation.pos.z+radius;
		
		
		for (int i=1; i<spheres.size(); i++)
		{
			s = spheres.get(i);
			minX = Math.min(minX, s.transformation.pos.x-s.radius);
			maxX = Math.max(maxX, s.transformation.pos.x+s.radius);
			minY = Math.min(minY, s.transformation.pos.y-s.radius);
			maxY = Math.max(maxY, s.transformation.pos.x+s.radius);
			minZ = Math.min(minZ, s.transformation.pos.z-s.radius);
			maxZ = Math.max(maxZ, s.transformation.pos.x+s.radius);
		}
		radius = Tools.max(maxX-minX, maxY-minY, maxZ-minZ)/2;
		radius = Tools.max(maxX-minX, maxY-minY, maxZ-minZ)/2;
		transformation.pos.x = Tools.avg(minX, maxX);
		transformation.pos.y = Tools.avg(minY, maxY);
		transformation.pos.z = Tools.avg(minZ, maxZ);		

	}
	
	public void drawElement(){
		glu.gluSphere(quadric, radius, 10, 10);
	}
	
	public Sphere getBoundingSphere(){
		return this;
	}
	
	public Polygons getPolygons(){
		return null;
	}
}
