package shaders;

import threeDimensions.PackedColor;
import threeDimensions.Triangle;
import threeDimensions.Vec4;
import threeDimensions.Vertex;

public class ColorShader extends Shader {

	@Override
	public Vertex VertexShader(float[] v) {
		Vec4 position = new Vec4(v[0], v[1], v[2], 1.0f);
		this.worldViewProjection.multiply(position);
		return new Vertex(position, v[3], v[4], v[5]);
	}
	
	@Override
	public int FragmentShader(Vertex v, Triangle t) {
		return PackedColor.makeRGB(v.data[0], v.data[1], v.data[2]);
	}
}
