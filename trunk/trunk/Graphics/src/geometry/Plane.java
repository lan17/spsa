package geometry;

import geometry.*;

public class Plane {
	Vector3d pt, normal;
	
	
	public Plane(Vector3d pt, Vector3d normal)
	{
		this.pt = pt;
		this.normal = normal;
	}
	
	public boolean isFront(Vector3d pt)
	{
		Vector3d v = new Vector3d(pt.x-this.pt.x, pt.y - this.pt.y, pt.z - this.pt.z);
		return normal.x * v.x + normal.y * v.y + normal.z * v.z > 0;
	}
	
	public Vector3d findIntersection(Vector3d pt1, Vector3d pt2)
	{
		return null;
	}
}
