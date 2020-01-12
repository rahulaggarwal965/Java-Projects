package threeDimensions;

public class VertexOut {

	public Vec4 pos;
	public Vec2 t;
	
	public VertexOut(Vec4 pos) {
		this.pos = pos;
	}
	
	public VertexOut(Vec4 pos, VertexOut v) {
		this.pos = pos;
		this.t = new Vec2(v.t);
	}
	
	public VertexOut(Vec4 pos, Vec2 t) {
		this.pos = pos;
		this.t = t;
	}
	
	public VertexOut(VertexOut v) {
		this.pos = new Vec4(v.pos);
		this.t = new Vec2(v.t);
	}
	
	public void add(VertexOut v) {
		this.pos.add(v.pos);
		this.t.add(v.t);
	}
	
	public VertexOut _add(VertexOut v) {
		return new VertexOut(pos._add(v.pos), t._add(v.t));
	}
	
	public void subtract(VertexOut v) {
		this.pos.subtract(v.pos);
		this.t.subtract(v.t);
	}
	
	public VertexOut _subtract(VertexOut v) {
		return new VertexOut(pos._subtract(v.pos), t._subtract(v.t));
	}
	
	public void multiply(float f) {
		this.pos.multiply(f);
		this.t.multiply(f);
	}
	
	public VertexOut _multiply(float f) {
		return new VertexOut(pos._multiply(f), t._multiply(f));
	}
	
	public void divide(float f) {
		this.pos.divide(f);
		this.t.divide(f);
	}
	
	public VertexOut _divide(float f) {
		return new VertexOut(pos._divide(f), t._divide(f));
	}
	
	public VertexOut interpolateTo(VertexOut dest, float alpha) {
		return new VertexOut(pos.interpolateTo(dest.pos, alpha), t.interpolateTo(dest.t, alpha));
	}

}
