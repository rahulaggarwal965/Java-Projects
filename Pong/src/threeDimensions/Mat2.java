package threeDimensions;

public class Mat2 {
	public float[][] data;
	
	public Mat2() {
		 this.data = new float[2][2];
	}
	
	public Mat2(float[][] data) {
		this();
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++ ) {
				this.data[i][j] = data[i][j];
			}
		}
	}
	
	public Mat2(float[] data) {
		this();
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				this.data[i][j] = data[i * 2 + j];
			}
		}
	}
	
	public Mat2(Mat2 m) {
		this(m.data);
	}
	
	public void multiply(float f) {
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++ ) {
				this.data[i][j] *= f;
			}
		}
	}
	
	public Mat2 multiply(Mat2 m) {
		Mat2 r = new Mat2();
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				for (int k = 0; k < 2; k++) {
					r.data[i][j] += (this.data[i][k] * m.data[k][j]);
				}
			}
		}
		return r;
	}
	
	public static Mat2 identity() {
		return new Mat2(new float[] {1.0f, 0.0f, 0.0f, 1.0f});
	}
	
	public static Mat2 rotation(float theta) {
		float cos = (float) Math.cos(theta); //Must be in radians
		float sin = (float) Math.sin(theta); //Must be in radians
		return new Mat2(new float[] {cos, -sin, sin, cos});
	}
	
	public static Mat2 scaling(float factor) {
		return new Mat2(new float[] {factor, 0.0f, 0.0f, factor});
	}
	
	public void multiply(Vec2 v) {
		float x = this.data[0][0]*v.x + this.data[0][1]*v.y;
		float y = this.data[1][0]*v.x + this.data[1][1]*v.y;
		v.x = x;
		v.y = y;
	}
	
	public Vec2 _multiply(Vec2 v) {
		return new Vec2(
				this.data[0][0]*v.x + this.data[0][1]*v.y,
				this.data[1][0]*v.x + this.data[1][1]*v.y
				);
	}
	
	
}
