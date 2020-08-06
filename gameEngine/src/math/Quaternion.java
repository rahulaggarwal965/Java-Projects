package math;

import threeDimensions.Camera;

public class Quaternion {
	
	private float x, y, z, w; //Vector (x, y, z) //Scalar w
	
	public Quaternion(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Quaternion(Quaternion q) {
		this.x = q.x;
		this.y = q.y;
		this.z = q.z;
		this.w = q.w;
	}
	
	public Quaternion(Vec3 v, float w) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
		this.w = w;
	}
	
	public void set(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public void set(Quaternion q) {
		this.set(q.x, q.y, q.z, q.w);
	}
	
	public Vec3 getVector() {
		return new Vec3(this.x, this.y, this.z);
	}
	
	public float getScalar() {
		return this.w;
	}
	
	public float getAngle() {
		Quaternion unit = this.getNormalized();
		float vMag = Maths.sqrt(unit.x * unit.x + unit.y * unit.y + unit.z * unit.z);
		return 2 * Maths.atan2(vMag, unit.w);
	}
	
	public Vec3 getRotationAxis() {
		float angle = this.getAngle();
		float mag = this.mag();
		
		Vec3 axis = new Vec3(0, 0, 0);
		if(Math.abs(angle) > Maths.EPSILON) {//make sure its not 0
			float sin = (float) Math.sin(angle / 2.0f);
			axis.x = this.x / (mag * sin);
			axis.y = this.y / (mag * sin);
			axis.z = this.z / (mag * sin);
		}
		
		return axis;
	}
	
	public static Quaternion Identity() {
		return new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);
	}
	
	public static Quaternion AxisAngle(Vec3 axis, float angle) {
		float mag = axis.mag();
		if(mag < Maths.EPSILON) {
			return Quaternion.Identity();
		}
		
		Vec3 axisNorm = axis._divide(mag);
		float halfAngle = angle / 2.0f;
		axisNorm._multiply((float) Math.sin(halfAngle));
		return new Quaternion(axisNorm, (float) Math.cos(halfAngle));
	}
	
	public static Quaternion EulerAngles(Vec3 eulerAngles) {
		return Quaternion.EulerAngles(eulerAngles.x, eulerAngles.y, eulerAngles.z);
	}
	
	public static Quaternion EulerAngles(float yaw, float roll, float pitch) {
		float angle;
        float sinRoll, sinPitch, sinYaw, cosRoll, cosPitch, cosYaw;
        angle = pitch * 0.5f;
        sinPitch = (float) Math.sin(angle);
        cosPitch = (float) Math.cos(angle);
        angle = roll * 0.5f;
        sinRoll = (float) Math.sin(angle);
        cosRoll = (float) Math.cos(angle);
        angle = yaw * 0.5f;
        sinYaw = (float) Math.sin(angle);
        cosYaw = (float) Math.cos(angle);
        
        float cosRollXcosPitch = cosRoll * cosPitch;
        float sinRollXsinPitch = sinRoll * sinPitch;
        float cosRollXsinPitch = cosRoll * sinPitch;
        float sinRollXcosPitch = sinRoll * cosPitch;
        
        Quaternion q = new Quaternion(
        		cosRollXcosPitch * sinYaw + sinRollXsinPitch * cosYaw,
        		sinRollXcosPitch * cosYaw + cosRollXsinPitch * sinYaw,
        		cosRollXsinPitch * cosYaw - sinRollXcosPitch * sinYaw,
        		cosRollXcosPitch * cosYaw - sinRollXsinPitch * sinYaw
        		);
        q.normalize();
        return q;
	}
	
	public boolean isIdentity() {
		return Math.abs(this.magSq() - 1.0) < Maths.EPSILON && Math.abs(this.w - 1.0) < Maths.EPSILON;
	}
	
	public boolean isUnit() {
		return Math.abs(this.mag() - 1.0) < Maths.EPSILON;
	}
	
	public boolean equals(Quaternion q, float threshold) {
		if(q == null) return false;
		
		return Math.abs(q.x - this.x) < threshold &&
				Math.abs(q.y - this.y) < threshold &&
				Math.abs(q.z - this.z) < threshold &&
				Math.abs(q.w - this.w) < threshold;
	}
	
	public float magSq() {
		return this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
	}
	
	public float mag() {
		return Maths.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w);
	}
	
	public void normalize() {
		float mag = this.mag();
		this.x /= mag;
		this.y /= mag;
		this.z /= mag;
		this.w /= mag;
	}
	
	public Quaternion getNormalized() {
		float mag = this.mag();
		return new Quaternion(this.x / mag, this.y / mag, this.z / mag, this.w / mag);
	}
	
	public void conjugate() {
		this.x = -this.x;
		this.y = -this.y;
		this.z = -this.z;
	}
	
	public Quaternion _conjugate() {
		return new Quaternion(-this.x, this.y, -this.z, this.w);
	}
	
	public void add(Quaternion q) {
		this.x += q.x;
		this.y += q.y;
		this.z += q.z;
		this.w += q.w;
	}
	
	public Quaternion _add(Quaternion q) {
		return new Quaternion(this.x + q.x, this.y + q.y, this.z + q.z, this.w + q.w);
	}
	
	public void multiply(float f) {
		this.x *= f;
		this.y *= f;
		this.z *= f;
		this.w *= f;
	}
	
	public Quaternion _multiply(float f) {
		return new Quaternion(this.x * f, this.y * f, this.z * f, this.w * f);
	}
	
	public void multiply(Quaternion q) {
		float w = q.w * this.w - q.x * this.x - q.y * this.y - q.z * this.z;
		float x = q.w * this.x + q.x * this.w - q.y * this.z + q.z * this.y;
		float y = q.w * this.y + q.x * this.z + q.y * this.w - q.z * this.x;
		float z = q.w * this.z - q.x * this.y + q.y * this.x + q.z * this.w;
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Quaternion _multiply(Quaternion q) {
		return new Quaternion(	
				q.w * this.x + q.x * this.w - q.y * this.z + q.z * this.y,
				q.w * this.y + q.x * this.z + q.y * this.w - q.z * this.x,
				q.w * this.z - q.x * this.y + q.y * this.x + q.z * this.w,
				q.w * this.w - q.x * this.x - q.y * this.y - q.z * this.z
				);
	}
	
	public void inverse() {
		float invMagSq = 1.0f / this.magSq();
		this.x *= -invMagSq;
		this.y *= -invMagSq;
		this.z *= -invMagSq;
		this.w *= invMagSq;
	}
	
	public Quaternion _inverse() {
		float invMagSq = 1.0f / this.magSq();
		return new Quaternion(this.x * -invMagSq, this.y * -invMagSq, this.z * -invMagSq, this.w * invMagSq);
	}
	
	public void divide(Quaternion q) {
		this.multiply(q._inverse());
	}
	
	public Quaternion _divide(Quaternion q) {
		Quaternion r = new Quaternion(this);
		r.divide(q);
		return r;
	}
	
	public Quaternion exp() {
		Vec3 v = this.getVector();
		float mag = v.mag();
		
		if(mag < Maths.EPSILON) {
			return new Quaternion(0, 0, 0, (float) Math.exp(this.w));
		}
		
		v.multiply((float) (Math.sin(mag) / mag));
		
		Quaternion r = new Quaternion(v, (float) Math.cos(mag));
		r.multiply((float) Math.exp(this.w));
		return r;
	}
	
	public Matrix getRotationMatrix(int size) {
		if(size == 3) {
			this.normalize();
			float xx = x * x;
			float yy = y * y;
			float zz = z * z;
			float xy = x * y;
			float xz = x * z;
			float yz = y * z;
			float wx = w * x;
			float wy = w * y;
			float wz = w * z;
			
			return new Matrix(3, 3, 
					1.0f - 2 * (yy + zz), 2 * (xy - wz), 2 * (xz + wy),
		      		2 * (xy + wz), 1.0f - 2 * (xx + zz), 2 * (yz - wx),
		      		2 * (xz - wy), 2 * (yz + wx), 1.0f - 2 * (xx + yy));
		} else if(size == 4) {
			this.normalize();
			float xx = x * x;
			float yy = y * y;
			float zz = z * z;
			float xy = x * y;
			float xz = x * z;
			float yz = y * z;
			float wx = w * x;
			float wy = w * y;
			float wz = w * z;
			
			return new Matrix(4, 4, 
					1.0f - 2 * (yy + zz), 2 * (xy - wz), 2 * (xz + wy), 0, 
					2 * (xy + wz), 1.0f - 2 * (xx + zz), 2 * (yz - wx), 0, 
					2 * (xz - wy), 2 * (yz + wx), 1.0f - 2 * (xx + yy), 0, 
		      		0, 0, 0, 1);
		} else throw new IllegalArgumentException("Only Rotation Matrices of Size 3 and 4 are supported");
	}
	
	public static Quaternion interpolate(Quaternion a, Quaternion b, float t) {
		return a._multiply(1 - t)._add(b._multiply(t));
	}
	
	public static Quaternion LookAt(Vec3 src, Vec3 dst) {
		Vec3 f = src._subtract(dst);
		f.normalize();
		
		float dot = Camera.worldForward.dot(f);
		
		if(Math.abs(dot + 1.0f) < Maths.EPSILON) {
			return new Quaternion(Camera.worldUp.x, Camera.worldUp.y, Camera.worldUp.z, 3.1415926535897932f);
		}
		
		if(Math.abs(dot - 1.0f) < Maths.EPSILON) {
			return Quaternion.Identity();
		}
		
		float rotAngle = (float) Math.acos(dot);
		Vec3 axis = f.cross(Camera.worldForward);
		axis.normalize();
		return Quaternion.AxisAngle(axis, rotAngle);
	}
	
	
	
	
	
	
}
