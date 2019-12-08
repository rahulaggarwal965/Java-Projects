package components;

import threeDimensions.PackedColor;
import threeDimensions.Vec3;

public class Gradient {
	
	private Vec3[] colors;
	private float[] positions;

	public Gradient(Vec3[] colors, float[] positions) {
		assert(colors.length == positions.length);
		this.colors = colors;
		this.positions = positions;
	}
	
	//TODO: Add different interpolation types (linear, quadratic, cubic, etc.)
	public int getColor(float position) {
		assert(position >= 0f && position <= 1f);
		int index = 0;
		for (int i = 0; i < positions.length - 1; i++) {
			if(position < positions[i]) break;
			index = i;
		}
		float a = (position - positions[index])/(positions[index + 1] - positions[index]);
		
		//Linear Interpolation
		return PackedColor.makeRGB(colors[index].interpolateTo(colors[index + 1], a));
	}
}
