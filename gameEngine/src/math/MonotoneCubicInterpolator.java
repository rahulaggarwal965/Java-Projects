package math;

public class MonotoneCubicInterpolator extends Interpolator {
	
	private float[] m;

	public MonotoneCubicInterpolator(float[] x, float[] y) {
		super(x, y);
		final int n = x.length;
		this.m = new float[n];
		float[] s = new float[n-1];
		
		//Calculate Secants Lines between each point and the next
		for (int i = 0; i < n - 1; i++) {
			s[i] = (y[i + 1] - y[i])/(x[i+1] - x[i]);
		}
		
		//Set the slopes for each point
		this.m[0] = s[0];
		for (int i = 1; i < n - 1; i++) {
			if((s[i - 1] < 0f && s[i] > 0f) || (s[i-1] > 0f && s[i] < 0f)) {
				this.m[i] = 0;
			} else {
				this.m[i] = (s[i - 1] + s[i]) * 0.5f;
			}
		}
		this.m[n - 1] = s[n - 2];
		
		//Prevent Overshoot
		for (int i = 0; i < n - 1; i++) {
			if(s[i] == 0f) {
				this.m[i] = 0f;
				this.m[i + 1] = 0f;
			} else {
				float a = this.m[i] / s[i];
				float b = this.m[i + 1] / s[i];
				float h = (float) Math.hypot(a, b);
				if(h > 9f) {
					float t = 3f / h;
					this.m[i] = t * a * s[i];
					this.m[i + 1] = t * b * s[i];
				}
			}
		}
	}

	@Override
	public float interpolate(float value) {
		assert(value >= this.x[0] && value <= this.x[x.length - 1]);
		int index = 0;
		for (int i = 0; i < this.x.length - 1; i++) {
			if(value == this.x[i]) return this.y[i];
			if(value < this.x[i]) break;
			index = i;
		}
		
		float h = this.x[index + 1] - this.x[index];
		float a = (value - this.x[index])/h;
		return (this.y[index] * (1 + 2 * a) + h * this.m[index] * a) * (1 - a) * (1 - a) 
				+ (this.y[index + 1] * (3 - 2 * a) + h * this.m[index + 1] * (a - 1)) * a * a;
	}

}
