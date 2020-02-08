package threeDimensions;

import java.util.Arrays;

import math.Maths;

public class Vertex {

	public Vec4 position;
	public float[] data;
	
	public Vertex(Vec4 position, float ... data) {
		this.position = position;
		this.data = data;
	}
	
	public Vertex(Vec4 position, int length) {
		this.position = position;
		this.data = new float[length]; 
	}

	public Vertex(Vertex v) {
		this.position = new Vec4(v.position);
		this.data = new float[v.data.length];
		for (int i = 0; i < this.data.length; i++) {
			this.data[i] = v.data[i];
		}
	}
	
	public Vertex(Vertex v, float ... data) {
		this(new Vec4(v.position), v.data.length + data.length);
		for (int i = 0; i < v.data.length; i++) {
			this.data[i] = v.data[i];
		}
		for(int i = 0; i < data.length; i++) {
			this.data[i + v.data.length] = data[i];
		}
	}
	
	public void abs() {
		this.position.abs();
		for (int i = 0; i < this.data.length; i++) {
			this.data[i] = Math.abs(this.data[i]);
		}
	}
	
	public Vertex _abs() {
		Vertex v = new Vertex(this.position._abs(), this.data.length);
		for (int i = 0; i < this.data.length; i++) {
			v.data[i] = Math.abs(this.data[i]);
		}
		return v;
	}
	
	public void negate() {
		this.position.negate();
		for (int i = 0; i < this.data.length; i++) {
			this.data[i] = -this.data[i];
		}
	}
	
	public Vertex _negate() {
		Vertex v = new Vertex(this.position._negate(), this.data.length);
		for (int i = 0; i < this.data.length; i++) {
			v.data[i] = -this.data[i];
		}
		return v;
	}
	
	public void multiply(Vertex v) {
		//if(this.data.length != v.data.length) throw new IllegalArgumentException("RefactoredVertex Lengths must match");
		this.position.multiply(v.position);
		for (int i = 0; i < this.data.length; i++) {
			this.data[i] *= v.data[i];
		}
	}
	
	public Vertex _multiply(Vertex v) {
		//if(this.data.length != v.data.length) throw new IllegalArgumentException("RefactoredVertex Lengths must match");
		Vertex r = new Vertex(this.position._multiply(v.position), this.data.length);
		for (int i = 0; i < this.data.length; i++) {
			r.data[i] = this.data[i] * v.data[i];
		}
		return r;
	}
	
	public void multiply(float f) {
		this.position.multiply(f);
		for (int i = 0; i < this.data.length; i++) {
			this.data[i] *= f;
		}
	}
	
	public Vertex _multiply(float f) {
		Vertex v = new Vertex(this.position._multiply(f), this.data.length);
		for (int i = 0; i < this.data.length; i++) {
			v.data[i] = this.data[i] * f;
		}
		return v;
	}
	
	public void divide(Vertex v) {
		//if(this.data.length != v.data.length) throw new IllegalArgumentException("RefactoredVertex Lengths must match");
		this.position.divide(v.position);
		for (int i = 0; i < this.data.length; i++) {
			this.data[i] /= v.data[i];
		}
	}
	
	public Vertex _divide(Vertex v) {
		//if(this.data.length != v.data.length) throw new IllegalArgumentException("RefactoredVertex Lengths must match");
		Vertex r = new Vertex(this.position._divide(v.position), this.data.length);
		for (int i = 0; i < this.data.length; i++) {
			r.data[i] = this.data[i] / v.data[i];
		}
		return r;
	}
	
	public void divide(float f) {
		this.position.divide(f);
		for (int i = 0; i < this.data.length; i++) {
			this.data[i] /= f;
		}
	}
	
	public Vertex _divide(float f) {
		Vertex v = new Vertex(this.position._divide(f), this.data.length);
		for (int i = 0; i < this.data.length; i++) {
			v.data[i] = this.data[i]/ f;
		}
		return v;
	}
	
	public void add(Vertex v) {
		//if(this.data.length != v.data.length) throw new IllegalArgumentException("RefactoredVertex Lengths must match");
		this.position.add(v.position);
		for (int i = 0; i < this.data.length; i++) {
			this.data[i] += v.data[i];
		}
	}
	
	public Vertex _add(Vertex v) {
		//if(this.data.length != v.data.length) throw new IllegalArgumentException("RefactoredVertex Lengths must match");
		Vertex r = new Vertex(this.position._add(v.position), this.data.length);
		for (int i = 0; i < this.data.length; i++) {
			r.data[i] = this.data[i] + v.data[i];
		}
		return r;
	}
	
	public void add(float f) {
		this.position.add(f);
		for (int i = 0; i < this.data.length; i++) {
			this.data[i] += f;
		}
	}
	
	public Vertex _add(float f) {
		Vertex v = new Vertex(this.position._add(f), this.data.length);
		for (int i = 0; i < this.data.length; i++) {
			v.data[i] = this.data[i] + f;
		}
		return v;
	}
	
	public void subtract(Vertex v) {
		//if(this.data.length != v.data.length) throw new IllegalArgumentException("RefactoredVertex Lengths must match");
		this.position.subtract(v.position);
		for (int i = 0; i < this.data.length; i++) {
			this.data[i] -= v.data[i];
		}
	}
	
	public Vertex _subtract(Vertex v) {
		//if(this.data.length != v.data.length) throw new IllegalArgumentException("RefactoredVertex Lengths must match");
		Vertex r = new Vertex(this.position._subtract(v.position), this.data.length);
		for (int i = 0; i < this.data.length; i++) {
			r.data[i] = this.data[i] - v.data[i];
		}
		return r;
	}
	
	public void subtract(float f) {
		this.position.subtract(f);
		for (int i = 0; i < this.data.length; i++) {
			this.data[i] -= f;
		}
	}
	
	public Vertex _subtract(float f) {
		Vertex v = new Vertex(this.position._subtract(f), this.data.length);
		for (int i = 0; i < this.data.length; i++) {
			v.data[i] = this.data[i] - f;
		}
		return v;
	}
	
	public void max(float f) {
		this.position._max(f);
		for (int i = 0; i < this.data.length; i++) {
			this.data[i] = Math.max(this.data[i], f);
		}
	}
	
	public Vertex _max(float f) {
		Vertex v = new Vertex(this.position._max(f), this.data.length);
		for (int i = 0; i < this.data.length; i++) {
			v.data[i] = Math.max(this.data[i], f);
		}
		return v;
	}
	
	public void min(float f) {
		this.position.min(f);
		for (int i = 0; i < this.data.length; i++) {
			this.data[i] = Math.min(this.data[i], f);
		}
	}
	
	public Vertex _min(float f) {
		Vertex v = new Vertex(this.position._min(f), this.data.length);
		for (int i = 0; i < this.data.length; i++) {
			v.data[i] = Math.min(this.data[i], f);
		}
		return v;
	}
	
	public void clamp(float l, float h) {
		this.position._clamp(l, h);
		for (int i = 0; i < this.data.length; i++) {
			this.data[i] = Maths.clamp(this.data[i], l, h);
		}
	}
	
	public Vertex _clamp(float l, float h) {
		Vertex v = new Vertex(this.position._clamp(l, h), this.data.length);
		for (int i = 0; i < this.data.length; i++) {
			v.data[i] = Maths.clamp(this.data[i], l, h);
		}
		return v;
	}
	
	public Vertex interpolateTo(Vertex v, float a) {
		//if(this.data.length != v.data.length) throw new IllegalArgumentException("RefactoredVertex Lengths must Match");
		Vertex r = new Vertex(this.position.interpolateTo(v.position, a), this.data.length);
		for (int i = 0; i < this.data.length; i++) {
			r.data[i] = Maths.interpolate(this.data[i], v.data[i], a);
		}
		return r;
	}
	
	public boolean equals(Vertex v) {
		if(!this.position.equals(v.position)) return false;
		if(this.data.length != v.data.length) return false;
		else {
			for (int i = 0; i < this.data.length; i++) {
				if(this.data[i] != v.data[i]) return false;
			}
			return true;
		}
	}
	
	public void print() {
		System.out.printf("Position: %s\nData: %s\n", this.position.toString(), Arrays.toString(this.data));
	}


}
