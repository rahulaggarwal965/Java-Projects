package components;

import java.awt.event.KeyEvent;

import gameEngine.GameEngine;
import threeDimensions.IndexedLineList;
import threeDimensions.IndexedTriangleList;
import threeDimensions.Vec3;
import threeDimensions.Vertex;

public class Paddle3D {
	private Vec3 position;
	private Vec3 dimensions;
	private Vec3 velocity;
	private boolean side;
	
	public Paddle3D(float x, float y, float z, float width, float height, float depth, boolean side) {
		this.position = new Vec3(x, y, z);
		this.dimensions = new Vec3(width, height, depth);
		this.side = side;
	}
	
	public Paddle3D(Vec3 position, Vec3 dimensions, boolean side) {
		this.position = position;
		this.dimensions = dimensions;
		this.side = side;
	}
	
	public void input() {
		this.velocity.y = 0;
		this.velocity.z = 0;
		if(!this.side) {
			if(GameEngine.keyboard.keysPressed[KeyEvent.VK_A]) {
				this.velocity.z = -.01f;
			}
			if(GameEngine.keyboard.keysPressed[KeyEvent.VK_D]) {
				this.velocity.z = .01f;
			}
			if(GameEngine.keyboard.keysPressed[KeyEvent.VK_W]) {
				this.velocity.y = -.01f;
			}
			if(GameEngine.keyboard.keysPressed[KeyEvent.VK_S]) {
				this.velocity.y = .01f;
			}
		} else {
			if(GameEngine.keyboard.keysPressed[KeyEvent.VK_LEFT]) {
				this.velocity.z = -.01f;
			}
			if(GameEngine.keyboard.keysPressed[KeyEvent.VK_RIGHT]) {
				this.velocity.z = .01f;
			}
			if(GameEngine.keyboard.keysPressed[KeyEvent.VK_UP]) {
				this.velocity.y = -.01f;
			}
			if(GameEngine.keyboard.keysPressed[KeyEvent.VK_DOWN]) {
				this.velocity.y = .01f;
			}
		}
	}
	
	public IndexedLineList<Vertex> getLines() {
		return RectangularPrism.getLines(this.dimensions.x, this.dimensions.y, this.dimensions.z);
	}
	
	public IndexedTriangleList<Vertex> getTriangles() {
		return RectangularPrism.getTriangles(this.dimensions.x, this.dimensions.y, this.dimensions.z);
	}
	
	public void update() {
		this.position._add(this.velocity);
		if(this.position.y - this.dimensions.y/2 < -1.0f || this.position.y + this.dimensions.y/2  > 1.0f) {
			this.position.y -= velocity.y;
		}
	}
	
}
