package components;

import java.util.ArrayList;
import java.util.Arrays;

import threeDimensions.IndexedTriangleList;
import threeDimensions.Vec2;
import threeDimensions.Vec3;
import threeDimensions.Vertex;

public class Cube {
	
	public static IndexedTriangleList<Vertex> getTexturedTriangles(float size, float dim) {
		final float side = size / 2.0f;
		
		final int num = 8;
		
		ArrayList<Vec3> positions = new ArrayList<Vec3>(num);
		ArrayList<Vec2> t = new ArrayList<Vec2>(num);
		
		positions.add(new Vec3( -side,-side,-side ));
		t.add(new Vec2(0.0f, dim));
		positions.add(new Vec3( side,-side,-side ));
		t.add(new Vec2(dim, dim));
		positions.add(new Vec3( -side,side,-side ));
		t.add(new Vec2(0.0f, 0.0f));
		positions.add(new Vec3( side,side,-side ));
		t.add(new Vec2(dim, 0.0f));
		positions.add(new Vec3( -side,-side,side ));
		t.add(new Vec2(dim, dim));
		positions.add(new Vec3( side,-side,side ));
		t.add(new Vec2(0.0f, dim));
		positions.add(new Vec3( -side,side,side ));
		t.add(new Vec2(dim, 0.0f));
		positions.add(new Vec3( side,side,side ));
		t.add(new Vec2(0.0f, 0.0f));
		
		ArrayList<Vertex> vertices = new ArrayList<Vertex>(num);
		
		for (int i = 0; i < num; i++) {
			vertices.add(new Vertex(positions.get(i), t.get(i)));
		}
		
		return new IndexedTriangleList<Vertex>(vertices, new int[] {
				0,2,1, 2,3,1,
				1,3,5, 3,7,5,
				2,6,3, 3,6,7,
				4,5,7, 4,7,6,
				0,4,2, 2,4,6,
				0,1,4, 1,5,4	
		});
	}
}
