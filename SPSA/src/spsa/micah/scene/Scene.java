package spsa.micah.scene;
import java.util.Vector;
import javax.media.opengl.GL;
import javax.media.opengl.GLContext;
import javax.media.opengl.glu.GLU;

import spsa.micah.animations.*;
import spsa.micah.collidables.*;
import spsa.micah.drawables.*;
import spsa.micah.geometry.*;


public class Scene extends Drawable
{	
	public static Scene scene;//store the scene, I'm sick of passing it around
	Vector<Animation> animations = new Vector<Animation>();
	Vector<Drawable> dItems = new Vector<Drawable>();
	Vector<Collidable> cItems = new Vector<Collidable>();
	
	public Eye eye = new Eye();
	
	public double elapsedTime;//in seconds
	private double lastDraw = System.currentTimeMillis();//in milliseconds
	public double currentTime;
	
	public Scene(){
		scene = this;
	}
	
	protected void drawElement()
	{
		lastDraw = currentTime;
		currentTime = System.currentTimeMillis() / 1000d;
		elapsedTime = (currentTime - lastDraw);
		
		eye.look();
		
		for (int i=0; i < animations.size(); i++)
		{
			if (!animations.get(i).animate(this))
				animations.remove(i);
		}
		
		handleCollisions();
		
		for (int i=0; i < dItems.size(); i++)
		{
			dItems.get(i).draw();
		}
		for (int i=0; i < cItems.size(); i++)
		{
			cItems.get(i).draw();
		}
		
	}
	
	
	public void init(){
		gl = GLContext.getCurrent().getGL();
		glu = new GLU();
		
		gl.glMatrixMode(GL.GL_PROJECTION);
	    glu.gluPerspective(45,1,.1,10000); 
	    
	    gl.glMatrixMode(GL.GL_MODELVIEW);
	    
	    gl.glClearColor(0, 0, 0, 0);
        gl.glShadeModel(GL.GL_SMOOTH);
        gl.glClearDepth(1.0f);
	    gl.glEnable(GL.GL_DEPTH_TEST);
	    gl.glEnable(GL.GL_CULL_FACE);
	    gl.glEnable(GL.GL_LINE_SMOOTH);
	    gl.glDepthFunc(GL.GL_LEQUAL);
	    gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
	    
	    gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
	    
		//lighting

		float ambient[]  = { 0.0f, 0.0f, 0.0f, 0f };
	    float diffuse[]  = { 1.0f, 1.0f, 1.0f, 1.0f };
	    float specular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
	    float position[] = { 0, 0, 1000, 1.0f };
		
        gl.glEnable(GL.GL_LIGHTING);
        gl.glEnable(GL.GL_LIGHT0);
        gl.glEnable(GL.GL_NORMALIZE);
        gl.glEnable(GL.GL_COLOR_MATERIAL);

        gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT,  ambient, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE,  diffuse, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, specular, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, position, 0);		
	}
	
	
	public void handleCollisions(){
		Collidable c1, c2;
		Polygons p1, p2;
		Sphere s1, s2;
		
		for (int i=0; i < cItems.size(); i++){
			for (int j=i+1; j < cItems.size(); j++){
				c1 = cItems.get(i);
				c2 = cItems.get(j);
				s1 = c1.getBoundingSphere();
				s2 = c2.getBoundingSphere();
				
				gl.glColor3d(.9f,.3f,.3f);
				s1.draw();
				s2.draw();//draw for testing
				
				if (!s1.intersects(s2))
					continue;
				
				//for now just use bounding spheres for collision detection
				
				Vector3d v = s1.transformation.pos.addN(s2.transformation.pos.multiplyN(-1));
				
				c1.handleCollision(c2, s1.transformation.pos.addN(v.multiplyN(s1.radius / s1.transformation.pos.dist(s2.transformation.pos)).multiply(-1)));
				c2.handleCollision(c1, s2.transformation.pos.addN(v.multiplyN(s2.radius / s1.transformation.pos.dist(s2.transformation.pos))));
				
				/*Uncomment the following once I write Polygon.intersects
				p1 = c1.getPolygons();
				p2 = c2.getPolygons();
				
				for (int k=0; k < p1.polygons.size(); k++){
					for (int l=0; l < p2.polygons.size(); l++){
						Vector<Vector3d> points = p1.polygons.get(k).intersects(p2.polygons.get(l));
						if (points != null)
						{
							c1.handleCollision(c2, points.get(0));
							c2.handleCollision(c1, points.get(0));
						}
					}
				}
				*/				
			}
		}
	}
	
	
	public void Collision(Collidable c1, Collidable c2){
		if (!c1.getBoundingSphere().intersects(c2.getBoundingSphere()))
			return;
		
		Polygons vp1 = c1.getPolygons(), vp2 = c2.getPolygons();
		
		//eventually I will need to figure out to take motion between draws into account
		
		for (int i=0; i < vp1.polygons.size(); i++){
			for (int j=0; j < vp2.polygons.size(); j++){
				Vector<Vector3d> points = vp1.polygons.get(i).intersects(vp2.polygons.get(j));
				if (points != null)
				{
					c1.handleCollision(c2, points.get(0));
					c2.handleCollision(c1, points.get(0));
				}
			}
		}
	}
	
	public void addItem(Drawable d){
		dItems.add(d);
	}
	public void addItem(Collidable c){
		cItems.add(c);
	}
	public void removeItem(Drawable d){
		dItems.add(d);
	}
	public void removeItem(Collidable c){
		cItems.add(c);
	}
	
	public void addAnimation(Animation a)
	{
		a.init(this);
		animations.add(a);
	}
	
	public boolean removeAnimation(Animation a)
	{
		return animations.remove(a);
	}
	
	//unused
	public Polygons getPolygons(){
		return null;
	}
	//unused
	public Sphere getBoundingSphere(){
		return null;
	}
	
}