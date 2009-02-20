package spsa.micah.animations;


import spsa.micah.drawables.Drawable;
import spsa.micah.geometry.Vector3d;
import spsa.micah.scene.Scene;

public class Move implements Animation
{
	Drawable d;
	public double duration;
	public Vector3d velocity, acceleration;
	
	public Move(Drawable d, Vector3d velocity, double duration)
	{
		this(d, velocity, new Vector3d(), duration);
	}
	
	public Move(Drawable d, Vector3d velocity, Vector3d acceleration, double duration)
	{
		this.d = d;
		this.velocity = velocity;
		this.acceleration = acceleration;
		this.duration = duration;
	}
	
	public void init(Scene s){}
	
	public boolean animate(Scene s)
	{
		duration -= s.elapsedTime;
		if (duration <= 0)
			return false;
		
		velocity.add(acceleration.multiplyN(s.elapsedTime));
		d.transformation.pos.add(velocity.multiplyN(s.elapsedTime));
		return true;
	}
	
	//move back to position in previous draw
	public void moveBack(Scene s){
		d.transformation.pos.add(velocity.multiplyN(s.elapsedTime).multiplyN(-1));
	}
}