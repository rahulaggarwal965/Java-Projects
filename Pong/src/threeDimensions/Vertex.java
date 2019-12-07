package threeDimensions;

public class Vertex {
	public Vec3 pos;
	
	public Vertex() {};
	
	public Vertex(Vec3 pos) {
		this.pos = pos;
	}
	
	public Vertex(Vertex v) {
		this.pos = new Vec3(v.pos);
	}
	
	public void add(Vertex v) {
		this.pos.add(v.pos);
	}
	
	public Vertex _add(Vertex v) {
		return new Vertex(pos._add(v.pos));
	}
	
	public void subtract(Vertex v) {
		this.pos.subtract(v.pos);
	}
	
	public Vertex _subtract(Vertex v) {
		return new Vertex(pos._subtract(v.pos));
	}
	
	public void multiply(float f) {
		this.pos.multiply(f);
	}
	
	public Vertex _multiply(float f) {
		return new Vertex(pos._multiply(f));
	}
	
	public void divide(float f) {
		this.pos.divide(f);
	}
	
	public Vertex _divide(float f) {
		return new Vertex(pos._divide(f));
	}
	
	public Vertex interpolateTo(Vertex dest, float alpha) {
		return new Vertex(pos.interpolateTo(dest.pos, alpha));
	}
}
