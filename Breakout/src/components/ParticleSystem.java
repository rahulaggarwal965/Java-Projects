package components;

import java.awt.Color;
import java.awt.Graphics2D;

import threeDimensions.PackedColor;
import threeDimensions.Vec2;
import threeDimensions.Vec4;

public class ParticleSystem {
	
	class Particle extends GameObject {
		
		public static final int RECTANGLE_MODE = 0;
		public static final int CIRCLE_MODE = 1;
		
		private double life;
		private int drawMode = 0;
		private Vec4 currentColor, deltaColor;

		public Particle() {
			super(0, 0, 0, 0, Color.black);
			this.life = 0;
		}
		
		@Override
		public void init() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void input() {
			// TODO Auto-generated method stub
			
		}
		
		public void set(float x, float y, float width, float height, float angle, float speed, float ax, float ay, double life, Vec4 startColor, Vec4 endColor, int drawMode) {
			this.position.x = x;
			this.position.y = y;
			this.size.x = width;
			this.size.y = height;
			this.hitbox.setRect(x, y, width, height);
			this.angle = angle;
			this.speed = speed;
			this.velocity.x = this.speed * (float) Math.cos(angle);
			this.velocity.y = this.speed * (float) Math.sin(angle);
			this.acceleration.x = ax;
			this.acceleration.y = ay;
			this.life = life;
			this.currentColor = startColor;
			this.deltaColor = endColor._subtract(startColor)._divide((float) life);
			this.drawMode = drawMode;
		}
		
		@Override
		public void update(double deltaTime) {
			if(life > 0) {
				this.currentColor.add(deltaColor._multiply((float) deltaTime));
				life -= deltaTime;
				this.setVelocity(new Vec2(this.velocity.x + this.acceleration.x * (float) deltaTime, this.velocity.y + this.acceleration.y * (float) deltaTime));
				super.update(deltaTime);
			}
		}

		@Override
		public void render(Graphics2D g2d) {
			// TODO Auto-generated method stub
			if(life > 0) {
				g2d.setColor(new Color(PackedColor.makeRGBA(currentColor), true));
				if(this.drawMode == CIRCLE_MODE) {
					g2d.fillOval((int) this.position.x, (int) this.position.y, (int) this.size.x, (int) this.size.y);
				} else {
					g2d.fillRect((int) this.position.x, (int) this.position.y, (int) this.size.x, (int) this.size.y);
				}
				
			}
		}
	}

	private Particle[] particlePool;
	private int particleCount;
	
	public ParticleSystem(int num) {
		this.particlePool = new Particle[num];
		for (int i = 0; i < num; i++) {
			this.particlePool[i] = new Particle();
		}
		this.particleCount = 0;
	}
	
	public void createExplosion(Vec2 position, Vec2 size, Color col) {
		Color trans = new Color(col.getRed(), col.getGreen(), col.getBlue(), 0);
		Vec2 half_size = size._multiply(0.5f);
		this.addParticle(position, size, 0, 0, 0, 0, 0.3f, col, trans, Particle.RECTANGLE_MODE);
		for (int i = 0; i < 10; i++) {
			float s = (float) (25 + Math.random() * 5);
			Vec2 sz = new Vec2(s, s); 
			this.addParticle(position._add(half_size), sz, (float) (Math.random() * 2 * Math.PI), 50 + (float) (Math.random() * 50), 0, 0, 1.0f, col, trans, Particle.RECTANGLE_MODE);
		}
	}
	
	//We want to actually update the particle here, not allocate a new one, doing this for test
	public void addParticle(Vec2 position, Vec2 size, float angle, float speed, float ax, float ay, float life, Color startColor, Color endColor, int drawMode) {
		if(this.particleCount != this.particlePool.length) {
			this.particlePool[particleCount].set(position.x, position.y, size.x, size.y, angle, speed * (float) Math.random(), ax, ay, life, PackedColor.toVector(startColor), PackedColor.toVector(endColor), drawMode);
			particleCount++;
		}
	}
	
	public void update(double deltaTime) {
		for (int i = 0; i < this.particleCount; i++) {
			if(this.particlePool[i].life <= 0) {
				Particle temp = this.particlePool[i];
				this.particlePool[i] = this.particlePool[this.particleCount - 1];
				this.particlePool[this.particleCount - 1] = temp;
				this.particleCount--;
			} else {
				this.particlePool[i].update(deltaTime);
			}
		}
	}
	
	public void render(Graphics2D g2d) {
		for (int i = 0; i < this.particleCount; i++) {
			this.particlePool[i].render(g2d);
		}
	}
}
