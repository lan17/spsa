package spsa.micah.scene;
import java.util.Vector;

import spsa.micah.general.*;
import spsa.micah.geometry.*;

public class Cell
{
	Cell parent;
	Cell back, front;
	Vector<Polygon> polygons = new Vector<Polygon>();
	
	Vector3d nCorner, pCorner;
	Plane divider;
	int level;
	final static int maxPoly = 5;
	
	Cell(Vector3d nCorner, Vector3d pCorner, int level)
	{
		this.nCorner = nCorner;
		this.pCorner = pCorner;
		this.level = level;
	}
	
	void split()
	{
		Vector3d common;
		Vector<Polygon> holder;
		
		//Break determine child cells
		switch (level % 3)
		{
			case 0:
				common = new Vector3d(Tools.avg(nCorner.x, pCorner.x), pCorner.y , pCorner.z);
				back = new Cell(nCorner, common, level++);
				front = new Cell(common, pCorner, level++);
				divider = new Plane(common, new Vector3d(1,0,0));
				break;
			
			case 1:
				common = new Vector3d(pCorner.x, Tools.avg(nCorner.y, pCorner.y) , pCorner.z);
				back = new Cell(nCorner, common, level++);
				front = new Cell(common, pCorner, level++);
				divider = new Plane(common, new Vector3d(0,1,0));
				break;
				
			default:
				common = new Vector3d(pCorner.x, pCorner.y, Tools.avg(nCorner.z, pCorner.z));
				back = new Cell(nCorner, common, level++);
				front = new Cell(common, pCorner, level++);
				divider = new Plane(common, new Vector3d(0,0,1));
				break;
		}
		
		holder = polygons;
		polygons = null;
		for (int i=0; i < holder.size(); i++)
		{
			addPolygon(holder.get(i));
		}
		
	}
	
	void addPolygon(Polygon p)
	{
		//if this isn't a leaf pass the polygon down
		if (polygons == null)
		{
			Polygon[] pieces = p.split(divider);
			if (pieces[0] != null)
				back.addPolygon(pieces[0]);
			if (pieces[1] != null)
				front.addPolygon(pieces[1]);
		}
		else
		{
			polygons.add(p);
			if (polygons.size() > maxPoly)
				split();
		}
	}
}