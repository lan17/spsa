package spsa.micah.scene;

import spsa.micah.geometry.*;

public class Transformation {
	public Vector3d pos, rot, scale;
	
	public Transformation(){
		this(new Vector3d(), new Vector3d(), new Vector3d());
	}
	
	public Transformation(Vector3d pos, Vector3d rot, Vector3d scale){
		this.pos = pos;
		this.rot = rot;
		this.scale = scale;
	}
	
	public Transformation add(Transformation t){
		pos = pos.add(t.pos);
		rot = rot.add(t.rot);
		scale = scale.add(t.scale);
		return this;
	}
	
	public Transformation addN(Transformation t){
		return new Transformation(pos.addN(t.pos), rot.addN(t.rot), scale.addN(t.scale));
	}
}
