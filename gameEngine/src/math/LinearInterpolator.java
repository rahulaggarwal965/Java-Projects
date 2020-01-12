package math;

public class LinearInterpolator extends Interpolator {

	public LinearInterpolator(float[] x, float[] y) {
		super(x, y);
	}

	@Override
	public float interpolate(float value) {
		int index = 0;
		for (int i = 0; i < this.x.length - 1; i++) {
			if(value < this.x[i]) break;
			index = i;
		}
		float a = (value - x[index])/(x[index + 1] - x[index]);
		
		//Linear Interpolation
		return Maths.interpolate(y[index], y[index + 1], a);
	}

}
