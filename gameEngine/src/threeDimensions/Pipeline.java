package threeDimensions;

import gameEngine.GameEngine;
import math.Vec4;
import shaders.Shader;

public class Pipeline {

	public static final int TRIANGLE_MODE = 0;
	public static final int LINE_MODE = 1;
	
	public static final int RGB = 0;
	public static final int ARGB = 1;
	public static final int ADDITIVE = 2;
		
	private Graphics3D g3d;
	private ZBuffer depthBuffer;
	private Shader s;
	private ScreenTransformer st;
	
	private int drawMode = TRIANGLE_MODE;
	private int colorMode = RGB;
	private boolean depthTest = true;
	
	public Pipeline(Shader s) {
		this.s = s;
		this.st = new ScreenTransformer();
		this.depthBuffer = new ZBuffer();
	}
	
	public void setDrawMode(int mode) {
		this.drawMode = mode;
	}
	
	public void setColorMode(int mode) {
		this.colorMode = mode;
	}
	
	public void setDepthTest(boolean state) {
		this.depthTest = state;
	}
	
	public void setShader(Shader s) {
		this.s = s;
	}
	
	public Shader getShader() {
		return this.s;
	}
	
	public void beginFrame() {
		depthBuffer.clear();
		
	}
	
	public void draw(Graphics3D g3d, Mesh m) {
		this.g3d = g3d;
		processVertices(m.vertices, m.indices);
	}
	
	private void processVertices(float[][] vertices, int[] indices) {
		
		Vertex[] verticesOut = new Vertex[vertices.length];
		for (int i = 0; i < vertices.length; i++) {
			verticesOut[i] = this.s.VertexShader(vertices[i]);
		}
		
		assembleTriangles(verticesOut, indices);
	}
	
	private void assembleTriangles(Vertex[] vertices, int[] indices) {
		final Vec4 eye = this.s.getProjection()._multiply(new Vec4(0, 0, 0, 1));
		for (int i = 0; i < indices.length / 3; i++) {
			Vertex v0 = vertices[indices[i*3]];
			Vertex v1 = vertices[indices[i*3 + 1]];
			Vertex v2 = vertices[indices[i*3 + 2]];
			
			//Backface Culling
			if( (v1.position._subtract(v0.position).cross(v2.position._subtract(v0.position)).dot(v0.position._subtract(eye)) <= 0.0f)) {
				processTriangle(v0, v1, v2);
			}
		}
	}
	
	private void processTriangle(Vertex v0, Vertex v1, Vertex v2) {
		//Geometry Shader
		
		clipCullTriangle(this.s.GeometryShader(v0, v1, v2));
	}
	
	//Clipping Routines
	public void clip1(Vertex v0, Vertex v1, Vertex v2) {
		final float aA = (-v0.position.z) / (v1.position.z - v0.position.z);
		final float aB = (-v0.position.z) / (v2.position.z - v0.position.z);
		
		final Vertex v0A = v0.interpolateTo(v1, aA);
		final Vertex v0B = v0.interpolateTo(v2, aB);
		
		//Ugly but optimized for the least amount of copying
		postProcessTriangleVertices(new Triangle(new Vertex(v0A), v1, new Vertex(v2)));
		postProcessTriangleVertices(new Triangle(v0B, v0A, v2));
	}
	
	public void clip2(Vertex v0, Vertex v1, Vertex v2) {
		final float aA = (-v0.position.z) / (v2.position.z - v0.position.z);
		final float aB = (-v1.position.z) / (v2.position.z - v1.position.z);
		
		v0 = v0.interpolateTo(v2, aA);
		v1 = v1.interpolateTo(v2, aB);
		
		postProcessTriangleVertices(new Triangle(v0, v1, v2));
	}
	
	private void clipCullTriangle(Triangle t) {
		
		if(t.v0.position.x > t.v0.position.w && t.v1.position.x > t.v1.position.w && t.v2.position.x > t.v2.position.w) return;
		if(t.v0.position.x < -t.v0.position.w && t.v1.position.x < -t.v1.position.w && t.v2.position.x < -t.v2.position.w) return;
		if(t.v0.position.y > t.v0.position.w && t.v1.position.y > t.v1.position.w && t.v2.position.y > t.v2.position.w) return;
		if(t.v0.position.y < -t.v0.position.w && t.v1.position.y < -t.v1.position.w && t.v2.position.y < -t.v2.position.w) return;
		if(t.v0.position.z > t.v0.position.w && t.v1.position.z > t.v1.position.w && t.v2.position.z > t.v2.position.w) return;
		if(t.v0.position.z < 0 && t.v1.position.z < 0 && t.v2.position.z < 0) return;
		
		
		if(t.v0.position.z < 0) {
			if(t.v1.position.z < 0) {
				clip2(t.v0, t.v1, t.v2);
			} else if(t.v2.position.z < 0) {
				clip2(t.v0, t.v2, t.v1);
			} else {
				clip1(t.v0, t.v1, t.v2);
			}
		} else if(t.v1.position.z < 0) {
			if(t.v2.position.z < 0) {
				clip2(t.v1, t.v2, t.v0);
			} else {
				clip1(t.v1, t.v0, t.v2);
			}
		} else if (t.v2.position.z < 0){
			clip1(t.v2, t.v0, t.v1);
		} else {
			postProcessTriangleVertices(t);
		}
	}
	
	private void postProcessTriangleVertices(Triangle t) {
		st.transform(t.v0);
		st.transform(t.v1);
		st.transform(t.v2);
		
		if(this.drawMode == TRIANGLE_MODE) {
			drawTriangle(t);
		} else if(this.drawMode == LINE_MODE) {
			this.g3d.drawTriangle(t, this.s.getDefaultColor());
		}
	}
	
	private void drawTriangle(Triangle triangle) {
		
		Vertex v0 = triangle.v0;
		Vertex v1 = triangle.v1;
		Vertex v2 = triangle.v2;
		
		//Swapping so we can sort by y
		if(v1.position.y < v0.position.y) {Vertex temp = v0; v0 = v1; v1 = temp;}
		if(v2.position.y < v1.position.y) {Vertex temp = v1; v1 = v2; v2 = temp;}
		if(v1.position.y < v0.position.y) {Vertex temp = v0; v0 = v1; v1 = temp;}
		
		if(v0.position.y == v1.position.y) {
			if(v1.position.x < v0.position.x) {Vertex temp = v0; v0 = v1; v1 = temp;}
			this.drawFlatTopTriangle(triangle, v0, v1, v2);
		} else if(v1.position.y == v2.position.y) {
			if(v2.position.x < v1.position.x) {Vertex temp = v1; v1 = v2; v2 = temp;}
			this.drawFlatBottomTriangle(triangle, v0, v1, v2);
		} else {
			final float a = (v1.position.y - v0.position.y)/(v2.position.y - v0.position.y);
			Vertex v = v0.interpolateTo(v2, a);
			if(v1.position.x < v.position.x) {
				this.drawFlatBottomTriangle(triangle, v0, v1, v);
				this.drawFlatTopTriangle(triangle, v1, v, v2);
			} else {
				this.drawFlatBottomTriangle(triangle, v0, v, v1);
				this.drawFlatTopTriangle(triangle, v, v1, v2);
			}
		}
	}
	
	private void drawFlatTopTriangle(Triangle t, Vertex v0, Vertex v1, Vertex v2) {
		final float deltaY = v2.position.y - v0.position.y;
		final Vertex dv0= v2._subtract(v0)._divide(deltaY);
		final Vertex dv1 = v2._subtract(v1)._divide(deltaY);
		
		Vertex iteratorEdge2  = new Vertex(v1);
		
		this.drawFlatTriangle(t, v0, v1, v2, dv0, dv1, iteratorEdge2);
	}
	
	private void drawFlatBottomTriangle(Triangle t, Vertex v0, Vertex v1, Vertex v2) {
		final float deltaY = v2.position.y - v0.position.y;
		final Vertex dv0= v1._subtract(v0)._divide(deltaY);
		final Vertex dv1 = v2._subtract(v0)._divide(deltaY);
		
		Vertex iteratorEdge2  = new Vertex(v0);
		
		this.drawFlatTriangle(t, v0, v1, v2, dv0, dv1, iteratorEdge2);
	}
	
	private void drawFlatTriangle(Triangle t, Vertex v0, Vertex v1, Vertex v2, Vertex dv0, Vertex dv1, Vertex iteratorEdge1) {
		
		Vertex iteratorEdge0 = new Vertex(v0);
		
		final int yStart = Math.max((int) Math.ceil(v0.position.y - 0.5f), 0);
		final int yEnd = Math.min((int) Math.ceil(v2.position.y - 0.5f), GameEngine.displayHeight - 1);;
		
		iteratorEdge0.add(dv0._multiply((float) yStart + 0.5f - v0.position.y));
		iteratorEdge1.add(dv1._multiply((float) yStart + 0.5f - v0.position.y));
		
		for(int y = yStart; y < yEnd; y++, iteratorEdge0.add(dv0), iteratorEdge1.add(dv1)) {
			
			
			int xStart = Math.max((int) Math.ceil(iteratorEdge0.position.x - 0.5f), 0);
			int xEnd = Math.min((int) Math.ceil(iteratorEdge1.position.x - 0.5f), GameEngine.displayWidth - 1);
			
			final Vertex dIteratorLine = iteratorEdge1._subtract(iteratorEdge0)._divide(iteratorEdge1.position.x - iteratorEdge0.position.x);
			
			Vertex iteratorLine = iteratorEdge0._add(dIteratorLine._multiply((float) xStart + 0.5f - iteratorEdge0.position.x));
			
			for(int x = xStart; x < xEnd; x++, iteratorLine.add(dIteratorLine)) {
				
				
				if(!this.depthTest || depthBuffer.testAndSet(x, y, iteratorLine.position.z)) {
					final float w = 1.0f/ iteratorLine.position.w;
					
					final Vertex attributes = iteratorLine._multiply(w);
					
					if(this.colorMode == Pipeline.RGB) {
						this.g3d.drawPixel(x, y, this.s.FragmentShader(attributes, t));
					} else if(this.colorMode == Pipeline.ARGB) {
						this.g3d.drawAlphaPixel(x, y, this.s.FragmentShader(attributes, t));
					} else if(this.colorMode == Pipeline.ADDITIVE) {
						this.g3d.drawAdditivePixel(x, y, this.s.FragmentShader(attributes, t));
					}
				}
			}
		}
	}

}
