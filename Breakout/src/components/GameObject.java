package components;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import threeDimensions.Texture;
import threeDimensions.Vec2;

public abstract class GameObject {
	
	protected int id;
	protected Vec2 position, size, velocity, acceleration;
	protected float speed = 0, angle = 0;
	protected Rectangle hitbox;
	protected Color c;
	
	public GameObject(float x, float y, float width, float height, float angle, float speed, Color c) {
		this.position = new Vec2(x, y);
		this.size = new Vec2(width, height);
		this.angle = angle;
		this.speed = speed;
		this.velocity = new Vec2(speed * (float) Math.cos(angle), -speed * (float) Math.sin(angle));
		this.hitbox = new Rectangle((int) x, (int) y, (int) width, (int) height);
		this.c = c;
	}
	
	public GameObject(float x, float y, float width, float height, Color c) {
		this.position = new Vec2(x, y);
		this.size = new Vec2(width, height);
		this.velocity = new Vec2(0, 0);
		this.acceleration = new Vec2(0, 0);
		this.hitbox = new Rectangle((int) x, (int) y, (int) width, (int) height);
		this.c = c;
	}
	
	public abstract void init();
	
	public abstract void input();
	
	public void setVelocity(Vec2 velocity) {
		this.velocity = velocity;
		this.angle = (float) Math.atan2(velocity.y, velocity.x);
		this.speed = this.velocity.mag();
	}
	
	public void setVelocity(float angle,float speed) {
		this.angle = angle;
		this.speed = speed;
		this.velocity.x = speed * (float) Math.cos(angle);
		this.velocity.y = -speed * (float) Math.sin(angle);
	}
	
	public void setAngle(float angle) {
		this.angle = angle;
		this.velocity.x = this.speed * (float) Math.cos(angle);
		this.velocity.y = -this.speed * (float) Math.sin(angle);
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
		this.velocity.x = this.speed * (float) Math.cos(angle);
		this.velocity.y = -this.speed * (float) Math.sin(angle);
	}

	public void updateHitbox() {
		this.hitbox.setRect(this.position.x, this.position.y, this.size.x, this.size.y);
	}
	
	public void update(double deltaTime) {
		
		this.position.add(this.velocity._multiply((float) deltaTime));
		this.updateHitbox();
		//this.hitbox.setLocation((int) this.position.x, (int) this.position.y); 
	}
	
	public abstract void render(Graphics2D g2d);

	public float getWidth() {
		return this.size.x;
	}

	public void setWidth(float width) {
		this.size.x = width;
		this.updateHitbox();
	}
	
	public float getHeight() {
		return this.size.y;
	}

	public void setHeight(float height) {
		this.size.x = height;
		this.updateHitbox();
	}
	
	public float getX() {
		return this.position.x;
	}

	public void setX(float x) {
		this.position.x = x;
		this.updateHitbox();
	}
	
	public float getY(float y) {
		return this.position.y;
	}

	public void setY(float y) {
		this.position.y = y;
		this.updateHitbox();
	}
}
