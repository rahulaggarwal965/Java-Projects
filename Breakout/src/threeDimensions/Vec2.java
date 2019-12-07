package threeDimensions;

public class Vec2 {
	public float x, y;
	
	public Vec2(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vec2(Vec2 v) {
		this.x = v.x;
		this.y = v.y;
	}
	
	public void abs() {
		this.x = Math.abs(this.x);
		this.y = Math.abs(this.y);
	}
	
	public Vec2 _abs() {
		return new Vec2(Math.abs(this.x), Math.abs(this.y));
	}
	
	public float lenSq() {
		return this.x*this.x + this.y*this.y;
	}
	
	public float len() {
		return (float) Math.sqrt(this.x*this.x + this.y*this.y);
	}
	
	public void normalize() {
		this.divide(this.len());
	}
	
	public Vec2 getNormalized() {
		float l = this.len();
		return new Vec2(this.x/l, this.y/l);
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
	
	public boolean isEqual(Vec2 v) {
		return (this.x == v.x && this.y == v.y);
	}
	
	public Vec2 interpolateTo(Vec2 dest, float alpha) {
		return this._add(dest._subtract(this)._multiply(alpha));
	}
	
	public void copy(Vec2 v) {
		this.x = v.x;
		this.y = v.y;
	}
}
