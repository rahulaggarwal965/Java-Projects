package components;

import java.util.ArrayList;
import java.util.Arrays;

import threeDimensions.IndexedTriangleList;
import threeDimensions.Vec2;
import threeDimensions.Vec3;
import threeDimensions.Vertex;

public class TestTriangle {

	public static IndexedTriangleList<Vertex> getPlain() {
		
		ArrayList<Vec3> positions = new ArrayList<Vec3>(3);
		
		positions.add(new Vec3( 0, 0.8f, 0));
		positions.add(new Vec3( 0.8f, -0.8f, 0));
		positions.add(new Vec3( -0.8f, -0.8f, 0.0f));
		
		ArrayList<Vertex> vertices = new ArrayList<Vertex>(3);
		
		for (int i = 0; i < 3; i++) {
			vertices.add(new Vertex(positions.get(i), new Vec2(0,0)));
		}
		
		return new IndexedTriangleList<Vertex>(vertices, new int[] {
				0,1,2
		});
	}

}
