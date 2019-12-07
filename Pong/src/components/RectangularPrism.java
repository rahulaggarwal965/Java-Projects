package components;

import java.util.ArrayList;
import java.util.Arrays;

import threeDimensions.IndexedLineList;
import threeDimensions.IndexedTriangleList;
import threeDimensions.Vec2;
import threeDimensions.Vec3;
import threeDimensions.Vertex;

public class RectangularPrism {
	
	public static IndexedTriangleList<Vertex> getTriangles(float width, float height, float depth) {
		
		final float w2 = width/2, h2 = height/2, d2 = depth/2;
		
		final int num = 8;
		
		ArrayList<Vec3> positions = new ArrayList<Vec3>(num);
		ArrayList<Vec2> t = new ArrayList<Vec2>(num);
		
		positions.add(new Vec3( -w2,-h2,-d2 ));
		positions.add(new Vec3( w2,-h2,-d2 ));
		positions.add(new Vec3( -w2,h2,-d2 ));
		positions.add(new Vec3( w2,h2,-h2 ));
		positions.add(new Vec3( -w2,-h2,h2 ));
		positions.add(new Vec3( w2,-h2,h2 ));
		positions.add(new Vec3( -w2,h2,h2 ));
		positions.add(new Vec3( w2,h2,h2 ));
		
		ArrayList<Vertex> vertices = new ArrayList<Vertex>(num);
		
		for (int i = 0; i < num; i++) {
			vertices.add(new Vertex(positions.get(i)));
		}
		
		return new IndexedTriangleList<Vertex>(vertices, new ArrayList<Integer>(Arrays.asList(
				0,2,1, 2,3,1,
				1,3,5, 3,7,5,
				2,6,3, 3,6,7,
				4,5,7, 4,7,6,
				0,4,2, 2,4,6,
				0,1,4, 1,5,4	
		)));
	}
	
public static IndexedLineList<Vertex> getLines(float width, float height, float depth) {
		
		final float w2 = width/2, h2 = height/2, d2 = depth/2;
		
		final int num = 8;
		
		ArrayList<Vec3> positions = new ArrayList<Vec3>(num);
		ArrayList<Vec2> t = new ArrayList<Vec2>(num);
		
		positions.add(new Vec3( -w2,-h2,-d2 ));
		positions.add(new Vec3( w2,-h2,-d2 ));
		positions.add(new Vec3( -w2,h2,-d2 ));
		positions.add(new Vec3( w2,h2,-h2 ));
		positions.add(new Vec3( -w2,-h2,h2 ));
		positions.add(new Vec3( w2,-h2,h2 ));
		positions.add(new Vec3( -w2,h2,h2 ));
		positions.add(new Vec3( w2,h2,h2 ));
		
		ArrayList<Vertex> vertices = new ArrayList<Vertex>(num);
		
		for (int i = 0; i < num; i++) {
			vertices.add(new Vertex(positions.get(i)));
		}
		
		return new IndexedLineList<Vertex>(vertices, new ArrayList<Integer>(Arrays.asList(
				0,1,  1,3,  3,2,  2,0,
				0,4,  1,5,	3,7,  2,6,
				4,5,  5,7,	7,6,  6,4
		)));
	}
}
