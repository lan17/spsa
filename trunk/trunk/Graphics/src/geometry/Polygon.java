package geometry;
import javax.media.opengl.GL;

import java.util.Vector;
import geometry.*;

public class Polygon {
	public Vector<Vector3d> vertices;
	

	
	//returns null for no intersection, otherwise a vector of intersection points
	public Vector<Vector3d> intersects(Polygon p){
		
		
		return null;
	}
	
	
	//[0] -> back, [1] -> front
	public Polygon[] split(Plane divider)
	{
		Polygon ret[] = new Polygon[2];
		
		int lastIntersect = 0, timesCrossedDivider = 0;
		Polygon frontPoly = new Polygon(), backPoly = new Polygon(), currentPoly;//only used if the polygon is split
		Vector3d intersection;
		
		boolean side = divider.isFront(vertices.get(0));
		for (int i=1; i < vertices.size(); i++)
		{
			//if a pt is on a different side of divider
			if (divider.isFront(vertices.get(i)) != side)
			{
				timesCrossedDivider++;
				
				if (side)
					currentPoly = frontPoly;
				else
					currentPoly = backPoly;
				
				for (int j = lastIntersect; j < i; j++)
				{
					currentPoly.vertices.add(vertices.get(j));
				}
				
				lastIntersect = i;
				
				//calculate plane intersection point
				intersection = divider.findIntersection(vertices.get(i), vertices.get(i-1));
				//add intersection to both polys
				frontPoly.vertices.add(intersection);
				backPoly.vertices.add(intersection);			
				
				
				side = !side;
			}
			
		}
		
		//the plane was not intersected, just return this to correct side
		if (lastIntersect == 0)
		{
			if (side)
			{
				ret[1] = this;
			}
			else
			{
				ret[0] = this;
			}
		}
		else
		{
			//add remaining vertices to the appropriate poly
			if (side)
				currentPoly = frontPoly;
			else
				currentPoly = backPoly;
			
			for (int i = lastIntersect; i < vertices.size(); i++)
			{
				currentPoly.vertices.add(vertices.get(i));
			}
			
			
			if (timesCrossedDivider != 2)
			{//the points never returned to the original side
				
				//the last point will be the intersection of the first and last points
				intersection = divider.findIntersection(vertices.get(vertices.size()-1), vertices.get(0));
				frontPoly.vertices.add(intersection);
				backPoly.vertices.add(intersection);
			}
			
			ret[0] = backPoly;
			ret[1] = frontPoly;
		}		

		return ret;
	}
}