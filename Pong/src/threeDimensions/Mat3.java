package threeDimensions;

public class Mat3 {
public float[][] data;
	
	public Mat3() {
		 this.data = new float[3][3];
	}
	
	public Mat3(float[][] data) {
		this();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++ ) {
				this.data[i][j] = data[i][j];
			}
		}
	}
	
	public Mat3(float[] data) {
		this();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				this.data[i][j] = data[i * 3 + j];
			}
		}
	}
	
	public Mat3(Mat3 m) {
		this(m.data);
	}
	
	public void multiply(float f) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++ ) {
				this.data[i][j] *= f;
			}
		}
	}
	
	public Mat3 multiply(Mat3 m) {
		Mat3 r = new Mat3();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				for (int k = 0; k < 3; k++) {
					r.data[i][j] += (this.data[i][k] * m.data[k][j]);
				}
			}
		}
		return r;
	}
	
	public static Mat3 identity() {
		return new Mat3(new float[] {1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f});
	}
	
	public static Mat3 rotationX(float theta) {
		float cos = (float) Math.cos(theta); //Must be in radians
		float sin = (float) Math.sin(theta); //Must be in radians
		return new Mat3(new float[] {
				1.0f, 0.0f, 0.0f, 
				0.0f, cos, -sin, 
				0.0f, sin, cos});
	}
	
	public static Mat3 rotationY(float theta) {
		float cos = (float) Math.cos(theta); //Must be in radians
		float sin = (float) Math.sin(theta); //Must be in radians
		return new Mat3(new float[] {
				cos, 0.0f, sin, 
				0.0f, 1.0f, 0.0f, 
				-sin, 0.0f, cos});
	}
	
	public static Mat3 rotationZ(float theta) {
		float cos = (float) Math.cos(theta); //Must be in radians
		float sin = (float) Math.sin(theta); //Must be in radians
		return new Mat3(new float[] {
				cos, -sin, 0.0f, 
				sin, cos, 0.0f, 
				0.0f, 0.0f, 1.0f});
	}
	
	public static Mat3 scaling(float factor) {
		return new Mat3(new float[] {factor, 0.0f, 0.0f, 0.0f, factor, 0.0f, 0.0f, 0.0f, factor});
	}
	
	
	
	public void multiply(Vec3 v) {
		float x = this.data[0][0]*v.x + this.data[0][1]*v.y + this.data[0][2]*v.z;
		float y = this.data[1][0]*v.x + this.data[1][1]*v.y + this.data[1][2]*v.z;
		float z = this.data[2][0]*v.x + this.data[2][1]*v.y + this.data[2][2]*v.z;
		v.x = x;
		v.y = y;
		v.z = z;
	}
	
	public Vec3 _multiply(Vec3 v) {
		return new Vec3(
				this.data[0][0]*v.x + this.data[0][1]*v.y + this.data[0][2]*v.z, 
				this.data[1][0]*v.x + this.data[1][1]*v.y + this.data[1][2]*v.z, 
				this.data[2][0]*v.x + this.data[2][1]*v.y + this.data[2][2]*v.z
				);
	}
}
