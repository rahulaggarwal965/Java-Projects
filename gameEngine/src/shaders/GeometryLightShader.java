package shaders;

import threeDimensions.Matrix;
import threeDimensions.PackedColor;
import threeDimensions.Triangle;
import threeDimensions.Vec3;
import threeDimensions.Vec4;
import threeDimensions.Vertex;

public class GeometryLightShader extends ColorShader {

	private Vec3 diffuse = new Vec3(1, 1, 1);
	private Vec3 ambient = new Vec3(0.3f, 0.3f, 0.3f);
	private Vec3 color = new Vec3(1f, 1.f, 1f);
	private Vec4 lightDirection = new Vec4(0, -1, 0, 0);
	private Vec4 transformedLightDirection = new Vec4(0, -1, 0, 0);
	
	public void setDiffuse(Vec3 diffuse) {
		this.diffuse = diffuse;
	}
	
	public void setAmbient(Vec3 ambient) {
		this.ambient = ambient;
	}

	public void setColor(Vec3 color) {
		this.color = color;
	}

	public void setLightDirection(Vec4 lightDirection) {
		if(lightDirection.magSq() >= 0.001f) {
			this.lightDirection = lightDirection;
			this.transformedLightDirection = this.view._multiply(lightDirection);
		}
	}
	
	public void setView(Matrix view) {
		this.view = view;
		this.worldView = this.view.multiply(this.world);
		this.worldViewProjection = this.projection.multiply(this.worldView);
		this.transformedLightDirection = this.view._multiply(lightDirection);
	}
	
	@Override
	public Triangle GeometryShader(Vertex v0, Vertex v1, Vertex v2) {
		Vec3 normal = (v1.position._subtract(v0.position).cross(v2.position._subtract(v0.position))).getNormalized();
		Vec3 diffuse = this.diffuse._multiply(Math.max(normal._negate().dot(this.transformedLightDirection), 0));
		
		//Vector matColor = Vector.fromArray(v0.data, 0, 3)._add(Vector.fromArray(v1.data, 0, 3))._add(Vector.fromArray(v2.data, 0, 3))._divide(3*255);
		Vec3 matColor = new Vec3(
				(v0.data[0] + v1.data[0] + v2.data[0])/(255*3),
				(v0.data[1] + v1.data[1] + v2.data[1])/(255*3), 
				(v0.data[2] + v1.data[2] + v2.data[2])/(255*3)
				);
		Vec3 vColor = matColor._multiply(this.color._multiply(diffuse._add(this.ambient))._clamp(0.0f, 1.0f)._multiply(255f));
		return new Triangle(
				new Vertex (new Vec4(v0.position)),
				new Vertex (new Vec4(v1.position)),
				new Vertex (new Vec4(v2.position)),
				PackedColor.makeRGB(vColor)
				);
	}
	
	@Override
	public int FragmentShader(Vertex v, Triangle triangle) {
		return triangle.color;
	}

}
