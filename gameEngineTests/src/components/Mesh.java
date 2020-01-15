package components;

import java.util.ArrayList;
import java.util.Arrays;

import threeDimensions.IndexedTriangleList;
import threeDimensions.Vec2;
import threeDimensions.Vec3;
import threeDimensions.Vertex;

public class Mesh {

	public static IndexedTriangleList<Vertex> getTexturedTriangle(int rows, int cols, float[] map) {
		int size = rows*cols;
		Vec3[] positions = new Vec3[size];
		Vec2[] t = new Vec2[size];
		for (int j = 0; j < rows; j++) {
			for (int i = 0; i < cols; i++) {
				int index = i + j*cols;
				positions[index] =  new Vec3((float) i/cols, map[index], (float) j / cols); //Weird Scale
				t[index] = new Vec2(0, 0);
			}
		}
		int[] indices = new int[(rows - 1)*(cols - 1)*6];
		int a = 0;
		for (int j = 0; j < rows - 1; j++) {
			for (int i = 0; i < cols - 1; i++) {
				int index = i + j*cols;
				//First Triangle
				indices[a++] = index;//i
				indices[a++] = index + cols; //i + cols + j*cols
				indices[a++] = index + 1; //i + 1 + j*cols

				//Second Triangle
				indices[a++] = index + 1;//i + 1 + j*cols
				indices[a++] = index + cols;//i + cols + j*cols
				indices[a++] = index + 1 + cols;//i + 1 + cols + j*cols
			}
		}
		
		ArrayList<Vertex> vertices = new ArrayList<Vertex>(size);
		for (int i = 0; i < size; i++) {
			vertices.add(new Vertex(positions[i], t[i]));
		}
		
		
		return new IndexedTriangleList<Vertex>(vertices, indices);
	}

}
