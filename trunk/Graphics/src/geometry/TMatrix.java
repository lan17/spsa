package geometry;

//transformation matrix
public class TMatrix {
	float [] matrix;
	
	public TMatrix(){
		matrix = new float [16];
	}
	
	public TMatrix(float [] matrix){
		this.matrix = matrix;
	}
	
	public void mult(TMatrix tm){
		
		
	}
	
	public Vector3d mult(Vector3d pt){
		
		
		return null;
	}
	
	public void rot(double x, double y, double z){
		
		
	}
	
	public void trans(double x, double y, double z){
		
		
	}
	
	public void trans(Vector3d direction, double distance){
		
		
		
	}
}
