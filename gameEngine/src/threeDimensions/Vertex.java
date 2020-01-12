package threeDimensions;

public class Vertex {
	public Vec3 pos;
	public Vec2 t;
	
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
	
}
