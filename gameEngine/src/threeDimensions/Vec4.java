package threeDimensions;

import math.Maths;

public class Vec4 extends Vec3 {
	
	public float w;

	public Vec4(float x, float y, float z, float w) {
		super(x, y, z);
		this.w = w;
	}

	public Vec4(Vec4 v) {
		this(v.x, v.y, v.z, v.w);
	}
	
	public Vec4(Vec3 v, float w) {
		super(v);
		this.w = w;
	}
	
	public void set(Vec4 v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
		this.w = v.w;
	}
	
	public void set(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public void abs() {
		this.x = Math.abs(this.x);
		this.y = Math.abs(this.y);
		this.z = Math.abs(this.z);
		this.w = Math.abs(this.w);
	}
	
	public Vec4 _abs() {
		return new Vec4(Math.abs(this.x), Math.abs(this.y), Math.abs(this.z), Math.abs(this.w));
	}
	
	public float magSq() {
		return this.x*this.x + this.y*this.y + this.z*this.z + this.w*this.w;
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
	
	public float dot(Vec4 v) {
		return this.x * v.x + this.y * v.y + this.z*v.z + this.w*this.w;
	}
	
	public void negate() {
		this.x = -this.x;
		this.y = -this.y;
		this.z = -this.z;
		this.w = -this.w;
	}
	
	public Vec4 _negate() {
		return new Vec4(-this.x, -this.y, -this.z, -this.w);
	}
	
	public void multiply(Vec4 v) {
		this.x *= v.x;
		this.y *= v.y;
		this.z *= v.z;
		this.w *= v.w;
	}
	
	public Vec4 _multiply(Vec4 v) {
		return new Vec4(this.x*v.x, this.y*v.y, this.z*v.z, this.w*v.w);
	}
	
	public void multiply(float f) {
		this.x *= f;
		this.y *= f;
		this.z *= f;
		this.w *= f;
	}
	
	public Vec4 _multiply(float f) {
		return new Vec4(this.x*f, this.y*f, this.z*f, this.w*f);
	}
	
	public void divide(Vec4 v) {
		this.x /= v.x;
		this.y /= v.y;
		this.z /= v.z;
		this.w /= v.w;
	}
	
	public Vec4 _divide(Vec4 v) {
		return new Vec4(this.x/v.x, this.y/v.y, this.z/v.z, this.w/v.w);
	}
	
	public void divide(float f) {
		this.x /= f;
		this.y /= f;
		this.z /= f;
		this.w /= f;
	}
	
	public Vec4 _divide(float f) {
		return new Vec4(this.x/f, this.y/f, this.z/f, this.w/f);
	}
	
	public void add(Vec4 v) {
		this.x += v.x;
		this.y += v.y;
		this.z += v.z;
		this.w += v.w;
	}
	
	public Vec4 _add(Vec4 v) {
		return new Vec4(this.x+v.x, this.y+v.y, this.z+v.z, this.w+v.w);
	}
	
	public void add(float f) {
		this.x += f;
		this.y += f;
		this.z += f;
		this.w += f;
	}
	
	public Vec4 _add(float f) {
		return new Vec4(this.x+f, this.y+f, this.z+f, this.w+f);
	}
	
	public void subtract(Vec4 v) {
		this.x -= v.x;
		this.y -= v.y;
		this.z -= v.z;
		this.w -= v.w;
	}
	
	public Vec4 _subtract(Vec4 v) {
		return new Vec4(this.x-v.x, this.y-v.y, this.z-v.z, this.w-v.w);
	}
	
	public void subtract(float f) {
		this.x -= f;
		this.y -= f;
		this.z -= f;
		this.w -= f;
	}
	
	public Vec4 _subtract(float f) {
		return new Vec4(this.x-f, this.y-f, this.z-f, this.w-f);
	}
	
	public void max(float f) {
		this.x = Math.max(this.x, f);
		this.y = Math.max(this.y, f);
		this.z = Math.max(this.z, f);
		this.w = Math.max(this.w, f);
	}
	
	public Vec4 _max(float f) {
		return new Vec4(Math.max(this.x, f), Math.max(this.y, f), Math.max(this.z, f), Math.max(this.w, f));
	}
	
	public void min(float f) {
		this.x = Math.min(this.x, f);
		this.y = Math.min(this.y, f);
		this.z = Math.min(this.z, f);
		this.w = Math.min(this.w, f);
	}
	
	public Vec4 _min(float f) {
		return new Vec4(Math.min(this.x, f), Math.min(this.y, f), Math.min(this.z, f), Math.min(this.w, f));
	}
	
	public void clamp(float l, float h) {
		this.x = Maths.clamp(this.x, l, h);
		this.y = Maths.clamp(this.y, l, h);
		this.z = Maths.clamp(this.z, l, h);
		this.w = Maths.clamp(this.w, l, h);
	}
	
	public Vec4 _clamp(float l, float h) {
		return new Vec4(
				Maths.clamp(this.x, l, h),
				Maths.clamp(this.y, l, h),
				Maths.clamp(this.z, l, h),
				Maths.clamp(this.w, l, h)
				);
	}
	
	public Vec4 interpolateTo(Vec4 dest, float alpha) {
		return this._add(dest._subtract(this)._multiply(alpha));
	}
	
	public boolean equals(Vec4 v) {
		return (this.x == v.x && this.y == v.y && this.z == v.z && this.w == v.w);
	}
	
	public void print() {
		System.out.printf("x: %f, y: %f, z: %f, w: %f\n", this.x, this.y, this.z, this.w);
	}
}
