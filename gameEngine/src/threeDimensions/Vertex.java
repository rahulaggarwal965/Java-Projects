package threeDimensions;

public class Vertex {
	public Vec3 pos;
	public Vec2 t;
	
	public Vertex() {};
	
	public Vertex(Vec3 pos) {
		this.pos = pos;
	}
	
	public Vertex(Vec3 pos, Vertex v) {
		this.pos = pos;
		this.t = new Vec2(v.t);
	}
	
	public Vertex(Vec3 pos, Vec2 t) {
		this.pos = pos;
		this.t = t;
	}
	
	public Vertex(Vertex v) {
		this.pos = new Vec3(v.pos);
		this.t = new Vec2(v.t);
	}
	
	public void add(Vertex v) {
		this.pos.add(v.pos);
		this.t.add(v.t);
	}
	
	public Vertex _add(Vertex v) {
		return new Vertex(pos._add(v.pos), t._add(v.t));
	}
	
	public void subtract(Vertex v) {
		this.pos.subtract(v.pos);
		this.t.subtract(v.t);
	}
	
	public Vertex _subtract(Vertex v) {
		return new Vertex(pos._subtract(v.pos), t._subtract(v.t));
	}
	
	public void multiply(float f) {
		this.pos.multiply(f);
		this.t.multiply(f);
	}
	
	public Vertex _multiply(float f) {
		return new Vertex(pos._multiply(f), t._multiply(f));
	}
	
	public void divide(float f) {
		this.pos.divide(f);
		this.t.divide(f);
	}
	
	public Vertex _divide(float f) {
		return new Vertex(pos._divide(f), t._divide(f));
	}
	
	public Vertex interpolateTo(Vertex dest, float alpha) {
		return new Vertex(pos.interpolateTo(dest.pos, alpha), t.interpolateTo(dest.t, alpha));
	}
}
