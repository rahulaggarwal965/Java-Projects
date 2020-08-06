package math;

public class Vec3 extends Vec2 {
	
	public float z;
	
	public static final Vec3 zero = new Vec3(0, 0, 0);

	public Vec3(float x, float y, float z) {
		super(x, y);
		this.z = z;
	}
	
	public Vec3(Vec3 v) {
		this(v.x, v.y, v.z);
	}
	
	public Vec3(Vec2 v, float z) {
		super(v);
		this.z = z;
	}
	
	public void set(Vec3 v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}
	
	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void abs() {
		this.x = Math.abs(this.x);
		this.y = Math.abs(this.y);
		this.z = Math.abs(this.z);
	}
	
	public Vec3 _abs() {
		return new Vec3(Math.abs(this.x), Math.abs(this.y), Math.abs(this.z));
	}
	
	public float magSq() {
		return this.x*this.x + this.y*this.y + this.z*this.z;
	}
	
	public float mag() {
		return (float) Math.sqrt(this.magSq());
	}
	
	public void normalize() {
		this.divide(this.mag());
	}
	
	public Vec3 getNormalized() {
		return this._divide(this.mag());
	}
	
	public float dot(Vec3 v) {
		return this.x * v.x + this.y * v.y + this.z*v.z;
	}
	
	public Vec3 cross(Vec3 v ) {
		return new Vec3(
				this.y * v.z - this.z * v.y,
				this.z * v.x - this.x * v.z,
				this.x * v.y - this.y * v.x
				);
	}
	
	public void negate() {
		this.x = -this.x;
		this.y = -this.y;
		this.z = -this.z;
	}
	
	public Vec3 _negate() {
		return new Vec3(-this.x, -this.y, -this.z);
	}
	
	public void multiply(Vec3 v) {
		this.x *= v.x;
		this.y *= v.y;
		this.z *= v.z;
	}
	
	public Vec3 _multiply(Vec3 v) {
		return new Vec3(this.x*v.x, this.y*v.y, this.z*v.z);
	}
	
	public void multiply(float f) {
		this.x *= f;
		this.y *= f;
		this.z *= f;
	}
	
	public Vec3 _multiply(float f) {
		return new Vec3(this.x*f, this.y*f, this.z*f);
	}
	
	public void divide(Vec3 v) {
		this.x /= v.x;
		this.y /= v.y;
		this.z /= v.z;
	}
	
	public Vec3 _divide(Vec3 v) {
		return new Vec3(this.x/v.x, this.y/v.y, this.z/v.z);
	}
	
	public void divide(float f) {
		this.x /= f;
		this.y /= f;
		this.z /= f;
	}
	
	public Vec3 _divide(float f) {
		return new Vec3(this.x/f, this.y/f, this.z/f);
	}
	
	public void add(Vec3 v) {
		this.x += v.x;
		this.y += v.y;
		this.z += v.z;
	}
	
	public Vec3 _add(Vec3 v) {
		return new Vec3(this.x+v.x, this.y+v.y, this.z+v.z);
	}
	
	public void add(float f) {
		this.x += f;
		this.y += f;
		this.z += f;
	}
	
	public Vec3 _add(float f) {
		return new Vec3(this.x+f, this.y+f, this.z+f);
	}
	
	public void subtract(Vec3 v) {
		this.x -= v.x;
		this.y -= v.y;
		this.z -= v.z;
	}
	
	public Vec3 _subtract(Vec3 v) {
		return new Vec3(this.x-v.x, this.y-v.y, this.z-v.z);
	}
	
	public void subtract(float f) {
		this.x -= f;
		this.y -= f;
		this.z -= f;
	}
	
	public Vec3 _subtract(float f) {
		return new Vec3(this.x-f, this.y-f, this.z-f);
	}
	
	public void mod(float f) {
		this.x %= f;
		this.y %= f;
		this.z %= f;
	}
	
	public Vec3 _mod(float f) {
		return new Vec3(this.x % f, this.y % f, this.z % f);
	}
	
	
	public void max(float f) {
		this.x = Math.max(this.x, f);
		this.y = Math.max(this.y, f);
		this.z = Math.max(this.z, f);
	}
	
	public Vec3 _max(float f) {
		return new Vec3(Math.max(this.x, f), Math.max(this.y, f), Math.max(this.z, f));
	}
	
	public void min(float f) {
		this.x = Math.min(this.x, f);
		this.y = Math.min(this.y, f);
		this.z = Math.min(this.z, f);
	}
	
	public Vec3 _min(float f) {
		return new Vec3(Math.min(this.x, f), Math.min(this.y, f), Math.min(this.z, f));
	}
	
	public void clamp(float l, float h) {
		this.x = Maths.clamp(this.x, l, h);
		this.y = Maths.clamp(this.y, l, h);
		this.z = Maths.clamp(this.z, l, h);
	}
	
	public Vec3 _clamp(float l, float h) {
		return new Vec3(
				Maths.clamp(this.x, l, h),
				Maths.clamp(this.y, l, h),
				Maths.clamp(this.z, l, h)
				);
	}
	
	public Vec3 interpolateTo(Vec3 dest, float alpha) {
		return this._add(dest._subtract(this)._multiply(alpha));
	}
	
	public boolean equals(Vec3 v) {
		return (this.x == v.x && this.y == v.y && this.z == v.z);
	}
	
	public void print() {
		System.out.printf("x: %f, y: %f, z: %f\n", this.x, this.y, this.z);
	}
	
	public static Vec3 random(float f) {
		return new Vec3(Maths.randomBilateral(f), Maths.randomBilateral(f), Maths.randomBilateral(f));
	}
	
	public static Vec3 random(float x, float y, float z) {
		return new Vec3(Maths.randomBilateral(x), Maths.randomBilateral(y), Maths.randomBilateral(z));
	}

}
