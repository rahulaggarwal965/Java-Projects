package shaders;

import math.Vec4;
import threeDimensions.PackedColor;
import threeDimensions.Triangle;
import threeDimensions.Vertex;

public class TextureTintShader extends TextureShader {

	@Override	
	public int FragmentShader(Vertex v, Triangle triangle) {
		Vec4 color = PackedColor.toVector(this.texture.getPixel(
				(int) Math.min(v.data[0] * tWidth + 0.5f, tXClamp),
				(int) Math.min(v.data[1] * tHeight + 0.5f,  tYClamp)));
		return PackedColor.makeRGBA(color._multiply(PackedColor.toVector(this.defaultColor)._divide(255.0f)));
		
				
	}

}
