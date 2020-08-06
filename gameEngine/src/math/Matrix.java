package math;

public class Matrix {
	
	private float[] data;
	private int rows, cols;

	public Matrix(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		this.data = new float[rows * cols];
	}
	
	public Matrix(int rows, int cols, float ... data) {
		this.rows = rows;
		this.cols = cols;
		this.data = new float[rows * cols];
		for (int i = 0; i < data.length; i++) {
			this.data[i] = data[i];
		}
	}
	
	public Matrix(Matrix m) {
		this(m.rows, m.cols, m.data);
	}
	
	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}
	
	public float[] getData() {
		return this.data;
	}
	
	public boolean isSquare() {
		return this.rows == this.cols;
	}
	
	public void multiply(float f) {
		for (int i = 0; i < this.data.length; i++) {
			this.data[i] *= f;
		}
	}
	
	public Matrix _multiply(float f) {
		Matrix m = new Matrix(this);
		for (int i = 0; i < m.data.length; i++) {
			m.data[i] *= f;
		}
		return m;
	}
	
	public Matrix multiply(Matrix m) {
		if(this.cols != m.rows) throw new IllegalArgumentException("Columns do not match rows");
		Matrix r = new Matrix(this.rows, m.cols);
		
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < m.cols; j++) {
				float sum = 0;
				for (int k = 0; k < this.cols; k++) {
					sum += this.data[i*this.cols + k] * m.data[k*m.cols + j];
				}
				r.data[i*m.cols + j] = sum;
			}
		}
		return r;
		
	}
	
	public Matrix transpose() {
		Matrix r = new Matrix(this.cols, this.rows);
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				r.data[j + i*cols] = this.data[i + j*cols];
			}
		}
		return r;
	}
	
	public static Matrix Identity(int size) {
		if(size == 3) return new Matrix(3, 3, new float[] {
				1, 0, 0, 
				0, 1, 0, 
				0, 0, 1});
		else if(size == 4) return new Matrix(4, 4, new float[] {
				1, 0, 0, 0, 
				0, 1, 0, 0, 
				0, 0, 1, 0, 
				0, 0, 0, 1});
		else throw new IllegalArgumentException("Only size 3 and 4 identity matrices are supported");
		
	}
	
	public static Matrix Scale(int size, float factor) {
		if(size == 3) return new Matrix(3, 3, new float[] {
				factor, 0, 0, 
				0, factor, 0, 
				0, 0, factor});
		else if(size == 4) return new Matrix(4, 4, new float[] {
				factor, 0, 0, 0, 
				0, factor, 0, 0, 
				0, 0, factor, 0, 
				0, 0, 0, 1});
		else throw new IllegalArgumentException("Only size 3 and 4 scale matrices are supported");
	}
	
	public static Matrix Scale(int size, float x, float y, float z) {
		if(size == 3) return new Matrix(3, 3, new float[] {
				x, 0, 0, 
				0, y, 0, 
				0, 0, z});
		else if(size == 4) return new Matrix(4, 4, new float[] {
				x, 0, 0, 0, 
				0, y, 0, 0, 
				0, 0, z, 0, 
				0, 0, 0, 1});
		else throw new IllegalArgumentException("Only size 3 and 4 scale matrices are supported");
	}
	
	public static Matrix rotationX(int size, float theta) {
		float cos = (float) Math.cos(theta); //Must be in radians
		float sin = (float) Math.sin(theta); //Must be in radians
		if(size == 3) return new Matrix(3, 3, new float[] {
				1, 0, 0, 
				0, cos, -sin, 
				0, sin, cos});
		else if(size == 4) return new Matrix(4, 4, new float[] {
				1, 0, 0, 0, 
				0, cos, -sin, 0,
				0, sin, cos, 0, 
				0, 0, 0, 1});
		else throw new IllegalArgumentException("Only size 3 and 4 rotationX matrices are supported");
	}
	
	public static Matrix rotationY(int size, float theta) {
		float cos = (float) Math.cos(theta); //Must be in radians
		float sin = (float) Math.sin(theta); //Must be in radians
		if(size == 3) return new Matrix(3, 3, new float[] {
				cos, 0, sin, 
				0, 1, 0, 
				-sin, 0, cos});
		else if(size == 4) return new Matrix(4, 4, new float[] {
				cos, 0, sin, 0, 
				0, 1, 0, 0, 
				-sin, 0, cos, 0, 
				0, 0, 0, 1});
		else throw new IllegalArgumentException("Only size 3 and 4 rotationY matrices are supported");
	}
	
	public static Matrix rotationZ(int size, float theta) {
		float cos = (float) Math.cos(theta); //Must be in radians
		float sin = (float) Math.sin(theta); //Must be in radians
		if(size == 3) return new Matrix(3, 3, new float[] {
				cos, -sin, 0, 
				sin, cos, 0, 
				0, 0, 1});
		else if(size == 4) return new Matrix(4, 4, new float[] {
				cos, -sin, 0, 0, 
				sin, cos, 0, 0,
				0, 0, 1, 0, 
				0, 0, 0, 1});
		else throw new IllegalArgumentException("Only size 3 and 4 rotationZ matrices are supported");
	}
	
	public static Matrix Translation(Vec3 v) {
		return Matrix.Translation(4, v.x, v.y, v.z);
	}
	
	public static Matrix Translation(int size, float x, float y, float z) {
		if(size == 4) return new Matrix(4, 4, new float[] {
				1, 0, 0, x, 
				0, 1, 0, y, 
				0, 0, 1, z, 
				0, 0, 0, 1});
		else throw new IllegalArgumentException("Only size 4 Translation matrices are supported");
	}
	
	public static Matrix Projection(int size, float w, float h, float n, float f) {
		if(size == 4) return new Matrix(4, 4, new float[] {
			2 * n / w, 0, 0, 0,
			0, 2 * n /h, 0, 0,
			0, 0, f / (f - n), -n * f / (f - n),
			0, 0, 1, 0
		});
		else throw new IllegalArgumentException("Only size 4 Projection matrices are supported");
	}
	
	public static Matrix ProjectionFOV(int size, float fov, float aspectRatio, float n, float f) {
		if(size == 4) {
			double rad = Math.toRadians(fov);
			float w = (float) (1/Math.tan(rad/2));
			float h = w * aspectRatio;
			return new Matrix(4, 4, new float[] {
				w, 0, 0, 0,
				0, h, 0, 0,
				0, 0, f / (f - n), -n * f / (f - n),
				0, 0, 1, 0
			});
		}
		else throw new IllegalArgumentException("Only size 4 Projection matrices are supported");
	}
	
	public Matrix inverse() {
		if(this.rows == 4 && this.cols == 4) {
			    float[] inv = new float[16]; 
			    float[] invOut = new float[16];
			    float det;
			    int i;

			    inv[0] = this.data[5]  * this.data[10] * this.data[15] - 
			             this.data[5]  * this.data[11] * this.data[14] - 
			             this.data[9]  * this.data[6]  * this.data[15] + 
			             this.data[9]  * this.data[7]  * this.data[14] +
			             this.data[13] * this.data[6]  * this.data[11] - 
			             this.data[13] * this.data[7]  * this.data[10];

			    inv[4] = -this.data[4]  * this.data[10] * this.data[15] + 
			              this.data[4]  * this.data[11] * this.data[14] + 
			              this.data[8]  * this.data[6]  * this.data[15] - 
			              this.data[8]  * this.data[7]  * this.data[14] - 
			              this.data[12] * this.data[6]  * this.data[11] + 
			              this.data[12] * this.data[7]  * this.data[10];

			    inv[8] = this.data[4]  * this.data[9] * this.data[15] - 
			             this.data[4]  * this.data[11] * this.data[13] - 
			             this.data[8]  * this.data[5] * this.data[15] + 
			             this.data[8]  * this.data[7] * this.data[13] + 
			             this.data[12] * this.data[5] * this.data[11] - 
			             this.data[12] * this.data[7] * this.data[9];

			    inv[12] = -this.data[4]  * this.data[9] * this.data[14] + 
			               this.data[4]  * this.data[10] * this.data[13] +
			               this.data[8]  * this.data[5] * this.data[14] - 
			               this.data[8]  * this.data[6] * this.data[13] - 
			               this.data[12] * this.data[5] * this.data[10] + 
			               this.data[12] * this.data[6] * this.data[9];

			    inv[1] = -this.data[1]  * this.data[10] * this.data[15] + 
			              this.data[1]  * this.data[11] * this.data[14] + 
			              this.data[9]  * this.data[2] * this.data[15] - 
			              this.data[9]  * this.data[3] * this.data[14] - 
			              this.data[13] * this.data[2] * this.data[11] + 
			              this.data[13] * this.data[3] * this.data[10];

			    inv[5] = this.data[0]  * this.data[10] * this.data[15] - 
			             this.data[0]  * this.data[11] * this.data[14] - 
			             this.data[8]  * this.data[2] * this.data[15] + 
			             this.data[8]  * this.data[3] * this.data[14] + 
			             this.data[12] * this.data[2] * this.data[11] - 
			             this.data[12] * this.data[3] * this.data[10];

			    inv[9] = -this.data[0]  * this.data[9] * this.data[15] + 
			              this.data[0]  * this.data[11] * this.data[13] + 
			              this.data[8]  * this.data[1] * this.data[15] - 
			              this.data[8]  * this.data[3] * this.data[13] - 
			              this.data[12] * this.data[1] * this.data[11] + 
			              this.data[12] * this.data[3] * this.data[9];

			    inv[13] = this.data[0]  * this.data[9] * this.data[14] - 
			              this.data[0]  * this.data[10] * this.data[13] - 
			              this.data[8]  * this.data[1] * this.data[14] + 
			              this.data[8]  * this.data[2] * this.data[13] + 
			              this.data[12] * this.data[1] * this.data[10] - 
			              this.data[12] * this.data[2] * this.data[9];

			    inv[2] = this.data[1]  * this.data[6] * this.data[15] - 
			             this.data[1]  * this.data[7] * this.data[14] - 
			             this.data[5]  * this.data[2] * this.data[15] + 
			             this.data[5]  * this.data[3] * this.data[14] + 
			             this.data[13] * this.data[2] * this.data[7] - 
			             this.data[13] * this.data[3] * this.data[6];

			    inv[6] = -this.data[0]  * this.data[6] * this.data[15] + 
			              this.data[0]  * this.data[7] * this.data[14] + 
			              this.data[4]  * this.data[2] * this.data[15] - 
			              this.data[4]  * this.data[3] * this.data[14] - 
			              this.data[12] * this.data[2] * this.data[7] + 
			              this.data[12] * this.data[3] * this.data[6];

			    inv[10] = this.data[0]  * this.data[5] * this.data[15] - 
			              this.data[0]  * this.data[7] * this.data[13] - 
			              this.data[4]  * this.data[1] * this.data[15] + 
			              this.data[4]  * this.data[3] * this.data[13] + 
			              this.data[12] * this.data[1] * this.data[7] - 
			              this.data[12] * this.data[3] * this.data[5];

			    inv[14] = -this.data[0]  * this.data[5] * this.data[14] + 
			               this.data[0]  * this.data[6] * this.data[13] + 
			               this.data[4]  * this.data[1] * this.data[14] - 
			               this.data[4]  * this.data[2] * this.data[13] - 
			               this.data[12] * this.data[1] * this.data[6] + 
			               this.data[12] * this.data[2] * this.data[5];

			    inv[3] = -this.data[1] * this.data[6] * this.data[11] + 
			              this.data[1] * this.data[7] * this.data[10] + 
			              this.data[5] * this.data[2] * this.data[11] - 
			              this.data[5] * this.data[3] * this.data[10] - 
			              this.data[9] * this.data[2] * this.data[7] + 
			              this.data[9] * this.data[3] * this.data[6];

			    inv[7] = this.data[0] * this.data[6] * this.data[11] - 
			             this.data[0] * this.data[7] * this.data[10] - 
			             this.data[4] * this.data[2] * this.data[11] + 
			             this.data[4] * this.data[3] * this.data[10] + 
			             this.data[8] * this.data[2] * this.data[7] - 
			             this.data[8] * this.data[3] * this.data[6];

			    inv[11] = -this.data[0] * this.data[5] * this.data[11] + 
			               this.data[0] * this.data[7] * this.data[9] + 
			               this.data[4] * this.data[1] * this.data[11] - 
			               this.data[4] * this.data[3] * this.data[9] - 
			               this.data[8] * this.data[1] * this.data[7] + 
			               this.data[8] * this.data[3] * this.data[5];

			    inv[15] = this.data[0] * this.data[5] * this.data[10] - 
			              this.data[0] * this.data[6] * this.data[9] - 
			              this.data[4] * this.data[1] * this.data[10] + 
			              this.data[4] * this.data[2] * this.data[9] + 
			              this.data[8] * this.data[1] * this.data[6] - 
			              this.data[8] * this.data[2] * this.data[5];

			    det = this.data[0] * inv[0] + this.data[1] * inv[4] + this.data[2] * inv[8] + this.data[3] * inv[12];

			    if (det == 0) throw new IllegalStateException("This Matrix does not have an inverse");

			    det = 1.0f / det;

			    for (i = 0; i < 16; i++)
			        invOut[i] = inv[i] * det;
			    
			    return new Matrix(4, 4, invOut);
			} 
		else throw new IllegalStateException("Currently only 4x4 Matrices can be inverted");
	}
	
	public void multiply(Vec3 v) {
		if(this.cols != 3) throw new IllegalStateException("Matrix does not have 3 columns");
		float x = this.data[0]*v.x + this.data[1]*v.y + this.data[2]*v.z;
		float y = this.data[3]*v.x + this.data[4]*v.y + this.data[5]*v.z;
		float z = this.data[6]*v.x + this.data[7]*v.y + this.data[8]*v.z;
		v.x = x;
		v.y = y;
		v.z = z;
	}
	
	public Vec3 _multiply(Vec3 v) {
		if(this.cols != 3) throw new IllegalStateException("Matrix does not have 3 columns");
		return new Vec3(
				this.data[0]*v.x + this.data[1]*v.y + this.data[2]*v.z, 
				this.data[3]*v.x + this.data[4]*v.y + this.data[5]*v.z, 
				this.data[6]*v.x + this.data[7]*v.y + this.data[8]*v.z
				);
	}
	
	public void multiply(Vec4 v) {
		if(this.cols != 4) throw new IllegalStateException("Matrix does not have 4 columns");
		float x = this.data[0]*v.x + this.data[1]*v.y + this.data[2]*v.z + this.data[3]*v.w;
		float y = this.data[4]*v.x + this.data[5]*v.y + this.data[6]*v.z + this.data[7]*v.w;
		float z = this.data[8]*v.x + this.data[9]*v.y + this.data[10]*v.z + this.data[11]*v.w;
		float w = this.data[12]*v.x + this.data[13]*v.y + this.data[14]*v.z + this.data[15]*v.w;
		v.x = x;
		v.y = y;
		v.z = z;
		v.w = w;
	}
	
	public Vec4 _multiply(Vec4 v) {
		if(this.cols != 4) throw new IllegalStateException("Matrix does not have 4 columns");
		return new Vec4(
				this.data[0]*v.x + this.data[1]*v.y + this.data[2]*v.z + this.data[3]*v.w, 
				this.data[4]*v.x + this.data[5]*v.y + this.data[6]*v.z + this.data[7]*v.w, 
				this.data[8]*v.x + this.data[9]*v.y + this.data[10]*v.z + this.data[11]*v.w,
				this.data[12]*v.x + this.data[13]*v.y + this.data[14]*v.z + this.data[15]*v.w
				);
	}

	public void print() {
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				System.out.printf("%f, ", this.data[i * this.cols + j]);
			}
			System.out.println();
		}
	}
	
	public boolean isEqual(Matrix m) {
		if(this.rows != m.rows || this.cols != m.cols) return false;
		for (int i = 0; i < this.data.length; i++) {
			if(this.data[i] != m.data[i]) return false;
		}
		return true;
	}
	
}
