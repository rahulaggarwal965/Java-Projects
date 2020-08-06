package math;

import threeDimensions.PackedColor;

public class Gradient {

	private Vec3[] colors;
	private float[] positions;

	public Gradient(Vec3[] colors, float[] positions) {
		if(colors.length != positions.length) throw new IllegalArgumentException("Length of Colors must match length of positions");
		this.colors = colors;
		this.positions = positions;
	}
	
	public Vec3 getColor(float position) {
		position = Maths.clamp(position, 0f, 1f);
		int num = this.positions.length - 1;
		if(position > this.positions[num]) return this.colors[num];
		int i;
		for (i = 0; i < num; i++) {
			if(position <= positions[i+1]) break;
		}
		float a = (position - positions[i])/(positions[i+1] - positions[i]);
		
		//Linear Interpolation
		return colors[i].interpolateTo(colors[i+1], a);
	}
	
	//TODO: Add different interpolation types (linear, quadratic, cubic, etc.)
	public int getPackedColor(float position) {
		return PackedColor.makeRGB(getColor(position));
	}

}
