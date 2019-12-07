package components;

import java.awt.Color;
import java.awt.Graphics2D;

import threeDimensions.PackedColor;
import threeDimensions.Vec2;
import threeDimensions.Vec3;

public class ParticleSystem {
	
	class Particle extends GameObject {
		
		private double life;
		private float dvx, dvy = 600;
		private Vec3 currentColor, deltaColor;

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
		
		public void set(float x, float y, float width, float height, float angle, float speed, double life, Vec3 startColor, Vec3 endColor) {
			this.position.x = x;
			this.position.y = y;
			this.size.x = width;
			this.size.y = height;
			this.hitbox.setRect(x, y, width, height);
			this.angle = angle;
			this.speed = speed;
			this.velocity.x = this.speed * (float) Math.cos(angle);
			this.velocity.y = this.speed * (float) Math.sin(angle);
			this.life = life;
			this.currentColor = startColor;
			this.deltaColor = endColor._subtract(startColor)._divide((float) life);
		}
		
		@Override
		public void update(double deltaTime) {
			if(life > 0) {
				this.currentColor.add(deltaColor._multiply((float) deltaTime));
				life -= deltaTime;
				this.setVelocity(new Vec2(this.velocity.x, this.velocity.y +  dvy * (float) deltaTime));
				super.update(deltaTime);
			}
		}

		@Override
		public void render(Graphics2D g2d) {
			// TODO Auto-generated method stub
			if(life > 0) {
				g2d.setColor(new Color(PackedColor.makeRGB(currentColor)));
				g2d.fillRect((int) this.position.x, (int) this.position.y, (int) this.size.x, (int) this.size.y);
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
	
	public void createExplosion(float x, float y, Color col) {
		for (int i = 0; i < 50; i++) {
			this.addParticle(x, y, 10, 10, (float) (Math.random() * 2 * Math.PI), 300, 1.5f, col, Color.black);
		}
	}
	
	//We want to actually update the particle here, not allocate a new one, doing this for test
	public void addParticle(float x, float y, float width, float height, float angle, float speed, float life, Color startColor, Color endColor) {
		if(this.particleCount != this.particlePool.length) {
			this.particlePool[particleCount].set(x, y, width, height, angle, speed * (float) Math.random(), life, PackedColor.toVector(startColor), PackedColor.toVector(endColor));
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
