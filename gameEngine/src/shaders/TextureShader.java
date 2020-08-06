package shaders;

import math.Vec4;
import threeDimensions.Texture;
import threeDimensions.Triangle;
import threeDimensions.Vertex;

public class TextureShader extends Shader {

	protected Texture texture;
	protected float tWidth, tHeight, tXClamp, tYClamp;
	
	public void setTexture(Texture texture) {
		this.texture = texture;
		this.tWidth = (float) texture.getWidth();
		this.tHeight = (float) texture.getHeight();
		this.tXClamp = tWidth - 1.0f;
		this.tYClamp = tHeight - 1.0f;
	}
	
	@Override
	public Vertex VertexShader(float[] v) {
		Vec4 position = new Vec4(v[0], v[1], v[2], 1.0f);
		this.worldViewProjection.multiply(position);
		return new Vertex(position, v[3], v[4]);
	}
	
	@Override	
	public int FragmentShader(Vertex v, Triangle triangle) {
		return this.texture.getPixel(
				(int) Math.min(v.data[0] * tWidth + 0.5f, tXClamp),
				(int) Math.min(v.data[1] * tHeight + 0.5f,  tYClamp));
				
	}

}
