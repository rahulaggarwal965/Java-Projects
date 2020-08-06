package math;

public class Vec2{
	public float x, y;
	
	public static final Vec2 zero = new Vec2(0, 0);
	
	public Vec2(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vec2(Vec2 v) {
		this.x = v.x;
		this.y = v.y;
	}
	
	public void set(Vec2 v) {
		this.x = v.x;
		this.y = v.y;
	}
	
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void abs() {
		this.x = Math.abs(this.x);
		this.y = Math.abs(this.y);
	}
	
	public Vec2 _abs() {
		return new Vec2(Math.abs(this.x), Math.abs(this.y));
	}
	
	public float magSq() {
		return this.x*this.x + this.y*this.y;
	}
	
	public float mag() {
		return (float) Math.sqrt(this.x*this.x + this.y*this.y);
	}
	
	public void normalize() {
		this.divide(this.mag());
	}
	
	public Vec2 getNormalized() {
		return this._divide(this.mag());
	}
	
	public float dot(Vec2 v) {
		return this.x * v.x + this.y * v.y;
	}
	
	public void negate() {
		this.x = -this.x;
		this.y = -this.y;
	}
	
	public Vec2 _negate() {
		return new Vec2(-this.x, -this.y);
	}
	
	public void multiply(Vec2 v) {
		this.x *= v.x;
		this.y *= v.y;
	}
	
	public Vec2 _multiply(Vec2 v) {
		return new Vec2(this.x*v.x, this.y*v.y);
	}
	
	public void multiply(float f) {
		this.x *= f;
		this.y *= f;
	}
	
	public Vec2 _multiply(float f) {
		return new Vec2(this.x * f, this.y * f);
	}
	
	public void divide(Vec2 v) {
		this.x /= v.x;
		this.y /= v.y;
	}
	
	public Vec2 _divide(Vec2 v) {
		return new Vec2(this.x/v.x, this.y/v.y);
	}
	
	public void divide(float f) {
		this.x /= f;
		this.y /= f;
	}
	
	public Vec2 _divide(float f) {
		return new Vec2(this.x/f, this.y/f);
	}
	
	public void add(Vec2 v) {
		this.x += v.x;
		this.y += v.y;
	}
	
	public Vec2 _add(Vec2 v) {
		return new Vec2(this.x + v.x, this.y + v.y);
	}
	
	public void add(float f) {
		this.x += f;
		this.y += f;
	}
	
	public Vec2 _add(float f) {
		return new Vec2(this.x + f, this.y + f);
	}
	
	public void subtract(Vec2 v) {
		this.x -= v.x;
		this.y -= v.y;
	}
	
	public Vec2 _subtract(Vec2 v) {
		return new Vec2(this.x - v.x, this.y - v.y);
	}
	
	public void subtract(float f) {
		this.x -= f;
		this.y -= f;
	}
	
	public Vec2 _subtract(float f) {
		return new Vec2(this.x - f, this.y - f);
	}
	
	public void mod(float f) {
		this.x %= f;
		this.y %= f;
	}
	
	public Vec2 _mod(float f) {
		return new Vec2(this.x % f, this.y % f);
	}
	
	public void max(float f) {
		this.x = Math.max(this.x, f);
		this.y = Math.max(this.y, f);
	}
	
	public Vec2 _max(float f) {
		return new Vec2(Math.max(this.x, f), Math.max(this.y, f));
	}
	
	public void min(float f) {
		this.x = Math.min(this.x, f);
		this.y = Math.min(this.y, f);
	}
	
	public Vec2 _min(float f) {
		return new Vec2(Math.min(this.x, f), Math.min(this.y, f));
	}
	
	public void clamp(float l, float h) {
		this.x = Maths.clamp(this.x, l, h);
		this.y = Maths.clamp(this.y, l, h);
	}

	public Vec2 _clamp(float l, float h) {
		return new Vec2(
				Maths.clamp(this.x, l, h),
				Maths.clamp(this.y, l, h));
	}
	
	public Vec2 interpolateTo(Vec2 dest, float alpha) {
		return this._add(dest._subtract(this)._multiply(alpha));
	}
	
	public boolean equals(Vec2 v) {
		return (this.x == v.x && this.y == v.y);
	}
	
	public void print() {
		System.out.printf("x: %f, y: %f\n", this.x, this.y);
	}
	
	public static Vec2 random(float f) {
		return new Vec2(Maths.randomBilateral(f), Maths.randomBilateral(f));
	}
	
	public static Vec2 random(float x, float y) {
		return new Vec2(Maths.randomBilateral(x), Maths.randomBilateral(y));
	}
}
