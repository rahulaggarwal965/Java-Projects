package threeDimensions;

import java.util.ArrayList;

public class IndexedLineList<V> {

	public ArrayList<V> vertices;
	public ArrayList<Integer> indices; 
	
	public IndexedLineList(ArrayList<V> vertices, ArrayList<Integer> indices) {
		this.vertices = vertices;
		this.indices = indices;
	}
}
