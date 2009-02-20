package spsa.micah.drawables;
import spsa.micah.scene.*;

public class Waves implements Function3d {
	Scene scene;
	
	public Waves(Scene scene)
	{
		this.scene = scene;
	}
	
	public double z(double x, double y)
	{
		double z = (scene.currentTime % 10000) / 100;
		//System.out.println("z: " + z + " | x: " + x + " | " + (100 - (x - z)));
		return 100 - (x - z);
	}
}
