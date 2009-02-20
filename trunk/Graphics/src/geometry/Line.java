package geometry;

public class Line {
	Vector3d pt1, pt2;
	
	public Line(){
		this(new Vector3d(), new Vector3d());
		
	}
	
	public Line(Vector3d pt1, Vector3d pt2){
		this.pt1 = pt2;
		this.pt2 = pt2;
	}
}
