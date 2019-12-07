package components;

import java.util.Random;

import threeDimensions.Vec3;

public class Ball3D {
	private float radius, speed;
	private Vec3 position;
	private Vec3 velocity;
	
	public void init() {
		Random rand = new Random();
		this.position = new Vec3(0.0f, 0.0f, 2.0f);
		this.velocity = new Vec3(0, 0.01f * (rand.nextInt(2) * 2 - 1), 0.01f * (rand.nextInt(2) * 2 - 1));
	}
	
	public void update() {
		this.position._add(this.velocity);
		if(position.y - this.radius <= -1.0f || position.y + this.radius >= 1.0f) {
			this.velocity.y *= -1;
		}
	}
}
