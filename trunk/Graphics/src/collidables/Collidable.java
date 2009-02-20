package collidables;
import scene.Transformation;
import drawables.*;
import geometry.*;
import animations.*;
import scene.*;

public abstract class Collidable extends Drawable {
	public Move movement = new Move(this, new Vector3d(), 99999);
	public Collidable()
	{
		this(new Vector3d(), new Vector3d(), new Vector3d(1,1,1));
	}
	
	public Collidable(Vector3d pos, Vector3d rot, Vector3d scale)
	{
		Scene.scene.addAnimation(movement);
		this.transformation = new Transformation(pos, rot, scale);
	}
	
	public void handleCollision(Collidable other, Vector3d where){
		System.out.println("Collision!:"+where.x+","+where.y+","+where.z);
		
		movement.moveBack(Scene.scene);
		movement.velocity.multiply(-1);
		Scene.scene.addAnimation(new Rotate(this, new Vector3d(-3,-3,-3), new Vector3d(.2,.2,.2), 500));
	}
	
	public void setVelocity(Vector3d v){
		if (movement.velocity.isZero())
			Scene.scene.removeAnimation(movement);
		else
			movement.velocity = v;
	}
	
}
