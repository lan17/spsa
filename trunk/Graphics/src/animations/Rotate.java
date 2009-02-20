package animations;


import geometry.Vector3d;
import drawables.Drawable;
import scene.Scene;

public class Rotate implements Animation
{
	Drawable d;
	double duration;
	Vector3d rotationSpeed, rotationAcceleration;
	
	public Rotate(Drawable d, Vector3d rotationSpeed, double duration)
	{
		this(d, rotationSpeed, new Vector3d(), duration);
	}
	
	public Rotate(Drawable d, Vector3d rotationSpeed, Vector3d rotationAcceleration, double duration)
	{
		this.d = d;
		this.rotationSpeed = rotationSpeed;
		this.rotationAcceleration = rotationAcceleration;
		this.duration = duration;
		System.out.println("Rotate constructor complete");
	}
	
	public void init(Scene s){}
	
	public boolean animate(Scene s)
	{
		duration -= s.elapsedTime;
		if (duration <= 0)
			return false;
		
		rotationSpeed.add(rotationAcceleration.multiplyN(s.elapsedTime));
		d.transformation.rot.add(rotationSpeed.multiplyN(s.elapsedTime));
		return true;
	}
}