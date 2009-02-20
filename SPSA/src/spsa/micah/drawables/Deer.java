package spsa.micah.drawables;

import java.util.Vector;



import javax.media.opengl.glu.GLUquadric;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.GLContext;

import spsa.micah.collidables.*;
import spsa.micah.geometry.Polygon;
import spsa.micah.geometry.Sphere;
import spsa.micah.geometry.Vector3d;
import spsa.micah.main.Canvas;



public class Deer extends Collidable
{
	static double baseSize;
	static int detail = 10;
	
	private static int buckList, doeList, maleYearlingList, femaleYearlingList, maleFawnList, femaleFawnList;
	
	//Deer Types implemented, change to enum eventually
	public static final int BUCK=0, DOE=1, MALE_YEARLING=2, FEMALE_YEARLING=3, MALE_FAWN=4, FEMALE_FAWN=5;
	int type;
	
	public Deer(Canvas canvas, Vector3d pos, float size)
	{
		this(pos, size, BUCK);
	}
	
	public Deer(Vector3d pos, double size, int type)
	{
		super(pos, new Vector3d(), new Vector3d(size,size,size));
		this.type = type;
	}
	
	public void drawElement()
	{
		GLU glu = new GLU();
		
		gl.glPushMatrix();
			gl.glScaled(baseSize, baseSize, baseSize);
			
			// draw deer
			switch (type)
			{
			case BUCK:
				gl.glCallList(buckList);
				break;
			case DOE:
				gl.glCallList(doeList);
				break;
			case MALE_YEARLING:
				gl.glCallList(maleYearlingList);
				break;
			case FEMALE_YEARLING:
				gl.glCallList(femaleYearlingList);
				break;
			case MALE_FAWN:
				gl.glCallList(maleFawnList);
				break;
			case FEMALE_FAWN:
				gl.glCallList(femaleFawnList);
				break;
			}

		gl.glPopMatrix();
	}
	
	//must be called before a deer can be drawn
	public static void init()
	{
		
		//create display list
		
		//makes current thread able to make gl calls
		//canvas.glcontext.makeCurrent();
		
		GL gl = GLContext.getCurrent().getGL();;
		GLU glu = new GLU();
		GLUquadric quadric = glu.gluNewQuadric();
		
		int lists = gl.glGenLists(6);
		buckList = lists;
		doeList = lists + DOE;
		maleYearlingList = lists + MALE_YEARLING;
		femaleYearlingList = lists + FEMALE_YEARLING;
		maleFawnList = lists + MALE_FAWN;
		femaleFawnList = lists + FEMALE_FAWN;
		
		Vector3d color = null;
		boolean antlers = false;
		for (int type=0; type < 6; type++)
		{
			//make deer type distinctions
			switch (type)
			{
				case BUCK:
					baseSize = 1.2;
					color = new Vector3d(.7f,.3f,.1f);
					antlers = true;
					break;
				case DOE:
					baseSize = 1.1;
					color = new Vector3d(.8f,.4f,.1f);
					antlers = false;
					break;
				case MALE_YEARLING:
					baseSize = .9;
					color = new Vector3d(.9f,.6f,.3f);
					antlers = true;
					break;
				case FEMALE_YEARLING:
					baseSize = .8;
					color = new Vector3d(.9f,.6f,.3f);
					antlers = false;
					break;
				case MALE_FAWN:
					baseSize = .6;
					color = new Vector3d(.95f,.7f,.4f);
					antlers = true;
					break;
				case FEMALE_FAWN:
					baseSize = .6;
					color = new Vector3d(.95f,.75f,.45f);
					antlers = false;
					break;
			}
			
			
			gl.glNewList(lists + type,GL.GL_COMPILE);
	
				gl.glColor3d(color.x, color.y, color.z);//brown
				
				gl.glPushMatrix();
					gl.glScaled(baseSize, baseSize, baseSize);
					
					//draw body
					gl.glPushMatrix();
						gl.glTranslatef(0, 0, 2);
						gl.glScalef(1,2,1);
						glu.gluSphere(quadric, 0.5f, detail, detail);
					gl.glPopMatrix();
					
					//draw head
					gl.glPushMatrix();
						gl.glTranslatef(0,1.2f, 2.4f);
						gl.glPushMatrix();
							gl.glScalef(.6f, 1.4f, .6f);
							glu.gluSphere(quadric, 0.5f, detail, detail);
						gl.glPopMatrix();
						
						//draw ears
						gl.glBegin(GL.GL_TRIANGLES);
							gl.glNormal3f(0, 1, 0);
							gl.glVertex3f(-.3f,-.1f, 0);
							gl.glVertex3f(-.1f, -.1f, .3f);
							gl.glVertex3f(-.2f, -.15f, .6f);
							
							gl.glVertex3f(.3f,-.1f, 0);
							gl.glVertex3f(.1f, -.1f, .3f);
							gl.glVertex3f(.2f, -.15f, .6f);
						gl.glEnd();
						
						//draw antlers
						if (antlers)
						{
							gl.glColor3f(1,1,1);
							TexturedCube tc = new TexturedCube(gl);
							gl.glPushMatrix();
								gl.glTranslatef(-.2f, 0, 1f);
								gl.glScalef(.1f,.1f,2);
								tc.draw();
							gl.glPopMatrix();
							gl.glPushMatrix();
								gl.glTranslatef(.2f, 0, 1);
								gl.glScalef(.1f,.1f,2);
								tc.draw();
							gl.glPopMatrix();
							gl.glPushMatrix();
								gl.glTranslatef(0,0,1);
								gl.glScalef(2, .1f, .1f);
								tc.draw();
							gl.glPopMatrix();
						}
							
						
						//draw eyes
						gl.glColor3f(0,0,0);
						gl.glPushMatrix();
							gl.glTranslated(-.2, .25, .20);
							glu.gluSphere(quadric, .05, detail, detail);
						gl.glPopMatrix();
						gl.glPushMatrix();
							gl.glTranslated(.2, .25, .20);
							glu.gluSphere(quadric, .05, detail, detail);
						gl.glPopMatrix();
					gl.glPopMatrix();
					
					//draw legs
					gl.glColor3f(.8f,.4f,.11f);//brown
					gl.glPushMatrix();
						gl.glTranslatef(-.3f, .75f, 1f);
						gl.glScalef(.2f,.2f, 2);
						glu.gluSphere(quadric, 0.5f, detail, detail);
					gl.glPopMatrix();
					
					gl.glPushMatrix();
						gl.glTranslatef(.3f, .75f, 1f);
						gl.glScalef(.25f,.25f, 2);
						glu.gluSphere(quadric, 0.5f, detail, detail);
					gl.glPopMatrix();
					
					gl.glPushMatrix();
						gl.glTranslatef(-.3f, -.75f, 1f);
						gl.glScalef(.25f,.25f, 2);
						glu.gluSphere(quadric, 0.5f, detail, detail);
					gl.glPopMatrix();
					
					gl.glPushMatrix();
						gl.glTranslatef(.3f, -.75f, 1f);
						gl.glScalef(.25f,.25f, 2);
						glu.gluSphere(quadric, 0.5f, detail, detail);
					gl.glPopMatrix();
					
					//draw tail
					gl.glColor3f(1,1,1);
					gl.glPushMatrix();
						gl.glTranslatef(0,-1,2);
						glu.gluSphere(quadric, .15f, detail, detail);
					gl.glPopMatrix();
				gl.glPopMatrix();
			gl.glEndList();
		}
		
	}
	
	public Sphere getBoundingSphere(){
		return new Sphere(transformation.pos, transformation.scale.x*2);
	}
	public Polygons getPolygons(){
		return new Polygons(new Vector<Polygon>());
	}
}
