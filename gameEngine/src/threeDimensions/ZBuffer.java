package threeDimensions;

import gameEngine.GameEngine;

public class ZBuffer {
	private float[] depth;
	
	public ZBuffer() {
		this.depth = new float[GameEngine.displayWidth * GameEngine.displayHeight];
	}
	
	public void clear() {
		for (int i = 0; i < this.depth.length; i++) {
			this.depth[i] = Float.MAX_VALUE;
		}
	}
	
	public float getPixel(int x, int y) {
		assert(x >= 0 && x < GameEngine.displayWidth);
		assert(y >= 0 && y < GameEngine.displayHeight);
		return this.depth[y * GameEngine.displayWidth + x];
	}
	
	private void setPixel(int x, int y, float depth) {
		this.depth[y * GameEngine.displayWidth + x] = depth;
	}
	
	public boolean testAndSet(int x, int y, float z) {
		float depth = getPixel(x, y);
		if (z < depth) {
			this.setPixel(x, y, z);
			return true;
		}
		return false;
	}
}
