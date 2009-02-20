package main;
//import javax.swing.JApplet;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;



import com.sun.opengl.util.texture.*;
import com.sun.opengl.util.*;
import java.io.*;
import java.util.*;


public class MyFrame extends JFrame {
	
	static Canvas canvas = new Canvas();
	
	public static void main(String[] args)
	{
		System.out.println(System.getProperty("user.dir"));
		
		MyFrame mf = new MyFrame();
		mf.setVisible(true);
	}
	
	public MyFrame()
	{
		setTitle("Vagsicles");
		setSize(800,600); // default size is 0,0
		setLocation(0,0);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLayout(new BorderLayout());
		
		Container contentPane = getContentPane();
		contentPane.add(canvas);
	}
}