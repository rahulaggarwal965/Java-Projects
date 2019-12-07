package threeDimensions;

import java.util.ArrayList;

public class Pipeline {
	
	private Graphics3D g3d;
	private Mat3 rotation;
	private Vec3 translation;
	private ScreenTransformer st;
	private ZBuffer depthBuffer;
	public FragmentShader fragShader;
	
	public Pipeline() {
		st = new ScreenTransformer();
		fragShader = new FragmentShader();
		depthBuffer = new ZBuffer();
	}
	
	public void beginFrame() {
		depthBuffer.clear();
	}
	
	public void draw(Graphics3D g3d, IndexedTriangleList<Vertex> triangleList) {
		this.g3d = g3d;
		processVertices(triangleList.vertices, triangleList.indices);
	}
	
	public void setRotation(Mat3 rotation) {
		this.rotation = rotation;
	}
	
	public void setTranslation(Vec3 translation) {
		this.translation = translation;
	}
	
	//I use ArrayLists to prepare for infinite terrain
	private void processVertices(ArrayList<Vertex> vertices, ArrayList<Integer> indices) {
		ArrayList<Vertex> vOut = new ArrayList<Vertex>(vertices.size());
		
		for (Vertex v : vertices) {
			vOut.add(new Vertex(rotation._multiply(v.pos)._add(translation), v.t ));
		}
		
		assembleTriangles(vOut, indices);
	}
	
	private void assembleTriangles(ArrayList<Vertex> vertices, ArrayList<Integer> indices) {
		for (int i = 0; i < indices.size() / 3; i++) {
			Vertex v0 = vertices.get(indices.get(i*3));
			Vertex v1 = vertices.get(indices.get(i*3 + 1));
			Vertex v2 = vertices.get(indices.get(i*3 + 2));
			
			//Backface Culling
			if( (v1.pos._subtract(v0.pos).cross(v2.pos._subtract(v0.pos)).dot(v0.pos) <= 0.0f)) {
				processTriangle(new Vertex(v0), new Vertex(v1), new Vertex(v2));
			}
		}
	}
	
	private void processTriangle(Vertex v0, Vertex v1, Vertex v2) {
		//Geometry Shader
		
		postProcessTriangleVertices(new Triangle<Vertex>(v0, v1, v2));
	}
	
	private void postProcessTriangleVertices(Triangle<Vertex> triangle) {
		st.transform(triangle.v0);
		st.transform(triangle.v1);
		st.transform(triangle.v2);
		
		drawTriangle(triangle);
	}
	
	private void drawTriangle(Triangle<Vertex> triangle) {
		
		Vertex v0 = triangle.v0;
		Vertex v1 = triangle.v1;
		Vertex v2 = triangle.v2;
		
		//Swapping so we can sort by y
		if(v1.pos.y < v0.pos.y) {Vertex temp = v0; v0 = v1; v1 = temp;}
		if(v2.pos.y < v1.pos.y) {Vertex temp = v1; v1 = v2; v2 = temp;}
		if(v1.pos.y < v0.pos.y) {Vertex temp = v0; v0 = v1; v1 = temp;}
		
		if(v0.pos.y == v1.pos.y) {
			if(v1.pos.x < v0.pos.x) {Vertex temp = v0; v0 = v1; v1 = temp;}
			this.drawFlatTopTriangle(v0, v1, v2);
		} else if(v1.pos.y == v2.pos.y) {
			if(v2.pos.x < v1.pos.x) {Vertex temp = v1; v1 = v2; v2 = temp;}
			this.drawFlatBottomTriangle(v0, v1, v2);
		} else {
			final float a = (v1.pos.y - v0.pos.y)/(v2.pos.y - v0.pos.y);
			Vertex v = v0.interpolateTo(v2, a);
			if(v1.pos.x < v.pos.x) {
				this.drawFlatBottomTriangle(v0, v1, v);
				this.drawFlatTopTriangle(v1, v, v2);
			} else {
				this.drawFlatBottomTriangle(v0, v, v1);
				this.drawFlatTopTriangle(v, v1, v2);
			}
		}
	}
	
	private void drawFlatTopTriangle(Vertex v0, Vertex v1, Vertex v2) {
		final float deltaY = v2.pos.y - v0.pos.y;
		final Vertex dv0= v2._subtract(v0)._divide(deltaY);
		final Vertex dv1 = v2._subtract(v1)._divide(deltaY);
		
		Vertex iteratorEdge2  = new Vertex(v1);
		
		this.drawFlatTriangle(v0, v1, v2, dv0, dv1, iteratorEdge2);
	}
	
	private void drawFlatBottomTriangle(Vertex v0, Vertex v1, Vertex v2) {
		final float deltaY = v2.pos.y - v0.pos.y;
		final Vertex dv0= v1._subtract(v0)._divide(deltaY);
		final Vertex dv1 = v2._subtract(v0)._divide(deltaY);
		
		Vertex iteratorEdge2  = new Vertex(v0);
		
		this.drawFlatTriangle(v0, v1, v2, dv0, dv1, iteratorEdge2);
	}
	
	private void drawFlatTriangle(Vertex v0, Vertex v1, Vertex v2, Vertex dv0, Vertex dv1, Vertex iteratorEdge1) {
		
		Vertex iteratorEdge0 = new Vertex(v0);
		
		final int yStart = (int) Math.ceil(v0.pos.y - 0.5f);
		final int yEnd = (int) Math.ceil(v2.pos.y - 0.5f);
		
		iteratorEdge0.add(dv0._multiply((float) yStart + 0.5f - v0.pos.y));
		iteratorEdge1.add(dv1._multiply((float) yStart + 0.5f - v0.pos.y));
		
		for(int y = yStart; y < yEnd; y++, iteratorEdge0.add(dv0), iteratorEdge1.add(dv1)) {
			
			
			int xStart = (int) Math.ceil(iteratorEdge0.pos.x - 0.5f);
			int xEnd = (int) Math.ceil(iteratorEdge1.pos.x - 0.5f);
			
			final Vertex dIteratorLine = iteratorEdge1._subtract(iteratorEdge0)._divide(iteratorEdge1.pos.x - iteratorEdge0.pos.x);
			
			Vertex iteratorLine = iteratorEdge0._add(dIteratorLine._multiply((float) xStart + 0.5f - iteratorEdge0.pos.x));
			
			for(int x = xStart; x < xEnd; x++, iteratorLine.add(dIteratorLine)) {
				final float z = 1.0f/ iteratorLine.pos.z;
				
				if(depthBuffer.testAndSet(x, y, z)) {
					final Vertex attributes = iteratorLine._multiply(z);
					
					this.g3d.drawPixel(x, y, fragShader.shade(attributes));
				}
			}
		}
	}
	
}
