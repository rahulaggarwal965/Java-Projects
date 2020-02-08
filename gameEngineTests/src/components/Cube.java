package components;

import threeDimensions.Mesh;

public class Cube {
	
	public static Mesh getTexturedTriangles(float size, float dim) {
		final float side = size / 2.0f;
		float[] positions = {
				-side,-side,-side,
				side,-side,-side,
				-side,side,-side,
				side,side,-side,
				-side,-side,side,
				side,-side,side,
				-side,side,side,
				side,side,side
				};
		
		float[] textures = {
				0.0f, dim,
				dim, dim,
				0.0f, 0.0f,
				dim, 0.0f,
				dim, dim,
				0.0f, dim,
				dim, 0.0f,
				0.0f, 0.0f
				};
		
		int[] indices = {
				0,2,1, 2,3,1,
				1,3,5, 3,7,5,
				2,6,3, 3,6,7,
				4,5,7, 4,7,6,
				0,4,2, 2,4,6,
				0,1,4, 1,5,4	
				};
		
		return new Mesh(indices, new int[] {3, 2}, positions, textures);
	}
}
