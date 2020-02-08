package threeDimensions;

public class Mesh {

	public float[][] vertices;
	public int[] indices;
	
	public Mesh(int[] indices, int[] lengths, float[] ... vertices) {
		
		this.indices = indices;
		
		//Check Relative Attribute Count
		int numVertices = vertices[0].length/lengths[0];
		for (int i = 1; i < vertices.length; i++) {
			if(numVertices != vertices[i].length/lengths[i]) throw new IllegalArgumentException("Vertex Counts do not match");
		}
		
		int vertexSize = arraySum(lengths);
		this.vertices = new float[numVertices][vertexSize];
		
		for (int i = 0; i < numVertices; i++) {
			for (int j = 0, k = 0; j < vertices.length; k += lengths[j], j++) {
				copyRange(vertices[j], i * lengths[j], this.vertices[i], k, lengths[j]);
			}
		}
	}
	
	private void copyRange(float[] from, int index0, float[] to, int index1, int length) {
		for (int i = 0; i < length; i++) {
			to[i + index1] = from[i + index0];
		}
	}
	
	private int arraySum(int[] array) {
		int sum = 0; 
		for (int i : array) {
			sum += i;
		}
		return sum;
	}

}
