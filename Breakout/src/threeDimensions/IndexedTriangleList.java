package threeDimensions;

import java.util.ArrayList;

public class IndexedTriangleList<T> {
	public ArrayList<T> vertices;
	//Might change to ArrayList in the future when we automate edge splitting
	public ArrayList<Integer> indices; 
	
	public IndexedTriangleList(ArrayList<T> vertices, ArrayList<Integer> indices) {
		this.vertices = vertices;
		/*this.vertices = new ArrayList<T>(vertices.size());
		for (T t: vertices) {
			this.vertices.add(new T(t));
		}*/
		this.indices = indices;
	}
}
