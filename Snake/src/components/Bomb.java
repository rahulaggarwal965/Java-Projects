package components;

import java.awt.Graphics2D;
import java.awt.Point;

import math.Maths;
import threeDimensions.Graphics3D;
import threeDimensions.PackedColor;
import threeDimensions.ParticleSystem;

public class Bomb implements GameObject {
	
	private float life;
	private static final int color = PackedColor.Green;
	private Point position;
	
	private float moveCooldown = 0.0f;
	private float moveInterval = 0.6f;

	public Bomb(Point position) {
		this.position = position;
		this.life = Maths.random(5.0f) + 5.0f;
	}
	
	public Bomb(int x, int y) {
		this(new Point(x, y));
	}
	
	public float getLife() {
		return this.life;
	}
	
	public Point getPosition() {
		return this.position;
	}

	@Override
	public void init() {
	}

	@Override
	public void input() {
	}

	@Override
	public void update(float deltaTime) {
		this.life -= deltaTime;
		this.moveCooldown -= deltaTime;
		if(this.moveCooldown <= 0) {
			this.position.x += (int) Maths.randomBilateral(2.0f) * Snek.SIZE;
			this.position.y += (int) Maths.randomBilateral(2.0f) * Snek.SIZE;
			this.moveCooldown = moveInterval;
		}
 	}

	@Override
	public void render(Graphics2D g) {
	}

	@Override
	public void render(Graphics3D g) {
		g.fillRectangle(this.position.x - Snek.HALF_SIZE + 1, this.position.y - Snek.HALF_SIZE + 1, Snek.SIZE - 2, Snek.SIZE - 2, Bomb.color);
	}

}
