package threeDimensions;

import gameEngine.GameEngine;

public class ScreenTransformer {
	private float xFactor, yFactor;
	
	public ScreenTransformer() {
		this.xFactor = (float) GameEngine.displayWidth / 2.0f;
		this.yFactor = (float) GameEngine.displayHeight / 2.0f;
	}
	
	public void transform(Vertex v) {
		final float wInv = 1.0f/v.position.w;
		v.multiply(wInv);
		
		v.position.x = (v.position.x + 1.0f) * xFactor;
		v.position.y = (-v.position.y + 1.0f) * yFactor;
		
		v.position.w = wInv;
	}
	
	public Vertex getTransformed(Vertex v) {
		Vertex r = new Vertex(v);
		this.transform(r);
		return r;
	}
	
}
