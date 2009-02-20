package spsa.micah.general;
import java.lang.Math;

public class Tools {
	public static double min(double d1, double d2, double d3)
	{
		return Math.min(Math.min(d1, d2), Math.min(d1, 3));
	}
	
	public static double max(double d1, double d2, double d3)
	{
		return Math.max(Math.max(d1, d2), Math.max(d1, 3));
	}
	
    public static double avg(double d1, double d2)
    {
    	return (d1 + d2) / 2;
    }	
}
