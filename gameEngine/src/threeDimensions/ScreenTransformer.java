package threeDimensions;

import gameEngine.GameEngine;

public class ScreenTransformer {
	private float xFactor, yFactor;
	
	public ScreenTransformer() {
		this.xFactor = (float) GameEngine.displayWidth / 2.0f;
		this.yFactor = (float) GameEngine.displayHeight / 2.0f;
	}
	
	public void transform(Vertex v) {
		final float zInv = 1.0f/v.pos.z;
		v.multiply(zInv);
		v.pos.x = (v.pos.x + 1.0f) * xFactor;
		v.pos.y = (-v.pos.y + 1.0f) * yFactor;
		
		v.pos.z = zInv;
	}
	
	public Vertex getTransformed(Vertex v) {
		Vertex r = new Vertex(v);
		this.transform(v);
		return r;
	}
	
}
