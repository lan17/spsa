package geometry;


public class Geometry {
    public static Vector3d getNormal( Vector3d a, Vector3d  b, Vector3d  c )
    {
    	Vector3d tmp = new Vector3d( -c.x, -c.y, -c.z );
        a.add( tmp );
        b.add( tmp );
        double
        	i = ( a.y * b.z - a.z * b.y ),
        	j = ( a.x * b.z - a.z * b.x ),
        	k = ( a.x * b.y - a.y * b.x );
        a.add( c );
        b.add( c );
        return new Vector3d(i, -j, k );
    }  
}
