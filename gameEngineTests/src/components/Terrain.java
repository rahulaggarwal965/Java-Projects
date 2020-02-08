package components;

import math.Gradient;
import threeDimensions.Mesh;
import threeDimensions.Vec3;

public class Terrain {

	public static Mesh getTriangles(float scaleX, float scaleY, float scaleZ, float[] map, int rows, int cols, Gradient g) {
		int size = rows*cols;
		
		int[] lengths = {3, 3};
		float[] positions = new float[size * 3];
		float[] colors = new float[size * 3];
		for (int j = 0; j < rows; j++) {
			for (int i = 0; i < cols; i++) {
				int mapIndex = i + j*cols;
				int index = (i + j*cols)*3;
				positions[index] = ((float) i / (cols - 1) * 2 - 1)*scaleX;
				positions[index + 1] = map[mapIndex] * scaleY;
				positions[index + 2] = ((float) j / (rows - 1) * 2 - 1)*scaleZ;
				Vec3 color = g.getColor(map[mapIndex]);
				colors[index] = color.x;
				colors[index+1] = color.y;
				colors[index+2] = color.z;
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
		
		return new Mesh(indices, lengths, positions, colors);
	}

}
