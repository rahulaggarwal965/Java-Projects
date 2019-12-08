package libraries;

public abstract class Interpolator {
	protected float[] x, y;
	
	public Interpolator(float[] x, float[] y) {
		assert(x.length == y.length);
		this.x = x;
		this.y = y;
	}
	
	public abstract float interpolate(float value);
}
