package components;

import java.awt.Color;
import java.awt.Graphics2D;

import breakout.BreakoutSound;
import gameEngine.GameEngine;
import threeDimensions.Vec2;

public class Ball extends GameObject {
	
	public static final int initialSpeed = 430;
	private Vec2 previousPosition;
	private boolean alive = true;

	public Ball(float x, float y, float width, float height, Color col) {
		super(x, y, width, height, col);
		this.id = 1;
		this.previousPosition = new Vec2(0, 0);
	}
	
	@Override
	public void init() {
		this.position.x = GameEngine.displayWidth/2;
		this.position.y = 650;
		this.setVelocity((float) (Math.random() * Math.PI/2 + Math.PI/4), initialSpeed);
	}

	@Override
	public void input() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(double deltaTime) {
		if(this.alive) {
			this.previousPosition.copy(this.position);
			super.update(deltaTime);
			if(this.position.y <= 0) {
				this.position.copy(this.previousPosition);
				this.updateHitbox();
				this.setAngle(-this.angle);
				BreakoutSound.HIT.play(false);
			}
			if(this.position.x <= 0 || this.position.x + this.size.x >= GameEngine.displayWidth) {
				this.position.copy(this.previousPosition);
				this.updateHitbox();
				this.setAngle((float) Math.PI - this.angle);
				BreakoutSound.HIT.play(false);
			}
		}
	}

	@Override
	public void render(Graphics2D g2d) {
		// TODO Auto-generated method stub
		if(this.alive) {
			g2d.setColor(this.c);
			g2d.fillOval((int) this.position.x, (int) this.position.y, (int) this.size.x, (int) this.size.y);
		}
	}
	
	public boolean isOffBottomEdge() {
		return (this.position.y + this.size.y >= GameEngine.displayHeight);
	}
	
	public Vec2 getPreviousPosition() {
		return this.previousPosition;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

}
