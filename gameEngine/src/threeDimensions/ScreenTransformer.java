package threeDimensions;

import gameEngine.GameEngine;

public class ScreenTransformer {
	private float xFactor, yFactor;
	
	public ScreenTransformer() {
		this.xFactor = (float) GameEngine.displayWidth / 2.0f;
		this.yFactor = (float) GameEngine.displayHeight / 2.0f;
	}
	
	public void transform(VertexOut v) {
		final float wInv = 1.0f/v.pos.w;
		v.multiply(wInv);
		v.pos.x = (v.pos.x + 1.0f) * xFactor;
		v.pos.y = (-v.pos.y + 1.0f) * yFactor;
		
		v.pos.w = wInv;
	}
	
	public VertexOut getTransformed(VertexOut v) {
		VertexOut r = new VertexOut(v);
		this.transform(r);
		return r;
	}
	
}
