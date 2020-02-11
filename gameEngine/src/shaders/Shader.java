package shaders;

import threeDimensions.Matrix;
import threeDimensions.PackedColor;
import threeDimensions.Triangle;
import threeDimensions.Vec4;
import threeDimensions.Vertex;

public class Shader {

	//RefactoredVertexShader Fields
	protected Matrix world = Matrix.Identity(4);
	protected Matrix projection = Matrix.Identity(4);
	protected Matrix view = Matrix.Identity(4);
	protected Matrix worldView = Matrix.Identity(4);
	protected Matrix worldViewProjection = Matrix.Identity(4);
	
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
	
	public Matrix getProjection() {
		return this.projection;
	}
	
	//GeometryShader Fields
	
	//FragmentShader Fields
	protected int defaultColor = PackedColor.White;
	
	public Vertex VertexShader(float[] v)  {
		Vec4 position = new Vec4(v[0], v[1], v[2], 1.0f);
		this.worldViewProjection.multiply(position);
		return new Vertex(position);
	}
	
	public Triangle GeometryShader(Vertex v0, Vertex v1, Vertex v2) {
		return new Triangle(
				new Vertex(v0),
				new Vertex(v1),
				new Vertex(v2)
				);
	}
	
	public int FragmentShader(Vertex vertex, Triangle triangle) {
		return this.defaultColor;
	}

}
