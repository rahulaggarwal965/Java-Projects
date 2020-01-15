package threeDimensions;

import java.util.ArrayList;

public class IndexedTriangleList<T> {
	public ArrayList<T> vertices;
	public int[] indices; 
	
	public IndexedTriangleList(ArrayList<T> vertices, int[] indices) {
		this.vertices = vertices;
		/*this.vertices = new ArrayList<T>(vertices.size());
		for (T t: vertices) {
			this.vertices.add(new T(t));
		}*/
		this.indices = indices;
	}
}
