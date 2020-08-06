package threeDimensions;

import java.awt.Graphics2D;

import math.Vec3;

public class ParticleSystem {
	
	private Particle[] particlePool;
	private int particleCount;

	public ParticleSystem(int num) {
		this.particlePool = new Particle[num];
		for (int i = 0; i < num; i++) {
			this.particlePool[i] = new Particle();
		}
		this.particleCount = 0;
	}
	
	public void addParticle(Mesh m, Vec3 position, Vec3 rotation, float scale, Vec3 velocity, float life) {
		if(this.particleCount != this.particlePool.length) {
			this.particlePool[particleCount].set(m, position, rotation, scale, velocity, life);
			particleCount++;
		}
	}
	
	public void clear() {
		for (int i = 0; i < this.particleCount; i++) {
			this.particlePool[i].setLife(0);
		}
		this.particleCount = 0;
	}
	
	public void update(float deltaTime) {
		for (int i = 0; i < this.particleCount; i++) {
			if(this.particlePool[i].getLife() <= 0) {
				Particle temp = this.particlePool[i];
				this.particlePool[i] = this.particlePool[this.particleCount - 1];
				this.particlePool[this.particleCount - 1] = temp;
				this.particleCount--;
			} else {
				this.particlePool[i].update(deltaTime);
			}
		}
	}
	
	public void render(Graphics3D g, Pipeline p){
		for (int i = 0; i < this.particleCount; i++) {
			this.particlePool[i].render(g, p);
		}
	}
	
}
