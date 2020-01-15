package threeDimensions;

import java.util.ArrayList;

import gameEngine.GameEngine;

public class Pipeline {
	
	private Graphics3D g3d;
	private ScreenTransformer st;
	private ZBuffer depthBuffer;
	public FragmentShader fragShader;
	
	
	//Transformation
	private Matrix world = Matrix.Identity(4);
	private Matrix projection = Matrix.Identity(4);
	private Matrix view = Matrix.Identity(4);
	private Matrix worldView = Matrix.Identity(4);
	private Matrix worldViewProjection = Matrix.Identity(4);
	

	public Pipeline() {
		st = new ScreenTransformer();
		fragShader = new FragmentShader();
		depthBuffer = new ZBuffer();
	}
	
	public void beginFrame() {
		depthBuffer.clear();
	}
	
	public void setWorld(Matrix transformation) {
		this.world = transformation;
		this.worldView = this.view.multiply(this.world);
		this.worldViewProjection = this.projection.multiply(this.worldView);
	}
	
	public void setProjection(Matrix projection) {
		this.projection = projection;
		this.worldViewProjection = this.projection.multiply(this.worldView);
	}
	
	public void setView(Matrix view) {
		this.view = view;
		this.worldView = this.view.multiply(this.world);
		this.worldViewProjection = this.projection.multiply(this.worldView);
	}
	
	public void draw(Graphics3D g3d, IndexedTriangleList<Vertex> triangleList) {
		this.g3d = g3d;
		processVertices(triangleList.vertices, triangleList.indices);
	}
	
	//I use ArrayLists to prepare for infinite terrain TODO: DO I really need?
	private void processVertices(ArrayList<Vertex> vertices, int[] indices) {
		ArrayList<VertexOut> vOut = new ArrayList<VertexOut>(vertices.size());
		
		for (Vertex v : vertices) {
			VertexOut vO = new VertexOut(this.worldViewProjection._multiply(new Vec4(v.pos)), v.t);
			vOut.add(vO);
		}
		
		assembleTriangles(vOut, indices);
	}
	
	private void assembleTriangles(ArrayList<VertexOut> vertices, int[] indices) {
		final Vec4 eye = this.projection._multiply(new Vec4(0, 0, 0, 1));
		for (int i = 0; i < indices.length / 3; i++) {
			VertexOut v0 = vertices.get(indices[i*3]);
			VertexOut v1 = vertices.get(indices[i*3 + 1]);
			VertexOut v2 = vertices.get(indices[i*3 + 2]);
			
			//Backface Culling
			if( (v1.pos._subtract(v0.pos).cross(v2.pos._subtract(v0.pos)).dot(v0.pos._subtract(eye)) <= 0.0f)) {
				processTriangle(new VertexOut(v0), new VertexOut(v1), new VertexOut(v2));
			}
		}
	}
	
	private void processTriangle(VertexOut v0, VertexOut v1, VertexOut v2) {
		//Geometry Shader
		
		clipCullTriangle(new Triangle<VertexOut>(v0, v1, v2));
	}
	
	//Clipping Routines
	public void clip1(VertexOut v0, VertexOut v1, VertexOut v2) {
		final float aA = (-v0.pos.z) / (v1.pos.z - v0.pos.z);
		final float aB = (-v0.pos.z) / (v2.pos.z - v0.pos.z);
		
		final VertexOut v0A = v0.interpolateTo(v1, aA);
		final VertexOut v0B = v0.interpolateTo(v2, aB);
		
		
		//Ugly but optimized for the least amount of copying
		postProcessTriangleVertices(new Triangle<VertexOut>(new VertexOut(v0A), v1, new VertexOut(v2)));
		postProcessTriangleVertices(new Triangle<VertexOut>(v0B, v0A, v2));
	}
	
	public void clip2(VertexOut v0, VertexOut v1, VertexOut v2) {
		final float aA = (-v0.pos.z) / (v2.pos.z - v0.pos.z);
		final float aB = (-v1.pos.z) / (v2.pos.z - v1.pos.z);
		
		v0 = v0.interpolateTo(v2, aA);
		v1 = v1.interpolateTo(v2, aB);
		
		postProcessTriangleVertices(new Triangle<VertexOut>(v0, v1, v2));
	}
	
	private void clipCullTriangle(Triangle<VertexOut> t) {
		
		if(t.v0.pos.x > t.v0.pos.w && t.v1.pos.x > t.v1.pos.w && t.v2.pos.x > t.v2.pos.w) return;
		if(t.v0.pos.x < -t.v0.pos.w && t.v1.pos.x < -t.v1.pos.w && t.v2.pos.x < -t.v2.pos.w) return;
		if(t.v0.pos.y > t.v0.pos.w && t.v1.pos.y > t.v1.pos.w && t.v2.pos.y > t.v2.pos.w) return;
		if(t.v0.pos.y < -t.v0.pos.w && t.v1.pos.y < -t.v1.pos.w && t.v2.pos.y < -t.v2.pos.w) return;
		if(t.v0.pos.z > t.v0.pos.w && t.v1.pos.z > t.v1.pos.w && t.v2.pos.z > t.v2.pos.w) return;
		if(t.v0.pos.z < 0 && t.v1.pos.z < 0 && t.v2.pos.z < 0) return;
		
		
		if(t.v0.pos.z < 0) {
			if(t.v1.pos.z < 0) {
				clip2(t.v0, t.v1, t.v2);
			} else if(t.v2.pos.z < 0) {
				clip2(t.v0, t.v2, t.v1);
			} else {
				clip1(t.v0, t.v1, t.v2);
			}
		} else if(t.v1.pos.z < 0) {
			if(t.v2.pos.z < 0) {
				clip2(t.v1, t.v2, t.v0);
			} else {
				clip1(t.v1, t.v0, t.v2);
			}
		} else if (t.v2.pos.z < 0){
			clip1(t.v2, t.v0, t.v1);
		} else {
			postProcessTriangleVertices(t);
		}
	}
	
	private void postProcessTriangleVertices(Triangle<VertexOut> t) {
		st.transform(t.v0);
		st.transform(t.v1);
		st.transform(t.v2);
		
		drawTriangle(t);
	}
	
	private void drawTriangle(Triangle<VertexOut> triangle) {
		
		VertexOut v0 = triangle.v0;
		VertexOut v1 = triangle.v1;
		VertexOut v2 = triangle.v2;
		
		//Swapping so we can sort by y
		if(v1.pos.y < v0.pos.y) {VertexOut temp = v0; v0 = v1; v1 = temp;}
		if(v2.pos.y < v1.pos.y) {VertexOut temp = v1; v1 = v2; v2 = temp;}
		if(v1.pos.y < v0.pos.y) {VertexOut temp = v0; v0 = v1; v1 = temp;}
		
		if(v0.pos.y == v1.pos.y) {
			if(v1.pos.x < v0.pos.x) {VertexOut temp = v0; v0 = v1; v1 = temp;}
			this.drawFlatTopTriangle(v0, v1, v2);
		} else if(v1.pos.y == v2.pos.y) {
			if(v2.pos.x < v1.pos.x) {VertexOut temp = v1; v1 = v2; v2 = temp;}
			this.drawFlatBottomTriangle(v0, v1, v2);
		} else {
			final float a = (v1.pos.y - v0.pos.y)/(v2.pos.y - v0.pos.y);
			VertexOut v = v0.interpolateTo(v2, a);
			if(v1.pos.x < v.pos.x) {
				this.drawFlatBottomTriangle(v0, v1, v);
				this.drawFlatTopTriangle(v1, v, v2);
			} else {
				this.drawFlatBottomTriangle(v0, v, v1);
				this.drawFlatTopTriangle(v, v1, v2);
			}
		}
	}
	
	private void drawFlatTopTriangle(VertexOut v0, VertexOut v1, VertexOut v2) {
		final float deltaY = v2.pos.y - v0.pos.y;
		final VertexOut dv0= v2._subtract(v0)._divide(deltaY);
		final VertexOut dv1 = v2._subtract(v1)._divide(deltaY);
		
		VertexOut iteratorEdge2  = new VertexOut(v1);
		
		this.drawFlatTriangle(v0, v1, v2, dv0, dv1, iteratorEdge2);
	}
	
	private void drawFlatBottomTriangle(VertexOut v0, VertexOut v1, VertexOut v2) {
		final float deltaY = v2.pos.y - v0.pos.y;
		final VertexOut dv0= v1._subtract(v0)._divide(deltaY);
		final VertexOut dv1 = v2._subtract(v0)._divide(deltaY);
		
		VertexOut iteratorEdge2  = new VertexOut(v0);
		
		this.drawFlatTriangle(v0, v1, v2, dv0, dv1, iteratorEdge2);
	}
	
	private void drawFlatTriangle(VertexOut v0, VertexOut v1, VertexOut v2, VertexOut dv0, VertexOut dv1, VertexOut iteratorEdge1) {
		
		VertexOut iteratorEdge0 = new VertexOut(v0);
		
		final int yStart = Math.max((int) Math.ceil(v0.pos.y - 0.5f), 0);
		final int yEnd = Math.min((int) Math.ceil(v2.pos.y - 0.5f), GameEngine.displayHeight - 1);;
		
		iteratorEdge0.add(dv0._multiply((float) yStart + 0.5f - v0.pos.y));
		iteratorEdge1.add(dv1._multiply((float) yStart + 0.5f - v0.pos.y));
		
		for(int y = yStart; y < yEnd; y++, iteratorEdge0.add(dv0), iteratorEdge1.add(dv1)) {
			
			
			int xStart = Math.max((int) Math.ceil(iteratorEdge0.pos.x - 0.5f), 0);
			int xEnd = Math.min((int) Math.ceil(iteratorEdge1.pos.x - 0.5f), GameEngine.displayWidth - 1);
			
			final VertexOut dIteratorLine = iteratorEdge1._subtract(iteratorEdge0)._divide(iteratorEdge1.pos.x - iteratorEdge0.pos.x);
			
			VertexOut iteratorLine = iteratorEdge0._add(dIteratorLine._multiply((float) xStart + 0.5f - iteratorEdge0.pos.x));
			
			for(int x = xStart; x < xEnd; x++, iteratorLine.add(dIteratorLine)) {
				
				
				if(depthBuffer.testAndSet(x, y, iteratorLine.pos.z)) {
					final float w = 1.0f/ iteratorLine.pos.w;
					
					final VertexOut attributes = iteratorLine._multiply(w);
					
					this.g3d.drawPixel(x, y, fragShader.shade(attributes));
				}
			}
		}
	}
	
}
