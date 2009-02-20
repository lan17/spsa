package spsa.micah.geometry;

import java.lang.Math;

public class Vector3d
{
	public double x=0,y=0,z=0;
	
	public Vector3d()
	{
		x=0;
		y=0;
		z=0;
	}
	
	public Vector3d(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void clear()
	{
		x=0;y=0;z=0;
	}
	
	public boolean isZero(){
		return x==0&&y==0&&z==0;
	}
	
	public Vector3d add(Vector3d v)
	{
		x += v.x;
		y += v.y;
		z += v.z;
		
		return this;
	}
	
	public Vector3d addN(Vector3d v)
	{
		return new Vector3d(x + v.x, y + v.y, z + v.z);
	}
	
	public Vector3d multiply(double d){
		x = x*d;
		y = y*d;
		z = z*d;
		
		return this;
	}
	
	public Vector3d multiplyN(double d)
	{
		return new Vector3d(x * d, y * d, z * d);
	}
	
	
	public double dist(Vector3d v){	
		return Math.sqrt(Math.pow(v.x-x, 2)+Math.pow(v.y-y, 2)+Math.pow(v.z-z, 2));
	}
	
	public void normalize(){
		multiply(1 / dist(new Vector3d()));
	}
	
	public Vector3d normalizeN(){
		return multiplyN(1 / dist(new Vector3d()));
	}

	
}
